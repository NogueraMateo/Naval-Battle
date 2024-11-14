package com.example.navalbattle.models;

import javafx.scene.Group;

import java.util.ArrayList;

/**
 * The PositionTable class represents the player's table there could be placed all the player's ships
 * it has 10x10 grid with the information of the player's ships of varying size in the ships arraylist
 */

public class PositionTable {
    /**
     * It's the 10x10 matrix that represents the player's table where the ships are going to be placed
     */
    private int[][] positionTable = new int[10][10];
    /**
     * ships is an ArrayList that stores the information of the ships, the size, the amount and the type
     */
    ArrayList<Ship> ships = new ArrayList<Ship>();

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

    public PositionTable(int[][] positionTable) {
        ships.add(null);
        ships.add(new Ship(1,1, 0));
        ships.add(new Ship(2,2, 0));
        ships.add(new Ship(3,3, 0));
        ships.add(new Ship(4,4, 0));



        this.positionTable = positionTable;
        System.out.println("Previous position table loaded");
        this.printBoard();
    }

    public int[][] getPositionTable() {
        return positionTable;
    }

    /**
     * Allows the player to place the ship in the matrix if there's enough ships to be placed and
     * if the ship is not overlapping.
     * @param shipIndex is the ship that's going to be placed
     * @param row is the row where the ship is going to be placed
     * @param col is the column where the ship is going to be placed
     * @param orientation is the orientation of the ship, 0 if is horizontal or 1 if is vertical.
     */
    public void setShipPosition(int shipIndex,int row, int col, int orientation) {
        Ship ship = ships.get(shipIndex);
        int shipSize = ship.getShipSize();
        int shipType = ship.getShipType();

        //VERTICAL ORIENTATION
        if (orientation == 1){
            for (int i = row; i < shipSize + row; i++){
                positionTable[i][col] = shipType;
            }
        }
        //HORIZONTAL ORIENTATION
        else if (orientation == 0){
            for (int j = col; j < shipSize + col; j++){
                positionTable[row][j] = shipType;
            }
        }
        ship.setShipAmount(ship.getShipAmount() - 1);
    }

    /**
     *Check the position where the ship is going to be placed.
     * @param shipIndex is the ship that's going to be placed
     * @param row is the row where the ship is going to be placed
     * @param col is the column where the ship is going to be placed
     * @param orientation is the orientation of the ship, 0 if is horizontal or 1 if is vertical.
     * @return
     */
    public boolean checkPosition(int shipIndex, int row, int col, int orientation) {
        Ship ship = ships.get(shipIndex);
        int shipSize = ship.getShipSize();
        int checkCounter = 0;

        //VERTICAL ORIENTATION
        if (orientation == 1){
            for (int i = row; i < shipSize + row; i++){
                if (positionTable[i][col] == 0)
                    checkCounter++;
            }
        }
        //HORIZONTAL ORIENTATION
        else if (orientation == 0){
            for (int j = col; j < shipSize + col; j++){
                if (positionTable[row][j] == 0)
                    checkCounter++;
            }
        }

        if (checkCounter == shipSize)
            return true;
        return false;
    }

    public boolean checkAmount(int shipIndex){
        Ship ship = ships.get(shipIndex);
        int shipAmount = ship.getShipAmount();

        if (shipAmount == 0){
            return false;
        }
        return true;
    }

    /**
     * Prints the player's board
     */
    public void printBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                System.out.print(positionTable[row][col] + " ");
            }
            System.out.println();
        }
    }

    public boolean isBoardFull() {
        for (Ship ship : ships) {
            if (ship == null) continue;
            if (ship.getShipAmount() > 0) {
                return false;
            }
        }
        return true;
    }
}
