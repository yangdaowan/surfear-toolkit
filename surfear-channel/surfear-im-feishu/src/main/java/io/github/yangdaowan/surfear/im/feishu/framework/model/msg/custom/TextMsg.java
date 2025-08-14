package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype.At;
import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype.Text;
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

    private final String msg_type = "text";

    private Text content;

    private At at;

    public Text getContent() {
        if (at != null) {
            if (at.getAtOpenId() != null && !at.getAtOpenId().isEmpty()) {
                content.setText( "<at user_id=\"" + at.getAtOpenId() + "\"></at>" + content.getText() );
            }
        }
        return content;
    }

    public TextMsg(Text content) {
        this.content = content;
    }

    public TextMsg(At at) {
        this.at = at;
    }
}
