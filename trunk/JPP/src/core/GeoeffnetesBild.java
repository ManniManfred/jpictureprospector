package core;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Ein Objekt dieser Klasse stellt ein geoffnetes Bild dar. 
 * @author Manfred Rosskamp
 */
public class GeoeffnetesBild {
  
  /** Enthaelt die Datei zu diesem geoeffnentem Bild. */
  private File datei;
  
  /** Enthaelt das tatsaechlich geoffnete Bild in Form eines BufferedImage. */
  private BufferedImage bild;
  
  /** Enthaelt das Bildformat dieses Bildes in Kleinbuchstaben.
   * Z.B. "jpg", "png" oder "gif" 
   */
  private String format;
  
  
  /**
   * Erzeugt ein neues geoeffnetes Bild.
   * @param datei
   * @throws IOException wenn die Datei nicht gelesen werden konnte
   */
  public GeoeffnetesBild(File datei) throws IOException {
    this.datei = datei;
    
    /* Bild oeffnen */
    bild = ImageIO.read(datei);
    
    if (bild == null) {
      throw new IOException("Konnte das Bild \"" + datei.getAbsolutePath() 
          + " nicht einlesen.");
    }
    
    /* Format aus dem Dateinamen bestimmen */
    String dateiname = datei.getName();
    format = dateiname.substring(dateiname.lastIndexOf(".") + 1).toLowerCase();
    
  }
  
//  Wird nicht benoetigt, da das BufferedImage bild auch nicht geschlossen
//  werden muss.
//  
//  /**
//   * Schlieszt dieses geoeffnete Bild. Nach diesem Methodenaufruf, sollte dieses
//   * Objekt nicht weiter verwendet werden.
//   */
//  public void schliesze() {
//    
//  }
  
  
  /**
   * Gibt die Datei dieses geoeffenten Bildes zurueck.
   * @return Datei dieses geoeffenten Bildes
   */
  public File getDatei() {
    return datei;
  }
  
  /**
   * Gibt das BufferedImage dieses geoeffnenten Bildes zurueck.
   * @return das tatsaechlich geoffnete Bild in Form eines BufferedImage
   */
  public BufferedImage getBild() {
    return bild;
  }

  /**
   * Gibt das Format dieses Bildes in Kleinbuchstaben
   * z.B. "jpg", "png" oder "gif" zurueck.
   * 
   * @return Format dieses Bildes in Kleinbuchstaben
   */
  public String getFormat() {
    return format;
  }

}
