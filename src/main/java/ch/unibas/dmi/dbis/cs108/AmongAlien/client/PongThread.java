package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a extra Thread to handle the Pings.
 *
 * @author Rayes Diyab
 * @version 2021.05.10
 */
public class PongThread extends Thread{
    private final AmongAlienClient client;
    private int counter = 0;

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * This is a own Thread to send e /ping to the server.
     *
     * @param client the client instance
     */
    public PongThread(AmongAlienClient client) {
        this.client = client;
    }


    /**
     * It runs the code below on Thread start
     */
    @Override
    public void run() {
        while (counter < 2){
            client.sendToServer("/PONG ");
            counter++;

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                LOGGER.error("Thread Interrupted");
            } catch (NullPointerException e){
                LOGGER.error(client.userName);
                LOGGER.error(e.getStackTrace());
            }

        }
        //System.out.println("Connection Lost to Server");
        ClientProtocolInterpret.clientInterpret("/CONNECTIONLOST", client);
    }

    /**
     * The counter gets reset
     */
    public void resetCounter(){
        counter = 0;
    }
}