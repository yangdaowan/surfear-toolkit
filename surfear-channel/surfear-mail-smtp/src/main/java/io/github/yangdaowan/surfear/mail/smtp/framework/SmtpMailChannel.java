package io.github.yangdaowan.surfear.mail.smtp.framework;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.config.ConfigUtil;
import io.github.yangdaowan.surfear.core.config.converter.ConfigConverter;
import io.github.yangdaowan.surfear.core.exception.SendFailedException;
import io.github.yangdaowan.surfear.core.spi.channel.AbsMessageChannel;
import io.github.yangdaowan.surfear.core.spi.channel.Channel;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import io.github.yangdaowan.surfear.mail.smtp.core.AttachmentFactory;
import io.github.yangdaowan.surfear.mail.smtp.core.SmtpMailRequest;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author ycf
 **/
@AutoService(MessageChannel.class)
@Channel(
        type = MessageType.MAIL,
        name = "smtp",
        description = "SMTP协议邮件",
        model = SmtpMailModel.class,
        config = SmtpMailConfig.class,
        exception = SmtpMailException.class
)
public class SmtpMailChannel extends AbsMessageChannel {

    @Override
    public MessageResult send(MessageContext context) {
        try {
            // 配置
            SmtpMailConfig config = ConfigUtil.getChannelConfigObject(context.getConfig(), SmtpMailConfig.class);
            Properties props = ConfigConverter.convertToProperties(config, "mail.smtp.");
            // 模型
            SmtpMailModel model = (SmtpMailModel) context.getModel();
            // 会话
            Session session = SmtpMailRequest.openSession(props);
            // 消息
            MimeMessage message = SmtpMailRequest.createMimeMessage(
                    session,
                    model.getTo(),
                    model.getCc(),
                    model.getBcc(),
                    model.getSubject(),
                    model.getContent(),
                    model.getAttachments());

            Transport.send(message);

        } catch (MessagingException e) {
            throw new SendFailedException("邮件发送对象构建失败", e);
        } finally {
            // 清理临时文件
            AttachmentFactory.cleanupTempFiles();
        }
        return MessageResult.success(context, null);
    }

}
