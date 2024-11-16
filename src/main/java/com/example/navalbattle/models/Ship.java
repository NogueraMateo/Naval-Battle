package com.example.navalbattle.models;

import java.io.Serializable;

public class Ship implements Serializable {
    int shipSize;
    int shipType;
    int shipAmount;

    public Ship(int shipType, int shipSize, int shipAmount) {
        this.shipSize = shipSize;
        this.shipType = shipType;
        this.shipAmount = shipAmount;
    }

    public int getShipSize() {
        return shipSize;
    }

    public int getShipType() {
        return shipType;
    }

    public int getShipAmount() {
        return shipAmount;
    }

    public void setShipAmount(int shipAmount) {
        this.shipAmount = shipAmount;
    }
}