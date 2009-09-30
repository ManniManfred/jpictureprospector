<?php

include_once("exceptions/ParseException.cls.php");
include_once("Trefferliste.cls.php");
include_once("BildDokument.cls.php");
include_once("Merkmal.cls.php");

class XmlTrefferlisteParser {

  private $xmlParser;
  private $trefferliste;
  private $currentDok;
  private $currentMerkmal;
  
  public function XmlTrefferlisteParser() {
    $this->xmlParser = xml_parser_create();
    xml_set_object($this->xmlParser,&$this);
    xml_set_element_handler(
            $this->xmlParser,
            "startElement",
            "endElement");
            
    xml_set_character_data_handler(
        $this->xmlParser,
        "tagdata"
    );
  }
  
  /**
   * Parst den uebergebenen XML-Inhalt in ein PHP-Trefferlisten Objekt.
   * @param $xml_content {String} Inhalt der geparst werden soll
   * @return {Trefferliste} die geparste Trefferliste
   * @throws ParseException wenn der Inhalt nicht geparst werden konnte
   */
  public function parse($xml_content) {
  
    if (!xml_parse($this->xmlParser, $xml_content)) {
        throw new ParseException("XML error: " 
          . xml_error_string(xml_get_error_code($xml_parser))
          . " at line" . xml_get_current_line_number($xml_parser));
    }
    xml_parser_free($this->xmlParser);
    
    return $this->trefferliste;
  }
  

  private function startElement($parser, $name, $attrs) {
    if ($name == "TREFFERLISTE") {
      $this->trefferliste = new Trefferliste();
      $this->trefferliste->setGesamtAnzahlTreffer($attrs["ANZAHL"]);
      $this->log("Create Trefferliste");
    } else if ($name == "BILDDOKUMENT") {
      $this->currentDok = new BildDokument();
      
      if($this->trefferliste == null) {
        throw new ParseException("Die XML ist nicht gültig.");
      }
      // @TODO score muss noch aus der XML geholt werden
      $this->trefferliste->addBildDokument($this->currentDok, 1);
      $this->log("Add Dokument");
    } else if (true) { // @TODO check if name starts with "jpp.merkmale."
      $this->currentMerkmal = new Merkmal($name);
      $this->log("Create Merkmal mit name " . $name);

      if ($this->currentDok == null) {
        throw new ParseException("Die XML ist nicht gültig.");
      }
      $this->currentDok->addMerkmal($this->currentMerkmal);
    } else {
      throw new ParseException("Die XML ist nicht gültig.");
    }
  }

  private function endElement($parser, $name) {
  }
  
  private function tagdata($parser, $data) {
    if ($data != "" && $data != "\n") { 
      if ($this->currentMerkmal == null) {
        throw new ParseException("Die XML ist nicht gültig.");
      } else {
        $this->currentMerkmal->setWert($this->currentMerkmal->getWert() . $data);
      }
    }
  }


    private function log($msg) {
/*
      $handle = fopen("/tmp/jpp.log", "a");
       
      fputs($handle, $msg . "\n");
       
      fclose($handle);
*/
    }
}


?>