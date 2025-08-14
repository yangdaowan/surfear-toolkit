package io.github.yangdaowan.surfear.core.spi.message;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;

/**
 * @author ycf
 **/
@Getter
public class MessageResult {

    private final JSONObject data;
    private final boolean success;
    private final String message;
    @JSONField(serialize = false, deserialize = false)
    private final Throwable error;
    private final String messageId;
    private final long duration;

    public MessageResult(JSONObject data, boolean success, String message, Throwable error, String messageId, long startTime) {
        this.data = data;
        this.success = success;
        this.message = message;
        this.error = error;
        this.messageId = messageId;

        this.duration = System.currentTimeMillis() - startTime;;
    }

    public static MessageResult success(MessageContext context) {
        return success(context, null);
    }

    public static  MessageResult success(MessageContext context, JSONObject data) {
        return new MessageResult(data, true, "ok", null, context.getMessageId(), context.getStartTime());
    }

    public static  MessageResult error(MessageContext context, Throwable error) {
        return new MessageResult(null,false, error.getMessage(), error, context.getMessageId(), context.getStartTime());
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
