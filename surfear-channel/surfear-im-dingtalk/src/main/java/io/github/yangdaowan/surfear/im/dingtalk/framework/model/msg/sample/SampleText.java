package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.Text;

/**
 * @author ycf
 **/
public class SampleText {

    private final String msgKey = "sampleText";

    private Text msgParam;

    public SampleText(Text msgParam) {
        this.msgParam = msgParam;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public Text getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(Text msgParam) {
        this.msgParam = msgParam;
    }
}
