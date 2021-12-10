package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kuranov.client.handler.Command;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CommandMessage extends AbstractMessage {
    private String file;
    private Command command;
}
