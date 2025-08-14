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
public class Links {

    /**
     * feedCard消息内每条内容的标题。。
     */
    private String title;
    /**
     * feedCard消息内每条内容的图片URL，建议使用上传媒体文件接口获取。。
     */
    private String picURL;
    /**
     * feedCard消息内每条内容上午跳转链接。。
     */
    private String messageURL;

}
