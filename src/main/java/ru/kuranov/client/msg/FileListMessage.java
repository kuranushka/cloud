package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileListMessage extends AbstractMessage {
    private String[] files;
}
