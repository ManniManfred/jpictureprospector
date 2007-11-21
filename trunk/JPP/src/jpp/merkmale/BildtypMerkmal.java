package jpp.merkmale;

import jpp.core.GeoeffnetesBild;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


/**
 * Ein Objekt dieser Klasse stellt das Merkmal Bildtyp eines 
 * Bilddokumentes dar.
 * @author Marion Mecking
 */
public class BildtypMerkmal extends Merkmal { 
    
  /** Name des Lucene-Feldes fuer dieses Merkmal. */
  public static final String FELDNAME = "Bildtyp";

  /** 
   * Erzeugt ein neues BildtypMerkmal.
   */
  public BildtypMerkmal() {
    super(FELDNAME);
  }   
  
  /**
   * Liest die Dateiendung aus dem uebergebenen geoeffneten
   * Bild und speichert diesen in diesem Merkmal-Objekt.
   * @param bild  Bild, aus dem der Merkmalswert gelesen wird
   */
  public void leseMerkmalAus(GeoeffnetesBild bild) {
    this.wert = bild.getFormat();
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
