package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;

/**
 * HighScoreListGUIController is a Controller for the HighScoreListGUI.fxml
 *
 * it controls the GUI components for High Score List GUI
 * updatet through the methods below
 *
 * @author Rayes Diyab
 * @version 2021.05.23
 */
public class HighScoreListGUIController{

    @FXML
    private Button lobbyButton;
    @FXML
    private ListView<String> playerName;
    @FXML
    private ListView<String> humanWins;
    @FXML
    private ListView<String> alienWins;

    /**
     * Updates the HighScoreList started from ServerProtocll Interpret
     *
     * @param usernameForList Ussernames as String of all HighScore Players
     * @param winAsHumanForList wins as Human as String of all HighScore Players
     * @param winAsALienForList wins as Alien as String of all HighScore Players
     */
    public void updateHighScoreGUI(String usernameForList, String winAsHumanForList, String winAsALienForList){
        playerName.getItems().clear();
        humanWins.getItems().clear();
        alienWins.getItems().clear();

        String[] splitUsernameForList = usernameForList.split(":");
        String[] splitWinAsHumanForList = winAsHumanForList.split(":");
        String[] splitWinAsALienForList = winAsALienForList.split(":");

        playerName.getItems().addAll(splitUsernameForList);
        humanWins.getItems().addAll(splitWinAsHumanForList);
        alienWins.getItems().addAll(splitWinAsALienForList);
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
