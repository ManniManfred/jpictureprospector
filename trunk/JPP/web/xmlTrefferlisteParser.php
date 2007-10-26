<?php

class XML_Trefferliste_Parser {

  __construct() {
  }
  
  /**
   * Parst den uebergebenen XML-Inhalt in ein PHP-Trefferlisten Objekt.
   * @param $xml_content {String} Inhalt der geparst werden soll
   * @return {Trefferliste} die geparste Trefferliste
   */
  function parse($xml_content) {
    $xml_parser = xml_parser_create();
    xml_set_element_handler($xml_parser, "startElement", "endElement");

    if (!xml_parse($xml_parser, $xml_content)) {
        die(sprintf("XML error: %s at line %d",
                    xml_error_string(xml_get_error_code($xml_parser)),
                    xml_get_current_line_number($xml_parser)));
    }
    xml_parser_free($xml_parser);
  }
  
  function endElement($parser, $name) {
    global $depth;
    $depth[$parser]--;
  }

  function startElement($parser, $name, $attrs) {
    global $depth;
    for ($i = 0; $i < $depth[$parser]; $i++) {
        echo "  ";
    }
    echo "$name\n";
    $depth[$parser]++;
  }
  
  
}


?>