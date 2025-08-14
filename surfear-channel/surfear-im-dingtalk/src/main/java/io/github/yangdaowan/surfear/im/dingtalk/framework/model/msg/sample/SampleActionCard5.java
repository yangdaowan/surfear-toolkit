package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.ActionCard5;

/**
 * @author ycf
 **/
public class SampleActionCard5 {

    private final String msgKey = "sampleActionCard5";

    private ActionCard5 msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public ActionCard5 getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(ActionCard5 msgParam) {
        this.msgParam = msgParam;
    }
}
