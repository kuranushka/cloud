package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileServerCreate extends AbstractMessage {
    private String file;
    private boolean isFile;


    public boolean isFile() {
        return isFile;
    }
}
