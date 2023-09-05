package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The BufferedImageLoader takes a Path an loads the
 * Image in the Background.
 *
 * @author Joel Erbsland
 * @version 2021.05.10
 */
public class BufferedImageLoader {
    private BufferedImage image;

    /**
     * The loadImage Method will load the image at the given path in
     * the background (RAM)
     *
     * @param path The images path
     * @return the BufferedImage form the image at the given path.
     * @throws IOException if loadImage failed
     */
    public BufferedImage loadImage(String path) throws IOException {
        //System.out.println(BufferedImageLoader.class.getResource("/MapTex.png"));
        image = ImageIO.read(BufferedImageLoader.class.getResource(path));
        return image;
    }
}
