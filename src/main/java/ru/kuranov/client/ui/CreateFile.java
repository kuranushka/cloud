package ru.kuranov.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import ru.kuranov.client.handler.ClientMessageHandler;
import ru.kuranov.client.handler.OnMessageReceived;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateFile implements Initializable {
    public Label createLabel;
    public TextField createTextField;
    public Button createButton;
    private ClientMessageHandler handler;
    private OnMessageReceived callback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = ClientMessageHandler.getInstance(callback);
    }

    public void create(ActionEvent event) {
        handler.createFile(createTextField.getText());
        Window window = ((Node)(event.getSource())).getScene().getWindow();
        window.hide();
    }
}
