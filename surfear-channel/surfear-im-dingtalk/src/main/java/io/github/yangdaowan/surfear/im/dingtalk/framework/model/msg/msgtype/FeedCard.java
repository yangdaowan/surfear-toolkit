package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ycf
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedCard {

    private List<Links> links;

}
