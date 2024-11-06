package com.example.navalbattle.models;

import java.util.Arrays;
import java.util.Random;

/**
 * The Machine class represents the machine's game board in a battleship game.
 * The board is a 10x10 grid with randomly placed ships of varying sizes.
 * Ships sizes are defined in the shipsSize array.
 * @author JuanToro
 */
public class Machine {
    /**
     * A 10x10 grid representing the game board.
     * Each cell can be either 0 (water) or 1 (occupied by a ship).
     */
    private final int[][] board = new int[10][10];

    /**
     * Array defining the sizes of each ship to be placed on the board.
     */
    private final int[] shipsSize = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

    /**
     * Constructor for the Machine class. Initializes the game board,
     * places the ships randomly, and displays the board.
     */
    public Machine (){
        startBoard();
        showBoard();
    }

    /**
     * Initializes the board by setting all cells to 0 (water) and then places the ships.
     */
    public void startBoard() {
        for (int[] nums : board) {
            Arrays.fill(nums, 0);
        }
        setShips();
    }

    /**
     * Randomly places ships on the board based on the ship sizes defined in shipsSize.
     * Ensures that ships do not overlap or extend beyond the board's boundaries.
     */
    public void setShips() {
        Random random = new Random();

        for (int size : shipsSize) {
            boolean placed = false;

            while (!placed) {
                int row = random.nextInt(10);
                int column = random.nextInt(10);
                boolean horizontal = random.nextBoolean();

                if (canPlaced(row, column, size, horizontal)) {
                    for (int j = 0; j < size; j++) {
                        if (horizontal) {
                            board[row][column + j] = 1;
                        } else {
                            board[row + j][column] = 1;
                        }
                    }
                    placed = true;
                }
            }
        }
    }

    /**
     * Checks if a ship of a given size can be placed at the specified position and orientation.
     * @param row        the starting row for the ship
     * @param column     the starting column for the ship
     * @param size       the size of the ship
     * @param horizontal the orientation of the ship (true for horizontal, false for vertical)
     * @return true if the ship can be placed at the specified position, false otherwise
     */
    public boolean canPlaced(int row, int column, int size, boolean horizontal) {
        if (horizontal) {
            if (column + size > 10) return false;
            for (int i = 0; i < size; i++) {
                if (board[row][column + i] == 1) return false;
            }
        } else {
            if (row + size > 10) return false;
            for (int i = 0; i < size; i++) {
                if (board[row + i][column] == 1) return false;
            }
        }
        return true;
    }

    /**
     * Returns the current state of the game board.
     * @return a 10x10 integer array representing the game board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Displays the current state of the board in the console.
     * Cells with value 0 are shown as '~' (water) and cells with value 1 as 'B' (ship).
     */
    public void showBoard() {
        System.out.println("   0 1 2 3 4 5 6 7 8 9");
        System.out.println("  ---------------------");
        for (int fila = 0; fila < board.length; fila++) {
            System.out.print(fila + " | ");
            for (int columna = 0; columna < board[fila].length; columna++) {
                if (board[fila][columna] == 0) {
                    System.out.print("~ ");
                } else {
                    System.out.print("B ");
                }
            }
            System.out.println();
        }
    }
}
