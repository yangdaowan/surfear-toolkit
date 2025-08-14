package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author ycf
 **/
@NoArgsConstructor
@Getter
public class At {

    /**
     * 被@人的OpenId。
     */
    private String atOpenId;

    public At setAtAll() {
        this.atOpenId = "all";
        return this;
    }

    public At setAtOpenId(String atOpenId) {
        this.atOpenId = atOpenId;
        return this;
    }
}
