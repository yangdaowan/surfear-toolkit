package io.github.yangdaowan.surfear.sms.aliyun.core.api;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.config.ConfigUtil;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.sms.aliyun.core.AliyunApiSignV3;
import io.github.yangdaowan.surfear.sms.aliyun.core.api.base.AliyunOpenApi;
import io.github.yangdaowan.surfear.sms.aliyun.framework.AliyunConfig;
import io.github.yangdaowan.surfear.sms.aliyun.framework.AliyunSmsModel;


/**
 * @see <a href="https://next.api.aliyun.com/document/Dysmsapi/2017-05-25/SendSms">阿里云短信接口</a>
 * @see <a href="https://next.api.aliyun.com/meta/v1/products/Dysmsapi/versions/2017-05-25/apis/SendSms/api?spm=api-workbench.API%20Document.0.0.5be31c6csLRD1q">接口元数据</a>
 * @author ycf
 **/
public class AliyunSendSmsApi extends AliyunOpenApi {

    private final MessageContext context;
    private final AliyunConfig config;
    private final AliyunSmsModel model;

    public AliyunSendSmsApi(MessageContext context) {
        this.context = context;
        this.model = (AliyunSmsModel) context.getModel();
        this.config = ConfigUtil.getChannelConfigObject(context.getConfig(), AliyunConfig.class);

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口信息
        this.setPostJson("https://dysmsapi.aliyuncs.com/");
        // 请求头 无body，可以先初始化请求头
        this.initHeader("SendSms", "2017-05-25");
        // Query参数
        this.queryParam.put("PhoneNumbers", this.model.getPhoneNumbers());
        this.queryParam.put("SignName", this.model.getSignName());
        this.queryParam.put("TemplateCode", this.model.getTemplateCode());
        if(model.getTemplateParam() != null) {
            queryParam.put("TemplateParam", JSONObject.toJSONString(this.model.getTemplateParam()));
        }
        if(model.getSmsUpExtendCode() != null) {
            queryParam.put("SmsUpExtendCode", this.model.getSmsUpExtendCode());
        }
        if(model.getOutId() != null) {
            queryParam.put("OutId", this.model.getOutId());
        }
        // 签名
        AliyunApiSignV3.generateAuthorization(this, config);
    }


}

