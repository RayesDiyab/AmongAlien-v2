package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.MapTeils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * AmongAlienClient is connection to the AmongAlienServer
 *
 * feelds -String hostname, is the Servers IP-Address oder URL
 *        -int port, is the Port listening.
 *        -String userName, is the Clients changable Nickname
 *
 * @author Joel Erbsland, Rayes Diyab, Hamza Zarah and Jonahtan Burge
 * @version 2021.05.10
 */
public class AmongAlienClient {

    public int[][] allPlayers2 = new int[3][10];
    private Image map;
    private ImageView mapWindow;
    private Rectangle2D viewport;
    private Stage stage;
    private Scene scene;
    private GridPane grid;
    private MapTeils[][] mapMatrix;
    protected boolean mapIsRunning;
    private boolean isAlien = false;
    public int posX = 0;
    public int posY = 0;
    private int skinID = 0;
    protected int[] materials = new int[50];
    private static PrintStream writer;
    private static BufferedReader reader;
    public final String hostname;
    public final int port;
    public String userName;
    public int playerNumber;
    public PongThread ponger;

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * This constructor of AmongAlienClient will initialize a AmongAlienClient with the
     * given hostname and portnumber and gives the graphic Stage, Scene and grid from the Application GUI.
     *
     * @param hostname IPv4 address of the server.
     * @param port     is the specific portnumber, which the server uses.
     * @param userName the wished username
     */
    public AmongAlienClient(String hostname, int port, String userName) {
        this.hostname = hostname;
        this.port = port;
        this.userName = userName;

        LOGGER.info("AmongAlienCleint with hostname = " + hostname
            + " port = " + port + " userName = " + userName + " was added.");
    }

    /**
     * A Socket will be created, ReadThread and WriteThread will be started.
     * It catches the error if there is no such host to find or it has a
     * input or output fougth.
     *
     * @throws UnknownHostException if the Host is Unknown.
     * @throws IOException          if Socket-Connection does not work.
     */
    public void execute() throws UnknownHostException, IOException {
        //The socket is the connection between Server and Client
        Socket socket = new Socket(hostname, port);

        System.out.println("Verbunden mit Server :)\n");

        //ReadThread is listening for a inputStream and will send
        //a inputStream as a String to the ClientProtocolInterpret
        ReadThread read = new ReadThread(socket, this);
        read.start();
        //WriterThread is listening on Console for userinput
        WriteThread write = new WriteThread(socket, this);
        write.start();

        //Output Stream (sending to Server)
        OutputStream output = socket.getOutputStream();
        writer = new PrintStream(output, true);

        //Input Stream (getting Messages from Server)
        InputStream input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));

        //Starts a new PingThread
        this.ponger = new PongThread(this);
        ponger.start();

        sendToServer("/LOGIN " + userName);
        LOGGER.debug("************************************\n" +
                "       Your name is: " + userName + "\n         Chat started!\n" +
                "   Enter /HELP for the Help Menu\n" +
                "************************************");
    }

    /**
     * This Method will send text to the writer witch sends
     * the text to the server.
     *
     * @param text The text to send to the writer
     */
    public void sendToServer(String text) {
        LOGGER.debug("Client sent to server: " + text);
        writer.println(text);
    }

    /**
     * @return the Players x-Coordinate.
     */
    public int getPosX() {
        LOGGER.debug("getPosX = " + posX);
        return posX;
    }

    /**
     * @param posX the Players x-Coordinate.
     */
    public void setPosX(int posX) {
        LOGGER.debug("setPosX = " + posX);
        this.posX = posX;
    }

    /**
     * @return the Players y-Coordinate.
     */
    public int getPosY() {
        LOGGER.debug("getPosY = " + posY);
        return posY;
    }

    /**
     * @param posY the Players y-Coordinate.
     */
    public void setPosY(int posY) {
        LOGGER.debug("setPosY = " + posY);
        this.posY = posY;
    }

    /**
     * adds material
     *
     * @param textureID the textureID
     */
    public void addMat(int textureID) {
        int i = 0;
        while (materials[i] != 0) {
            i++;
        }
        materials[i] = textureID;
    }
}
