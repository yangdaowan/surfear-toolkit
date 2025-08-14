package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.At;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.Text;
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
public class TextMsg extends CustomRobotMsg{

    private final String msgtype = "text";

    private Text text;

    private At at;

    public TextMsg(Text text) {
        this.text = text;
    }

    public TextMsg(At at) {
        this.at = at;
    }
}
