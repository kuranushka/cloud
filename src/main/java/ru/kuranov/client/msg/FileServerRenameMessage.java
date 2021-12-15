package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileServerRenameMessage extends AbstractMessage {
    private String oldName;
    private String newName;
}
