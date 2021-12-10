package ru.kuranov.client.handler;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.auth.Authentication;
import ru.kuranov.client.msg.AbstractMessage;
import ru.kuranov.client.msg.AuthMessage;
import ru.kuranov.client.msg.FileTransferMessage;
import ru.kuranov.client.net.NettyClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
@Slf4j
public class Controller {
    private static Controller instance;
    private OnMessageReceived callback;
    private NettyClient nettyClient;

    private Controller Controller(){
        return instance;
    }

    private Controller(ChannelHandlerContext ctx, AbstractMessage msg, OnMessageReceived callback) {
        this.callback = callback;
        nettyClient = NettyClient.getInstance(System.out::println);
        auth(msg);



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



    private void auth(AbstractMessage msg){
        if (msg.getClass() == AuthMessage.class && ((AuthMessage) msg).isAuth()) {
            Authentication.setAuth(true);
            System.out.println("clientHandler__" + Authentication.isAuth());
            this.callback.onReceive(msg);
        }
    }

    public static Controller getInstance(ChannelHandlerContext ctx, AbstractMessage msg, OnMessageReceived callback) {
        if (instance == null) {
            instance = new Controller(ctx, msg, callback);
            return instance;
        } else {
            return instance;
        }
    }
}
