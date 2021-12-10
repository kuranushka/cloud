package ru.kuranov.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.msg.AbstractMessage;
import ru.kuranov.client.msg.AuthMessage;
import ru.kuranov.server.auth.AuthDB;
import ru.kuranov.server.auth.DBConnections;

@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private DBConnections connection;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) {

        // авторизация
        if (msg.getClass() == AuthMessage.class
                && !((AuthMessage) msg).isAuth()
                && !((AuthMessage) msg).isNewUser()) {
            connection = AuthDB.getInstance();
            ctx.writeAndFlush(connection.auth((AuthMessage) msg));

        }

        // приём файлов




        log.debug("Received {}", msg);
        ctx.writeAndFlush(msg);


    }
}
