package com.example.navalbattle.controllers;

import com.example.navalbattle.models.GameModel;
import com.example.navalbattle.views.GameView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField inputUsername;

    @FXML
    protected void onHelloButtonPlay(Event event) throws IOException {
        if (inputUsername == null || inputUsername.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your username!");
            alert.showAndWait();
        } else {
            Node source = (Node) event.getSource();
            Stage actualStage = (Stage) source.getScene().getWindow();
            actualStage.close();

            GameView.getInstance();
        }
    }
}