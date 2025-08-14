package io.github.yangdaowan.surfear.core.interceptor.support.template.service;

import io.github.yangdaowan.surfear.core.interceptor.support.template.model.MessageTemplate;

public interface TemplateCrudService {

    MessageTemplate createTemplate(String source, String messageType, String templateMode, String templateName, String content);

    MessageTemplate updateTemplate(String messageType, String templateMode, String templateName, String content);

    void deleteTemplate(String messageType, String templateName);

    MessageTemplate getTemplate(String messageType, String templateName);
}
