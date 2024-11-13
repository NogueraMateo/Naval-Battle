package com.example.navalbattle.controllers;


import com.example.navalbattle.models.MainTable;
import com.example.navalbattle.models.PositionTable;
import com.example.navalbattle.views.ShipDrawer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

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
    private HBox shipsContainer;

    @FXML
    private Button orientationButton;

    private final ShipDrawer drawer;

    private final MainTable mainTable;

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
        mainTable = new MainTable();
    }

    @FXML
    private void initialize() {

        Group ship1 = drawer.drawFrigate();
        ship1.getStyleClass().add("ship");
        ship1.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {shipType = 1;
            System.out.println(shipType);});

        Group ship2 = drawer.drawDestroyer(false);
        ship2.getStyleClass().add("ship");
        ship2.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {shipType = 2;
            System.out.println(shipType);});

        Group ship3 = drawer.drawSubmarine(false);
        ship3.getStyleClass().add("ship");
        ship3.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {shipType = 3;
            System.out.println(shipType);});

        Group ship4 = drawer.drawAircraftCarrier(false);
        ship4.getStyleClass().add("ship");
        ship4.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {shipType = 4;
            System.out.println(shipType);});

        setUpToolTips(ship1, ship2, ship3, ship4);
        shipsContainer.getChildren().addAll(ship1, ship2, ship3, ship4);
    }


    /**
     * Sets up tooltips for each ship in the sample container.
     * Each tooltip provides brief information about the ship's type,
     * size, and specific capabilities.
     *
     * @param frigate the Group representing the frigate ship
     * @param destroyer the Group representing the destroyer ship
     * @param submarine the Group representing the submarine ship
     * @param aircraft the Group representing the aircraft carrier ship
     */
    public void setUpToolTips(Group frigate, Group destroyer, Group submarine, Group aircraft) {
        Tooltip frigateToolTip = new Tooltip("Frigate:\n- Size: 1 cell\n- Agile and small");
        frigateToolTip.getStyleClass().add("tooltip");
        Tooltip.install(frigate, frigateToolTip);

        Tooltip destroyerTooltip = new Tooltip("Destroyer:\n- Size: 2 cells\n- Fast and versatile");
        destroyerTooltip.getStyleClass().add("tooltip");
        Tooltip.install(destroyer, destroyerTooltip);

        Tooltip submarineTooltip = new Tooltip("Submarine:\n- Size: 3 cells\n- Deep maneuvering capability");
        submarineTooltip.getStyleClass().add("tooltip");
        Tooltip.install(submarine, submarineTooltip);

        Tooltip carrierTooltip = new Tooltip("Aircraft carrier:\n- Size: 4 cells\n- The biggest ship, provides air support");
        carrierTooltip.getStyleClass().add("tooltip");
        Tooltip.install(aircraft, carrierTooltip);
    }


    /**
     * Places the machine's fleet on the board using the coordinates obtained
     * from the mainTable
     * Each ship is drawn on the board at the specified position and orientation.
     */
    public void setMachinesFleet() {
        List<int[]> shipCoordinates = mainTable.getShipCoordinatesList();
        for (int[] coordinates : shipCoordinates) {
            int row = coordinates[0];
            int column = coordinates[1];
            boolean vertical = (coordinates[4] == 0);
            int type = coordinates[5];
            Group ship = drawShip(type, vertical);
            machinesFleet.add(ship, column, row);
        }
    }

    /**
     * Draws the ship corresponding to the specified type and orientation.
     *
     * @param cell The type of ship (1 = Frigate, 2 = Destroyer, 3 = Submarine, 4 = Aircraft Carrier).
     * @param vertical Determines if the ship is vertical (true) or horizontal (false).
     * @return A {@link Group} object representing the ship to be added to the graphical user interface.
     */
    private Group drawShip(int cell, boolean vertical) {
        return switch (cell) {
            case 1 -> drawer.drawFrigate();
            case 2 -> drawer.drawDestroyer(vertical);
            case 3 -> drawer.drawSubmarine(vertical);
            case 4 -> drawer.drawAircraftCarrier(vertical);
            default -> null;
        };
    }


    @FXML
    private void getGridPaneCoordinates(MouseEvent event) {
        Node clickedNode = (Node) event.getSource();
        gridPaneRow = GridPane.getRowIndex(clickedNode);
        gridPaneCol = GridPane.getColumnIndex(clickedNode);

        if (gridPaneRow == null) gridPaneRow = 0;
        if (gridPaneCol == null) gridPaneCol = 0;

        boolean checkPosition = positionTable.checkPosition(shipType, gridPaneRow, gridPaneCol, shipOrientation);
        boolean checkAmount = positionTable.checkAmount(shipType);
        boolean orientation = false;

        if (shipOrientation == 0) orientation = false;
        else orientation = true;

        if (checkPosition) {
            if (checkAmount) {
                positionTable.setShipPosition(shipType, gridPaneRow, gridPaneCol, shipOrientation);
                System.out.println("++++++++++++++++++++++++++MOVEMENT++++++++++++++++++++++++++");
                positionTable.printBoard();
                switch (shipType){
                    case 1: Group frigate = drawer.drawFrigate();
                        userFleet.add(frigate, gridPaneCol, gridPaneRow);
                        break;
                    case 2: Group destroyer = drawer.drawDestroyer(orientation);
                        userFleet.add(destroyer, gridPaneCol, gridPaneRow);
                        break;
                    case 3: Group submarine = drawer.drawSubmarine(orientation);
                        userFleet.add(submarine, gridPaneCol, gridPaneRow);
                        break;
                    case 4: Group aircraft = drawer.drawAircraftCarrier(orientation);
                        userFleet.add(aircraft, gridPaneCol, gridPaneRow);
                        break;
                }
            }
            else
                System.out.println("THERE IS NO AMOUNT OF THIS SHIP");
        }
        else
            System.out.println("THERE IS A SHIP ALREADY");
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
}
