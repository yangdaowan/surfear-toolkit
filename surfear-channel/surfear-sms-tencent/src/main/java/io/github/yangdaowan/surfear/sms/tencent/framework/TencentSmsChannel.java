package io.github.yangdaowan.surfear.sms.tencent.framework;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.core.spi.channel.AbsMessageChannel;
import io.github.yangdaowan.surfear.core.spi.channel.Channel;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import io.github.yangdaowan.surfear.sms.tencent.core.api.TencentSendSmsApi;

/**
 * @author ycf
 **/

@AutoService(MessageChannel.class)
@Channel(
        type = MessageType.SMS,
        name = "tencent",
        description = "腾讯云短信",
        model = TencentSmsModel.class,
        config = TencentConfig.class,
        exception = TencentException.class
)
public class TencentSmsChannel extends AbsMessageChannel {

    @Override
    public MessageResult send(MessageContext context) {
        TencentSendSmsApi api = new TencentSendSmsApi(context);
        return MessageResult.success(context, api.parseResponse(new OkhttpClient().execute(api)));
    }
}
