package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import ch.unibas.dmi.dbis.cs108.AmongAlien.client.AmongAlienClient;
import ch.unibas.dmi.dbis.cs108.AmongAlien.client.ClientProtocolInterpret;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * VotingController is a Controller for the GUIVoting.fxml file
 * it controls the GUI components for the Voting GUI which get
 * updatet through the methods below
 *
 * @author Jonathan Burge (no longer teammate!)
 * @version 2021.05.22
 */
public class VotingController{

    @FXML
    private Button helpButton;
    @FXML
    private Button vote;
    @FXML
    private TextField chatInput;
    @FXML
    private Button sendButton;
    @FXML
    private TextArea chatBox;
    @FXML
    private ListView voteList;
    @FXML
    public TextArea chatWindow;
    @FXML
    public StackPane votingPane;
    @FXML
    public AnchorPane anchor;
    public int[] playerNumbers = new int[10];

    public AmongAlienClient client = GUI.getInstance().client;
    public ImageView[] players = new ImageView[10];
    public Image[] playerImage = new Image[10];

    /**
     * Starts with VotingGUI instance
     */
    public void initialize(){
        chatWindow.setDisable(true);
    }

    /**
     * Shows help
     */
    public void showHelp(){
        client.sendToServer("/HELP");
    }

    /**
     * Sends the vote to the Server.
     */
    public void vote(){ //gives names to the Server should give numbers
        int counter = 1;
        for (int i = 0; i < 10; i++) {
            if (GUI.getInstance().isActive[i]) {
                playerNumbers[counter] = i +1;
                counter++;
            }
        }
        System.out.println(voteList.getSelectionModel());
        if (GUI.getInstance().isActive[GUI.getInstance().playerNumber] && voteList.getSelectionModel() != null) {
            ClientProtocolInterpret.clientInterpret("/VOTEFOR " + playerNumbers[voteList.getSelectionModel().getSelectedIndex()],
                    client);
            if (voteList.getSelectionModel().getSelectedIndex()!= 0) {
                chatWindow.appendText("You voted for Player: " + playerNumbers[voteList.getSelectionModel().getSelectedIndex()] + ", number "
                        + voteList.getSelectionModel().getSelectedIndex() + "\n");
            } else if (voteList.getSelectionModel().getSelectedIndex() == 0) {
                chatWindow.appendText("You voted to skip");
            }
        }
    }

    /**
     * Shows the the list with players to vote for.
     */
    public void showVotingList(){
        voteList.getItems().clear();
        voteList.getItems().add("Skip the vote");
        System.out.println(voteList.getFixedCellSize());
        voteList.setFixedCellSize(51);
        System.out.println(votingPane);
        int numOfPic = 0;
        for (int i = 0; i < 10; i++) {
            if (GUI.getInstance().isActive[i]) {
                playerImage[numOfPic] = new Image("/Players/P"
                        + GUI.getInstance().playerPrefImg[i] + 12 + ".png");
                numOfPic++;
            }
        }
        Platform.runLater(()->{
            for (int i = 0; i < GUI.getInstance().numberOfPlayers; i++) {

            voteList.getItems().add(GUI.getInstance().lobbyMembers[i]);
            players[i] = new ImageView();
            players[i].setImage(playerImage[i]);
            players[i].setX(550);
            players[i].setY(35 + (i + 1) * 51);
            players[i].setFitHeight(43);
            players[i].setPreserveRatio(true);
            anchor.getChildren().add(players[i]);
            }
            for (int i = GUI.getInstance().numberOfPlayers; i < 11; i++) {
                voteList.getItems().add("");

            }
        });
    }

    /**
     * Sends Chat from Lobby Players (used for voting)
     */
    public void sendChat(){
        if(chatInput.getText().isEmpty() == false){
            if (chatInput.getText().startsWith("#")) {
                ClientProtocolInterpret.clientInterpret("/VOTEFOR " + chatInput.getText(), client);
                chatWindow.appendText("     " + chatInput.getText() + "\n");
                chatInput.clear();
            } else {
                chatWindow.appendText("     " + chatInput.getText() + "\n");
                String text = "/SETMSG " + chatInput.getText();
                ClientProtocolInterpret.clientInterpret(text, client);
                chatInput.clear();
            }
        }
    }

}
