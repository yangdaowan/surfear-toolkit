package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.Video;

/**
 * @author ycf
 **/
public class SampleVideo {

    private final String msgKey = "sampleVideo";

    private Video msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public Video getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(Video msgParam) {
        this.msgParam = msgParam;
    }
}
