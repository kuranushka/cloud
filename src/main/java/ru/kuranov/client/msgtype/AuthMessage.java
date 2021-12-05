package ru.kuranov.client.msgtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kuranov.client.AbstractMessage;

@Data
@AllArgsConstructor
public class AuthMessage extends AbstractMessage {
    private boolean isNewUser;
    private boolean isAuth;
    private String user;
    private String password;
}
