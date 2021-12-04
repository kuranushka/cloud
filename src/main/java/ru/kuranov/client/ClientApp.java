package ru.kuranov.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader client = new FXMLLoader(Client.class.getResource("client.fxml"));
        FXMLLoader welcome = new FXMLLoader(Welcome.class.getResource("welcome.fxml"));
        showWelcome(welcome, stage);
        //showClient(client, stage);

    }

    private void showClient(FXMLLoader f, Stage s) throws IOException {
        Scene clientScene = new Scene(f.load());
        s.setScene(clientScene);
        s.show();
    }


    private void showWelcome(FXMLLoader f, Stage s) throws IOException {
        Scene welcomeScene = new Scene(f.load());
        s.setScene(welcomeScene);
        s.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
