package com.example.navalbattle.interfaces;

public interface ShipInterface {
    /**
     * Returns the size of the ship.
     *
     * @return the size of the ship.
     */
    int getShipSize();

    /**
     * Returns the type of the ship.
     *
     * @return the type of the ship.
     */
    int getShipType();

    /**
     * Returns the amount of this type of ship.
     *
     * @return the amount of this type of ship.
     */
    int getShipAmount();

    /**
     * Sets the amount of this type of ship.
     *
     * @param shipAmount the new amount of this type of ship.
     */
    void setShipAmount(int shipAmount);
}
