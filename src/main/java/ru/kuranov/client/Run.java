package ru.kuranov.client;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kuranov.client.ui.AuthWindow;
import ru.kuranov.client.ui.MainWindow;

import java.io.IOException;

public class Run extends Application {
    private AuthWindow auth;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        showAuth();
    }

    public static void main(String[] args) {
        new Thread(() -> {
            Client client = new Client();
        }).start();
        launch();

    }

    private void showAuth() throws IOException {
        Stage stage = new Stage();
        Parent parent = FXMLLoader.load(AuthWindow.class.getResource("auth.fxml"));
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        auth = new AuthWindow();


    }
}
