<?php

include_once("logger.inc.php");
include_once("exceptions/CouldNotConnectException.cls.php");
class SocketVerbindung {

  private $stopzeichen = "$";

  private $port = 4567;
  private $address;
  private $socket;
  
  

  /**
   * Erzeugt ein neues Verbindungsobjekt und baut direkt eine Verbindung auf.
   * 
   * @throws CouldNotConnectException wenn keine Verbindung aufgebaut werden
   *  konnte.
   */
  function SocketVerbindung($hostname = "localhost", $port = 4567) {
    $this->port = $port;
    $this->address = gethostbyname($hostname);
    $this->socket = socket_create(AF_INET, SOCK_STREAM,SOL_TCP);
    
    if ($this->socket < 0) {
      throw new CouldNotConnectException("Konnte keinen Socket erzeugen.");
    } else {
      if (socket_connect($this->socket, $this->address, $this->port) === false) {
        throw new CouldNotConnectException("Konnte nicht über Port $port mit $hostname({$this->address}) verbinden.");
      }
    }
  }

  function __destruct() {
    $this->write("exit");
    socket_close($this->socket);
  }

  public function read() {
    $gelesen = "";
    $zeichen = "";
    $timeOut = 2000; // in millisekunden
    $timestep = 100;
    $time = 0;
    while($zeichen != $this->stopzeichen && $time < $timeOut) {
      $zeichen = socket_read($this->socket, 1);
      if (strlen($zeichen) == 0) {
        sleep($timestep);
        $time += $timestep;
      }
      $gelesen .= $zeichen;
    }
    if ($time >= $timeOut) {
      logge("Lesen vom Server hat zulange gedauert.", WARNING);
    }
    return substr($gelesen, 0, -2); // ohne die beiden Zeichen "\n>"
  }

  public function write($anfrage) {
    socket_write($this->socket, $anfrage);
  }
  
  public function writeln($anfrage) {
    socket_write($this->socket, $anfrage . "\n");
  }
}
?>