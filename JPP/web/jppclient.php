<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/transitional.dtd">
<html>
<head>
  <title>JPPServer - Test</title>
</head>
<body>
<pre>
<?php

$address = "127.0.0.1";
$port = 5656;

$passwort = "bla";
$suchtext = "jpg";


// Socket erzeugen
echo "Socket erzeugen ... ";
$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
if ($socket < 0) {
    echo "FAILED\nsocket_create() fehlgeschlagen: Grund: " . socket_strerror ($socket) . "\n";
} else {
    echo "OK.\n";
}

// Zum Server verbinden
echo "Zum Server " . $address . " auf Port " . $port . " verbinden ... ";
$result = socket_connect ($socket, $address, $service_port);
if ($result < 0) {
    echo "FAILED\nsocket_connect() fehlgeschlagen.\nGrund: ($result) " . socket_strerror($result) . "\n";
} else {
    echo "OK.\n";
}

// Passwort senden
echo "Passwort zum Server senden ...";
socket_write ($socket, $passwort, strlen ($passwort));
echo "OK.\n";


// Suche starten
echo "Suche nach \"" . $suchtext . "\" ... ";
$befehl = "suche $suchtext";
socket_write ($socket, $befehl, strlen ($befehl));
echo "OK.\n";

// Suchergebnis ausgeben
echo "Die Antwort lesen:\n\n";
$xml_trefferliste = "";
while ($read = socket_read ($socket, 2048)) {
    $xml_trefferliste .= $read;
}

echo "XML-Trefferliste: " . $xml_trefferliste;

// Verbindung schliessen
echo "Verbindung schlissen ... ";
$befehl = "exit";
socket_write ($socket, $befehl, strlen ($befehl));
socket_close($socket);
echo "OK.\n\n";


// XML verarbeiten
echo "\nXML verarbeiten ... ";


?>

</pre>

</body>
</html>