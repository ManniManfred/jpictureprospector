<?php

include_once("logger.php");

class SocketVerbindung {

  private $port = 4567;
  private $address;
  private $socket;


  function SocketVerbindung($hostname = "localhost", $port = 4567) {
    $this->port = $port;
    $this->address = gethostbyname($hostname);
    $this->socket = socket_create(AF_INET, SOCK_STREAM,SOL_TCP);
    if ($this->socket < 0) {
      logge("Socket konnte nicht erzeugt werden", ERROR);
    } else {
      if (socket_connect($this->socket, $this->address, $this->port) === false) {
        logge("Konnte nicht über Port $port mit $address verbinden.", ERROR);
      } else {

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
    while($zeichen != ">" && $time < $timeOut) {
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