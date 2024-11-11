package com.example.navalbattle.controllers;


import com.example.navalbattle.views.ShipDrawer;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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

    private final ShipDrawer drawer;

    /**
     * Constructs a new GameController and initializes a ShipDrawer
     * to handle the drawing of various ship types.
     */
    public GameController() {
        drawer = new ShipDrawer();
    }

    @FXML
    private void initialize() {

        Group ship1 = drawer.drawFrigate();
        ship1.getStyleClass().add("ship");
        Group ship2 = drawer.drawDestroyer(false);
        ship2.getStyleClass().add("ship");
        Group ship3 = drawer.drawSubmarine(false);
        ship3.getStyleClass().add("ship");
        Group ship4 = drawer.drawAircraftCarrier(false);
        ship4.getStyleClass().add("ship");
        setUpToolTips(ship1, ship2, ship3, ship4);
        shipsContainer.getChildren().addAll(ship1, ship2, ship3, ship4);

        Group frigate = drawer.drawFrigate();
        userFleet.add(frigate, 0, 0);

        Group destroyer = drawer.drawDestroyer(false);
        userFleet.add(destroyer, 0, 1);

        Group submarine = drawer.drawSubmarine(false);
        userFleet.add(submarine, 0, 2);

        Group aircraft = drawer.drawAircraftCarrier(false);
        userFleet.add(aircraft, 0,3 );

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
}
