package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.At;
import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.Markdown;
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
public class MarkdownMsg extends CustomRobotMsg{

    private final String msgtype = "markdown";

    private Markdown markdown;

    private At at;

    public MarkdownMsg(Markdown markdown) {
        this.markdown = markdown;
    }

    public MarkdownMsg(At at) {
        this.at = at;
    }
}
