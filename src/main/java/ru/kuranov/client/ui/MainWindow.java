package ru.kuranov.client.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.ClientStart;
import ru.kuranov.client.handler.Command;
import ru.kuranov.client.msg.CommandMessage;
import ru.kuranov.client.net.NettyClient;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Slf4j
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

    private File rootDirectory;
    private String[] files;
    private String selectedFile;
    private ObservableList<String> items;
    private NettyClient nettyClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rootDirectory = new File("src/main/resources/ru/myComputer");
        files = rootDirectory.list();
        //selectedFile = new HashSet<>();
        items = FXCollections.observableArrayList(files);
        homeFileList.setItems(items);
        homeFileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        log.debug("Files: {}", Arrays.toString(files));


        homeFileList.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends String> change) ->
                {
                    ObservableList<String> oList = homeFileList.getSelectionModel().getSelectedItems();
                    log.debug("ObservableList: {}", oList);

                    selectedFile = oList.get(0);
                    log.debug("Selected files: {}", selectedFile);
                });


        buttonSendFile.setOnAction(event -> {
            if (!selectedFile.isEmpty()) {
                CommandMessage commandMessage = new CommandMessage(selectedFile, Command.SEND);
                nettyClient = ClientStart.getNettyClient();
                nettyClient.sendMessage(commandMessage);
            }
        });
    }
}
