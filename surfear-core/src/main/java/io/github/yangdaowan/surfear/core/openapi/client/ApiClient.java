package io.github.yangdaowan.surfear.core.openapi.client;

import io.github.yangdaowan.surfear.core.openapi.api.OpenApi;

public interface ApiClient<T, E> {

    /**
     * 构造请求
     */
    T build(OpenApi openApi);

    /**
     * 执行请求
     */
    E execute(OpenApi openApi);

}
