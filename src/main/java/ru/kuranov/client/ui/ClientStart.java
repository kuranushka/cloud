package ru.kuranov.client.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.kuranov.client.auth.Authentication;
import ru.kuranov.client.msg.AuthMessage;
import ru.kuranov.client.net.NettyClient;

import java.io.IOException;

public class ClientStart extends Application {

    public Label label;
    public Button sendAuthButton;
    public TextField user;
    public PasswordField password;
    public RadioButton radioButton;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private AuthMessage authMessage;
    private NettyClient netty;
    private boolean isNewUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        netty = NettyClient.getInstance(System.out::println);
        Parent root = FXMLLoader.load(getClass().getResource("auth.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        netty = NettyClient.getInstance(System.out::println);
        label = new Label();
        user = new TextField();
        password = new PasswordField();
        sendAuthButton = new Button();

    }

    public void send(ActionEvent e) {
        authMessage = new AuthMessage(isNewUser, false, user.getText(), password.getText());
        netty = NettyClient.getInstance(System.out::println);
        netty.sendMessage(authMessage);

        try {
            Thread.sleep(1000);// задержка на отправку и возврат авторизации из базы
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        if (Authentication.isAuth()) {
            Auth auth = new Auth();
            try {
                auth.auth(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            label.setText("login or password uncorrected, try again ...");
            user.clear();
            password.clear();

        }
    }

    public void newUser(ActionEvent event) {
        if (radioButton.isSelected()) {
            isNewUser = true;
        } else {
            isNewUser = false;
        }
    }
}