package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateCardData {

    private String template_id;
    private String template_version_name = "1.0.0";
    private Map<String, Object> template_variable;

    public TemplateCardData(String template_id, Map<String, Object> template_variable) {
        this.template_id = template_id;
        this.template_variable = template_variable;
    }
}
