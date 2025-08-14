package io.github.yangdaowan.surfear.core.interceptor.support.template.service;

import io.github.yangdaowan.surfear.core.interceptor.support.template.model.MessageTemplate;

import java.util.List;

public interface TemplateLoad {

    /**
     * 获取消息模版
     */
    List<MessageTemplate> getMessageTemplate();

}
