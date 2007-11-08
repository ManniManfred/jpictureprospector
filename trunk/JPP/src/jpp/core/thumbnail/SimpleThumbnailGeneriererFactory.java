package jpp.core.thumbnail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jpp.core.Einstellungen;
import jpp.core.GeoeffnetesBild;

public class SimpleThumbnailGeneriererFactory {

  private static Logger logger = 
    Logger.getLogger("jpp.core.thumbnail.SimpleThumbnailGeneriererFactory");

  /**
   * Speichert die in der statischen Methode getZuordnung() einmal erstellte
   * Zuordnung von Format zu Generierer ab.
   */
  private static Map<String, ThumbnailGenerierer> thumbZuordnung;




  public static ThumbnailGenerierer erzeugeThumbnailGenerierer(
      GeoeffnetesBild bild) {

    /*
     * Hole aus der Zuordnungstabelle fuer das Format den richtigen Generierer
     */
    Map<String, ThumbnailGenerierer> zuordnung;
    try {
      zuordnung = getZuordnung();
    } catch (Exception e) {
      /* TODO evtl. eine Exception werfen */
      logger.log(Level.WARNING, "Zuordnung konnte nicht gelesen werden.", e);
      return null;
    }
    ThumbnailGenerierer generierer = zuordnung.get(bild.getFormat());

    /*
     * Falls fuer dieses Format kein Generierer angegeben ist, verwende den
     * Generierer, der fuer alle restlichen Formate zustaendig ist.
     */
    if (generierer == null) {
      generierer = zuordnung.get("*");
    }
    return generierer;
  }




  /**
   * Liest aus der Datei "thumbnailGenerierer" ein, welcher ThumbnailGenerierer
   * bei welchem Format verwendet werden soll.
   * 
   * @return eine Zuordnung, vom Format zu dem richtigen ThumbnailGenerierer-
   *         Objekt. Alle Formate, die nicht zugeordnet sind, werden von dem
   *         Generierer bearbeitet, der dem "*" zugeordnet ist.
   */
  private static Map<String, ThumbnailGenerierer> getZuordnung()
      throws Exception {
    if (thumbZuordnung == null) {
      thumbZuordnung = new HashMap<String, ThumbnailGenerierer>();

      String klassenname = "";

      try {
        for (int i = 0; i < Einstellungen.THUMB_ZUORDUNGEN.length; i++) {
          String zeile = Einstellungen.THUMB_ZUORDUNGEN[i];
          int posGleich = zeile.indexOf("=");
          String formateStr = zeile.substring(0, posGleich);

          /* Formate einlesen, die der Generierer bearbeiten soll */
          String[] formate = formateStr.split(",");
          klassenname = zeile.substring(posGleich + 1).trim();

          /* Generierer-Object erzeugen */
          Class klasse = Class.forName(klassenname);
          ThumbnailGenerierer generierer = (ThumbnailGenerierer) klasse
              .newInstance();

          /* Die Zuordnung von den Formaten zu dem Generierer abspeichern */
          for (int j = 0; j < formate.length; j++) {
            thumbZuordnung.put(formate[j].trim().toLowerCase(), generierer);
          }

        }
      } catch (ClassNotFoundException e) {
        throw new Exception("Konnte die Klasse \"" + klassenname
            + "\" nicht finden.", e);
      } catch (InstantiationException e) {
        throw new Exception("Konnte kein Objekt der Klasse \"" + klassenname
            + "\" erzeugen.", e);
      } catch (IllegalAccessException e) {
        throw new Exception("Konnte kein Objekt der Klasse \"" + klassenname
            + "\" erzeugen.", e);
      }
    }
    return thumbZuordnung;
  }

}
