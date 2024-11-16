package com.example.navalbattle.models;

import java.io.IOException;

/**
 * The GameModel class represents the core data structure and state management
 * for the Naval Battle game. It holds the main game tables (player's position table
 * and machine's main table) and integrates persistence functionality to allow
 * saving and loading game progress.
 */
public class GameModel {

    private PositionTable positionTable;
    private MainTable mainTable;
    private GamePersistenceModel gamePersistenceModel;
    private MatchStatusSerializable previousMatch;
    private String nickname;
    /**
     * Constructs a new GameModel, initializing the position and main tables.
     * Attempts to load a previous match from storage; if none is found,
     * a new game state is created.
     */
    public GameModel() {
        gamePersistenceModel = new GamePersistenceModel();

    }

    public String getNickname() {
        return nickname;
    }

    public MainTable getMainTable() {
        return mainTable;
    }

    public PositionTable getPositionTable() {
        return positionTable;
    }

    public boolean existsPreviousMatch(String nickname) {
        try {
            previousMatch = this.previousMatch();
        } catch (ClassNotFoundException | IOException e) {
            newMatch(nickname);
            return false;
        }
        return true;
    }

    public void newMatch(String nickname) {
        positionTable = new PositionTable();
        mainTable = new MainTable();
        gamePersistenceModel.registerNewMatch(mainTable ,positionTable, nickname);
    }

    public void loadPreviousMatch() {
        nickname = previousMatch.getNickName();
        positionTable = previousMatch.getPositionTable();
        mainTable = previousMatch.getMainTable();
    }

    /**
     * Loads the previous match state from persistent storage.
     * This method allows the game to resume from the last saved state.
     *
     * @return the deserialized MatchStatusSerializable containing the previous game state
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     * @throws IOException if an I/O error occurs while reading the saved file
     */
    public MatchStatusSerializable previousMatch() throws ClassNotFoundException, IOException {
        return gamePersistenceModel.deserialize();
    }

    public void saveGame() {
        gamePersistenceModel.takeSnapshot(mainTable, positionTable);
    }
}
