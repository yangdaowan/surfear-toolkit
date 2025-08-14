package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg;

import io.github.yangdaowan.surfear.core.openapi.bean.FileUpload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class DingtalkFileUpload extends FileUpload {

    // 允许的文件类型
    private static final String[] ALLOWED_TYPES1 = {
            "image",        // image：图片，图片最大20MB。支持上传jpg、gif、png、bmp格式。
            "voice",        // voice：语音，语音文件最大2MB。支持上传amr、mp3、wav格式。
            "video",        // video：视频，视频最大20MB。支持上传mp4格式。
            "file"          // file：普通文件，最大20MB。支持上传doc、docx、xls、xlsx、ppt、pptx、zip、pdf、rar格式。
    };

    // 允许的文件类型及其对应的扩展名数组
    private static final String[] ALLOWED_TYPES2 = {
            "jpg", "gif", "png", "bmp", // image：图片，图片最大20MB。支持上传jpg、gif、png、bmp格式。
            "amr", "mp3", "wav",        // voice：语音，语音文件最大2MB。支持上传amr、mp3、wav格式。
            "mp4",                      // video：视频，视频最大20MB。支持上传mp4格式。
            "doc", "docx", "xls", "xlsx", // file：普通文件，最大20MB。支持上传doc、docx、xls、xlsx、ppt、pptx、zip、pdf、rar格式。
            "ppt", "pptx", "zip", "pdf", "rar"
    };

    /**
     * 必填: 是<br/>
     * 描述: 媒体文件类型<br/>
     * 示例值："mp4"<br/>
     * 可选值有：<br/>
     * - image：图片，图片最大20MB。支持上传jpg、gif、png、bmp格式。<br/>
     * - voice：语音，语音文件最大2MB。支持上传amr、mp3、wav格式。<br/>
     * - video：视频，视频最大20MB。支持上传mp4格式。<br/>
     * - file：普通文件，最大20MB。支持上传doc、docx、xls、xlsx、ppt、pptx、zip、pdf、rar格式。
     */
    private final String fileType;

    /**
     * 必填: 是<br/>
     * 描述: 要上传的媒体文件。<br/>
     * 示例值：二进制文件<br/>
     */
    private final File file;


    public DingtalkFileUpload(String type, File file) {
        this.fileType = type;
        this.file = file;
        this.checkFileType();
    }

    public void checkFileType() {
        // 检查类型是否在允许列表中
        if(!Arrays.asList(ALLOWED_TYPES1).contains(this.fileType)){
            throw new IllegalArgumentException("不支持的文件类型: " + this.fileType + "，仅支持 " + String.join(", ", ALLOWED_TYPES1));
        }

        String fileName = file.getName();
        // 获取小写的文件扩展名（不带点）
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);

        // 检查扩展名是否在允许列表中
        if(!Arrays.asList(ALLOWED_TYPES2).contains(extension)){
            throw new IllegalArgumentException("不支持的文件类型: " + extension + "，仅支持 " + String.join(", ", ALLOWED_TYPES2));
        }
    }

    @Override
    public void uploadPart(Object builder) {
        if(builder instanceof MultipartBody.Builder){
            MultipartBody.Builder okhttpBuilder = (MultipartBody.Builder) builder;
            if (this.getFile() != null) {
                okhttpBuilder.addFormDataPart( "type", this.getFileType() );
                okhttpBuilder.addFormDataPart(
                        "media",
                        this.file.getName(),
                        RequestBody.create(this.getFile(), MediaType.parse("application/octet-stream"))
                );
            }
        }
    }
}
