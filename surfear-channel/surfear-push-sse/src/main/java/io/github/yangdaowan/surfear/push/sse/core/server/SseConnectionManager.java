package io.github.yangdaowan.surfear.push.sse.core.server;

import io.github.yangdaowan.surfear.core.concurrent.ExecutorFactory;
import io.github.yangdaowan.surfear.push.sse.core.config.SseServerConfig;
import io.github.yangdaowan.surfear.push.sse.framework.SseException;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class SseConnectionManager {
    private static final SseConnectionManager INSTANCE = new SseConnectionManager();

    // 客户端连接管理，外层Map的key是clientId
    private final ConcurrentMap<String, ConcurrentMap<ChannelHandlerContext, Connection>> clientConnections = new ConcurrentHashMap<>();
    // 分组管理
    private final ConcurrentMap<String, Set<Connection>> groupConnections = new ConcurrentHashMap<>();
    // 统计数据
    private final AtomicInteger totalConnections = new AtomicInteger(0);
    private final AtomicInteger activeConnections = new AtomicInteger(0);

    private final SseServerConfig config;

    private SseConnectionManager() {
        this.config = SseServerConfig.builder().build(); // 使用默认配置
    }

    public static SseConnectionManager getInstance() {
        return INSTANCE;
    }

    public synchronized boolean registerConnection(String clientId, String groupId, ChannelHandlerContext ctx) {
        clientId = clientId != null ? clientId : generateClientId();

        // 检查连接数限制
        if (config.getSecurityConfig().isRequireClientId()) {
            int currentConnections = getClientConnectionCount(clientId);
            if (currentConnections >= config.getSecurityConfig().getMaxConnectionsPerClient()) {
                log.warn("客户端 {} 超过最大连接限制: {}",
                    clientId, config.getSecurityConfig().getMaxConnectionsPerClient());
                throw new SseException("客户端连接数超过限制");
            }
        }

        // 注册客户端连接
        Connection conn = new Connection(clientId, groupId, ctx);
        clientConnections.computeIfAbsent(clientId, k -> new ConcurrentHashMap<>())
                        .put(ctx, conn);

        // 注册分组
        if (groupId != null) {
            groupConnections.computeIfAbsent(groupId, k -> ConcurrentHashMap.newKeySet())
                          .add(conn);
            log.debug("客户端 {} 已添加到分组 {}", clientId, groupId);
        }

        totalConnections.incrementAndGet();
        int active = activeConnections.incrementAndGet();
        log.info("客户端 {} 已连接, 分组: {}, 当前活跃连接数: {}",
                clientId, groupId, active);

        return true;
    }

    public synchronized void removeConnection(ChannelHandlerContext ctx) {
        // 找到对应的连接
        Connection conn = findConnectionByContext(ctx);
        if (conn != null) {
            // 从客户端连接中移除
            ConcurrentMap<ChannelHandlerContext, Connection> clientConns =
                clientConnections.get(conn.getClientId());
            if (clientConns != null) {
                clientConns.remove(ctx);
                if (clientConns.isEmpty()) {
                    clientConnections.remove(conn.getClientId());
                }
            }

            // 从分组中移除
            if (conn.getGroupId() != null) {
                Set<Connection> groupConns = groupConnections.get(conn.getGroupId());
                if (groupConns != null) {
                    groupConns.remove(conn);
                    if (groupConns.isEmpty()) {
                        groupConnections.remove(conn.getGroupId());
                    }
                }
            }

            int active = activeConnections.decrementAndGet();
            log.info("客户端 {} 已断开连接, 分组: {}, 当前活跃连接数: {}",
                    conn.getClientId(), conn.getGroupId(), active);
        }
    }

    private Connection findConnectionByContext(ChannelHandlerContext ctx) {
        return clientConnections.values().stream()
            .map(conns -> conns.get(ctx))
            .filter(conn -> conn != null)
            .findFirst()
            .orElse(null);
    }

    public int getClientConnectionCount(String clientId) {
        ConcurrentMap<ChannelHandlerContext, Connection> conns = clientConnections.get(clientId);
        return conns != null ? conns.size() : 0;
    }

    public CompletableFuture<Integer> broadcast(String event, String data) {
        return CompletableFuture.supplyAsync(() -> {
            int count = 0;
            long eventId = System.currentTimeMillis();

            for (Connection conn : clientConnections.values().stream()
                                                    .flatMap(conns -> conns.values().stream())
                                                    .collect(Collectors.toSet())) {
                if (sendEvent(conn, event, data, eventId)) {
                    count++;
                }
            }
            return count;
        }, ExecutorFactory.getDefaultExecutor());
    }

    public CompletableFuture<Integer> sendToGroup(String groupId, String event, String data) {
        return CompletableFuture.supplyAsync(() -> {
            Set<Connection> groupMembers = groupConnections.get(groupId);
            if (groupMembers == null || groupMembers.isEmpty()) {
                log.warn("未找到分组 {} 的连接", groupId);
                return 0;
            }

            long eventId = System.currentTimeMillis();
            int successCount = 0;

            // 创建副本避免并发修改
            Set<Connection> connections = new HashSet<>(groupMembers);
            for (Connection conn : connections) {
                try {
                    if (conn.getContext().channel().isActive() && sendEvent(conn, event, data, eventId)) {
                        successCount++;
                        log.debug("成功发送消息到分组 {} 中的客户端 {}",
                            groupId, conn.getClientId());
                    }
                } catch (Exception e) {
                    log.error("发送消息到分组 {} 中的客户端 {} 失败",
                        groupId, conn.getClientId(), e);
                    throw new SseException("发送分组消息失败", e);
                }
            }

            log.info("已发送分组消息到 {} 中的 {}/{} 个客户端",
                groupId, successCount, connections.size());
            return successCount;
        }, ExecutorFactory.getDefaultExecutor());
    }

    public CompletableFuture<Boolean> sendToClient(String clientId, String event, String data) {
        return CompletableFuture.supplyAsync(() -> {
            ConcurrentMap<ChannelHandlerContext, Connection> clientConns = clientConnections.get(clientId);
            if (clientConns == null || clientConns.isEmpty()) {
                return false;
            }

            // 发送到该客户端的所有连接
            long eventId = System.currentTimeMillis();
            boolean anySuccess = false;

            for (Connection conn : clientConns.values()) {
                if (sendEvent(conn, event, data, eventId)) {
                    anySuccess = true;
                }
            }

            return anySuccess;
        }, ExecutorFactory.getDefaultExecutor());
    }

    private boolean sendEvent(Connection conn, String event, String data, long eventId) {
        if (!conn.getContext().channel().isActive()) {
            log.debug("客户端 {} 的通道不活跃", conn.getClientId());
            return false;
        }

        try {
            // 确保所有行结束都是 \r\n，符合 HTTP 规范
            StringBuilder message = new StringBuilder()
                .append("id: ").append(eventId).append("\r\n")
                .append("event: ").append(event).append("\r\n")
                .append("data: ").append(data).append("\r\n\r\n");

            // 使用 Netty 的 ByteBuf 发送消息
            conn.getContext().writeAndFlush(
                Unpooled.copiedBuffer(message.toString(), StandardCharsets.UTF_8)
            );

            log.debug("已发送事件到客户端 {}: {}", conn.getClientId(), message);
            return true;
        } catch (Exception e) {
            log.error("发送事件到客户端 {} 失败", conn.getClientId(), e);
            throw new SseException("发送事件失败", e);
        }
    }

    public Map<String, Integer> getStats() {
        Map<String, Integer> stats = new ConcurrentHashMap<>();
        stats.put("totalConnections", totalConnections.get());
        stats.put("activeConnections", activeConnections.get());
        stats.put("uniqueClients", clientConnections.size());
        stats.put("groupCount", groupConnections.size());
        return stats;
    }

    private String generateClientId() {
        return "client-" + totalConnections.get();
    }

    @lombok.Value
    private static class Connection {
        String clientId;
        String groupId;
        ChannelHandlerContext context;
    }
}
