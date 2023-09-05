package ch.unibas.dmi.dbis.cs108.AmongAlien.server;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * This class checks uf a /files directory does exists and creates one if not. Then it will check if
 * the /files directory does include a highscoreList.txt and if not it will create one. This list has
 * to match patter,means: First line has to be: "PlayerName:WinsAsHuman:WinsAsAlien"
 * The following lines will be read by Starting the server and they will be stored in a ArrayList"Wins"
 * and they can also be written.
 *
 * @author Joel Erbsland
 * @version 2021.05.22
 */
public class HighscoreList {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
    private static ArrayList<Wins> highscores = new ArrayList<>();
    private String pattern;
    private String folderPath = "files";
    private String listPath = folderPath + "/highscoreList";
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    private PrintWriter pw = null;

    /**
     * Standard Constructor
     * The highscoreList.txt file will be created or loaded if already existing.
     */
    public HighscoreList() {
        File listFileFolder = new File(folderPath);
        File listFile = new File(listPath + ".txt");
        if (!listFileFolder.exists()) {
            if (listFileFolder.mkdir()) {
                System.out.println("Created /files directory!");
                try {
                    if (listFile.createNewFile()) {
                        System.out.println("New " + listPath + ".txt created!");
                        writer = new BufferedWriter(new FileWriter(listPath + ".txt"));
                        writer.write("PlayerName:WinsAsHuman:WinsAsAlien");
                        writer.newLine();
                        writer.write("Joel:10:50");
                        writer.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("Failed to create " + listPath + ".txt!");
                    LOGGER.error(e.getStackTrace());
                }
            } else {
                System.out.println("Failed to creat /files directory!");
                LOGGER.fatal("Can't handle HighscoreList.");
            }
        } else {
            LOGGER.warn("/files directory does already exists!");
            if (!listFile.exists()) {
                try {
                    if (listFile.createNewFile()) {
                        System.out.println("New " + listPath + ".txt created!");
                        writer = new BufferedWriter(new FileWriter(listPath + ".txt", true));
                        writer.write("PlayerName:WinsAsHuman:WinsAsAlien");
                    } else {
                        LOGGER.error("Failed to create " + listPath + ".txt!");
                    }
                } catch (IOException e) {
                    LOGGER.error("Failed to create " + listPath + ".txt!");
                    LOGGER.error(e.getStackTrace());
                }
            } else {
                LOGGER.warn(listPath + ".txt does already exists!");
                try {
                    reader = new BufferedReader(new FileReader(listPath + ".txt"));
                    if (!reader.readLine().equals("PlayerName:WinsAsHuman:WinsAsAlien")) {
                        LOGGER.warn(listPath + ".txt is brocken!");
                    } else {
                        LOGGER.warn(listPath + ".txt does already exists and is usable!");
                    }
                    reader = null;
                } catch (IOException e) {
                    LOGGER.error(listPath + ".txt is brocken!");
                    LOGGER.error(e.getStackTrace());
                }
            }
        }
        try {
            reader = new BufferedReader(new FileReader(listPath + ".txt"));
            pattern = reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = new String[3];
                parts = line.split(":");
                highscores.add(new Wins(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
            }
        } catch (IOException e) {
            LOGGER.error(listPath + ".txt does not match pattern!");
            LOGGER.error(e.getStackTrace());
        }

    }

    /**
     * This method will actualize the WinsAsHumans of a given Group.
     *
     * @param usersToUpdate a String[] listing all Users that have won as human.
     */
    public static void actualizeWinsAsHuman(String[] usersToUpdate) {
        for (int i = 0; i < usersToUpdate.length; i++) {
            if (!getContainingUsers().contains(usersToUpdate[i])) {
                highscores.add(new Wins(usersToUpdate[i], 1, 0));
            } else {
                highscores.get(getContainingUsers().indexOf(usersToUpdate[i])).addWinAsHuman();
            }
        }
    }

    /**
     * This method will actualize the WinsAsHumans of a given Group.
     *
     * @param usersToUpdate a String listing all Users that have won as human.
     */
    public static void actualizeWinsAsAlien(String[] usersToUpdate) {
        for (int i = 0; i < usersToUpdate.length; i++) {
            if (!getContainingUsers().contains(usersToUpdate[i])) {
                highscores.add(new Wins(usersToUpdate[i], 1, 0));
            } else {
                highscores.get(getContainingUsers().indexOf(usersToUpdate[i])).addWinAsAlien();
            }
        }
    }

    /**
     * @return ArrayList"Wins" highscores witch stores PlayerNames, WinsAsHuman and WinsAsAlien
     */
    public static ArrayList<Wins> getHighscoreList() {
        return highscores;
    }

    /**
     * @return a String[] with all userNames in ArrayList"Wins" highscores.
     */
    public static ArrayList<String> getContainingUsers() {
        ArrayList<String> containingUsers = new ArrayList<>();
        for (int i = 0; i < highscores.size(); i++) {
            containingUsers.add(highscores.get(i).getUserName());
        }
        return containingUsers;
    }

    /**
     * @return a String with linewise "PlayerName:WinsAsHuman:WinsAsAlien" while the firs line is the description.
     */
    public static String highscoreToString() {
        String toString = "PlayerName:WinsAsHuman:WinsAsAlien";
        for (int i = 0; i < highscores.size(); i++) {
            toString += "\n" + highscores.get(i).getUserName() + ":"
                    + highscores.get(i).getWinsAsHuman() + ":"
                    + highscores.get(i).getWinsAsAlien();
        }
        return toString;
    }

    /**
     * Converts the whole HighScoreList to One String only
     *
     * @return the HighScore String
     */
    public static String highscoreToOneLineString() {
        String toString = "PlayerName:WinsAsHuman:WinsAsAlien";
        for (int i = 0; i < highscores.size(); i++) {
            toString += ":" + highscores.get(i).getUserName() + ":"
                    + highscores.get(i).getWinsAsHuman() + ":"
                    + highscores.get(i).getWinsAsAlien();
        }
        return toString;
    }

    /**
     * This method writes the HighscoreList into a .txt file.
     *
     * @throws IOException if something goes wrong while writing the File.
     */
    public static void writeFile() throws IOException{
        File out = new File("files/highscoreList.txt");
        BufferedWriter olaf = new BufferedWriter(new FileWriter(out));
        olaf.write(highscoreToString());
        olaf.close();
    }

    /**
     * Just to selfecheckt this Class
     *
     * @param args like args as always :)
     */
    public static void main(String[] args) {
        HighscoreList testList = new HighscoreList();

        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Hans-Ueli", "DerDieDas"});
        actualizeWinsAsHuman(new String[] {"Olaf", "Hans-Ueli", "DerDieDas", "Michael"});

        actualizeWinsAsAlien(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas", "Michael"});
        actualizeWinsAsAlien(new String[] {"Olaf", "Gerdrude", "Hans-Ueli", "DerDieDas"});
        actualizeWinsAsAlien(new String[] {"Olaf", "Gerdrude", "Hans-Ueli"});
        actualizeWinsAsAlien(new String[] {"Olaf", "Gerdrude", "Hans-Ueli"});
        actualizeWinsAsAlien(new String[] {"Olaf", "Gerdrude", "Hans-Ueli"});
        actualizeWinsAsAlien(new String[] {"Olaf", "Gerdrude", "Hans-Ueli"});
        actualizeWinsAsAlien(new String[] {"Olaf", "Gerdrude"});
        actualizeWinsAsAlien(new String[] {"Olaf", "Gerdrude"});
        actualizeWinsAsAlien(new String[] {"Olaf"});
        actualizeWinsAsAlien(new String[] {"Olaf"});
        actualizeWinsAsAlien(new String[] {"Olaf"});
        actualizeWinsAsAlien(new String[] {"Olaf"});
        actualizeWinsAsAlien(new String[] {"Olaf"});



        for (int i = 0; i < testList.highscores.size(); i++) {
            System.out.println("Line" + i + " = " + testList.highscores.get(i).getUserName() + ":"
                    + testList.highscores.get(i).getWinsAsHuman() + ":"
                    + testList.highscores.get(i).getWinsAsAlien());
        }
        System.out.println(highscoreToString());
        try {
            writeFile();
        } catch (IOException e) {
            LOGGER.error(e.getStackTrace());
        }
    }

}
