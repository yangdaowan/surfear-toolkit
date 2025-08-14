package io.github.yangdaowan.surfear.core.spi.channel;

import io.github.yangdaowan.surfear.core.exception.ChannelLoadException;

/**
 * @author ycf
 **/
public abstract class AbsMessageChannel implements MessageChannel {

    public final ChannelMetadata metadata;

    public AbsMessageChannel() {
        Channel annotation = this.getClass().getAnnotation(Channel.class);
        if(annotation == null){
            throw new ChannelLoadException("通道注解为空："+ this.getClass().getName());
        }
        this.metadata = new ChannelMetadata(annotation);
    }

    public ChannelMetadata getMetadata() {
        return metadata;
    }

}
