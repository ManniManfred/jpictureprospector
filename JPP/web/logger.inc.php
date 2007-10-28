<?php

define("INFO",    0);
define("WARNING", 1);
define("ERROR",   2);


function logge($message, $status = WARNING) {
	echo '<p style="color: red;">' . $message . '</p>';
  flush();
}

?>