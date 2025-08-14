package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.ActionCard;

/**
 * @author ycf
 **/
public class SampleActionCard {

    private final String msgKey = "sampleActionCard";
    private final String msgtype = "sampleActionCard";

    private ActionCard msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public ActionCard getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(ActionCard msgParam) {
        this.msgParam = msgParam;
    }
}
