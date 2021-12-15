package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FileSendMessage extends AbstractMessage {
    private String file;
    private byte[] byf;
}
