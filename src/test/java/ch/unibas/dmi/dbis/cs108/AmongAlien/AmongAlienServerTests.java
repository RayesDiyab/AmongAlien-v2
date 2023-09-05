package ch.unibas.dmi.dbis.cs108.AmongAlien;

import ch.unibas.dmi.dbis.cs108.AmongAlien.client.AmongAlienClient;
import ch.unibas.dmi.dbis.cs108.AmongAlien.server.*;
import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.MapMatrix;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * This is a Junit Test Class to test most of the Server functionality
 * It uses Mockito() to mock the Server, AmongAlienClient, ClientThread
 * and the HighscoreList
 *
 * @author Rayes Diyab
 * @version 01/05/2022
 */
public class AmongAlienServerTests {

    @InjectMocks
    Server server = new Server(0);
    //@InjectMocks
    //ServerSocket serverSocket = new ServerSocket(0);
    //@InjectMocks
    //Socket socket;
    @InjectMocks
    ClientThread client = new ClientThread(0,server, null);
    @InjectMocks
    AmongAlienClient amongAlienClient = new AmongAlienClient("localhost", 0, "Rayes");
    @InjectMocks
    HighscoreList highscoreList = new HighscoreList();

    /*
     * Streams to store system.out and system.err content
     */
    private ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    private ByteArrayOutputStream errStream = new ByteArrayOutputStream();

    /*
     * Here we store the previous pointers to system.out / system.err
     */
    private PrintStream outBackup;
    private PrintStream errBackup;

    /**
     * This is a method that throws an exception because by
     * Mocking the ClientThread one has to throw an exception
     * @throws IOException from the ClientThread on line 34
     */
    public AmongAlienServerTests() throws IOException {}

    /**
     * This method is executed before each test.
     * It redirects System.out and System.err to our variables {@link #outStream} and {@link #errStream}.
     * This allows us to test their content later.
     */
    @BeforeEach
    public void redirectStdOutStdErr() {
        outBackup = System.out;
        errBackup = System.err;
        System.setOut(new PrintStream(outStream));
        System.setErr(new PrintStream(errStream));
    }

    /**
     * This method is run after each test.
     * It redirects System.out / System.err back to the normal streams.
     */
    @AfterEach
    public void reestablishStdOutStdErr() {
        System.setOut(outBackup);
        System.setErr(errBackup);
    }

    /**
     * Test to start the main Method without any Parameters
     * This shouldn't be possible, and thus it should display
     * the correct Error Message and pass the Test
     */
    @Test
    public void startMainWithoutParameters() {
        String[] argsG = {""};
        Main.main(argsG);
        String toTest = outStream.toString();
        assertTrue(toTest.contains("Das Erste Argument muss entweder \"client\" or \"server port\"sein!\n"
                + "Beispiel: client Oder server 8090"));
    }

    /**
     * Create a Socket in the Mocked Server
     * This should pass if the Socket gets
     * created successfully
     */
    @Test
    public void testServerSocketGetsCreated(){
        assertNotNull(server.createNewServerSocket(0));
    }

    /**
     * Tries to create a Server with an out of Range Port
     * which should throw an IllegalArgumentException
     * for it to pass
     */
    @Test
    public void startServerWithOutOfRangePortUncaught() {
        int port = 2147483647;
        Server faultyServer = new Server(port);
        faultyServer.running = false;
        assertThrows(IllegalArgumentException.class, faultyServer::execute, "Faulty Port " +
                "should throw Exception");
    }

    /**
     * Should complete the WriteFileTask
     * without throwing any Exception
     */
    @Test
    void testWriteFile() {
        assertDoesNotThrow(HighscoreList::writeFile);
    }

    /**
     * Adds a new Win to the Highscorelist to the named PlayerName
     * as a Human and checks if the Player has been added to the List
     * If the String is contained in the ArrayList it passes
     */
    @Test
    void testActualizeWinsAsHuman() {
        HighscoreList.actualizeWinsAsHuman(new String[]{"HumansToUpdate"});
        ArrayList<String> containingUsers = HighscoreList.getContainingUsers();
        Assertions.assertTrue(containingUsers.contains("HumansToUpdate"));
    }

    /**
     * Adds a new Win to the Highscorelist to the named PlayerName
     * as an Alien and checks if the Player has been added to the List
     * If the String is contained in the ArrayList it passes
     */
    @Test
    void testActualizeWinsAsAlien() {
        HighscoreList.actualizeWinsAsAlien(new String[]{"AliensToUpdate"});
        ArrayList<String> containingUsers = HighscoreList.getContainingUsers();
        Assertions.assertTrue(containingUsers.contains("AliensToUpdate"));
    }

    /**
     * Test if the Client is in a Lobby after setting his Lobby to null
     * by calling the unsetLobby() Method in th ClientThread
     * If the Client.isInLobby is false it passes
     */
    @Test
    void testIsClientInLobby() {
        client.unsetLobby();
        Assertions.assertFalse(client.isInLobby());
    }

    /**
     * It tests if the addMat Method passes without Throwing any Exceptions
     */
    @Test
    void testAddMat() {
        int textureID = 25;
        assertDoesNotThrow(() -> amongAlienClient.addMat(textureID));

    }

    /**
     * Removes the empty Line created by System.out.println()
     * from the given String and returns the updated String
     * @param str original String before empty line removal
     * @return  updated String after empty line removal
     */
    private static String removeNewline(String str) {
        return str.replace("\n", "").replace("\r", "");
    }
}
