package io.github.yangdaowan.surfear.im.dingtalk.core;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.im.dingtalk.core.api.base.DingtalkAccessTokenApi;
import io.github.yangdaowan.surfear.im.dingtalk.core.store.AccessTokenStore;
import io.github.yangdaowan.surfear.im.dingtalk.core.store.InMemoryAccessTokenStore;
import io.github.yangdaowan.surfear.im.dingtalk.framework.config.DingtalkConfig;

/**
 * @author ycf
 */
public class DingtalkAccessTokenUtil {

    // 单例实例
    private static final DingtalkAccessTokenUtil INSTANCE = new DingtalkAccessTokenUtil();

    // 默认使用 InMemoryAccessTokenStore 的单例
    private static final InMemoryAccessTokenStore DEFAULT_STORE = new InMemoryAccessTokenStore();

    // 当前使用的 store
    private static AccessTokenStore store = DEFAULT_STORE;

    // 获取单例实例
    public static DingtalkAccessTokenUtil getInstance() {
        return INSTANCE;
    }

    // 获取当前 store
    public AccessTokenStore getStore() {
        return this.store;
    }

    public String getAccessToken(DingtalkConfig config) {
        String appKey = config.getClientId();

        String accessToken;
        if(store.isExist(appKey)){
            accessToken = store.getAccessToken(appKey);
        } else {
            DingtalkAccessTokenApi tokenApi = new DingtalkAccessTokenApi(config);
            JSONObject result = tokenApi.parseResponse(new OkhttpClient().execute(tokenApi));

            accessToken = result.getString("accessToken");
            long expireIn = result.getLong("expireIn");

            store.setAccessToken(appKey, accessToken, expireIn);
        }
        return accessToken;
    }
}
