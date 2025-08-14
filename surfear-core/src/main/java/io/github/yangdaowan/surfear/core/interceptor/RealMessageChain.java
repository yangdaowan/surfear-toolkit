package io.github.yangdaowan.surfear.core.interceptor;

import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 具体的消息拦截器链实现
 * @author ycf
 **/
public class RealMessageChain implements MessageChain {

    private final List<MessageInterceptor> interceptors;
    private final MessageContext context;
    private final MessageChannel channel;
    private final int index;

    public RealMessageChain(List<MessageInterceptor> interceptors, MessageContext context, MessageChannel channel, int index) {
        this.interceptors = interceptors;
        this.context = context;
        this.channel = channel;
        this.index = index;
    }

    @Override
    public MessageContext context() {
        return context;
    }

    @Override
    public MessageResult proceed(MessageContext context) {
        if (index >= interceptors.size()) {
            // 如果没有更多拦截器，则执行最终的消息发送
            return channel.send(context);
        }

        // 获取下一个拦截器
        MessageInterceptor interceptor = interceptors.get(index);
        RealMessageChain nextChain = new RealMessageChain(interceptors, context, channel, index + 1);

        // 调用拦截器
        return interceptor.intercept(nextChain);
    }

}

