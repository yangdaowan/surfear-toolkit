## SSE推送服务-使用文档

----------

### 功能介绍

SSE (Server-Sent Events) 是一种基于 HTTP 的服务器推送技术，允许服务器向客户端推送实时数据。本模块提供了一个基于 Netty 实现的高性能 SSE 服务器，支持多种推送场景。

### 功能特点

| 功能类型          | 是否支持 | 说明                          |
|---------------|------|------------------------------|
| 客户端认证         | 是    | 支持自定义认证逻辑                   |
| 多客户端支持        | 是    | 支持每个客户端ID多个连接              |
| 分组广播          | 是    | 支持向指定分组的所有客户端推送消息          |
| 定向推送          | 是    | 支持向指定客户端推送消息               |
| 全局广播          | 是    | 支持向所有连接的客户端推送消息            |
| 心跳保活          | 是    | 自动心跳维持连接                    |
| 断线重连          | 是    | 支持客户端断线重连和消息重放             |
| 连接数限制         | 是    | 支持限制每个客户端ID的最大连接数          |
| CORS跨域支持      | 是    | 支持配置跨域访问策略                 |
| 消息确认机制        | 是    | 支持消息ID和确认机制                |

### 特色功能

1. **高性能设计**
   - 基于 Netty 实现的异步非阻塞架构
   - 支持大量并发连接
   - 内存占用优化

2. **灵活的分组管理**
   - 支持动态创建和管理分组
   - 支持客户端多分组归属
   - 分组消息广播

3. **完善的监控指标**
   - 连接数统计
   - 消息投递状态
   - 客户端活跃度监控

### 快速开始

1. **添加依赖**

```xml
<dependency>
    <groupId>io.github.yangdaowan</groupId>
    <artifactId>surfear-push-sse</artifactId>
    <version>${version}</version>
</dependency>
```

2. **配置服务器**

```java
SseServerConfig config = SseServerConfig.builder()
    .port(21100)
    .path("/push/sse")
    .idleTimeout(60)
    .heartbeatInterval(30)
    .allowCors(true)
    .sseAuthHandler(new SseAuthHandler() {
        @Override
        public boolean authenticate(ChannelHandlerContext ctx, FullHttpRequest request, String auth) {
           // 实现认证逻辑
           return true;
        }
     })
    .securityConfig(SseServerConfig.SecurityConfig.builder()
        .requireClientId(true)
        .maxConnectionsPerClient(2)
        .build())
    .build();

SseNettyServer server = new SseNettyServer(config);
server.start();
```

3. **发送消息示例**

```java
// 向指定客户端发送消息
SsePushModel model = new SsePushModel();
model.setTo(targetClient);
model.setEvent("client-event");
model.setContent(message);
model.setType(PushType.TARGET);

MessageSender.send("push:sse", model);

// 向分组广播消息
SsePushModel model = new SsePushModel();
model.setTo(groupId);
model.setEvent("group-event");
model.setContent(message);
model.setType(PushType.GROUP);

MessageSender.send("push:sse", model);

// 全局广播消息
SsePushModel model = new SsePushModel();
model.setTo("所有人");
model.setEvent("all-event");
model.setContent(message);
model.setType(PushType.BROADCAST);
```

### 客户端示例

**JavaScript客户端**

```javascript
const clientId = "client-001";
const source = new EventSource(`http://localhost:21100/push/sse?clientId=${clientId}`);

source.addEventListener('message', (e) => {
    console.log('收到消息:', e.data);
});

source.addEventListener('error', (e) => {
    console.error('连接错误:', e);
});

// 自定义事件监听
source.addEventListener('custom-event', (e) => {
    console.log('收到自定义事件:', e.data);
});
```

### 配置项参考
```java
io.github.yangdaowan.surfear.mail.smtp.framework.SmtpMailConfig
```


