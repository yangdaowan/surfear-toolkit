package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.ActionCard6;

/**
 * @author ycf
 **/
public class SampleActionCard6 {

    private final String msgKey = "sampleActionCard6";

    private ActionCard6 msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public ActionCard6 getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(ActionCard6 msgParam) {
        this.msgParam = msgParam;
    }
}
