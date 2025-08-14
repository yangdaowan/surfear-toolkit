package io.github.yangdaowan.surfear.push.sse.core.server;

import io.github.yangdaowan.surfear.push.sse.core.config.SseServerConfig;
import io.github.yangdaowan.surfear.push.sse.framework.SseException;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * SSE服务器处理器
 * <p>
 * 处理SSE连接请求，管理客户端连接生命周期，
 * 包括认证、限流、CORS等功能。
 * </p>
 * 
 * @author ycf
 */
@Slf4j
public class SseServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final AttributeKey<String> CLIENT_ID_ATTR = AttributeKey.valueOf("clientId");
    private static final AttributeKey<String> GROUP_ID_ATTR = AttributeKey.valueOf("groupId");

    private final SseConnectionManager sseConnectionManager;
    private final SseServerConfig config;

    public SseServerHandler(SseServerConfig config) {
        this.config = config;
        this.sseConnectionManager = SseConnectionManager.getInstance();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        // 基本请求检查
        if (!validateRequest(ctx, request)) {
            return;
        }

        // 安全检查
        if (!securityCheck(ctx, request)) {
            return;
        }

        // 获取客户端信息
        String clientId = request.headers().get("X-Client-ID");
        String groupId = request.headers().get("X-Group-ID");

        if (config.getSecurityConfig().isRequireClientId() && clientId == null) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST, "需要提供客户端ID");
            throw new SseException("缺少必需的客户端ID");
        }

        // 尝试注册连接
        if (!sseConnectionManager.registerConnection(clientId, groupId, ctx)) {
            sendError(ctx, HttpResponseStatus.TOO_MANY_REQUESTS,
                "客户端 " + clientId + " 超过最大连接限制");
            throw new SseException("超过客户端最大连接限制");
        }

        // 设置SSE响应头
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers()
            .set(HttpHeaderNames.CONTENT_TYPE, "text/event-stream")
            .set(HttpHeaderNames.CACHE_CONTROL, "no-cache")
            .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
            .set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);

        // CORS 配置
        if (config.isAllowCors()) {
            String origin = request.headers().get(HttpHeaderNames.ORIGIN, "*");
            response.headers()
                .set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, origin)
                .set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }

        // 发送响应头
        ctx.writeAndFlush(response);

        // 发送初始化事件
        String initEvent = String.format("event: init\ndata: Connected as %s\nid: %d\n\n",
            clientId, System.currentTimeMillis());
        ctx.writeAndFlush(Unpooled.copiedBuffer(initEvent, StandardCharsets.UTF_8));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            String heartbeat = String.format("event: heartbeat\ndata: ping\nid: %d\n\n",
                System.currentTimeMillis());
            ctx.writeAndFlush(Unpooled.copiedBuffer(heartbeat, StandardCharsets.UTF_8))
                .addListener(future -> {
                    if (!future.isSuccess()) {
                        log.error("发送心跳包失败，关闭连接", future.cause());
                        ctx.close();
                    }
                });
        }
    }

    private boolean validateRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        // 路径检查
        if (!request.uri().equals(config.getPath())) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND, "无效的请求路径");
            return false;
        }

        // HTTP方法检查
        if (request.method() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, "不支持的HTTP方法");
            return false;
        }

        return true;
    }

    private boolean securityCheck(ChannelHandlerContext ctx, FullHttpRequest request) {
        // 检查客户端连接数限制
        String clientId = request.headers().get("X-Client-ID");
        if (config.getSecurityConfig().isRequireAuthentication()) {
            String auth = request.headers().get(HttpHeaderNames.AUTHORIZATION);
            if (auth == null || !validateAuthentication(ctx, request, auth)) {
                sendError(ctx, HttpResponseStatus.UNAUTHORIZED, "未授权的访问");
                return false;
            }
        }

        if (clientId != null) {
            int currentConnections = sseConnectionManager.getClientConnectionCount(clientId);
            if (currentConnections >= config.getSecurityConfig().getMaxConnectionsPerClient()) {
                sendError(ctx, HttpResponseStatus.TOO_MANY_REQUESTS,
                    "超过每个客户端的最大连接数限制");
                return false;
            }
        }

        return true;
    }

    private boolean validateAuthentication(ChannelHandlerContext ctx, FullHttpRequest request, String auth) {
        // 实现认证逻辑
        if(config.getSseAuthHandler() != null) {
            return config.getSseAuthHandler().authenticate(ctx, request, auth);
        }
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        sseConnectionManager.removeConnection(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("通道异常", cause);
        ctx.close();
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, String message) {
        FullHttpResponse response = new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            status,
            Unpooled.copiedBuffer(message, StandardCharsets.UTF_8)
        );
        response.headers()
            .set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8")
            .set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        sendError(ctx, status, status.toString());
    }
}
