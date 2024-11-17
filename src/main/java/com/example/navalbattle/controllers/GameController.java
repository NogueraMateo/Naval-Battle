package com.example.navalbattle.controllers;

import com.example.navalbattle.models.GameModel;
import com.example.navalbattle.models.Ship;
import com.example.navalbattle.views.GameView;
import com.example.navalbattle.views.ShipDrawer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private GridPane userFleet, machinesFleet, selectionGrid;

    @FXML
    private Label destroyerCounter, frigateCounter, submarineCounter, aircraftCounter, messageLabel, descriptionLabel, labelPlayerName;

    @FXML
    private Button fireButton, playAgain, startGame;

    @FXML
    private Button revealFleet;

    @FXML
    private HBox revealFleetContainer;

    private final ShipDrawer drawer;
    private GameModel gameModel;

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

    private boolean playerTurn = false;
    private String username;
    private boolean successfulShot = false;

    /**
     * Constructs a new GameController and initializes a ShipDrawer
     * to handle the drawing of various ship types.
     */
    public GameController() {
        gameModel = new GameModel();
        drawer = new ShipDrawer();

        crosshairImg = new Image(getClass().getResourceAsStream("/com/example/navalbattle/images/Scope.png"));
        crosshairView = new ImageView(crosshairImg);
        crosshairView.setFitWidth(55);
        crosshairView.setFitHeight(55);
    }

    /**
     * Initializes the game by setting the player's username and checking for any previous match.
     * If a previous match exists, it loads it; otherwise, it initializes a new match.
     *
     * @param username the username of the player
     */
    @FXML
    public void initialize(String username) {
        this.username = username;
        labelPlayerName.setText(username + "'s Fleet");
        if (gameModel.existsPreviousMatch(username)) {
            handlePreviousMatch();
        } else {
            initializeNewMatch();
        }
    }

    /**
     * Handles the player's shoot turn. Sets the player's turn flag to true and triggers turn management.
     *
     * @param event the action event triggered by the player
     */
    @FXML
    private void playerShootTurn(ActionEvent event) {
        playerTurn = true;
        turnManagement();
    }

    /**
     * Starts the game by disabling the start button, making the fire button visible,
     * disabling the user fleet controls, and printing the main board.
     *
     * @param event the action event triggered by clicking the start button
     */
    @FXML
    private void startButton(ActionEvent event) {
        startGame.setDisable(true);
        fireButton.setVisible(true);
        userFleet.setDisable(true);
    }

    /**
     * Handles the case where there is a previous match in progress.
     * Asks the player if they want to load the previous match or start a new one.
     */
    private void handlePreviousMatch() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Previous Game in Progress");
        alert.setHeaderText(null);
        alert.setContentText("You have a previous game in progress. Do you want to load it?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            loadPreviousMatch();
        } else {
            initializeNewMatch();
        }
    }

    /**
     * Loads the previous match, sets the fleet, updates labels, and sets up events for ships and bombs.
     */
    private void loadPreviousMatch() {
        gameModel.loadPreviousMatch();
        setFleet(false);
        updateLabels();
        setCellsEvents();
        setUpShipEvents();
        setGhostShips();
        setBombs();
        createShotHandlers();

        if (gameModel.getPositionTable().isBoardFull()) {
            startGame.setDisable(true);
            fireButton.setVisible(true);
        }

        labelPlayerName.setText(gameModel.getNickname() + "'s Fleet");
    }

    /**
     * Initializes a new match by setting up the game model and configuring the game events.
     */
    private void initializeNewMatch() {
        gameModel.newMatch(this.username);
        setCellsEvents();
        setUpShipEvents();
        setGhostShips();
        createShotHandlers();
    }

    /**
     * Updates the counters for the number of each ship type (frigates, destroyers, submarines, aircraft).
     */
    public void updateLabels() {
        ArrayList<Ship> ships = gameModel.getPositionTable().getShips();
        for (Ship ship : ships) {
            if (ship == null) continue;
            if (ship.getShipType() == 1) {
                frigateCounter.setText("x" + ship.getShipAmount());
            } else if (ship.getShipType() == 2) {
                destroyerCounter.setText("x" + ship.getShipAmount());
            } else if (ship.getShipType() == 3) {
                submarineCounter.setText("x" + ship.getShipAmount());
            } else if (ship.getShipType() == 4) {
                aircraftCounter.setText("x" + ship.getShipAmount());
            }
        }
    }

    /**
     * Sets up event handlers for clicking on cells to place ships or change ship orientation.
     * Also sets up events for the machine's fleet cells to allow shooting.
     */
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
        for (Node node : machinesFleet.getChildren()){
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    playerShoot(gameModel.getMainTable().getBoard(), node);
                    node.setDisable(true);
                    turnManagement();
                    setWinner();
                }
            });
        }
    }

    /**
     * Updates the board with bombs and shots, drawing missed shots, bombs, and fires based on the shot grids.
     */
    private void setBombs() {
        int[][] machineShotGrid = gameModel.getMainTable().getShotGrid();
        int[][] positionShotGrid = gameModel.getPositionTable().getShotGrid();

        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                if (positionShotGrid[row][col] == 5) {
                    Group missedShot = drawer.drawMissedShot();
                    machinesFleet.add(missedShot, col, row);
                } else if (positionShotGrid[row][col] == 6) {

                    if (gameModel.getMainTable().getBoard()[row][col] == 1) {
                        Group fire = drawer.drawFire();
                        machinesFleet.add(fire, col, row);
                    } else {
                        Group bomb = drawer.drawBomb();
                        machinesFleet.add(bomb, col, row);
                    }
                }

                if (machineShotGrid[row][col] == 5) {
                    Group missedShot = drawer.drawMissedShot();
                    userFleet.add(missedShot, col, row);
                } else if (machineShotGrid[row][col] == 6) {
                    if (gameModel.getPositionTable().getBoard()[row][col] == 1) {
                        Group fire = drawer.drawFire();
                        userFleet.add(fire, col, row);
                    } else {
                        Group bomb = drawer.drawBomb();
                        userFleet.add(bomb, col, row);
                    }
                }
            }
        }
        checkSameMachineShip();
        checkSamePlayerShip();
    }

    /**
     * Sets up the ghost ships (invisible ships used for previewing placement)
     * for each ship type in the game. The ships are drawn but not placed on the board.
     */
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

    /**
     * Sets up events for ship types that allow users to select and place them on the board.
     * Displays a description of the selected ship.
     */
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

    /**
     * Adds sample ships to the ship selection grid for the user to choose from.
     *
     * @param ship1 The first ship to add to the selection grid.
     * @param ship2 The second ship to add to the selection grid.
     * @param ship3 The third ship to add to the selection grid.
     * @param ship4 The fourth ship to add to the selection grid.
     */
    private void setSampleShips(Group ship1, Group ship2, Group ship3, Group ship4) {
        selectionGrid.add(ship1, 0, 0);
        selectionGrid.add(ship2, 0, 1);
        selectionGrid.add(ship3, 0, 2);
        selectionGrid.add(ship4, 0, 3);
    }

    /**
     * Places the fleet on the board using the coordinates from either the machine's
     * or player's fleet.
     *
     * @param machine Boolean indicating whether the fleet is for the machine (true)
     * or the player (false).
     */
    private void setFleet(boolean machine) {
        List<int[]> shipCoordinates;
        if (machine) {
            shipCoordinates = gameModel.getMainTable().getShipCoordinatesList();
        } else {
            shipCoordinates = gameModel.getPositionTable().getShipCoordinatesList();
        }
        for (int[] coordinates : shipCoordinates) {
            int row = coordinates[0];
            int column = coordinates[1];
            boolean vertical = (coordinates[4] == 0);
            int type = coordinates[5];
            Group ship = drawShip(type, vertical);
            ship.getStyleClass().add("mShip");
            if (machine) {
                machinesFleet.add(ship, column, row);
            } else {
                userFleet.add(ship, column, row);
            }
        }
    }

    /**
     * Updates the ship counter on the UI when a ship is placed on the board.
     * Decreases the count for the specified ship type and removes the ghost
     * ship if there are no ships left.
     *
     * @param shipType The type of the ship to update (1 for Frigate, 2 for Destroyer,
     *                 3 for Submarine, 4 for Aircraft Carrier).
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

    /**
     * Creates handlers for mouse events during the shot phase, including hover
     * effects and crosshair visibility.
     */
    private void createShotHandlers() {
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
    }

    /**
     * This method sets some event filters to the Machine's Fleet so
     * a cross-hair is shown on the Machine's Fleet when the user
     * has to shoot.
     */
    private void setScopePointer() {
        if (!machinesFleet.getChildren().contains(crosshairView)) {
            machinesFleet.add(crosshairView, 0, 0);
        }
        crosshairView.setVisible(false);

        machinesFleet.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, mouseEnteredHandler);
        machinesFleet.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, mouseExitedHandler);
        machinesFleet.addEventFilter(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
    }

    /**
     * Checks if the player or machine has won the game based on the number of hits
     * recorded on the shot grids. If either player or machine has hit all the opponent's ships,
     * the winner is declared and the game ends.
     */
    private void setWinner(){
        int[][] playerShotTable = gameModel.getMainTable().getShotGrid();
        int[][] machineShotTable = gameModel.getPositionTable().getShotGrid();
        int machineShotCounter = 0;
        int playerShotCounter = 0;

        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                if (playerShotTable[i][j] == 6) machineShotCounter++;
                if (machineShotTable[i][j] == 6) playerShotCounter++;
            }
        }

        if (playerShotCounter == 20 || machineShotCounter == 20){
            userFleet.setDisable(true);
            machinesFleet.setDisable(true);
            fireButton.setDisable(true);

        }

        if (playerShotCounter == 20){
            gameModel.removeMatchTracking();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("The Player Won!");
            alert.showAndWait();
            playAgain.setDisable(false);
            playAgain.setVisible(true);
        }
        else if (machineShotCounter == 20){
            gameModel.removeMatchTracking();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("The Machine Won!");
            alert.showAndWait();
            playAgain.setDisable(false);
            playAgain.setVisible(true);
        }
    }

    /**
     * Manages the turns of the game. When it's the player's turn, the fire button is enabled
     * and the player can shoot at the machine's fleet. When it's the machine's turn, it automatically
     * shoots at the player's fleet after a short pause.
     */
    private void turnManagement(){
        if (playerTurn) {
            fireButton.setDisable(false);
            machinesFleet.setDisable(false);
            setScopePointer();
        }
        else {
            removeScopePointer();
            fireButton.setDisable(true);
            machinesFleet.setDisable(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(1));

            pause.setOnFinished(event -> {
                int[] machineRandCoordinates;
                if (successfulShot) {
                    machineRandCoordinates = gameModel.getMainTable().smartShot();
                } else {
                    machineRandCoordinates = gameModel.getMainTable().shot();
                }
                int machineRandX = machineRandCoordinates[0];
                int machineRandY = machineRandCoordinates[1];
                machineShoot(gameModel.getPositionTable().getBoard(), machineRandX, machineRandY);
                if(playerTurn){
                    fireButton.setDisable(false);
                }
            });
            pause.play();
        }
    }

    /**
     * Handles the machine's shooting logic. It updates the shot grid, draws the appropriate shot, and updates the player's turn.
     *
     * @param playerTable The game table of the player.
     * @param machineShootX The x-coordinate of the shot on the table.
     * @param machineShootY The y-coordinate of the shot on the table.
     */
    private void machineShoot(int[][] playerTable, int machineShootX, int machineShootY){
        if (playerTable[machineShootX][machineShootY] == 0){
            successfulShot = false;
            gameModel.getMainTable().getShotGrid()[machineShootX][machineShootY] = 5;
            Group missedShot = drawer.drawMissedShot();
            userFleet.add(missedShot, machineShootY, machineShootX);
            playerTurn = true;
        }
        else if(playerTable[machineShootX][machineShootY] == 1) {
            successfulShot = false;
            gameModel.getMainTable().getShotGrid()[machineShootX][machineShootY] = 6;
            Group fire = drawer.drawFire();
            userFleet.add(fire, machineShootY, machineShootX);
            playerTurn = false;
            turnManagement();
        }
        else if (playerTable[machineShootX][machineShootY] != 0 && playerTable[machineShootX][machineShootY] != 5){
            successfulShot = true;
            gameModel.getMainTable().getShotGrid()[machineShootX][machineShootY] = 6;
            Group bomb = drawer.drawBomb();
            userFleet.add(bomb, machineShootY, machineShootX);
            playerTurn = false;
            turnManagement();
        }
        checkSamePlayerShip();
        gameModel.saveGame();
    }

    /**
     * Handles the player's shooting logic. It updates the shot grid, draws the appropriate shot, and updates the player's turn.
     *
     * @param machineTable The game table of the machine.
     * @param clickedNode The clicked node (cell) on the game board.
     */
    private void playerShoot(int[][] machineTable, Node clickedNode){
        if (playerTurn){
            Integer machinePaneRow = GridPane.getRowIndex(clickedNode);
            Integer machinePaneCol = GridPane.getColumnIndex(clickedNode);

            if (machinePaneRow == null) machinePaneRow = 0;
            if (machinePaneCol == null) machinePaneCol = 0;

            if (machineTable[machinePaneRow][machinePaneCol] == 0){
                gameModel.getPositionTable().getShotGrid()[machinePaneRow][machinePaneCol] = 5;
                Group missedShot = drawer.drawMissedShot();
                machinesFleet.add(missedShot, machinePaneCol, machinePaneRow);
                playerTurn = false;
            }
            else if(machineTable[machinePaneRow][machinePaneCol] == 1) {
                gameModel.getPositionTable().getShotGrid()[machinePaneRow][machinePaneCol] = 6;
                Group fire = drawer.drawFire();
                machinesFleet.add(fire, machinePaneCol, machinePaneRow);
                playerTurn = true;
            }
            else if (machineTable[machinePaneRow][machinePaneCol] != 0 && machineTable[machinePaneRow][machinePaneCol] != 5){
                gameModel.getPositionTable().getShotGrid()[machinePaneRow][machinePaneCol] = 6;
                Group bomb = drawer.drawBomb();
                machinesFleet.add(bomb, machinePaneCol, machinePaneRow);
                playerTurn = true;
            }
            checkSameMachineShip();
        }
        gameModel.saveGame();
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

    /**
     * Traverses the position array to check the status of ships (damaged or sunk) and updates their graphical representation.
     *
     * @param currentArray The current ship's coordinates to be checked.
     * @param player Indicates whether the check is for the player (true) or machine (false).
     */
    private void traversePositionArray(int[] currentArray, boolean player) {
        int[][] board;
        int counterType4 = 0;
        int counterType3 = 0;
        int counterType2 = 0;
        if (player) {
            board = gameModel.getPositionTable().getBoard();
        } else {
            board = gameModel.getMainTable().getBoard();
        }

        for (int row = currentArray[0]; row <= currentArray[2]; row++) {
            for (int col = currentArray[1]; col <= currentArray[3]; col++) {
                if ((gameModel.getMainTable().getShotGrid()[row][col] == 6 && player)
                        || (gameModel.getPositionTable().getShotGrid()[row][col] == 6 && !player)){
                    if (board[row][col] == 4) {
                        counterType4++;
                    }
                    else if (board[row][col] == 3) {
                        counterType3++;
                    }
                    else if (board[row][col] == 2) {
                        counterType2++;
                    }
                }
            }
        }

        if (counterType4 == 4 || counterType3 == 3 || counterType2 == 2) {
            for (int n = currentArray[0]; n <= currentArray[2]; n++) {
                for (int m = currentArray[1]; m <= currentArray[3]; m++) {
                    Group fire = drawer.drawFire();
                    if (player) userFleet.add(fire, m, n);
                    else machinesFleet.add(fire, m, n);
                }
            }
        }
    }

    /**
     * Checks and updates the visual representation of the player's ships after a shot is fired.
     */
    private void checkSamePlayerShip() {
        List<int[]> shipCoordinates = gameModel.getPositionTable().getShipCoordinatesList();

        for (int[] coordinates : shipCoordinates) {
            traversePositionArray(coordinates, true);
        }
    }

    /**
     * Checks and updates the visual representation of the machine's ships after a shot is fired.
     */
    private void checkSameMachineShip() {
        List<int[]> shipCoordinates = gameModel.getMainTable().getShipCoordinatesList();

        for (int[] coordinates: shipCoordinates) {
            traversePositionArray(coordinates, false);
        }
    }

    /**
     * Places a ship on the player's board based on the selected cell and orientation.
     *
     * @param clickedNode The clicked node (cell) on the game board where the ship is to be placed.
     */
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
     * Changes the orientation of the selected ship (from horizontal to vertical or vice versa).
     */
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
        rotate.setAngle(0);
        shipOrientation = 1;
        // If there is a ship already selected then it removes the current ghost
        if (currentGhost != null) {
            userFleet.getChildren().remove(currentGhost);
        }

        // If there are no ships left of the ship selected no ghost is shown
        if (!gameModel.getPositionTable().checkAmount(shipType)) {
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

    /**
     * Displays a message to the user on the interface.
     *
     * @param msg The message to be displayed.
     */
    private void showMessage(String msg) {
        messageLabel.setText(msg);
        messageLabel.setVisible(true);
        FadeTransition transition = new FadeTransition(Duration.seconds(2), messageLabel);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();
    }

    /**
     * Triggered by a button click to reveal the machine's fleet when the correct keyword is entered.
     *
     * @param event The action event triggered by the button.
     */
    @FXML
    private void onActionRevealFleet(ActionEvent event) {
        showCustomModal();
    }

    /**
     * Displays a modal dialog to enter a secret keyword and reveal the machine's fleet if the correct keyword is provided.
     */
    private void showCustomModal() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Reveal Fleet");

        VBox dialogBox = new VBox(15);
        dialogBox.setId("keywordContainer");
        dialogBox.getStylesheets().add(getClass().getResource("/com/example/navalbattle/styles/keyword-modal.css").toExternalForm());
        Label label = new Label("Enter the secret keyword to reveal the fleet");
        label.setId("keywordLabel");
        TextField textField = new TextField();
        textField.setId("keywordTextField");
        Button confirmButton = new Button("Confirm");
        confirmButton.setId("confirmKeyword");
        Button cancelButton = new Button("Cancel");
        cancelButton.setId("cancelKeyword");

        confirmButton.setOnAction(event -> {
            String keyword = textField.getText();
            if (keyword.equalsIgnoreCase("supersecretpassword")) {
                setFleet(true);
                setHideButton();
                dialog.close();
            } else {
                showMessage("Incorrect Keyword. Try again");
            }
        });

        cancelButton.setOnAction(event -> dialog.close());

        dialogBox.getChildren().addAll(label, textField, confirmButton, cancelButton);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setPadding(new Insets(10));

        Scene dialogScene = new Scene(dialogBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * Hides the player's fleet and toggles the button to show the fleet again.
     */
    private void setHideButton() {
        revealFleet.setVisible(false);
        revealFleet.setManaged(false);
        Button hideButton = new Button("Hide Fleet");
        hideButton.setId("hideButton");
        revealFleetContainer.getChildren().add(hideButton);
        hideButton.setOnAction(event -> {
            List<Node> nodesToRemove = new ArrayList<>();

            for (Node node : machinesFleet.getChildren()) {
                if (node.getStyleClass().contains("mShip")) {
                    nodesToRemove.add(node);
                }
            }

            machinesFleet.getChildren().removeAll(nodesToRemove);

            hideButton.setVisible(false);
            hideButton.setManaged(false);
            revealFleet.setVisible(true);
            revealFleet.setManaged(true);
        });
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

    /**
     * Resets the game by closing the current stage and opening a new one, allowing the player to play again.
     *
     * @param event The event triggered by the "Play Again" button.
     * @throws IOException If an error occurs during stage initialization.
     */
    @FXML
    private void playAgain(Event event) throws IOException {
        Node source = (Node) event.getSource();
        Stage actualStage = (Stage) source.getScene().getWindow();
        actualStage.close();

        GameView gameView = new GameView();
        gameView.show();
        GameController gameController = gameView.getGameController();
        gameController.initialize(username);
        this.gameModel = new GameModel();
    }
}