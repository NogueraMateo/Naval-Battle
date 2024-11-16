package com.example.navalbattle.interfaces;

import com.example.navalbattle.models.MatchStatusSerializable;
import com.example.navalbattle.models.MainTable;
import com.example.navalbattle.models.PositionTable;

import java.io.IOException;

/**
 * Interface that defines the core methods for managing the game state
 * and persistence in the Naval Battle game.
 * Provides methods for creating a new match, loading and saving game states,
 * and removing match tracking.
 */
public interface GameModelInterface {

    /**
     * Retrieves the player's nickname.
     *
     * @return the nickname of the player
     */
    String getNickname();

    /**
     * Retrieves the main table of the machine.
     *
     * @return the main table of the machine
     */
    MainTable getMainTable();

    /**
     * Retrieves the position table of the player.
     *
     * @return the position table of the player
     */
    PositionTable getPositionTable();

    /**
     * Checks if there is a previous match saved for the given nickname.
     * If a previous match is found, it loads the match; otherwise, creates a new match.
     *
     * @param nickname the nickname of the player
     * @return true if a previous match exists, false otherwise
     */
    boolean existsPreviousMatch(String nickname);

    /**
     * Creates a new match by initializing new position and main tables,
     * then registers the new match in persistence.
     *
     * @param nickname the nickname of the player
     */
    void newMatch(String nickname);

    /**
     * Loads the previous match from persistent storage.
     * This method restores the match's state by assigning the player's nickname,
     * position table, and main table.
     */

    void loadPreviousMatch();

    /**
     * Loads the previous match state from persistent storage.
     * This method allows the game to resume from the last saved state.
     *
     * @return the deserialized MatchStatusSerializable containing the previous game state
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     * @throws IOException if an I/O error occurs while reading the saved file
     */
    MatchStatusSerializable previousMatch() throws ClassNotFoundException, IOException;

    /**
     * Saves the current game state to persistent storage.
     * This method takes a snapshot of the current position and main tables and saves it.
     */
    void saveGame();

    /**
     * Removes the tracking of the current match by deleting the saved match state.
     * This will remove any persistent data related to the match.
     */
    void removeMatchTracking();
}
