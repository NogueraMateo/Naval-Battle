package com.example.navalbattle.interfaces;

import java.io.Serializable;
import java.util.List;

/**
 * Interface representing the machine's game board in the Battleship game.
 * This interface provides methods to interact with the game board,
 * such as placing ships, taking shots, and retrieving board information.
 */
public interface MainTableInterface extends Serializable {

    /**
     * Initializes the game board, setting all cells to 0 (water) and placing the ships.
     */
    void startBoard();

    /**
     * Places ships randomly on the board based on the ship sizes defined in the Ships ArrayList.
     */
    void setShips();

    /**
     * Returns the current state of the game board.
     * @return a 10x10 integer array representing the game board
     */
    int[][] getBoard();

    /**
     * Retrieves the list of ship coordinates.
     * This list contains the coordinates and related data for each ship, such as position, orientation, and type.
     *
     * @return A list of integer arrays where each array represents the coordinates and other relevant data of a ship.
     */
    List<int[]> getShipCoordinatesList();

    /**
     * Returns the machine's shot grid.
     * This grid tracks the locations of shots taken.
     *
     * @return a 10x10 integer array representing the shot grid
     */
    int[][] getShotGrid();

    /**
     * Generates a random shot that has not been previously shot at.
     *
     * @return an array of two integers representing the (x, y) coordinates of the shot.
     */
    int[] shot();

    /**
     * Generates a smart shot based on the last shot fired.
     *
     * @return an array of two integers representing the (x, y) coordinates of the smart shot.
     */
    int[] smartShot();

    /**
     * Checks if a ship of a given size can be placed at the specified position and orientation.
     * @param row        the starting row for the ship
     * @param column     the starting column for the ship
     * @param size       the size of the ship
     * @param horizontal the orientation of the ship (true for horizontal, false for vertical)
     * @return true if the ship can be placed at the specified position, false otherwise
     */
    boolean canPlaced(int row, int column, int size, boolean horizontal);

    /**
     * Prints the game board to the console for debugging purposes.
     * @param board the board to print
     */
    void printMainBoard(int[][] board);
}