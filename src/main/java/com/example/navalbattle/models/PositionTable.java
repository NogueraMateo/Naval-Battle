package com.example.navalbattle.models;

import com.example.navalbattle.interfaces.PositionTableInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The PositionTable class represents the player's table where all ships are placed.
 * It includes a 10x10 grid, information about the player's ships, and their placement on the board.
 * This class implements the {@link PositionTableInterface} interface and can be serialized.
 *
 * <p>The class manages the placement and checking of ships on the board, and ensures that ships
 * are placed correctly without overlap. It also tracks the state of the board and the remaining ships
 * to be placed.</p>
 *
 * @see PositionTableInterface
 */
public class PositionTable implements PositionTableInterface, Serializable {
    /**
     * The 10x10 matrix representing the player's table where ships are placed.
     */
    private final int[][] positionTable = new int[10][10];

    /**
     * The 10x10 grid for tracking shots fired on the board.
     */
    private final int[][] shotGrid = new int[10][10];

    /**
     * A list that stores information about ships, including their size, type, and remaining amount.
     */
    ArrayList<Ship> ships = new ArrayList<Ship>();

    /**
     * A list of coordinates that stores the positions of all placed ships.
     */
    private final List<int[]> shipCoordinatesList = new ArrayList<>();

    /**
     * The constructor method of the positionTable class, it sets the ships inside the ArrayList on
     * creation.
     */
    public PositionTable() {
        ships.add(null);
        ships.add(new Ship(1,1, 4));
        ships.add(new Ship(2,2, 3));
        ships.add(new Ship(3,3, 2));
        ships.add(new Ship(4,4, 1));
    }

    /**
     * Returns the 10x10 board representing the player's ship positions.
     *
     * @return the position table as a 2D array.
     */
    @Override
    public int[][] getBoard() {
        return positionTable;
    }

    /**
     * Returns the 10x10 shot grid to track fired shots on the board.
     *
     * @return the shot grid as a 2D array.
     */
    @Override
    public int[][] getShotGrid() {
        return shotGrid;
    }

    /**
     * Returns the list of ships in the player's fleet.
     *
     * @return the list of ships.
     */
    @Override
    public ArrayList<Ship> getShips() {
        return ships;
    }

    /**
     * Sets the position of a ship on the board based on the given index, row, column, and orientation.
     *
     * @param shipIndex the index of the ship to be placed.
     * @param row the starting row where the ship should be placed.
     * @param col the starting column where the ship should be placed.
     * @param orientation the orientation of the ship (0 for horizontal, 1 for vertical).
     */
    @Override
    public void setShipPosition(int shipIndex,int row, int col, int orientation) {
        Ship ship = ships.get(shipIndex);
        int shipSize = ship.getShipSize();
        int shipType = ship.getShipType();

        //VERTICAL ORIENTATION
        if (orientation == 0){
            for (int i = row; i < shipSize + row; i++){
                positionTable[i][col] = shipType;
            }
            shipCoordinatesList.add(new int[]{row, col, row + shipSize - 1, col, orientation, shipType });
        }
        //HORIZONTAL ORIENTATION
        else if (orientation == 1){
            for (int j = col; j < shipSize + col; j++){
                positionTable[row][j] = shipType;
            }
            shipCoordinatesList.add(new int[]{row, col, row, col + shipSize - 1, orientation, shipType });
        }
        ship.setShipAmount(ship.getShipAmount() - 1);
    }

    /**
     * Checks if a ship can be placed at the specified position based on its orientation.
     *
     * @param shipIndex the index of the ship to be placed.
     * @param row the row where the ship should be placed.
     * @param col the column where the ship should be placed.
     * @param orientation the orientation of the ship (0 for horizontal, 1 for vertical).
     * @return true if the ship can be placed, false otherwise.
     */
    @Override
    public boolean checkPosition(int shipIndex, int row, int col, int orientation) {
        Ship ship = ships.get(shipIndex);
        int shipSize = ship.getShipSize();
        int checkCounter = 0;

        //VERTICAL ORIENTATION
        if (orientation == 0){
            for (int i = row; i < shipSize + row; i++){
                if (positionTable[i][col] == 0)
                    checkCounter++;
            }
        }
        //HORIZONTAL ORIENTATION
        else if (orientation == 1){
            for (int j = col; j < shipSize + col; j++){
                if (positionTable[row][j] == 0)
                    checkCounter++;
            }
        }

        if (checkCounter == shipSize)
            return true;
        return false;
    }

    /**
     * Checks if there are any remaining ships of the specified type to place.
     *
     * @param shipIndex the index of the ship to check.
     * @return true if there are ships remaining, false otherwise.
     */
    @Override
    public boolean checkAmount(int shipIndex){
        Ship ship = ships.get(shipIndex);
        int shipAmount = ship.getShipAmount();

        if (shipAmount == 0){
            return false;
        }
        return true;
    }

    /**
     * Prints the current state of the player's board to the console.
     */
    @Override
    public void printBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                System.out.print(positionTable[row][col] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Checks if all ships have been placed on the board.
     *
     * @return true if the board is fully populated, false otherwise.
     */
    @Override
    public boolean isBoardFull() {
        for (Ship ship : ships) {
            if (ship == null) continue;
            if (ship.getShipAmount() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the list of coordinates where ships have been placed on the board.
     *
     * @return the list of ship coordinates.
     */
    public List<int[]> getShipCoordinatesList() {
        return shipCoordinatesList;
    }
}
