<?php


/**
 * Ein Objekt dieser Klasse stellt einen Ausschnitt einer Trefferliste sortiert 
 * nach der Treffergenauigkeit einer Suche dar.
 * 
 * @author Manfred Rosskamp
 */
class Trefferliste {

  /** {int} Gibt die Anzahl aller gefundenen Treffer an. */
  private $anzahlTreffer;

  /** {Array} Enthaelt eine Liste mit allen Treffern dieser Trefferliste. */
  private $bildDokumente;

  /** {Array} Enthaelt zu jedem BildDokument die Treffergenauigkeit. */
  private $score;

  
  /**
   * Erzeugt eine leere Trefferliste.
   */
  public function Trefferliste() {
    $this->bildDokumente = array();
    $this->score = array();
    $this->anzahlTreffer = 0;
  }

  /**
   * Fuegt diese Trefferliste ein BildDokument hinzu.
   */
  public function addBildDokument($dokument, $score) {
    array_push($this->bildDokumente, $dokument);
    array_push($this->score, $score);
  }
  
  /**
   * Gibt die Anzahl aller Treffer zurueck.
   * 
   * @return {int} Anzahl der Treffer
   */
  public function getGesamtAnzahlTreffer() {
    return $this->anzahlTreffer;
  }

  /**
   * Setzt die Anzahl aller Treffer.
   */
  public function setGesamtAnzahlTreffer($anzahlTreffer) {
    $this->anzahlTreffer = $anzahlTreffer;
  }
  
  /**
   * Gibt die Anzahl der Treffer dieser Liste zurueck. Dies m�ssen nicht alle
   * Treffer zu einer Suche sein. 
   * 
   * @return {int} Anzahl der Treffer dieser Liste
   */
  public function getAnzahlTreffer() {
    return count($this->bildDokumente);
  }
  
  /**
   * Gibt den Treffer mit der Nummer <code>treffernummer</code> zurueck.
   * 
   * @param $treffernummer {int} Nummer des Treffers beginnend mit 0 bis maximal
   *          <code>getAnzahlTreffer()</code> - 1
   * @return {BildDokument} BildDokument dieser Trefferliste an der Stelle treffernummer oder
   *         null, falls an die treffernummer ungueltig ist
   */
  public function getBildDokument($treffernummer) {
    return ($treffernummer < $this->getAnzahlTreffer() && $treffernummer >= 0)
        ? $this->bildDokumente[$treffernummer]
        : null;
  }

  /**
   * Gibt die Treffergenauigkeit mit der Nummer <code>treffernummer</code>
   * zurueck. Die Treffergenauigkeit liegt zwischen 1 und 0, wobei 1 die beste
   * Treffergenauigkeit angibt. -1 wird bei einem Fehler zurueckgegeben.
   * 
   * @param $treffernummer {int} Nummer des Treffers
   * @return {float} Treffergenauigkeit des Treffers mit der Nummer
   *         <code>treffernummer</code>
   */
  public function getTrefferGenauigkeit($treffernummer) {
    return $this->score[$treffernummer];
  }

}

?>