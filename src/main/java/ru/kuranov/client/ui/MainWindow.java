package ru.kuranov.client.ui;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;

@Data
public class MainWindow implements Initializable {

    public Button buttonUpDirectory;
    public Button buttonOpenFile;
    public Button buttonSendFile;
    public Button buttonReceiveFile;
    public Button buttonRenameFile;
    public Button buttonDeleteFile;
    public ListView<String> homeFileList;
    public TextField homeLine;
    public ListView<String> serverFileList;
    public TextField serverLine;
    public Button buttonCreateFile;
    public Label myComputerLabel;
    public Label cloudStorageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonCreateFile.setOnAction(event -> serverLine.setText("CATCH"));
    }
}
