

package merkmale;

import core.GeoeffnetesBild;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


/**
 * Ein Objekt dieser Klasse stellt das Merkmal Bildhoehe eines 
 * Bilddokumentes dar.
 * @author Marion Mecking
 */
public class BildhoeheMerkmal extends Merkmal {   
    
  /** Name des Lucene-Feldes für dieses Merkmal. */
  private static final String FELDNAME = "Bildhoehe";
  
  /** 
   * Erzeugt ein neues BildhoeheMerkmal.
   */
  public BildhoeheMerkmal() {
    super(FELDNAME);
  } 
  
  /**
   * Liest die Bildbreite dieses Merkmals aus dem uebergebenen geoeffneten
   * Bild und speichert diesen in diesem Merkmal-Objekt.
   * @param bild  Bild, aus dem der Merkmalswert gelesen wird
   */
  public void leseMerkmalAus(GeoeffnetesBild bild) {
    this.wert = bild.getBild().getHeight() + "";
  }

  /**
   * Liest den Merkmalswert aus einem Lucene-Document und speichert diesen in 
   * diesem Merkmal-Objekt.
   * @param doc  Lucene-Document, von dem ueber ein zu diesem Merkmal 
   *    gehoerigen Field der Wert ausgelesen wird
   */
  public void leseMerkmalAusLuceneDocument(Document doc) {   
    this.wert = doc.get(FELDNAME);  
  }
   
  /**
   * Erzeugt aus diesem Merkmal ein entsprechendes Lucene-Field.
   * @return ein entsprechendes Lucene-Field
   */
  public Field erzeugeLuceneField() {
    return new Field(FELDNAME, this.getWert().toString(), Field.Store.YES, 
      Field.Index.UN_TOKENIZED);
  }
  
  /**
   * Gibt zurueck, ob dieses Merkmal vom Endbenutzer editierbar sein kann oder
   * ob ein sich nicht aenderndes festes Merkmal ist.
   * @return <code>true</code>, wenn dieses Merkmal editierbar sein soll
   */
  public boolean istEditierbar() {
    return false; 
  }  
}
