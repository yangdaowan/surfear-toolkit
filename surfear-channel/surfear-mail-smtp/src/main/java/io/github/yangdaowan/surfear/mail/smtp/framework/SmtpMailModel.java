package io.github.yangdaowan.surfear.mail.smtp.framework;

import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import io.github.yangdaowan.surfear.mail.smtp.core.MailAttachment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SmtpMailModel extends Message {

    /**
     * 目标邮箱
     */
    @Required
    private String to;
    /**
     * 邮件主题（标题）
     */
    @Required
    private String subject;
    /**
     * 邮件内容
     */
    @Required
    private String content;
    /**
     * 抄送邮箱
     */
    private String cc;
    /**
     * 密送邮箱
     */
    private String bcc;
    /**
     * 邮件附件
     */
    private List<MailAttachment> attachments;

    public void setSubject(String subject) {
        this.subject = subject;
        super.setContent(subject);
    }
}
