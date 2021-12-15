package ru.kuranov.client.msg;

import lombok.Data;

@Data
public class Message extends AbstractMessage{
    private String fileName;
    private Command command;
    private byte[] buf;
    private String oldFileName;

    public Message(String fileName, Command command, byte[] buf) {
        this.fileName = fileName;
        this.command = command;
        this.buf = buf;
    }
    public Message(String fileName, Command command) {
        this.fileName = fileName;
        this.command = command;
    }

    public Message(String fileName, String oldFileName, Command command) {
        this.fileName = fileName;
        this.command = command;
        this.oldFileName = oldFileName;
    }
}
