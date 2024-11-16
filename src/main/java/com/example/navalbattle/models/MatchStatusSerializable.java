package com.example.navalbattle.models;

import com.example.navalbattle.interfaces.MatchStatusSerializableInterface;

import java.io.Serializable;

/**
 * Represents the status of a match in the Battleship game, containing
 * the boards for both the player and the machine.
 * This class is serializable, enabling the game state to be saved
 * and loaded, allowing the player to resume from the last saved state.
 * @author Mateo Noguera Pinto
 */
public class MatchStatusSerializable implements MatchStatusSerializableInterface, Serializable {

    private MainTable mainTable;
    private PositionTable positionTable;
    private final String nickName;

    /**
     * Constructs a new MatchStatusSerializable with the given player
     * and machine boards.
     *
     * @param mainTable      the machine's game board
     * @param positionTable  the player's game board
     */
    public MatchStatusSerializable(MainTable mainTable, PositionTable positionTable, String nickName) {
        this.mainTable = mainTable;
        this.positionTable = positionTable;
        this.nickName = nickName;
    }

    /**
     * Returns the machine's game board (main table).
     *
     * @return a 2D array representing the machine's board
     */
    @Override
    public MainTable getMainTable() {
        return mainTable;
    }

    /**
     * Returns the player's game board (position table).
     *
     * @return a 2D array representing the player's board
     */
    @Override
    public PositionTable getPositionTable() {
        return positionTable;
    }

    /**
     * Returns the player's username.
     *
     * @return a String representing the username
     */
    @Override
    public String getNickName() {
        return nickName;
    }

    /**
     * Updates the current game status by saving the provided boards.
     *
     * @param mainTable      the machine's game board to save
     * @param positionTable  the player's game board to save
     */
    @Override
    public void saveSnapShot(MainTable mainTable, PositionTable positionTable) {
        this.mainTable = mainTable;
        this.positionTable = positionTable;
    }
}
