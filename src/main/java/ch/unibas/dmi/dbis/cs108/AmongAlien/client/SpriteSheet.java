package ch.unibas.dmi.dbis.cs108.AmongAlien.client;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * The SpriteSheet class is snipping out a part of a image.
 * The image's raws and column will be counted as 64pixels.
 * The whole image will be loaded in the Background by the
 * BufferedImageLoader
 *
 * @see BufferedImageLoader
 * You just grape the part you will have out of the whole Image.
 *
 * @author Joel Erbsland
 * @version 2021.05.10
 */
public class SpriteSheet {
    private BufferedImage image;

    /**
     * Constructor for the Spritesheet class
     *
     * @param spriteSheet the given BufferedImage
     */
    public SpriteSheet(BufferedImage spriteSheet) {
        this.image = spriteSheet;
    }

    /**
     * This is the Method witch will grape your subimage. You can also rotate.
     *
     * @param column the column the Subimage is located (upper left edge)
     * @param row the row the Subimage is located (upper left edge)
     * @param width amount ofPixel from upper left edge to the upper right edge
     * @param height amount ofPixel from upper left edge to the lower left edge
     * @param rotation the angle the grappled image will be rotated (in Degrees)
     * @return the grappled Subimage as a BufferedImage.
     * @see AffineTransform
     * @see BufferedImage
     */
    public BufferedImage grabImage(int column, int row, int width, int height, int rotation) {
        BufferedImage cuttedImage = image.getSubimage(column, row,
                width, height);
        if (rotation != 0) {
            AffineTransform afftra = new AffineTransform();
            afftra.rotate(Math.toRadians(rotation), cuttedImage.getWidth() / 2,
                    cuttedImage.getHeight() / 2);
            AffineTransformOp afftraOp = new AffineTransformOp(afftra, AffineTransformOp.TYPE_BILINEAR);
            cuttedImage = afftraOp.filter(cuttedImage, null);
        }
        return cuttedImage;
    }
}
