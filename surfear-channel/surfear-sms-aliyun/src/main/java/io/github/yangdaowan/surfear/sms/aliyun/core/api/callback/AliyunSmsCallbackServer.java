package io.github.yangdaowan.surfear.sms.aliyun.core.api.callback;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import io.github.yangdaowan.surfear.core.exception.CallbackException;
import io.github.yangdaowan.surfear.core.openapi.smscallback.AbstractSmsCallbackServer;
import io.github.yangdaowan.surfear.core.openapi.smscallback.SmsCallbackHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 阿里云短信回调服务实现
 * <a href="https://help.aliyun.com/zh/sms/developer-reference/smsup-http?spm=a2c4g.11186623.help-menu-44282.d_5_4_3_0.536f41d9OIr1Kb&scm=20140722.H_101847._.OR_help-T_cn~zh-V_1">上行<a/>
 * <a href="https://help.aliyun.com/zh/sms/developer-reference/smsreport-http?spm=a2c4g.11186623.0.0.7f3a6caeWE9c8P#concept-hpb-htk-3gb">状态<a/>
 * @author ycf
 **/
@Slf4j
public class AliyunSmsCallbackServer extends AbstractSmsCallbackServer {

    private static volatile AliyunSmsCallbackServer INSTANCE;

    private AliyunSmsCallbackServer(AliyunSmsCallbackConfig config, SmsCallbackHandler<JSONArray> handler) {
        super(config, handler);
    }

    /**
     * 创建阿里云回调服务实例
     */
    public static synchronized AliyunSmsCallbackServer createInstance(AliyunSmsCallbackConfig config, SmsCallbackHandler<JSONArray> handler) {
        if (INSTANCE != null) {
            log.warn("[{}] - 尝试使用新配置创建回调服务实例，但实例已存在", config.getServiceName());
            return INSTANCE;
        }
        INSTANCE = new AliyunSmsCallbackServer(config, handler);
        return INSTANCE;
    }

    /**
     * 获取阿里云回调服务实例
     */
    public static AliyunSmsCallbackServer getInstance() {
        if (INSTANCE == null) {
            throw new CallbackException("阿里云回调服务尚未初始化，请先调用 createInstance 方法");
        }
        return INSTANCE;
    }

    /**
     * 销毁服务实例
     */
    public static void destroyInstance() {
        if (INSTANCE != null) {
            synchronized (AliyunSmsCallbackServer.class) {
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
                    log.info("阿里云回调服务实例已销毁");
                }
            }
        }
    }

    @Override
    protected AbstractCallbackHandler createCallbackHandler() {
        return new AliyunCallbackHandler();
    }

    private class AliyunCallbackHandler extends AbstractCallbackHandler {

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
                if (callbackData.getJSONObject(0).containsKey("report_time")) {
                    handler.handleStatusReport(callbackData);
                } else if (callbackData.getJSONObject(0).containsKey("content")) {
                    handler.handleUplinkMessage(callbackData);
                } else {
                    handler.handleUnknownCallback(callbackData);
                }

                JSONObject response = new JSONObject();
                response.put("code", 0);
                response.put("msg", "OK");

                sendResponse(exchange, 200, response.toJSONString());
            } catch (Exception e) {
                log.error("[{}] - 回调处理错误", config.getServiceName(), e);
                sendResponse(exchange, 500, "Internal Server Error");
            }
        }

    }

}
