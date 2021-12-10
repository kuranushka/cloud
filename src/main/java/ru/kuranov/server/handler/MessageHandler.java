package ru.kuranov.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.handler.Command;
import ru.kuranov.client.msg.AbstractMessage;
import ru.kuranov.client.msg.AuthMessage;
import ru.kuranov.client.msg.FileListMessage;
import ru.kuranov.client.msg.FileTransferMessage;
import ru.kuranov.server.auth.AuthDB;
import ru.kuranov.server.auth.DBConnections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private DBConnections connection;
    private Path directory;
    private File file;
    private Path path;
    private boolean isAuth;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) {

        // авторизация в базе данных
        if (msg.getClass() == AuthMessage.class
                && !((AuthMessage) msg).isAuth()
                && !((AuthMessage) msg).isNewUser()) {
            connection = AuthDB.getInstance();
            isAuth = connection.auth((AuthMessage) msg);
            path = Paths.get("." + ((AuthMessage) msg).getUser());
            file = new File("." + ((AuthMessage) msg).getUser());

            // создание папки пользователя
            if (isAuth && !file.exists()) {
                try {
                    directory = Files.createDirectory(path);
                } catch (IOException e) {
                    log.debug("Directory not create");
                }
                log.debug("Create user directory: {}", path);
            }

            // приём файла
            if (isAuth
                    && msg.getClass() == FileTransferMessage.class
                    && (((FileTransferMessage) msg).getCommand() == Command.SEND)) {
                receiveFile(ctx, msg);
            }
            ctx.writeAndFlush(connection.auth((AuthMessage) msg));
        }
        log.debug("Received {}", msg);
        ctx.writeAndFlush(msg);
        sendListFiles(ctx);
    }

    // сохраняем файл от клиента
    private void receiveFile(ChannelHandlerContext ctx, AbstractMessage msg) {
        try {
            FileOutputStream fos = new FileOutputStream(((FileTransferMessage) msg).getFile());
            byte[] buf = ((FileTransferMessage) msg).getByf();
            fos.write(buf);
            log.debug("Files {} saved", ((FileTransferMessage)msg).getFile());
            sendListFiles(ctx);
        } catch (IOException e) {
            log.debug("File not saved in user directory");
        }
    }

    // отправить клиенту список файлов из его папки
    private void sendListFiles(ChannelHandlerContext ctx) {
        log.debug("Try send listFiles from user directory ...");
        //file = new File(directory.toString());
        String[] files = file.list();
        FileListMessage msg = new FileListMessage(files);
        ctx.writeAndFlush(msg);
        log.debug("Send listFiles from user directory ...");
    }
}
