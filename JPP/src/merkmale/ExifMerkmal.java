
package merkmale;

import core.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * Ein Objekt dieser Klasse repraesentiert ein Exif - Merkmal mit einem 
 * Mermalsnamen und einem zu dem Merkmal gehoerenden Wert. 
 * Exif-Merkmale können nur aus JPG und TIF gewonnen werden und werden nicht
 * in Lucene abgespeichert.
 * 
 * @author Marion Mecking
 */
public class ExifMerkmal {
  
  /** Enthaelt den Namen dieses Merkmals. */
  protected String name;
  
  /** Enthaelt den Wert dieses Merkmals. */
  protected Object wert;
   
  /** 
   * Erzeugt ein neues Merkmal mit einem eindeutig identifiezierenden Namen.
   * @param name  Name dieses Merkmals, z.B "Bildhoehe"
   */
  public ExifMerkmal(String name, String wert) {
    this.name = name;
    this.wert = wert;
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
   * Setzt den Wert dieses Merkmals.
   * @param wert  Wert, auf den dieses Merkmal gesetzt werden soll.
   */
  public void setWert(Object wert) {
    this.wert = wert;
  }
}
