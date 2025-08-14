package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.Image;

/**
 * @author ycf
 **/
public class SampleImageMsg extends SampleMsg {

    private final String msgKey = "sampleImageMsg";

    private Image msgParam;

    public SampleImageMsg(Image msgParam) {
        this.msgParam = msgParam;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public Image getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(Image msgParam) {
        this.msgParam = msgParam;
    }
}
