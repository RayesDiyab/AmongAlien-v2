package ch.unibas.dmi.dbis.cs108.AmongAlien.tools;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * A Matrix of MapTeils. It stores all the Information about the Map.
 * The Server can actualize the Informations and send it to the
 * Client. The Client will build a BufferdeImage with the Informations
 * so that the User can  see a Map on his screen.
 *
 * @author Joel Erbsland
 * @version 2021.05.10
 */
public class MapMatrix {
    Random olaf = new Random();
    final int MAP_SIZE = 30;
    final int PIXELS_FOR_EACH_MAPTEIL = 64;
    private int mapRows;
    private int mapColumns;
    private int amountOfMaterial = 0;
    private int amountOfPlayers = 0;
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
    private String[] taskLocation = new String[10];

    MapTeils[][] mapMatrix;

    /**
     * Creates a MapMatrix with a fixed Map Size. The Size defines how
     * many MapTiles are in a raw.
     */
    public MapMatrix() {
        mapMatrix = new MapTeils[MAP_SIZE][MAP_SIZE];
        LOGGER.debug("Standard constructor of MapMatrix created a standard MapMatrix.");
    }

    /**
     * Creates a MapMatrix with given Map Size (columns/rows).
     *
     * @param columns The amount of columns.
     * @param rows The amount of rows.
     */
    public MapMatrix(int columns, int rows) {
        LOGGER.debug("MapMatrix constructor is called with an int columns and a int rows!");
        mapMatrix = new MapTeils[columns][rows];
        mapRows = rows;
        mapColumns = columns;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                mapMatrix[i][j] = new MapTeils(0, 0);
                LOGGER.debug("MapTeil column = " + i + " and row = " + j + " has textureID = 0 and fieldUSE = 0");
            }
        }
    }

    /**
     * This Method is a hard coded generating demand for a very simpel
     * Map with just grass texture an a border of trees.
     *
     * @param worldName the worlds Name
     * @param quadraticSize the quadratic size of how mani Tiles per
     *                      row and column.
     */
    public MapMatrix(String worldName, int quadraticSize) {
        mapMatrix = new MapTeils[quadraticSize][quadraticSize];
        mapRows = quadraticSize;
        mapColumns = quadraticSize;

        if (worldName.equals("TestWelt")) {
            createTestMap(worldName, quadraticSize);
        } else {
            createAmap(worldName, quadraticSize);
        }

    }

    /**
     * This Method is a hard coded generating demand for a very simple
     * Map with just grass texture an a border of trees.
     *
     * @param worldName the worlds Name
     * @param quadraticSize the quadratic size of how mani Tiles per
     *                      row and column.
     * @param amountOfPlayers the amount of Players in the Game.
     */
    public MapMatrix(String worldName, int quadraticSize, int amountOfPlayers) {
        mapMatrix = new MapTeils[quadraticSize][quadraticSize];
        mapRows = quadraticSize;
        mapColumns = quadraticSize;
        this.amountOfPlayers = amountOfPlayers;

        if (worldName.equals("TestWelt")) {
            createTestMap(worldName, quadraticSize);
        } else {
            createAmap(worldName, quadraticSize);
        }

    }

    /**
     * This Method creates the actual map and not the Test Map
     *
     * @param worldName the worlds Name
     * @param quadraticSize the quadratic size of how mani Tiles per
     *                      row and column.
     */
    public void createAmap(String worldName, int quadraticSize) {
        int taskNum = 1;
        String[] taskString = new String[11];
        //System.out.println(getClass().getResource("/HiddenInForrest.txt").toString());
        if (amountOfPlayers < 4) {
            amountOfPlayers = 4;
        }
        String mapPath = "/" + worldName + ".txt";
        String taskPath = "/Tasks/tasksFor" + amountOfPlayers + ".txt";
        //BufferedImageLoader.class.getResource(path)
        String[] aLine = new String[quadraticSize];
        //System.out.println(getClass().getResource(path).toString());
        //File mapFile = new File(path);
        //File mapFile = new File(getClass().getResource(path).toString());
        InputStream mapFile = this.getClass().getResourceAsStream(mapPath);
        InputStream taskFile = this.getClass().getResourceAsStream(taskPath);
        //File mapFile = new File(MapMatrix.class.getResource(mapPath).getFile());
        //File taskFile = new File(MapMatrix.class.getResource(taskPath).getFile());
        if (mapFile != null) {
            System.out.println("mapFile does exists!");
        } else {
            System.out.println("mapFile does not exists!");
        }
        if (taskFile != null) {
            System.out.println("taskFile does exists!");
        } else {
            System.out.println("taskFile does not exists!");
        }
        BufferedReader olaf = null;
        BufferedReader tasker = null;

        try {
            //tasker = new BufferedReader(new FileReader(MapMatrix.class.getResource(taskPath).getFile()));
            tasker = new BufferedReader(new InputStreamReader(taskFile));
            String trow = null;
            int trows = 0;
            while ((trow = tasker.readLine()) != null) {
                taskString[trows] = trow;
                System.out.println("Task" + trows + ": " + taskString[trows]);
                trows++;

            }


            LOGGER.debug("/" + worldName + ".txt");
            //olaf = new BufferedReader(new FileReader(path));
            //olaf = new BufferedReader(new FileReader(MapMatrix.class.getResource(mapPath).getFile()));
            olaf = new BufferedReader(new InputStreamReader(mapFile));
            String row = null;
            int rows = 0;
            while ((row = olaf.readLine()) != null) {
                LOGGER.debug("Zeile: " + rows);
                LOGGER.debug(row + "   ||   ");
                aLine = row.split(",");
                for (int columns = 0; columns < quadraticSize; columns++) {
                    LOGGER.debug("  ***  " + aLine[columns] + "  ***  ");
                    String[] splitter = aLine[columns].split(":");
                    if (splitter[1].equals("3")) {
                        String thisTask = taskString[taskNum];
                        String[] taskStringArray = (thisTask.split(":"));
                        int[] taskIntArray = new int[taskStringArray.length];
                        for (int a = 0; a < taskStringArray.length; a++) {
                            taskIntArray[a] = Integer.parseInt(taskStringArray[a]);
                        }
                        this.mapMatrix[columns][rows] = new MapTeils(Integer.parseInt(splitter[0]),
                                Integer.parseInt(splitter[1]), taskIntArray);
                        LOGGER.debug("MapTeil column = " + columns + " and row = " + rows
                                + " has textureID = " + splitter[0] + " and fieldUSE = " + splitter[1]
                                + " and tasks got increased by one.");
                        taskNum++;
                    } else {
                        this.mapMatrix[columns][rows] = new MapTeils(Integer.parseInt(splitter[0]),
                                Integer.parseInt(splitter[1]));
                        LOGGER.debug("MapTeil column = " + columns + " and row = " + rows
                                + " has textureID = " + splitter[0] + " and fieldUSE = " + splitter[1]);
                    }
                }
                rows++;
            }
        } catch (IOException e) {
            LOGGER.fatal("Can't read the map file.");
            e.printStackTrace();
        }
    }

    /**
     * This Method is a hard coded TestMap generator for a very simple
     * Map with just grass texture and a border of trees.
     *
     * @param worldName the worlds Name
     * @param quadraticSize the quadratic size of how many Tiles per
     *                      row and column.
     */
    public void createTestMap(String worldName, int quadraticSize) {
        mapMatrix = new MapTeils[quadraticSize][quadraticSize];
        mapRows = quadraticSize;
        mapColumns = quadraticSize;

        if (!worldName.equals("TestWelt")) {
            LOGGER.fatal("Unknown Map");
            return;
        }
        if (quadraticSize < 30) {
            LOGGER.fatal("Map to small!");
            return;
        }
        if (quadraticSize % 2 != 0) {
            LOGGER.fatal("Can not make tree borders! (quadraticSize % 2 != 0)");
            return;
        }
        /*------------------------------------------------------------*/
        /* Fill whole Map with textureID 0 and fileUSE = 1;           */
        /*------------------------------------------------------------*/
        for (int row = 0; row < quadraticSize; row++) {
            for (int column = 0; column < quadraticSize; column++) {
                mapMatrix[column][row] = new MapTeils(0, 0);
            }
        }
        /*------------------------------------------------------------*/
        /* Fill upper and lower border with Trees and watter          */
        /*------------------------------------------------------------*/
        for (int column = 0; column < quadraticSize; column++) {
            mapMatrix[column][0] = new MapTeils(10, 1);
            mapMatrix[column][1] = new MapTeils(20, 1);
            mapMatrix[column][quadraticSize - 2] = new MapTeils(3, 1);
            mapMatrix[column][quadraticSize - 1] = new MapTeils(13, 1);
        }
        mapMatrix[0][quadraticSize - 2] = new MapTeils(2, 1);
        mapMatrix[0][quadraticSize - 1] = new MapTeils(12, 1);
        mapMatrix[quadraticSize - 1][quadraticSize - 2] = new MapTeils(4, 1);
        mapMatrix[quadraticSize - 1][quadraticSize - 1] = new MapTeils(14, 1);
        /*------------------------------------------------------------*/
        /* Generate a fishingroth                                      */
        /*------------------------------------------------------------*/
        mapMatrix[quadraticSize / 2][quadraticSize - 3] = new MapTeils(33, 4);
        /*------------------------------------------------------------*/
        /* Fill left and right border with Trees                      */
        /*------------------------------------------------------------*/
        for (int row = 2; row < quadraticSize - 2; row++) {
            mapMatrix[0][row] = new MapTeils(10, 1);
            mapMatrix[1][row] = new MapTeils(10, 1);
            mapMatrix[quadraticSize - 2][row] = new MapTeils(10, 1);
            mapMatrix[quadraticSize - 1][row] = new MapTeils(10, 1);
            row++;
            mapMatrix[0][row] = new MapTeils(20, 1);
            mapMatrix[1][row] = new MapTeils(20, 1);
            mapMatrix[quadraticSize - 2][row] = new MapTeils(20, 1);
            mapMatrix[quadraticSize - 1][row] = new MapTeils(20, 1);
        }
        /*------------------------------------------------------------*/
        /* Create a House                                             */
        /*------------------------------------------------------------*/
        mapMatrix[6][6] = new MapTeils(77, 1);
        mapMatrix[7][6] = new MapTeils(78, 1);
        mapMatrix[8][6] = new MapTeils(79, 1);
        mapMatrix[6][7] = new MapTeils(87, 1);
        mapMatrix[7][7] = new MapTeils(88, 1);
        mapMatrix[8][7] = new MapTeils(89, 1);
        mapMatrix[6][8] = new MapTeils(97, 1);
        mapMatrix[7][8] = new MapTeils(98, 1);
        mapMatrix[8][8] = new MapTeils(99, 1);
        /*------------------------------------------------------------*/
        /* Create the Lagerfire                                       */
        /*------------------------------------------------------------*/
        mapMatrix[quadraticSize / 2][quadraticSize / 2] = new MapTeils(25, 1);
        /*------------------------------------------------------------*/
        /* Create GetTask-Signs                                       */
        /*------------------------------------------------------------*/
        mapMatrix[10][10] = new MapTeils(26, 1);
        mapMatrix[32][17] = new MapTeils(26, 1);
        mapMatrix[16][19] = new MapTeils(26, 1);
        mapMatrix[27][21] = new MapTeils(26, 1);
        mapMatrix[31][9] = new MapTeils(26, 1);
        mapMatrix[9][30] = new MapTeils(26, 1);

        LOGGER.debug("**********************************************************");
        LOGGER.debug("Map createt with: ");
        LOGGER.debug("Rows = " + mapRows);
        LOGGER.debug("Columns = " + mapColumns);
        LOGGER.debug("**********************************************************");
    }

    /**
     * @return the width of the MapMatrix (all Tiles) in pixels as int.
     */
    public int getWidth() {
        LOGGER.debug("getWidth = " + mapColumns * PIXELS_FOR_EACH_MAPTEIL);
        return mapColumns * PIXELS_FOR_EACH_MAPTEIL;
    }

    /**
     * @return the height of the MapMatrix (all Tiles) in pixels as int.
     */
    public int getHeight() {
        LOGGER.debug("getHeight = " + mapRows * PIXELS_FOR_EACH_MAPTEIL);
        return mapRows * PIXELS_FOR_EACH_MAPTEIL;
    }

    /**
     * @return the amount of rows in the MapMatrix (2D Array of MapTiles)
     */
    public int getRows() {
        LOGGER.debug("getRows = " + mapRows);
        return mapRows;
    }

    /**
     * @return the amount of columns in the MapMatrix (2D Array of MapTiles)
     */
    public  int getColumns() {
        LOGGER.debug("getColumns = " + mapColumns);
        return mapColumns;
    }

    /**
     * @return the amount of pixels in one line of the quadratic MapTile
     */
    public  int getPixelsForEachTeil() {
        LOGGER.debug("getPixelsForEachTeil = " + PIXELS_FOR_EACH_MAPTEIL);
        return PIXELS_FOR_EACH_MAPTEIL;
    }

    /**
     * This Method returns the textureID specific
     *
     * @param row the amount of rows in the MapMatrix
     * @param column the amount of columns in the MapMatrix
     * @return int textureID: The textureID of the specific MapTile
     */
    public int getTeilsTextureID(int column, int row) {
        LOGGER.debug("----now in MapMatrix----");
        LOGGER.debug("Row =" + row + ", Column = " + column);
        LOGGER.debug("---------go BACK--------");
        LOGGER.debug("Rows = " + mapMatrix.length);
        LOGGER.debug("Colums =  " + mapMatrix[0].length);
        if (row < 0 || column < 0 || row > this.getRows()
                || column > this.getColumns()) {
            LOGGER.fatal("Row or Column out of Bounds!");
        }
        return mapMatrix[column][row].getTextureID();
    }

    /**
     * This Method returns the textureID specific
     *
     * @param column the specific columns in the MapMatrix
     * @param row the specific rows in the MapMatrix
     * @return int textureID: The textureID of the specific MapTile
     */
    public int getTeilsfieldUSE(int column, int row) {
        if (row < 0 || column < 0 || row > this.getRows()
                || column > this.getColumns()) {
            LOGGER.fatal("Row or Column out of Bounds!");
        }
        return mapMatrix[column][row].getFieldUSE();
    }

    /**
     * This Method returns the specific Taskarray of a MapTeil
     * or {0, 0, 0, 0, 0, 0} if this filed does not have any Task.
     *
     * @param column the specific columns in the MapMatrix
     * @param row the specific rows in the MapMatrix
     * @return a int[] with the amount of the materials to
     *          bring to complete this task.
     */
    public int[] getTeilsTask(int column, int row) {
        return this.mapMatrix[column][row].getTask();
    }

    /**
     * This Method will override a existing MapTile on a specific part
     * in the MapMatrix on witch the Method is colled.
     *
     * @param row the specific rows in the MapMatrix
     * @param column the specific columns in the MapMatrix
     * @param newTextureID the new textureID for this MapTiles
     * @param newFieldUSE the new fieldUSE for this MapTiles
     */
    public void changeMapTeils(int column, int row, int newTextureID, int newFieldUSE) {
        if (row < 0 || column < 0 || row > this.getRows()
                || column > this.getColumns()) {
            LOGGER.fatal("Row or Column out of Bounds!");
        }
        this.mapMatrix[column][row] = new MapTeils(newTextureID, newFieldUSE);
    }

    /**
     * A getter Method to get the amount of materials on the Map.
     *
     * @return the amount of material on Map as int.
     */
    public int getAmountOfMaterial() {
        return amountOfMaterial;
    }

    /**
     * This Mathod adds one to amountOfMaterial.
     */
    public void addMaterialToAmountOfMaterial() {
        amountOfMaterial++;
    }

    /**
     * This Method removes one Materiel from amountOfMaterial.
     */
    public void removeMaterialFromAmountOfMaterial() {
        amountOfMaterial--;
    }

    /**
     * This Method will spawn a given amount of Materials
     * to the given Map described by a MapMatrix.
     *
     * @param amount of how many Materials should be spawned.
     * @return a String formatted like: "xPos:yPos:textureID"
     */
    public String spawnMaterial(int amount) {
        int[] materialTexId = {11, 21, 30, 31, 32};
        String matString = "";
        boolean isFirst = true;
        int column1 = 0;
        int row1 = 0;
        int mat1 = 0;
        int tryes = 0;

        System.out.println("Spawning...");

        for (int i = 0; i < amount; i++) {
            //System.out.println("**************************************************************************************************************");
            //System.out.println("i = " + i);
            //System.out.println("**************************************************************************************************************");
            while (this.getTeilsfieldUSE(column1, row1) != 0 && tryes < 50) {

                tryes++;
                column1 = olaf.nextInt((this.getColumns() - 6) + 3);
                row1 = olaf.nextInt((this.getRows() - 6) + 3);
                mat1 = olaf.nextInt(5);

                //System.out.println("Will spawn on: " + column1 + ", " + row1 + " With FieldUSE: " + this.getTeilsfieldUSE(column1, row1)
                        //+ "\nTexID: " + materialTexId[mat1] );


            }

            if (this.getTeilsfieldUSE(column1, row1) == 0) {
                this.changeMapTeils(column1, row1, materialTexId[mat1], 2);
                if (!isFirst) {
                    matString += ":";
                }
                isFirst = false;
                this.addMaterialToAmountOfMaterial();
                matString += column1 + ":" + row1 + ":" + materialTexId[mat1];
                this.mapMatrix[column1][row1] = new MapTeils(materialTexId[mat1], 2);
                changeMapTeils(column1, row1, materialTexId[mat1], 2);

                //System.out.println("------------------------------------------------------------------------------------------------------");
                //System.out.println("Have spawned on: " + column1 + ", " + row1 + " With FieldUSE: " + this.getTeilsfieldUSE(column1, row1)
                        //+ "\nTexID: " + materialTexId[mat1] );
                //System.out.println("------------------------------------------------------------------------------------------------------");

            }

            tryes = 0;
        }
        return matString;
    }
}
