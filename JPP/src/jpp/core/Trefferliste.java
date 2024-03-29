package jpp.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jpp.core.exceptions.ErzeugeBildDokumentException;
import jpp.core.exceptions.ErzeugeException;

import org.apache.lucene.search.Hits;


/**
 * Ein Objekt dieser Klasse stellt einen Ausschnitt einer Trefferliste sortiert 
 * nach der Treffergenauigkeit einer Suche dar.
 * 
 * @author Manfred Rosskamp
 * @author Nils Verheyen
 */
public class Trefferliste {

  /** Gibt die Anzahl aller gefundenen Treffer zurueck. */
  protected int anzahlTreffer;

  /** Enthaelt eine Liste mit allen Treffern dieser Trefferliste. */
  protected List<BildDokument> bildDokumente;

  /** Enthaelt zu jedem BildDokument die Treffergenauigkeit. */
  protected List<Float> score;

  /**
   * Erzeugt eine leere Trefferliste.
   */
  protected Trefferliste() {
    bildDokumente = new ArrayList<BildDokument>();
    score = new ArrayList<Float>();
    anzahlTreffer = 0;
  }


  /**
   * Erzeugt eine neue Trefferliste aus der Lucene-Trefferliste.
   * 
   * @param treffer Trefferliste aus Lucene, aus der diese erzeugt wird
   * @param offset Nummer des Bilddokumentes aller Treffer, ab der die Treffer
   *          in dieser Trefferliste aufgenommen werden sollen
   * @param maxanzahl Anzahl der Bilddokumente, die maximal in dieser 
   *          Trefferliste aufgenommen werden sollen
   * @throws ErzeugeException wird geworgen, wenn das Erzeugen dieser
   *           Trefferliste misslingt. Z.B wenn aus dem Lucene-Document nicht
   *           das BildDokument erzeugt werden kann.
   */
  public Trefferliste(Hits treffer, int offset, int maxanzahl)
      throws ErzeugeException {
    bildDokumente = new ArrayList<BildDokument>();
    score = new ArrayList<Float>();

    /* erzeuge aus der Lucene Trefferliste eine Liste mit BildDokumenten */
    try {
      anzahlTreffer = treffer.length();

      for (int i = offset; i < treffer.length() && i < offset + maxanzahl; i++) {
        bildDokumente.add(BildDokument.erzeugeAusLucene(treffer.doc(i)));
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
  
  public Trefferliste(List<BildDokument> doks, int gesamtanzahl) {
    bildDokumente = doks;
    anzahlTreffer = gesamtanzahl;
  }

  /**
   * Gibt die Anzahl aller Treffer zurueck.
   * 
   * @return Anzahl der Treffer
   */
  public int getGesamtAnzahlTreffer() {
    return anzahlTreffer;
  }

  /**
   * Gibt die Anzahl der Treffer dieser Liste zurueck. Dies m�ssen nicht alle
   * Treffer zu einer Suche sein. 
   * 
   * @return Anzahl der Treffer dieser Liste
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
