package ru.kuranov.client.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.handler.ClientMessageHandler;
import ru.kuranov.client.handler.OnMessageReceived;
import ru.kuranov.client.net.NettyClient;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Slf4j
@Data
public class Window implements Initializable {

    private static Window instance;
    public Button buttonUpDirectory;
    public Button buttonOpenFile;
    public Button buttonSendFile;
    public Button buttonReceiveFile;
    public Button buttonRenameFile;
    public Button buttonDeleteFile;
    public ListView<String> homeFileList;
    public ListView<String> serverFileList;
    public Button buttonCreateFile;
    public Label myComputerLabel;
    public Label cloudStorageLabel;
    private File rootDirectory;
    private String[] homeFiles;
    private String[] serverFiles;
    private String selectedHomeFile;
    private String selectedServerFile;
    private ObservableList<String> itemsHome;
    private ObservableList<String> itemsServer;
    private NettyClient nettyClient;
    private ClientMessageHandler handler;
    private OnMessageReceived callback;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        handler = ClientMessageHandler.getInstance(callback);
        rootDirectory = new File(".");
        homeFiles = rootDirectory.list();
        itemsHome = FXCollections.observableArrayList(homeFiles);
        homeFileList.setItems(itemsHome);
        homeFileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        log.debug("Files: {}", Arrays.toString(homeFiles));

        homeFileList.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends String> change) ->
                {
                    ObservableList<String> oList = homeFileList.getSelectionModel().getSelectedItems();
                    log.debug("ObservableList: {}", oList);

                    selectedHomeFile = oList.get(0);
                    log.debug("Selected files: {}", selectedHomeFile);
                });
        showServerFiles();

        buttonSendFile.setOnAction(event -> {
            handler.sendFile(selectedHomeFile);
            showServerFiles();
        });
    }

    private void showServerFiles(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("Try show server files");
        serverFiles = handler.getServerFiles();
        itemsServer = FXCollections.observableArrayList(serverFiles);
        serverFileList.setItems(itemsServer);
        serverFileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        log.debug("ServerFiles: {}", Arrays.toString(serverFiles));
        serverFileList.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends String> change) ->
                {
                    ObservableList<String> oList = serverFileList.getSelectionModel().getSelectedItems();
                    log.debug("Server ObservableList: {}", oList);

                    selectedServerFile = oList.get(0);
                    log.debug("Server Selected files: {}", selectedServerFile);
                });
    }
}
