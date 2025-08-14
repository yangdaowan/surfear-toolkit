package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype.RichText;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 富文本消息类型
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RichTextMsg extends CustomRobotMsg {

    private final String msg_type = "post";

    private RichText content;

}
