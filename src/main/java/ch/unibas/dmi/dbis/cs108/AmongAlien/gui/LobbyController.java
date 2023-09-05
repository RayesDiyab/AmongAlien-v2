package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import ch.unibas.dmi.dbis.cs108.AmongAlien.client.AmongAlienClient;
import ch.unibas.dmi.dbis.cs108.AmongAlien.server.ClientThread;
import ch.unibas.dmi.dbis.cs108.AmongAlien.server.Server;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

/**
 * LobbyController is a Controller for the GUILobby.fxml
 * It controls the GUI elements of the below stated GUI
 * elements
 *
 * @author Rayes Diyab
 * @version 2021.05.22
 */
public class LobbyController {

    @FXML
    private ListView<String> lobbyList;
    @FXML
    private Button joinButton;
    @FXML
    private Button leaveButton;
    @FXML
    private TextField newLobby;
    @FXML
    private Button createLobby;
    @FXML
    private Label nameLabel;
    @FXML
    private ListView<String> playerList;
    @FXML
    private TextField newNickname;
    @FXML
    private Button changeName;
    @FXML
    private Button startLobby;
    @FXML
    public TextArea chatWindow;
    @FXML
    private TextField chatInput;
    @FXML
    private Button sendButton;
    @FXML
    private Label lobbyName;
    @FXML
    private ListView<String> totalPlayerList;
    @FXML
    private Button highScoreListButon;

    private AmongAlienClient client;
    private ClientThread socket;
    //private GameServer gameServer;
    private Server server;



    /**
     * Starts with LobbyGUI instance
     */
    public void initialize(){
        chatWindow.setDisable(true);
        GUI.getInstance().sendMessage("/UPDATEPLAYERS");
        GUI.getInstance().sendMessage("/UPDATELOBBYLIST");
    }

    /**
     * Join a Lobby that gets the LobbyName from the chatInput Textfield.
     */
    public void joinLobby(){
        String LobbyName = lobbyList.getSelectionModel().getSelectedItem();
        if(!LobbyName.equals("")){
            GUI.getInstance().sendMessage("/JOINLOBBY " + LobbyName);
            chatWindow.clear();
        }
    }

    /**
     * Handles what happens if the leavelobby button was pressed. The player will send to
     * the server a /LEAVELOBBY and the chat will be cleared.
     */
    public void leaveLobby(){
        GUI.getInstance().sendMessage("/LEAVELOBBY");
        chatWindow.clear();
    }

    /**
     * Sends to the General Lobby Chat and it gets the chat message from the chatInput Textfield.
     */
    public void sendChat(){
        if (!chatInput.getText().equals("")) {
            String chat = chatInput.getText();
            if (chat.charAt(0) != '/'){
                chatWindow.appendText("     " + chat + "\n");
            }
            GUI.getInstance().sendMessage(chat);
            System.out.println("In LobbyController ="+chat);
            chatInput.clear();
        }
    }


    /**
     * Updates the gamelist. Therefor it sends to the server a /GETGAMELIST to get
     * the actual one.
     */
    public void updateGameList() {
        GUI.getInstance().sendMessage("/GETGAMELIST");
        try {
            GUI.getInstance().switchScene("/GameListGUI.fxml");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the Highscorelist. Therefor it asks the server for the actual one.
     */
    public void updateHighScoreList() {
        GUI.getInstance().sendMessage("/GETSCORE");
        try {
            GUI.getInstance().switchScene("/HighScoreListGUI.fxml");
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Updates Lobby Lists by getting the Lists from the Server as a String
     *
     * @param lobbies the lobby
     */
    public void updateLobbyLists(String lobbies) {
        lobbyList.getItems().clear();
        String[] split = lobbies.split(":");
        lobbyList.getItems().addAll(split);
    }


    /**
     * Updates Total Players List by getting the Lists from the Server as a String
     *
     * @param names the Players
     */
    public void updateAllPlayerList(String names){
        totalPlayerList.getItems().clear();
        String[] split = names.split(":");
        totalPlayerList.getItems().addAll(split);
        }

    /**
     * Updates Lobby Name of the current Lobby the player is in
     *
     * @param LobbyName the lobby's name
     */
    public void updateLobbyName(String LobbyName){
        lobbyName.setText(LobbyName);
        }

    /**
     * Updates Player Lists by receiving the List of current PLayers in Lobby from Server
     *
     * @param players to uptate the PlayersList.
     */
    public void updatePlayerLists(String players) {
        playerList.getItems().clear();
        String[] split = players.split(":");
        System.out.println(split);
        playerList.getItems().addAll(split);
    }

    /**
     * Leaves the game by sending /QUITLOBBYGUI to the Server
     */
    public void quitLobbyGUI(){
        GUI.getInstance().sendMessage("/QUITLOBBYGUI");
    }

    /**
     * Creates anew Lobby and gets Lobby name from the Textfield called newLobby.
     */
    public void createLobby(){
        if (!newLobby.getText().equals("")) {
            //later needs to check if lobby exists allready.
            String Lobby = "/NEWLOBBY " + newLobby.getText();
            GUI.getInstance().sendMessage(Lobby);
            Lobby = "/UPDATELOBBYNAMES " + newLobby.getText();
            GUI.getInstance().sendMessage(Lobby);
            newLobby.clear();
            chatWindow.clear();
        }
    }

    /**
     * Updates Usernames by sending to the ServerClientProtocol
     *
     * @param Username the username to update the old one into.
     */
    public void updateUserName(String Username){
        nameLabel.setText(Username);
    }


    /**
     * Starts a new Game with the chosen Lobby
     */
    public void startLobby(){
        GUI.getInstance().sendMessage("/STARTGAME  ");
    }

    /**
     * Updates the left lobbies list.
     */
    public void updateLeftLobby() {
        playerList.getItems().clear();
    }
}
