package jpp.merkmale;

import java.net.MalformedURLException;
import java.net.URL;

import jpp.core.GeoeffnetesBild;
import jpp.core.exceptions.LeseMerkmalAusException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


/**
 * Ein Objekt dieser Klasse stellt das Merkmal Dateipfad eines 
 * Bilddokumentes in Form einer URL dar. Z.B. "file:///E:/bilder/bla.jpg" oder
 * "http://www.bilder.de/bla.jpg"
 * 
 * @author Marion Mecking
 */
public class DateipfadMerkmal extends Merkmal { 

  /** Name des Lucene-Feldes fuer dieses Merkmal. */
  public static final String FELDNAME = "Dateipfad";

  /** 
   * Erzeugt ein neues DateipfadMerkmal.
   */
  public DateipfadMerkmal() {
    super(FELDNAME);
  }   
  
  /**
   * Liest den Dateipfad aus dem uebergebenen geoeffneten
   * Bild und speichert diesen in diesem Merkmal-Objekt.
   * @param bild  Bild, aus dem der Merkmalswert gelesen wird
   */
  public void leseMerkmalAus(GeoeffnetesBild bild) {
    this.wert = bild.getURL();
  }

  /**
   * Liest den Merkmalswert aus einem Lucene-Document und speichert diesen in 
   * diesem Merkmal-Objekt.
   * @param doc  Lucene-Document, von dem ueber ein zu diesem Merkmal 
   *    gehoerigen Field der Wert ausgelesen wird
   */
  public void leseMerkmalAusLuceneDocument(Document doc) {   
    try {
      this.wert = new URL(doc.get(FELDNAME));
    } catch (MalformedURLException e) {
      System.out.println("Konnte den Dateipfad nicht in eine URL umwandeln");
      e.printStackTrace();
    }
  }
  
  public void leseMerkmalAusString(String wert) throws LeseMerkmalAusException {
    try {
      this.wert = new URL(wert);
    } catch (MalformedURLException e) {
      throw new LeseMerkmalAusException(
          "Konnte den String \"" + wert + "\" nicht in eine URL umwandeln.", e);
    }
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
