package ru.kuranov.client.msgtype;

import lombok.Data;

@Data
public class AuthMessage extends AbstractMessage {
    private static AuthMessage authMessage;
    private boolean isNewUser;
    private boolean isAuth;
    private String user;
    private String password;

    private AuthMessage(boolean isNewUser, boolean isAuth, String user, String password) {
        this.isNewUser = isNewUser;
        this.isAuth = isAuth;
        this.user = user;
        this.password = password;
    }

    public static AuthMessage getAuthMessage(boolean isNewUser, boolean isAuth, String user, String password) {
        if (authMessage == null) {
            System.out.println("try to create new AutMessage");
            AuthMessage au = new AuthMessage(isNewUser, isAuth, user, password);
            return au;
        }
        return authMessage;
    }

    public static AuthMessage simpleAuthMessage(){
        return authMessage;
    }
}
