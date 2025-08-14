package io.github.yangdaowan.surfear.im.wecom.framework.channel;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.core.spi.channel.AbsMessageChannel;
import io.github.yangdaowan.surfear.core.spi.channel.Channel;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import io.github.yangdaowan.surfear.im.wecom.core.api.WecomWebhookRobotApi;
import io.github.yangdaowan.surfear.im.wecom.framework.config.WecomWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.wecom.framework.exception.WecomException;
import io.github.yangdaowan.surfear.im.wecom.framework.model.WecomWebhookRobotModel;

/**
 * @see <a href="https://developer.work.weixin.qq.com/document/path/99110#%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8%E7%BE%A4%E6%9C%BA%E5%99%A8%E4%BA%BA">群机器人配置说明</a>
 * </br>
 * - 每个机器人发送的消息不能超过20条/分钟。
 * @author ycf
 **/
@AutoService(MessageChannel.class)
@Channel(
        type = MessageType.IM,
        name = "wecom:webhook:robot",
        description = "企微webhook自定义机器人",
        model = WecomWebhookRobotModel.class,
        config = WecomWebhookRobotConfig.class,
        exception = WecomException.class
)
public class WecomWebhookRobotChannel extends AbsMessageChannel {

    @Override
    public MessageResult send(MessageContext context) {
        WecomWebhookRobotApi api = new WecomWebhookRobotApi(context);
        return MessageResult.success(context, api.parseResponse(new OkhttpClient().execute(api)));
    }

}
