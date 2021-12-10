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
import ru.kuranov.client.handler.Direction;
import ru.kuranov.client.msg.FileTransferMessage;
import ru.kuranov.client.net.NettyClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

        rootDirectory = new File(".");
        files = rootDirectory.list();
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
            nettyClient = ClientStart.getNettyClient();
            File file = new File(selectedFile);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file.getName());
            } catch (FileNotFoundException e) {
                log.debug("File not found ... {}", e);
            }
            byte[] buf = new byte[0];
            try {
                buf = new byte[fis.available()];
                fis.read(buf);
            } catch (IOException e) {
                log.debug("File not read or not buffered ... {}", e);
            }
            log.debug("Read file {}", file.getName());

            FileTransferMessage fileTransferMessage = new FileTransferMessage(file.getName(), Command.SEND, Direction.TRANSFER_TO_SERVER, buf);
            nettyClient.sendMessage(fileTransferMessage);
            log.debug("Send file {}", file.getName());
        });
    }
}
