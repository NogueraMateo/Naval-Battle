package com.example.navalbattle.controllers;


import com.example.navalbattle.models.GameModel;
import com.example.navalbattle.models.MainTable;
import com.example.navalbattle.models.PositionTable;
import com.example.navalbattle.views.ShipDrawer;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;

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

    @FXML
    private Button startGame;

    private final ShipDrawer drawer;
    private final MainTable mainTable;
    private PositionTable positionTable = new PositionTable();
    private final GameModel gameModel;

    private Integer gridPaneRow;
    private Integer gridPaneCol;
    private int shipType;
    private int shipOrientation = 1;

    private Group frigateGhost;
    private Group destroyerGhost;
    private Group submarineGhost;
    private Group aircraftGhost;
    private Group currentGhost;
    private Rotate rotate;

    private Image crosshairImg;
    private ImageView crosshairView;

    private EventHandler<MouseEvent> mouseEnteredHandler;
    private EventHandler<MouseEvent> mouseExitedHandler;
    private EventHandler<MouseEvent> mouseMovedHandler;

    /**
     * Constructs a new GameController and initializes a ShipDrawer
     * to handle the drawing of various ship types.
     */
    public GameController() {
        gameModel = new GameModel();
        drawer = new ShipDrawer();
        mainTable = new MainTable();

        crosshairImg = new Image(getClass().getResourceAsStream("/com/example/navalbattle/images/Scope.png"));
        crosshairView = new ImageView(crosshairImg);
        crosshairView.setFitWidth(55);
        crosshairView.setFitHeight(55);
    }

    @FXML
    private void initialize() {
        if (gameModel.existsPreviousMatch()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Previous Game in Progress");
            alert.setHeaderText(null);
            alert.setContentText("You have a previous game in progress. Do you want to load it?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                gameModel.loadPreviousMatch();
                setFleet(true);
                setFleet(false);

                if (!gameModel.getPositionTable().isBoardFull()) {
                    setCellsEvents();
                    setUpShipEvents();
                    setGhostShips();
                }
            } else {
                gameModel.newMatch();
                setCellsEvents();
                setUpShipEvents();
                setGhostShips();
            }

        } else {
            gameModel.newMatch();
            setCellsEvents();
            setUpShipEvents();
            setGhostShips();
        }

    }

    private void setGhostShips() {
        rotate = new Rotate(0, 0, 0);
        frigateGhost = drawer.drawFrigate(false);
        frigateGhost.getTransforms().add(rotate);
        destroyerGhost = drawer.drawDestroyer(false, false);
        destroyerGhost.getTransforms().add(rotate);
        submarineGhost = drawer.drawSubmarine(false, false);
        submarineGhost.getTransforms().add(rotate);
        aircraftGhost = drawer.drawAircraftCarrier(false, false);
        aircraftGhost.getTransforms().add(rotate);
    }

    private void setCellsEvents() {
        for (Node node : userFleet.getChildren()) {
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    placeShip(node);
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    changeOrientation();
                }
            });
        }
    }

    private void setUpShipEvents() {
        Group ship1 = drawer.drawFrigate(true);
        ship1.getStyleClass().add("ship");
        ship1.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {shipType = 1;
            showGhostShip(shipType);
            descriptionLabel.setText("Frigate: A fast and light ship, occupies 1 cells.");});

        Group ship2 = drawer.drawDestroyer(true, false);
        ship2.getStyleClass().add("ship");
        ship2.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            shipType = 2;
            showGhostShip(shipType);
            descriptionLabel.setText("Destroyer: An agile combat ship, occupies 2 cells.");});

        Group ship3 = drawer.drawSubmarine(true, false);
        ship3.getStyleClass().add("ship");
        ship3.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            shipType = 3;
            showGhostShip(shipType);
            descriptionLabel.setText("Submarine: A stealthy underwater attack ship, occupies 3 cells.");});

        Group ship4 = drawer.drawAircraftCarrier(true, false);
        ship4.getStyleClass().add("ship");
        ship4.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {shipType = 4;
            showGhostShip(shipType);
            descriptionLabel.setText("Carrier: A massive ship that carries aircraft, occupies 4 cells");});

        setSampleShips(ship1, ship2, ship3, ship4);

    }

    private void setSampleShips(Group ship1, Group ship2, Group ship3, Group ship4) {
        selectionGrid.add(ship1, 0, 0);
        selectionGrid.add(ship2, 0, 1);
        selectionGrid.add(ship3, 0, 2);
        selectionGrid.add(ship4, 0, 3);
    }


    /**
     * Places the machine's fleet on the board using the coordinates obtained
     * from the mainTable
     * Each ship is drawn on the board at the specified position and orientation.
     */
    public void setFleet(boolean machine) {
        List<int[]> shipCoordinates;
        if (machine) {
            shipCoordinates = gameModel.getMainTable().getShipCoordinatesList();
        } else {
            shipCoordinates = gameModel.getPositionTable().getShipCoordinatesList();
        }
        for (int[] coordinates : shipCoordinates) {
            int row = coordinates[0];
            int column = coordinates[1];
            boolean vertical = (coordinates[2] == 0);
            int type = coordinates[3];
            Group ship = drawShip(type, vertical);
            if (machine) {
                machinesFleet.add(ship, column, row);
            } else {
                userFleet.add(ship, column, row);
            }
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
            case 1 -> drawer.drawFrigate(false);
            case 2 -> drawer.drawDestroyer(vertical, true);
            case 3 -> drawer.drawSubmarine(vertical, true);
            case 4 -> drawer.drawAircraftCarrier(vertical, true);
            default -> null;
        };
    }


    private void placeShip(Node clickedNode) {
        boolean checkPosition = false, checkAmount = false;
        gridPaneRow = GridPane.getRowIndex(clickedNode);
        gridPaneCol = GridPane.getColumnIndex(clickedNode);

        if (gridPaneRow == null) gridPaneRow = 0;
        if (gridPaneCol == null) gridPaneCol = 0;

        try {
            checkPosition = gameModel.getPositionTable().checkPosition(shipType, gridPaneRow, gridPaneCol, shipOrientation);
        } catch (NullPointerException e) {
            showMessage("PLEASE SELECT A SHIP TO PLACE");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            showMessage("THE SHIP DOESN'T FIT" + ((shipOrientation == 0) ? "VERTICALLY" : "HORIZONTALLY"));
        }
        checkAmount = gameModel.getPositionTable().checkAmount(shipType);

        if (checkPosition) {
            if (checkAmount) {
                gameModel.getPositionTable().setShipPosition(shipType, gridPaneRow, gridPaneCol, shipOrientation);
                gameModel.saveGame();
                gameModel.getPositionTable().printBoard();
                switch (shipType){
                    case 1: Group frigate = drawer.drawFrigate(false);
                        userFleet.add(frigate, gridPaneCol, gridPaneRow);
                        break;
                    case 2: Group destroyer = drawer.drawDestroyer(shipOrientation == 0, true);
                        userFleet.add(destroyer, gridPaneCol, gridPaneRow);
                        break;
                    case 3: Group submarine = drawer.drawSubmarine(shipOrientation == 0, true);
                        userFleet.add(submarine, gridPaneCol, gridPaneRow);
                        break;
                    case 4: Group aircraft = drawer.drawAircraftCarrier(shipOrientation == 0, true);
                        userFleet.add(aircraft, gridPaneCol, gridPaneRow);
                        break;
                }
                updateCounter(shipType);
            }
            else
                showMessage("THERE IS NO AMOUNT OF THIS SHIP");
        }
        else
            showMessage("THERE IS A SHIP ALREADY");

        // Activates 'Start Game' Button
        if (gameModel.getPositionTable().isBoardFull()) {
            startGame.setDisable(false);
        }
    }

    /**
     * Decreases the ship amount by one on the interface
     * @param shipType
     */
    private void updateCounter(int shipType) {
        int currentAmount = 0;
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

        // If there are no ships left then no ghost is shown
        if (currentAmount == 0) {
            userFleet.getChildren().remove(currentGhost);
        }
    }

    private void changeOrientation() {
        if (shipOrientation == 1){
            rotate.setAngle(90);
            shipOrientation = 0;
        }
        else if (shipOrientation == 0) {
            rotate.setAngle(0);
            shipOrientation = 1;
        }
    }

    /**
     * Creates a Ghost version of the ship selected that follows the pointer
     * so the user has a preview of the ship on the grid.
     * @param shipType
     */
    private void showGhostShip(int shipType) {
        shipOrientation = 1;
        // If there is a ship already selected then it removes the current ghost
        if (currentGhost != null) {
            userFleet.getChildren().remove(currentGhost);
        }

        // If there are no ships left of the ship selected no ghost is shown
        if (!positionTable.checkAmount(shipType)) {
            return;
        }

        switch (shipType) {
            case 1: currentGhost = frigateGhost;
            break;
            case 2: currentGhost = destroyerGhost;
            break;
            case 3: currentGhost = submarineGhost;
            break;
            case 4: currentGhost = aircraftGhost;
            break;
        }

        if (currentGhost != null) {
            currentGhost.setOpacity(0.6);
            userFleet.getChildren().add(currentGhost);
        }

        // The ghost ship follows the pointer
        userFleet.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            currentGhost.setTranslateX(mouseX);
            currentGhost.setTranslateY(mouseY);

        });
    }

    private void showMessage(String msg) {
        messageLabel.setText(msg);
        messageLabel.setVisible(true);
        FadeTransition transition = new FadeTransition(Duration.seconds(2), messageLabel);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();
    }

    /**
     * This method sets some event filters to the Machine's Fleet so
     * a cross-hair is shown on the Machine's Fleet when the user
     * has to shoot.
     */
    private void setScopePointer() {
        machinesFleet.add(crosshairView, 0, 0);
        crosshairView.setVisible(false);

        mouseEnteredHandler = mouseEvent -> {
            machinesFleet.setCursor(Cursor.DISAPPEAR);
            crosshairView.setVisible(true);
        };

        mouseExitedHandler = mouseEvent-> {
            machinesFleet.setCursor(Cursor.DEFAULT);
            crosshairView.setVisible(false);
        };

        mouseMovedHandler = mouseEvent -> {
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            crosshairView.setTranslateX(mouseX - 20);
            crosshairView.setTranslateY(mouseY);};

        machinesFleet.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, mouseEnteredHandler);
        machinesFleet.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, mouseExitedHandler);
        machinesFleet.addEventFilter(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
    }

    /**
     * This method removes all the event filters set to the Machines Fleet.
     * This is supposed to be used when the machine has to shoot.
     */
    private void removeScopePointer() {
        machinesFleet.removeEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, mouseEnteredHandler);
        machinesFleet.removeEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, mouseExitedHandler);
        machinesFleet.removeEventFilter(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
    }
}