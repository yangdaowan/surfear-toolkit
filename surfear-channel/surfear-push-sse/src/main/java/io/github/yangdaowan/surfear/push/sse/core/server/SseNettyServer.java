package io.github.yangdaowan.surfear.push.sse.core.server;

import io.github.yangdaowan.surfear.core.concurrent.ExecutorFactory;
import io.github.yangdaowan.surfear.push.sse.core.config.SseServerConfig;
import io.github.yangdaowan.surfear.push.sse.framework.SseException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * SSE Netty服务器
 * <p>
 * 基于Netty实现的Server-Sent Events服务器，
 * 支持高并发的实时消息推送。
 * </p>
 * 
 * @author ycf
 */
@Slf4j
public class SseNettyServer {
    private static volatile SseNettyServer INSTANCE;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private Channel serverChannel;
    private final SseServerConfig config;

    private SseNettyServer(SseServerConfig config) {
        // 创建配置的深拷贝，防止外部修改
        this.config = config;
        this.bossGroup = new NioEventLoopGroup(config.getBossThreads());
        this.workerGroup = new NioEventLoopGroup(config.getWorkerThreads());
    }

    /**
     * 创建服务器实例
     * @param config 服务器配置
     * @return 服务器实例
     */
    public static synchronized SseNettyServer createInstance(SseServerConfig config) {
        if (INSTANCE != null) {
            log.warn("[{}] - 尝试使用新配置创建服务器实例，但实例已存在", "SSE");
            return INSTANCE;
        }
        INSTANCE = new SseNettyServer(config);
        return INSTANCE;
    }

    /**
     * 获取服务器实例
     * @return 服务器实例，如果实例不存在则返回null
     */
    public static SseNettyServer getInstance() {
        return INSTANCE;
    }

    /**
     * 销毁服务器实例
     */
    public static void destroyInstance() {
        if (INSTANCE != null) {
            synchronized (SseNettyServer.class) {
                if (INSTANCE != null) {
                    if (INSTANCE.isRunning()) {
                        try {
                            INSTANCE.stop().get(); // 等待服务器完全停止
                        } catch (Exception e) {
                            log.error("[{}] - 销毁服务器实例时发生错误", "SSE", e);
                        }
                    }
                    INSTANCE = null;
                    log.info("[{}] - 服务器实例已销毁", "SSE");
                }
            }
        }
    }

    public CompletableFuture<Void> start() {
        return CompletableFuture.runAsync(() -> {
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(config.getPort()))
                    // TCP配置
                    .option(ChannelOption.SO_BACKLOG, config.getTcpConfig().getBacklog())
                    .childOption(ChannelOption.TCP_NODELAY, config.getTcpConfig().isTcpNoDelay())
                    .childOption(ChannelOption.SO_KEEPALIVE, config.getTcpConfig().isKeepAlive())
                    .childOption(ChannelOption.SO_RCVBUF, config.getTcpConfig().getReceiveBufferSize())
                    .childOption(ChannelOption.SO_SNDBUF, config.getTcpConfig().getSendBufferSize())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(config.getMaxContentLength()))
                                .addLast(new IdleStateHandler(
                                    config.getIdleTimeout(),
                                    config.getHeartbeatInterval(),
                                    0
                                ))
                                .addLast(new SseServerHandler(config));
                        }
                    });

                serverChannel = bootstrap.bind().sync().channel();
                log.info("[{}] - 推送服务启动 http://localhost:{}{}",
                        "SSE", config.getPort(), config.getPath());
            } catch (Exception e) {
                log.error("[{}] - 推送服务启动失败", "SSE", e);
                throw new SseException("SSE推送服务启动失败", e);
            }
        }, ExecutorFactory.getDefaultExecutor());
    }

    public CompletableFuture<Void> stop() {
        return CompletableFuture.runAsync(() -> {
            try {
                if (serverChannel != null) {
                    serverChannel.close().sync();
                }
                CompletableFuture.allOf(
                    CompletableFuture.runAsync(bossGroup::shutdownGracefully),
                    CompletableFuture.runAsync(workerGroup::shutdownGracefully)
                ).get();
                log.info("[{}] - 推送服务已停止", "SSE");
            } catch (Exception e) {
                log.error("[{}] - 推送服务停止失败", "SSE", e);
                throw new SseException("SSE推送服务停止失败", e);
            }
        }, ExecutorFactory.getDefaultExecutor());
    }

    public boolean isRunning() {
        return serverChannel != null && serverChannel.isActive();
    }
}
