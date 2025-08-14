package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.ActionCard4;

/**
 * @author ycf
 **/
public class SampleActionCard4 {

    private final String msgKey = "sampleActionCard4";

    private ActionCard4 msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public ActionCard4 getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(ActionCard4 msgParam) {
        this.msgParam = msgParam;
    }
}
