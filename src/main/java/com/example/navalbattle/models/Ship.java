package com.example.navalbattle.models;

import com.example.navalbattle.interfaces.ShipInterface;

import java.io.Serializable;

/**
 * The Ship class represents a ship used in the Naval Battle game.
 * It holds the size, type, and amount of a particular type of ship.
 * This class implements the {@link ShipInterface} interface and can be serialized.
 *
 * <p>This class is used to define the properties of a ship and manipulate the
 * number of ships of a particular type during the game.</p>
 *
 * @see ShipInterface
 */
public class Ship implements ShipInterface,Serializable {
    int shipSize;
    int shipType;
    int shipAmount;

    /**
     * Constructs a Ship object with the specified ship type, size, and amount.
     *
     * @param shipType   the type of the ship (e.g., 1 for aircraft carrier, 2 for submarine).
     * @param shipSize   the size of the ship (e.g., 5 for aircraft carrier, 3 for submarine).
     * @param shipAmount the amount of ships of this type.
     */
    public Ship(int shipType, int shipSize, int shipAmount) {
        this.shipSize = shipSize;
        this.shipType = shipType;
        this.shipAmount = shipAmount;
    }

    /**
     * Returns the size of the ship.
     *
     * @return the size of the ship.
     */
    @Override
    public int getShipSize() {
        return shipSize;
    }

    /**
     * Returns the type of the ship.
     *
     * @return the type of the ship.
     */
    @Override
    public int getShipType() {
        return shipType;
    }

    /**
     * Returns the amount of ships of this type.
     *
     * @return the amount of ships of this type.
     */
    @Override
    public int getShipAmount() {
        return shipAmount;
    }

    /**
     * Sets the amount of ships of this type.
     *
     * @param shipAmount the new amount of ships of this type.
     */
    @Override
    public void setShipAmount(int shipAmount) {
        this.shipAmount = shipAmount;
    }
}