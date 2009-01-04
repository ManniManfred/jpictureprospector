package jpp.settings;

import java.io.File;


public class ServerSettings {

  /**
   * Gibt den Port an, auf den der Server laufen soll.
   */
  public int port = 8080;


  /**
   * Gibt Pfad an, unter der diese Webapplikation zu finden ist.
   */
  public String contextPath = "/jpp";

  /**
   * Verzeichnis in dem die JPP-Daten liegen sollen (Bilder, Thumbnails und
   * Index)
   */
  public String jppDataDir = "/opt/jppdata/";

  /**
   * URL unter der die Bilder zu finden sind.
   */
  public String jppDataURL = "http://localhost/jppdata/";

  /**
   * Die maximale Größe, die ein Bild haben darf, welches hochgeladen wird.
   * (In Bytes)
   */
  public int maxContentSize = 20000000;
  
  /**
   * Hier kann eine Vorschrift angegeben werden, um die URL des Bildes
   * (DateipfadMerkmal) in eine Andere umzuwandeln. Z.B. kann man so aus der
   * file-Angabe eine Http-Angabe machen.
   */
  public String getDateipfadSuchMapping() {
    return "file:" + jppDataDir;
  }
  
  public String getDateipfadErsatzMapping() {
    return jppDataURL;
  }
  
  /**
   * Hier kann angegeben werden, wo Thumbnailbilder abgelegt sind.
   */
  public String getThumbnailOrdner() {
    return concatFolder(jppDataDir, "thumbs/");
  }


  /**
   * Gibt den Ordner an, in dem die Bilder eines Import-Vorgangs gespeichert
   * werden sollen.
   */
  public String getUploadOrdner() {
    return concatFolder(jppDataDir, "images/");
  }

  /**
   * Gibt den Pfad zum Lucene Index an, in dem die Bilder gespeichert werden
   * sollen.
   */
  public String getIndexDir() {
    return concatFolder(jppDataDir, "imageIndex/");
  }
  
  private String concatFolder(String part1, String part2) {
    String result;
    if (part1.endsWith(File.separator)) {
      result = part1 + part2;
    } else {
      result = part1 + File.separator + part2;
    }
    return result;
  }
}
