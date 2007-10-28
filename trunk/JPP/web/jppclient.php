<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/transitional.dtd">
<html>
<head>
  <title>JPPServer - Test</title>
</head>
<body>
<pre>
<?php

include_once("SocketVerbindung.cls.php");

$address = "192.168.2.145";
$port = 4567;

$username = "kalle";
$passwort = "bla";
$suchtext = "jpg";


// Zum Server verbinden
echo "Zum Server " . $address . " auf Port " . $port . " verbinden ... ";
flush();
$conn = new SocketVerbindung();
echo "OK.\n";
flush();

// Vom Server lesen
echo "Gelesen: " . $conn->read();
flush();

// Anfrage stellen
$conn->write("suche " . $suchtext . "\n");

// Vom Server lesen
echo "Gelesen: " . $conn->read();
flush();

// XML verarbeiten
echo "\nXML verarbeiten ... ";


?>

</pre>

</body>
</html>