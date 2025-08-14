 package io.github.yangdaowan.surfear.core.interceptor.support.template.service;

import io.github.yangdaowan.surfear.core.interceptor.support.template.model.MessageTemplate;

import java.util.Map;

 public interface TemplateRender {
     /**
      * 渲染模板
      * @param templateName 模板名称
      * @param variables 模板变量
      * @return 渲染后的内容
      */
     String renderTemplate(String messageType, String templateMode, String templateName, Map<String, Object> variables);

     /**
      * 渲染模板
      * @param messageTemplate 模板
      * @param variables 模板变量
      * @return 渲染后的内容
      */
     String renderTemplate(MessageTemplate messageTemplate, Map<String, Object> variables);

 }