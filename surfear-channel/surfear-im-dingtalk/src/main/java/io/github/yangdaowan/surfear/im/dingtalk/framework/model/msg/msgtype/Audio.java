package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

/**
 * @author ycf
 **/
public class Audio {

    /**
     * 通过上传媒体文件接口，获取media_id参数值。
     */
    private String mediaId;
    /**
     * 语音消息时长，单位毫秒。
     */
    private String duration;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
