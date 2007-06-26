package jpp.core.thumbnail;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import jpp.core.GeoeffnetesBild;


/**
 * Ein Objekt dieses Klasse stellt einen Thumbnailgenerierer fuer alle Formaten
 * dar.
 * 
 * @author Manfred Rosskamp
 */
public class AlleFormateGenerierer implements ThumbnailGenerierer {

  /**
   * Erzeugt ein neues Thumbnail aus dem uebergebenem geoeffneten Bild
   * <code>bild</code>.
   * 
   * @param bild Bild, aus dem das Thumbnail erzeugt wird
   * @param maxBreite maximale Breite des zu erzeugenden Thumbnails
   * @param maxHoehe maximale Hoehe des zu erzeugenden Thumbnails
   * @return das erzeugte Thumbnail
   */
  public BufferedImage generiereThumbnail(GeoeffnetesBild bild, int maxBreite,
      int maxHoehe) {

    /* BufferedImage aus dem geoeffnetem Bild holen */
    BufferedImage original = bild.getBild();

    /*
     * richtige breite und hoehe des Thumbnails berechnen, sodass das
     * Verhaeltnis noch passt.
     */
    int thumbWidth = maxBreite;
    int thumbHeight = maxHoehe;
    double thumbRatio = (double) thumbWidth / (double) thumbHeight;
    int imageWidth = original.getWidth();
    int imageHeight = original.getHeight();
    double imageRatio = (double) imageWidth / (double) imageHeight;
    if (thumbRatio < imageRatio) {
      thumbHeight = (int) (thumbWidth / imageRatio);
    } else {
      thumbWidth = (int) (thumbHeight * imageRatio);
    }

    /* BufferedImage fuer das Thumbnail erzeugen */
    BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g = thumbImage.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    /* Bild ins Thumbnail zeichnen */
    g.drawImage(original, 0, 0, thumbWidth, thumbHeight, null);

    g.dispose();

    return thumbImage;
  }

}
