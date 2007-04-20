/*
 * DateinameMerkmal.java
 *
 * Created on 20. April 2007, 12:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package merkmalprototyp.merkmale;

import java.io.File;
import java.util.Date;
import merkmalprototyp.Merkmal;

/**
 *
 * @author Besitzer
 */
public class LetzterZugriffMerkmal implements Merkmal {
  
  private String name;
  private Object wert;  
  
  /** Creates a new instance of DateinameMerkmal */
  public LetzterZugriffMerkmal() {
    this.name = "Letzter Zugriff";
  }
  
  public void liesMerkmal(File datei){
    this.wert = new Date(datei.lastModified());
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
