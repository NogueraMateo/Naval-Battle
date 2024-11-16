package com.example.navalbattle.models;

import java.io.IOException;

/**
 * Interface defining the structure for game persistence operations, specifically for
 * managing and maintaining the state of matches in the "Battleship" game.
 * This interface provides methods to initialize new matches, save and load game states,
 * and verify the progress of ongoing matches.
 * @author Mateo Noguera Pinto
 */
public interface GamePersistenceInterface {

    /**
     * Registers a new match by initializing a new `MatchStatusSerializable` instance
     * with the given game boards and serializes it to save the starting state.
     *
     * @param mainTable      the initial game board for the machine
     * @param positionTable  the initial game board for the player
     */
    void registerNewMatch(MainTable mainTable, PositionTable positionTable);


    /**
     * Serializes the provided match state, saving it to a file.
     * This method overwrites the previous save file with the current match state.
     *
     * @param match the `MatchStatusSerializable` object representing the current game state
     */
    void serialize(MatchStatusSerializable match);


    /**
     * Deserializes the last saved match state, allowing the game to resume
     * from the previous position.
     *
     * @return the last saved `MatchStatusSerializable` object
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     * @throws IOException if an I/O error occurs while reading the file
     */
    MatchStatusSerializable deserialize() throws ClassNotFoundException, IOException;


    /**
     * Updates the current game state with the latest game boards and immediately
     * serializes it to save the progress.
     *
     * This method should be called after every move, whether it's by the player or
     * the machine, to ensure the game state is saved continuously.
     *
     * @param mainTable      the updated game board for the machine
     * @param positionTable  the updated game board for the player
     */
    void takeSnapshot(MainTable mainTable, PositionTable positionTable);
}
