package ch.unibas.dmi.dbis.cs108.AmongAlien;
import ch.unibas.dmi.dbis.cs108.AmongAlien.server.Server;
import javafx.application.Application;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Scanner;

/**
 * The start of AmongAlien
 * This class handels the start of AmongAlien 2.0 and defines if a server will be
 * started on a given port oder if a client will be started with given hostadress,
 * port and username.
 *
 * Parameters to start a server: server
 *                               server [port]
 * Parameters to start a client: client
 *                               client [hostadress]:[port]
 *                               client [hostadress] [port]
 *                               client [hostadress]:[port] [username]
 *                               client [hostadress] [port] [username]
 */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * Main Method to start the Game with either given parameters or no parameters. The
     * Main Method can start a server or a client. If the first parameter is "client" it
     * starts a client and if the first parameter is "server" it starts a server. If the
     * second parameter after "server" is a Portnumber it will start the server on this
     * given Port. By the client it can be added a parameter for hostadress, port and
     * username.
     *
     * @param args Die Argumente welche bestimmen ob Server oder Client
     *             gestartet werden.
     */
    public static void main(String[] args) {
        int argsLen = args.length;
        if (argsLen > 0) { //Looks if there are Parameters
            //args[0] should always be either client or server
            String serverOrClient = args[0].toUpperCase();
            if (args[0].equalsIgnoreCase("CLIENT")) {
                LOGGER.info("Started a client!");
                //if args[0] is client we switch over the number of args
                //to handle the different inputs that cold come up.
                switch (argsLen) {
                    case 1:
                        //if input is just client we start and ask for the
                        //needed parameters later
                        Application.launch(ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GUI.class, "", "", "");
                        break;
                    case 2:
                        //Parameters client and just one other, should be
                        //hostadress and port separated by :
                        if (args[1].contains(":")) {
                            String[] splitter = args[1].split(":");
                            Application.launch(ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GUI.class, splitter[0], splitter[1],
                                    System.getProperty("user.name"));
                        } else {
                            LOGGER.fatal("The arguments does not match pattern!");
                        }
                        break;
                    case 3:
                        //Parameters can be client, hostadress and port or
                        //client, hostadress:port and Username
                        if (args[1].contains(":")) {
                            String[] splitter = args[1].split(":");
                            Application.launch(ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GUI.class, splitter[0], splitter[1], args[2]);
                        } else {
                            Application.launch(ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GUI.class, args[1], args[2],
                                    System.getProperty("user.name"));
                        }
                        break;
                    case 4:
                        //Parameters are client, hostadres, port, username
                        Application.launch(ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GUI.class, args[1], args[2], args[3]);
                        break;
                    default:
                        LOGGER.fatal("The arguments does not match pattern!");
                        break;
                }
            } else if (args[0].equalsIgnoreCase("SERVER")) {
                LOGGER.info("Started a server!");
                if (argsLen < 2) {
                    System.out.println("Please enter a Port!");
                    try {
                        Scanner olaf = new Scanner(System.in);
                        int port = olaf.nextInt();
                        Server.startServer(port);
                    } catch (NumberFormatException e) {
                        System.out.println("Port must be a Number!");
                        System.exit(0);
                    }
                } else if (args.length == 2) {
                    try {
                        int port = Integer.parseInt(args[1]);
                        Server.startServer(port);
                    } catch (NumberFormatException e) {
                        System.out.println("Port must be a Number!");
                        System.exit(0);
                    }
                } else {
                    System.out.println("Das Erste Argument muss entweder \"client\" or \"server port\"sein!\n"
                            + "Beispiel: client Oder server 8090");
                }
            }
        }
        System.out.println("Das Erste Argument muss entweder \"client\" or \"server port\"sein!\n"
                + "Beispiel: client Oder server 8090");
    }

    /**
     * A testing Method to test the System.out.Println function
     */
    public static void test(){
        System.out.println("Has Printed!");
    }

    /**
     * A Logging Tool to print out given Logging Statements
     * in their respective Logging Level
     * @param par given Logging Statements
     */
    public void logMessagers(String par) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("This is a debug: " + par);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("This is a info: " + par);
        }
        LOGGER.warn("This is a warn: " + par);
        LOGGER.error("This is a error: " + par);
        LOGGER.fatal("This is a fatal: " + par);
    }
}
