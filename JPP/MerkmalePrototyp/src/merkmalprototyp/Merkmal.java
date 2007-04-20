/*
 * Merkmal.java
 *
 * Created on 20. April 2007, 12:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package merkmalprototyp;

import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Besitzer
 */
public interface Merkmal {
  
  
  public abstract void liesMerkmal(File datei);
  
  public abstract Object getWert();
  
  public abstract void setWert(Object wert);
  
  public abstract String getName();
  
}
