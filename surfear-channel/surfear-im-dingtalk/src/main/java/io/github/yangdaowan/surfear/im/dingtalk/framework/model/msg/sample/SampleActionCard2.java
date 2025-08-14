package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.ActionCard2;

/**
 * @author ycf
 **/
public class SampleActionCard2 {

    private final String msgKey = "sampleActionCard2";

    private ActionCard2 msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public ActionCard2 getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(ActionCard2 msgParam) {
        this.msgParam = msgParam;
    }
}
