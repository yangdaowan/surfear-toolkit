package io.github.yangdaowan.surfear.sms.baidu.core.api;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.sms.baidu.core.api.base.BaiduOpenApi;
import io.github.yangdaowan.surfear.sms.baidu.framework.BaiduSmsModel;

/**
 * @author ycf
 * @see <a href="https://cloud.baidu.com/doc/SMS/s/lkijy5wvf">百度云短信接口</a>
 **/
public class BaiduSendSmsApi extends BaiduOpenApi {

    private final BaiduSmsModel model;

    public BaiduSendSmsApi(MessageContext context) {
        super(context);
        this.model = (BaiduSmsModel)context.getModel();

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口配置
        this.setPostJson("https://smsv3.bj.baidubce.com/api/v3/sendSms");
        // 请求体
        this.setBody(JSONObject.toJSONString(model));
        // 签名
        super.buildRequest();
    }

}
