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
public class MediaFile {

    /**
     * 是否必填: 是<br/>
     * 说明: 文件id，通过下文的文件上传接口获取
     */
    private String media_id;

}
