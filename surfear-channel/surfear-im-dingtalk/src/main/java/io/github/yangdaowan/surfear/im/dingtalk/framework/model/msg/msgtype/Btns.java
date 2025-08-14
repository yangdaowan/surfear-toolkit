package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ycf
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Btns {

    /**
     * 按钮标题。
     */
    private String title;
    /**
     * 点击按钮触发的URL
     */
    private String actionURL;

}
