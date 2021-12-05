package ru.kuranov.client;

import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient(System.out::println);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            nettyClient.sendMsg(msg);
        }
    }
}
