package com.example.navalbattle.controllers;

import com.example.navalbattle.models.GameModel;
import com.example.navalbattle.views.GameView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    private GameModel gameModel = new GameModel();

    @FXML
    protected void onHelloButtonClick(Event event) throws IOException {
        Node source = (Node) event.getSource();
        Stage actualStage = (Stage) source.getScene().getWindow();
        actualStage.close();

        GameView.getInstance();
    }
}