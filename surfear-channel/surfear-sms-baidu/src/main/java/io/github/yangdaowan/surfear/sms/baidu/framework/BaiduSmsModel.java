package io.github.yangdaowan.surfear.sms.baidu.framework;

import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author ycf
 * @see <a href="https://cloud.baidu.com/doc/SMS/s/lkijy5wvf">百度云短信接口</a>
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BaiduSmsModel extends Message {

    /**
     * 多个手机号之间以英文逗号分隔，一次请求最多支持200个手机号。
     */
    @Required
    private String mobile;

    /**
     * 短信模板ID
     */
    @Required
    private String template;

    /**
     * 短信签名ID
     */
    @Required
    private String signatureId;

    /**
     * 模板变量内容，用于替换短信模板中定义的变量，为json字符串格式
     */
    @Required
    private Map<String, Object> contentVar;

    /**
     * 用户自定义参数，格式为字符串，状态回调时会回传该值
     */
    private String custom;

    /**
     * 通道自定义扩展码，上行回调时会回传该值，其格式为纯数字串。默认为不开通，请求时无需设置该参数。如需开通请联系SMS帮助申请
     */
    private String userExtId;

    public void setMobile(String mobile) {
        this.mobile = mobile;
        super.setTo(mobile);
    }

    public void setTemplate(String template) {
        this.template = template;
        super.setTemplateFileName(template + ".txt");
    }

    public void setContentVar(Map<String, Object> contentVar) {
        this.contentVar = contentVar;
        super.setTemplateParams(contentVar);
    }
}
