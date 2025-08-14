package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.ActionCard3;

/**
 * @author ycf
 **/
public class SampleActionCard3 {

    private final String msgKey = "sampleActionCard3";

    private ActionCard3 msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public ActionCard3 getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(ActionCard3 msgParam) {
        this.msgParam = msgParam;
    }
}
