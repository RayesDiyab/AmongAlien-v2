package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.Arrays;

/**
 * HighScoreListGUIController is a Controller for the HighScoreListGUI.fxml
 *
 * it controls the GUI components for High Score List GUI
 * updatet through the methods below
 *
 * @author Rayes Diyab
 * @version 2021.05.23
 */
public class GameListGUIController {

    @FXML
    private Button lobbyButton;
    @FXML
    private ListView<String> openGames;
    @FXML
    private ListView<String> ongoingGames;
    @FXML
    private ListView<String> finishedGames;

    /**
     * Updates the HighScoreList started from ServerProtocll Interpret
     *
     * @param finished the finished lobbies
     * @param ongoing the ongoing lobbies
     * @param open the opened lobbies
     */
    public void updateGameListGUI(String[] open, String[] ongoing, String[] finished) {
        int openL = open.length;
        int ongoingL = ongoing.length;

        String[] openCorrect = new String[openL-1];
        for (int i = 0; i < openL-1; i++){
            openCorrect[i] = open[i];
        }

        String[] ongoingCorrect = new String[ongoingL-1];
        for (int i = 0; i < ongoingL-1; i++){
            ongoingCorrect[i] = ongoing[i];
        }


        openGames.getItems().clear();
        ongoingGames.getItems().clear();
        finishedGames.getItems().clear();
        System.out.println("IM CONTROLLER ongoing"+Arrays.toString(ongoingCorrect));
        //System.out.println("IM CONTROLLER"+open[openL-1]);
        openGames.getItems().addAll(openCorrect);
        ongoingGames.getItems().addAll(ongoingCorrect);
        finishedGames.getItems().addAll(finished);
    }

    /**
     * When clicking the backToLobby it loads the lobby GUI
     */
    public void backToLobby() {
        Platform.runLater(()->{
        try {
            GUI.getInstance().switchScene("/GUILobby.fxml");
        }catch(IOException e){
            e.printStackTrace();
        }
        });
        GUI.getInstance().goToLobby();
    }
}
