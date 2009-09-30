<?php

/**
 * Ein Objekt dieser Klasse repraesentiert ein Merkmal mit einem 
 * Mermalsnamen und einem zu dem Merkmal gehoerenden Wert. 
 *  
 * @author Manfred Rosskamp
 */
class Merkmal {
  
  /** Enthaelt den Namen dieses Merkmals. */
  private $name;
  
  /** Enthaelt den Wert dieses Merkmals. */
  private $wert;
   
  /** 
   * Erzeugt ein neues Merkmal mit einem eindeutig identifiezierenden Namen.
   * @param name  Name dieses Merkmals
   */
  public function Merkmal($name) {
    $this->name = $name;
    $this->wert = "";
  }

  /**
   * Gibt den Namen dieses Merkmals, der im Konstruktor gesetzt wurde, zurueck.
   * @return {String} Name dieses Merkmals
   */
  public function getName() {
    return $this->name;
  }
 
  /**
   * Gibt den Wert dieses Merkmals zurueck.
   * @return Wert dieses Merkmals
   */
  public function getWert() {
    return $this->wert;
  }

  /**
   * Setzt den Wert dieses Merkmals.
   * @param wert  Wert, auf den dieses Merkmal gesetzt werden soll.
   */
  public function setWert($wert) {
    $this->wert = $wert;
  }
}
?>