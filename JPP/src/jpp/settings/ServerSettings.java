package jpp.settings;


public class ServerSettings {


  /**
   * Gibt den Port an, auf den der Server laufen soll.
   */
  public int port = 8080;
  
  
  /**
   * Gibt Pfad an, unter der diese Webapplikation zu finen ist.
   */
  public String contextPath = "/jpp";
  
  
  
  /** 
   * Webapp-Verzeichnis des JPP.
   */
  public String jppDir = "/windows/daten/Entwicklung/JPP/webapp/";
  
  /**
   * URL von JPP.
   */
  public String jppURL = "http://10.22.20.29:" + port + contextPath + "/";
  
  
  /**
   * Hier kann eine Vorschrift angegeben werden, um die URL des
   * Bildes (DateipfadMerkmal) in eine Andere umzuwandeln. Z.B. kann man so
   * aus der file-Angabe eine Http-Angabe machen.
   */
  public String dateipfadSuchMapping = "file:" + jppDir;

  public String dateipfadErsatzMapping = jppURL;
  
  
  /**
   * Hier kann angegeben werden, wo Thumbnailbilder abgelegt sind.
   */
  public String thumbnailOrdner = jppDir + "thumbs/";
  
  /**
   * Gibt den Ordner an, in dem die Bilder eines Import-Vorgangs gespeichert
   * werden sollen.
   */
  public String uploadOrdner = jppDir + "images/";
  
  
  /**
   * Gibt den Pfad zum Lucene Index an, in dem die Bilder gespeichert werden
   * sollen.
   */
  public String indexDir = jppDir + "imageIndex/";
  
  
}
