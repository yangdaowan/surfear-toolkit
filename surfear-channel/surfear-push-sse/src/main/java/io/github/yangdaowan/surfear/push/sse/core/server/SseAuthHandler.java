package io.github.yangdaowan.surfear.push.sse.core.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author ycf
 **/
public interface SseAuthHandler {

    boolean authenticate(ChannelHandlerContext ctx, FullHttpRequest request, String auth);
}
