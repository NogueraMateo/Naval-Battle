package com.example.navalbattle.controllers;

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

    public void onActionButtonInstructions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(""" 
                Welcome to Naval Battle instructions!
                
                Boards:
                Position Board: This is the human player's board, showing the location of their own ships and marking any opponent's attacks. No shots can be made on this board.
                Main Board: This is the human player's attack board. Here, they try to guess the location of the enemy’s ships on the computer’s board.
                
                Game Objective:
                The goal is to sink all of the opponent's ships before they sink yours.
                
                How to Play:
                Shooting: The player selects a square on the main board to shoot.
                If they hit a square occupied by an enemy ship, it is marked as hit.
                If the shot lands on an empty square, it is marked with an X indicating miss, and the turn passes to the computer.
                If the shot sinks an entire ship (all of its parts hit, or it’s a one-square ship), the ship is fully marked as sunk.
                
                Game Terminology:
                Miss: Indicates that the shot did not hit any ship.
                Hit: The shot hit part of an enemy ship but did not sink it entirely.
                Sunk: The shot has destroyed an entire ship. The player can continue shooting until they miss.
                
                The game ends when one player has sunk the entire fleet of the opponent.
                Good luck, and enjoy the battle at sea!
                """);
        alert.showAndWait();
    }
}