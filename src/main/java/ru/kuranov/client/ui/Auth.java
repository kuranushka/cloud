package ru.kuranov.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import ru.kuranov.client.msg.AuthMessage;
import ru.kuranov.client.net.NettyClient;

import java.io.IOException;

@Slf4j
public class Auth {
    public Label label;
    public Button sendAuthButton;
    public TextField user;
    public PasswordField password;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private AuthMessage authMessage;
    private NettyClient netty;

    public void auth(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("window.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
