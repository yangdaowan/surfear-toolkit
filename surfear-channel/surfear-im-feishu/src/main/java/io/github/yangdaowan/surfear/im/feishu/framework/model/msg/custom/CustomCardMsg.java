package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.custom;

import com.alibaba.fastjson2.JSONObject;
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
public class CustomCardMsg extends CustomRobotMsg {

    private final String msg_type = "interactive";

    private JSONObject card;

}
