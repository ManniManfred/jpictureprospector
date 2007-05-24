package ui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

/**
 * Ein Objekt der Klasse repraesentiert einen Dateifilter, der bestimmte
 * Dateitypen akzeptiert, die Bilder sind.
 * 
 * @author  Nils Verheyen
 */
public class Bildfilter extends FileFilter {
  
  /** Enthaelt die zu akzeptierenden Dateitypen. */
  private static ArrayList<String> dateitypen = null;
  
  /**
   * Erstellt ein neues Objekt der Klasse mit den entsprechenden
   * Dateitypen, die akzeptiert werden sollen.
   */
  public Bildfilter() {
    
    dateitypen = new ArrayList<String>();
    dateitypen.add("jpg");
    dateitypen.add("jpeg");
    dateitypen.add("gif");
    dateitypen.add("bmp");
    dateitypen.add("tif");
    dateitypen.add("tiff");
    dateitypen.add("png");
  }

  /**
   * Gibt an welche Dateien von diesem Filter akzeptiert werden und welche
   * nicht. Verzeichnisse werden vom Filter automatisch geoeffnet, es ist
   * also nicht moeglich Verzeichnisse als Quelle anzugeben.
   * 
   * @param f  Die Datei, die akzeptiert werden soll
   * @return  <code>true</code> wenn die Datei dem Filter entspricht
   */
  @Override
  public boolean accept(File f) {
    
    if (f.isDirectory()) {
      return true;
    } else {
      String extension = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');
      if (i > 0 &&  i < s.length() - 1) {
        extension = s.substring(i+1).toLowerCase();
      }
      if (extension != null) {
        if (dateitypen.contains(extension)) {
        return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }
  }

  /**
   * Liefert die Beschreibung die dem Benutzer angezeigt wird,
   * wenn er Dateien importiert.
   * 
   * @return  die Beschreibung fuer alle Bilddateien
   */
  @Override
  public String getDescription() {
    
    return "Bilddateien";
  }

}
