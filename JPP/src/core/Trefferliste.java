
package core;

import java.io.IOException;

import org.apache.lucene.search.Hits;

/**
 * Ein Objekt dieser Klasse stellt die Trefferliste sortiert nach der 
 * Treffergenauigkeit einer Suche dar.
 * @author Manfred Rosskamp
 * @author Nils Verheyen
 */
public class Trefferliste {
  
  /** Enthaelt die Trefferliste, der Lucenesuche. */
  private Hits treffer;
  
  /**
   * Erzeugt eine neue Trefferliste
   */
  public Trefferliste(Hits treffer) {
    this.treffer = treffer;
  }
  
  /**
   * Gibt die Anzahl der Treffer zurueck.
   * @return Anzahl der Treffer
   */
  public int getAnzahlTreffer() {
    return treffer.length();
  }
  
  /**
   * Gibt den Treffer mit der Nummer <code>treffernummer</code> zurueck.
   * @param treffernummer  Nummer des Treffers beginnend mit 0 bis maximal
   *    <code>getAnzahlTreffer()</code> - 1
   * @return BildDokument dieser Trefferlist an der Stelle treffernummer
   */
  public BildDokument getBildDokument(int treffernummer) {
    try {
      return BildDokument.erzeugeAusLucene(treffer.doc(treffernummer));
    } catch (ErzeugeBildDokumentException e) {
      System.out.println(e);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Gibt die Treffergenauigkeit mit der Nummer <code>treffernummer</code> 
   * zurueck. Die Treffergenauigkeit liegt zwischen 1 und 0, wobei 1 die beste
   * Treffergenauigkeit angibt. -1 wird bei einem Fehler zurueckgegeben.
   * @param treffernummer  Nummer des Treffers
   * @return  Treffergenauigkeit des Treffers mit der Nummer 
   *      <code>treffernummer</code>
   */
  public float getTrefferGenauigkeit(int treffernummer) {
    try {
      return treffer.score(treffernummer);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return -1;
  }
}
