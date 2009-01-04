package jpp.webapp;

import java.net.MalformedURLException;
import java.net.URL;

import jpp.settings.ServerSettings;
import jpp.settings.SettingsManager;

public class Mapping {

  /**
   * Enthaelt das ServerSettings Objekt mit allen wichtigen serverSettings 
   * dieser Anwendung.
   */
  private static ServerSettings serverSettings = 
    SettingsManager.getSettings(ServerSettings.class);
  
  
  /**
   * Wandelt den Pfad der lokalen Datei um in eine, auf die das www zugreifen
   * kann.
   * 
   * @param lokal URL, die umgewandelt wird.
   * @return die umgewandelte URL
   * @throws MalformedURLException 
   */
  public static URL wandleInWWW(URL lokal) throws MalformedURLException {
    
    String urlstring = lokal.toString().replaceAll(
        serverSettings.getDateipfadSuchMapping(), 
        serverSettings.getDateipfadErsatzMapping());
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
    String urlstring = www.toString().replaceAll(
        serverSettings.getDateipfadErsatzMapping(),
        serverSettings.getDateipfadSuchMapping());
    return new URL(urlstring);
  }
  
}
