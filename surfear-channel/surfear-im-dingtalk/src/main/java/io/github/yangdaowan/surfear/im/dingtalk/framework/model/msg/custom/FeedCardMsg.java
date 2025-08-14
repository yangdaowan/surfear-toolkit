package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.FeedCard;
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
public class FeedCardMsg extends CustomRobotMsg{

    private final String msgtype = "feedCard";

    private FeedCard feedCard;

}
