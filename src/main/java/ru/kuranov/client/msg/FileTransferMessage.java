package ru.kuranov.client.msg;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kuranov.client.handler.Command;
import ru.kuranov.client.handler.Direction;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FileTransferMessage extends AbstractMessage {
    private String file;
    private Command command;
    private Direction direction;
    private byte[] byf;
}
