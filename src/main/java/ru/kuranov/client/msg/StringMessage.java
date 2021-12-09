package ru.kuranov.client.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class StringMessage extends AbstractMessage {
    private String content;
    private LocalDate time;
}
