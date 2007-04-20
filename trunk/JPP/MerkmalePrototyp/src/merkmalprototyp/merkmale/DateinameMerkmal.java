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
import merkmalprototyp.Merkmal;

/**
 *
 * @author Besitzer
 */
public class DateinameMerkmal implements Merkmal {
  
  private String name;
  private Object wert;
  
  /** Creates a new instance of DateinameMerkmal */
  public DateinameMerkmal() {
    this.name = "Dateiname";
  }
  
  public void liesMerkmal(File datei){
    this.wert = datei.getName();
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
