package ru.kuranov.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import ru.kuranov.client.NettyClient;
import ru.kuranov.client.msgtype.AuthMessage;
import ru.kuranov.client.ui.MainWindow;

import java.io.IOException;


public class App extends Application {
    AuthMessage authMessage;
    NettyClient nettyClient;

    @Override
    public void start(Stage authStage) throws IOException {
        nettyClient = new NettyClient(System.out::println);
        authStage.setTitle("Cloud Storage Authorisation");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Label userLabel = new Label("User:");
        grid.add(userLabel, 0, 1);
        TextField user = new TextField();
        grid.add(user, 1, 1);
        Label passLabel = new Label("Password:");
        grid.add(passLabel, 0, 2);
        PasswordField password = new PasswordField();
        grid.add(password, 1, 2);
        Button enter = new Button("Enter");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(enter);
        grid.add(hbBtn, 1, 4);


        enter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {



                authMessage = AuthMessage.getAuthMessage(false, false, user.getText(), password.getText());
                nettyClient.sendMessage(authMessage);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                authMessage = AuthMessage.simpleAuthMessage();
                System.out.println("testForm" + authMessage);


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


            }
        });



        Scene scene = new Scene(grid, 330, 275);
        authStage.setScene(scene);
        authStage.show();
    }
}