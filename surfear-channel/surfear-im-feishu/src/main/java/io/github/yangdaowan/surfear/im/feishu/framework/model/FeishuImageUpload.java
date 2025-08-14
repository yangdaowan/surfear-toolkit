package io.github.yangdaowan.surfear.im.feishu.framework.model;

import io.github.yangdaowan.surfear.core.openapi.bean.FileUpload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class FeishuImageUpload extends FileUpload {

    /**
     * 必填：是<br/>
     * 描述：图片类型<br/>
     * 示例值："message"<br/>
     * 可选值有：<br/>
     * - message：用于发送消息，当前工具包写死<br/>
     * - avatar：用于设置头像<br/>
     */
    private final String imageType = "message";

    /**
     * 必填：是<br/>
     * 描述：图片内容。传值方式可以参考请求体示例。<br/>
     * 注意：上传的图片大小不能超过 10 MB，也不能上传大小为 0 的图片。<br/>
     * 分辨率限制：<br/>
     * - GIF 图片分辨率不能超过 2000 x 2000，其他图片分辨率不能超过 12000 x 12000。<br/>
     * - 用于设置头像的图片分辨率不能超过 4096 x 4096。<br/>
     * 示例值：二进制文件<br/>
     */
    private final File file;

    public FeishuImageUpload(File file) {
        this.file = file;
    }

    @Override
    public void uploadPart(Object builder) {
        if(builder instanceof MultipartBody.Builder){
            MultipartBody.Builder okhttpBuilder = (MultipartBody.Builder) builder;
            if (this.getFile() != null) {
                okhttpBuilder.addFormDataPart( "image_type", this.getImageType() );
                okhttpBuilder.addFormDataPart(
                        "image",
                        this.getFile().getName(),
                        RequestBody.create(this.getFile(), MediaType.parse("image/jpeg"))
                );
            }
        }
    }
}
