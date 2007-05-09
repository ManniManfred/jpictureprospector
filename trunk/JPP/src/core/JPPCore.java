
package core;

import java.io.File;
import java.io.IOException;

/**
 * Ein Objekt dieser Klasse stellt die Hauptaufgaben dieser JPictureProspector
 * Anwendung bereit.
 * @author Manfred Rosskamp
 */
public class JPPCore {
  
  /** 
   * Erstellt ein neues JPPCore-Objekt.
   */
  public JPPCore() {
  }
  
  /**
   * Importiert eine Bilddatei in diese Anwendung.
   * 
   * @param datei  Bilddatei, die importiert werden soll
   * @return die importierte Bilddatei als BildDokument 
   */
  public BildDokument importiere(File datei) throws IOException {
    BildDokument dokument = BildDokument.erzeugeAusDatei(datei);
    
    /* BildDokument Lucene hinzufuegen */
    //Document doc = dokument.erzeugeLuceneDocument();
    //...
    
    return dokument;
  }
  
  /**
   * Suche in allen importierten Bilder nach dem Suchtext und gibt eine 
   * entsprechende Trefferliste mit den Suchergebnissen zurueck.
   * 
   * @param suchtext  Suchtext, nach dem gesucht wird
   * @return Trefferliste mit den Suchergebnissen
   */
  public Trefferliste suche(String suchtext) {
    /* TODO wirkliche Suche starten */
    return new Trefferliste();
  }
  
  
  /**
   * Uebernimmt die Aenderungen, die an dem BildDokument gemacht wurden, wie
   * z.B. das Hinzufuegen von Schluesselwoerter.
   * 
   * @param bild  Bilddokument, von dem die Aenderungen uebernommen werden
   */
  public void aendere(BildDokument bild) {
    /* TODO bilddokument wirklich aendern. */
  }
  
  
  /**
   * Entfernt das uebergebene Bild aus dieser Anwendung und, wenn <code>
   * auchVonFestplatte</code> gesetzt ist, dann auch von der Festplatte.
   * @param bild  BildDokument, welches entfernt werden soll
   * @param auchVonFestplatte  gibt an, ob das Bild auch von der Festplatte
   *      entfernt werden soll
   */
  public void entferne(BildDokument bild, boolean auchVonFestplatte) {
    /* TODO bilddokument aus dieser Anwendung entfernen und evtl. auch von
     * der Festplatte
     */
    
    
//    /* Evtl. Bilddatei von der Festplatte entfernen. */
//    if (auchVonFestplatte) {
//    }
  }
  
}
