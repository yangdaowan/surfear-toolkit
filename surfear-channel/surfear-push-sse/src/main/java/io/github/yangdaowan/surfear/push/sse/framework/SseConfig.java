package io.github.yangdaowan.surfear.push.sse.framework;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author ycf
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SseConfig {

    /**
     * 端口
     */
    private Integer port = 21100;
    /**
     * URI
     */
    private String uri = "/push/sse";
    /**
     * 最大内容长度 字节 64kb
     */
    private Integer maxContentLength = 64 * 1024;
    /**
     * 读取空闲超时 秒，维持SSE长连接
     * 每15秒若服务端未发送任何数据，触发 WRITER_IDLE
     * SseHeartbeatHandler 发送SSE心跳包（:heartbeat\n\n）
     * 建议：15-30秒	保持心跳频率高于Nginx默认60秒超时
     */
    private Integer readIdleTimeout = 30;
    /**
     * 写入空闲超时 秒，检测死连接
     * 30秒内未收到客户端任何请求（如浏览器关闭）
     * SseHeartbeatHandler 主动关闭连接释放资源
     * 建议：2-3倍心跳间隔	避免网络延迟误判
     */
    private Integer writeIdleTimeout = 15;
    /**
     * 是否开启断线重发
     */
    private Boolean reissueEnable = true;
    /**
     * 事件存活时间(毫秒) 1小时
     */
    private Long eventTTL = TimeUnit.HOURS.toSeconds(1) * 1000;
    /**
     * 预检请求缓存时间（秒），直接影响SSE重连性能
     */
    private Long maxAge = TimeUnit.HOURS.toSeconds(1);

}
