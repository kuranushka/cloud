package ru.kuranov.client.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("CLoud Storage");
        stage.setScene(scene);
        stage.show();
    }
}
