package ch.unibas.dmi.dbis.cs108.AmongAlien.server;


import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static ch.unibas.dmi.dbis.cs108.AmongAlien.server.Server.lobbyList;

/**
 * This is the Lobby-Class. A Player can create a Lobby to
 * make a group of Players waiting for the Game to be started.
 *
 * @author Joel Erbsland, Rayes Diyab, Hamza Zarah and Jonahtan Buerge
 * @version 2021.05.10
 */
public class Lobby extends Thread{
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
    public String lobbyName;
    public boolean isRunning = false;
    public ArrayList<ClientThread> lobbyClients = new ArrayList<>();
    private int numMembers;
    ClientThread client;
    protected ArrayList<Lobby> thislobbylist = lobbyList;
    public GameServer game;
    Server server;

    /**
     * Standard Constructor to create a named Lobby by a known
     * Host.
     *
     * @param lobbyName the Lobby's name
     * @param client the EchoClientThread with the specific connection to the Client.
     */
    public Lobby(String lobbyName, ClientThread client) {
        boolean isExisting = true;
        if (client.isInLobby()) {
            leaveLobby(client);
        }
        this.numMembers = 1;
        int number = 0;
        while (isExisting) {
            for (Lobby lobby : thislobbylist) {
                if (lobby.lobbyName.equals(lobbyName)) {
                    number++;
                    lobbyName += number;
                    break;
                }
            }
            isExisting = false;
        }

        this.client = client;
        this.lobbyName = lobbyName;
        thislobbylist.add(this);
        this.lobbyClients.add(client);
        client.setLobbyHost();
        client.setLobby(this);
        //System.out.println("Number of Members " + lobbyName + "  = " + numMembers);

        for (ClientThread lobbyClient : lobbyClients) {
            lobbyClient.sendMessage("/UPDATELOBBYPLAYERS " + getPlayerListAsString());
        }
    }

    /**
     * Overrides the Thread constructor
     * Is empty
     */
    @Override
    public void run() { }

    /**
     * This Method handles the join event to this lobby.
     *
     * @param client the EchoServerThread with the connection to the client.
     */
    public void joinLobby(ClientThread client) {
        if (this.isJoinable()) {
            if (client.isInLobby() && client.currentLobby != this) {
                leaveLobby(client);
            }
            if(client.currentLobby == this){
                client.sendMessage("You are already in this Lobby!");
            }else{
                this.lobbyClients.add(client);
                client.setLobby(this);
                numMembers++;
                //System.out.println("Number of Members " + lobbyName + "  = " + numMembers);

                for (ClientThread lobbyClient : lobbyClients) {
                    lobbyClient.sendMessage("/UPDATELOBBYPLAYERS " + getPlayerListAsString());
                }
            }
            client.sendMessage("/UPDATEPLAYERLOBBY " + lobbyName);
        } else if(this.isRunning){
            client.sendMessage("Lobby has an ongoing Game \nand can't be joined!");
        } else {
            client.sendMessage("Lobby is full!");
        }
    }


    /**
     * The given Player will leave the this lobby and if he was
     * the lobby Host, the host will be from now on the next
     * Player in the playerInLobby list.
     *
     * @param client the EchoClientThread with the connection to the clietn.
     */
    public void leaveLobby(ClientThread client) {
        this.lobbyClients.remove(client);
        this.numMembers--;
        if (numMembers <= 0 && client.isLobbyHost) {
            lobbyClients.remove(client);
            thislobbylist.remove(client.currentLobby);
            client.sendMessage("/UPDATELEFTLOBBY");
            broadcast("/UPDATELOBBYGUI");
        } else if (numMembers > 0 && client.isLobbyHost) {
            lobbyClients.remove(client);
            lobbyClients.get(0).setLobbyHost();
            client.sendMessage("/UPDATELEFTLOBBY");
        } else if (numMembers != 0) {
            lobbyClients.remove(client);
            client.sendMessage("/UPDATELEFTLOBBY");
        }
        client.unsetLobbyHost();
        client.unsetLobby();
        for (ClientThread lobbyClient : lobbyClients) {
            lobbyClient.sendMessage("/UPDATELOBBYPLAYERS " + getPlayerListAsString());
        }
    }

    /**
     * The endGame Method will be called at the end of a game. The given
     * lobby will be removed from the list.
     * @param lobby the given Lobby
     */
    public void endGame(Lobby lobby){
            //thislobbylist.remove(client.currentLobby);
            this.server = client.server;
            server.removeLobby(lobby);
            LOGGER.info("Updates LobbyList: "+server.getLobbyListAsString());
            server.broadcast("/UPDATELOBBYLIST " + server.getLobbyListAsString());
    }

    /**
     * @return true if there are less than 10 Players waiting for
     * the game to be started.
     */
    public boolean isJoinable() {
        return (numMembers < 10 && !isRunning);
    }

    /**
     * @return true if there are 4 or more Players in the Lobby
     * because we need at least 4 Players for the game to be
     * started.
     */
    public boolean isStartable() {
        numMembers = lobbyClients.size();
        return (numMembers >= 4 && !isRunning); //later ca. 4
    }


    /**
     * @return a List of all Players in a Lobby as a String. The
     *         names of the Lobbies are separated by the ":".
     */
    public String getPlayerListAsString() {

        StringBuilder members = new StringBuilder();
        for (ClientThread userThread : lobbyClients) {
            members.append(userThread.userName);
            members.append(":");
        }
        return members.toString();
    }


    /**
     * To start a Game with all players in this lobby.
     */
    public void startGame() {
        if (this.isStartable() && !isRunning) {
            this.isRunning = true;            GameServer game = new GameServer(this, lobbyClients);
            this.game = game;
            game.execute();
        }
    }

    /**
     * Sends Message to all clients except the excluded User.
     *
     * @param message Message that should be sent.
     * @param excludeUser User that sends the message.
     */
    void broadcast (String message, ClientThread excludeUser){
        for (ClientThread aUser : lobbyClients) {
            if (!aUser.userName.equals(excludeUser.userName)) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Sends message to all players in Lobby.
     *
     * @param message Message that should be sended.
     */
    void broadcast (String message){
        for (ClientThread aUser : lobbyClients) {
            aUser.sendMessage(message);
        }
    }

    /**
     * The getLobby Method will return a lobby instance for a given lobby name.
     *
     * @param lobbyName the name of the lobby you are looking for.
     * @return the lobby with the given name.
     */
    public static Lobby getLobby(String lobbyName) {
        lobbyName = " "+lobbyName;
        System.out.println(lobbyName);
        for (Lobby lobby : lobbyList) {
            if (lobby.lobbyName.equals(lobbyName)) {
                return lobby;
            }
        }
        LOGGER.warn("There is no such Lobby! "+(lobbyName)+"getLobby() IN LOBBY CLASS");
        return null;
    }
}
