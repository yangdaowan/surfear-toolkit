package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.ActionCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class ActionCardMsg extends CustomRobotMsg {

    private final String msgtype = "actionCard";

    private ActionCard actionCard;

}
