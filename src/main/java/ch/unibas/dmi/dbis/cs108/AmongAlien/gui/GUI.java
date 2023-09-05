package ch.unibas.dmi.dbis.cs108.AmongAlien.gui;

import ch.unibas.dmi.dbis.cs108.AmongAlien.client.*;
import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.MapMatrix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * GUI is the main GUI Application
 *
 * This is where the magic happens
 * It has a lot of Methods and can do lots of stuff as described
 * in the method Java docs below
 *
 * @author Rayes Diyab, Hamza Zarah and Joel Erbsland
 * @version 2021.05.23
 */
public class GUI extends Application {

    private static GUI instance;
    public Stage mainStage;
    public LobbyController lobbyController;
    LoginController loginController;
    public ConnectionLostController connectionLostController;
    public HighScoreListGUIController highScoreListGUIController;
    public GameListGUIController gameListGUIController;
    public GameController gameController;
    public VotingController votingController;
    public WinLoseController winLoseController;
    public PlayerChooseController playerChooseController;
    public AmongAlienClient client;
    public int playerNumber;
    public String playerName;
    //protected ImageView mapWindow;
    private final MapImageCreator mapImageCreator = new MapImageCreator();
    public MapMatrix mapMatrix = new MapMatrix("hiddenInForrestMap", 60);
    public Image map;
    protected Image[] playerImage = new Image[10];
    protected Image[][] playerImg = new Image[10][5];
    protected ImageView[] playerView = new ImageView[10];
    protected Rectangle2D viewport;
    protected static final double WIDTH = 914, HEIGHT = 588;
    protected Scene scene;
    public boolean inGame;
    public String[] lobbyMembers = new String[10];
    public int numberOfPlayers;
    public boolean isAlien = false;
    public int[] playerPrefImg = new int[10];
    public boolean[] isActive= new boolean[10];
    public boolean introSeen  = false;
    public AnchorPane root;
    public boolean marked;
    public int markedPlayer;
    public int ejectedPlayer;

    /*----------------------------------------------------------------------*/
    /* Variables for the Movement.                                          */
    /*----------------------------------------------------------------------*/
    private int velocityX = 0;
    private int velocityY = 0;


    /**
     * A simply method to get the GUIs instance.
     *
     * @return the Instance of this GUI
     */
    public static GUI getInstance() {
        return instance;
    }

    /**
     * Starts the GUI.
     *
     * @param stage of the GUI.
     */
    @Override
    public void start(Stage stage){
        instance = this;
        mainStage = stage;
        Image icon = new Image("/logo.png");
        mainStage.getIcons().add(icon);
        Parameters params = getParameters();
        String[] args = params.getRaw().toArray(new String[3]);


        TimerTask movementTask = new TimerTask() {
            public void run() {
                tick();
            }
        };
        Timer timer = new Timer("Timer");
        timer.scheduleAtFixedRate(movementTask, 25L, 25L);


        /*--------------------------------------------------------------------------------*/
        /* Filling PlayerImg 3D Array with the Images of the specific Player in direction */
        /*--------------------------------------------------------------------------------*/
        for (int playerImgNum = 0; playerImgNum < 10; playerImgNum++) {
            for (int directionImgNum = 0; directionImgNum < 4; directionImgNum++) {
                playerImg[playerImgNum][directionImgNum] = new Image("/Players/P"
                        + playerImgNum
                        + (directionImgNum + 1)
                        + 2
                        + ".png");


            }
            playerImg[playerImgNum][4] =  new Image("/Players/PEmpty.png");
        }



        if (args[0].equals("")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUILogin.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else { //All parameters are there and will be set


            //Does not work because the is fault with the declaration of the GUI
            System.out.println(args[0] + ":" + args[1] + ":" + args[2]);
            System.out.println("Skipped Login Screen because Parameters have been given!");
            System.out.println("Verbinden mit Server.....");

            try {

            client = new AmongAlienClient(args[0], Integer.parseInt(args[1]), args[2]);
            client.execute();

            }catch(IOException e){
                System.out.println("Server not found!");
            }

            try {
                System.out.println("loading");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUILogin.fxml"));
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIintro.fxml"));

                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                GUI.getInstance().switchScene("/GUILobby.fxml");
                //GUI.getInstance().switchScene("/GUIintro.fxml");
                stage.show();
            }catch (Exception e){
                e.printStackTrace();
            }

            if (inGame) {
                //gameController.pane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                //    event.consume();
                //});
                gameController.markButton.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    event.consume();
                    // handleKeys(event);
                });
                gameController.pane.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
                    event.consume();
                });

                gameController.gameWindow.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
                    event.consume();
                });
            }
        }
        stage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {

            /**
             * Knows all keys that can be pressed to interact with the game and
             * is controlling the needed effect.
             */
            @Override
            public void handle(KeyEvent event){
                if (inGame) {
                    switch (event.getCode()) {
                        case W:
                            setVelocityY(-3);
                            break;
                        case A:
                            setVelocityX(-3);
                            break;
                        case D:
                            setVelocityX(3);
                            break;
                        case S:
                            setVelocityY(3);
                            break;
                        case T:
                            int xi = GUI.getInstance().client.allPlayers2[0][playerNumber]
                                    / GUI.getInstance().mapMatrix.getPixelsForEachTeil();
                            int yi = GUI.getInstance().client.allPlayers2[1][playerNumber]
                                    / GUI.getInstance().mapMatrix.getPixelsForEachTeil();
                            client.sendToServer("/ADDMAT " + xi + ":" + yi + ":2");
                            //System.out.println("Position is " + client.allPlayers2[0][playerNumber] + "|"
                            //+ client.allPlayers2[1][playerNumber] + "  made to " + xi + "|" + yi);
                            break;
                        case E:
                            int taskXi = GUI.getInstance().client.allPlayers2[0][playerNumber]
                                    / GUI.getInstance().mapMatrix.getPixelsForEachTeil();
                            int taskYi = GUI.getInstance().client.allPlayers2[1][playerNumber]
                                    / GUI.getInstance().mapMatrix.getPixelsForEachTeil();
                            client.sendToServer("/DOTASK " + taskXi + ":" + taskYi);
                            //System.out.println("Position is " + client.allPlayers2[0][playerNumber] + "|"
                            //+ client.allPlayers2[1][playerNumber] + "  made to " + taskXi + "|" + taskYi);
                            break;
                    }
                }
            }
        });

        stage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {

            /**
             * Knows all keys that can be pressed to interact with the game and
             * is controlling the needed effect.
             */
            @Override
            public void handle(KeyEvent event){
                if (inGame) {
                    switch (event.getCode()) {
                        case W:
                            //System.out.println("released W");
                            setVelocityY(0);
                            break;
                        case A:
                            //System.out.println("released A");
                            setVelocityX(0);
                            break;
                        case D:
                            //System.out.println("released D");
                            setVelocityX(0);
                            break;
                        case S:
                            //System.out.println("released S");
                            setVelocityY(0);
                            break;
                    }
                }
            }
        });
        stage.setOnCloseRequest(event -> lobbyController.quitLobbyGUI());
    }


    /**
     * Switches Scenes depending on the requested Scene
     *
     * @param filename is the requested Scene as String
     * @throws IOException if there is a Input or output fault.
     */
    public void switchScene(String filename) throws IOException {
        root = null;
        URL url;
        if(filename.equals("/GUIVoted.fxml")){
            url = getClass().getResource("/GUILobby.fxml");
        } else{
            url = getClass().getResource(filename);
        }
        System.out.println("switching now1...");
        FXMLLoader loader = new FXMLLoader(url);
        System.out.println("switching now2...");
        loader.setLocation(url);
        //mainStage.getScene().setRoot(loader.load());
        root = loader.load();
        //mainStage.hide();
        System.out.println("switching now3...");


        switch (filename) {
            case "/GUILogin.fxml":
                loginController = loader.getController();
                break;

            case "/GUILobby.fxml":
                if (!introSeen) {
                    try {
                        MediaPlayer player = VideoPlayer.getTrailer();
                        MediaView mediaView = new MediaView();
                        mediaView.setFitHeight(720);
                        mediaView.setFitWidth(1280);
                        mediaView.setMediaPlayer(player);
                        root.getChildren().add(mediaView);
                        Timeline t1 = new Timeline(new KeyFrame(Duration.seconds(29)));
                        t1.setCycleCount(1);
                        t1.setOnFinished(event -> {
                            player.stop();
                            root.getChildren().remove(mediaView);
                        });
                        t1.play();
                        player.play();
                        introSeen = true;
                    } catch (URISyntaxException e345) {
                        e345.printStackTrace();
                    }
                }
                lobbyController = loader.getController();
                sendMessage("/UPDATELOBBYGUI");
                //sendMessage("/SETNAME "+ client.userName);
                mainStage.setTitle(playerName);
                break;

            case "/ConnectionLost.fxml":
                connectionLostController = loader.getController();
                break;

            case "/HighScoreListGUI.fxml":
                highScoreListGUIController = loader.getController();
                break;

            case "/GameListGUI.fxml":
                gameListGUIController = loader.getController();
                break;

            case "/GUIGame.fxml":
                gameController = loader.getController();
                break;

            case "/GUIVoting.fxml":
                if(marked) {
                    try {
                        MediaPlayer player = SoakUp.getTrailer();
                        MediaView mediaView2 = new MediaView();
                        mediaView2.setFitHeight(720);
                        mediaView2.setFitWidth(1280);
                        mediaView2.setMediaPlayer(player);
                        root.getChildren().add(mediaView2);
                        System.out.println("URL = " + url);
                        Image image = new Image(String.valueOf(getClass().getResource("/Players/P" + playerPrefImg[markedPlayer]
                                + "12.png")));
                        ImageView imageView = new ImageView(image);
                        imageView.setLayoutY(570);
                        imageView.setLayoutX(570);
                        imageView.setFitHeight(70);
                        imageView.setFitWidth(70);
                        root.getChildren().add(imageView);
                        Timeline t1 = new Timeline(new KeyFrame(Duration.seconds(12)));
                        t1.setCycleCount(1);
                        t1.setOnFinished(event -> {
                            player.stop();
                            root.getChildren().remove(mediaView2);
                            root.getChildren().remove(imageView);
                            votingController.showVotingList();
                        });
                        t1.play();
                        player.play();
                    } catch (URISyntaxException e345) {
                        e345.printStackTrace();
                    }
                }
                votingController = loader.getController();
                if (!marked) {
                    votingController.showVotingList();
                }
                break;

            case "/GUIVoted.fxml":
                    try {
                        MediaPlayer player = SoakUp.getTrailer();
                        MediaView mediaView2 = new MediaView();
                        mediaView2.setFitHeight(720);
                        mediaView2.setFitWidth(1280);
                        mediaView2.setMediaPlayer(player);
                        root.getChildren().add(mediaView2);
                        System.out.println("URL = " + url);
                        Image image = new Image(String.valueOf(getClass().getResource("/Players/P" + playerPrefImg[markedPlayer]
                                + "12.png")));
                        ImageView imageView = new ImageView(image);
                        imageView.setLayoutY(570);
                        imageView.setLayoutX(570);
                        imageView.setFitHeight(70);
                        imageView.setFitWidth(70);
                        root.getChildren().add(imageView);
                        Timeline t1 = new Timeline(new KeyFrame(Duration.seconds(12)));
                        t1.setCycleCount(1);
                        t1.setOnFinished(event -> {
                            player.stop();
                            root.getChildren().remove(mediaView2);
                            root.getChildren().remove(imageView);
                        });
                        t1.play();
                        player.play();
                    } catch (URISyntaxException e345) {
                        e345.printStackTrace();
                    }
                lobbyController = loader.getController();
                sendMessage("/UPDATELOBBYGUI");
                //sendMessage("/SETNAME "+ client.userName);
                mainStage.setTitle(playerName);
                break;

            case "/GUIWinLose.fxml":
                GameController.safety = 0;
                GameController.hunger = 0;
                GameController.materials = new int[10];
                winLoseController = loader.getController();
                break;

            case "/GUIPlayerChoose.fxml":
                playerChooseController = loader.getController();
                break;

            default:
                break;
        }
        mainStage.getScene().setRoot(root);
    }

    /**
     * Runs the won State in the WinLose Controller
     */
    public void gameIsWon(){
        try {
            switchScene("GUIWinLose.fxml");
        }catch (IOException e){
            System.out.println("Scene not found WINLOSE");
        }
        Platform.runLater(()->{
        //winLoseController.showVictory();
        });
    }

    /**
     * Runs the defeat State in the WinLose Controller
     */
    public void gameIsLost(){
        try {
            switchScene("GUIWinLose.fxml");
        }catch (IOException e){
            System.out.println("Scene not found WINLOSE");
        }
        Platform.runLater(()->{
        });
    }

    /**
     * The chatMessage method appends a given Text to the GUI's text box.
     *
     * @param message to send on the GUI's TextBox
     */
    public void chatMessage(String message) {
        if (lobbyController != null) {
            lobbyController.chatWindow.appendText(message + "\n");
        }
        if (gameController != null) {
            gameController.chatWindow.appendText(message + "\n");
        }
        if (votingController != null) {
            votingController.chatWindow.appendText(message + "\n");
        }
    }

    /**
     * Sends Message to Client.
     *
     * @param message the Message to send.
     */
    public void sendMessage(String message) {
        client.sendToServer(message);
    }

    /**
     * Updates all players textfield in the Lobby GUI.
     *
     * @param names String with all connected clients
     */
    public void updateAllPlayerList(String names){
        Platform.runLater(()->{
            GUI.getInstance().lobbyController.updateAllPlayerList(names);
        });
    }

    /**
     * Main method that Starts GUI.
     *
     * @param args as the input parameters witch can be (server [port]) or (client [hostadress] [port] [username]).
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Creates New MapMatrix and saves it in map.
     */
    public void createNewMap() {
        mapImageCreator.createMapImage(mapMatrix);
        gameController.gameWindow.setImage(map);
    }


    /**
     * This Method will create a mapImage with MapImageCreator and save it as a png
     * for the ImageView to grab.
     */
    public void startMap() {
        for (int i = 0; i < 10; i++) {
            this.client.allPlayers2[0][i] = 1920;
            this.client.allPlayers2[1][i] = 1920;
        }
        //mapMatrix = new MapMatrix("hiddenInForrestMap", 60);
        setVelocityX(0);
        setVelocityY(0);
        createNewMap();
        //System.out.println("will map laden");
        //File file = new File("/images/mapImage.png");
        //map = new Image(file.toURI().toString());
        System.out.println(map);
        //map = new Image("mapImage.png");
        //System.out.println("hat map geladen");


        //for (int i = 0; i < 10; i++) {
        //    playerImage[i] = new Image("Player" + (1 + i) +".png");
        //}
        //System.out.println("hat Player geladen");
        gameController.gameWindow.setImage(map);

        //System.out.println(playerNumber + "|" + client.playerNumber);
        //System.out.println(client.allPlayers2[0][playerNumber]);
        //System.out.println(client.allPlayers2[1][playerNumber]);
        viewport = new Rectangle2D(client.allPlayers2[0][playerNumber] - WIDTH / 2,
                client.allPlayers2[1][playerNumber] - HEIGHT / 2 , WIDTH, HEIGHT);
        gameController.gameWindow.setViewport(viewport);
        inGame = true;

        /*----------------------------------------------------------------------*/
        /* Methods for the Movement.                                            */
        /*----------------------------------------------------------------------*/
        //ClientGameLoop gameLoop = new ClientGameLoop();
        //gameLoop.start();
        /*----------------------------------------------------------------------*/
        /* No longer for Movement.                                              */
        /*----------------------------------------------------------------------*/

        Platform.runLater(()->{

            for (int i = 0; i < 10; i++) {
                if (isActive[i]) {
                    playerView[i] = new ImageView(playerImg[playerPrefImg[i]][0]);
                    playerView[i].setX(client.allPlayers2[0][i] - (client.allPlayers2[0][playerNumber])
                            + WIDTH / 2.0 + 8);
                    playerView[i].setY(client.allPlayers2[1][i] - (client.allPlayers2[1][playerNumber])
                            + HEIGHT / 2.0 + 7);
                    playerView[i].setVisible(true);
                    gameController.pane.getChildren().add(playerView[i]);

                    /*for (int i = 0; i < numberOfPlayers; i++) {
                        playerView[i] = new ImageView(playerImage[i]);
                        playerView[i].setX(client.allPlayers2[0][i] - (client.allPlayers2[0][playerNumber])
                                + WIDTH/2.0 + 8);
                        playerView[i].setY(client.allPlayers2[1][i] - (client.allPlayers2[1][playerNumber])
                                + HEIGHT/2.0 + 7);
                        playerView[i].setVisible(true);
                        gameController.pane.getChildren().add(playerView[i]);

                     */
                }
            }

        });

        updateMapWindow();
    }


    /**
     * Updates the mapWindow so that the Player is in the center if possible.
     */
    public void updateMapWindow() {
        double xPos = client.allPlayers2[0][playerNumber] - WIDTH/2;
        double yPos = client.allPlayers2[1][playerNumber] - HEIGHT/2;
        //gameController.gameWindow2.setViewport(new Rectangle2D(xPos, yPos, WIDTH - 500, HEIGHT - 300));
        gameController.gameWindow.setViewport(new Rectangle2D(xPos, yPos, WIDTH, HEIGHT));
        updatePlayers();

        // would check visibility
        //if (xPos <= GUI.getInstance().map.getWidth() - GUI.getInstance().scene.getWidth() / 2.0
        //       && yPos <= GUI.getInstance().map.getHeight() - GUI.getInstance().scene.getHeight() / 2.0) { //checks if position is showable
        //viewport = new Rectangle2D(xPos - WIDTH/2.0, yPos - HEIGHT/ 2.0, WIDTH, HEIGHT);
    }

    /**
     * Updates players position on the map.
     */
    public void updatePlayers() {
        Platform.runLater(()->{
            for (int i = 0; i < 10; i++) {
                if (isActive[i]) {
                    //System.out.println(i + " " + playerPrefImg[i] + client.allPlayers2[2][i]);
                    //System.out.println("me is " + playerNumber);
                    if (Math.abs(client.allPlayers2[0][i] - (client.allPlayers2[0][playerNumber])) < WIDTH / 2
                            && Math.abs(client.allPlayers2[1][i] - (client.allPlayers2[1][playerNumber])) < HEIGHT / 2) {
                        //Player is in sight.
                        //System.out.println(playerView[i]);
                        //System.out.println(client.allPlayers2[0][i]);
                        //System.out.println(client.allPlayers2[0][playerNumber]);
                        //System.out.println(WIDTH / 2);
                        //System.out.println(playerNumber);
                        //System.out.println(client.allPlayers2[1][i]);
                        //System.out.println(client.allPlayers2[1][playerNumber]);
                        playerView[i].setX(client.allPlayers2[0][i] - client.allPlayers2[0][playerNumber]
                                + WIDTH / 2 + 8 - 22);
                        playerView[i].setY(client.allPlayers2[1][i] - client.allPlayers2[1][playerNumber]
                                + HEIGHT / 2 + 7 - 22);
                        playerView[i].setImage(playerImg[playerPrefImg[i]][client.allPlayers2[2][i]]);
                        playerView[i].setVisible(true);
                    } else {
                        playerView[i].setVisible(false);
                    }
                }
            }
        });
    }

    /**
     * Updates the playerlist for the GamePlayers
     *
     * @param players List of Players in Game to update.
     */
    public void updateGamePlayers(String players){
        System.out.println("IN GUI PRINT GAMEPLAYERS ="+players);
        String[] split = players.split(":");
        //String[] reversesplit = reverse(split, split.length);
        for(int i = 0; i < split.length; i++){
            lobbyMembers[i] = split[i];
        }
    }

    /**
     * A Method to set the movement's speed.
     *
     * @param velocityX the speed in x-Direction.
     */
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * A Method to set the movement's speed.
     *
     * @param velocityY the speed in y-Direction.
     */
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * The tick() Method is used for the movement of the Player. It uses the velocity
     * of the x and y direction and appends it to the /SETPOS command, so that the
     * server gets a position change request.
     */
    public void tick() {
        if(!this.inGame) {
            return;
        }
        int dir = 0;
        if(velocityX < 0) {
            dir = 1;
        } else if(velocityX > 0) {
            dir = 2;
        }
        if(velocityY < 0) {
            dir = 3;
        } else if(velocityY > 0) {
            dir = 0;
        }
        if(velocityX == 0 && velocityY == 0) {
            return;
        }
        client.sendToServer("/SETPOS " + (client.allPlayers2[0][playerNumber] + velocityX)
                + ":" + (client.allPlayers2[1][playerNumber] + velocityY) + ":" + dir);
    }


    /**
     * Updates Lobby Lists
     *
     * @param Lobbies a String of lobbies.
     */
    public void updateLobbyLists(String Lobbies){
        if(Lobbies == null){
            System.out.println("Keine Lobbies!");
        }else{
            Platform.runLater(()->{
                System.out.println("GUI HAT LOBBIES BEKOMMEN :" + Lobbies);
            GUI.getInstance().lobbyController.updateLobbyLists(Lobbies);
            });
        }
    }

    /**
     * Updates the playerlist for the GamePlayers
     *
     * @param players List of Players in Game to update.
     */
    public void updateLobbyPlayers(String players){
        Platform.runLater(()->{
            GUI.getInstance().lobbyController.updatePlayerLists(players);
        });
    }

    /**
     * Update LobbyName Label in LobbyController
     *
     * @param LobbyName the new LobbyName.
     */
    public void updateLobbyName(String LobbyName){
        Platform.runLater(()->{
            GUI.getInstance().lobbyController.updateLobbyName(LobbyName);
        });
    }

    /**
     * Updates the Username found in the LobbyController
     *
     * @param Username the new Username.
     */
    public void updateUserName(String Username){
        Platform.runLater(()->{
            GUI.getInstance().lobbyController.updateUserName(Username);
        });
    }

    /**
     * Updates the list of the left lobbies.
     */
    public void updateLeftLobby() {
        Platform.runLater(()->{
            GUI.getInstance().lobbyController.updateLeftLobby();
        });
    }

    /**
     * Client switches to the LobbyGUI and needs to update the
     * Username field on that GUI, so he updates the name himself
     * because he already received it from the Server before
     */
    public void goToLobby() {
        ClientProtocolInterpret.clientInterpret("/UPDATENAME " + client.userName, client);
    }
}
