package ru.kuranov.client.handler;

import ru.kuranov.client.msg.AbstractMessage;

public interface OnMessageReceived {
    void onReceive(AbstractMessage msg);
}
