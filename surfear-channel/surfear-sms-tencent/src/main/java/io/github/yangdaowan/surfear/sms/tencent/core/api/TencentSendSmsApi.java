package io.github.yangdaowan.surfear.sms.tencent.core.api;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.config.ConfigUtil;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.sms.tencent.core.TencentApiSignV3;
import io.github.yangdaowan.surfear.sms.tencent.core.api.base.TencentOpenApi;
import io.github.yangdaowan.surfear.sms.tencent.framework.TencentConfig;
import io.github.yangdaowan.surfear.sms.tencent.framework.TencentSmsModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ycf
 * @see <a href="https://cloud.tencent.com/document/api/382/55981">腾讯云短信接口文档</a>
 **/
public class TencentSendSmsApi extends TencentOpenApi {

    private final TencentSmsModel model;
    private final TencentConfig config;

    public TencentSendSmsApi(MessageContext context) {
        this.model = (TencentSmsModel)context.getModel();
        this.config = ConfigUtil.getChannelConfigObject(context.getConfig(), TencentConfig.class);

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口配置
        this.setPostJson("https://sms.tencentcloudapi.com/");
        // 请求头
        this.initHeader("sms", "SendSms", "2021-01-11", "ap-guangzhou");
        // 请求参数
        Map<String, Object> params = new HashMap<>(6);
        params.put("SmsSdkAppId", model.getSmsSdkAppId());
        params.put("SignName", model.getSignName());
        params.put("TemplateId", model.getTemplateId());
        params.put("TemplateParamSet", model.getTemplateParamSet());
        params.put("PhoneNumberSet", model.getPhoneNumberSet());
        params.put("SessionContext", model.getSessionContext());
        this.setBody(JSONObject.toJSONString(params));
        // 签名
        TencentApiSignV3.generateAuthorization(this, config);
    }

}
