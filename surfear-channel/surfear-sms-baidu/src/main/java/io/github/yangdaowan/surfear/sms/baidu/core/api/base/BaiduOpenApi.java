package io.github.yangdaowan.surfear.sms.baidu.core.api.base;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.config.ConfigUtil;
import io.github.yangdaowan.surfear.core.openapi.api.AbsOpenApi;
import io.github.yangdaowan.surfear.core.openapi.response.JsonResponse;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.util.SignUtil;
import io.github.yangdaowan.surfear.sms.baidu.core.BaiduApiSignV1;
import io.github.yangdaowan.surfear.sms.baidu.framework.BaiduConfig;

/**
 * @author ycf
 * @see <a href="https://cloud.baidu.com/doc/SMS/s/Rkioaqev6">百度云公共请求头</a>
 **/
public class BaiduOpenApi extends AbsOpenApi {

    private final MessageContext context;
    private final BaiduConfig config;

    public BaiduOpenApi(MessageContext context) {
        this.context = context;
        this.config = ConfigUtil.getChannelConfigObject(context.getConfig(), BaiduConfig.class);;
    }

    @Override
    public void buildRequest() {
        this.queryParam.put("clientToken", this.context.getMessageId());
        this.initHeader();
        BaiduApiSignV1.generateAuthorization( this, config);
    }

    @Override
    public JSONObject parseResponse(String body) {
        return new JsonResponse("code", "message", "1000").parseResponse(body);
    }

    // init headers
    private void initHeader() {
        this.headers.put("Host", host);
        this.headers.put("x-bce-date", SignUtil.utcTimestamp());
    }
}
