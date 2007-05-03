/*
 * GeoeffnetesBild.java
 *
 * Created on 3. Mai 2007, 12:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package core;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author Nils
 */
public class GeoeffnetesBild {
  
  private File datei;
  private BufferedImage bild;
  private String format;
  
  /** Creates a new instance of GeoeffnetesBild */
  public GeoeffnetesBild(File datei) {
    this.datei = datei;
  }
  
  public void schliesze() {
    datei = null;
  }
  
  public File getDatei() {
    return datei;
  }
  
  public BufferedImage getBild() {
    return bild;
  }

  public String getFormat() {
    return format;
  }
}
