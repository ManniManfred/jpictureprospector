/*
 * Merkmal.java
 *
 * Created on 3. Mai 2007, 12:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package core;

/**
 *
 * @author Nils
 */
public abstract class Merkmal {
  
  private String name;
  
  private Object wert;
  
  public Merkmal(String name) {
    
  }

  public String getName() {
    return name;
  }

  public Object getWert() {
    return wert;
  }

  public void setWert(Object wert) {
    this.wert = wert;
  }
  
  public abstract void leseMerkmalAus(GeoeffnetesBild bild);
  public abstract Field erzeugeLuceneField();
  public abstract void leseMerkmalAusLuceneDocument(Document doc);
  public abstract boolean istEditierbar();
}
