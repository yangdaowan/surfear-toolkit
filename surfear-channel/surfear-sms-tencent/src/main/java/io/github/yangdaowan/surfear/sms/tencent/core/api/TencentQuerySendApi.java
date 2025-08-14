package io.github.yangdaowan.surfear.sms.tencent.core.api;

import com.alibaba.fastjson2.JSONObject;
import io.github.yangdaowan.surfear.sms.tencent.core.TencentApiSignV3;
import io.github.yangdaowan.surfear.sms.tencent.core.api.base.TencentOpenApi;
import io.github.yangdaowan.surfear.sms.tencent.framework.TencentConfig;
import io.github.yangdaowan.surfear.sms.tencent.framework.TencentSmsQueryModel;

/**
 * @see <a href="https://next.api.aliyun.com/document/Dysmsapi/2017-05-25/QuerySendDetails">查询短信发送详情</a>
 * @author ycf
 **/
public class TencentQuerySendApi extends TencentOpenApi {

    private final TencentConfig config;
    private final TencentSmsQueryModel model;

    public TencentQuerySendApi(TencentConfig config, TencentSmsQueryModel model) {
        this.config = config;
        this.model = model;

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口配置
        this.setPostJson("https://sms.tencentcloudapi.com/");
        // 请求头
        this.initHeader("sms", "PullSmsSendStatusByPhoneNumber", "2021-01-11", "ap-guangzhou");
        // Body参数
        JSONObject body = new JSONObject();
        body.put("SmsSdkAppId", model.getSmsSdkAppId());
        body.put("PhoneNumber", model.getPhoneNumber());
        body.put("Limit", model.getLimit());
        body.put("Offset", model.getOffset());
        body.put("BeginTime", model.getBeginTime());
        if(model.getEndTime() != null){
            body.put("EndTime", model.getEndTime());
        }
        this.setBody(body.toJSONString());

        // 签名
        TencentApiSignV3.generateAuthorization(this, config);
    }

}
