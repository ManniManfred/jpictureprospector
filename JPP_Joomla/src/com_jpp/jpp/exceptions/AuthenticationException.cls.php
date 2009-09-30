<?php

class AuthenticationException extends Exception {
  
  
  public function NotConnectedException($message, $code = 0) {
    parent::__construct($message, $code);
    
  }

  public function __toString() {
    return __CLASS__ . ": [{$this->code}]: {$this->message}\n";
  }
}


?>