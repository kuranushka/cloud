package ru.kuranov.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.auth.Authentication;
import ru.kuranov.client.msg.AbstractMessage;
import ru.kuranov.client.msg.AuthMessage;
import ru.kuranov.client.msg.FileListMessage;
import ru.kuranov.client.msg.FileTransferMessage;
import ru.kuranov.client.net.NettyClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
public class ClientMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private static ClientMessageHandler instance;
    private OnMessageReceived callback;
    private NettyClient nettyClient;
    private AbstractMessage msg;
    private String[] serverFiles;

    private ClientMessageHandler(OnMessageReceived callback) {
        this.callback = callback;
        nettyClient = NettyClient.getInstance(System.out::println);
    }

    public static ClientMessageHandler getInstance(OnMessageReceived callback) {
        if (instance == null) {
            instance = new ClientMessageHandler(callback);
            return instance;
        } else {
            return instance;
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) {
        auth(msg);
        readServerFileList(msg);
    }

    private void readServerFileList(AbstractMessage msg){
        if(msg.getClass() == FileListMessage.class) {
            serverFiles = ((FileListMessage) msg).getFiles();
        }
    }

    public String[] getServerFiles(){
        return serverFiles;
    }


    private void auth(AbstractMessage msg) {
        if (msg.getClass() == AuthMessage.class && ((AuthMessage) msg).isAuth()) {
            Authentication.setAuth(true);
            System.out.println("clientHandler__" + Authentication.isAuth());
            this.callback.onReceive(msg);
        }
    }

    public void sendFile(String s) {
        File file = new File(s);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file.getName());
        } catch (FileNotFoundException e) {
            log.debug("File not found ...", e);
        }
        byte[] buf = new byte[0];
        try {
            buf = new byte[fis.available()];
            fis.read(buf);
        } catch (IOException e) {
            log.debug("File not read or not buffered ...", e);
        }
        log.debug("Read file {}", file.getName());
        FileTransferMessage fileTransferMessage = new FileTransferMessage(file.getName(), Command.SEND, Direction.TRANSFER_TO_SERVER, buf);
        nettyClient.sendMessage(fileTransferMessage);
        log.debug("Send file {}", file.getName());
    }
}
