package ru.kuranov.client.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.handler.ClientMessageHandler;
import ru.kuranov.client.handler.OnMessageReceived;
import ru.kuranov.client.msg.Command;
import ru.kuranov.client.msg.Message;
import ru.kuranov.client.net.NettyClient;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
public class Window implements Initializable {

    /*    private static Window instance;
        public Button buttonUpDirectory;
        public Button buttonOpenFile;
        public Button buttonSendFile;
        public Button buttonReceiveFile;
        public Button buttonRenameFile;
        public Button buttonDeleteFile;*/
    public ListView<String> clientFileList;
    public ListView<String> serverFileList;
    //public Button buttonCreateFile;
    public Label myComputerLabel;
    public Label cloudStorageLabel;
    public Button clientLevelUpButton;
    //public TableView<FileInfo> clientTable;
    //private File rootDirectory;
    private String[] clientFiles;
    private String[] serverFiles;
    private String selectedHomeFile;
    private String selectedServerFile;
    private ObservableList<String> itemsClient;
    private ObservableList<String> itemsServer;
    private NettyClient netty;
    private ClientMessageHandler handler;
    private OnMessageReceived callback;
    private Path root;
    private boolean isSelectClientFile;
    private boolean isAuth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        netty = NettyClient.getInstance(System.out::println);
        root = Paths.get(System.getProperty("user.home"));
        handler = ClientMessageHandler.getInstance(callback);
        refreshClientFiles();


        //showServerFiles();
        selectedHomeFile = "CLIENT FILE";
        selectedServerFile = "SERVER FILE";
        isSelectClientFile = true;

        // двойной клик, навигация или открытие файла
        clientFileList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        if (isSelectClientFile) {
                            if (Files.isDirectory(Paths.get(root + "/" + convertString(selectedHomeFile)))) {
                                root = Paths.get(root + "/" + convertString(selectedHomeFile));
                                refreshClientFiles();
                            } else {
                                try {
                                    Desktop.getDesktop().open(new File(root + "/" + convertString(selectedHomeFile)));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
    }



    /*private void showServerFiles() {
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
    }*/

    // обновления отображения
    private void refreshClientFiles() {
        File file = new File(root.toString());
        List<String> dirs = Arrays.stream(file.list())
                .map(m -> new File(root.toString() + "\\" + m))
                .map(n -> {
                    if (n.isDirectory()) {
                        return "[Dir ]" + n;
                    } else {
                        return "[file]" + n + "\t\t" + convertTime(n.lastModified()) + " " + convertFileSize(n);
                    }
                })
                .sorted()
                .map(o -> o.substring(0, 6) + o.substring(o.lastIndexOf("\\") + 1))
                .peek(System.out::println)
                .collect(Collectors.toList());
        log.debug("Items in rootDir {}", dirs);

        //selectedServerFile = null;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        itemsClient = FXCollections.observableArrayList(dirs);
        clientFileList.setItems(itemsClient);
        clientFileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        clientFileList.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends String> change) ->
                {
                    ObservableList<String> oList = clientFileList.getSelectionModel().getSelectedItems();
                    log.debug("ObservableList: {}", oList);

                    selectedHomeFile = oList.get(0);
                    log.debug("Selected files: {}", selectedHomeFile);
                });
        updatePathLabel(root);
    }

    private void showClientFiles2() throws IOException {
        selectedServerFile = null;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientFiles = Files.list(root).toArray(String[]::new);
        itemsClient = FXCollections.observableArrayList(clientFiles);
        clientFileList.setItems(itemsClient);


        clientFileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        log.debug("Files: {}", Arrays.toString(clientFiles));

/*        homeFileList.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends String> change) ->
                {
                    ObservableList<String> oList = homeFileList.getSelectionModel().getSelectedItems();
                    log.debug("ObservableList: {}", oList);

                    selectedHomeFile = oList.get(0);
                    log.debug("Selected files: {}", selectedHomeFile);
                });*/
    }

    /*public void renameFile(ActionEvent event) throws IOException {
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
    }*/


    // создание файла , папки
    public void create(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("newFile");
        dialog.setTitle("create new file");
        dialog.setHeaderText("create ?");
        ComboBox comboBox = new ComboBox<String>();
        ObservableList<String> oList = FXCollections.observableArrayList();
        oList.addAll("File on Client", "Directory on Client", "File on Server", "Directory on Server");
        comboBox.setItems(oList);
        comboBox.getSelectionModel().selectFirst();
        dialog.setGraphic(comboBox);
        Optional<String> result = dialog.showAndWait();
        String entered = "";
        if (result.isPresent()) {
            entered = result.get();
        }
        switch ((String) comboBox.getValue()) {
            case "File on Client":
                try {
                    Files.createFile(Paths.get(root.toString() + "/" + entered));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            case "Directory on Client":
                try {
                    Files.createDirectory(Paths.get(root.toString() + "/" + entered));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            case "File on Server":
                netty.sendMessage(new Message(entered, Command.NEW_FILE));
            case "Directory on Server":
                netty.sendMessage(new Message(entered, Command.NEW_DIRECTORY));
        }
        refreshClientFiles();
    }

    // отправка файла
    public void upload(ActionEvent event) {
        if (isSelectClientFile) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("upload file");
            alert.setHeaderText("upload " + convertString(selectedHomeFile) + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        log.debug("File to upload {}", root + "/" + convertString(selectedHomeFile));
                        FileInputStream fis = new FileInputStream(root + "/" + convertString(selectedHomeFile));
                        byte[] buf = new byte[fis.available()];
                        fis.read(buf);
                        netty.sendMessage(new Message(convertString(selectedHomeFile), Command.UPLOAD, buf));
                        log.debug("Client file upload");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (result.get() == ButtonType.CANCEL) {
                    return;
                }
            }
        } else {
            return;
        }
    }

    // получение файла
    public void download(ActionEvent event) {
        if (!isSelectClientFile) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("download file");
            alert.setHeaderText("download " + convertString(selectedHomeFile) + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    netty.sendMessage(new Message(convertString(selectedServerFile), Command.DOWNLOAD));
                    log.debug("Client file download");

                } else if (result.get() == ButtonType.CANCEL) {
                    return;
                }
            }
        } else {
            return;
        }
    }

    // переименование файла папки
    public void rename(ActionEvent event) throws IOException {
        if (isSelectClientFile) {
            TextInputDialog dialog = new TextInputDialog(convertString(selectedServerFile));
            dialog.setTitle("rename file");
            dialog.setHeaderText("rename " + convertString(selectedHomeFile) + "?");
            Optional<String> result = dialog.showAndWait();
            String entered = "";
            if (result.isPresent()) {
                entered = result.get();
            }
            log.debug(entered);
            Files.move(Paths.get(root.toString() + "/" + convertString(selectedHomeFile)), Paths.get(root.toString() + "/" + entered));
        } else {
            TextInputDialog dialog = new TextInputDialog(convertString(selectedServerFile));
            dialog.setTitle("rename file");
            dialog.setHeaderText("rename " + convertString(selectedServerFile) + "?");
            Optional<String> result = dialog.showAndWait();
            String entered = "";
            if (result.isPresent()) {
                entered = result.get();
            }
            log.debug(entered);
            netty.sendMessage(new Message(entered, convertString(selectedServerFile), Command.RENAME));
        }
        refreshClientFiles();
    }

    // удаление файла, папки
    public void delete(ActionEvent event) {
        if (isSelectClientFile) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("delete file");
            alert.setHeaderText("delete " + convertString(selectedHomeFile) + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        log.debug("Client file delete");
                        Files.deleteIfExists(Paths.get(root.toString() + "/" + convertString(selectedHomeFile)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (result.get() == ButtonType.CANCEL) {
                    return;
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("delete file");
            alert.setHeaderText("delete " + convertString(selectedServerFile) + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    log.debug("Server file delete");
                    netty.sendMessage(new Message(convertString(selectedServerFile), Command.DELETE));
                } else if (result.get() == ButtonType.CANCEL) {
                    return;
                }
            }
        }
        refreshClientFiles();
    }

    // изменяем время последней модификации файла в удобный формат
    private String convertTime(long t) {
        Date date = new Date(t);
        Format format = new SimpleDateFormat("HH:mm dd.MM.yy");
        return format.format(date);
    }

    // конвертируем размер файла в удобный формат
    private String convertFileSize(File file) {
        long size = 0;
        try {
            size = Files.size(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (size < 1024) {
            return String.format("%,d b", size);
        } else {
            return String.format("%,d kb", size / 1024);
        }
    }

    // очищаем строки из таблицы
    private String convertString(String unconverted) {
        if (unconverted.contains("\t")) {
            return unconverted.substring(6, unconverted.indexOf("\t"));
        } else {
            return unconverted.substring(6);
        }

    }

    // навигация на уровень выше
    public void clientLevelUp(ActionEvent actionEvent) {
        root = Paths.get(root.toString().substring(0, root.toString().lastIndexOf("\\")));
        updatePathLabel(root);
        refreshClientFiles();
    }

    // обновить указатель пути
    private void updatePathLabel(Path root) {
        clientLevelUpButton.setText("↑↑  " + root.toString());
    }
}
