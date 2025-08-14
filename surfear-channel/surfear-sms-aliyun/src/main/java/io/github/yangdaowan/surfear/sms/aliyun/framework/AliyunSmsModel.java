package io.github.yangdaowan.surfear.sms.aliyun.framework;

import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @see <a href="https://next.api.aliyun.com/document/Dysmsapi/2017-05-25/SendSms">阿里云短信接口</a>
 * @see <a href="https://next.api.aliyun.com/meta/v1/products/Dysmsapi/versions/2017-05-25/apis/SendSms/api?spm=api-workbench.API%20Document.0.0.5be31c6csLRD1q">接口元数据</a>
 * @author ycf
 **/
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class AliyunSmsModel extends Message {

    /**
     * 手机号码之间以半角逗号（,）分隔。上限为 1000 个手机号码
     */
    @Required
    private String phoneNumbers;

    /**
     * 短信签名名称
     */
    @Required
    private String signName;

    /**
     * 短信模板 Code
     */
    @Required
    private String templateCode;

    /**
     * 短信模板变量对应的实际值，最终传递为JSON 字符串
     */
    private Map<String, Object> templateParam;

    /**
     * 上行短信扩展码。上行短信指发送给通信服务提供商的短信，用于定制某种服务、完成查询，或是办理某种业务等，需要收费，按运营商普通短信资费进行扣费。
     */
    private String smsUpExtendCode;

    /**
     * 外部流水扩展字段。
     */
    private String outId;

    /**
     * 对父类参数填值，以便在父类自动打印日志信息
     */
    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        super.setTo(phoneNumbers);
    }

    /**
     * 对父类参数填值，以便在父类自动打印日志信息
     */
    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
        super.setTemplateFileName(templateCode + ".txt");
    }

    /**
     * 对父类参数填值，以便在父类自动打印日志信息
     */
    public void setTemplateParam(Map<String, Object> templateParam) {
        this.templateParam = templateParam;
        super.setTemplateParams(templateParam);
    }

}
