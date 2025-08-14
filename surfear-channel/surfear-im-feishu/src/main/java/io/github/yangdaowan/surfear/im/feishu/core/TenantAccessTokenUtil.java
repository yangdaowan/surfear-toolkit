package io.github.yangdaowan.surfear.im.feishu.core;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.im.feishu.core.api.base.FeishuTenantAccessTokenApi;
import io.github.yangdaowan.surfear.im.feishu.core.store.AccessTokenStore;
import io.github.yangdaowan.surfear.im.feishu.core.store.InMemoryAccessTokenStore;
import io.github.yangdaowan.surfear.im.feishu.framework.config.FeishuConfig;

public class TenantAccessTokenUtil {

    // 单例实例
    private static final TenantAccessTokenUtil INSTANCE = new TenantAccessTokenUtil();

    // 默认使用 InMemoryAccessTokenStore 的单例
    private static final InMemoryAccessTokenStore DEFAULT_STORE = new InMemoryAccessTokenStore();

    // 当前使用的 store
    private static AccessTokenStore store = DEFAULT_STORE;

    // 获取单例实例
    public static TenantAccessTokenUtil getInstance() {
        return INSTANCE;
    }

    // 获取当前 store
    public AccessTokenStore getStore() {
        return this.store;
    }

    public String getAccessToken(FeishuConfig config) {
        if(config.getAppId() == null || config.getAppSecret() == null) {
            throw new IllegalArgumentException("appId 和 appSecret 不能为空！");
        }
        String appId = config.getAppId();

        String accessToken;
        if(store.isExist(appId)){
            accessToken = store.getAccessToken(appId);
        } else {
            FeishuTenantAccessTokenApi tokenApi = new FeishuTenantAccessTokenApi(config);
            JSONObject result = tokenApi.parseResponse(new OkhttpClient().execute(tokenApi));

            accessToken = result.getString("tenant_access_token");
            long expire = result.getLong("expire");

            store.setAccessToken(appId, accessToken, expire);
        }
        return accessToken;
    }
}
