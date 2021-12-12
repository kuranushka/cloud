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
    private String clientDeleteFile;
    private String serverDeleteFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = ClientMessageHandler.getInstance(callback);
        clientDeleteFile = handler.getClientDeletedFile();
        serverDeleteFile = handler.getServerDeletedFile();
        if (clientDeleteFile != null && serverDeleteFile == null) {
            deleteLabel.setText("Delete " + clientDeleteFile + " ?");
        }
        if (clientDeleteFile == null && serverDeleteFile != null) {
            deleteLabel.setText("Delete " + serverDeleteFile + " ?");
        }
    }

    public void yesDelete(ActionEvent event) {
        if (clientDeleteFile != null && serverDeleteFile == null) {
            handler.deleteClientFile(clientDeleteFile);
        }
        if (clientDeleteFile == null && serverDeleteFile != null) {
            handler.deleteServerFile(serverDeleteFile);
        }
        Window window = ((Node) event.getSource()).getScene().getWindow();
        window.hide();
    }

    public void noDelete(ActionEvent event) {
        Window window = ((Node) event.getSource()).getScene().getWindow();
        window.hide();
    }
}
