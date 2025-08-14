package io.github.yangdaowan.surfear.core.interceptor.support.template.interceptor;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.interceptor.MessageChain;
import io.github.yangdaowan.surfear.core.interceptor.MessageInterceptor;
import io.github.yangdaowan.surfear.core.interceptor.Order;
import io.github.yangdaowan.surfear.core.interceptor.support.template.DefaultTemplateManager;
import io.github.yangdaowan.surfear.core.interceptor.support.template.manager.TemplateManager;
import io.github.yangdaowan.surfear.core.interceptor.support.template.model.MessageTemplate;
import io.github.yangdaowan.surfear.core.spi.MessageChannelRegistry;
import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ycf
 **/
@Slf4j
@Order(1)
@AutoService(MessageInterceptor.class)
public class TemplateInterceptor implements MessageInterceptor {

    private final TemplateManager templateManager = DefaultTemplateManager.getInstance();

    @Override
    public MessageResult intercept(MessageChain chain) {
        MessageContext context = chain.context();
        MessageType type = MessageChannelRegistry.getMessageType(context.getChannelKey());
        Message model = context.getModel();
        if(model.getTemplateFileName() != null && !model.getTemplateFileName().isEmpty()) {
            try {
                MessageTemplate messageTemplate = templateManager.getTemplate(type.name().toLowerCase(), model.getTemplateFileName());
                String templateContent = templateManager.renderTemplate(messageTemplate, model.getTemplateParams());
                model.setContent(templateContent);
            } catch (Exception e) {
                log.warn(e.getMessage());
                if(!type.equals(MessageType.SMS)){
                    throw e;
                }
            }
        }

        return chain.proceed(context);
    }

}
