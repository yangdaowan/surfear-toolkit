package io.github.yangdaowan.surfear.im.feishu.framework.model;

import io.github.yangdaowan.surfear.core.openapi.bean.FileUpload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.util.Locale;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class FeishuFileUpload extends FileUpload {

    // 允许的文件类型及其对应的扩展名数组
    private static final String[] ALLOWED_TYPES = {
            "opus",
            "mp4", "m4v", "mov",  // 视频类
            "pdf",                 // PDF
            "doc", "docx",         // Word
            "xls", "xlsx",         // Excel
            "ppt", "pptx"          // PowerPoint
    };

    /**
     * 必填: 是<br/>
     * 描述: 待上传的文件类型<br/>
     * 示例值："mp4"<br/>
     * 可选值有：<br/>
     * - opus：OPUS 音频文件。其他格式的音频文件，请转为 OPUS 格式后上传。可使用 ffmpeg 转换格式：ffmpeg -i SourceFile.mp3 -acodec libopus -ac 1 -ar 16000 TargetFile.opus<br/>
     * - mp4：MP4 格式视频文件<br/>
     * - pdf：PDF 格式文件<br/>
     * - doc：DOC 格式文件<br/>
     * - xls：XLS 格式文件<br/>
     * - ppt：PPT 格式文件<br/>
     * - stream：stream 格式文件。若上传文件不属于以上枚举类型，可以使用 stream 格式
     */
    private final String fileType;

    /**
     * 必填: 是<br/>
     * 描述: 带后缀的文件名<br/>
     * 示例值："测试视频.mp4"<br/>
     */
    private final String fileName;

    /**
     * 必填: 否<br/>
     * 描述: 文件的时长（视频、音频），单位：毫秒。不传值时无法显示文件的具体时长。<br/>
     * 示例值：3000<br/>
     */
    private final int duration;

    /**
     * 必填: 是<br/>
     * 描述: 文件内容，具体的传值方式可参考请求体示例。<br/>
     * 注意：文件大小不得超过 30 MB，且不允许上传空文件。<br/>
     * 示例值：二进制文件<br/>
     */
    private final File file;

    public FeishuFileUpload(File file) {
        this.fileType = getFileType(file.getName());
        this.fileName = file.getName();
        this.duration = 0;
        this.file = file;
    }

    public FeishuFileUpload(File file, int duration) {
        this.fileType = getFileType(file.getName());
        this.fileName = file.getName();
        this.duration = duration;
        this.file = file;
    }

    public String getFileType(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "stream";
        }

        // 获取小写的文件扩展名（不带点）
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);

        // 检查扩展名是否在允许列表中
        for (String allowed : ALLOWED_TYPES) {
            if (allowed.equals(extension)) {
                return extension;
            }
        }
        return "stream";
    }

    @Override
    public void uploadPart(Object builder) {
        if(builder instanceof MultipartBody.Builder){
            MultipartBody.Builder okhttpBuilder = (MultipartBody.Builder) builder;
            if (this.getFile() != null) {
                okhttpBuilder.addFormDataPart( "file_type", this.getFileType() );
                okhttpBuilder.addFormDataPart( "file_name", this.getFileName() );
                if(this.getDuration() > 0){
                    okhttpBuilder.addFormDataPart( "duration", String.valueOf(this.getDuration()) );
                }
                okhttpBuilder.addFormDataPart(
                        "file",
                        this.getFileName(),
                        RequestBody.create(this.getFile(), MediaType.parse("application/octet-stream"))
                );
            }
        }
    }
}
