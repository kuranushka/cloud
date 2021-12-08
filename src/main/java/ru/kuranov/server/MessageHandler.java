package ru.kuranov.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.msgtype.AbstractMessage;
import ru.kuranov.client.msgtype.AuthMessage;

@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private DBConnections connection;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        if (msg.getClass() == AuthMessage.class
                && !((AuthMessage) msg).isAuth()
                && !((AuthMessage) msg).isNewUser()) {
            connection = new AuthDB();
            ctx.writeAndFlush(connection.auth((AuthMessage) msg));

        }
        log.debug("Received {}", msg);
        ctx.writeAndFlush(msg);
    }
}
