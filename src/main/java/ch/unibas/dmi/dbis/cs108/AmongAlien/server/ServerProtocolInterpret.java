package ch.unibas.dmi.dbis.cs108.AmongAlien.server;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.AmongAlienProtocol;
import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.MapMatrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The ServerProtocolInterpret gets commands from the client and
 * tells the Server what he should do. To do this, he uses the
 * AmongAlienProtocol and Switches these entries.
 *
 * @author Joel Erbsland, Rayes Diyab and Hamza Zarah
 */
public class ServerProtocolInterpret {
    /*----------------------------------------------------------------------*/
    /*                       int values for materials                       */
    /*----------------------------------------------------------------------*/
    final static int STONE = 11;
    final static int WOOD = 21;
    final static int CLAY = 30;
    final static int STRAWBERRY = 31;
    final static int BERRYBUSH = 32;
    final static int FISH = 34;

    final static int HOUSE_UP_LEFT = 77;
    final static int HOUSE_MIDDEL_LEFT = 87;
    final static int HOUSE_LOW_LEFT = 97;
    final static int HOUSE_UP_MIDDEL = 78;
    final static int HOUSE_MIDDEL_MIDDEL = 88;
    final static int HOUSE_LOW_MIDDEL = 98;
    final static int HOUSE_UP_RIGHT = 79;
    final static int HOUSE_MIDDEL_RIGHT = 89;
    final static int HOUSE_LOW_RIGHT = 99;

    final static int GET_TASK_SIGN = 26;
    final static int FLOWER_GRASS = 0;
    final static int NORMAL_GRASS = 1;

    /*----------------------------------------------------------------------*/
    /*                       int values for fieldUSE                        */
    /*----------------------------------------------------------------------*/
    final static int ACCESSIBLE_SPAWNABLE = 0;
    final static int INACCESSIBLE = 1;
    final static int FIELD_WITH_MATERIAL = 2;
    final static int GETTASK_FIELD = 3;
    final static int FISHINGROTH = 4;
    final static int ACCESSIBLE_NO_MAT = 5;

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * This is to Interpret of the Protocol. The given string will be analyzed
     * for commands. If there is a command, the interpreter calls the next instance.
     * The Interpreter is checking if the Message beginns with a Slash. If yes
     * the following words will be sent as the "toDo_"-String to the right case
     * in the Switch-Case to get the right function.
     *
     * @param toInterpret The String given to interpret (looking for a command)
     * @param client The client witch sent the toInterpret-String
     * @param server The server on witch all is Running (socket for connection)
     */
    public static void serverInterpret(String toInterpret, ClientThread client,
                                       Server server) {
        LOGGER.debug("ServerInterpret toInterpret = " + toInterpret);
        AmongAlienProtocol type;
        int index = 0;
        String commandRead = "";
        String toDo;
        toInterpret += " ";
        if (toInterpret.charAt(index) != '/') {
            LOGGER.debug("No command, just a MSG!");
            type = AmongAlienProtocol.SETMSG;
            toDo = toInterpret;
        } else {
            type = AmongAlienProtocol.SETMSG;
            index++;
            while(toInterpret.charAt(index) != ' ') {
                commandRead += toInterpret.charAt(index);
                index++;
            }
            LOGGER.debug("Command = " + commandRead);
            toDo = toInterpret.substring(index++, toInterpret.length() -1);
            try {
                type = AmongAlienProtocol.valueOf(commandRead);
            } catch (IllegalArgumentException ex) {
                LOGGER.fatal(ex.getStackTrace());
            }
        }
        System.out.println(toDo);

        switch(type) {
            case STARTGAME:
                if (client.currentLobby != null) {
                    if (client.isLobbyHost()) {
                        client.currentLobby.startGame();
                    }
                }
                break;
            case SETMSG:

                if (toDo.startsWith("GIVEMEALL")) {
                    for (int i = 0; i < 5; i++) {
                        client.addMat(STONE);
                        client.sendMessage("/TAKEMAT " + STONE);
                        client.addMat(WOOD);
                        client.sendMessage("/TAKEMAT " + WOOD);
                        client.addMat(STRAWBERRY);
                        client.sendMessage("/TAKEMAT " + STRAWBERRY);
                        client.addMat(BERRYBUSH);
                        client.sendMessage("/TAKEMAT " + BERRYBUSH);
                        client.addMat(FISH);
                        client.sendMessage("/TAKEMAT " + FISH);
                        client.addMat(CLAY);
                        client.sendMessage("/TAKEMAT " + CLAY);
                        client.hunger = 0.2;
                        client.sendMessage("/CHANGEHUNGER " + client.hunger);
                    }
                    toDo = " Player: " + client.userName + " used a Cheatcode";
                }

                if (toDo.startsWith("@")) {
                    int space = toDo.indexOf(' ', 1);
                    if(toDo.substring(1,space).equals("SHOUT")){
                        String message = toDo.substring(space + 1);
                        for(int i = 0; i < Server.userThreads.size(); i++){
                            Server.userThreads.get(i).sendMessage("/RECMSG [" + client.userName + "] SHOUTS: " + message.toUpperCase());
                        }
                        break;
                    }else{
                        space = toDo.indexOf(' ', 1);
                        String user = toDo.substring(1, space);
                        user = " "+user;
                        String message = toDo.substring(space + 1);
                        System.out.println("WHISPER IM SPI"+message);
                        for(int i = 0; i < Server.userThreads.size(); i++){
                            System.out.println(Server.userThreads.get(i).userName + "=" + user);
                            if(Server.userThreads.get(i).userName.equals(user)){
                                Server.userThreads.get(i).sendMessage("/RECMSG [" + client.userName + "] Privat : " + message);
                            }
                        }
                        break;
                    }
                }
                if (toDo.startsWith("  getUsernames")) {
                    for (String s : server.getUserNames()) {
                        client.sendMessage("/RECMSG -" + s);
                    }
                }  else {
                    toDo = "/RECMSG [" + client.getUserName() + "]: " + toDo;
                    if(client.isInLobby()){
                        for(int i = 0; i < client.currentLobby.lobbyClients.size(); i++) {
                            if (client.currentLobby.lobbyClients.get(i) != client) {
                                client.currentLobby.lobbyClients.get(i).sendMessage(toDo);
                            }
                        }
                    }else if(!client.isInLobby()){
                        for(int i = 0; i < Server.userThreads.size(); i++){
                                if(Server.userThreads.get(i) != client && !Server.userThreads.get(i).isInLobby()){
                                    Server.userThreads.get(i).sendMessage(toDo);
                                }
                        }
                    }
                }
                break;

            case ADDMAT:
                if (!client.isGhost) {
                    toDo = toDo.substring(1, toDo.length());
                    MapMatrix map = client.currentLobby.game.getMapMatrix();
                    String[] sPos = toDo.split(":");
                    int[] partPos = new int[3];

                    for (int i = 0; i < 3; i++) {
                        partPos[i] = Integer.parseInt(sPos[i]);
                    }

                    if (map.getTeilsfieldUSE(partPos[0], partPos[1]) == FIELD_WITH_MATERIAL) {
                        client.addMat((map.getTeilsTextureID(partPos[0], partPos[1])));
                        if (map.getTeilsTextureID(partPos[0], partPos[1]) == STRAWBERRY
                                || map.getTeilsTextureID(partPos[0], partPos[1]) == BERRYBUSH) {
                            client.hunger += 0.2;
                            if (client.hunger > 1.0) {
                                client.hunger = 1.0;
                            }
                            client.sendMessage("/CHANGEHUNGER " + client.hunger);
                        }

                        client.currentLobby.broadcast("/DELMAT " + toDo);
                        client.sendMessage("/TAKEMAT " + map.getTeilsTextureID(partPos[0], partPos[1]));
                        map.changeMapTeils(partPos[0], partPos[1], 0, 0);
                    } else if (map.getTeilsfieldUSE(partPos[0], partPos[1]) == FISHINGROTH) {
                        if (!client.hasFished) {
                            client.hasFished = true;
                            client.addMat(FISH);
                            client.sendMessage("/TAKEMAT " + FISH);
                            client.hunger += 0.2;
                            if (client.hunger > 1.0) {
                                client.hunger = 1.0;
                            }
                            client.sendMessage("/CHANGEHUNGER " + client.hunger);
                        }
                    } else if (map.getTeilsfieldUSE(partPos[0], partPos[1]) == GETTASK_FIELD) {
                        client.gameServer.getTask(client, partPos[0], partPos[1]);
                    } else {
                        client.sendMessage("[Server]: No material found");
                    }
                }
                break;

            case VOTEFOR:
                if (!client.hasVoted) {
                    if (Integer.parseInt(toDo.substring(2)) == 0) {
                        client.currentLobby.game.playerVotes[0]++;
                        client.hasVoted = true;
                    } else if(client.currentLobby.game.isActive[Integer.parseInt(toDo.substring(2)) - 1]) {
                        client.currentLobby.game.playerVotes[Integer.parseInt(toDo.substring(2))]++;
                        client.hasVoted = true;
                    }
                    boolean allPlayersVoted = true;
                    for (ClientThread aUser : client.currentLobby.game.userThreads) {
                        if (!aUser.hasVoted) {
                            allPlayersVoted = false;
                        }
                    }
                    if (allPlayersVoted) { //skips the voting if everyone has voted.
                        client.currentLobby.game.timer = 9;
                    }
                }
                break;

            case GETGAMELIST:
                String finishedGames = server.getFinishedGamesAsString();
                System.out.println(finishedGames);
                String openGames = server.getOpenGamesAsString();
                openGames = openGames + "o§";
                String runningGames = server.getGameListAsString();
                runningGames = runningGames + "r§";
                System.out.println("IM SPI RUNNING GAMES"+runningGames);
                client.sendMessage("/GAMELIST "+openGames+ runningGames + finishedGames);
                break;

            case NEWMAT:
                int point1 = toDo.indexOf(':');
                int point2 = toDo.indexOf(':', point1 + 1);
                int indexMat = 0;
                while (server.materials[1][indexMat] != 0 && server.materials[2][indexMat] != 0) {
                    indexMat++;
                }
                client.gameServer.materials[1][indexMat] = Integer.parseInt(toDo.substring(point1 + 1, point2));
                client.gameServer.materials[2][indexMat] = Integer.parseInt(toDo.substring(point2 + 1));
                client.gameServer.materials[0][indexMat] = Integer.parseInt(toDo.substring(1, point1));
                break;

            case DOTASK:
                if (client.isGhost) {
                    client.sendMessage("/SETMSG [Server]: You can't do Tasks as a Ghost!");
                } else if (client.isAlien()) {
                    client.sendMessage("[Server]: Doing Tasks is not your hobby!"
                            + "\nAliens should mark humans!");
                } else {
                    toDo = toDo.substring(1);
                    MapMatrix map = client.currentLobby.game.getMapMatrix();

                    String[] sPos = toDo.split(":");
                    int[] partPos = new int[2];

                    for (int i = 0; i < 2; i++) {
                        partPos[i] = Integer.parseInt(sPos[i]);
                    }

                    if (map.getTeilsfieldUSE(partPos[0], partPos[1]) != GETTASK_FIELD) {
                        client.sendMessage("[Server]: There is no Task to get!");
                        break;
                    }

                    int[] task = map.getTeilsTask(partPos[0], partPos[1]);

                    if (task[0] <= client.materials[STONE]
                            && task[1] <= client.materials[WOOD]
                            && task[2] <= client.materials[CLAY]
                            && task[3] <= client.materials[STRAWBERRY]
                            && task[4] <= client.materials[BERRYBUSH]
                            && task[5] <= client.materials[FISH]) {

                        client.removedMat(STONE, task[0]);
                        client.removedMat(WOOD, task[1]);
                        client.removedMat(CLAY, task[2]);
                        client.removedMat(STRAWBERRY, task[3]);
                        client.removedMat(BERRYBUSH, task[4]);
                        client.removedMat(FISH, task[5]);

                        client.sendMessage("/ACTMAT " + client.materials[STONE] + ":" + client.materials[WOOD]
                                + ":" + client.materials[CLAY] + ":" + client.materials[STRAWBERRY]
                                + ":" + client.materials[BERRYBUSH] + ":" + client.materials[FISH]);

                        client.currentLobby.game.addDoneTask();

                        int column = partPos[0];
                        int row = partPos[1];

                        LOGGER.debug("Aktualizing MapMatrix");
                        map.changeMapTeils(column, row, NORMAL_GRASS, ACCESSIBLE_NO_MAT);
                        map.changeMapTeils(column + 1, row, HOUSE_LOW_LEFT, INACCESSIBLE);
                        map.changeMapTeils(column + 1, row -1, HOUSE_MIDDEL_LEFT, INACCESSIBLE);
                        map.changeMapTeils(column + 1, row - 2, HOUSE_UP_LEFT, INACCESSIBLE);
                        map.changeMapTeils(column + 2, row, HOUSE_LOW_MIDDEL, INACCESSIBLE);
                        map.changeMapTeils(column + 2, row - 1, HOUSE_MIDDEL_MIDDEL, INACCESSIBLE);
                        map.changeMapTeils(column + 2, row -2, HOUSE_UP_MIDDEL, INACCESSIBLE);
                        map.changeMapTeils(column + 3, row, HOUSE_LOW_RIGHT, INACCESSIBLE);
                        map.changeMapTeils(column + 3, row - 1, HOUSE_MIDDEL_RIGHT, INACCESSIBLE);
                        map.changeMapTeils(column + 3, row -2, HOUSE_UP_RIGHT, INACCESSIBLE);

                        client.currentLobby.game.broadcast("/ACTMAP "
                                + column + ":" + row + ":" + 1 + ":" + ACCESSIBLE_NO_MAT  + ":"
                                + (column + 1) + ":" + (row - 0) + ":" + HOUSE_LOW_LEFT + ":" + INACCESSIBLE  + ":"
                                + (column + 1) + ":" + (row - 1) + ":" + HOUSE_MIDDEL_LEFT + ":" + INACCESSIBLE  + ":"
                                + (column + 1) + ":" + (row - 2) + ":" + HOUSE_UP_LEFT + ":" + INACCESSIBLE  + ":"
                                + (column + 2) + ":" + (row - 0) + ":" + HOUSE_LOW_MIDDEL + ":" + INACCESSIBLE  + ":"
                                + (column + 2) + ":" + (row - 1) + ":" + HOUSE_MIDDEL_MIDDEL + ":" + INACCESSIBLE  + ":"
                                + (column + 2) + ":" + (row - 2) + ":" + HOUSE_UP_MIDDEL + ":" + INACCESSIBLE  + ":"
                                + (column + 3) + ":" + (row - 0) + ":" + HOUSE_LOW_RIGHT + ":" + INACCESSIBLE  + ":"
                                + (column + 3) + ":" + (row - 1) + ":" + HOUSE_MIDDEL_RIGHT + ":" + INACCESSIBLE  + ":"
                                + (column + 3) + ":" + (row - 2) + ":" + HOUSE_UP_RIGHT + ":" + INACCESSIBLE);

                        client.currentLobby.game.changeSafety();

                        if (client.currentLobby.game.getDoneTasks() >= 10) {
                            client.currentLobby.game.wonGame();
                        }

                    } else {
                        client.sendMessage("[Server]: Not enough materials to complete"
                                + "\n          this task! Press T to see what you need!");
                    }
                }
                break;

            case EJECTION:
                System.out.println("EJECTION IN SPI");
                client.sendMessage("/LOBBYSCREEN");
                client.currentLobby.game.tellVotedToLeave(client.gameNum);
                ServerProtocolInterpret.serverInterpret("/UPDATELOBBYLIST", client, server);
                ServerProtocolInterpret.serverInterpret("/UPDATEPLAYERS", client, server);
                break;

            case UPDATELOBBYGUI:
                ServerProtocolInterpret.serverInterpret("/UPDATELOBBYLIST", client, server);
                ServerProtocolInterpret.serverInterpret("/UPDATEPLAYERS", client, server);
                break;

            case HUNGERED:
                client.sendMessage("/LOBBYSCREEN");
                client.currentLobby.game.tellVotedToLeave(client.gameNum);
                ServerProtocolInterpret.serverInterpret("/UPDATELOBBYLIST", client, server);
                ServerProtocolInterpret.serverInterpret("/UPDATEPLAYERS", client, server);
                break;

            case MARK:
                GameServer game = client.currentLobby.game;
                if (client.isAlien()) {
                    if(!game.markedToday) {
                        int markedPlayer = 10;
                        for (int i = 0; i < 10; i++) {
                            if (game.isActive[i] && client.gameNum != i) {
                                if (Math.abs(game.playerPositions[0][i] - game.playerPositions[0][client.gameNum]) < 50
                                        && Math.abs(game.playerPositions[1][i]
                                        - game.playerPositions[1][client.gameNum]) < 50) {
                                    markedPlayer = i;
                                    i = 10;
                                }
                            }
                        }
                        if (markedPlayer != 10 && game.dayLength - game.timer > 13) {
                            game.markedPlayer = markedPlayer;
                            game.markedToday = true;
                            client.sendMessage("/RECMSG [Server]: You marked Player " + (markedPlayer + 1));
                        }
                    }
                }
                break;

            case SETPLAYERIMAGE:
                int playerNr = Integer.parseInt(toDo.substring(1, 2));
                int prefImg = Integer.parseInt(toDo.substring(2));
                boolean isAvailable = true;
                for (int i = 0; i < client.currentLobby.game.numMembers; i++) {
                    if (client.currentLobby.game.playerPrefImg[i] == prefImg) {
                        isAvailable = false;
                        i = 10;
                    }
                }
                if (isAvailable) {
                    client.playersImage = prefImg;
                    client.currentLobby.game.playerPrefImg[playerNr] = prefImg;
                    client.currentLobby.broadcast("/SETPLAYERIMAGE " + playerNr + prefImg);
                }


                boolean allPlayerchose = true;
                for (int k = 0; k < client.currentLobby.game.numMembers; k++) {
                    if (client.currentLobby.game.isActive[k]) {
                        if (client.currentLobby.game.playerPrefImg[k] == 10) {
                            allPlayerchose = false;
                        }
                    }
                }
                if (allPlayerchose) {
                    client.currentLobby.game.startDay(false);
                }
                break;

            case SETPOS:
                String[] newPos = toDo.split(":");
                int point = toDo.indexOf(':');

                MapMatrix map2 = client.currentLobby.game.getMapMatrix();
                if (map2.getTeilsfieldUSE((Integer.parseInt(toDo.substring(1, point)) / map2.getPixelsForEachTeil()),
                        (Integer.parseInt(toDo.substring(point + 1,
                                toDo.length()- 2)) / map2.getPixelsForEachTeil())) != 1) {

                    client.currentLobby.game.playerPositions[0][client.gameNum]
                            = Integer.parseInt(toDo.substring(1, point));
                    client.currentLobby.game.playerPositions[1][client.gameNum]
                            = Integer.parseInt(toDo.substring(point + 1,
                            toDo.length()- 2));
                    if (!client.isGhost) {
                        client.currentLobby.game.playerPositions[2][client.gameNum]
                                = Integer.parseInt(toDo.substring(toDo.length() - 1));
                    }
                    serverInterpret("/SENDPOS  ", client, server);
                }

                break;

            case GETSCORE:
                client.sendMessage("/YOURSCORE " + Server.highscoreList.highscoreToOneLineString());
                break;

            case LOGIN:
                String userName = toDo;
                server.addUserName(userName, client);
                break;

            case SENDPOS:
                String posToString = "/NEWPOS ";
                for (int i = 0; i < 10; i++){
                    for (int j = 0; j < 3; j++) {
                        posToString += client.currentLobby.game.playerPositions[j][i] + ":";
                    }
                }
                client.currentLobby.broadcast(posToString);
                break;

            case UPDATEPLAYERS:
                String players = server.getPlayerListAsString();
                server.broadcast("/UPDATEPLIST " + players);
                System.out.println("Players in SPI" + players);
                break;

            case QUITGAMEGUI:
                ServerProtocolInterpret.serverInterpret("/LOGOUT", client, server);
                ServerProtocolInterpret.serverInterpret("/UPDATELOBBYLIST", client, server);
                ServerProtocolInterpret.serverInterpret("/UPDATEPLAYERS", client, server);
                break;

            case QUITLOBBYGUI:
                ServerProtocolInterpret.serverInterpret("/LEAVELOBBY", client, server);
                ServerProtocolInterpret.serverInterpret("/LOGOUT", client, server);
                ServerProtocolInterpret.serverInterpret("/UPDATELOBBYLIST", client, server);
                ServerProtocolInterpret.serverInterpret("/UPDATEPLAYERS", client, server);
                break;

            case LEAVELOBBY:
                try{
                    client.currentLobby.leaveLobby(client);
                    ServerProtocolInterpret.serverInterpret("/UPDATELOBBYLIST", client, server);
                    client.sendMessage("/UPDATEPLAYERLOBBY");
                }catch (NullPointerException ignored){
                    LOGGER.fatal(ignored.getStackTrace());
                }
                break;

            case JOINLOBBY:
                try {
                    toDo = toDo.substring(3);
                }catch (StringIndexOutOfBoundsException e){
                    LOGGER.error(e.getStackTrace());
                    break;
                }

                try{
                    System.out.println("To join Lobby in SPI: "+toDo);
                    Lobby.getLobby(toDo).joinLobby(client);
                    ServerProtocolInterpret.serverInterpret("/UPDATELOBBYLIST", client, server);
                }catch (NullPointerException e){
                    LOGGER.error("Cant join Lobby! NULLPOINTER IN SPI");
                    LOGGER.error(e.getStackTrace());
                }
                break;

            case NEWLOBBY:
                System.out.println("To create Lobby");
                Lobby lobby = new Lobby(toDo, client);
                lobby.start();
                ServerProtocolInterpret.serverInterpret("/UPDATELOBBYLIST", client, server);
                client.sendMessage("/UPDATEPLAYERLOBBY " + toDo);
                break;

            case UPDATELOBBYLIST:
                String lobbyList = server.getLobbyListAsString();
                server.broadcast("/UPDATELOBBYLIST " + lobbyList);
                break;

            case LOGOUT:
                server.removeUser(client);
                client.sendMessage("/QUIT");
                client.pingThread.stop();
                break;

            case PONG:
                client.pingThread.resetCounter();
                break;

            case HELP:
                client.sendMessage("/HELP");
                break;

            case SETNAME:
                server.changeName(toDo,client);
                ServerProtocolInterpret.serverInterpret("/UPDATEPLAYERS", client, server);
                if(client.isInLobby()){
                    String lobbyPlist = client.currentLobby.getPlayerListAsString();
                }
                client.sendMessage("/UPDATELOBBYPLAYERS");
                break;

            default:
                break;
        }
    }
}
