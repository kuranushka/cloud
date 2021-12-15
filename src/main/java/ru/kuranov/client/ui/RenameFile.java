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

public class RenameFile implements Initializable {
    public Label renameLabel;
    public TextField renameField;
    public Button renameButton;
    private ClientMessageHandler handler;
    private OnMessageReceived callback;
    private String clientOldName;
    private String serverOldName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = ClientMessageHandler.getInstance(callback);
        clientOldName = handler.getClientRenamedFile();
        serverOldName = handler.getServerRenamedFile();
        if (clientOldName != null && serverOldName == null) {
            renameLabel.setText("Rename " + handler.getClientRenamedFile() + " on computer?");
            renameField.setText(clientOldName);
        }
        if (clientOldName == null && serverOldName != null) {
            renameLabel.setText("Rename " + handler.getServerRenamedFile() + " on server?");
            renameField.setText(serverOldName);
        }
    }

    /*public void rename(ActionEvent event) {
        if (clientOldName != null && serverOldName == null) {
            handler.renameClientFile(clientOldName, renameField.getText());
        }

        if (clientOldName == null && serverOldName != null) {
            handler.renameServerFile(serverOldName, renameField.getText());
        }
        Window window = ((Node) event.getSource()).getScene().getWindow();
        window.hide();
    }*/
}
