package io.github.yangdaowan.surfear.sms.tencent.framework;

import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ycf
 * @see <a href="https://cloud.tencent.com/document/api/382/55981">腾讯云短信接口文档</a>
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class TencentSmsModel extends Message {

    /**
     * 下发手机号码，采用 E.164 标准，格式为+[国家或地区码][手机号]，单次请求最多支持200个手机号且要求全为境内手机号或全为境外手机号。
     * 例如：+8618501234444， 其中前面有一个+号 ，86为国家码，18501234444为手机号。
     * 注：发送国内短信格式还支持0086、86或无任何国家或地区码的11位手机号码，前缀默认为+86。
     * 示例值：["+8618501234444"]
     */
    @Required
    private String[] phoneNumberSet;

    /**
     * 短信 SdkAppId，在 短信控制台 添加应用后生成的实际 SdkAppId，示例如1400006666。
     * 示例值：1400006666
     */
    @Required
    private String smsSdkAppId;

    /**
     * 模板 ID，必须填写已审核通过的模板 ID。模板 ID 可前往 国内短信 或 国际/港澳台短信 的正文模板管理查看，若向境外手机号发送短信，仅支持使用国际/港澳台短信模板。
     * 示例值：1110
     */
    @Required
    private String templateId;

    /**
     * 短信签名内容，使用 UTF-8 编码，必须填写已审核通过的签名，例如：腾讯云，签名信息可前往 国内短信 或 国际/港澳台短信 的签名管理查看。
     * 注意
     * 发送国内短信该参数必填，且需填写签名内容而非签名ID。
     * 发送国际/港澳台短信该参数非必填。
     */
    @Required
    private String signName;

    /**
     * 模板参数，若无模板参数，则设置为空。
     * 注意
     * 模板参数的个数需要与 TemplateId 对应模板的变量个数保持一致。
     * 示例值：["4370"]
     */
    private String[] templateParamSet;

    /**
     * 短信码号扩展号，默认未开通，如需开通请联系 腾讯云短信小助手。
     * 示例值：10
     */
    private String extendCode;

    /**
     * 用户的 session 内容，可以携带用户侧 ID 等上下文信息，server 会原样返回。注意长度需小于512字节。
     * 示例值：outsid_1729495320_1011
     */
    private String sessionContext;

    /**
     * 国内短信无需填写该项；国际/港澳台短信已申请独立 SenderId 需要填写该字段，默认使用公共 SenderId，无需填写该字段。
     * 注：月度使用量达到指定量级可申请独立 SenderId 使用，详情请联系 腾讯云短信小助手。
     * 示例值：Qsms
     */
    private String senderId;


    public void setPhoneNumberSet(String[] phoneNumberSet) {
        this.phoneNumberSet = phoneNumberSet;
        super.setTo(String.join(",", phoneNumberSet));
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
        super.setTemplateFileName(templateId + ".txt");
    }

    public void setTemplateParamSet(String[] templateParamSet) {
        this.templateParamSet = templateParamSet;
        if(templateParamSet != null && templateParamSet.length > 0) {
            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < templateParamSet.length; i++) {
                params.put(String.valueOf(i), templateParamSet[i]);
            }
            super.setTemplateParams(params);
        }
    }
}
