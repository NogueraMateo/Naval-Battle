package com.example.navalbattle.interfaces;

import com.example.navalbattle.models.Ship;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface representing a player's position table in the Naval Battle game.
 * It defines the actions for placing ships, checking positions, and managing the game board.
 */
public interface PositionTableInterface {
    /**
     * Returns the player's 10x10 position table where ships are placed.
     *
     * @return the 2D array representing the player's ship position grid.
     */
    int[][] getBoard();

    /**
     * Returns the 10x10 shot grid for tracking shots on the board.
     *
     * @return the 2D array representing the shot grid.
     */
    int[][] getShotGrid();

    /**
     * Returns a list of ships available for placement on the board.
     *
     * @return the list of ships.
     */
    ArrayList<Ship> getShips();

    /**
     * Sets the position of a ship on the board, given the ship index, row, column, and orientation.
     *
     * @param shipIndex the index of the ship to be placed.
     * @param row the row where the ship should be placed.
     * @param col the column where the ship should be placed.
     * @param orientation the orientation of the ship (0 for horizontal, 1 for vertical).
     */
    void setShipPosition(int shipIndex, int row, int col, int orientation);

    /**
     * Checks if a ship can be placed at the given position.
     *
     * @param shipIndex the index of the ship to be placed.
     * @param row the row where the ship should be placed.
     * @param col the column where the ship should be placed.
     * @param orientation the orientation of the ship (0 for horizontal, 1 for vertical).
     * @return true if the ship can be placed, false otherwise.
     */
    boolean checkPosition(int shipIndex, int row, int col, int orientation);

    /**
     * Checks if there are any remaining ships of a given type to place.
     *
     * @param shipIndex the index of the ship type.
     * @return true if there are ships of this type left to place, false otherwise.
     */
    boolean checkAmount(int shipIndex);

    /**
     * Prints the current state of the player's board.
     */
    void printBoard();

    /**
     * Checks if the board is fully populated with all ships.
     *
     * @return true if the board is full, false otherwise.
     */
    boolean isBoardFull();

    /**
     * Returns the list of coordinates where ships have been placed.
     *
     * @return the list of ship coordinates.
     */
    List<int[]> getShipCoordinatesList();
}
