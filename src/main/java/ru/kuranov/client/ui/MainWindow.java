package ru.kuranov.client.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Data;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

@Data
public class MainWindow implements Initializable {

    public Button buttonUpDirectory;
    public Button buttonOpenFile;
    public Button buttonSendFile;
    public Button buttonReceiveFile;
    public Button buttonRenameFile;
    public Button buttonDeleteFile;
    public ListView<File> homeFileList;
    public TextField homeLine;
    public ListView<File> serverFileList;
    public TextField serverLine;
    public Button buttonCreateFile;
    public Label myComputerLabel;
    public Label cloudStorageLabel;

    private File rootDirectory;
    private File[] filesDirectory;
    private Set<File> selectedList;
    private ObservableList<File> items;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootDirectory = new File("src/main/resources/ru/my computer");
        filesDirectory = rootDirectory.listFiles();
        selectedList = new HashSet<>();
        items = FXCollections.observableArrayList(filesDirectory);
        homeFileList.setItems(items);
        homeFileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        homeFileList.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends File> change) ->
                {
                    ObservableList<File> oList = homeFileList.getSelectionModel().getSelectedItems();
                    selectedList.addAll(oList);
                });
        buttonCreateFile.setOnAction(event -> serverLine.setText("CATCH"));
    }
}
