package com.example.navalbattle.models;

import com.example.navalbattle.interfaces.GamePersistenceInterface;

import java.io.*;

/**
 * Handles the persistence of the game state for a single match in the Battleship game.
 * This class is responsible for saving and loading the game state to allow players
 * to resume from their last saved position.

 * The class operates by serializing a single {@link MatchStatusSerializable} instance
 * which represents the boards of both the player and the machine.
 */
public class GamePersistenceModel implements GamePersistenceInterface {

    private MatchStatusSerializable currentMatchStatus;

    /**
     * Constructor for the GamePersistenceModel class.
     * Initializes the directory for storing previous match data.
     * If the directory does not exist, it creates it.
     */
    public GamePersistenceModel() {
        File directory = new File("./src/main/resources/com/example/navalbattle/previousMatch");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    /**
     * Registers a new match by creating a {@link MatchStatusSerializable} instance
     * and serializing it to a file.
     *
     * @param mainTable     the game board of the machine
     * @param positionTable the game board of the player
     * @param nickname      the player's nickname
     */
    @Override
    public void registerNewMatch(MainTable mainTable, PositionTable positionTable, String nickname) {
        currentMatchStatus = new MatchStatusSerializable(mainTable, positionTable, nickname);
        serialize(currentMatchStatus);
    }

    /**
     * Serializes the current match status and saves it to a file.
     *
     * @param match the {@link MatchStatusSerializable} object to be serialized.
     */
    @Override
    public void serialize(MatchStatusSerializable match) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./src/main/resources/com/example/navalbattle/previousMatch/previous_match.ser"))){
            oos.writeObject(currentMatchStatus);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Error saving the current match status: " + e.getMessage());
        }
    }

    /**
     * Deserializes the match status from a saved file and returns it.
     *
     * @return the deserialized {@link MatchStatusSerializable} object representing the saved match state.
     * @throws ClassNotFoundException if the class for the object cannot be found.
     * @throws IOException            if there is an issue reading the file.
     */
    @Override
    public MatchStatusSerializable deserialize() throws ClassNotFoundException, IOException{
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/main/resources/com/example/navalbattle/previousMatch/previous_match.ser"))) {
            currentMatchStatus = (MatchStatusSerializable) ois.readObject();
        }
        return currentMatchStatus;
    }

    /**
     * Takes a snapshot of the current game state by saving the match status.
     * The snapshot is then serialized to store the current state.
     *
     * @param mainTable     the game board of the machine
     * @param positionTable the game board of the player
     */
    @Override
    public void takeSnapshot(MainTable mainTable, PositionTable positionTable) {
        currentMatchStatus.saveSnapShot(mainTable, positionTable);
        serialize(currentMatchStatus);
    }

    /**
     * Deletes the saved match status file if it exists.
     * Outputs a message indicating whether the deletion was successful.
     */
    @Override
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
