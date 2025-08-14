package io.github.yangdaowan.surfear.sms.baidu.core.callback;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import io.github.yangdaowan.surfear.core.openapi.smscallback.AbstractSmsCallbackServer;
import io.github.yangdaowan.surfear.core.openapi.smscallback.SmsCallbackHandler;
import io.github.yangdaowan.surfear.core.exception.CallbackException;
import io.github.yangdaowan.surfear.core.util.SignUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 百度云短信回调服务实现
 * @author ycf
 **/
@Slf4j
public class BaiduSmsCallbackServer extends AbstractSmsCallbackServer {

    private static volatile BaiduSmsCallbackServer INSTANCE;

    private BaiduSmsCallbackServer(BaiduSmsCallbackConfig config, SmsCallbackHandler<JSONObject> handler) {
        super(config, handler);
    }

    /**
     * 创建百度云回调服务实例
     */
    public static synchronized BaiduSmsCallbackServer createInstance(BaiduSmsCallbackConfig config, SmsCallbackHandler<JSONObject> handler) {
        if (INSTANCE != null) {
            log.warn("[{}] - 尝试使用新配置创建回调服务实例，但实例已存在", config.getServiceName());
            return INSTANCE;
        }
        INSTANCE = new BaiduSmsCallbackServer(config, handler);
        return INSTANCE;
    }

    /**
     * 获取百度云回调服务实例
     */
    public static BaiduSmsCallbackServer getInstance() {
        if (INSTANCE == null) {
            throw new CallbackException("百度云回调服务尚未初始化，请先调用 createInstance 方法");
        }
        return INSTANCE;
    }

    /**
     * 销毁服务实例
     */
    public static void destroyInstance() {
        if (INSTANCE != null) {
            synchronized (BaiduSmsCallbackServer.class) {
                if (INSTANCE != null) {
                    if (INSTANCE.isRunning()) {
                        try {
                            INSTANCE.stop().get();
                        } catch (Exception e) {
                            log.error("[{}] - 销毁回调服务实例时发生错误",
                                INSTANCE.config.getServiceName(), e);
                        }
                    }
                    INSTANCE = null;
                    log.info("百度云回调服务实例已销毁");
                }
            }
        }
    }

    @Override
    protected AbstractCallbackHandler createCallbackHandler() {
        return new BaiduCallbackHandler();
    }

    private class BaiduCallbackHandler extends AbstractCallbackHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // 只处理POST请求
                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendResponse(exchange, 405, "Method Not Allowed");
                    return;
                }

                // 读取请求体
                String requestBody = readRequestBody(exchange);
                log.debug("[{}] - 收到回调请求: {}", config.getServiceName(), requestBody);

                // 验证请求
                if (!validateRequest(exchange, requestBody)) {
                    sendResponse(exchange, 403, "Forbidden");
                    return;
                }

                // 解析JSON
                JSONObject callbackData = JSON.parseObject(requestBody);

                // 根据回调类型处理
                if (callbackData.containsKey("deliverTime")) {
                    handler.handleStatusReport(callbackData);
                } else if (callbackData.containsKey("content")) {
                    handler.handleUplinkMessage(callbackData);
                } else {
                    handler.handleUnknownCallback(callbackData);
                }

                sendResponse(exchange, 200, "OK");
            } catch (Exception e) {
                log.error("[{}] - 回调处理错误", config.getServiceName(), e);
                sendResponse(exchange, 500, "Internal Server Error");
            }
        }

        @Override
        protected boolean validateRequest(HttpExchange exchange, String requestBody) {
            String requestId = exchange.getRequestHeaders().getFirst("requestId");
            String signature = exchange.getRequestHeaders().getFirst("signature");
            String timestamp = exchange.getRequestHeaders().getFirst("timestamp");

            if (requestId == null || signature == null || timestamp == null) {
                log.error("[{}] 缺少必要的请求头信息", config.getServiceName());
                return false;
            }

            String computeSignature = SignUtil.md5Hex(timestamp + requestBody);
            if (!signature.equals(computeSignature)) {
                log.error("[{}] - requestId：{}，签名验证失败", config.getServiceName(), requestId);
                return false;
            }

            return true;
        }
    }

}
