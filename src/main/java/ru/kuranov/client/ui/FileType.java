package ru.kuranov.client.ui;

public enum FileType {
    FILE("FILE"),
    DIRECTORY("DIR");

    private final String name;

    FileType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
