package ru.kuranov.client.ui;

import com.sun.org.apache.xml.internal.security.Init;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    public Button up;
    public Button open;
    public Button create;
    public Button send;
    public Button recieve;
    public Button rename;
    public Button delete;
    public ListView<String> home;
    public TextField homeLine;
    public ListView<String> server;
    public TextField serverLine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
