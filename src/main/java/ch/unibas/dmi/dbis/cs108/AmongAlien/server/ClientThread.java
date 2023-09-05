package ch.unibas.dmi.dbis.cs108.AmongAlien.server;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * All of our Clients are running on their own Thread, the EchoClientThread
 *
 * @author Joel Erbsland, Rayes Diyab and Hamza Zarah
 */
public class ClientThread extends Thread {

    public GameServer gameServer;
    private int playerNumber;
    private int i = 2;
    private Socket socket;
    public Server server;
    protected String userName;
    private PrintWriter writer;
    private BufferedReader reader;
    public PingThread pingThread;
    public int posX = 0;
    public int posY = 0;
    protected boolean hasVoted;
    public int[] materials = new int[100];
    public boolean hasFished = true;
    public double hunger;
    public int playersImage;
    public boolean isLobbyHost = false;
    private boolean isInGame;
    private boolean isAlien;
    public int gameNum;
    Lobby currentLobby = null;
    public boolean isGhost;
    public boolean inactive = false;

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());


    /**
     * Constructor to create a ClientThread with playerNumber, server and socket.
     *
     * @param playerNumber of the player/client connected to
     * @param server instance
     * @param socket for connection
     * @throws SocketException if something went wrong with the connection.
     */
    public ClientThread(int playerNumber, Server server, Socket socket) throws SocketException {
        this.playerNumber = playerNumber;
        this.socket = socket;
        this.server = server;
    }

    /**
     * The start of the ClientThread. It makes the connection to the specific client.
     */
    public void run() {
        try {
            //Input Stream
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            //Output Stream
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            //Starts the Pong Thread
            this.pingThread = new PingThread(this, server);
            pingThread.start();



            String clientInMessage;
            do {
                clientInMessage = reader.readLine();
                if(clientInMessage == null){
                    //ServerProtocolInterpret.serverInterpret("/LOGOUT", this, server);
                    break;
                } else {
                    ServerProtocolInterpret.serverInterpret(clientInMessage, this, server);
                }
            } while (!clientInMessage.equals("/LOGOUT"));

        } catch (SocketException exception) {
            LOGGER.fatal(this.userName + " Socket closed unexpectedly :(\nClient will be logged out:");
            ServerProtocolInterpret.serverInterpret("/LOGOUT", this, server);
        } catch (Exception cMe) {
            LOGGER.error(cMe.getStackTrace());
        }
    }


    /**
     * Sends a message to the client.
     *
     * @param message the message to send to server as a String
     */
    protected void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * Set the isLobbyHost boolean to true if this player is the lobbyhost.
     */
    public void setLobbyHost() {
        isLobbyHost = true;
    }

    /**
     * Puts the player into a given lobby.
     *
     * @param currentLobby for the player to join.
     */
    public void setLobby(Lobby currentLobby){
        this.currentLobby = currentLobby;
    }

    /**
     * @return true if the player is in a lobby.
     */
    public boolean isInLobby(){
        return currentLobby != null;
    }

    /**
     * @return true if this player is the lobbyhost.
     */
    public boolean isLobbyHost(){
        return isLobbyHost;
    }

    /**
     * The unsetLobby Method will be called if a player should be no longer in any lobby.
     * The currentLobby variable will be set to null.
     */
    public void unsetLobby(){
        this.currentLobby = null;
    }

    /**
     * The setToHuman Method will make sur that a player is no alien. Therefor the boolean
     * isAlien will be set to false.
     */
    public void setToHuman(){
        isAlien = false;
    }

    /**
     * The setToAlien method will make sur that a player is an alien. Therefor the boolean
     * isAlien will be set to true.
     */
    public void setToAlien(){
        isAlien = true;
    }

    /**
     * Puts a Player into a given gameserver.
     *
     * @param gameServer the player should be set to.
     */
    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    /**
     * Sets the isInGame boolean to true. Means the player is in a game.
     */
    public void setInGame() {
        isInGame = true;
    }

    /**
     * Sets the isInGame boolean to false. Means the player is not or no longer in a game.
     */
    public void setNotInGame() {
        isInGame = false;
    }

    /**
     * Sets the isLobbyHost boolean to false. Means the player is no longer a lobbyhost and therefor
     * he is no longer able to press the start button.
     */
    public void unsetLobbyHost() {
        isLobbyHost = false;
    }

    /**
     * Sets the isGhost boolean to false.
     */
    public void setNotGhost() {
        isGhost = false;
    }

    /**
     * @return true if a player is an alien.
     */
    public boolean isAlien() {
        return isAlien;
    }

    /**
     * @return the players name as String.
     */
    public String getPlayerName() {
        return userName;
    }

    /**
     * Set player to NOT be in a Game
     *
     * @return true if a Player is in a Game.
     */
    public boolean gameState(){
        return isInGame;
    }

    /**
     * Adds a material to the inventory of a player.
     *
     * @param textureID the TextureID of the Material to add to the Players Inventory.
     */
    public void addMat(int textureID) {
        materials[textureID]++;
    }

    /**
     * Removes a given amount of a material of the players inventory.
     *
     * @param textureID the TextureID of the Material to remove to the Players Inventory.
     * @param amountMat the amount of Materials to remove.
     */
    public void removedMat(int textureID, int amountMat) {
        if (materials[textureID] - amountMat < 0) {
            this.currentLobby.game.userThreads.get(playerNumber).sendMessage("/SETMSG Mat with TexID = "
                    + textureID + "is empty!");
        }
        materials[textureID] -= amountMat;
    }

    /**
     * @return this Clients Username.
     */
    public String getUserName() {
        return userName;
    }
}
