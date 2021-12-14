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
import ru.kuranov.client.msg.*;

@Slf4j
public class NettyClient {
    static OnMessageReceived callback;
    static NettyClient instance;
    SocketChannel channel;

    private NettyClient(OnMessageReceived callback) {
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
                                        //new ClientMessageHandler(callback),
                                        ClientMessageHandler.getInstance(callback)
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

    public static NettyClient getInstance(OnMessageReceived callback) {
        if (instance == null) {
            instance = new NettyClient(callback);
            return instance;
        } else {
            return instance;
        }
    }

    public void sendMessage(FileReceiveMessage message) {
        channel.writeAndFlush(message);
    }

    public void sendMessage(AuthMessage message) {
        channel.writeAndFlush(message);
    }

    public void sendMessage(FileSendMessage message) {
        channel.writeAndFlush(message);
    }

    public void sendMessage(FileServerRenameMessage message) {
        channel.writeAndFlush(message);
    }

    public void sendMessage(FileServerCreate message) {
        channel.writeAndFlush(message);
    }

    public void sendMessage(FileServerDelete message) {
        channel.writeAndFlush(message);
    }



    public void sendMessageNew(Message message) {
    }
}
