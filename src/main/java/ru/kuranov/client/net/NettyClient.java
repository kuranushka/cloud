package ru.kuranov.client.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.handler.ClientMessageHandler;
import ru.kuranov.client.handler.OnMessageReceived;
import ru.kuranov.client.msg.AuthMessage;
import ru.kuranov.client.msg.StringMessage;

@Slf4j
public class NettyClient {
    static OnMessageReceived callback;
    SocketChannel channel;

    public NettyClient(OnMessageReceived callback) {
        NettyClient.callback = callback;
        new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                ChannelFuture future = bootstrap.channel(NioSocketChannel.class)
                        .group(group)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                channel = ch;
                                ch.pipeline().addLast(
                                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
                                        new ClientMessageHandler(callback)
                                );
                            }
                        }).connect("localhost", 8189).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                log.error("e=", e);
            } finally {
                group.shutdownGracefully();
            }
        }).start();
    }

    public void sendMessage(StringMessage stringMessage) {
        channel.writeAndFlush(stringMessage);
    }

    public void sendMessage(AuthMessage authMessage) {
        channel.writeAndFlush(authMessage);
    }
}
