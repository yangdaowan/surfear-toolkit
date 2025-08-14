package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype;

/**
 * @author ycf
 **/
public class Video {

    /**
     * 通过上传媒体文件接口，获取media_id参数值。
     */
    private String videoMediaId;
    /**
     * 视频类型，支持mp4格式。
     */
    private String videoType;
    /**
     * 视频封面图，通过上传媒体文件接口，获取media_id参数值。
     */
    private String picMediaId;
    /**
     * 语音消息时长，单位秒。
     */
    private String duration;

    public String getVideoMediaId() {
        return videoMediaId;
    }

    public void setVideoMediaId(String videoMediaId) {
        this.videoMediaId = videoMediaId;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getPicMediaId() {
        return picMediaId;
    }

    public void setPicMediaId(String picMediaId) {
        this.picMediaId = picMediaId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
