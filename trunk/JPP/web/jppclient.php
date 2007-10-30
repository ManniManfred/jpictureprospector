<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/transitional.dtd">
<html>
<head>
  <title>JPPServer - Test</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<form method="POST">
Suche <input name="suchtext" type="text">
<input type="submit">
</form>
<pre>
<?php

if (isset($_POST["suchtext"])) {
  
  include_once("JPPClient.cls.php");
  include_once("Trefferliste.cls.php");
  
  try {
    $client = new JPPClient();
    
    $liste = $client->suche($_POST["suchtext"], 0, 10);
    
    echo "\n\nAnzahl Treffer: " . $liste->getGesamtAnzahlTreffer();
    
    for ($i = 0, $l = $liste->getAnzahlTreffer(); $i < $l; $i++) {
      $dok = $liste->getBildDokument($i);
      echo "\nPfad = " . $dok->getMerkmal("Dateipfad")->getWert();
      echo "\n<img src='data:image/jpg;base64," 
        . $dok->getMerkmal("Thumbnail")->getWert() . "'>";
    }
  } catch(CouldNotConnectException $e) {
    echo "Konnte leider nicht verbinden";
  }
}
?>

</pre>

</body>
</html>