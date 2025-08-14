package io.github.yangdaowan.surfear.push.sse.core.config;

import io.github.yangdaowan.surfear.push.sse.core.server.SseAuthHandler;
import lombok.Builder;
import lombok.Data;

/**
 * SSE服务器配置
 */
@Data
@Builder
public class SseServerConfig {
    /**
     * 服务器端口
     */
    @Builder.Default
    private int port = 21100;

    /**
     * SSE路径
     */
    @Builder.Default
    private String path = "/push/sse";

    /**
     * 连接空闲超时时间（秒）
     */
    @Builder.Default
    private int idleTimeout = 60;

    /**
     * 心跳间隔（秒）
     */
    @Builder.Default
    private int heartbeatInterval = 30;

    /**
     * 是否允许跨域
     */
    @Builder.Default
    private boolean allowCors = true;

    /**
     * 最大内容长度
     */
    @Builder.Default
    private int maxContentLength = 65536;

    /**
     * Boss线程数
     */
    @Builder.Default
    private int bossThreads = 1;

    /**
     * Worker线程数
     */
    @Builder.Default
    private int workerThreads = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 每个客户端消息队列大小
     */
    @Builder.Default
    private int clientQueueSize = 1000;

    /**
     * 每个客户端消息队列大小
     */
    @Builder.Default
    private SseAuthHandler sseAuthHandler = null;

    /**
     * TCP参数配置
     */
    @Data
    @Builder
    public static class TcpConfig {
        @Builder.Default
        private boolean tcpNoDelay = true;

        @Builder.Default
        private boolean keepAlive = true;

        @Builder.Default
        private int backlog = 128;

        @Builder.Default
        private int soTimeout = 30000;

        @Builder.Default
        private int sendBufferSize = 64 * 1024;

        @Builder.Default
        private int receiveBufferSize = 64 * 1024;
    }

    /**
     * TCP配置
     */
    @Builder.Default
    private TcpConfig tcpConfig = TcpConfig.builder().build();

    /**
     * 安全配置
     */
    @Data
    @Builder
    public static class SecurityConfig {
        @Builder.Default
        private boolean requireClientId = false;

        @Builder.Default
        private boolean requireAuthentication = false;

        @Builder.Default
        private String allowedOrigins = "*";

        @Builder.Default
        private int maxConnectionsPerClient = 1;
    }

    /**
     * 安全配置
     */
    @Builder.Default
    private SecurityConfig securityConfig = SecurityConfig.builder().build();
}
