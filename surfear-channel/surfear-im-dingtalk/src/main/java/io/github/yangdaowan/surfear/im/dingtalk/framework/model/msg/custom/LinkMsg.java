package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkMsg extends CustomRobotMsg{

    private final String msgtype = "link";

    private Link link;

}
