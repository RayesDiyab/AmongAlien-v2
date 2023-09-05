package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * WinLoseController is a Controller for the GUIWinLose.fxml file
 * it controls the GUI components for the Win/Lose GUI which get
 * updatet through the methods below
 *
 * @author Rayes Diyab
 * @version 2021.05.22
 */
public class WinLoseController {

    @FXML
    private Label Defeat;
    @FXML
    private Label Victory;
    @FXML
    private Button EndGame;
    @FXML
    public Button lobbyButton;

    /**
     * this ends the game when clicking the quit button
     */
    public void leaveGame(){
        GUI.getInstance().sendMessage("/QUITGAMEGUI");
    }

    /**
     * This means go back to Lobby.
     */
    @FXML
    public void goToLobby(){
        try {
            GUI.getInstance().switchScene("/GUILobby.fxml");
        } catch (Exception e){
            e.printStackTrace();
        }
        GUI.getInstance().goToLobby();
    }

    /**
     * This shows the victory text and removes the defeat text
     */
    public void showVictory(){
        Defeat.setText("");
    }

    /**
     * This shows the defeat text and removes the victory text
     */
    public void showDefeat(){
        Victory.setText("");
    }

}
