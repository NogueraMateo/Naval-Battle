package com.example.navalbattle;

import com.example.navalbattle.views.HelloView;
import javafx.application.Application;
import javafx.stage.Stage;


import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        HelloView.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }
}