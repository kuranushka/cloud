package ru.kuranov.client.handler;

import java.util.Arrays;

public enum Commands {
    LEVEL_UP(".."),
    OPEN("open"),
    CREATE("touch"),
    SEND("send"),
    RECEIVE("receive"),
    RENAME("rename"),
    DELETE("delete");

    private final String command;


    Commands(String command) {
        this.command = command;
    }

    public static Commands byCommand(String command) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.getCommand().equals(command))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Command not found"));
    }

    public String getCommand() {
        return command;
    }
}
