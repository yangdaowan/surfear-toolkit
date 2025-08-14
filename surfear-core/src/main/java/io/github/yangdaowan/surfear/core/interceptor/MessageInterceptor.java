package io.github.yangdaowan.surfear.core.interceptor;

import io.github.yangdaowan.surfear.core.spi.message.MessageResult;

/**
 * 消息通道拦截器接口
 * @author ycf
 **/
public interface MessageInterceptor {
    /**
     * 拦截消息发送过程
     *
     * @param chain 拦截器链
     * @return 消息结果
     */
    MessageResult intercept(MessageChain chain);

    /**
     * 获取拦截器的顺序
     *
     * @return 拦截器顺序
     */
    default int getOrder() {
        Order annotation = this.getClass().getAnnotation(Order.class);
        if(annotation == null){
            return 99;
        }
        return annotation.value();
    }
}
