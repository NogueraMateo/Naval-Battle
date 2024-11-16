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
    public void registerNewMatch(MainTable mainTable, PositionTable positionTable, String nickname) {
        currentMatchStatus = new MatchStatusSerializable(mainTable, positionTable, nickname);
        serialize(currentMatchStatus);
    }

    @Override
    public void serialize(MatchStatusSerializable match) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/main/resources/com/example/navalbattle/previousMatch/previous_match.ser"));){
            oos.writeObject(currentMatchStatus);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error saving the current match status: " + e.getMessage());
        }
    }

    @Override
    public MatchStatusSerializable deserialize() throws ClassNotFoundException, IOException{
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/main/resources/com/example/navalbattle/previousMatch/previous_match.ser"))) {
            currentMatchStatus = (MatchStatusSerializable) ois.readObject();
        }
        return currentMatchStatus;
    }

    @Override
    public void takeSnapshot(MainTable mainTable, PositionTable positionTable) {
        currentMatchStatus.saveSnapShot(mainTable, positionTable);
        serialize(currentMatchStatus);
    }

    public void deleteMatchStatus() {
        File matchStatus = new File("./src/main/resources/com/example/navalbattle/previousMatch/previous_match.ser");

        if (matchStatus.exists()) {
            boolean deleted = matchStatus.delete();
            if (deleted) {
                System.out.println("Match deleted successfully");
            } else {
                System.out.println("Error deleting match status: " + matchStatus.getAbsolutePath());
            }
        } else {
            System.out.println("No match found");
        }
    }
}
