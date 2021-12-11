package ru.kuranov.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.auth.Authentication;
import ru.kuranov.client.msg.*;
import ru.kuranov.client.net.NettyClient;

import java.io.*;

@Slf4j
@Data
public class ClientMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private static ClientMessageHandler instance;
    private final OnMessageReceived callback;
    private final NettyClient netty;
    private AbstractMessage msg;
    private String[] serverFiles;
    private File rootDirectory;
    private String clientRenamedFile;
    private String serverRenamedFile;

    private ClientMessageHandler(OnMessageReceived callback) {
        this.callback = callback;
        netty = NettyClient.getInstance(System.out::println);
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

        if (msg.getClass() == FileSendMessage.class) {
            saveFile(msg);
        }
    }

    private void readServerFileList(AbstractMessage msg) {
        if (msg.getClass() == FileListMessage.class) {
            serverFiles = ((FileListMessage) msg).getFiles();
        }
    }

    public String[] getServerFileList() {
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
        FileSendMessage fileSendMessage = new FileSendMessage(file.getName(), Command.SEND, Direction.TRANSFER_TO_SERVER, buf);
        netty.sendMessage(fileSendMessage);
        log.debug("Send file {}", file.getName());
    }

    public void getServerFile(String file) {
        FileReceiveMessage message = new FileReceiveMessage(file);
        netty.sendMessage(message);
    }

    public void saveFile(AbstractMessage msg) {
        File file = new File(rootDirectory.toString() + "/" + ((FileSendMessage) msg).getFile());
        log.debug("Try to save file {} in {}", file, rootDirectory.toString());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = ((FileSendMessage) msg).getByf();
            fos.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("File {} saved", file);
    }

    public void renameClientFile(String oldName, String newName) {
        File oldFile = new File(oldName);
        oldFile.renameTo(new File(newName));
    }

    public void renameServerFile(String oldName, String newName) {
        FileServerRenameMessage message = new FileServerRenameMessage(oldName,newName);
        netty.sendMessage(message);
    }
}
