package ch.unibas.dmi.dbis.cs108.AmongAlien.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Class Server
 * Here the server and client thread is started by the previous
 * main method. After that, the server is ready to accept
 * client requests.
 */
public class Server {
    private int port;
    public static HighscoreList highscoreList = new HighscoreList();
    public static ArrayList<String> finishedGames = new ArrayList<>();
    protected int[][] materials = new int[3][100];
    public Set<ClientThread> deadClients = new HashSet<>();
    public ArrayList<String> userNames = new ArrayList<>();
    public static ArrayList<ClientThread> userThreads = new ArrayList<>();
    protected static ArrayList<Lobby> lobbyList = new ArrayList<>();
    private static int clientID;
    public static Server instance;
    private int i = 2;
    public static final Logger LOGGER = LogManager.getLogger(Server.class);
    public boolean running = true;
    public boolean executed = false;

    /**
     * Creates the server with the port
     *
     * @param port Port received from the startServer method
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * An empty standard Constructor
     */
    public Server(){}

    /**
     * This Method returns the Server instance
     * @return Server instance
     */
    public Server instance(){
        return this;
    }

    /**
     * The server is started here
     * It accepts requests from clients and creates its own
     * ClientThread per client.
     */
    public void execute() {
        finishedGames.add("Hogwarts");
        finishedGames.add("Unibas Tournament");
        ServerSocket serverSocket = createNewServerSocket(port);

        try (serverSocket) {
            instance = this;
            System.out.println("AmongAlienServer is listening on port " + port);
            while (running) {
                executed = true;
                //Accepts incoming requests from clients
                Socket socket = serverSocket.accept();
                System.out.println("A new user has connected to Server");
                //Creates new Client with Thread
                ClientThread newUser = new ClientThread(++clientID, this, socket);
                //Added User to Hashset
                userThreads.add(newUser);
                //Starts thread and begins Run method in ClientThread
                newUser.start();
            }
            executed = false;
        } catch (IOException e) {
            LOGGER.error("Error in the server: " + e.getMessage());
            LOGGER.error(e.getStackTrace());
        }
    }

    /**
     * This method creates a new ServerSocket by
     * receiving a specific port
     * @param port the specified port
     * @return if the creation of the ServerSocket was
     * successful, it returns a ServerSocket.
     * If not it returns null
     */
    public ServerSocket createNewServerSocket(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            return serverSocket;
        }catch (IOException e){
            LOGGER.error(e.getStackTrace());
        }
        return null;
    }

    /**
     * The server is created and started here
     * The server UI is also output.
     *
     * @param port specific port
     */
    public static void startServer(int port) {
        //Server UI is output to the console
        String rahmen = "**********************************"
                + "************************************";
        System.out.print(rahmen);
        System.out.printf("\n** %-64s **", "");
        System.out.printf("\n** %-64s **", "Dies ist der AmongAlien 2.0 Server");
        System.out.printf("\n** %-64s **", "");
        System.out.printf("\n** %-64s **", "Sie befinden sich in einer Konsolenanwendung");
        System.out.printf("\n** %-64s **", "");
        System.out.println("\n" + rahmen);

        //Creates a new server with passed port
        Server server = new Server(port);
        //Start the created server
        server.execute();
    }

    /**
     * This method adds the name of the new client
     * and logs it in!
     * The name is also automatically checked for duplicates here.
     * After logging in, all other connected clients are notified
     *
     * @param userName the client name to add
     * @param client the ClientThread of the name to be added
     */
    public void addUserName(String userName, ClientThread client){
        //Checks name for duplicates
        while (userNames.contains(userName)) {
            userName = userName + i;
            i++;
        }
        //Added the name to the server list
        userNames.add(userName);
        //Added the thread to the server list
        client.userName = userName;
        //Message to clients
        LOGGER.info(userName + " has Logged in!");
        client.sendMessage("/UPDATENAME " + userName);
        broadcast(userName + " has joined the Game!", client);
    }

    /**
     * The changeName Method is used to change a Players Playername.
     *
     * @param newUserName the name the old name should be replaced with.
     * @param client the client instance.
     */
    public void changeName(String newUserName, ClientThread client){
        String toChangeUser = client.userName;
        client.userName = newUserName;
        userNames.remove(client.userName);
        while (userNames.contains(newUserName)) {
            newUserName = newUserName + i;
            i++;
        }
        //Added the name to the server list
        userNames.add(newUserName);
        client.sendMessage("/UPDATENAME");
        LOGGER.info(toChangeUser + " is now called: " + newUserName);
        broadcast(toChangeUser + " is now called: " + newUserName);
    }

    /**
     * A client is removed by logging it out
     * The name and associated ClientThread are removed from
     * the server's lists.
     * After logging out, all other connected clients are
     * notified
     *
     * @param client The client to be removed
     */
    public void removeUser(ClientThread client){
        String toLogoutUser = client.userName;
        //Removes name from the server list
        userNames.remove(client.userName);
        //Removes ClientThread from the Server List
        userThreads.remove(client);
        //Message to clients
        LOGGER.info(toLogoutUser + " has Logged out!");
        broadcast(toLogoutUser + " exited the Game!");
    }

    /**
     * @return a List of all Players as a String. The names of
     *         the Players are separated by the ":".
     */
    public String getPlayerListAsString(){
        StringBuilder players = new StringBuilder();
        for (String aClient : userNames) {
            players.append(aClient);
            players.append(":");
        }
        return players.toString();
    }

    /**
     * @return a List of all Lobbies as a String. The names of
     *         the Lobbies are separated by the ":".
     */
    public String getLobbyListAsString(){
        StringBuilder lobbies = new StringBuilder();
        for (Lobby lobby : lobbyList) {
            lobbies.append(lobby.lobbyName);
            lobbies.append(":");
        }
        return lobbies.toString();
    }

    /**
     * @return a List of all Lobbies as a String. The names of
     *         the Lobbies are separated by the ":".
     */
    public String getOpenGamesAsString(){
        StringBuilder lobbies = new StringBuilder();
        for (int i = 0; i < lobbyList.size(); i++) {
            if(lobbyList.get(i).game == null){
                lobbies.append(lobbyList.get(i).lobbyName);
                lobbies.append(":");
            }
        }
        return lobbies.toString();
    }

    /**
     * @return a List of all Lobbies as a String. The names of
     *         the Lobbies are separated by the ":".
     */
    public String getGameListAsString(){
        StringBuilder Games = new StringBuilder();
        for(int i = 0; i < lobbyList.size(); i++){
            if(lobbyList.get(i).game != null){
                Games.append(lobbyList.get(i).lobbyName);
                Games.append(":");
            }
        }
        return Games.toString();
    }

    /**
     * @return a List of all Lobbies as a String. The names of
     *         the Lobbies are separated by the ":".
     */
    public String getFinishedGamesAsString(){
        StringBuilder finGames = new StringBuilder();
        for(int i = 0; i < finishedGames.size(); i++){
            finGames.append(finishedGames.get(i));
            finGames.append(":");
        }
        return finGames.toString();
    }




    /**
     * Returns a list with the names of the connected
     * clients
     *
     * @return the userName list is returned
     */
    ArrayList<String> getUserNames() {
        return this.userNames;
    }

    /**
     * This broadcast method sends a message to all
     * connected clients except the client
     * passed as parameter
     *
     * @param message The message
     * @param excludeUser The client to be excluded
     */
    void broadcast (String message, ClientThread excludeUser){
        //A loop in which a message is sent to everyone
        //except the excludeUser.
        for (ClientThread aUser : userThreads) {
            if (!aUser.userName.equals(excludeUser.userName)) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Removes a given Lobby from the Lobby List stored in the Server
     * @param lobby the given Lobby which should be removed
     */
    public void removeLobby(Lobby lobby){
        lobbyList.remove(lobby);
    }

    /**
     * This broadcast method sends a message to all
     * connected clients
     *
     * @param message The message
     */
    void broadcast (String message){
        //A loop in which a message is sent to everyone
        for (ClientThread aUser : userThreads) {
            aUser.sendMessage(message);
        }
    }
}
