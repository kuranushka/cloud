package ru.kuranov.client.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.kuranov.client.net.NettyClient;

public class AppStarter extends Application {

    private NettyClient netty;

    @Override
    public void start(Stage primaryStage) throws Exception {
        netty = NettyClient.getInstance(System.out::println);
        Parent root = FXMLLoader.load(getClass().getResource("auth.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
