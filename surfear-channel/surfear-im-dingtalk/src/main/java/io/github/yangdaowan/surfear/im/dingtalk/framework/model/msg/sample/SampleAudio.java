package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.Audio;

/**
 * @author ycf
 **/
public class SampleAudio {

    private final String msgKey = "sampleAudio";

    private Audio msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public Audio getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(Audio msgParam) {
        this.msgParam = msgParam;
    }
}
