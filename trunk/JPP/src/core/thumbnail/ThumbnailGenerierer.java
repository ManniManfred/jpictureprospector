package core.thumbnail;

import java.awt.image.BufferedImage;
import core.GeoeffnetesBild;

/**
 * Objekte, die dieses Interface implementieren, koennen Thumbnail aus einem
 * BufferedImage in einer bestimmten Groesse erzeugen.
 * @author Manfred Rosskamp
 */
public interface ThumbnailGenerierer {
  
  /**
   * Erzeugt ein neues Thumbnail aus dem uebergebenem geoeffneten Bild 
   * <code>bild</code>.
   * @param bild  Bild, aus dem das Thumbnail erzeugt wird
   * @param maxWidth  maximale Breite des zu erzeugenden Thumbnails
   * @param maxHeight  maximale Hoehe des zu erzeugenden Thumbnails
   * @return das erzeugte Thumbnail
   */
  BufferedImage generiereThumbnail(GeoeffnetesBild bild, int maxBreite, 
      int maxHoehe); 
}
