package ru.kuranov.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.kuranov.client.auth.Authentication;
import ru.kuranov.client.msg.AuthMessage;
import ru.kuranov.client.net.NettyClient;
import ru.kuranov.client.ui.MainWindow;

import java.io.IOException;


public class ClientStart extends Application {
    static NettyClient nettyClient;
    AuthMessage authMessage;

    public static NettyClient getNettyClient() {
        return nettyClient;
    }

    @Override
    public void start(Stage authStage) {
        nettyClient = new NettyClient(System.out::println);
        authStage.setTitle("Cloud Storage Authorisation");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label("Welcome, please enter your user  and password");
        grid.add(label, 0, 1);

        Label userLabel = new Label("User:");
        grid.add(userLabel, 0, 2);
        TextField user = new TextField("user1");
        grid.add(user, 1, 2);
        Label passLabel = new Label("Password:");
        grid.add(passLabel, 0, 3);
        PasswordField password = new PasswordField();
        password.appendText("pass1");
        grid.add(password, 1, 3);
        Button enter = new Button("Enter");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(enter);
        grid.add(hbBtn, 1, 4);

        enter.setOnAction(e -> {
            authMessage = new AuthMessage(false, false, user.getText(), password.getText());
            nettyClient.sendMessage(authMessage);

            try {
                Thread.sleep(1000);// задержка на отправку и возврат авторизации из базы
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (Authentication.isAuth()) {
                authStage.close();

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("window.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                stage.setScene(scene);
                stage.show();
            } else {
                user.setText("");
                password.setText("");
                label.setText("incorrect login or password!!!");
            }
        });
        Scene scene = new Scene(grid, 330, 275);
        authStage.setScene(scene);
        authStage.show();
    }
}