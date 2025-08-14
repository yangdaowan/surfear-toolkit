package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ycf
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {

    /**
     * 消息标题。
     */
    private String title;
    /**
     * 消息内容。如果太长只会部分展示。
     */
    private String text;
    /**
     * 图片URL。
     */
    private String picUrl;
    /**
     * 点击消息跳转的URL。<br/>
     * <a href="https://open.dingtalk.com/document/orgapp/message-link-description#title-bi2-kq8-nj5">消息链接在PC端侧边栏或外部浏览器打开<a/> <br/>
     */
    private String messageUrl;

}
