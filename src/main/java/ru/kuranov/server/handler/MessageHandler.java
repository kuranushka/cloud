package ru.kuranov.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

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
            if (msg.getClass() == AuthMessage.class) {
                connection = AuthDB.getInstance();
                isAuth = connection.auth((AuthMessage) msg);
                if (!isAuth) {
                    return;
                }
                path = Paths.get("." + ((AuthMessage) msg).getUser());
                file = new File("." + ((AuthMessage) msg).getUser());


                // создание папки пользователя
                if (!file.exists()) {
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
            }
        }


        // приём файла
        if (((Message)msg).getCommand() == Command.UPLOAD) {
            receiveFile(ctx, msg);
        }

        // отправка файла
        if (((Message)msg).getCommand() == Command.DOWNLOAD) {
            sendFile(ctx, msg);
        }

        // переименование папки, файла
        if (((Message)msg).getCommand() == Command.RENAME) {
            renameFile(msg);
        }

        // создание файла
        if (((Message)msg).getCommand() == Command.NEW_FILE) {
            createFile(msg);
        }

        // создание папки
        if (((Message)msg).getCommand() == Command.NEW_DIRECTORY) {
            createDirectory(msg);
        }

        // удаление папки, файла
        if (((Message)msg).getCommand() == Command.DELETE) {
            deleteFile(msg);
        }

        sendListFiles(ctx);
    }

    // сохраняем файл от клиента
    private void receiveFile(ChannelHandlerContext ctx, AbstractMessage msg) {
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


    private void createDirectory(AbstractMessage msg) {
    }

    private void deleteFile(AbstractMessage msg) {
        try {
            Files.deleteIfExists(Paths.get(directory.toFile() + "/" + ((FileServerDelete) msg).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renameFile(AbstractMessage msg) {
        String oldName = ((FileServerRenameMessage) msg).getOldName();
        String newName = ((FileServerRenameMessage) msg).getNewName();
        try {
            Files.move(Paths.get(directory.toFile() + "/" + oldName), Paths.get(directory.toFile() + "/" + newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFile(AbstractMessage msg) {
        try {
            if (((FileServerCreate) msg).isFile()) {
                Files.createFile(Paths.get(directory.toFile() + "/" + ((FileServerCreate) msg).getFile()));
            } else {
                Files.createDirectory(Paths.get(directory.toFile() + "/" + ((FileServerCreate) msg).getFile()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // отправляем файл клиенту
    private void sendFile(ChannelHandlerContext ctx, AbstractMessage msg) {
        String fileName = ((FileReceiveMessage) msg).getFile();
        log.debug("Send {} to client", fileName);
        try {
            FileInputStream fis = new FileInputStream(directory.toFile() + "/" + fileName);
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            FileSendMessage message = new FileSendMessage(fileName, buf);
            ctx.writeAndFlush(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    // отправить клиенту список файлов из его папки
    private void sendListFiles(ChannelHandlerContext ctx) {
        String[] files = file.list();
        Optional<String[]> optList = Optional.ofNullable(files);
        FileListMessage msg = new FileListMessage(optList);
        ctx.writeAndFlush(msg);
        log.debug("Dir {} files: {}", directory + "/", Arrays.toString(files));
    }
}
