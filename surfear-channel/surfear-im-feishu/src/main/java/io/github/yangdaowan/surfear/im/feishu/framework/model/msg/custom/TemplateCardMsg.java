package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype.TemplateCard;
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

    private final String msg_type = "interactive";

    private TemplateCard card;

}
