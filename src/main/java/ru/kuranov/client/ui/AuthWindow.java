package ru.kuranov.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthWindow implements Initializable {
    public Label upLabel;
    public Label downLabel;
    public TextField login;
    public TextField pass;
    public Button enter;
    public Button create;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

