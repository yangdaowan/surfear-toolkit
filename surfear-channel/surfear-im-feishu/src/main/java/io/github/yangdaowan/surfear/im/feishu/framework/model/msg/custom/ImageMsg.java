package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom;

import io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype.Image;
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
public class ImageMsg extends CustomRobotMsg {

    private final String msg_type = "image";

    private Image content;

}
