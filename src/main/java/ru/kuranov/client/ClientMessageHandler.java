package ru.kuranov.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private OnMessageReceived callback;

    public ClientMessageHandler(OnMessageReceived callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        callback.onReceive(msg);
    }
}
