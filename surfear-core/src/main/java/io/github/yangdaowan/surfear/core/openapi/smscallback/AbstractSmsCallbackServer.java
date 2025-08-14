package io.github.yangdaowan.surfear.core.openapi.smscallback;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.github.yangdaowan.surfear.core.concurrent.ExecutorFactory;
import io.github.yangdaowan.surfear.core.exception.CallbackException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * 抽象短信回调服务
 * <p>
 * 提供短信回调服务的基础实现，支持HTTP服务器的启动、停止和回调处理。
 * 子类需要实现具体的回调数据解析逻辑。
 * </p>
 * 
 * @author ycf
 */
@Slf4j
public abstract class AbstractSmsCallbackServer {

    protected final SmsCallbackConfig config;
    protected final SmsCallbackHandler handler;
    protected HttpServer server;
    protected ExecutorService httpExecutor;

    protected AbstractSmsCallbackServer(SmsCallbackConfig config, SmsCallbackHandler handler) {
        validateConfig(config);
        this.config = config;
        this.handler = handler;
    }

    protected void validateConfig(SmsCallbackConfig config) {
        if (config == null) {
            throw new CallbackException("回调服务配置不能为空");
        }
        if (config.getPort() <= 0 || config.getPort() > 65535) {
            throw new CallbackException("无效的端口号: " + config.getPort());
        }
        if (config.getPath() == null || !config.getPath().startsWith("/")) {
            throw new CallbackException("无效的回调路径: " + config.getPath());
        }
    }

    /**
     * 启动服务
     */
    public CompletableFuture<Void> start() {
        return CompletableFuture.runAsync(() -> {
            try {
                this.httpExecutor = ExecutorFactory.createThreadPoolExecutor("callback-http-" + config.getServiceName() + "-%d");

                // 创建并配置服务器
                server = HttpServer.create(new InetSocketAddress(config.getPort()), 0);
                server.createContext(config.getPath(), createCallbackHandler());
                server.setExecutor(this.httpExecutor);
                server.start();

                log.info("[{}] - 回调服务启动 http://localhost:{}{}",
                    config.getServiceName(), config.getPort(), config.getPath());

            } catch (Exception e) {
                log.error("[{}] - 回调服务启动失败", config.getServiceName(), e);
                throw new CallbackException("回调服务启动失败", e);
            }
        }, ExecutorFactory.getDefaultExecutor());
    }

    /**
     * 停止服务
     */
    public CompletableFuture<Void> stop() {
        return CompletableFuture.runAsync(() -> {
            try {
                if (server != null) {
                    server.stop(0);
                    log.info("[{}] - 回调服务已停止", config.getServiceName());
                }
            } catch (Exception e) {
                log.error("[{}] - 回调服务停止失败", config.getServiceName(), e);
                throw new CallbackException("回调服务停止失败", e);
            }
        }, ExecutorFactory.getDefaultExecutor());
    }

    public boolean isRunning() {
        return server != null;
    }

    /**
     * 创建具体的回调处理器
     */
    protected abstract AbstractCallbackHandler createCallbackHandler();

    /**
     * 抽象回调处理器
     */
    protected abstract static class AbstractCallbackHandler implements HttpHandler {

        protected String readRequestBody(HttpExchange exchange) throws IOException {
            try (InputStream is = exchange.getRequestBody()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                return bos.toString("UTF-8");
            }
        }

        protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }

        protected boolean validateRequest(HttpExchange exchange, String requestBody) {
            // 默认验证逻辑，子类可覆盖
            return true;
        }
    }
}
