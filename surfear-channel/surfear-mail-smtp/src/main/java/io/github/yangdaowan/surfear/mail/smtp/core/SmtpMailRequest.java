package io.github.yangdaowan.surfear.mail.smtp.core;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * @author ycf
 **/
public class SmtpMailRequest {

    public static Session openSession(Properties props){
        String user = props.getProperty("mail.smtp.user");
        String password = props.getProperty("mail.smtp.password");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        user, password
                );
            }
        });
    }

    /**
     * 创建带有附件的 MIME 消息。
     *
     * @param to        收件人邮箱地址列表 - "," 分割
     * @param cc        抄送邮箱地址列表 - "," 分割
     * @param bcc       密送邮箱地址列表 - "," 分割
     * @param subject   邮件主题
     * @param content   邮件正文内容
     * @param attachments 邮件附件
     * @return 创建的 MIME 消息
     * @throws MessagingException 如果消息创建失败
     */
    public static MimeMessage createMimeMessage(Session session,
                                                String to, String cc, String bcc,
                                                String subject, String content,
                                                List<MailAttachment> attachments)
            throws MessagingException {

        String user = session.getProperty("mail.smtp.user");
        MimeMessage message = new MimeMessage(session);
        message.setFrom(user);

        if (to != null) {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        }
        if (cc != null) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
        }
        if (bcc != null) {
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
        }

        message.setSubject(subject);

        // 创建多部分消息容器
        MimeMultipart multipart = new MimeMultipart();

        // 添加附件
        if (attachments != null && !attachments.isEmpty()) {
            for (MailAttachment attachment : attachments) {

                MimeBodyPart attachmentPart = new MimeBodyPart();

                attachmentPart.setDataHandler(attachment.getData());
                // 对文件名进行 MIME 编码（UTF-8）
                String encodedFileName;
                try {
                    encodedFileName = MimeUtility.encodeText(attachment.getFilename(), "UTF-8", null);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                attachmentPart.setFileName(encodedFileName);
                multipart.addBodyPart(attachmentPart);
            }
        }

        // 创建文本部分
        MimeBodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(content, "text/html; charset=utf-8");
        multipart.addBodyPart(contentPart);

        // 将多部分消息设置到邮件中
        message.setContent(multipart);

        return message;
    }


}
