<?php
  /**
   * @version    $Id: mod_related_items.php 9030 2007-09-26 21:55:26Z jinx $
   * @package    Joomla
   * @copyright  Copyright (C) 2005 - 2007 Open Source Matters. All rights reserved.
   * @license    GNU/GPL, see LICENSE.php
   * Joomla! is free software. This version may have been modified pursuant
   * to the GNU General Public License, and as distributed it includes or
   * is derivative of works licensed under the GNU General Public License or
   * other free or open source software licenses.
   * See COPYRIGHT.php for copyright notices and details.
   */
   
  // no direct access
  defined('_JEXEC') or die('Restricted access');
   
  include_once("exceptions/AuthenticationException.cls.php");
  include_once("XmlTrefferlisteParser.cls.php");

  /**
   * Ein Objekt dieser Klasse stellt ein JPPClient dar, der sich mit dem JPPServer
   * verbindet, der in der config.inc.php eingetragen ist.
   */
  class JPPClient {
     
    private $jppserver;
    private $gruppe;
    private $size_index;
     
    private $jpp_session_id;
     
     
    /**
     * Erzeugt einen neuen JPPClient und verbindet sich direkt mit dem JPPServer.
     */
    public function JPPClient($jppserver, $user, $password, $gruppe, $size_index = -1) {
      $this->jppserver = $jppserver;
      $this->gruppe = $gruppe;
      $this->size_index = $size_index;
       
      /* Als erstes muessen wir uns am JPPServer anmelden */
      $this->log("Wir melden uns am JPP-Server {$this->jppserver} an...");
       
      $data = "loginname=$user&passwort=$password";
       
      $opts = array (
      'http' => array (
      'method' => 'POST',
        'header' => "Content-type: application/x-www-form-urlencoded\r\n" . "Content-Length: " . strlen($data) . "\r\n",
        'content' => $data )
      );
      $context = stream_context_create($opts);
       
      $result = file_get_contents($this->jppserver . "anmelden", FALSE, $context);
       
       
      if (substr($result, 0, strlen('true')) == 'true') {
        $this->log('DONE\n');
         
        /* Lese die Sessionid aus */
        $this->jpp_session_id = substr($result, strripos($result, "=") + 1);
      } else {
        $this->log('FAILED\n');
        $this->log('Es ist folgender Fehler aufgetreten: ' . $result);
         
        throw new AuthenticationException($result);
      }
    }
     
    public function getAlben() {
       
      $data = "gruppe=" . urlencode($this->gruppe);
      $opts = array (
      'http' => array (
      'method' => 'POST',
        'header' => "Content-type: application/x-www-form-urlencoded\r\n" . "Content-Length: " . strlen($data) . "\r\n" . "Cookie: JSESSIONID={$this->jpp_session_id}\r\n",
        'content' => $data )
      );
      $context = stream_context_create($opts);
       
      $anfrage = $this->jppserver . "getAlben";
       
      $albenStr = file_get_contents($anfrage, FALSE, $context);
      $albenStr = trim($albenStr);
      return split(";", $albenStr);
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
    public function suche($suchtext, $offset = 0, $maxanzahl = 20) {
       
      $opts = array(
      'http' => array(
      'method' => "GET",
        'header' => "Cookie: JSESSIONID={$this->jpp_session_id}\r\n" )
      );
      $context = stream_context_create($opts);
       
      $suchtext = urlencode("(" . $suchtext . ") && Gruppe:\"" . $this->gruppe . "\"");
      $anfrage = $this->jppserver . "suche?suchtext=$suchtext&offset=$offset&maxanzahl=$maxanzahl&format=xml" . "&sizeIndex=" . $this->size_index;
       
      $xml_result = file_get_contents($anfrage, FALSE, $context);

      $parser = new XmlTrefferlisteParser();
      $trefferliste = $parser->parse($xml_result);

      return $trefferliste;
    }
     
    private function log($msg) {
      $handle = fopen("/tmp/jpp.log", "a");
       
      fputs($handle, $msg . "\n");
       
      fclose($handle);
    }
  }
   
?>
