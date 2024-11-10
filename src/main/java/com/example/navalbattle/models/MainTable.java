package com.example.navalbattle.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The mainTable class represents the machine's game board in a battleship game.
 * The board is a 10x10 grid with randomly placed ships of varying sizes.
 * Ships sizes are defined in the shipsSize array.
 * @author JuanToro
 */
public class MainTable {
    /**
     * A 10x10 grid representing the game board.
     * Each cell can be either 0 (water) or 1 (occupied by a ship).
     */
    private final int[][] board = new int[10][10];

    /**
     * ArrayList of ship type objects
     */
    ArrayList<Ship> ships = new ArrayList<Ship>();

    /**
     * Constructor for the Machine class. Initializes the game board,
     * places the ships randomly, and displays the board.
     */
    public MainTable(){
        ships.add(new Ship(4,4, 1));
        ships.add(new Ship(3,3, 2));
        ships.add(new Ship(2,2, 3));
        ships.add(new Ship(1,1, 4));
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
     * Place ships randomly on the board based on the ship sizes defined in the Ships ArrayList.
     * Ensures that ships do not overlap or extend beyond the board boundaries.
     */
    public void setShips() {
        Random random = new Random();

        for (Ship ship : ships) {
            int amount = ship.getShipAmount();
            for (int i = 0; i < amount; i++) {
                int size = ship.getShipSize();
                boolean placed = false;
                int aux = ship.getShipType();

                while (!placed) {
                    int row = random.nextInt(10);
                    int column = random.nextInt(10);
                    boolean horizontal = random.nextBoolean();

                    if (canPlaced(row, column, size, horizontal)) {
                        for (int j = 0; j < size; j++) {
                            if (horizontal) {
                                board[row][column + j] = aux;
                            } else {
                                board[row + j][column] = aux;
                            }
                        }
                        placed = true;
                    }
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
                if (board[row][column + i] == 1 || board[row][column + i] == 2 || board[row][column + i] == 3 || board[row][column + i] == 4) {
                    return false;
                }
            }
        } else {
            if (row + size > 10) return false;
            for (int i = 0; i < size; i++) {
                if (board[row + i][column] == 1 || board[row + i][column] == 2 || board[row + i][column] == 3 || board[row + i][column] == 4) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generates a random shot on the board by selecting a random coordinate.
     * @return an integer array containing the x (row) and y (column) coordinates of the shot
     */
    public int[] shot(){
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        return new int[]{x, y};
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
     * Cells with value 0 display as '~' (water) and cells with value 1, 2, 3 or 4 display the first letter of the ship type
     */
    public void showBoard() {
        System.out.println("   0 1 2 3 4 5 6 7 8 9");
        System.out.println("  ---------------------");
        for (int fila = 0; fila < board.length; fila++) {
            System.out.print(fila + " | ");
            for (int columna = 0; columna < board[fila].length; columna++) {
                if (board[fila][columna] == 0) {
                    System.out.print("~ ");
                } else if (board[fila][columna] == 4) {
                    System.out.print("P ");
                } else if (board[fila][columna] == 3) {
                    System.out.print("S ");
                } else if (board[fila][columna] == 2) {
                    System.out.print("D ");
                } else if (board[fila][columna] == 1) {
                    System.out.print("F ");
                }
            }
            System.out.println();
        }
    }
}
