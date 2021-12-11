package ru.kuranov.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Window;
import ru.kuranov.client.handler.ClientMessageHandler;
import ru.kuranov.client.handler.OnMessageReceived;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteFile implements Initializable {
    public Label deleteLabel;
    public Button deleteButtonYes;
    public Button deleteButtonNo;
    private ClientMessageHandler handler;
    private OnMessageReceived callback;
    private String file;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = ClientMessageHandler.getInstance(callback);
        file = handler.getClientDeletedFile();
        deleteLabel.setText("Delete " + file + " ?");
    }

    public void yesDelete(ActionEvent event) {
        handler.deleteFile(file);
        Window window = ((Node) event.getSource()).getScene().getWindow();
        window.hide();
    }

    public void noDelete(ActionEvent event) {
        Window window = ((Node) event.getSource()).getScene().getWindow();
        window.hide();
    }
}
