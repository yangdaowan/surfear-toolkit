package io.github.yangdaowan.surfear.core.openapi.client;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * HTTP客户端工厂，统一管理HTTP连接资源
 */
@Slf4j
public class OkHttpClientFactory {

    private static final int DEFAULT_CONNECT_TIMEOUT = 10;
    private static final int DEFAULT_READ_TIMEOUT = 30;
    private static final int DEFAULT_WRITE_TIMEOUT = 10;
    private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 32;
    private static final long DEFAULT_KEEP_ALIVE_DURATION = 5L;

    private static volatile OkHttpClient defaultClient;

    public static OkHttpClient getDefaultClient() {
        if (defaultClient == null) {
            synchronized (OkHttpClientFactory.class) {
                if (defaultClient == null) {
                    defaultClient = createClient(
                        DEFAULT_CONNECT_TIMEOUT,
                        DEFAULT_READ_TIMEOUT,
                        DEFAULT_WRITE_TIMEOUT,
                        DEFAULT_MAX_IDLE_CONNECTIONS,
                        DEFAULT_KEEP_ALIVE_DURATION
                    );
                }
            }
        }
        return defaultClient;
    }

    public static OkHttpClient createClient(
            int connectTimeout,
            int readTimeout,
            int writeTimeout,
            int maxIdleConnections,
            long keepAliveDuration) {

        return new OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(
                maxIdleConnections,
                keepAliveDuration,
                TimeUnit.MINUTES
            ))
            .retryOnConnectionFailure(true)
            .build();
    }

    public static void shutdown() {
        if (defaultClient != null) {
            defaultClient.dispatcher().executorService().shutdown();
            defaultClient.connectionPool().evictAll();
        }
    }
}
