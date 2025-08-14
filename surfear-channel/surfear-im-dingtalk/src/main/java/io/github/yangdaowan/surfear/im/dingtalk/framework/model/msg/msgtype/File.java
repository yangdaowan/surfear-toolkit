package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

/**
 * @author ycf
 **/
public class File {

    /**
     * 通过上传媒体文件接口，获取media_id参数值。
     */
    private String mediaId;
    /**
     * 文件名称。
     */
    private String fileName;
    /**
     * 文件类型。
     */
    private String fileType;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
