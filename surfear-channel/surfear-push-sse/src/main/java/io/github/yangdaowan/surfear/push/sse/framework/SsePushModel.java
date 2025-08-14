package io.github.yangdaowan.surfear.push.sse.framework;

import io.github.yangdaowan.surfear.core.spi.message.Message;
import io.github.yangdaowan.surfear.core.interceptor.support.required.Required;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ycf
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SsePushModel extends Message {
    /**
     * 接收人
     */
    @Required
    private String to;
    /**
     * 内容
     */
    @Required
    private String content;
    /**
     * 推送类型
     */
    @Required
    private PushType type;
    /**
     * 推送事件
     */
    @Required
    private String event;

    @Override
    public void setContent(String content) {
        this.content = content;
        super.setContent(content);
    }

}
