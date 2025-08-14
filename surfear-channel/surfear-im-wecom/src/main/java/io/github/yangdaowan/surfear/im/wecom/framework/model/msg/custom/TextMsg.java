package io.github.yangdaowan.surfear.im.wecom.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.wecom.framework.model.msg.msgtype.Text;
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
public class TextMsg extends CustomRobotMsg {

    private final String msgtype = "text";

    private Text text;

}
