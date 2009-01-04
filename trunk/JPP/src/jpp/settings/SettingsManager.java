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
  public static final String dbDatei = "settings.yap";

  /** db4o Datenbank */
  private static ObjectContainer db;


  /**
   * Bevor die Settingsobjekte mit der Methode getSettings geholt werden können,
   * muss diese Methode aufgerufen werden, und am die Methode close aufgerufen
   * werden.
   */
  public static void open() {
    if (db == null) {
      db = Db4o.openFile(dbDatei);
    }
  }

  public static void close() {
    if (db != null) {
      db.rollback();
      db.close();
      db = null;
    }
  }

  /**
   * Gibt die die StarterSettings zurueck.
   * 
   * @param klasse Klasse, der Setting, welche zurueckgegeben wird
   * @return Settings-Object des Angegeben Typs
   */
  public static <T> T getSettings(Class<T> klasse) {
    T ergebnis;

    if (db == null) {
      throw new IllegalStateException("Es muss zuerst open aufgerufen werden.");
    }
    
    try {
      ObjectSet<T> ssettings = db.get(klasse);

      if (ssettings.size() > 0) {
        ergebnis = ssettings.get(0);

        if (ssettings.size() > 1) {
          logger.log(Level.WARNING, "Es sind in der Datenbank mehrere "
              + "StarterSetting Objekte vorhanden.");
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
    }

    return ergebnis;
  }

  /**
   * Speichert die uebergebenen Settings.
   * 
   * @param settings Settings, die gespeichert werden
   */
  public static void saveSettings(Object settings) {
    if (db == null) {
      throw new IllegalStateException("Es muss zuerst open aufgerufen werden.");
    }
    
    /* Alle bisherigen Settings entfernen */
    ObjectSet ssettings = db.get(settings.getClass());

    for (Object alteSettings : ssettings) {
      if (alteSettings != settings) {
        db.delete(alteSettings);
      }
    }
    db.set(settings);
    db.commit();
  }
}
