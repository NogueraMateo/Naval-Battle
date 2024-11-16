package com.example.navalbattle.interfaces;

import com.example.navalbattle.models.MainTable;
import com.example.navalbattle.models.PositionTable;

import java.io.Serializable;

/**
 * Interface representing the status of a match in the Battleship game.
 * This interface defines methods for retrieving and updating the state of the game,
 * including the player's and machine's boards, and the player's nickname.
 */
public interface MatchStatusSerializableInterface extends Serializable {

    /**
     * Returns the machine's game board (main table).
     *
     * @return the machine's main game board.
     */
    MainTable getMainTable();

    /**
     * Returns the player's game board (position table).
     *
     * @return the player's position game board.
     */
    PositionTable getPositionTable();

    /**
     * Returns the player's username.
     *
     * @return the player's username.
     */
    String getNickName();

    /**
     * Updates the current game status by saving the provided boards.
     *
     * @param mainTable      the machine's game board to save
     * @param positionTable  the player's game board to save
     */
    void saveSnapShot(MainTable mainTable, PositionTable positionTable);
}
