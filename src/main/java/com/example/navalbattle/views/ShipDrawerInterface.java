package com.example.navalbattle.views;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * Interface defining the structure for drawing different types of ships and
 * basic shapes used in the Battleship game.
 * @author Mateo Noguera Pinto
 */
public interface ShipDrawerInterface {

    /**
     * Draws a frigate, represented as a Group with a single cell size.
     *
     * @return a Group representing the frigate
     */
    Group drawFrigate(boolean vertical);

    /**
     * Draws a destroyer, represented as a Group with a size of two cells.
     *
     * @param vertical specifies if the ship should be drawn vertically; if false, the ship will be horizontal
     * @return a Group representing the destroyer
     */
    Group drawDestroyer(boolean vertical, boolean insideGrid);

    /**
     * Draws a submarine, represented as a Group with a size of three cells.
     *
     * @param vertical specifies if the ship should be drawn vertically; if false, the ship will be horizontal
     * @return a Group representing the submarine
     */
    Group drawSubmarine(boolean vertical, boolean insideGrid);

    /**
     * Draws an aircraft carrier, represented as a Group with a size of four cells.
     *
     * @param vertical specifies if the ship should be drawn vertically; if false, the ship will be horizontal
     * @return a Group representing the aircraft carrier
     */
    Group drawAircraftCarrier(boolean vertical, boolean insideGrid);

    /**
     * Creates a rectangle with specified dimensions, position, and color properties.
     *
     * @param x         the x-coordinate of the rectangle's position
     * @param y         the y-coordinate of the rectangle's position
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     * @param fillHex   the fill color of the rectangle in hexadecimal format
     * @param strokeHex the stroke (border) color of the rectangle in hexadecimal format
     * @return a Rectangle object with the specified properties
     */
    Rectangle rectangle(int x, int y, double width, double height, String fillHex, String strokeHex);

    /**
     * Draws a line with specified start and end coordinates, color, and stroke width.
     *
     * @param fromX       the starting x-coordinate of the line
     * @param fromY       the starting y-coordinate of the line
     * @param toX         the ending x-coordinate of the line
     * @param toY         the ending y-coordinate of the line
     * @param color       the color of the line in hexadecimal format
     * @param strokeWidth the width of the line
     * @return a Line object with the specified properties
     */
    Line drawLine(double fromX, double fromY, double toX, double toY, String color, double strokeWidth);
}
