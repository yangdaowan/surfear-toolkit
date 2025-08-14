package io.github.yangdaowan.surfear.core.interceptor.support.template.engine;

import java.util.Map;

public interface TemplateEngine {

    /**
     * 使用给定的模板名称和模型数据处理模板。
     *
     * @param templateMode 模板模型
     * @param templateContent 模板内容
     * @param model        模型变量的 Map
     * @return 解析后的 模版 字符串
     */
    String processTemplate(String templateMode, String templateContent, Map<String, Object> model);

}
