package io.github.yangdaowan.surfear.push.sse.framework;

import com.google.auto.service.AutoService;
import io.github.yangdaowan.surfear.core.spi.channel.AbsMessageChannel;
import io.github.yangdaowan.surfear.core.spi.channel.Channel;
import io.github.yangdaowan.surfear.core.spi.channel.MessageChannel;
import io.github.yangdaowan.surfear.core.spi.message.MessageContext;
import io.github.yangdaowan.surfear.core.spi.message.MessageResult;
import io.github.yangdaowan.surfear.core.spi.message.MessageType;
import io.github.yangdaowan.surfear.push.sse.core.server.SseConnectionManager;
import io.github.yangdaowan.surfear.push.sse.core.server.SseNettyServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoService(MessageChannel.class)
@Channel(
        type = MessageType.PUSH,
        name = "sse",
        description = "SSE协议消息",
        model = SsePushModel.class,
        exception = SseException.class
)
public class SseChannel extends AbsMessageChannel {

    @Override
    public MessageResult send(MessageContext context) {
        if(SseNettyServer.getInstance() == null || !SseNettyServer.getInstance().isRunning()){
            throw new SseException("SSE服务器未启动，请先启动SSE服务器");
        }

        SsePushModel model = (SsePushModel) context.getModel();

        switch (model.getType()) {
            case TARGET:
                SseConnectionManager.getInstance()
                        .sendToClient(model.getTo(), model.getEvent(), model.getContent());
                break;
            case GROUP:
                SseConnectionManager.getInstance()
                        .sendToGroup(model.getTo(), model.getEvent(), model.getContent());
                break;
            case BROADCAST:
                SseConnectionManager.getInstance()
                        .broadcast(model.getEvent(), model.getContent());
                break;
        }

        return MessageResult.success(context);
    }


}

