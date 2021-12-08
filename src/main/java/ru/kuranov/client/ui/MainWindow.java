package ru.kuranov.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;
@Data
public class MainWindow implements Initializable {

    public Button up;
    public Button open;
    public Button send;
    public Button recieve;
    public Button rename;
    public Button delete;
    public ListView<String> home;
    public TextField homeLine;
    public ListView<String> server;
    public TextField serverLine;
    public Button createFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                serverLine.setText("CATCH");
            }
        });
    }
}
