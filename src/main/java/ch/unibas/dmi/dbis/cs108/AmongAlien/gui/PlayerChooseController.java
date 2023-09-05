package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * PlayerChooseController is a Controller for the
 * GUIPlayerChoose.fxml file which controls the
 * player choose in the GUI when starting a game
 *
 * @author Jonahtan Burge (no longer teammate!)
 * @version 2021.05.22
 */
public class PlayerChooseController {

    @FXML
    public Label humanText;

    @FXML
    public Label alienText;

    @FXML
    public ImageView player1;

    @FXML
    public ImageView player6;

    @FXML
    public ImageView player3;

    @FXML
    public ImageView player8;

    @FXML
    public ImageView player2;

    @FXML
    public ImageView player7;

    @FXML
    public ImageView player4;

    @FXML
    public ImageView player9;

    @FXML
    public ImageView player5;

    @FXML
    public ImageView player10;

    @FXML
    public ImageView definitivPlayer;

    public ImageView[] players = new ImageView[10];

    /**
     * Sends to server that this client has voted for player 1.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote1(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 0);
    }

    /**
     * Sends to server that this client has voted to skip.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote10(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 9);
    }

    /**
     * Sends to server that this client has voted for player 2.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote2(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 1);
    }

    /**
     * Sends to server that this client has voted for player 2.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote3(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 2);
    }

    /**
     * Sends to server that this client has voted for player 4.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote4(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 3);
    }

    /**
     * Sends to server that this client has voted for player 5.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote5(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 4);
    }

    /**
     * Sends to server that this client has voted for player 6.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote6(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 5);
    }

    /**
     * Sends to server that this client has voted for player 7.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote7(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 6);
    }

    /**
     * Sends to server that this client has voted for player 8.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote8(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 7);
    }

    /**
     * Sends to server that this client has voted for player 9.
     *
     * @param event for the mouseclick
     */
    @FXML
    void vote9(MouseEvent event) {
        GUI.getInstance().sendMessage("/SETPLAYERIMAGE " + GUI.getInstance().playerNumber + 8);
    }

    /**
     * This method loads the player names on Startup
     */
    @FXML
    public void initialize() {
        players[0] = player1;
        players[1] = player2;
        players[2] = player3;
        players[3] = player4;
        players[4] = player5;
        players[5] = player6;
        players[6] = player7;
        players[7] = player8;
        players[8] = player9;
        players[9] = player10;
    }

    /**
     * Changes the text if the player is an Alien
     * or a Human
     *
     * @param isAlien Is true if the player is a ALien
     *                false if a Human
     */
    public void setText(boolean isAlien) {
        System.out.println(isAlien);
        if (isAlien) {
            //humanText.setText("");
            humanText.setVisible(false);
        } else {
            //alienText.setText("");
            alienText.setVisible(false);
        }
    }

    /**
     * diasbles Player picture after being voted by another player
     *
     * @param player chosen player
     */
    public void disablePlayer(int player) {
        players[player].isDisable();
        players[player].setVisible(false);
    }

    /**
     * Uses the chosen player and expands it on the screen
     *
     * @param def chosen player number
     */
    public void setDefinitivPlayer(int def) {
        definitivPlayer.setLayoutY(301);
        definitivPlayer.setImage(players[def].getImage());
        for (int i = 0; i < 10; i++) {
            disablePlayer(i);
        }
    }

}
