package ru.kuranov.client.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.handler.ClientMessageHandler;
import ru.kuranov.client.handler.OnMessageReceived;
import ru.kuranov.client.net.NettyClient;

import java.awt.*;
import java.io.File;
import java.io.IOException;
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

        showClientFiles();
        showServerFiles();

        // отправить файл на сервер
        buttonSendFile.setOnAction(event -> {
            handler.sendFile(selectedHomeFile);
            showServerFiles();
        });

        // принять файл с сервера
        buttonReceiveFile.setOnAction(event -> {
            handler.setRootDirectory(rootDirectory);
            log.debug("Catch {}", selectedServerFile);
            handler.getServerFile(selectedServerFile);
            showClientFiles();
        });
    }

    private void showServerFiles() {
        selectedHomeFile = null;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverFiles = handler.getServerFileList();
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

    private void showClientFiles() {
        selectedServerFile = null;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    }

    public void renameFile(ActionEvent event) throws IOException {
        log.debug("Home file: " + selectedHomeFile + " Server file: " + selectedServerFile);
        if (selectedHomeFile != null && selectedServerFile == null) {
            handler.setClientRenamedFile(selectedHomeFile);
            handler.setServerRenamedFile(null);
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(RenameFile.class.getResource("rename.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Rename " + selectedHomeFile);
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
        if (selectedHomeFile == null && selectedServerFile != null) {
            handler.setServerRenamedFile(selectedServerFile);
            handler.setClientRenamedFile(null);
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(RenameFile.class.getResource("rename.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Rename " + selectedServerFile);
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }

    }

    public void updateClientFilesList(MouseEvent mouseEvent) {
        showClientFiles();
    }

    public void updateServerFilesList(MouseEvent mouseEvent) {
        showServerFiles();
    }

    public void openFile(ActionEvent event) {
        if (selectedHomeFile != null && selectedServerFile == null) {
            try {
                Desktop.getDesktop().open(new File(selectedHomeFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (selectedHomeFile == null && selectedServerFile != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You can only open files from your computer");
            alert.show();
        }

    }

    public void createFile(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(CreateFile.class.getResource("create.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Create file");
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();

    }

    public void deleteFile(ActionEvent event) throws IOException {
        if (selectedHomeFile != null && selectedServerFile == null) {
            handler.setClientDeletedFile(selectedHomeFile);
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(DeleteFile.class.getResource("delete.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Delete " + selectedHomeFile + " ?");
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }

        if (selectedHomeFile == null && selectedServerFile != null) {
            handler.setServerDeletedFile(selectedServerFile);
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(DeleteFile.class.getResource("delete.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Delete " + selectedServerFile + " ?");
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
    }
}
