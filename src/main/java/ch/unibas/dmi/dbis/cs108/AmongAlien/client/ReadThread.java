package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;


/**
 * This thread is responsible for reading server's input and printing it
 * to the console.
 * It runs in an infinite loop until the client disconnects from the server.
 *
 * @author Rayes Diyab
 * @version 16.05.2022
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private AmongAlienClient client;

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * This constructor creates a ReadThread witch is listening to the servers messeges an give the to the
     * ClientProtocolInterpret to get the knowledge about what should be done with the server's message.
     *
     * @param socket with the connection to the server.
     * @param client the AmongAlienClient Instance
     */
    public ReadThread(Socket socket, AmongAlienClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            LOGGER.error("Can't get inputstream: " + e.getMessage());
            LOGGER.error(e.getStackTrace());
        }
    }

    /**
     * This is where things go on. This run is running on a extra thread an is allways listening to
     * the servers messages and sending them to the ClientProtocolInterpret.
     */
    public void run() {

        String response;
        try {
            do {
                response = reader.readLine();
                ClientProtocolInterpret.clientInterpret(Objects.requireNonNullElse(response, ""), this.client);
            } while (!(response == null));
        }catch (IOException ignored) {
            LOGGER.error(ignored.getStackTrace());
            }
    }
}
