package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileServerDelete extends AbstractMessage {
    private String file;
}
