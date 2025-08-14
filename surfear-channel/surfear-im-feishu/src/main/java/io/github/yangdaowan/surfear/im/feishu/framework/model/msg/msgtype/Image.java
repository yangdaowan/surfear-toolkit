package io.github.yangdaowan.surfear.im.feishu.framework.model.msg.msgtype;

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
     * 描述: 图片的唯一标识。可通过<a href="https://open.feishu.cn/document/server-docs/im-v1/image/create">上传图片</a> 接口获取 image_key。
     */
    private String image_key;

}
