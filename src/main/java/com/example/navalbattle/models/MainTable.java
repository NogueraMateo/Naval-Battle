package com.example.navalbattle.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private int[][] board = new int[10][10];

    /**
     * A list that stores the coordinates and related data for each ship.
     * Each entry in the list is an integer array representing the coordinates and other relevant data for a ship.
     * The array contains:
     * - row index (int)
     * - column index (int)
     * - additional ship-related data (e.g., orientation, type).

     * This list is populated with ship data and is used for placing ships on the board.
     */
    private final List<int[]> shipCoordinatesList = new ArrayList<>();
    private final List<int[]> shots = new ArrayList<>();
    private final Random random = new Random();

    /**
     * ArrayList of ship
     */
    ArrayList<Ship> ships = new ArrayList<>();

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
    }

    public MainTable(int[][] board){
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
                        int endX = horizontal ? row : row + size - 1;
                        int endY = horizontal ? column + size - 1 : column;
                        int orientation = horizontal ? 1 : 0;

                        shipCoordinatesList.add(new int[]{row, column, endX, endY, orientation, aux});

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
     * Generates a random shot that has not been previously shot at.

     * This method generates a random (x, y) coordinate pair for a shot within a 10x10 grid.
     * It ensures that the generated shot has not been taken before by checking against the list of
     * previous shots. If the shot has already been taken, it tries again until a unique shot is found.
     *
     * @return an array of two integers representing the (x, y) coordinates of the shot.
     */
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

     * This method attempts to generate a shot near the last shot taken. It checks in all four
     * possible directions (up, down, left, right) from the last shot's coordinates. If a valid
     * shot (within bounds and not previously shot at) is found, it is chosen as the next shot.
     * If no valid shot is found, it defaults to a random shot by calling the {@link #shot()} method.
     *
     * @return an array of two integers representing the (x, y) coordinates of the smart shot.
     */
    public int[] smartShot() {
        if (shots.isEmpty()) {
            return shot();
        }

        int[] lastShot = shots.get(shots.size() - 1);
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

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
     * Checks if a shot has already been taken at the specified coordinates.

     * This helper method checks if a given shot (represented by a pair of coordinates)
     * has already been added to the list of previous shots. It returns true if the shot
     * has already been taken, otherwise false.
     *
     * @param shot an array of two integers representing the (x, y) coordinates of the shot to check.
     * @return true if the shot has already been taken, false otherwise.
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
     * @return a 10x10 integer array representing the game board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Retrieves the list of ship coordinates.
     * This list contains the coordinates and related data for each ship, such as position, orientation, and type.
     *
     * @return A list of integer arrays where each array represents the coordinates and other relevant data of a ship.
     *         Each array contains:
     *         - row index (int)
     *         - column index (int)
     *         - some other ship-related data (e.g., orientation, type).
     */
    public List<int[]> getShipCoordinatesList() {
        return shipCoordinatesList;
    }


}
