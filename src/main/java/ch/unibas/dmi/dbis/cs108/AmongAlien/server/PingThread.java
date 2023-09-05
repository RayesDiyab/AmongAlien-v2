package ch.unibas.dmi.dbis.cs108.AmongAlien.server;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a Thread just to handle the Pong.
 *
 * @author Rayes Diyab
 * @version 2021.05.10
 */
public class PingThread extends Thread {
    private final ClientThread client;
    private final Server server;
    public int counter = 0;

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * A own Thread witch just pongs the server.
     *
     * @param server the server instance
     * @param client the client instance
     */
    public PingThread(ClientThread client, Server server) {
        this.client = client;
        this.server = server;
    }

    /**
     * run() is abstract in Thread.
     * It lets the Thread sleep for 10 seconds
     * afterwords it sends a /pong to the client.
     */
    @Override
    public void run() {
        while (counter < 2) {
            client.sendMessage("/PING ");
            counter++;

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                LOGGER.fatal("Thread Interrupted");
            }
        }
        LOGGER.error("Connection Lost from " + client.userName);
        ServerProtocolInterpret.serverInterpret("/QUITLOBBYGUI ", client, server);
    }

    /**
     * Resets the Counter to zero
     */
    public void resetCounter(){
        counter = 0;
    }
}

