package ru.kuranov.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kuranov.client.msgtype.AuthMessage;
import ru.kuranov.client.ui.AuthWindow;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    public Client() {
        NettyClient netty = new NettyClient(System.out::println);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String user = scanner.nextLine();
            String password = scanner.nextLine();
            netty.sendMessage(new AuthMessage(false, false, user, password));
        }
    }
}

