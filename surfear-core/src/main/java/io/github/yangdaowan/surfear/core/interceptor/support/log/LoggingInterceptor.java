package io.github.yangdaowan.surfear.core.interceptor.support.log;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.interceptor.MessageChain;
import io.github.yangdaowan.surfear.core.interceptor.MessageInterceptor;
import io.github.yangdaowan.surfear.core.interceptor.Order;
import io.github.yangdaowan.surfear.core.spi.MessageChannelRegistry;
import io.github.yangdaowan.surfear.core.spi.channel.ChannelMetadata;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ycf
 **/
@Order(0)
@AutoService(MessageInterceptor.class)
public class LoggingInterceptor implements MessageInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public MessageResult intercept(MessageChain chain) {
        MessageContext context = chain.context();
        ChannelMetadata metadata = MessageChannelRegistry.getMetadata(context.getChannelKey());

        try {
            // 继续执行下一个拦截器或最终的消息发送
            MessageResult result = chain.proceed(context);

            // 打印日志输出
            log.info("[{} - {}] 结果：\r\n{}",
                    metadata.getDescription(),
                    context.getMessageId(),
                    result.toString()
            );

            return result;
        } catch (Throwable e) {
            log.error("[{} - {}] 错误原因：\r\n{}",  metadata.getDescription(), context.getMessageId(), e.getMessage());
            e.printStackTrace();
            return MessageResult.error(context, e);

        }
    }

}