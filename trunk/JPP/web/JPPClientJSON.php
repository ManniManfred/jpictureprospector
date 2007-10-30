<?php
include_once("JPPClient.cls.php");

/**
 * Ein Objekt dieser Klasse stellt JSON-Wrapper für den JPPClient dar.
 */
class class_JPPClient {
  
  private $client;
  
  /**
   * Erzeugt einen neuen JPPClient und verbindet sich direkt mit dem JPPServer.
   * 
   * @throws Exception, wenn keine Verbindung aufgebaut werden konnte
   */
  public function class_JPPClient() {
    $client = new JPPClient();
  }
  
  
  /**
   * Suche in allen importierten Bilder nach dem Suchtext und gibt eine
   * entsprechende Trefferliste mit den Suchergebnissen zurueck.
   * 
   * @param suchtext Suchtext, nach dem gesucht wird
   * @param offset Nummer des Bilddokumentes aller Treffer, ab der die Treffer
   *          in der Trefferliste aufgenommen werden sollen
   * @param maxanzahl Anzahl der Bilddokumente, die maximal in der Trefferliste
   *          aufgenommen werden sollen
   * @return Trefferliste mit den Suchergebnissen
   * @throws SucheException wird geworfen, wenn die Suche nicht erfolgreich mit
   *           einer erzeugten Trefferliste beendet werden kann
   */
  public function method_suche($suchtext, $offset, $maxanzahl) {
    return $client->suche($suchtext, $offset, $maxanzahl);
  }


}


?>