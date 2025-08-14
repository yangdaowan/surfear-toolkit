package io.github.yangdaowan.surfear.sms.aliyun.core.api;

import io.github.yangdaowan.surfear.sms.aliyun.core.AliyunApiSignV3;
import io.github.yangdaowan.surfear.sms.aliyun.core.api.base.AliyunOpenApi;
import io.github.yangdaowan.surfear.sms.aliyun.framework.AliyunConfig;
import io.github.yangdaowan.surfear.sms.aliyun.framework.AliyunSmsQueryModel;

/**
 * @see <a href="https://next.api.aliyun.com/document/Dysmsapi/2017-05-25/QuerySendDetails">查询短信发送详情</a>
 * @author ycf
 **/
public class AliyunQuerySendApi extends AliyunOpenApi {

    private final AliyunConfig config;
    private final AliyunSmsQueryModel model;

    public AliyunQuerySendApi( AliyunConfig config, AliyunSmsQueryModel model) {
        this.config = config;
        this.model = model;

        this.buildRequest();
    }

    @Override
    public void buildRequest() {
        // 接口信息
        this.setGetJson("https://dysmsapi.aliyuncs.com/");
        // 请求头 无body，可以先初始化请求头
        this.initHeader("QuerySendDetails", "2017-05-25");
        // Query参数
        this.queryParam.put("PhoneNumber", this.model.getPhoneNumber());
        this.queryParam.put("BizId", this.model.getBizId());
        this.queryParam.put("SendDate", this.model.getSendDate());
        this.queryParam.put("PageSize", this.model.getPageSize());
        this.queryParam.put("CurrentPage", this.model.getCurrentPage());

        // 签名
        AliyunApiSignV3.generateAuthorization(this, config);
    }




}
