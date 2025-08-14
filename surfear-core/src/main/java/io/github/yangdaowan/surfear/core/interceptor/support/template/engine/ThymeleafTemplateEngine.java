package io.github.yangdaowan.surfear.core.interceptor.support.template.engine;

import io.github.yangdaowan.surfear.core.interceptor.support.template.engine.processor.SimpleDialect;
import io.github.yangdaowan.surfear.core.interceptor.support.template.enums.TemplateMode;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ycf
 **/
public class ThymeleafTemplateEngine implements TemplateEngine {

    private final Map<String, org.thymeleaf.TemplateEngine> templateEngineMap = new HashMap<>();

    private org.thymeleaf.TemplateEngine getTemplateEngine(String templateMode) {
        if(templateEngineMap.containsKey(templateMode)) {
            return templateEngineMap.get(templateMode);
        }
        org.thymeleaf.TemplateEngine templateEngine = createTemplateEngine(templateMode);
        templateEngineMap.put(templateMode, templateEngine);
        return templateEngine;
    }

    private org.thymeleaf.TemplateEngine createTemplateEngine(String templateMode) {
        org.thymeleaf.TemplateEngine templateEngine = new org.thymeleaf.TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();

        if(templateMode.equalsIgnoreCase(TemplateMode.HTML.name())) {
            resolver.setTemplateMode(org.thymeleaf.templatemode.TemplateMode.HTML);
        } else if(templateMode.equalsIgnoreCase(TemplateMode.TXT.name())) {
            resolver.setTemplateMode(org.thymeleaf.templatemode.TemplateMode.TEXT);
            templateEngine.addDialect(new SimpleDialect());
        }
        resolver.setCacheable(false);
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

    /**
     * 使用给定的模板名称和模型数据处理模板。
     *
     * @param templateContent 模板内容
     * @param model        模型变量的 Map
     * @return 解析后的 HTML 字符串
     */
    public String processTemplate(String templateMode, String templateContent, Map<String, Object> model) {
        Context context = new Context();
        context.setVariables(model);

        return getTemplateEngine(templateMode).process(templateContent, context);
    }
}
