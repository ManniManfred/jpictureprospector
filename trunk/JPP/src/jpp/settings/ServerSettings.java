package jpp.settings;

public class ServerSettings {

  /**
   * Hier kann eine Vorschrift angegeben werden, um die URL des
   * Bildes (DateipfadMerkmal) in eine Andere umzuwandeln. Z.B. kann man so
   * aus der file-Angabe eine Http-Angabe machen.
   */
  public String dateipfadSuchMapping = "file:/tmp/webtmp/";

  public String dateipfadErsatzMapping = "http://10.22.20.29/tmp/";
  
  /**
   * Hier kann angegeben werden, wo Thumbnailbilder abgelegt sind.
   */
  public String thumbnailOrdner = "/tmp/webtmp/thumbs/"; //"/windows/sonstig/walls/.thumbs/"; 
  
  public String uploadOrdner = "/tmp/webtmp/bilder/";
  
  
  
  
}
