package ch.unibas.dmi.dbis.cs108.AmongAlien.server;

/**
 * The Wins Class gets different statistics for the HighScore
 * List on demand
 *
 * @author Joel Erbsland
 * @version 2021.05.22
 */
public class Wins {

    private String userName;
    private int winsAsHuman;
    private int winsAsAlien;

    /**
     * Sets the Wins
     *
     * @param userName Username
     * @param winsAsHuman Number of Wins as Human
     * @param winsAsAlien Number of Wins as Alien
     */
    public Wins(String userName, int winsAsHuman, int winsAsAlien) {
        this.userName = userName;
        this.winsAsHuman = winsAsHuman;
        this.winsAsAlien = winsAsAlien;
    }

    /***
     * Gets the Username
     *
     * @return Username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the Number of Wins as Human
     *
     * @return Number of Wins as Human
     */
    public int getWinsAsHuman() {
        return winsAsHuman;
    }

    /**
     * Gets the Number of Wins as Alien
     *
     * @return  Number of Wins as Alien
     */
    public int getWinsAsAlien() {
        return winsAsAlien;
    }

    /**
     * Adds a win to the Number of as Human Wins
     */
    public void addWinAsHuman() {
        winsAsHuman++;
    }

    /**
     * Adds a win to the Number of as Alien Wins
     */
    public void addWinAsAlien() {
        winsAsAlien++;
    }
}
