package io.github.yangdaowan.surfear.core.interceptor;

import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;

import java.util.concurrent.CompletableFuture;

/**
 * 消息拦截器链
 * @author ycf
 **/
public interface MessageChain {

    /**
     * 获取消息上下文
     */
    MessageContext context();

    /**
     * 继续执行下一个拦截器或最终的消息发送
     */
    MessageResult proceed(MessageContext context);

}
