package io.github.yangdaowan.surfear.im.dingtalk.framework.channel;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.core.spi.channel.AbsMessageChannel;
import io.github.yangdaowan.surfear.core.spi.channel.Channel;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import io.github.yangdaowan.surfear.im.dingtalk.core.api.DingtalkWebhookRobotApi;
import io.github.yangdaowan.surfear.im.dingtalk.framework.config.DingtalkWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.dingtalk.framework.exception.DingtalkException;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.DingtalkWebhookRobotModel;

/**
 * <a href="https://open.dingtalk.com/document/orgapp/custom-robots-send-group-messages">自定义机器人发送群消息</a>
 * <br/>
 * <a href="https://open.dingtalk.com/document/orgapp/obtain-the-webhook-address-of-a-custom-robot?spm=ding_open_doc.document.0.0.ae823665EBLCTp">获取自定义机器人 Webhook 地址</a>
 * <br/>
 * 每个机器人每分钟最多发送20条消息到群里，如果超过20条，会限流10分钟。
 * @author ycf
 **/
@AutoService(MessageChannel.class)
@Channel(
        type = MessageType.IM,
        name = "dingtalk:webhook:robot",
        description = "钉钉webhook自定义机器人",
        model = DingtalkWebhookRobotModel.class,
        config = DingtalkWebhookRobotConfig.class,
        exception = DingtalkException.class
)
public class DingtalkWebhookRobotChannel extends AbsMessageChannel {

    @Override
    public MessageResult send(MessageContext context) {
        DingtalkWebhookRobotApi api = new DingtalkWebhookRobotApi(context);
        return MessageResult.success(context, api.parseResponse(new OkhttpClient().execute(api)));
    }
}
