package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class FileListMessage extends AbstractMessage {
    private Optional<String[]> files;
}
