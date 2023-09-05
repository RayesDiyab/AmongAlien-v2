package ch.unibas.dmi.dbis.cs108.AmongAlien.server;

import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.MapMatrix;

import java.util.ArrayList;
import java.util.Random;

/**
 * This is the Game class. It stores all the players ClientThreads
 * who are in the same game in an Arraylist
 *
 * @author Joel Erbsland
 * @version 2021.05.10
 */
public class Game extends Thread {

    private ArrayList<ClientThread> playerList = new ArrayList<>();
    private Random olaf = new Random(); //Olaf is back :)
    private MapMatrix map = new MapMatrix("TestWelt", 40);

    /**
     * Is the standard constructor to create a Game. It randomly puts
     * one player to an Alien but goes sure that there are no other
     * Alien in this Game.
     *
     * @param playerList the List of all Players in the Game.
     */
    public Game(ArrayList<ClientThread> playerList) {
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setToHuman();
        }
        this.playerList = playerList;
        int index = olaf.nextInt(playerList.size() + 1);
        playerList.get(index).setToAlien();
    }
}
