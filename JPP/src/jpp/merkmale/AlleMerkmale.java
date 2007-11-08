
package jpp.merkmale;

import java.io.Serializable;

/**
 * Ein Objekt dieser Klasse repraesentiert ein Exif - Merkmal mit einem 
 * Mermalsnamen und einem zu dem Merkmal gehoerenden Wert. 
 * Exif-Merkmale koennen nur aus JPG und TIF gewonnen werden und werden nicht
 * in Lucene abgespeichert.
 * 
 * @author Marion Mecking
 */
public abstract class AlleMerkmale {
  
  /** Enthaelt den Namen dieses Merkmals. */
  protected String name;
  
  /** Enthaelt den Wert dieses Merkmals. */
  protected Object wert;
   
  /** 
   * Erzeugt ein neues Merkmal mit einem eindeutig identifiezierenden Namen.
   * @param name  Name dieses Merkmals
   */
  public AlleMerkmale (String name) {
    this.name = name;
    this.wert = "";
  }

  /**
   * Gibt den Namen dieses Merkmals, der im Konstruktor gesetzt wurde, zurueck.
   * @return Name dieses Merkmals
   */
  public String getName() {
    return name;
  }
 
  /**
   * Gibt den Wert dieses Merkmals zurueck.
   * @return Wert dieses Merkmals
   */
  public Object getWert() {
    return wert;
  }
  
  /** 
   * Gibt den name dieses Merkmals XML-Komform zurück, d.h. z.B. ohne 
   * Leerzeichen.
   */
  public String getXmlName() {
    return name;
  }
  
  /**
   * Gibt den Wert dieses Merkmals zurueck, der serialisierbar ist. Falls
   * der normale Wert nicht serialisierbar ist, muss diese Methode 
   * ueberschrieben werden.
   * 
   * @return den serialisierbaren Wert dieses Merkmals
   */
  public String getXmlWert() {
    return getWert().toString();
  }
  
  /**
   * Setzt den Wert dieses Merkmals.
   * @param wert  Wert, auf den dieses Merkmal gesetzt werden soll.
   */
  public void setWert(Object wert) {
    this.wert = wert;
  }
  
  /**
   * Ein AlleMerkmale ist gleich, wenn der Name und der Wert des Merkmals gleich
   * sind.
   */
  public boolean equals(Object obj) {
    if (obj instanceof AlleMerkmale) {
      AlleMerkmale m2 = (AlleMerkmale) obj;
      return this.getName().equals(m2.getName()) && this.getWert().equals(m2.getWert());
    }
    return false;
  }
}