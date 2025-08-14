package io.github.yangdaowan.surfear.im.feishu.core.api.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.ExistJsonResponse;
import io.github.yangdaowan.surfear.im.feishu.framework.config.FeishuConfig;

/**
 * <a href="https://open.feishu.cn/document/server-docs/authentication-management/access-token/tenant_access_token_internal">自建应用获取 tenant_access_token</a>
 * <br/>
 * 自建应用通过此接口获取 tenant_access_token。请注意：
 * <br/>
 * - tenant_access_token的有效期为7200秒（2小时）。
 * <br/>
 * - 剩余有效期小于 30 分钟时，调用本接口会返回一个新的 tenant_access_token，这会同时存在两个有效的 tenant_access_token。
 * <br/>
 * - 剩余有效期大于等于 30 分钟时，调用本接口会返回原有的 tenant_access_token。
 * @author ycf
 **/
public class FeishuTenantAccessTokenApi extends AbsOpenApi {

    private final FeishuConfig config;

    public FeishuTenantAccessTokenApi(FeishuConfig config) {
        this.config = config;
        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口信息
        this.setPostJson("https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal");
        // 请求体
        JSONObject body = new JSONObject();
        body.put("app_id", config.getAppId());
        body.put("app_secret", config.getAppSecret());
        this.setBody(body.toJSONString());
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new ExistJsonResponse("code", "msg", "tenant_access_token").parseResponse(body);
    }

}
