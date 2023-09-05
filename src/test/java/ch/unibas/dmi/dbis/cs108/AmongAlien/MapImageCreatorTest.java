package ch.unibas.dmi.dbis.cs108.AmongAlien;

import ch.unibas.dmi.dbis.cs108.AmongAlien.client.BufferedImageLoader;
import ch.unibas.dmi.dbis.cs108.AmongAlien.client.MapImageCreator;
import ch.unibas.dmi.dbis.cs108.AmongAlien.client.SpriteSheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;

/**
 * This is a Junit Test Class to test most of the MapImageCreator Class
 * It is a Core part of our Game and without the Map being created
 * successfully the Game can't run
 *
 */
class MapImageCreatorTest {
    @Mock
    BufferedImageLoader hansUeli;
    @Mock
    SpriteSheet mapSpreadedSpreadSheet;

    /**
     * This method is executed before each test.
     * It initializes the @Mock statements in this Class
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * This Method opens the hiddenInforestMap method which
     * is used to manually create a Map of the Game
     * It passes if no exceptions occur
     */
    @Test
    void MainMethodOfMapImageCreator() {
        Assertions.assertNull(assertDoesNotThrow(() -> hansUeli.loadImage(anyString())));
        Assertions.assertNull(mapSpreadedSpreadSheet.grabImage(anyInt(), anyInt(), anyInt(), anyInt(), anyInt()));
        assertDoesNotThrow(() -> MapImageCreator.main(new String[]{"args"}));
    }

    /**
     * This Method opens the hiddenInforestMap method which
     * is used to create, save and output a .png file of the
     * Test Map of the Game. It passes if no exceptions occur
     */
    @Test
    void TestMap() {
        Assertions.assertNull(assertDoesNotThrow(() -> hansUeli.loadImage(anyString())));
        Assertions.assertNull(mapSpreadedSpreadSheet.grabImage(anyInt(), anyInt(), anyInt(), anyInt(), anyInt()));
        assertDoesNotThrow(MapImageCreator::testMap);
    }
}