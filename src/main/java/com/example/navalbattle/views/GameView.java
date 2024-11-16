package com.example.navalbattle.views;

import com.example.navalbattle.controllers.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the main game window (view) for the Naval Battle game.
 * This class extends JavaFX's Stage and loads the game-view.fxml layout
 * file to set up the initial UI components of the game.
 *
 * GameView uses the Singleton pattern to ensure only one instance of
 * the game view is created and managed at a time.
 */
public class GameView extends Stage {
    private final GameController gameController;

    /**
     * Initializes the GameView by loading the FXML layout and setting
     * the window's title, scene, and other properties.
     *
     * @throws IOException if there is an issue loading the FXML file
     */
    public GameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/navalbattle/game-view.fxml"));
        Parent root = loader.load();
        this.setTitle("Naval Battle");
        this.gameController = loader.getController();
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setResizable(false);
        this.show();
    }

    /**
     * Holds the single instance of GameView, following the Singleton pattern.
     */
    private static class GameViewHolder {
        private static GameView INSTANCE;
    }

    /**
     * Provides access to the single instance of GameView.
     * If the instance does not exist, it is created; otherwise, the existing
     * instance is returned.
     *
     * @return the singleton instance of GameView
     * @throws IOException if there is an issue loading the FXML file
     */
    public static GameView getInstance() throws IOException {

        if (GameViewHolder.INSTANCE == null) {
            return GameViewHolder.INSTANCE = new GameView();
        } else {
            return GameViewHolder.INSTANCE;
        }
    }

    public GameController getGameController() {
        return gameController;
    }

}
