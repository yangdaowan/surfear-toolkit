package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.Markdown;

/**
 * @author ycf
 **/
public class SampleMarkdown extends SampleMsg {

    private final String msgKey = "sampleMarkdown";

    private Markdown msgParam;

    public SampleMarkdown(Markdown msgParam) {
        this.msgParam = msgParam;
    }

    public Markdown getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(Markdown msgParam) {
        this.msgParam = msgParam;
    }

    public String getMsgKey() {
        return msgKey;
    }
}
