package io.github.yangdaowan.surfear.im.dingtalk.core.api.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.ExistJsonResponse;
import io.github.yangdaowan.surfear.im.dingtalk.framework.config.DingtalkConfig;

/**
 * <a href="https://open.dingtalk.com/document/orgapp/obtain-the-access_token-of-an-internal-app">获取企业内部应用的accessToken</a>
 * <br/>
 * 在使用accessToken时，请注意：
 * <br/>
 * - accessToken的有效期为7200秒（2小时），有效期内重复获取会返回相同结果并自动续期，过期后获取会返回新的accessToken。
 * <br/>
 * - 开发者需要缓存accessToken，用于后续接口的调用。因为每个应用的accessToken是彼此独立的，所以进行缓存时需要区分应用来进行存储。
 * <br/>
 * - 不能频繁调用gettoken接口，否则会受到频率拦截。
 * @author ycf
 **/
public class DingtalkAccessTokenApi extends AbsOpenApi {

    private final DingtalkConfig config;

    public DingtalkAccessTokenApi(DingtalkConfig config) {
        this.config = config;
        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口信息
        this.setPostJson("https://api.dingtalk.com/v1.0/oauth2/accessToken");
//        this.setPostJson("https://oapi.dingtalk.com/gettoken");
        // 请求体
        JSONObject body = new JSONObject();
        body.put("appKey", config.getClientId());
        body.put("appSecret", config.getClientSecret());
        this.setBody(body.toJSONString());
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new ExistJsonResponse("code", "message", "accessToken").parseResponse(body);
    }

}
