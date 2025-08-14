package io.github.yangdaowan.surfear.sms.tencent.core.api.callback;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import io.github.yangdaowan.surfear.core.openapi.smscallback.AbstractSmsCallbackServer;
import io.github.yangdaowan.surfear.core.openapi.smscallback.SmsCallbackHandler;
import io.github.yangdaowan.surfear.core.exception.CallbackException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 腾讯云短信回调服务实现
 * <a href="https://cloud.tencent.com/document/product/382/37809">配置基础信息<a/>
 * <a href="https://cloud.tencent.com/document/product/382/59178">短信下发状态通知<a/>
 * @author ycf
 **/
@Slf4j
public class TencentSmsCallbackServer extends AbstractSmsCallbackServer {

    private static volatile TencentSmsCallbackServer INSTANCE;

    private TencentSmsCallbackServer(TencentSmsCallbackConfig config, SmsCallbackHandler<JSONArray> handler) {
        super(config, handler);
    }

    /**
     * 创建腾讯云回调服务实例
     */
    public static synchronized TencentSmsCallbackServer createInstance(TencentSmsCallbackConfig config, SmsCallbackHandler<JSONArray> handler) {
        if (INSTANCE != null) {
            log.warn("[{}] - 尝试使用新配置创建回调服务实例，但实例已存在", config.getServiceName());
            return INSTANCE;
        }
        INSTANCE = new TencentSmsCallbackServer(config, handler);
        return INSTANCE;
    }

    /**
     * 获取腾讯云回调服务实例
     */
    public static TencentSmsCallbackServer getInstance() {
        if (INSTANCE == null) {
            throw new CallbackException("腾讯云回调服务尚未初始化，请先调用 createInstance 方法");
        }
        return INSTANCE;
    }

    /**
     * 销毁服务实例
     */
    public static void destroyInstance() {
        if (INSTANCE != null) {
            synchronized (TencentSmsCallbackServer.class) {
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
                    log.info("腾讯云回调服务实例已销毁");
                }
            }
        }
    }

    @Override
    protected AbstractCallbackHandler createCallbackHandler() {
        return new TencentCallbackHandler();
    }

    private class TencentCallbackHandler extends AbstractCallbackHandler {

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
                JSONArray callbackData = JSONArray.parseArray(requestBody);

                // 根据回调类型处理
                if (callbackData.getJSONObject(0).containsKey("report_status")) {
                    handler.handleStatusReport(callbackData);
                } else if (callbackData.getJSONObject(0).containsKey("text")) {
                    handler.handleUplinkMessage(callbackData);
                } else {
                    handler.handleUnknownCallback(callbackData);
                }

                JSONObject response = new JSONObject();
                response.put("result", 0);
                response.put("errmsg", "OK");

                sendResponse(exchange, 200, response.toJSONString());
            } catch (Exception e) {
                log.error("[{}] - 回调处理错误", config.getServiceName(), e);
                sendResponse(exchange, 500, "Internal Server Error");
            }
        }

    }

}
