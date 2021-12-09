package ru.kuranov.client.handler;

import ru.kuranov.client.net.NettyClient;

import java.io.File;
import java.util.List;

public class CommandHandler {
    private NettyClient nettyClient;

    public CommandHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;

    }
}
