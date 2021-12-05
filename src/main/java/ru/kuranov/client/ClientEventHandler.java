package ru.kuranov.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientEventHandler extends SimpleChannelInboundHandler<String> {

    private final Callback callback;

    public ClientEventHandler(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        callback.receive(s);
    }
}
