package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import ch.unibas.dmi.dbis.cs108.AmongAlien.gui.GUI;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.util.Objects;


/**
 * This class is responsible for playing the intro
 * video, when a client starts the game and connects to the server
 */
public class VideoPlayer {

    /**
     * Creates new MediaPlayer object
     *
     * @return MediaPlayer that contains the intro video
     * @throws URISyntaxException If URI is loaded incorrectly
     */
    public static MediaPlayer getTrailer() throws URISyntaxException {
        Media media = new Media(Objects
                .requireNonNull(GUI.class.getClassLoader().getResource("Intro.mp4"))
                .toURI().toString());
        return new MediaPlayer(media);
    }
}

