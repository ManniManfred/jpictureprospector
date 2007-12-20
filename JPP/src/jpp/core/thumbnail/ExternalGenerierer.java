package jpp.core.thumbnail;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jpp.core.GeoeffnetesBild;
import jpp.core.exceptions.GeneriereException;


/**
 * Ein Objekt dieser Klasse repraesentiert ein ThumbnailGenerierer, welcher
 * ein externes Programm dazu verwendet.
 * 
 * @author Manfred Rosskamp
 */
public class ExternalGenerierer implements ThumbnailGenerierer {

  private static final String syntax = "convert %orig% -resize %breite%x%hoehe% %thumb%";
  
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
    
    String command = syntax.replaceAll("%orig%", "" + bild.getURL().getPath());
    command = command.replaceAll("%thumb%", "" + "/dev/stdout");
    command = command.replaceAll("%breite%", "" + maxBreite);
    command = command.replaceAll("%hoehe%", "" + maxHoehe);
    
    BufferedImage image;
    try {
      ProcessBuilder builder = new ProcessBuilder(command.split(" "));
      Process p = builder.start();
      
      image = ImageIO.read(p.getInputStream());
    } catch (IOException e) {
      throw new GeneriereException("Konnte das Thumbnail nicht generieren.", e);
    }
    
    return image;
  }

}
