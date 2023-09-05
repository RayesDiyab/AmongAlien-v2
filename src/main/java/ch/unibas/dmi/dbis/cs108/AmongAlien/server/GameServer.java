package ch.unibas.dmi.dbis.cs108.AmongAlien.server;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.MapMatrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the GameServer. It stores all the ClientThread-Objects
 * and ClientNames of the players who are in the same game
 * It lalso handles all the Gameupdates and sends them to the GUI
 *
 * @author Joel Erbsland, Rayes Diyab, Hamza Zarah and Jonahtan Burge
 * @version 2021.05.10
 */
public class GameServer extends Server{
    protected ArrayList<ClientThread> userThreads;
    protected ArrayList<String> humansInGame = new ArrayList<>();
    protected ArrayList<String> aliensInGame = new ArrayList<>();
    private MapMatrix map;
    public int[][] playerPositions = new int[3][10];
    protected int[] playerVotes = new int[11];
    protected int[][] materials = new int[3][100];
    protected Lobby lobby;
    public Timer timerDay;
    public int numMembers;
    public int Aliens = 0;
    public int Humans = 0;
    private int doneTasks = 0;
    public int dayLength = 120;
    public int votingLength = 51;
    public int timer = dayLength;

    public boolean isRunning;
    public boolean isDay;
    public double safety;
    public int[] playerPrefImg = new int[10];
    public boolean[] isActive = new boolean[10];
    public boolean markedToday = false;
    public int markedPlayer = 10;

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public TimerTask timerTaskDay = new TimerTask() {
        @Override
        public void run() {
            if (timer == 8 && !isDay) {
                throwOut();
            }
            if (timer >= 0) {
                getWinState();
                System.out.println(timer);
                timer--;
                if (timer % 10 == 0 && isDay) {
                    takeFood();
                }
            } else if (timer == -1) {
                if (isDay) {
                    lobby.broadcast("/UPDATEMARKED " + markedPlayer + markedToday);
                    if (markedToday) {
                        tellMarkedToLeave();
                    }
                    updateActivePlayers();
                    startVoting();
                    timer = votingLength;
                    isDay = false;
                } else {
                    startDay(true);
                    timer = dayLength;
                    isDay = true;
                }
            }
        }
    };

    /**
     * The tellVotedToLeave Method is used to remove a voted player from the game.
     *
     * @param playerNum of player voted.
     */
    public void tellVotedToLeave(int playerNum) {
        //userThreads.get(playerNum).sendMessage("/VOTEDOUT ");
        userThreads.get(playerNum).sendMessage("/UPDATENAME " + userThreads.get(playerNum).userName);
        removeUser(playerNum);
    }

    /**
     * The updateActivePlayers Method is used to update the list of the active player.
     * Therefor it brodcasts a /SETACTIVE to all clients in lobby.
     */
    private void updateActivePlayers() {
        lobby.broadcast("/SETACTIVE " + returnActivePlayersAsString());
    }

    /**
     * The tellMarkedToLeave Method is used to remove the marked player from the game.
     */
    private void tellMarkedToLeave() {
        userThreads.get(markedPlayer).sendMessage("/ABDUCTED ");
        userThreads.get(markedPlayer).sendMessage("/UPDATENAME " + userThreads.get(markedPlayer).userName);
        removeUser(markedPlayer);
        resetMarked();
    }

    /**
     * The resetMarked Method resets all variables that are used for a mark event. This Method should be
     * called at the beginning of a day.
     */
    private void resetMarked() {
        markedToday = false;
        markedPlayer = 10;
    }

    //The map is described in MapMatrix.class
    //private MapMatrix testMap = new MapMatrix("TestWelt", 40);

    /**
     * @param lobby the Lobby with all the Players of teh game.
     * @param userThreads the Thread in witch this specific user is running on.
     */
    public GameServer(Lobby lobby, ArrayList<ClientThread> userThreads) {
        isRunning = true;
        this.lobby = lobby;
        this.userThreads = userThreads;
        isDay = true;
        int gameNumCounter = 0;
        for (ClientThread aUser : userThreads) {
            aUser.setGameServer(this);
            aUser.setInGame();
            aUser.gameNum = gameNumCounter;
            gameNumCounter++;
        }
        for(int i = 0; i < userThreads.size(); i++){
            this.userThreads.get(i).userName = userThreads.get(i).userName;
        }
        numMembers = lobby.thislobbylist.size();
        map = new MapMatrix("hiddenInForrestMap", 60, numMembers);

        String toDo = getUserNamesAsString();
        for(int i = 0; i < this.userThreads.size(); i++){
            this.userThreads.get(i).sendMessage("/UPDATEGAMECLIENTS " + toDo);
        }
    }

    /**
     * @return instance of this Lobby.
     */
    public GameServer getInstance(){
        return this;
    }

    /**
     * Gameserver starts a game by giving the Players the map.
     */
    public void execute() {
        for (int i = 0; i < userThreads.size(); i++) {
            userThreads.get(i).setToHuman();
            userThreads.get(i).setInGame();
            userThreads.get(i).setNotGhost();
            numMembers = userThreads.size();
        }

        for (int i = 0; i < numMembers; i++) {
            isActive[i] = true;
        }

        Random random = new Random();
        int int_random = random.nextInt(userThreads.size());
        userThreads.get(int_random).setToAlien();

        //Make ALien/Human
        for (int i = 0; i < userThreads.size(); i++) {
            if (userThreads.get(i).isAlien()) {
                Aliens++;
                aliensInGame.add(userThreads.get(i).getPlayerName());
                userThreads.get(i).sendMessage("/MAKEALIEN true");
            } else {
                Humans++;
                humansInGame.add(userThreads.get(i).getPlayerName());
                userThreads.get(i).sendMessage("/MAKEALLIEN false");
            }
        }
        int i = 0;
        for (ClientThread aUser : userThreads) {
            playerPrefImg[i] = 10;
            if(aUser.isAlien()){
                Aliens++;
                aUser.sendMessage("/REVEAL " + i + true);
            }else{
                Humans++;
                aUser.sendMessage("/REVEAL " + i + false);
            }
            i++;
        }
    }

    /**
     * Message to all clients (broadcasting) except excludedUser.
     */
    void broadcast (String message, ClientThread excludeUser){
        for (ClientThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Message to all clients (broadcasting)
     */
    void broadcast (String message){
        for (ClientThread aUser : userThreads) {
            aUser.sendMessage(message);
        }
    }

    /**
     * @return Usernames as a String with all usernames separated by ":".
     */

    public String getUserNamesAsString(){
        StringBuilder names = new StringBuilder();
        for(int i = 0; i < userThreads.size(); i++){
            names.append(userThreads.get(i).getPlayerName());
            names.append(":");
        }
        return names.toString();
    }

    /**
     * calcualates humans and Aliens
     */
    public void humansRatio(){
        Aliens = 0;
        Humans = 0;
        for (ClientThread client : userThreads) {
            if (client.isAlien()) {
                Aliens++;
            } else {
                Humans++;
            }
        }
    }


    /**
     * @param throwOut (true) decides that a Player can be thrown out.
     */
    public void startDay(Boolean throwOut) {

        if (!throwOut) {
            timerDay = new Timer();
            timerDay.schedule(timerTaskDay, 0, 1000);
            for (ClientThread aClient : userThreads) {
                aClient.hunger = 0.65;
                aClient.sendMessage("/CHANGEHUNGER 0.65");
            }
        }

        if (lobby.isRunning) {
            lobby.broadcast("/SETACTIVE " + returnActivePlayersAsString());

            for (ClientThread aClient : lobby.lobbyClients) {
                    aClient.hasFished = false;
                    aClient.sendMessage("/STARTDAY " + numMembers + ":" + aClient.gameNum);
            }
            String outfits = "/SETOUTFIT ";
            for (int i = 0; i < 10; i++) {
                outfits += playerPrefImg[i];
            }
            lobby.broadcast(outfits);


            //Sends new Mat to all Users in Game.
            String spawnMaterials = map.spawnMaterial(40);
            lobby.broadcast("/NEWMAT " + spawnMaterials);

            for (int k = 0; k < 10; k++) {
                playerPositions[0][k] = map.getPixelsForEachTeil() * map.getColumns() / 2;
                playerPositions[1][k] = map.getPixelsForEachTeil() * map.getRows() / 2;
                playerPositions[2][k] = 0;
            }
            if (userThreads.get(0) != null) {
                ServerProtocolInterpret.serverInterpret("/SENDPOS ", userThreads.get(0),
                        userThreads.get(0).gameServer);
            }
        }
    }

    /**
     * Starts Voting
     */
    public void startVoting() {
        lobby.broadcast("/VOTESTART  ");
        for (int i = 0; i < 11; i++) {
            this.playerVotes[i] = 0;
        }
        for (ClientThread aUser : userThreads) {
            aUser.hasVoted = false;
        }
    }

    /**
     * Determines the Winstate of the Game and shows the next scene according to the state
     */
    public void getWinState(){
        humansRatio();

        if(Humans <= Aliens){
            this.lobby.isRunning = false;
            lostGame();
        } else if(Aliens == 0){
            this.lobby.isRunning = false;
            wonGame();
        }
    }

    /**
     * The Game is won and the server gets told
     */
    public void wonGame(){
        Server.highscoreList.actualizeWinsAsHuman(humansInGame.toArray(new String[humansInGame.size()]));
        lobby.broadcast("/WONGAME ");
        endGame();
    }

    /**
     * The Game is lost and the server gets told
     */
    public void lostGame(){
        Server.highscoreList.actualizeWinsAsAlien(aliensInGame.toArray(new String[aliensInGame.size()]));
        lobby.broadcast("/LOSTGAME ");
        endGame();
    }

    /**
     * The Game is lost and the server gets told
     */
    public void endGame(){
        isRunning = false;
        Server.finishedGames.add(this.lobby.lobbyName);
        try {
            Server.highscoreList.writeFile();
        } catch (IOException e) {
            LOGGER.error(e.getStackTrace());
        }

        LOGGER.info("EndsGame");
        timerDay.cancel();
        lobby.isRunning = false;
        lobby.game = null; //not the best way
        lobby.endGame(lobby);
        for (ClientThread aClient : userThreads) {
            aClient.gameServer = null;
        }
        userThreads.clear();
    }

    /**
     * @return the MapMatrix of this Game.
     */
    public MapMatrix getMapMatrix() {
        return this.map;
    }

    /**
     * Send the client the Task.
     *
     * @param client the client instance.
     * @param column of the GetTaskSign.
     * @param row of the GetTaskSign.
     */
    public void getTask(ClientThread client, int column, int row){
        int[] task = this.map.getTeilsTask(column, row);
        client.sendMessage("\nYour Task:"
                + "\nStone:      " + task[0]
                + "\nWood:       " + task[1]
                + "\nClay:       " + task[2]
                + "\nStrawberry: " + task[3]
                + "\nBerry:      " + task[4]
                + "\nFish:       " + task[5] + "\n\n");
    }

    /**
     * Removes food from the Player and reduces his health
     */
    public void takeFood() {
        for(int i = 0; i < userThreads.size(); i++){
            userThreads.get(i).hunger -= (0.2 / 6);
            if(userThreads.get(i).hunger < 0){
                broadcast("/EJECTION " + userThreads.get(i).gameNum);
                //removeUser(userThreads.get(i).gameNum);
                lobby.broadcast("/RECMSG *SERVER* Player " + userThreads.get(i).userName + " died of hunger.");
            } else {
                userThreads.get(i).sendMessage("/CHANGEHUNGER " + userThreads.get(i).hunger);
            }
        }
    }

    /**
     * If a client disconnects: The thread stops and the name will be deleted.
     *
     * @param playerNr the players number.
     */
    public void removeUser(int playerNr){
        for(int i = 0; i < userThreads.size(); i++){
            if(playerNr == userThreads.get(i).gameNum){
                userThreads.get(i).hasFished = true;
                userThreads.get(i).hasVoted = true;
                userThreads.get(i).setNotInGame();
                lobby.leaveLobby(userThreads.get(i));
                numMembers = userThreads.size();
                String toDo = getUserNamesAsString();
                System.out.println(toDo);
                lobby.broadcast("/UPDATEGAMECLIENTS " + toDo);
            }
        }
        playerPositions[2][playerNr] = 4;
    }

    /**
     * Updates the active players who arent ghosts and are
     * actively playing the game
     *
     * @return String of ActivePlayers
     */
    public String returnActivePlayersAsString() {
        String activePlayers = "";
        int isActive = 0;
        for (ClientThread aUser: lobby.lobbyClients) {
            if (!aUser.isGhost) {
                activePlayers += aUser.gameNum + ":";
                this.isActive[isActive] = true;
            } else {
                this.isActive[isActive] = false;
            }
            isActive++;
        }
        return activePlayers;
    }

    /**
     * @return the amount of tasks done. (int)
     */
    public int getDoneTasks() {
        return doneTasks;
    }

    /**
     * Adds one done Task to the amount of done Tasks.
     */
    public void addDoneTask() {
        doneTasks++;
    }

    /**
     * Changes safety and broadcasts it to clients.
     */
    public void changeSafety() {
        safety = doneTasks / 10.0;
        lobby.broadcast("/CHANGESAFETY " + safety);
    }

    /**
     * Here the game determines which player has the most amount of votes
     * and throws that player out. It also tells all the other Players in
     * the Lobby who the thrown out Player is
     */
    public void throwOut() {
        for (int i = 0; i < 11; i++) {
            LOGGER.debug("Player " + i + "has " + playerVotes[i] + " Votes");
        }
        boolean oneMost = false; //should throw voted player out.
        int maxVotes = 0;
        int hasMostVotes = 0;
        for (int i = 0; i < 10 + 1; i++) {
            if (this.playerVotes[i] > maxVotes) {
                oneMost = true;
                maxVotes = this.playerVotes[i];
                hasMostVotes = i;
            } else if (this.playerVotes[i] == maxVotes) {
                oneMost = false;
            }
        }

        if (oneMost) {
            if (hasMostVotes == 0) {
                broadcast("You voted to SKIP");
                lobby.broadcast("/EJECTION 10");
            } else {
                this.broadcast("/RECMSG PLayer number " + hasMostVotes + " should be removed from game");
                lobby.broadcast("/EJECTION " + (hasMostVotes - 1));
                LOGGER.debug("Aliens = " + Aliens + "/nHumans = " + Humans);
            }
        }
    }
}
