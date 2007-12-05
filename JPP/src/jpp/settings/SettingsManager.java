package jpp.settings;

import java.util.logging.Level;
import java.util.logging.Logger;


import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class SettingsManager {

  /** Der zu verwendene Logger */
  private static Logger logger = Logger.getLogger("SettingsManager"); 
  
  /** Enthaelt den Pfad zur Datenbank-Datei. */
  private static final String dbDatei = "settings.yap";
  
    
  /**
   * Gibt die die StarterSettings zurueck.
   * 
   * @param klasse Klasse, der Setting, welche zurueckgegeben wird
   * @return Settings-Object des Angegeben Typs
   */
  public static <T>T getSettings(Class<T> klasse) {
    T ergebnis;

    ObjectContainer db = Db4o.openFile(dbDatei);
    try {
      ObjectSet<T> ssettings = db.get(klasse);
      
      if (ssettings.size() > 0) {
        ergebnis = ssettings.get(0);
        
        if (ssettings.size() > 1) {
          logger.log(Level.WARNING, "Es sind in der Datenbank mehrere " +
          		"StarterSetting Objekte vorhanden.");
        }
      } else {
        ergebnis = klasse.newInstance();
        db.set(ergebnis);
      }
      db.commit();
    } catch (InstantiationException e) {
      logger.log(Level.SEVERE, "Die Klasse " + klasse.getName() 
          + " konnte nicht instanziiert werden.");
      ergebnis = null;
    } catch (IllegalAccessException e) {
      logger.log(Level.SEVERE, "Die Klasse " + klasse.getName() 
          + " konnte nicht instanziiert werden.");
      ergebnis = null;
    } finally {
      db.close();
    }
    
    return ergebnis;
  }
  
  /**
   * Speichert die uebergebenen Settings.
   * 
   * @param settings Settings, die gespeichert werden
   */
  public static void saveSettings(Object settings) {

    ObjectContainer db = Db4o.openFile(dbDatei);
    try {
      /* Alle bisherigen Settings entfernen */
      ObjectSet ssettings = db.get(settings.getClass());
      
      for (Object alteSettings : ssettings) {
        if (alteSettings != settings) {
          db.delete(alteSettings);
        }
      }
      
      db.set(settings);
      db.commit();
    } finally {
      db.close();
    }
    
  }
}
