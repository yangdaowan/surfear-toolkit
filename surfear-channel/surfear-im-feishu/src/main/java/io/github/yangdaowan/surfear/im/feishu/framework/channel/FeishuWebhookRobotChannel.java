package io.github.yangdaowan.surfear.im.feishu.framework.channel;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.core.spi.channel.AbsMessageChannel;
import io.github.yangdaowan.surfear.core.spi.channel.Channel;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import io.github.yangdaowan.surfear.im.feishu.core.api.FeishuWebhookRobotApi;
import io.github.yangdaowan.surfear.im.feishu.framework.config.FeishuWebhookRobotConfig;
import io.github.yangdaowan.surfear.im.feishu.framework.exception.FeishuException;
import io.github.yangdaowan.surfear.im.feishu.framework.model.FeishuWebhookRobotModel;

/**
 * @see <a href="https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot#478cb64f">自定义机器人使用指南</a>
 * </br>
 * - 自定义机器人的频率控制和普通应用不同，为单租户单机器人 100 次/分钟，5 次/秒。建议发送消息尽量避开诸如 10:00、17:30 等整点及半点时间，否则可能出现因系统压力导致的 11232 限流错误，导致消息发送失败。<br/>
 * - 发送消息时，请求体的数据大小不能超过 20 KB。
 * @author ycf
 **/
@AutoService(MessageChannel.class)
@Channel(
        type = MessageType.IM,
        name = "feishu:webhook:robot",
        description = "飞书webhook自定义机器人",
        model = FeishuWebhookRobotModel.class,
        config = FeishuWebhookRobotConfig.class,
        exception = FeishuException.class
)
public class FeishuWebhookRobotChannel extends AbsMessageChannel {

    @Override
    public MessageResult send(MessageContext context) {
        FeishuWebhookRobotApi api = new FeishuWebhookRobotApi(context);
        return MessageResult.success(context, api.parseResponse(new OkhttpClient().execute(api)));
    }
}
