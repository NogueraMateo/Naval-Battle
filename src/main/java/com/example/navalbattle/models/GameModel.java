package com.example.navalbattle.models;

import com.example.navalbattle.interfaces.GameModelInterface;

import java.io.IOException;
import java.io.Serializable;

/**
 * The GameModel class represents the core data structure and state management
 * for the Naval Battle game. It holds the main game tables (player's position table
 * and machine's main table) and integrates persistence functionality to allow
 * saving and loading game progress.
 */
public class GameModel implements GameModelInterface, Serializable {

    private PositionTable positionTable;
    private MainTable mainTable;
    private final GamePersistenceModel gamePersistenceModel;
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
    /**
     * Retrieves the player's nickname.
     *
     * @return the nickname of the player
     */
    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Retrieves the machine's main game table.
     *
     * @return the main table of the machine
     */
    @Override
    public MainTable getMainTable() {
        return mainTable;
    }

    /**
     * Retrieves the player's position table.
     *
     * @return the position table of the player
     */
    @Override
    public PositionTable getPositionTable() {
        return positionTable;
    }

    /**
     * Checks if there is a previous match saved for the given nickname.
     * If a previous match is found, it loads the match; otherwise, creates a new match.
     *
     * @param nickname the nickname of the player
     * @return true if a previous match exists, false otherwise
     */
    @Override
    public boolean existsPreviousMatch(String nickname) {
        try {
            previousMatch = this.previousMatch();
        } catch (ClassNotFoundException | IOException e) {
            newMatch(nickname);
            return false;
        }
        return true;
    }

    /**
     * Creates a new match by initializing new position and main tables,
     * then registers the new match in persistence.
     *
     * @param nickname the nickname of the player
     */
    @Override
    public void newMatch(String nickname) {
        positionTable = new PositionTable();
        mainTable = new MainTable();
        gamePersistenceModel.registerNewMatch(mainTable ,positionTable, nickname);
    }

    /**
     * Loads the previous match from persistent storage.
     * This method restores the match's state by assigning the player's nickname,
     * position table, and main table.
     */
    @Override
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
    @Override
    public MatchStatusSerializable previousMatch() throws ClassNotFoundException, IOException {
        return gamePersistenceModel.deserialize();
    }

    /**
     * Saves the current game state to persistent storage.
     * This method takes a snapshot of the current position and main tables and saves it.
     */
    @Override
    public void saveGame() {
        gamePersistenceModel.takeSnapshot(mainTable, positionTable);
    }

    /**
     * Removes the tracking of the current match by deleting the saved match state.
     * This will remove any persistent data related to the match.
     */
    @Override
    public void removeMatchTracking() {
        gamePersistenceModel.deleteMatchStatus();
    }
}
