package com.example.navalbattle.views;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * The ShipDrawer class provides implementations for drawing various types of ships
 * used in the Battleship game. Each ship is represented as a Group composed of shapes
 * (such as rectangles and lines) with specific styles and dimensions.
 *
 * This class implements the ShipDrawerInterface, offering methods for creating
 * different types of ships (frigate, destroyer, submarine, and aircraft carrier)
 * with options for orientation (horizontal or vertical).
 *
 * The class also includes helper methods for creating basic shapes (rectangles
 * and lines) with specific properties, such as position, size, and color.
 * @author Mateo Noguera Pinto
 */
public class ShipDrawer implements ShipDrawerInterface{

    private final double SHIP_HEIGHT = 22.0;
    private final int CELL_SIZE = 30;

    @Override
    public Group drawFrigate(boolean vertical) {
        double shipLength = this.CELL_SIZE;
        Path mainContainer = new Path();

        mainContainer.getElements().addAll(
                new MoveTo(3, 3),
                new LineTo(shipLength - 10, 3),
                new LineTo(shipLength - 4, this.SHIP_HEIGHT / 2),
                new LineTo(shipLength - 10, this.SHIP_HEIGHT),
                new LineTo(3, this.SHIP_HEIGHT),
                new ArcTo(10, 10, 0, 3, 3, false, true)
        );

        mainContainer.setFill(Color.web("#D9D9D9"));
        mainContainer.setStroke(Color.TRANSPARENT);

        Path subContainer = new Path();
        subContainer.getElements().addAll(
                new MoveTo(4, 4),
                new LineTo(shipLength - 14, 4),
                new LineTo(shipLength - 6, this.SHIP_HEIGHT / 2),
                new LineTo(shipLength - 14, this.SHIP_HEIGHT - 1),
                new LineTo(4, this.SHIP_HEIGHT - 1),
                new ArcTo(10, 10, 0, 4, 4, false, true)
        );
        subContainer.setFill(Color.web("#61697A"));
        subContainer.setStroke(Color.TRANSPARENT);

        Rectangle smallWindow = rectangle((int) shipLength - 15, (int) SHIP_HEIGHT / 2 - 3, 4, 8, "#2B303B", "#00000000");
        Rectangle bigWindow = rectangle(3, 5, 8 , 16, "#2B303B", "#00000000");
        Group frigateGroup = new Group(mainContainer, subContainer, smallWindow, bigWindow);
        if (vertical) {
            frigateGroup.setRotate(90);
        }
        return frigateGroup;
    }

    @Override
    public Group drawDestroyer(boolean vertical, boolean insideGrid) {
        double shipLength = this.CELL_SIZE * 2;

        Path mainContainer = new Path();
        mainContainer.getElements().addAll(
                new MoveTo(2, 2),
                new LineTo(shipLength - 10, 2),
                new LineTo(shipLength, this.SHIP_HEIGHT / 2),
                new LineTo(shipLength - 10, this.SHIP_HEIGHT),
                new LineTo(2, this.SHIP_HEIGHT),
                new ArcTo(14, 14, 0, 2, 2, false, true)
        );
        mainContainer.setFill(Color.web("#D9D9D9"));
        mainContainer.setStroke(Color.TRANSPARENT);

        Rectangle bigWindow = rectangle(24, 4, 8, 16, "#2B303B", "#00000000");
        Path arrow = new Path();
        arrow.getElements().addAll(
                new MoveTo(36, 6),
                new LineTo(shipLength - 15, 6),
                new LineTo(shipLength - 5, this.SHIP_HEIGHT / 2),
                new LineTo(shipLength - 15, this.SHIP_HEIGHT - 4),
                new LineTo(36, this.SHIP_HEIGHT - 4),
                new LineTo(shipLength - 15, this.SHIP_HEIGHT / 2),
                new LineTo(36, 6)
        );
        arrow.setFill(Color.web("#2B303B"));
        arrow.setStroke(Color.TRANSPARENT);


        Rectangle rectangle = rectangle(4, 5, 18, 14, "#00000000", "#2B303B");
        Rectangle subRec = rectangle(7, 8, 9, 7, "#BEC0C3", "#2B303B");

        Circle circle = new Circle(12, 12, 1);
        circle.setFill(Color.web("#2B303B"));
        circle.setStroke(Color.TRANSPARENT);

        Group destroyerGroup = new Group(mainContainer, bigWindow, arrow, rectangle, subRec, circle);

        if (vertical) {
            destroyerGroup.setRotate(90);
            if (insideGrid) {
                destroyerGroup.setTranslateX(-16);
                destroyerGroup.setTranslateY(17);
            }
        }
        return destroyerGroup;
    }

    @Override
    public Group drawSubmarine(boolean vertical, boolean insideGrid) {
        double shipLength = this.CELL_SIZE * 3;
        Path mainContainer = new Path();
        mainContainer.getElements().addAll(
                new MoveTo(2, 2),
                new LineTo(shipLength - 10, 2),
                new LineTo(shipLength, this.SHIP_HEIGHT / 2),
                new LineTo(shipLength - 10, this.SHIP_HEIGHT),
                new LineTo(2, this.SHIP_HEIGHT),
                new ArcTo(14, 14, 0, 2, 2, false, true)
        );

        mainContainer.setFill(Color.web("#D9D9D9"));
        mainContainer.setStroke(Color.TRANSPARENT);

        Path arrow = new Path();
        arrow.getElements().addAll(
                new MoveTo(60, 6),
                new LineTo(shipLength - 15, 6),
                new LineTo(shipLength - 5, this.SHIP_HEIGHT / 2),
                new LineTo(shipLength - 15, this.SHIP_HEIGHT - 4),
                new LineTo(60, this.SHIP_HEIGHT - 4),
                new LineTo(shipLength - 15, this.SHIP_HEIGHT / 2),
                new LineTo(60, 6)
        );
        arrow.setFill(Color.web("#2B303B"));
        arrow.setStroke(Color.TRANSPARENT);

        Rectangle bigWindow = rectangle(50, 4, 8, 16, "#2B303B", "#00000000");
        Rectangle rectangle = rectangle(30, 5, 18, 14, "#00000000", "#2B303B");
        Rectangle subRec = rectangle(34, 8, 9, 7, "#BEC0C3", "#2B303B");

        Circle circle = new Circle(40, 12, 1);
        circle.setFill(Color.web("#2B303B"));
        circle.setStroke(Color.TRANSPARENT);

        Circle bigCircle = new Circle(17, 12, 5);
        circle.setFill(Color.web("#2B303B"));
        bigCircle.setStroke(Color.TRANSPARENT);

        Line line = drawLine(2, SHIP_HEIGHT / 2 + 1, 12, SHIP_HEIGHT / 2 + 1, "#2B303B", 1.0);

        Group submarineGroup = new Group(mainContainer, arrow, bigWindow, rectangle, subRec, circle, bigCircle, line);

        if (vertical) {
            submarineGroup.setRotate(90);
            if (insideGrid) {
                submarineGroup.setTranslateX(-31);
                submarineGroup.setTranslateY(32);
            }
        }
        return submarineGroup;
    }

    @Override
    public Group drawAircraftCarrier(boolean vertical, boolean insideGrid) {
        double shipLength = this.CELL_SIZE * 4 + 8;

        Path mainContainer = new Path();
        mainContainer.getElements().addAll(
                new MoveTo(2, 2),
                new LineTo(shipLength - 10, 2),
                new LineTo(shipLength, this.SHIP_HEIGHT / 2),
                new LineTo(shipLength - 10, this.SHIP_HEIGHT + 2),
                new LineTo(2, this.SHIP_HEIGHT +2),
                new ArcTo(14, 14, 0, 2, 2, false, true)
        );

        Path subContainer = new Path();
        subContainer.getElements().addAll(
                new MoveTo(4, 5),
                new LineTo(shipLength - 14, 4),
                new LineTo(shipLength - 6, this.SHIP_HEIGHT / 2),
                new LineTo(shipLength - 14, this.SHIP_HEIGHT - 2),
                new LineTo(4, this.SHIP_HEIGHT - 2),
                new ArcTo(10, 10, 0, 4, 5, false, true)
        );
        subContainer.setFill(Color.web("#61697A"));
        subContainer.setStroke(Color.TRANSPARENT);

        mainContainer.setFill(Color.web("#D9D9D9"));
        mainContainer.setStroke(Color.TRANSPARENT);

        Rectangle bigWindow = rectangle((int) shipLength - 24, 6, 10, 12, "#2B303B", "#00000000");

        Circle windowHole = new Circle(shipLength - 19, 10, 3);
        windowHole.setFill(Color.web("#D9D9D9"));
        windowHole.setStroke(Color.TRANSPARENT);

        Line windowHBar = drawLine(shipLength - 22, 10, shipLength -22 + 6, 10, "#000", 0.5);
        Line windowVBar = drawLine(shipLength - 19, 5, shipLength - 19, 15, "#000", 0.5);
        Rectangle rect = rectangle(30, 5, 40, SHIP_HEIGHT - 7, "2B303B", "#00000000");

        Path plane = new Path();
        plane.getElements().addAll(
                new MoveTo(35, 8),
                new LineTo(45, 8),
                new LineTo(55, this.SHIP_HEIGHT / 2),
                new LineTo(45, this.SHIP_HEIGHT - 7),
                new LineTo(35, this.SHIP_HEIGHT - 7),
                new LineTo(45, this.SHIP_HEIGHT / 2),
                new LineTo(35, 8)
        );
        plane.setFill(Color.web("#DEDEDE"));
        plane.setStroke(Color.TRANSPARENT);

        Line planeLine = drawLine(45, SHIP_HEIGHT / 2, 55, SHIP_HEIGHT / 2, "#000", 0.5);
        Line planeDiagonalLine = drawLine(40, SHIP_HEIGHT - 7,  50, SHIP_HEIGHT/2, "#000", 0.5);
        Line planeDiagonalLine2 = drawLine(40, 8, 50, SHIP_HEIGHT/2, "#000", 0.5);
        Line rope1 = drawLine(5, 8, 30, 8, "#2B303B", 1);
        Line rope2 = drawLine(5, SHIP_HEIGHT - 7, 30, SHIP_HEIGHT - 7, "#2B303B", 1);
        Line crossVLine = drawLine(12, 10, 12, 14, "#fff", 1);
        Line crossHLine = drawLine(10, 11, 14, 11, "#fff", 1);

        Circle c1 = new Circle(8, 3, 1.5);
        c1.setFill(Color.web("#2B303B"));

        Circle c2 = new Circle(40, 3, 1.5);
        c2.setFill(Color.web("#2B303B"));

        Circle c3 = new Circle(8, this.SHIP_HEIGHT - 1, 1.5);
        c3.setFill(Color.web("#2B303B"));

        Circle c4 = new Circle(40, this.SHIP_HEIGHT - 1, 1.5);
        c4.setFill(Color.web("#2B303B"));


        Group aircraftCarrierGroup = new Group(mainContainer, subContainer, bigWindow, windowHole, windowHBar, windowVBar,
                rect, plane, planeLine, planeDiagonalLine, planeDiagonalLine2, rope1, rope2, crossVLine, crossHLine, c1, c2, c3, c4);

        if (vertical) {
            aircraftCarrierGroup.setRotate(90);
            if (insideGrid) {
                aircraftCarrierGroup.setTranslateX(-51);
                aircraftCarrierGroup.setTranslateY(50);
            }
        }

        return aircraftCarrierGroup;
    }

    public Group drawBomb() {
        Path fireStar = new Path();
        fireStar.getElements().addAll(
                new MoveTo(10, 6),
                new LineTo(7, 3),
                new LineTo(10, 3),
                new LineTo(13, -2),
                new LineTo(16, 3),
                new LineTo(19, 3),
                new LineTo(16, 6),
                new LineTo(19, 11),
                new LineTo(13, 6),
                new LineTo(7, 11),
                new LineTo(10, 6)
        );
        fireStar.setStroke(Color.web("#e16717"));
        fireStar.setFill(Color.web("#fecb00"));

        Circle baseCircle = new Circle(SHIP_HEIGHT / 2);
        baseCircle.setFill(Color.web("#000"));
        baseCircle.setStroke(Color.web("#fff"));
        baseCircle.setStrokeWidth(1);
        baseCircle.setTranslateY(10);
        return new Group(baseCircle, fireStar);
    }

    public Group drawMissedShot() {
        Line diagonal1 = drawLine(CELL_SIZE - 28, CELL_SIZE - 2, CELL_SIZE - 2, 6, "#ac1b1b", 3);
        Line diagonal2 = drawLine(6, 6, CELL_SIZE - 2, CELL_SIZE - 2, "#ac1b1b", 3);

        return new Group(diagonal1, diagonal2);

    }

    @Override
    public Rectangle rectangle(int x, int y, double width, double height, String fillHex, String strokeHex) {
        Rectangle bigWindow = new Rectangle(width, height);
        bigWindow.setFill(Color.web(fillHex));
        bigWindow.setStroke(Color.web(strokeHex));
        bigWindow.setX(x);
        bigWindow.setY(y);

        return bigWindow;
    }

    @Override
    public Line drawLine(double fromX, double fromY, double toX, double toY, String color, double strokeWidth) {
        Line line = new Line(fromX, fromY, toX, toY);
        line.setStrokeWidth(strokeWidth);
        line.setStroke(Color.web(color));
        return line;
    }

    public Group drawFire() {
        Path fireStar = new Path();
        fireStar.getElements().addAll(
                new MoveTo(10, 6),
                new LineTo(7, 3),
                new LineTo(10, 3),
                new LineTo(13, -2),
                new LineTo(16, 3),
                new LineTo(19, 3),
                new LineTo(16, 6),
                new LineTo(19, 11),
                new LineTo(13, 6),
                new LineTo(7, 11),
                new LineTo(10, 6)
        );
        fireStar.setStroke(Color.web("#e16717"));
        fireStar.setFill(Color.web("#ff0000"));

        Circle baseCircle = new Circle(SHIP_HEIGHT / 2);
        baseCircle.setFill(Color.web("#ff0000"));
        baseCircle.setStroke(Color.web("#ff0000"));
        baseCircle.setStrokeWidth(1);
        baseCircle.setTranslateY(10);
        return new Group(baseCircle, fireStar);
    }
}
