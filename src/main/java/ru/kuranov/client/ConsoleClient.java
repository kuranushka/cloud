package ru.kuranov.client;

import ru.kuranov.client.msgtype.AuthMessage;
import ru.kuranov.client.msgtype.StringMessage;

import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleClient {
    public static void main(String[] args) {
        NettyClient netty = new NettyClient(System.out::println);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String user = scanner.nextLine();
            String password = scanner.nextLine();
            netty.sendMessage(new AuthMessage(false, false, user, password));
        }
    }
}
