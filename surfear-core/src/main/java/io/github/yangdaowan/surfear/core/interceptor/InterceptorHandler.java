package io.github.yangdaowan.surfear.core.interceptor;

import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持拦截器的消息通道包装类
 * @author ycf
 **/
public class InterceptorHandler {

    private final MessageChannel delegate;
    private final List<MessageInterceptor> interceptors = new ArrayList<>();

    public InterceptorHandler(MessageChannel delegate) {
        this.delegate = delegate;
    }

    public InterceptorHandler(MessageChannel delegate, List<MessageInterceptor> interceptors) {
        this.delegate = delegate;
        this.interceptors.addAll(interceptors);
    }

    public void addInterceptor(MessageInterceptor interceptor){
        this.interceptors.add(interceptor);
    }

    public MessageResult handler(MessageContext context) {
        // 创建拦截器链
        RealMessageChain chain = new RealMessageChain(interceptors, context, delegate, 0);
        return chain.proceed(context);
    }

}
