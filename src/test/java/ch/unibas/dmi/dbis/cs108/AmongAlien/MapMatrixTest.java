package ch.unibas.dmi.dbis.cs108.AmongAlien;

import ch.unibas.dmi.dbis.cs108.AmongAlien.tools.MapMatrix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a Junit Test Class for the MapMatrix Class to check if the Mathods work as expected.
 *
 * @author Joel Erbsland
 * @version 01.05.2022
 */
public class MapMatrixTest {

    /**
     * Test to check that the MapMatrix's changeMapTeils Method changes the TextureID correctly.
     * Therefore the TextureIDs of the middle diagonal will be changed and it will be checked if
     * only this MapTeils get a new TextureID.
     */
    @Test
    public void mapMatrixChangesOnlyWhishedTextureID() {
        MapMatrix testing = new MapMatrix(4, 4);
        for (int i = 0; i < 4; i++) {
            testing.changeMapTeils(i, i, 1, 1);
        }
        assertEquals(1, testing.getTeilsTextureID(0,0));
        assertEquals(1, testing.getTeilsTextureID(1,1));
        assertEquals(1, testing.getTeilsTextureID(2,2));
        assertEquals(1, testing.getTeilsTextureID(3,3));

        assertEquals(0, testing.getTeilsTextureID(1,0));
        assertEquals(0, testing.getTeilsTextureID(2,0));
        assertEquals(0, testing.getTeilsTextureID(3,0));
        assertEquals(0, testing.getTeilsTextureID(0,1));
        assertEquals(0, testing.getTeilsTextureID(2,1));
        assertEquals(0, testing.getTeilsTextureID(3,1));
        assertEquals(0, testing.getTeilsTextureID(0,2));
        assertEquals(0, testing.getTeilsTextureID(1,2));
        assertEquals(0, testing.getTeilsTextureID(3,2));
        assertEquals(0, testing.getTeilsTextureID(0,3));
        assertEquals(0, testing.getTeilsTextureID(1,3));
        assertEquals(0, testing.getTeilsTextureID(2,3));
        //cahnged from 38 to 0 expected
    }

    /**
     * Test to check that the MapMatrix's changeMapTeils Method changes the FieldUSE correctly.
     * Therefore the FieldUSEs of the middle diagonal will be changed and it will be checked if
     * only this MapTeils get a new FieldUSE.
     */
    @Test
    public void mapMatrixChangesOnlyWhishedFieldUSE() {
        MapMatrix testing = new MapMatrix(4, 4);
        for (int i = 0; i < 4; i++) {
            testing.changeMapTeils(i, i, 1, 1);
        }
        assertEquals(1, testing.getTeilsfieldUSE(0,0));
        assertEquals(1, testing.getTeilsfieldUSE(1,1));
        assertEquals(1, testing.getTeilsfieldUSE(2,2));
        assertEquals(1, testing.getTeilsfieldUSE(3,3));

        assertEquals(0, testing.getTeilsfieldUSE(1,0));
        assertEquals(0, testing.getTeilsfieldUSE(2,0));
        assertEquals(0, testing.getTeilsfieldUSE(3,0));
        assertEquals(0, testing.getTeilsfieldUSE(0,1));
        assertEquals(0, testing.getTeilsfieldUSE(2,1));
        assertEquals(0, testing.getTeilsfieldUSE(3,1));
        assertEquals(0, testing.getTeilsfieldUSE(0,2));
        assertEquals(0, testing.getTeilsfieldUSE(1,2));
        assertEquals(0, testing.getTeilsfieldUSE(3,2));
        assertEquals(0, testing.getTeilsfieldUSE(0,3));
        assertEquals(0, testing.getTeilsfieldUSE(1,3));
        assertEquals(0, testing.getTeilsfieldUSE(2,3));
    }

    /**
     * Test to check if a MapTeil with no Task gets a Task filled with zeros.
     */
    @Test
    public void mapMatrixDoesGetRightTaskAndFillsNoTasksAutomaticallyWitZeros() {
        MapMatrix testing = new MapMatrix(4, 4);
        for (int i = 0; i < 6; i++) {
            assertEquals(0, (testing.getTeilsTask(1, 1)[i]));
            assertEquals(0, (testing.getTeilsTask(2, 2)[i]));
            assertEquals(0, (testing.getTeilsTask(1, 3)[i]));
            assertEquals(0, (testing.getTeilsTask(3, 1)[i]));

        }
    }

    /**
     * Test to check if the changeMapTeils Method works correctly and if a changed
     * MapTeil has the right new values.
     */
    @Test
    public void mapMatrixDoesChangeMapTeilsCorrectly() {
        MapMatrix testing = new MapMatrix(4, 4);

        testing.changeMapTeils(0, 0, 10,2);
        assertEquals(10, testing.getTeilsTextureID(0, 0));
        assertEquals(2, testing.getTeilsfieldUSE(0, 0));

        testing.changeMapTeils(2, 3, 8,3);
        assertEquals(8, testing.getTeilsTextureID(2, 3));
        assertEquals(3, testing.getTeilsfieldUSE(2, 3));

        testing.changeMapTeils(3, 3, 89,5);
        assertEquals(89, testing.getTeilsTextureID(3, 3));
        assertEquals(5, testing.getTeilsfieldUSE(3, 3));
    }

    /**
     * Test to check if the the MapMatrix does return the right dimentions
     * for the second construktor.
     */
    @Test
    public void mapMatrixReturnsRightDimentionsForSecondConstruktor() {
        MapMatrix testing = new MapMatrix(4, 4);
        assertEquals(64 * 4, testing.getWidth());
        assertEquals(64 * 4, testing.getHeight());
        assertEquals(4, testing.getRows());
        assertEquals(4, testing.getColumns());
        assertEquals(64, testing.getPixelsForEachTeil());

        assertEquals(0, testing.getTeilsTextureID(0, 0));
        assertEquals(0, testing.getTeilsTextureID(0, 1));
        assertEquals(0, testing.getTeilsTextureID(0, 2));
        assertEquals(0, testing.getTeilsTextureID(0, 3));
        assertEquals(0, testing.getTeilsTextureID(1, 0));
        assertEquals(0, testing.getTeilsTextureID(1, 1));
        assertEquals(0, testing.getTeilsTextureID(1, 2));
        assertEquals(0, testing.getTeilsTextureID(1, 3));
        assertEquals(0, testing.getTeilsTextureID(2, 0));
        assertEquals(0, testing.getTeilsTextureID(2, 1));
        assertEquals(0, testing.getTeilsTextureID(2, 2));
        assertEquals(0, testing.getTeilsTextureID(2, 3));
        assertEquals(0, testing.getTeilsTextureID(3, 0));
        assertEquals(0, testing.getTeilsTextureID(3, 1));
        assertEquals(0, testing.getTeilsTextureID(3, 2));
        assertEquals(0, testing.getTeilsTextureID(3, 3));

        assertEquals(0, testing.getTeilsfieldUSE(0, 0));
        assertEquals(0, testing.getTeilsfieldUSE(0, 1));
        assertEquals(0, testing.getTeilsfieldUSE(0, 2));
        assertEquals(0, testing.getTeilsfieldUSE(0, 3));
        assertEquals(0, testing.getTeilsfieldUSE(1, 0));
        assertEquals(0, testing.getTeilsfieldUSE(1, 1));
        assertEquals(0, testing.getTeilsfieldUSE(1, 2));
        assertEquals(0, testing.getTeilsfieldUSE(1, 3));
        assertEquals(0, testing.getTeilsfieldUSE(2, 0));
        assertEquals(0, testing.getTeilsfieldUSE(2, 1));
        assertEquals(0, testing.getTeilsfieldUSE(2, 2));
        assertEquals(0, testing.getTeilsfieldUSE(2, 3));
        assertEquals(0, testing.getTeilsfieldUSE(3, 0));
        assertEquals(0, testing.getTeilsfieldUSE(3, 1));
        assertEquals(0, testing.getTeilsfieldUSE(3, 2));
        assertEquals(0, testing.getTeilsfieldUSE(3, 3));

    }

    /**
     * Test to check if the the MapMatrix does return the right dimentions
     * for the second construktor.
     */
    @Test
    public void mapMatrixReturnsRightDimentionsForFirstConstruktor() {
        MapMatrix testing = new MapMatrix();
        assertEquals(0, testing.getWidth());
        assertEquals(0, testing.getHeight());
        assertEquals(0, testing.getRows());
        assertEquals(0, testing.getColumns());
        assertEquals(64, testing.getPixelsForEachTeil());
    }
}
