package core.thumbnail;

import java.awt.image.BufferedImage;
import core.GeoeffnetesBild;
import core.exceptions.GeneriereException;

/**
 * Objekte, die dieses Interface implementieren, koennen Thumbnail aus einem
 * BufferedImage in einer bestimmten Groesse erzeugen.
 * 
 * @author Manfred Rosskamp
 */
public interface ThumbnailGenerierer {

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
  BufferedImage generiereThumbnail(GeoeffnetesBild bild, int maxBreite,
      int maxHoehe) throws GeneriereException;
}
