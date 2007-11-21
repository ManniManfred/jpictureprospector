package jpp.core;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

/**
 * Ein Objekt dieser Klasse stellt ein geoffnetes Bild dar.
 * 
 * @author Manfred Rosskamp
 */
public class GeoeffnetesBild {

  /** Enthaelt die URL zu diesem geoeffnentem Bild. */
  private URL url;
  
  /** Enthaelt das tatsaechlich geoffnete Bild in Form eines BufferedImage. */
  private BufferedImage bild;

  /**
   * Enthaelt das Bildformat dieses Bildes in Kleinbuchstaben. Z.B. "jpg", "png"
   * oder "gif"
   */
  private String format;

  /**
   * Enthaelt eine Verbindung zur Bilddatei.
   */
  private URLConnection connection;

  /**
   * Erzeugt ein neues geoeffnetes Bild.
   * 
   * @param bilddatei Bilddatei, welche geoeffnet wird
   * @throws IOException wenn die Datei nicht gelesen werden konnte
   */
  public GeoeffnetesBild(URL bilddatei) throws IOException {
    this.url = bilddatei;
    this.connection = url.openConnection();
    
    /* Bild oeffnen */
    bild = ImageIO.read(this.url);

    if (bild == null) {
      throw new IOException("Konnte das Bild \"" + bilddatei.toString()
          + "\" nicht einlesen.");
    }

    /* Format aus dem Dateinamen bestimmen */
    String dateiname = bilddatei.getFile();
    format = dateiname.substring(dateiname.lastIndexOf(".") + 1).toLowerCase();

  }
  

  
  /**
   * Gibt die Datei dieses geoeffenten Bildes zurueck.
   * 
   * @return Datei dieses geoeffenten Bildes
   */
  public URL getURL() {
    return this.url;
  }
  
  public URLConnection getURLConnection() {
    return connection;
  }
  
  /**
   * Gibt das BufferedImage dieses geoeffnenten Bildes zurueck.
   * 
   * @return das tatsaechlich geoffnete Bild in Form eines BufferedImage
   */
  public BufferedImage getBild() {
    return bild;
  }

  /**
   * Gibt das Format dieses Bildes in Kleinbuchstaben z.B. "jpg", "png" oder
   * "gif" zurueck.
   * 
   * @return Format dieses Bildes in Kleinbuchstaben
   */
  public String getFormat() {
    return format;
  }

}
