package com.example.navalbattle.models;

import com.example.navalbattle.interfaces.MainTableInterface;

import java.io.Serializable;
import java.util.*;

/**
 * The MainTable class represents the machine's game board in a battleship game.
 * The board is a 10x10 grid with randomly placed ships of varying sizes.
 * Ship sizes are defined in the shipsSize array.
 * The board is initialized with water (0), and ships are placed on it.
 * The class also supports generating shots, placing ships, and checking for valid shot positions.
 *
 * @author JuanToro
 */
public class MainTable implements MainTableInterface, Serializable {

    /**
     * A 10x10 grid representing the game board.
     * Each cell can be either 0 (water) or a number corresponding to a ship.
     */
    private int[][] board = new int[10][10];

    /**
     * A 10x10 grid representing the shot history grid.
     * Each cell is either 0 (no shot), 1 (hit), or 2 (miss).
     */
    private final int[][] shotGrid = new int[10][10];

    /**
     * A list storing the coordinates and related data for each ship.
     * The array contains:
     * - row index (int)
     * - column index (int)
     * - additional ship-related data (e.g., orientation, type).
     */
    private final List<int[]> shipCoordinatesList = new ArrayList<>();

    /**
     * A list storing the coordinates of the shots fired by the machine.
     */
    private final List<int[]> shots = new ArrayList<>();

    /**
     * A random number generator used for placing ships and selecting shots.
     */
    private final Random random = new Random();

    /**
     * A list of ships placed on the board.
     * Ships are defined by their size and amount of occurrences.
     */
    ArrayList<Ship> ships = new ArrayList<>();

    /**
     * Constructor for the MainTable class.
     * Initializes the game board, places the ships randomly, and displays the board.
     */
    public MainTable() {
        ships.add(new Ship(4,4, 1));
        ships.add(new Ship(3,3, 2));
        ships.add(new Ship(2,2, 3));
        ships.add(new Ship(1,1, 4));
        startBoard();
    }

    /**
     * Constructor for the MainTable class that loads a previous game board.
     *
     * @param board a 2D array representing the board to load.
     */
    public MainTable(int[][] board) {
        ships.add(new Ship(4,4, 0));
        ships.add(new Ship(3,3, 0));
        ships.add(new Ship(2,2, 0));
        ships.add(new Ship(1,1, 0));
        this.board = board;
        System.out.println("Previous Main Table loaded");
    }

    /**
     * Initializes the board by setting all cells to 0 (water) and then places the ships.
     */
    @Override
    public void startBoard() {
        for (int[] nums : board) {
            Arrays.fill(nums, 0);
        }
        setShips();
    }

    /**
     * Gets the shot grid, representing the history of shots made.
     *
     * @return a 2D array representing the shot grid.
     */
    @Override
    public int[][] getShotGrid() {
        return shotGrid;
    }

    /**
     * Places ships randomly on the board based on the ship sizes defined in the ships ArrayList.
     * Ships are placed in valid positions, ensuring they do not overlap or extend beyond the board boundaries.
     */
    @Override
    public void setShips() {
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
                        int endX = horizontal ? row : row + size - 1;
                        int endY = horizontal ? column + size - 1 : column;
                        int orientation = horizontal ? 1 : 0;

                        shipCoordinatesList.add(new int[]{row, column, endX, endY, orientation, aux, 0});

                        placed = true;
                    }
                }
            }
        }
    }

    /**
     * Checks if a ship of a given size can be placed at the specified position and orientation.
     *
     * @param row        the starting row for the ship.
     * @param column     the starting column for the ship.
     * @param size       the size of the ship.
     * @param horizontal the orientation of the ship (true for horizontal, false for vertical).
     * @return true if the ship can be placed at the specified position, false otherwise.
     */
    @Override
    public boolean canPlaced(int row, int column, int size, boolean horizontal) {
        if (horizontal) {
            if (column + size > 10) return false;
            for (int i = 0; i < size; i++) {
                if (board[row][column + i] != 0) {
                    return false;
                }
            }
        } else {
            if (row + size > 10) return false;
            for (int i = 0; i < size; i++) {
                if (board[row + i][column] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generates a random shot that has not been previously fired.
     *
     * @return an array of two integers representing the coordinates of the shot.
     */
    @Override
    public int[] shot() {
        int x, y;
        boolean isUnique;

        do {
            x = random.nextInt(10);
            y = random.nextInt(10);
            isUnique = true;

            for (int[] shot : shots) {
                if (shot[0] == x && shot[1] == y) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);

        shots.add(new int[]{x, y});
        return new int[]{x, y};
    }

    /**
     * Generates a smart shot based on the last shot fired.
     *
     * @return an array of two integers representing the coordinates of the smart shot.
     */
    @Override
    public int[] smartShot() {
        if (shots.isEmpty()) {
            return shot();
        }

        int[] lastShot = shots.get(shots.size() - 1);
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{0, 1});
        directions.add(new int[]{0, -1});
        directions.add(new int[]{1, 0});
        directions.add(new int[]{-1, 0});

        Collections.shuffle(directions);

        for (int[] direction : directions) {
            int newX = lastShot[0] + direction[0];
            int newY = lastShot[1] + direction[1];

            if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10) {
                int[] newShot = new int[]{newX, newY};

                if (!isShot(newShot)) {
                    shots.add(newShot);
                    return newShot;
                }
            }
        }

        return shot();
    }

    /**
     * Checks if a shot has already been fired at the specified coordinates.
     *
     * @param shot an array of two integers representing the coordinates of the shot.
     * @return true if the shot has already been fired, false otherwise.
     */
    private boolean isShot(int[] shot) {
        for (int[] existingShot : shots) {
            if (existingShot[0] == shot[0] && existingShot[1] == shot[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the current state of the game board.
     *
     * @return a 10x10 integer array representing the game board.
     */
    @Override
    public int[][] getBoard() {
        return board;
    }

    /**
     * Retrieves the list of ship coordinates.
     *
     * @return a list of integer arrays representing the coordinates and related data for each ship.
     */
    @Override
    public List<int[]> getShipCoordinatesList() {
        return shipCoordinatesList;
    }

    /**
     * Retrieves the list of ships placed on the board.
     *
     * @return an ArrayList containing the ships.
     */
    public ArrayList<Ship> getShips() {
        return ships;
    }

    /**
     * Prints the current state of the main board to the console.
     *
     * @param board the board to print.
     */
    @Override
    public void printMainBoard(int[][] board) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }
}
