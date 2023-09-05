package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GUI;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.util.Objects;


/**
 * This class is responsible for playing soakUp animation
 * when a player gets soaked up before voting
 *
 * @author Rayes Diyab
 * @version 2022.04.10
 */
public class SoakUp {

    /**
     * Creates new MediaPlayer
     *
     * @return MediaPlayer that contains soakUp video
     * @throws URISyntaxException If URI is loaded incorrectly
     */
    public static MediaPlayer getTrailer() throws URISyntaxException {
        Media media = new Media(Objects
                .requireNonNull(GUI.class.getClassLoader().getResource("sogUpVideo.mp4"))
                .toURI().toString());
        return new MediaPlayer(media);
    }
}
