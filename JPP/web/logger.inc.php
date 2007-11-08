<?php

include_once("config.inc.php");

define("INFO",    0);
define("WARNING", 1);
define("ERROR",   2);


/**
 * Vearbeitet Nachrichten oder Fehlermeldungen.
 * @param string $message    Nachricht oder Fehlermeldung
 * @param string $type       Typ der Nachricht oder Fehlermeldung (ERROR,WARNING,INFO,DEBUG)
 * @param string $dateiName  Name der Datei, aus der diese Methode aufgerufen wurde
 * @param string $methodName Name der Methode, aus der diese Methode aufgerufen wurde
 * @access private
 * @return void
 */
function logge($message, $type=INFO, $dateiName="", $methodName="") {

  $str = date("Y.m.d H:i:s") . " " . $type;

  if ($dateiName !="" || $methodName != "") {
    $str .= "(";
    if ($dateiName != "") {
      $str .= "File: " . $dateiName . " ";
    }
    if ($methodName != "") {
      $str .= "Methode: " . $methodName;
    }
    $str .= ")";
  }
  $str .= ": " . $message . "\n\r";

  if (isset($_ENV["LOGFILE"]) && $_ENV["LOGFILE"] != '') {
    $fd = fopen($_ENV["LOGFILE"], "a");

    fwrite($fd, $str);
    fclose($fd);
  } else {
    echo "LOGFILE ist nicht bekannt, um Meldungen in die Log-Datei zu schreiben.<br>\n";
    echo $str;
  }

}

?>