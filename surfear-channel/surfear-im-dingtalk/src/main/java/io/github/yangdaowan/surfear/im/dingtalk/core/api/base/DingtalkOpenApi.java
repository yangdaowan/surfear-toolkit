package io.github.yangdaowan.surfear.im.dingtalk.core.api.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.config.ConfigUtil;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.ExistJsonResponse;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.im.dingtalk.core.DingtalkAccessTokenUtil;
import io.github.yangdaowan.surfear.im.dingtalk.framework.config.DingtalkConfig;

/**
 * @author ycf
 **/
public abstract class DingtalkOpenApi extends AbsOpenApi {

    private final DingtalkConfig config;

    public DingtalkOpenApi(MessageContext context) {
        this.config = ConfigUtil.getChannelConfigObject(context.getConfig(), DingtalkConfig.class);
    }

    @Override
    public void buildRequest() {
        String accessToken = DingtalkAccessTokenUtil.getInstance().getAccessToken(this.config);
        // Header参数
        this.addHeader("x-acs-dingtalk-access-token", accessToken);
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new ExistJsonResponse("code", "message", "processQueryKey").parseResponse(body);
    }
}
