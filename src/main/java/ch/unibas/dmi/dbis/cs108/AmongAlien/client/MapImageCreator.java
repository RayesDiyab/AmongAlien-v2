package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import ch.unibas.dmi.dbis.cs108.AmongAlien.Main;
import ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GUI;
import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.MapMatrix;
import javafx.embed.swing.SwingFXUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The MapImageCrator makes a BufferdeImage with the information of every
 * MapTeil in the MapMatrix.
 *
 * @author Joel Erbsland
 * @version 2021.05.10
 */
public class MapImageCreator {
    BufferedImageLoader hansUeli = new BufferedImageLoader();
    BufferedImage mapSpreadSheet;
    BufferedImage mapImage;
    BufferedImage subBufferedImage;
    SpriteSheet mapSpreadedSpreadSheet;
    private final int TEILS_ON_EACH_LINE = 10;
    private int teilWidth; // = map.getPixelsForEachTeil();
    private int teilHeight; // = map.getPixelsForEachTeil();
    private int textureID;

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * Creates a mapImage.png with the from MapMatrix "map" given Information.
     *
     * @param map The describing MapMatrix
     */
    public void createMapImage(MapMatrix map) {
        teilWidth = map.getPixelsForEachTeil();
        teilHeight = map.getPixelsForEachTeil();
        try {
            mapImage = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_ARGB);
            //mapSpreadSheet = ImageIO.read(new File("MapImag.png"));
            mapSpreadSheet = hansUeli.loadImage("/MapTex.png");
            subBufferedImage = new BufferedImage(map.getPixelsForEachTeil(), map.getPixelsForEachTeil(),
                    BufferedImage.TYPE_INT_ARGB);
        } catch (IOException e) {
            LOGGER.error("Fehler beim Laden des Bildes / der Bilder");
            LOGGER.error(e.getStackTrace());
        }
        mapSpreadedSpreadSheet  = new SpriteSheet(mapSpreadSheet);
        Graphics2D mapToCreate = mapImage.createGraphics();
        //Graphics2D subImage = subBufferedImage.createGraphics();

        for (int row = 0; row < map.getRows(); row++) {
            /*
            System.out.println();
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
            System.out.println("Row = " + row);
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
            System.out.println();
            */
            for (int column = 0; column < map.getColumns(); column++) {
                textureID = map.getTeilsTextureID(row, column);
                /*
                System.out.println("textureID = " + textureID);
                System.out.println("Column = " + column);
                System.out.println("(textureID % TEILS_ON_EACH_LINE) * teilWidth = "
                        + (textureID % TEILS_ON_EACH_LINE) * teilWidth);
                System.out.println("(textureID / TEILS_ON_EACH_LINE) * teilHeight = "
                        + (textureID / TEILS_ON_EACH_LINE) * teilHeight);
                */
                subBufferedImage = mapSpreadedSpreadSheet.grabImage(
                        (textureID % TEILS_ON_EACH_LINE) * teilWidth,
                        (textureID / TEILS_ON_EACH_LINE) * teilHeight,
                        teilWidth, teilHeight, 0);
                //subImage = (Graphics2D) subBufferedImage.getGraphics();
                mapImage.getGraphics().drawImage(subBufferedImage, row * 64,
                        column * 64, teilWidth, teilHeight, null);
                //System.out.println("--------------------------------------------------");
            }
        }
        if (GUI.getInstance() != null) {
            GUI.getInstance().map = SwingFXUtils.toFXImage(mapImage, null);
        } else {

            try {
                LOGGER.debug("Versuche das Bild zu speichen!");
                //File newDirectory2 = new File(Paths.get("images").toString());
                //File newDirectory = new File(Paths.get("src/main/resources").toString());
                //if (!newDirectory.exists()) {
                //    newDirectory.mkdirs();
                //}
                //outFile = new File(Paths.get("src/main/resources/mapImage.png").toString());
                //outFile = new File(Paths.get("images/mapImage.png").toString());
                //GUI.getInstance().map = SwingFXUtils.toFXImage(mapImage, null);
                ImageIO.write(mapImage, "png", new File("mapImageTest.png"));
                //ImageIO.write(mapImage, "png", outFile);
            } catch (IOException e) {
                LOGGER.error("Fehler beim Schreiben des Bildes / der Bilder");
                LOGGER.error(e.getStackTrace());
        }
        }
    }

    /**
     * A Main Methode to manualy create a MapIMage.png.
     *
     * @param args The Args like Args you see every where!!!
     */
    public static void main(String[] args) {
        //testMap();
        hiddenInForrestMap();
    }

    /**
     * Just a debugmethod witch generades manualy a Map.
     */
    public static void testMap() {
        //System.out.println("Started");
        MapMatrix testMap = new MapMatrix("TestWelt", 40);
        testMap.spawnMaterial(20);
        //System.out.println("MapMatrix creaded");
        MapImageCreator ueli = new MapImageCreator();
        //System.out.println("MapImageCreator creaded");
        ueli.createMapImage(testMap);
        //System.out.println("Map übergeben und bild erstellt");
        //System.out.println("fertig");
    }

    /**
     * Manually create a hiddenInForestMap on demand
     */
    public static void hiddenInForrestMap() {
        MapMatrix hiddenInForrestMap = new MapMatrix("hiddenInForrestMap", 60 );
        //System.out.println("MapMatrix creadet.");
        hiddenInForrestMap.spawnMaterial(40);
        //System.out.println("Materials spawned!");
        MapImageCreator ueli = new MapImageCreator();
        //System.out.println("MapImageCreator creadet.");
        ueli.createMapImage(hiddenInForrestMap);
    }
}
