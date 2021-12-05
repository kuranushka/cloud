package ru.kuranov.server;

public class ServerStart {
    public static void main(String[] args) {
        DBhandler dBhandler = new DBhandler();
        NettyServer nettyServer = new NettyServer();
    }
}
