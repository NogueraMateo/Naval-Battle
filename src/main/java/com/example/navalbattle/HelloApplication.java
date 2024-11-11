package com.example.navalbattle;

import com.example.navalbattle.models.PositionTable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

        /*
        System.out.println("++++++++++++MOVEMENT 1++++++++++");
        positionTable.setShipPosition(0, 0, 0, 0);
        positionTable.printBoard();
        System.out.println("++++++++++++MOVEMENT 2++++++++++");
        positionTable.setShipPosition(0, 1, 0, 0);
        positionTable.printBoard();
        System.out.println("++++++++++++MOVEMENT 3++++++++++");
        positionTable.setShipPosition(1, 1, 0, 1);
        positionTable.printBoard();
        System.out.println("++++++++++++MOVEMENT 4++++++++++");
        positionTable.setShipPosition(1, 2, 3, 0);
        positionTable.printBoard();
        */
    }
}