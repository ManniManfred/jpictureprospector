
package merkmale;

import core.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * Ein Objekt dieser Klasse repraesentiert ein Merkmal mit einem Mermalsnamen
 * und einem zu dem Merkmal gehoerenden Wert. 
 * Zum Beispiel Merkmalsname: Bildhoehe; Wert: 300 (Pixel)
 * 
 * @author Nils Verheyen
 * @author Manfred Rosskamp
 * @author Marion Mecking
 */
public abstract class Merkmal extends AlleMerkmale {
  
  /** 
   * Erzeugt ein neues Merkmal mit einem eindeutig identifiezierenden Namen.
   * @param name  Name dieses Merkmals, z.B "Bildhoehe"
   */
  public Merkmal(String name) {
    super(name);
  }

  /**
   * Liest den Merkmalswert dieses Merkmals aus dem uebergebenen geoeffneten
   * Bild und speichert diesen in diesem Merkmal-Objekt.
   * @param bild  Bild, aus dem der Merkmalswert gelesen wird
   */
  public abstract void leseMerkmalAus(GeoeffnetesBild bild);

  /**
   * Liest den Merkmalswert aus einem Lucene-Document und speichert diesen in 
   * diesem Merkmal-Objekt.
   * @param doc  Lucene-Document, von dem ueber ein zu diesem Merkmal 
   *    gehoerigen Field der Wert ausgelesen wird
   */
  public abstract void leseMerkmalAusLuceneDocument(Document doc);
  
  /**
   * Erzeuge aus diesem Merkmal ein entsprechendes Lucene-Field.
   * @return ein entsprechendes Lucene-Field
   */
  public abstract Field erzeugeLuceneField();
  
  /**
   * Gibt zurueck, ob dieses Merkmal vom Endbenutzer editierbar sein kann oder
   * ob ein sich nicht aenderndes festes Merkmal ist.
   * @return <code>true</code>, wenn dieses Merkmal editierbar sein soll
   */
  public abstract boolean istEditierbar();
}
