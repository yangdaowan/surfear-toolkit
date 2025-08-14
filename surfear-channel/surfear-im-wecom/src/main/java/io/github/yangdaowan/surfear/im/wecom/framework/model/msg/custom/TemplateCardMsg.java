package io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateCardMsg extends CustomRobotMsg {

    private final String msgtype = "template_card";

    private JSONObject template_card;

}
