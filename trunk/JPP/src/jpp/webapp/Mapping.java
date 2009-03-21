package jpp.webapp;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import jpp.settings.ServerSettings;
import jpp.settings.SettingsManager;

public class Mapping {

  /**
   * Enthaelt das ServerSettings Objekt mit allen wichtigen serverSettings
   * dieser Anwendung.
   */
  private static ServerSettings serverSettings = SettingsManager
      .getSettings(ServerSettings.class);


  /**
   * Wandelt den Pfad der lokalen Datei um in eine, auf die das www zugreifen
   * kann.
   * 
   * @param lokal URL, die umgewandelt wird.
   * @return die umgewandelte URL
   * @throws MalformedURLException
   */
  public static URL wandleInWWW(URL lokal) throws MalformedURLException {
    
    String suchMapping = new URL(serverSettings.getDateipfadSuchMapping()).toString();
    String ersatzMapping = new URL(serverSettings.getDateipfadErsatzMapping()).toString();
    
    if (!suchMapping.endsWith("/")) {
      suchMapping += "/";
    }
    if (!ersatzMapping.endsWith("/")) {
      ersatzMapping += "/";
    }
    
    String urlstring = replace(lokal.toString(), suchMapping, ersatzMapping);
    
    
    return new URL(urlstring);
  }



  /**
   * Wandelt den Pfad einer vom www ereichbaren URL um in die lokale.
   * 
   * @param www URL, die umgewandelt wird.
   * @return die umgewandelte URL
   * @throws MalformedURLException
   */
  public static URL wandleInLokal(URL www) throws MalformedURLException {

    String suchMapping = new URL(serverSettings.getDateipfadSuchMapping()).toString();
    String ersatzMapping = new URL(serverSettings.getDateipfadErsatzMapping()).toString();
    
    if (!suchMapping.endsWith("/")) {
      suchMapping += "/";
    }
    if (!ersatzMapping.endsWith("/")) {
      ersatzMapping += "/";
    }
    
    String urlstring = replace(www.toString(), ersatzMapping, suchMapping);
    
    
    return new URL(urlstring);
  }

  
  public static String replace(String in, String remove, String replace) {
    if (in == null || remove == null || remove.length() == 0)
      return in;
    StringBuffer sb = new StringBuffer();
    int oldIndex = 0;
    int newIndex = 0;
    int remLength = remove.length();
    while ((newIndex = in.indexOf(remove, oldIndex)) > -1) {
      // copy from last to new appearance
      sb.append(in.substring(oldIndex, newIndex));
      sb.append(replace);
      // set old index to end of last apperance.
      oldIndex = newIndex + remLength;
    }
    int inLength = in.length();
    // add part after last appearance of string to remove
    if (oldIndex < inLength)
      sb.append(in.substring(oldIndex, inLength));
    return sb.toString();
  }

}
