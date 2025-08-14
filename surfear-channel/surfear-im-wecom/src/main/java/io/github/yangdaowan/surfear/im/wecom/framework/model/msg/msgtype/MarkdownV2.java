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
public class MarkdownV2 {

    /**
     * markdown格式的消息。<br/>
     * 是否必填: 是 <br/>
     * 说明: markdown_v2内容，最长不超过4096个字节，必须是utf8编码。<br/>
     * 特殊的，<br/>
     * 1. markdown_v2不支持字体颜色、@群成员的语法， 具体支持的语法可参考下面说明<br/>
     * 2. 消息内容在客户端 4.1.36 版本以下(安卓端为4.1.38以下) 消息表现为纯文本，建议使用最新客户端版本体验
     */
    private String content;

}
