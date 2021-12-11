package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FileReceiveMessage extends AbstractMessage {
    private String file;
}
