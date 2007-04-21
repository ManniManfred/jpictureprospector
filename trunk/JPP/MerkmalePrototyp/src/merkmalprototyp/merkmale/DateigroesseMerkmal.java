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
public class DateigroesseMerkmal implements Merkmal {
  
  private String name;
  private Object wert;
  
  
  /** Creates a new instance of DateinameMerkmal */
  public DateigroesseMerkmal() {
    this.name = "Dateigroesse";
  }
  
  public void liesMerkmal(File datei){
    this.wert =  (double) (datei.length() / 1024);
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
