package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * This thread is responsible for reading user's input and send it
 * to the server.
 * It runs in an infinite loop until the user types 'bye' to quit.
 *
 * @author Rayes Diyab
 * @version 2022.03.23
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private AmongAlienClient client;
    private String sysUsername = System.getProperty("user.name");

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * Constructor of WriteThread
     *
     * @param socket the socket witch keeps the connection
     * @param client the EchoClientThread on witch this Client connects to.
     */
    public WriteThread(Socket socket, AmongAlienClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            LOGGER.error("Can't get inputstream: " + e.getMessage());
            LOGGER.error(e.getStackTrace());
        }

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            LOGGER.error("Can't get outputstream: " + e.getMessage());
            LOGGER.error(e.getStackTrace());
        }
    }

    /**
     * This run() Method is handling the connection between Client and Server.
     */
    public void run() {
        Scanner olaf = new Scanner(System.in);

        String text;

        //Loop as long as you don't write /quit.
        do {
            text = olaf.nextLine();
            if (text.startsWith("/")) {
                writer.println(text);
            } else {
                text = "/SETMSG " + text;
                writer.println(text);
            }

        } while (!text.equals("/LOGOUT"));
        LOGGER.fatal("in WriteThread triggering QUIT");
        client.sendToServer("/LOGOUT");


        try {
            socket.close();
        } catch (IOException ex) {
            LOGGER.error("Couldn't write to the Server!");
            LOGGER.error(ex.getStackTrace());
        }
    }
}
