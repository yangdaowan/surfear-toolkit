package io.github.yangdaowan.surfear.im.wecom.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author ycf
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Text {

    /**
     * 是否必填: 是 <br/>
     * 说明: 文本内容，最长不超过2048个字节，必须是utf8编码
     */
    private String content;

    /**
     * 被@人的用户userId。<br/>
     * 是否必填: 否 <br/>
     * 说明: userid的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人，如果开发者获取不到userid，可以使用mentioned_mobile_list
     */
    private List<String> mentioned_list;

    /**
     * 被@人的手机号。<br/>
     * 是否必填: 否 <br/>
     * 说明: 手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人
     */
    private List<String> mentioned_mobile_list;

    public Text(String content) {
        this.content = content;
    }

    public Text setAtAll() {
        this.mentioned_list = Collections.singletonList("@all");
        return this;
    }

    public Text setMentioned_mobile_list(List<String> mentioned_mobile_list) {
        this.mentioned_mobile_list = mentioned_mobile_list;
        return this;
    }

    public Text setMentioned_list(List<String> mentioned_list) {
        this.mentioned_list = mentioned_list;
        return this;
    }
}
