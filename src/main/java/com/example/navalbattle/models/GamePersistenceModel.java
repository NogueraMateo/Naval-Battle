package com.example.navalbattle.models;

import java.io.*;

/**
 * Handles the persistence of the game state for a single match in the Battleship game.
 * This class is responsible for saving and loading the game state to allow players
 * to resume from their last saved position.
 *
 * The class operates by serializing a single `MatchStatusSerializable` instance
 * which represents the boards of both the player and the machine.
 */
public class GamePersistenceModel implements GamePersistenceInterface{

    private MatchStatusSerializable currentMatchStatus;

    public GamePersistenceModel() {
        File directory = new File("./src/main/resources/com/example/navalbattle/previousMatch");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    @Override
    public void registerNewMatch(int[][] mainTable, int[][] positionTable) {
        currentMatchStatus = new MatchStatusSerializable(mainTable, positionTable);
        serialize(currentMatchStatus);
    }

    @Override
    public void serialize(MatchStatusSerializable match) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/main/resources/com/example/navalbattle/previousMatch/previous_match.ser"));){
            oos.writeObject(currentMatchStatus);
            System.out.println("Current board has been saved");
        } catch (IOException e) {
            System.err.println("Error saving the current match status: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Current board has been saved");
    }

    @Override
    public MatchStatusSerializable deserialize() throws ClassNotFoundException, IOException{
        MatchStatusSerializable previousMatch;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/main/resources/com/example/navalbattle/previousMatch/previous_match.ser"))) {
            previousMatch = (MatchStatusSerializable) ois.readObject();
        }
        return previousMatch;
    }

    @Override
    public void takeSnapshot(int[][] mainTable, int[][] positionTable) {
        currentMatchStatus.saveSnapShot(mainTable, positionTable);
        serialize(currentMatchStatus);
    }
}
