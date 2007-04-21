/*
 * DateinameMerkmal.java
 *
 * Created on 20. April 2007, 12:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package merkmalprototyp.merkmale;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import merkmalprototyp.Merkmal;

/**
 *
 * @author Besitzer
 */
public class BildbreiteMerkmal implements Merkmal {
  
  private String name;
  private Object wert;
  
  /** Creates a new instance of DateinameMerkmal */
  public BildbreiteMerkmal() {
    this.name = "Bildbreite";
  }
  
  public void liesMerkmal(File datei){
    BufferedImage bild;
    
    try {
      bild = ImageIO.read(datei);
      this.wert = bild.getWidth();
    } catch (IOException ex) {
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
