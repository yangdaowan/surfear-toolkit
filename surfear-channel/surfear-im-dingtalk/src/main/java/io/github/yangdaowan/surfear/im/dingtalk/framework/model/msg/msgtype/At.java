package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ycf
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class At {

    /**
     * 是否@所有人。
     */
    private boolean isAtAll;
    /**
     * 被@人的手机号。
     */
    private List<String> atMobiles;
    /**
     * 被@人的用户userId。
     */
    private List<String> atUserIds;

    public At setAtAll() {
        isAtAll = true;
        return this;
    }

    public At setAtMobiles(List<String> atMobiles) {
        this.atMobiles = atMobiles;
        return this;
    }

    public At setAtUserIds(List<String> atUserIds) {
        this.atUserIds = atUserIds;
        return this;
    }
}
