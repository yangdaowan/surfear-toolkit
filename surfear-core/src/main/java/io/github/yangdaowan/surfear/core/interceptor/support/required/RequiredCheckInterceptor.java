package io.github.yangdaowan.surfear.core.interceptor.support.required;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.exception.ModelParamValidateException;
import io.github.yangdaowan.surfear.core.interceptor.MessageChain;
import io.github.yangdaowan.surfear.core.interceptor.MessageInterceptor;
import io.github.yangdaowan.surfear.core.interceptor.Order;
import io.github.yangdaowan.surfear.core.spi.MessageChannelRegistry;
import io.github.yangdaowan.surfear.core.spi.channel.ChannelMetadata;
import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;

import java.lang.reflect.Field;

/**
 * 消息模型必填字段检查
 * @author ycf
 **/
@Order(2)
@AutoService(MessageInterceptor.class)
public class RequiredCheckInterceptor implements MessageInterceptor {

    @Override
    public MessageResult intercept(MessageChain chain) {
        MessageContext context = chain.context();
        Message model = context.getModel();

        ChannelMetadata metadata = MessageChannelRegistry.getMetadata(context.getChannelKey());
        // 消息模型是否支持检查
        if (!metadata.getModel().isInstance(model)) {
            throw new ModelParamValidateException("模型不支持：当前通道不支持 "+ model.getClass().getName()+" 类型的消息模型");
        }

        for (Field field : model.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Required annotation = field.getAnnotation(Required.class);
            Object value;
            try {
                value = field.get(model);
            } catch (IllegalAccessException e) {
                throw new ModelParamValidateException("当前的安全管理器不允许对私有成员进行访问", e);
            }
            if (annotation != null && value == null) {
                String msg = annotation.message().replace("{}", field.getName());
                throw new ModelParamValidateException("必填参数缺失："+ msg);
            }
        }

        return chain.proceed(context);
    }
}
