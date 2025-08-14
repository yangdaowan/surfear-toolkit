package io.github.yangdaowan.surfear.sms.baidu.framework;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.openapi.client.OkhttpClient;
import io.github.yangdaowan.surfear.core.spi.channel.AbsMessageChannel;
import io.github.yangdaowan.surfear.core.spi.channel.Channel;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import io.github.yangdaowan.surfear.sms.baidu.core.api.BaiduSendSmsApi;

/**
 * @author ycf
 **/

@AutoService(MessageChannel.class)
@Channel(
        type = MessageType.SMS,
        name = "baidu",
        description = "百度云短信",
        model = BaiduSmsModel.class,
        config = BaiduConfig.class,
        exception = BaiduException.class
)
public class BaiduSmsChannel extends AbsMessageChannel {

    @Override
    public MessageResult send(MessageContext context) {
        BaiduSendSmsApi api = new BaiduSendSmsApi(context);
        return MessageResult.success(context, api.parseResponse(new OkhttpClient().execute(api)));
    }
}
