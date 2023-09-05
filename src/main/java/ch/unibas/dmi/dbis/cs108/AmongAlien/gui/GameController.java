package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import ch.unibas.dmi.dbis.cs108.AmongAlien.client.AmongAlienClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * GameController is a Controller for the GameGUI.fxml file
 *
 * it controls the GUI components for the Game GUI which get
 * updatet through the methods below
 *
 * @author Joël Erbsland, Rayes Diyab, Hamza Zarah and Jonahtan Burge
 * @version 2021.05.01
 */
public class GameController {

    @FXML
    private Button helpButton;
    @FXML
    public Button markButton;
    @FXML
    private TextField chatInput;
    @FXML
    private Button sendButton;
    @FXML
    public TextArea chatWindow;
    @FXML
    public ImageView gameWindow;
    @FXML
    protected StackPane stack;
    @FXML
    protected ImageView playerWindow;
    @FXML
    public AnchorPane pane;
    @FXML
    public Label holzInLabel;
    @FXML
    public Label erdbeerInLabel;
    @FXML
    public Label berryBushInLabel;
    @FXML
    public Label fischInLabel;
    @FXML
    public Label lehmInLabel;
    @FXML
    public Label steinInLabel;
    @FXML
    private ProgressBar dorfSicherheitBar;
    @FXML
    private ProgressBar hungerBar;
    @FXML
    public Button leaveGameButton;
    @FXML
    public Label hungerText;

    private GUI gui = GUI.getInstance();
    public ImageView player1;
    public AmongAlienClient client = GUI.getInstance().client;
    public static int[] materials = new int[100];
    public static double safety;
    public static double hunger;

    /**
     * Starts with the GAMEGUI Instance
     */
    @FXML
    public void initialize(){
        leaveGameButton.setDisable(true);
        leaveGameButton.setVisible(false);
        updateMaterials();
        chatWindow.setEditable(false);
        chatWindow.setBlendMode(BlendMode.OVERLAY);
        Platform.runLater(()->{
            hungerBar.setStyle("-fx-accent: green");
            dorfSicherheitBar.setStyle("-fx-accent: red");
            if (GUI.getInstance().isAlien) {
                markButton.setText("MARK");
            } else {
                //markButton.setDisable(true);
            }
            markButton.requestFocus();
            changeHunger(hunger);
            System.out.println(hunger);
            changeDorfsicherheit(safety);
            System.out.println(safety);
        });
    }

    /**
     * Shows the help in chat
     */
    public void showHelp(){
        client.sendToServer("/HELP");
    }

    /**
     * Updates the Materials in the inGame GUI
     */
    public void updateMaterials(){
        int stone = 11;
        int wood = 21;
        int clay = 30;
        int strawberry = 31;
        int berrybush = 32;
        int fish = 34;
        Platform.runLater(()->{
            holzInLabel.setText("" + materials[wood]);
            erdbeerInLabel.setText("" + materials[strawberry]);
            berryBushInLabel.setText("" + materials[berrybush]);
            fischInLabel.setText("" + materials[fish]);
            lehmInLabel.setText("" + materials[clay]);
            steinInLabel.setText("" + materials[stone]);
        });
    }

    /**
     * adds Matterial to the specified player
     *
     * @param textureID of the Material to add.
     */
    public void addMat(int textureID) {
        materials[textureID]++;
    }

    /**
     * Sends the players Vote
     */
    @FXML
    public void markPlayer(){
        if (gui.isAlien) {
            client.sendToServer("/MARK ");
        }
    }

    /**
     * Sends the Chat from the ChatInput
     */
    public void sendChat(){
        if (!chatInput.getText().equals("")) {
            String chat = chatInput.getText();
            if (chat.charAt(0) != '/'){
                chatWindow.appendText("     " + chat + "\n");
            }
            GUI.getInstance().sendMessage(chat);
            System.out.println("In GameController ="+chat);
            chatInput.clear();
        }
        markButton.requestFocus();
    }

    /**
     * Can change "Dorfsicherheit" in both ways
     *
     * @param change double Value of how safe the Village is.
     */
    public void changeDorfsicherheit(double change){
        Platform.runLater(()->{
            dorfSicherheitBar.setProgress(change);
            if (change < 0.3) {
            dorfSicherheitBar.setStyle("-fx-accent: red");
            } else if (change < 0.65) {
                dorfSicherheitBar.setStyle("-fx-accent: orange");
            } else {
                dorfSicherheitBar.setStyle("-fx-accent: green");
            }
        });
    }

    /**
     * Can change "Hunger" in both ways
     *
     * @param change double value of how many Hunger a Player has.
     */
    public void changeHunger(double change){
        Platform.runLater(()->{
            hungerBar.setProgress(change);
            if (change < 0.3) {
                hungerBar.setStyle("-fx-accent: red");
            } else if (change < 0.60) {
                hungerBar.setStyle("-fx-accent: orange");
            } else {
                hungerBar.setStyle("-fx-accent: green");
            }
        });
    }

    /**
     * Changes the chosen Player to a ghost
     *
     * @param isGhost the chosen player
     */
    public void setGhost(boolean isGhost) {
        if (isGhost) {
            hungerBar.setVisible(false);
            hungerText.setVisible(false);
            leaveGameButton.setVisible(true);
            leaveGameButton.setDisable(false);
        }
    }
}
