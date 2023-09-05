package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import javafx.fxml.FXML;

/**
 * ConnectionLostController is a Controller for the
 * ConnectionLost.fxml file. It has only one Button
 *
 * @author Rayes Diyab
 * @version 2021.05.22
 */
public class ConnectionLostController {

    @FXML
    public javafx.scene.control.Button tryAgain;

    /**
     * reconnects the Player to the Server after losing connection
     * when clicking the Try Again button
     */
    public void reconnect() {
        GUI.getInstance().start(GUI.getInstance().mainStage);
    }
}
