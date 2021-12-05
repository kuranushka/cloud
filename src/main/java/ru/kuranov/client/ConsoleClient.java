package ru.kuranov.client;

import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleClient {
    public static void main(String[] args) {
        NettyClient netty = new NettyClient(System.out::println);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            netty.sendMessage(new StringMessage(msg, LocalDate.now()));
        }
    }
}
