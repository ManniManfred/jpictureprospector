package jpp.settings;

public class StarterSettings {

  /**
   * Gibt an, ob die StarterSettings gespeichert werden sollen.
   */
  public boolean saveStarterSettings = true;
  
  /**
   * Gibt an, ob lokal und nicht ueber einen Server gearbeitet wird.
   */
  public boolean arbeitetLokal = true;
  
  
  /**
   * Gibt den Pfad zum Ordner an, indem Lucene den Index fuer die Bilder ablegt.
   */
  public String imageIndex = "imageIndex";
  
  /**
   * URL zum zu verwendenen JPPServer.
   */
  public String jppServer = "http://localhost:8084/jpp/";
  
  
  /**
   * Benutzername mit dem sich der Client am JPPServer anmeldet.
   */
  public String user = "Karl";
  
  
  /**
   * Passwort des Users mit dem sich der Client am JPPServer anmeldet.
   */
  public String password = "xxxx";
  
  
}
