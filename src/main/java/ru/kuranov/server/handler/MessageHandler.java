package ru.kuranov.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.handler.Command;
import ru.kuranov.client.handler.Direction;
import ru.kuranov.client.msg.*;
import ru.kuranov.server.auth.AuthDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private AuthDB connection;
    private Path directory;
    private File file;
    private Path path;
    private boolean isAuth;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) {

        if (!isAuth) {
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
                } else {
                    directory = Paths.get("." + ((AuthMessage) msg).getUser());
                }
                // возвращаем авторизацию клиенту
                ctx.writeAndFlush(msg);
                //ctx.writeAndFlush(connection.auth((AuthMessage) msg));
            }
        }


        // приём файла
        if (isAuth
                && msg.getClass() == FileSendMessage.class
                && (((FileSendMessage) msg).getCommand() == Command.SEND)) {
            receiveFile(ctx, msg);
        }

        if (isAuth && msg.getClass() == FileReceiveMessage.class) {
            sendFile(ctx, msg);
        }

        if (isAuth && msg.getClass() == FileServerRenameMessage.class) {
            renameFile(ctx, msg);
        }


        log.debug("Received {}", msg);
        sendListFiles(ctx);
    }

    private void renameFile(ChannelHandlerContext ctx, AbstractMessage msg) {
        String oldName = ((FileServerRenameMessage) msg).getOldName();
        String newName = ((FileServerRenameMessage) msg).getNewName();
        File oldFile = new File(directory.toFile() + "/" + oldName);
        oldFile.renameTo(new File(directory.toFile() + "/" + newName));

    }


    // отправляем файл клиенту
    private void sendFile(ChannelHandlerContext ctx, AbstractMessage msg) {
        String fileName = ((FileReceiveMessage) msg).getFile();
        log.debug("Send {} to client", fileName);
        try {
            FileInputStream fis = new FileInputStream(directory.toFile() + "/" + fileName);
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            FileSendMessage message = new FileSendMessage(fileName, Command.SEND, Direction.TRANSFER_FROM_SERVER, buf);
            ctx.writeAndFlush(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // сохраняем файл от клиента
    private void receiveFile(ChannelHandlerContext ctx, AbstractMessage msg) {
        log.debug("Place to save {}", directory.toString());
        try {
            FileOutputStream fos = new FileOutputStream(directory.toFile() + "/" + ((FileSendMessage) msg).getFile());
            byte[] buf = ((FileSendMessage) msg).getByf();
            fos.write(buf);
            log.debug("Files {} saved to {}", ((FileSendMessage) msg).getFile(), directory.toFile());
            sendListFiles(ctx);
        } catch (IOException e) {
            log.debug("File not saved in user directory");
        }
    }

    // отправить клиенту список файлов из его папки
    private void sendListFiles(ChannelHandlerContext ctx) {
        String[] files = file.list();
        FileListMessage msg = new FileListMessage(files);
        ctx.writeAndFlush(msg);
        log.debug("Dir {} files: {}", directory + "/", Arrays.toString(files));
    }
}
