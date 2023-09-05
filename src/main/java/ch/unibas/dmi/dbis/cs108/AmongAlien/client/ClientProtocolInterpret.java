package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GUI;
import ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GameController;
import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.AmongAlienProtocol;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

/**
 * The ClientProtocolInterpret will be called if the client gets a message from
 * the server. The ClientProtocolInterpret checks if it is a command or just a
 * simple message. The ClientProtocolInterpret calls the next steps depending
 * on the command.
 *
 * @author Joel Erbsland, Rayes Diyab, Hamza Zarah
 * @version 16.05.2022
 */
public class ClientProtocolInterpret {

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /** Method separates the prefix that indicates the type of interpretation
     * and the String which contains the message.
     * Then it switches to the certain case of interpretation.
     * To get a Information about a CASE you can go over the ProtocolType
     * with your cursor.
     *
     * @param toInterpret is the Sting which needs to get interpreted.
     * @param client is the client who asks for the interpretation.
     */
    public static void clientInterpret(String toInterpret, AmongAlienClient client) {

        AmongAlienProtocol type = AmongAlienProtocol.RECMSG;
        int index = 0;
        String commandRead = "";
        String toDo;
        toInterpret += " ";
        if (toInterpret.charAt(index) != '/') {
            toDo = toInterpret;
        } else {
            index++;
            while (toInterpret.charAt(index) != ' ') {
                commandRead += toInterpret.charAt(index);
                index++;
            }
            toDo = toInterpret.substring(index, toInterpret.length() - 1);

            try {
                type = AmongAlienProtocol.valueOf(commandRead);
            } catch (IllegalArgumentException ex) {
                LOGGER.error(ex.getStackTrace());
            }
        }

        switch (type) {
            case SETMSG:
                client.sendToServer("/SETMSG " + toDo);
                break;

            case RECMSG:
                GUI.getInstance().chatMessage(toDo);
                break;

            case QUIT:
                System.out.println("You have exited Chat!");
                System.exit(0);
                break;

            case PING:
                client.ponger.resetCounter();
                break;


            case UPDATEPLIST:
                GUI.getInstance().updateAllPlayerList(toDo);
                break;

            case UPDATELOBBYLIST:
                GUI.getInstance().updateLobbyLists(toDo);
                break;

            case UPDATELOBBYPLAYERS:
                GUI.getInstance().updateLobbyPlayers(toDo);
                break;

            case UPDATEPLAYERLOBBY:
                GUI.getInstance().updateLobbyName(toDo);
                break;

            case UPDATELEFTLOBBY:
                GUI.getInstance().updateLeftLobby();
                break;

            case UPDATENAME:
                GUI.getInstance().updateUserName(toDo);
                break;

            case HELP:
                String Help = "******************************************\n"
                        + ("            Help Menu\nAll Commands must be in CAPITAL Letters!")
                        + ("\n/LOGOUT     Logout from the Server and\n" + "            close the Client")
                        + ("\n/QUIT       Close the Client without logout.")
                        + "\n******************************************\n";

                GUI.getInstance().chatMessage(Help);
                break;

            case CONNECTIONLOST:
                try {
                    String filename = "/ConnectionLost.fxml";
                    GUI.getInstance().switchScene(filename);
                } catch (IOException e) {
                    LOGGER.error(e.getStackTrace());
                    LOGGER.error("Failed to load ConnectionLost.fxml");
                }
                break;

            case PLAYERNUM:
                int space = toDo.indexOf(' ',1);
                client.playerNumber = Integer.parseInt(toDo.substring(1, space));
                GUI.getInstance().playerNumber
                        = Integer.parseInt(toDo.substring(1, space));
                GUI.getInstance().playerName = toDo.substring(space + 1);
                break;

            case NEWPOS: //right now to possibilities. hashmap with all player or array with all positions.
                String[] splitPos = toDo.substring(1).split(":");
                for (int i = 0; i < splitPos.length / 3; i++) {
                    client.allPlayers2[0][i] = Integer.parseInt(splitPos[3 * i]);
                    client.allPlayers2[1][i] = Integer.parseInt(splitPos[3 * i + 1]);
                    client.allPlayers2[2][i] = Integer.parseInt(splitPos[3 * i + 2]);
                }
                GUI.getInstance().updateMapWindow();
                break;

            case LOBBYSCREEN:
                try{
                    String filename = "/GUILobby.fxml";
                    GUI.getInstance().switchScene(filename);
                } catch (IOException e) {
                    LOGGER.error("Failed to load /GUILobby.fxml");
                }

                break;

            case NEWMAT:
                toDo = toDo.substring(1, toDo.length());
                String[] partsAdd = toDo.split(":");
                for (int i = 0; i < partsAdd.length; i += 3) {
                    GUI.getInstance().mapMatrix.changeMapTeils(Integer.parseInt(partsAdd[i]),
                            Integer.parseInt(partsAdd[i + 1]),
                            Integer.parseInt(partsAdd[i + 2]),2);
                }
                GUI.getInstance().createNewMap();
                break;

            case DELMAT:
                toDo = toDo.substring(1);
                String[] partsDel = toDo.split(":");
                for (int i = 0; i < partsDel.length; i += 3) {
                    GUI.getInstance().mapMatrix.changeMapTeils(Integer.parseInt(partsDel[i]),
                            Integer.parseInt(partsDel[i + 1]), 0,0);

                }
                GUI.getInstance().createNewMap();
                break;

            case SETNAME:
                GUI.getInstance().updateUserName(toDo);
                break;

            case VOTESTART:
                GUI.getInstance().inGame = false;
                try{
                    String filename = "/GUIVoting.fxml";
                    GUI.getInstance().switchScene(filename);
                } catch (Exception e) {
                    LOGGER.error(e.getStackTrace());
                    LOGGER.error("/GUIVoting.fxml not availible");
                }
                break;

            case ABDUCTED:
                GUI.getInstance().inGame = false;
                try{
                    String filename = "/GUIVoted.fxml";
                    GUI.getInstance().switchScene(filename);
                } catch (Exception e) {
                    LOGGER.error(e.getStackTrace());
                    LOGGER.error("/GUIVoted.fxml not availible");
                }
                break;

            case SETMAP:
                GUI.getInstance().createNewMap();
                break;

            case STARTDAY:
                try {
                    int i = toDo.indexOf(':');
                    GUI.getInstance().numberOfPlayers = Integer.parseInt(toDo.substring(1, i));
                    GUI.getInstance().playerNumber = Integer.parseInt(toDo.substring(1 + i));
                    String filename = "/GUIGame.fxml";
                    GUI.getInstance().switchScene(filename);
                    GUI.getInstance().startMap();

                } catch (IOException e) {
                    LOGGER.error("Scene not found" + e.getMessage());
                    LOGGER.error(e.getStackTrace());
                }

                GUI.getInstance().gameController.setGhost(!GUI.getInstance().isActive[GUI.getInstance().playerNumber]);
                break;

            case VOTEFOR:
                client.sendToServer("/VOTEFOR " + toDo);
                break;

            case GAMEISWON:
                GUI.getInstance().gameIsWon();
                break;

            case GAMEISLOST:
                GUI.getInstance().gameIsLost();
                break;

            case TAKEMAT:
                GUI.getInstance().gameController.addMat(Integer.parseInt(toDo.substring(1)));
                GUI.getInstance().gameController.updateMaterials();
                break;

            case EJECTION:
                int player = Integer.parseInt(toDo.substring(1));

                    GUI.getInstance().ejectedPlayer = player;

                    if (player == GUI.getInstance().playerNumber) { //You are ejected
                        try {
                            client.sendToServer("/EJECTION");
                        } catch (Exception e) {
                            LOGGER.error(e.getStackTrace());
                        }
                    }

                    //EJECTION 10 Skips the vote
                    //EJECTION ## sets the Ejected Player as the received PlayerNum
                break;

            case WONGAME:
                try {
                    String filename = "/GUIWinLose.fxml";
                    GUI.getInstance().switchScene(filename);
                    if (GUI.getInstance().isAlien) {
                        GUI.getInstance().winLoseController.showDefeat();
                    } else {
                        GUI.getInstance().winLoseController.showVictory();
                    }

                } catch (Exception e) {
                    LOGGER.error(e.getStackTrace());
                }
                break;

            case LOSTGAME:

                try {
                    String filename = "/GUIWinLose.fxml";
                    GUI.getInstance().switchScene(filename);
                    if (!GUI.getInstance().isAlien) {
                        GUI.getInstance().winLoseController.showDefeat();
                    } else {
                        GUI.getInstance().winLoseController.showVictory();
                    }

                } catch (Exception e) {
                    LOGGER.error(e.getStackTrace());
                }

                break;

            case CHANGEHUNGER:
                try {
                    GUI.getInstance().gameController.changeHunger(Double.parseDouble(toDo.substring(1)));
                    GUI.getInstance().gameController.hunger = Double.parseDouble(toDo.substring(1));
                } catch (NullPointerException e) {
                    LOGGER.error("caught NPE");
                    LOGGER.error(e.getStackTrace());
                }
                break;

            case CHANGESAFETY:
                try {
                    GUI.getInstance().gameController.changeDorfsicherheit(Double.parseDouble(toDo.substring(1)));
                    GUI.getInstance().gameController.safety = Double.parseDouble(toDo.substring(1));
                } catch (NullPointerException e) {
                    LOGGER.error("caught NPE");
                    LOGGER.error(e.getStackTrace());
                }
                break;

            case MAKEALIEN:
                boolean alien = Boolean.parseBoolean(toDo.substring(1));
                GUI.getInstance().isAlien = alien;
                break;

            case SETPLAYERIMAGE:

                GUI.getInstance().playerPrefImg[Integer.parseInt(toDo.substring(1, 2))]
                        = Integer.parseInt(toDo.substring(2)); //hide the Image
                if (Integer.parseInt(toDo.substring(1, 2)) == GUI.getInstance().playerNumber) {
                    GUI.getInstance().playerChooseController.setDefinitivPlayer(Integer.parseInt(toDo.substring(2)));

                } else {
                    GUI.getInstance().playerChooseController.disablePlayer(Integer.parseInt(toDo.substring(2)));
                }

                break;

            case REVEAL:
                GUI.getInstance().playerNumber = Integer.parseInt(toDo.substring(1, 2));
                GUI.getInstance().isAlien = Boolean.parseBoolean(toDo.substring(2));
                try {
                    String filename = "/GUIPlayerChoose.fxml";
                    GUI.getInstance().switchScene(filename);
                    GUI.getInstance().playerChooseController.setText(GUI.getInstance().isAlien);
                } catch (Exception e) {
                    LOGGER.error(e.getStackTrace());
                }
                break;

            case SETOUTFIT:

                toDo = toDo.substring(1);

                int limit = toDo.length();
                toDo += " ";
                for (int count = 0; count < limit; count++) {
                    GUI.getInstance().playerPrefImg[count] = Integer.parseInt(toDo.substring(count, count + 1));
                }

                break;


            case UPDATEGAMECLIENTS:
                LOGGER.debug("STIRBT IM CPI ="+toDo);
                GUI.getInstance().updateGamePlayers(toDo);
                break;

            case ACTMAT:
                //System.out.println("In CPI ACTMAT!");
                toDo = toDo.substring(1);
                //System.out.println("toDo is:" + toDo);
                int stone = 11;
                int wood = 21;
                int clay = 30;
                int strawberry = 31;
                int berrybush = 32;
                int fish = 34;
                String[] actmat = toDo.split(":");
                GameController.materials[stone] = Integer.parseInt(actmat[0]);
                GameController.materials[wood] = Integer.parseInt(actmat[1]);
                GameController.materials[clay] = Integer.parseInt(actmat[2]);
                GameController.materials[strawberry] = Integer.parseInt(actmat[3]);
                GameController.materials[berrybush] = Integer.parseInt(actmat[4]);
                GameController.materials[fish] = Integer.parseInt(actmat[5]);
                GUI.getInstance().gameController.updateMaterials();
                break;

            case ACTMAP:
                toDo = toDo.substring(1);
                String[] taskPartsAdd = toDo.split(":");
                for (int i = 0; i < taskPartsAdd.length; i += 4) {
                    GUI.getInstance().mapMatrix.changeMapTeils(Integer.parseInt(taskPartsAdd[i]),
                            Integer.parseInt(taskPartsAdd[i + 1]),
                            Integer.parseInt(taskPartsAdd[i + 2]),
                            Integer.parseInt(taskPartsAdd[i + 3]));
                }
                GUI.getInstance().createNewMap();
                break;

            case SETACTIVE:
                toDo = toDo.substring(1);
                String[] activeP = toDo.split(":");
                GUI.getInstance().numberOfPlayers = activeP.length;
                for (int i = 0; i < 10; i++) {
                    GUI.getInstance().isActive[i] = false;
                }
                for (int i = 0; i < activeP.length; i++) {
                    GUI.getInstance().isActive[Integer.parseInt(activeP[i])] = true;
                }
                break;

            case YOURSCORE:
                toDo = toDo.substring(36);
                StringBuilder usernameForList = new StringBuilder();
                StringBuilder winAsHumanForList = new StringBuilder();
                StringBuilder winAsALienForList = new StringBuilder();
                String[] highscores = toDo.split(":");
                for (int i = 3; i < highscores.length; i += 3) {

                    usernameForList.append(highscores[i]);
                    usernameForList.append(":");

                    winAsHumanForList.append(highscores[i + 1]);
                    winAsHumanForList.append(":");

                    winAsALienForList.append(highscores[i + 2]);
                    winAsALienForList.append(":");
                }

                String finalUsernameForList = usernameForList.toString();
                String finalWinAsHumanForList = winAsHumanForList.toString();
                String finalWinAsALienForList = winAsALienForList.toString();
                Platform.runLater(()->{
                    GUI.getInstance().highScoreListGUIController.updateHighScoreGUI(finalUsernameForList, finalWinAsHumanForList, finalWinAsALienForList);
                });
                break;

            case GAMELIST:
                String[] GamesList = toDo.split("§");
                System.out.println(Arrays.toString(GamesList));
                String[] OpenGames = GamesList[0].split(":");
                System.out.println(Arrays.toString(OpenGames));
                String[] RunningGames = GamesList[1].split(":");
                System.out.println(Arrays.toString(RunningGames));
                String[] FinishedGames = GamesList[2].split(":");
                Platform.runLater(()->{
                    GUI.getInstance().gameListGUIController.updateGameListGUI(OpenGames, RunningGames, FinishedGames);
                });
                break;

            case UPDATEMARKED:
                GUI.getInstance().markedPlayer = Integer.parseInt(toDo.substring(1,2));
                GUI.getInstance().marked = Boolean.parseBoolean(toDo.substring(2));
                break;

            default:
                break;
        }
    }
}
