package core.thumbnail;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import core.GeoeffnetesBild;

public class AlleFormateGenerierer implements ThumbnailGenerierer {

  public BufferedImage generiereThumbnail(GeoeffnetesBild bild, int maxBreite,
      int maxHoehe) {
    
    /* BufferedImage aus dem geoeffnetem Bild holen */
    BufferedImage original = bild.getBild();
        
    /* richtige breite und hoehe des Thumbnails berechnen, sodass das 
     * Verhaeltnis noch passt.
     */
    int thumbWidth = maxBreite;
    int thumbHeight = maxHoehe;
    double thumbRatio = (double)thumbWidth / (double)thumbHeight;
    int imageWidth = original.getWidth();
    int imageHeight = original.getHeight();
    double imageRatio = (double)imageWidth / (double)imageHeight;
    if (thumbRatio < imageRatio) {
      thumbHeight = (int)(thumbWidth / imageRatio);
    } else {
      thumbWidth = (int)(thumbHeight * imageRatio);
    }
    
    /* BufferedImage fuer das Thumbnail erzeugen */
    BufferedImage thumbImage = new BufferedImage(thumbWidth, 
        thumbHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = thumbImage.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    
    /* Bild ins Thumbnail zeichnen */
    g.drawImage(original, 0, 0, thumbWidth, thumbHeight, null);
    
    g.dispose();
    
    return thumbImage;
  }

}
