package com.example.navalbattle.models;

import java.io.Serializable;

/**
 * Represents the status of a match in the Battleship game, containing
 * the boards for both the player and the machine.
 * This class is serializable, enabling the game state to be saved
 * and loaded, allowing the player to resume from the last saved state.
 * @author Mateo Noguera Pinto
 */
public class MatchStatusSerializable implements Serializable {

    private int[][] mainTable;
    private int[][] positionTable;

    /**
     * Constructs a new MatchStatusSerializable with the given player
     * and machine boards.
     *
     * @param mainTable      the machine's game board
     * @param positionTable  the player's game board
     */
    public MatchStatusSerializable(int[][] mainTable, int[][] positionTable) {
        this.mainTable = mainTable;
        this.positionTable = positionTable;
    }

    /**
     * Returns the machine's game board (main table).
     *
     * @return a 2D array representing the machine's board
     */
    public int[][] getMainTable() {
        return mainTable;
    }

    /**
     * Returns the player's game board (position table).
     *
     * @return a 2D array representing the player's board
     */
    public int[][] getPositionTable() {
        return positionTable;
    }

    /**
     * Updates the current game status by saving the provided boards.
     *
     * @param mainTable      the machine's game board to save
     * @param positionTable  the player's game board to save
     */
    public void saveSnapShot(int[][] mainTable, int[][] positionTable) {
        this.mainTable = mainTable;
        this.positionTable = positionTable;
    }
}
