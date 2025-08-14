package io.github.yangdaowan.surfear.im.wecom.framework.model;

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
public class WecomFileUpload extends FileUpload {

    private final String fieldName;
    private final String fileName;
    private final File file;

    // 文件
    public WecomFileUpload(File file) {
        this.fieldName = "media";
        this.fileName = file.getName();
        this.file = file;
    }

    @Override
    public void uploadPart(Object builder) {
        if(builder instanceof MultipartBody.Builder){
            MultipartBody.Builder okhttpBuilder = (MultipartBody.Builder) builder;
            if (this.getFile() != null) {
                okhttpBuilder.addFormDataPart(
                        this.getFieldName(),
                        this.getFileName(),
                        RequestBody.create(this.getFile(), MediaType.parse("application/octet-stream"))
                );
            }
        }
    }
}
