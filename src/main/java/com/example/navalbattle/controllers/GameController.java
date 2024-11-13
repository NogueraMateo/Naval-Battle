package com.example.navalbattle.controllers;


import com.example.navalbattle.models.PositionTable;
import com.example.navalbattle.views.ShipDrawer;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.sql.Array;
import java.sql.Time;

/**
 * The GameController interacts with the view to place ships,
 * initialize the player's fleet, and handle tooltips to enhance
 * the user experience.
 * @author Mateo Noguera Pinto
 */
public class GameController {
    @FXML
    private GridPane userFleet;

    @FXML
    private GridPane machinesFleet;

    @FXML
    private Button orientationButton;

    @FXML
    private GridPane selectionGrid;

    @FXML
    private Label frigateCounter;

    @FXML
    private Label destroyerCounter;

    @FXML
    private Label submarineCounter;

    @FXML
    private Label aircraftCounter;

    @FXML
    private Label messageLabel;

    @FXML
    private Label descriptionLabel;

    private final ShipDrawer drawer;
    private PositionTable positionTable = new PositionTable();

    private Integer gridPaneRow;
    private Integer gridPaneCol;
    private int shipType;
    private int shipOrientation = 0;

    /**
     * Constructs a new GameController and initializes a ShipDrawer
     * to handle the drawing of various ship types.
     */
    public GameController() {
        drawer = new ShipDrawer();
    }

    @FXML
    private void initialize() {
        setUpShipEvents();
    }

    private void setUpShipEvents() {
        Group ship1 = drawer.drawFrigate(true);
        ship1.getStyleClass().add("ship");
        ship1.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {shipType = 1;
            descriptionLabel.setText("Frigate: A fast and light ship, occupies 1 cells.");
            System.out.println(shipType);});

        Group ship2 = drawer.drawDestroyer(true, false);
        ship2.getStyleClass().add("ship");
        ship2.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            shipType = 2;
            descriptionLabel.setText("Destroyer: An agile combat ship, occupies 2 cells.");
            System.out.println(shipType);});

        Group ship3 = drawer.drawSubmarine(true, false);
        ship3.getStyleClass().add("ship");
        ship3.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            shipType = 3;
            descriptionLabel.setText("Submarine: A stealthy underwater attack ship, occupies 3 cells.");
            System.out.println(shipType);});

        Group ship4 = drawer.drawAircraftCarrier(true, false);
        ship4.getStyleClass().add("ship");
        ship4.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {shipType = 4;
            descriptionLabel.setText("Carrier: A massive ship that carries aircraft, occupies 4 cells");
            System.out.println(shipType);});

        setSampleShips(ship1, ship2, ship3, ship4);

    }

    private void setSampleShips(Group ship1, Group ship2, Group ship3, Group ship4) {
        selectionGrid.add(ship1, 0, 0);
        selectionGrid.add(ship2, 0, 1);
        selectionGrid.add(ship3, 0, 2);
        selectionGrid.add(ship4, 0, 3);
    }


    @FXML
    private void getGridPaneCoordinates(MouseEvent event) {
        boolean checkPosition = false, checkAmount = false;
        Node clickedNode = (Node) event.getSource();
        gridPaneRow = GridPane.getRowIndex(clickedNode);
        gridPaneCol = GridPane.getColumnIndex(clickedNode);

        if (gridPaneRow == null) gridPaneRow = 0;
        if (gridPaneCol == null) gridPaneCol = 0;

        try {
            checkPosition = positionTable.checkPosition(shipType, gridPaneRow, gridPaneCol, shipOrientation);
        } catch (NullPointerException e) {
            showMessage("PLEASE SELECT A SHIP TO PLACE", "error");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            showMessage("THE SHIP DOESN'T FIT" + ((shipOrientation == 0) ? "VERTICALLY" : "HORIZONTALLY"), "error");
        }
        checkAmount = positionTable.checkAmount(shipType);

        boolean orientation = false;

        if (shipOrientation == 0) orientation = false;
        else orientation = true;

        if (checkPosition) {
            if (checkAmount) {
                positionTable.setShipPosition(shipType, gridPaneRow, gridPaneCol, shipOrientation);
                System.out.println("++++++++++++++++++++++++++MOVEMENT++++++++++++++++++++++++++");
                positionTable.printBoard();
                switch (shipType){
                    case 1: Group frigate = drawer.drawFrigate(false);
                        userFleet.add(frigate, gridPaneCol, gridPaneRow);
                        updateCounter(1);
                        break;
                    case 2: Group destroyer = drawer.drawDestroyer(orientation, true);
                        userFleet.add(destroyer, gridPaneCol, gridPaneRow);
                        updateCounter(2);
                        break;
                    case 3: Group submarine = drawer.drawSubmarine(orientation, true);
                        userFleet.add(submarine, gridPaneCol, gridPaneRow);
                        updateCounter(3);
                        break;
                    case 4: Group aircraft = drawer.drawAircraftCarrier(orientation, true);
                        userFleet.add(aircraft, gridPaneCol, gridPaneRow);
                        updateCounter(4);
                        break;
                }
            }
            else
                showMessage("THERE IS NO AMOUNT OF THIS SHIP", "error");
        }
        else
            showMessage("THERE IS A SHIP ALREADY", "error");
    }

    private void updateCounter(int shipType) {
        int currentAmount;
        switch (shipType) {
            case 1:
                currentAmount = Character.getNumericValue(frigateCounter.getText().charAt(1));
                currentAmount--;
                frigateCounter.setText("x" + currentAmount);
                break;
            case 2:
                currentAmount = Character.getNumericValue(destroyerCounter.getText().charAt(1));
                currentAmount--;
                destroyerCounter.setText("x" + currentAmount);
                break;
            case 3:
                currentAmount = Character.getNumericValue(submarineCounter.getText().charAt(1));
                currentAmount--;
                submarineCounter.setText("x" + currentAmount);
                break;
            case 4:
                currentAmount = Character.getNumericValue(aircraftCounter.getText().charAt(1));
                currentAmount--;
                aircraftCounter.setText("x" + currentAmount);
                break;
        }
    }

    @FXML
    private void getShipOrientation(ActionEvent event) {
        if (shipOrientation == 0){
            orientationButton.setText("Orientation: Vertical");
            shipOrientation = 1;
        }
        else if (shipOrientation == 1){
            orientationButton.setText("Orientation: Horizontal");
            shipOrientation = 0;
        }
        System.out.println(shipOrientation);
    }

    private void showMessage(String msg, String msgType) {
        messageLabel.setText(msg);
        messageLabel.setVisible(true);
        messageLabel.getStyleClass().removeAll();
        messageLabel.getStyleClass().add(msgType);
        FadeTransition transition = new FadeTransition(Duration.seconds(2), messageLabel);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();
    }
}