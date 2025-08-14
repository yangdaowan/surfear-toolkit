package io.github.yangdaowan.surfear.im.wecom.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ycf
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Markdown {

    /**
     * markdown格式的消息。<br/>
     * 是否必填: 是 <br/>
     * 说明: markdown内容，最长不超过4096个字节，必须是utf8编码
     */
    private String content;

}
