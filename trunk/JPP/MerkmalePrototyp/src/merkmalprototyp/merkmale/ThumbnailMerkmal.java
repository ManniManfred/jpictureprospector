/*
 * DateinameMerkmal.java
 *
 * Created on 20. April 2007, 12:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package merkmalprototyp.merkmale;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import merkmalprototyp.Merkmal;

/**
 *
 * @author Besitzer
 */
public class ThumbnailMerkmal implements Merkmal {
  
  private String name;
  private Object wert;
  
  // Maximale Höhe und Breite des Thumbnails
  private static final int MAX_HOEHE = 100;
  private static final int MAX_BREITE = 100;
  
  /** Creates a new instance of DateinameMerkmal */
  public ThumbnailMerkmal() {
    this.name = "Thumbnail";
  }
  
  public void liesMerkmal(File datei){
    Image thumbnail = null;
    BufferedImage bild;
    
    // Bild einlesen
    try {
      bild = ImageIO.read(datei);
      if (bild.getWidth() > bild.getHeight()) {
	
      /* Bild ist im Querformat, Thumbnail bekommt maximale Breite, Höhe
       * wird proportional zur Breite berechnet
       */
	thumbnail = bild.getScaledInstance(MAX_BREITE, -1, bild.SCALE_DEFAULT);
      } else {
	
      /* Bild ist im Hochformat, Thumbnail bekommt maximale Höhe, Breite
       * wird proportional zur Breite berechnet
       */
	thumbnail = bild.getScaledInstance(-1, MAX_HOEHE, bild.SCALE_DEFAULT);
      }
      
      this.wert = new ImageIcon(thumbnail);
      
    } catch (IOException ex) {
      // Fehler: Datei keine Bilddatei      
//      ex.printStackTrace();
      this.wert = null;
    }
    
    
    
  }

public Object getWert() {
  return this.wert;
}

public void setWert(Object wert) {
  this.wert = wert;
}

public String getName() {
  return this.name;
}
}
