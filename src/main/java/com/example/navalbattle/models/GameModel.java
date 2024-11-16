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
    /**
     * Constructs a new GameModel, initializing the position and main tables.
     * Attempts to load a previous match from storage; if none is found,
     * a new game state is created.
     */
    public GameModel() {
        gamePersistenceModel = new GamePersistenceModel();
    }

    public boolean existsPreviousMatch() {
        try {
            previousMatch = this.previousMatch();
        } catch (ClassNotFoundException | IOException e) {
            newMatch();
            return false;
        }
        return true;
    }

    public void newMatch() {
        positionTable = new PositionTable();
        mainTable = new MainTable();
        gamePersistenceModel.registerNewMatch(mainTable ,positionTable);
    }

    public void loadPreviousMatch() {
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

    public MainTable getMainTable() {
        return mainTable;
    }

    public PositionTable getPositionTable() {
        return positionTable;
    }
}
