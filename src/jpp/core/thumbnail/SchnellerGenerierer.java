package jpp.core.thumbnail;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import jpp.core.GeoeffnetesBild;
import jpp.core.exceptions.GeneriereException;


/**
 * Dieser ThumbnailGenerierer ist schneller als der AlleFormateGenerierer kann
 * aber leider nur von den Standardbildformaten also JPG, GIF und PNG ein
 * Thumbnail erzeugen.
 * 
 * @author Manfred Rosskamp
 */
public class SchnellerGenerierer implements ThumbnailGenerierer {
  
  /**
   * Erzeugt ein neues Thumbnail aus dem uebergebenem geoeffneten Bild
   * <code>bild</code>.
   * 
   * @param bild Bild, aus dem das Thumbnail erzeugt wird
   * @param maxBreite maximale Breite des zu erzeugenden Thumbnails
   * @param maxHoehe maximale Hoehe des zu erzeugenden Thumbnails
   * @return das erzeugte Thumbnail
   * @throws GeneriereException wird geworfen, falls das Thumbnail nicht
   *           generiert werden konnte
   */
  public BufferedImage generiereThumbnail(GeoeffnetesBild bild, int maxBreite,
      int maxHoehe) throws GeneriereException {

    Image image = Toolkit.getDefaultToolkit().getImage(
        bild.getDatei().getAbsolutePath());

    MediaTracker mediaTracker = new MediaTracker(new Container());
    mediaTracker.addImage(image, 0);

    try {
      mediaTracker.waitForID(0);
    } catch (InterruptedException e) {
      throw new GeneriereException(
          "Der Mediatracker wurde beim laden unterbrochen.", e);
    }
    
    
    /*
     * richtige breite und hoehe des Thumbnails berechnen, sodass das
     * Verhaeltnis noch passt.
     */
    int thumbWidth = maxBreite;
    int thumbHeight = maxHoehe;
    double thumbRatio = (double) thumbWidth / (double) thumbHeight;
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
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

    g.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);


    return thumbImage;
  }

}
