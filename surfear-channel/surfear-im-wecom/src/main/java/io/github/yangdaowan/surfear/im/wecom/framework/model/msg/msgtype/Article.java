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
public class Article {

    /**
     * 是否必填: 是 <br/>
     * 说明: 标题，不超过128个字节，超过会自动截断
     */
    private String title;

    /**
     * 是否必填: 否 <br/>
     * 说明: 描述，不超过512个字节，超过会自动截断
     */
    private String description;

    /**
     * 是否必填: 是 <br/>
     * 说明: 点击后跳转的链接
     */
    private String url;

    /**
     * 是否必填: 否 <br/>
     * 说明: 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。
     */
    private String picurl;

}
