package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kuranov.client.handler.Command;

import java.io.File;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CommandMessage extends AbstractMessage{
    private Set<File> fileSet;
    private Command command;
}
