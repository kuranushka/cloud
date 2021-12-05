package ru.kuranov.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.AbstractMessage;

@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        log.debug("Received {}", msg);
        ctx.writeAndFlush(msg);
    }
}
