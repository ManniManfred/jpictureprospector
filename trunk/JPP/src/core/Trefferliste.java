package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.Hits;

import core.exceptions.ErzeugeBildDokumentException;
import core.exceptions.ErzeugeException;

/**
 * Ein Objekt dieser Klasse stellt die Trefferliste sortiert nach der
 * Treffergenauigkeit einer Suche dar.
 * 
 * @author Manfred Rosskamp
 * @author Nils Verheyen
 */
public class Trefferliste {

  /** Enthaelt eine Liste mit allen Treffern dieser Trefferliste. */
  private List<BildDokument> bildDokumente;

  /** Enthaelt zu jedem BildDokument die Treffergenauigkeit. */
  private List<Float> score;

  /**
   * Erzeugt eine leere Trefferliste.
   */
  public Trefferliste() {
    bildDokumente = new ArrayList<BildDokument>();
    score = new ArrayList<Float>();
  }

  /**
   * Erzeugt eine neue Trefferliste aus der Lucene-Trefferliste.
   * 
   * @param treffer Trefferliste aus Lucene, aus der diese erzeugt wird
   * @throws ErzeugeException wird geworgen, wenn das Erzeugen dieser
   *           Trefferliste misslingt. Z.B wenn aus dem Lucene-Document nicht
   *           das BildDokument erzeugt werden kann.
   */
  public Trefferliste(Hits treffer) throws ErzeugeException {
    bildDokumente = new ArrayList<BildDokument>();
    score = new ArrayList<Float>();

    /* erzeuge aus der Lucene Trefferliste eine Liste mit BildDokumenten */
    try {
      for (int i = 0; i < treffer.length(); i++) {
        bildDokumente.add(i, BildDokument.erzeugeAusLucene(treffer.doc(i)));
        score.add(treffer.score(i));
      }
    } catch (ErzeugeBildDokumentException e) {
      throw new ErzeugeException("Konnte aus dem Lucene-Document kein "
          + "BildDokument erzeugen", e);
    } catch (IOException e) {
      throw new ErzeugeException("Konnte aus dem Lucene-Document kein "
          + "BildDokument erzeugen", e);
    }
  }

  /**
   * Gibt die Anzahl der Treffer zurueck.
   * 
   * @return Anzahl der Treffer
   */
  public int getAnzahlTreffer() {
    return bildDokumente.size();
  }

  /**
   * Gibt den Treffer mit der Nummer <code>treffernummer</code> zurueck.
   * 
   * @param treffernummer Nummer des Treffers beginnend mit 0 bis maximal
   *          <code>getAnzahlTreffer()</code> - 1
   * @return BildDokument dieser Trefferliste an der Stelle treffernummer oder
   *         null, falls an die treffernummer ungueltig ist
   */
  public BildDokument getBildDokument(int treffernummer) {
    return (treffernummer < getAnzahlTreffer() && treffernummer >= 0)
        ? bildDokumente.get(treffernummer)
        : null;
  }

  /**
   * Gibt die Treffergenauigkeit mit der Nummer <code>treffernummer</code>
   * zurueck. Die Treffergenauigkeit liegt zwischen 1 und 0, wobei 1 die beste
   * Treffergenauigkeit angibt. -1 wird bei einem Fehler zurueckgegeben.
   * 
   * @param treffernummer Nummer des Treffers
   * @return Treffergenauigkeit des Treffers mit der Nummer
   *         <code>treffernummer</code>
   */
  public float getTrefferGenauigkeit(int treffernummer) {
    return (treffernummer < getAnzahlTreffer() && treffernummer >= 0)
        ? score.get(treffernummer)
        : -1;
  }
}
