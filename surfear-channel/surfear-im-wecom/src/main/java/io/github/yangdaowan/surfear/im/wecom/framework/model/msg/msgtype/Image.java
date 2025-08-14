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
public class Image {

    /**
     * 是否必填: 是<br/>
     * 说明: 图片内容的base64编码<br/>
     * 注：图片（base64编码前）最大不能超过2M，支持JPG,PNG格式
     */
    private String base64;

    /**
     * 是否必填: 是<br/>
     * 说明: 图片内容（base64编码前）的md5值
     */
    private String md5;

}
