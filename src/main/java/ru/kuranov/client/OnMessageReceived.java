package ru.kuranov.client;

import ru.kuranov.client.msgtype.AbstractMessage;

public interface OnMessageReceived {
    void onReceive(AbstractMessage msg);
}
