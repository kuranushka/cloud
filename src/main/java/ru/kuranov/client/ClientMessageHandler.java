package ru.kuranov.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.kuranov.client.msgtype.AbstractMessage;
import ru.kuranov.client.msgtype.AuthMessage;

public class ClientMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private OnMessageReceived callback;
    private AuthMessage authMessage;

    public ClientMessageHandler(OnMessageReceived callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {



        if (msg.getClass() == AuthMessage.class && ((AuthMessage) msg).isAuth()) {

            authMessage = AuthMessage.simpleAuthMessage();
            authMessage.setAuth(((AuthMessage) msg).isAuth());

            System.out.println(""+((AuthMessage) msg).isAuth());

            callback.onReceive(msg);
        }
    }
}
