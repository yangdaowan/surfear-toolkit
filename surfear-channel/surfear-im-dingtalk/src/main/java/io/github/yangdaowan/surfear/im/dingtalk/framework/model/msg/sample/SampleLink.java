package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.Link;

/**
 * @author ycf
 **/
public class SampleLink {

    private final String msgKey = "sampleLink";

    private Link msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public Link getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(Link msgParam) {
        this.msgParam = msgParam;
    }
}
