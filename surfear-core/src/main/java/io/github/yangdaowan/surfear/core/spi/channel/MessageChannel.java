package io.github.yangdaowan.surfear.core.spi.channel;

import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;

/**
 * 消息通道接口
 *
 * @author ycf
 **/
public interface MessageChannel {

    /**
     * 获取通道注解元数据
     */
    ChannelMetadata getMetadata();

    /**
     * 消息同步推送
     * @param context 消息上下文
     * @return map 对象结果
     */
    MessageResult send(MessageContext context);

}
