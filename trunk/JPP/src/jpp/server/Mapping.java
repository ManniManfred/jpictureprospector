package jpp.server;

import java.net.MalformedURLException;
import java.net.URL;

import jpp.core.Einstellungen;

public class Mapping {

  
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
        Einstellungen.dateipfadSuchMapping, 
        Einstellungen.dateipfadErsatzMapping);
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
        Einstellungen.dateipfadErsatzMapping,
        Einstellungen.dateipfadSuchMapping);
    return new URL(urlstring);
  }
  
}
