package io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.sample;

import io.github.yangdaowan.surfear.im.dingtalk.framework.model.msg.msgtype.File;

/**
 * @author ycf
 **/
public class SampleFile {

    private final String msgKey = "sampleFile";

    private File msgParam;

    public String getMsgKey() {
        return msgKey;
    }

    public File getMsgParam() {
        return msgParam;
    }

    public void setMsgParam(File msgParam) {
        this.msgParam = msgParam;
    }
}
