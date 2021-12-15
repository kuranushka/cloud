package ru.kuranov.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.handler.ClientMessageHandler;
import ru.kuranov.client.handler.OnMessageReceived;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class CreateFile implements Initializable {
    public Label createLabel;
    public TextField createTextField;
    public Button createButton;
    public RadioButton createOnComp;
    public RadioButton createOnServer;
    public RadioButton fileOnCompButton;
    public RadioButton fileOnServerButton;
    private ClientMessageHandler handler;
    private OnMessageReceived callback;
    private boolean isCreateFile;
    private boolean isCreateOnComp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = ClientMessageHandler.getInstance(callback);
        createLabel.setText("To create a file on your computer enter the name");
    }

    /*public void create(ActionEvent event) {
        if (createTextField.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter name file ...");
            alert.show();
        }

        if (isCreateFile) {
            handler.setCreateFile(true);
        } else {
            handler.setCreateFile(false);
        }

        if (isCreateOnComp) {
            handler.createFileOnComp(createTextField.getText());
        } else {
            handler.createFileOnServer(createTextField.getText());
        }
        Window window = ((Node) (event.getSource())).getScene().getWindow();
        window.hide();
    }

    public void whereCreateFile(ActionEvent event) {
        ToggleGroup group = new ToggleGroup();
        createOnComp.setToggleGroup(group);
        createOnServer.setToggleGroup(group);
        if (createOnComp.isSelected()) {
            log.debug("Selected on comp");
            isCreateOnComp = true;
        } else {
            isCreateOnComp = false;
            log.debug("Seelected on server");
        }
    }

    public void whatToCreate(ActionEvent event) {
        ToggleGroup group = new ToggleGroup();
        fileOnCompButton.setToggleGroup(group);
        fileOnServerButton.setToggleGroup(group);
        if (fileOnCompButton.isSelected()) {
            isCreateFile = true;
            log.debug("Create file");
        }
        if (fileOnServerButton.isSelected()) {
            isCreateFile = false;
            log.debug("Create directory");
        }
    }*/
}
