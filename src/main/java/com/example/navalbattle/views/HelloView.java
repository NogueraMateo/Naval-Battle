package com.example.navalbattle.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.text.View;
import java.io.IOException;

public class HelloView extends Stage {
    /**
     * Initializes the GameView by loading the FXML layout and setting
     * the window's title, scene, and other properties.
     *
     * @throws IOException if there is an issue loading the FXML file
     */
    public HelloView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/navalbattle/hello-view.fxml"));
        Parent root = loader.load();
        this.setTitle("Naval Battle");
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setResizable(false);
        this.show();
    }

    /**
     * Holds the single instance of GameView, following the Singleton pattern.
     */
    private static class HelloViewHolder {
        private static HelloView INSTANCE;
    }

    /**
     * Provides access to the single instance of GameView.
     * If the instance does not exist, it is created; otherwise, the existing
     * instance is returned.
     *
     * @return the singleton instance of GameView
     * @throws IOException if there is an issue loading the FXML file
     */
    public static HelloView getInstance() throws IOException {

        if (HelloViewHolder.INSTANCE == null) {
            return HelloViewHolder.INSTANCE = new HelloView();
        } else {
            return HelloViewHolder.INSTANCE;
        }
    }
}
