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
import ru.kuranov.client.auth.Authentication;
import ru.kuranov.client.msg.AuthMessage;
import ru.kuranov.client.net.NettyClient;

import java.io.IOException;

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
        netty = NettyClient.getInstance(System.out::println);
        label = new Label();
        user = new TextField("user1");
        password = new PasswordField();
        password.appendText("pass1");
        sendAuthButton = new Button();
        authMessage = new AuthMessage(false, false, user.getText(), password.getText());
        netty.sendMessage(authMessage);
        try {
            Thread.sleep(1000);// задержка на отправку и возврат авторизации из базы
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        if (Authentication.isAuth()) {
            try {
                root = FXMLLoader.load(getClass().getResource("window.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            user.setText("");
            password.setText("");
            label.setText("incorrect login or password!!!");
        }
    }
}
