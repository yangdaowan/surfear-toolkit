package io.github.yangdaowan.surfear.sms.aliyun.framework;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.core.spi.channel.AbsMessageChannel;
import io.github.yangdaowan.surfear.core.spi.channel.Channel;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import io.github.yangdaowan.surfear.sms.aliyun.core.api.AliyunSendSmsApi;

/**
 * @author ycf
 **/
@AutoService(MessageChannel.class)
@Channel(
        type = MessageType.SMS,
        name = "aliyun",
        description = "阿里云短信",
        model = AliyunSmsModel.class,
        config = AliyunConfig.class,
        exception = AliyunException.class
)
public class AliyunSmsChannel extends AbsMessageChannel {

    @Override
    public MessageResult send(MessageContext context) {
        AliyunSendSmsApi api = new AliyunSendSmsApi(context);
        return MessageResult.success(context, api.parseResponse(new OkhttpClient().execute(api)));
    }

}
