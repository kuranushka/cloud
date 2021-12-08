package ru.kuranov.client.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;
@Data
public class AuthWindow extends Application implements Initializable {
    public Label upLabel;
    public Label downLabel;
    public TextField login;
    public PasswordField pass;
    public Button enter;
    public Button create;
    private Stage stageAuth;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                downLabel.setText("ANOTHER CATCH");
                stageAuth.initModality(Modality.NONE);
                stageAuth.hide();
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage stageMain = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stageMain.setScene(scene);
        stageMain.show();

        stageAuth = new Stage();
        Parent parent = FXMLLoader.load(AuthWindow.class.getResource("auth.fxml"));
        stageAuth.setScene(new Scene(parent));
        stageAuth.initModality(Modality.APPLICATION_MODAL);
        stageAuth.show();


    }


}

