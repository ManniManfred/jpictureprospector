<?php

include_once("Merkmal.cls.php");

/**
 * Ein Objekt dieser Klasse stellt ein Dokument mit vielen Merkmalen zu einem
 * Bild dar.
 * 
 * @author Manfred Rosskamp
 */
class BildDokument {

  /**
   * {Map} Zuordnung des Merkmalsnamen zu einem Merkmal dieses BildDokumentes,
   * wie z.B die Bildhoehe oder Bildbreite.
   */
  private $merkmale;

  /**
   * Erzeugt ein neues BildDokument.
   */
  public function BildDokument() {
    $this->merkmale = array();
  }

  /**
   * Gibt das Merkmal mit dem <code>merkmalName</code> dieses BildDokuments
   * zurueck.
   * 
   * @param merkmalName
   *          Name des Merkmals, welches zurueckgegeben werden soll
   * @return {Merkmal} Merkmal dieses BildDokuments mit dem <code>merkmalName</code>
   */
  public function getMerkmal($merkmalName) {
    return $this->merkmale[$merkmalName];
  }

  /**
   * Fuegt diesem BildDokument ein Merkmal hinzu.
   */
  public function addMerkmal($merkmal) {
    $this->merkmale[$merkmal->getName()] = $merkmal;
  }

  /**
   * Gibt eine Liste mit den Grundmerkmalen zurueck. Grundmerkmale sind
   * die, die in Lucene abgelegt werden und nach den gesucht werden kann.
   * 
   * @return Liste mit den Grundmerkmalen
   */
  public function gibGrundMerkmale() {
    return array_values($this->merkmale);
  }


}



?>