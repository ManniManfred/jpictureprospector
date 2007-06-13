package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import javax.swing.JOptionPane;

import ui.listener.BildimportListener;
import core.BildDokument;
import core.JPPCore;
import core.exceptions.ImportException;

/**
 * Ein Objekt der Klasse importiert in den Programmkern eine
 * Anzahl Dateien als eigenständiger Vorgang. Innerhalb der
 * Klasse werden Methoden aufgerufen, die zwischenzeitliche Ergebnisse
 * hinsichtlich des Bildimportiervorgangs liefern.
 */
public class Bildimportierer extends Thread {

  /** Enthaelt alle Listener die auf Zwischenergebnisse warten. */
  private List<BildimportListener> listener;
  
  /** Enthaelt alle Dateien, die importiert werden sollen. */
  private File[] dateien;
  
  /** Enthaelt den Kern des Programms in den importiert wird. */
  private JPPCore kern;
  
  /** Enthaelt die Groesze einen <code>ThumbnailAnzeigePanel</code>s. */
  private int tapGroesze;
  
  /** Enthaelt eine Liste aller Observer die die AnzeigePanel beobachten. */
  private List<Observer> tapObserver;
  
  /** Enthaelt eine Liste aller AnzeigePanel. */
  private List<ThumbnailAnzeigePanel> listeAnzeigePanel;
  
  /** Enthaelt eine Liste der Dateien, die bereits importiert wurden. */
  private List<String> bereitsImportierteDateien = null;
  
  /** Enthaelt eine Liste an Dateien, die defekt sind und nicht importiert
   * werden konnten.
   */
  private List<String> defekteDateien = null;
  
  /**
   * Erzeugt ein neuen Objekt der Klasse mit den entsprechenden Daten
   * 
   * @param dateien  die zu importierenden Dateien
   * @param kern  der zu verwendende Programmkern
   * @param tapGroesze  die Groesze der AnzeigePanel
   * @param tapObserver  alle Observer der AnzeigePanel
   */
  public Bildimportierer(File[] dateien, JPPCore kern, 
      int tapGroesze, List<Observer> tapObserver) {
    
    this.listener = new ArrayList<BildimportListener>();
    this.dateien = dateien;
    this.kern = kern;
    this.tapGroesze = tapGroesze;
    this.tapObserver = tapObserver;
  }
  
  /**
   * Liefert eine Liste aller zur Zeit importierten AnzeigePanel
   * 
   * @return  die aktuelle Liste an <code>ThumbnailAnzeigePanel</code>n
   */
  public List<ThumbnailAnzeigePanel> gibAnzeigePanel() {
    return this.listeAnzeigePanel;
  }
  
  /**
   * Ruft den Event fuer alle Listener auf, wenn ein Bild erfolgreich
   * importiert wurde.
   */
  private void fireBildImportiert() {
    for (BildimportListener l : listener) {
      l.bildImportiert();
    }
  }
  
  /**
   * Ruft den Event fuer alle Listener auf, wenn der Importvorgang
   * abgeschlossen wurde.
   */
  private void fireLadevorgangAbgeschlossen() {
    for (BildimportListener l : listener) {
      l.ladevorgangAbgeschlossen();
    }
  }
  
  /**
   * Startet diesen Thread und fuehrt alle im Hintergrund auszufuehrenden
   * Operationen aus.
   */
  public void run() {
    
    listeAnzeigePanel = new ArrayList<ThumbnailAnzeigePanel>();
    
    /* Import der Dateien */
    if (dateien != null) {
      for (int i = 0; i < dateien.length; i++) {
        try {
          // Zeit die verwendet wurde zum Import.
          long zeit = Calendar.getInstance().getTimeInMillis();
          BildDokument dok = kern.importiere(dateien[i]);
          System.out.println("Datei importiert: " + dateien[i].getAbsolutePath());
          System.out.println("Benoetigte Zeit:  " + 
              (Calendar.getInstance().getTimeInMillis() - zeit) + "ms");
          
          // Erzeugen des entsprechenden Panels
          ThumbnailAnzeigePanel tap = new ThumbnailAnzeigePanel(dok,
              tapGroesze, tapObserver, i);
          tap.setzeDateinamen(dateien[i].getName(), tapGroesze);
          listeAnzeigePanel.add(tap);          
        } catch (ImportException ie) {
          
          /* Bereits importierte Dateien und defekt Dateien werden
           * aufgezeichnet und spaeter ausgegeben
           */
          if (ie.getMessage().indexOf("bereits importiert") >= 0) {
            
            if (bereitsImportierteDateien == null) {
              bereitsImportierteDateien = new ArrayList<String>();
            }
            bereitsImportierteDateien.add(dateien[i].getName());
          } else if (ie.getMessage().indexOf("kein BildDokument") >= 0) {
            
            if (defekteDateien == null) {
              defekteDateien = new ArrayList<String>();
            }
            defekteDateien.add(dateien[i].getName());
          }
        } finally {
          
          // Es wurde ein Bild importiert oder eine Fehlermeldung erzeugt
          fireBildImportiert();
        }
      }
      
      /* Erzeugung der Fehlermeldung, wenn bereits importierte oder
       * defekte Dateien vorhanden sind
       */
      String fehlermeldung = "Die Datei(-en) konnten nicht " +
          "importiert werden.\n\n";
      
      if (bereitsImportierteDateien != null) {
        String importierteDateien = "Diese Dateien wurden bereits importiert:\n";
        for (String dateiname : bereitsImportierteDateien) {
          importierteDateien += dateiname + "\n";
        }
        fehlermeldung += importierteDateien;
      }
      if (defekteDateien != null) {
        
        String defekteDateien = "\nDiese Dateien sind defekt:\n";
        for (String defekteDatei : this.defekteDateien) {
          defekteDateien += defekteDatei + "\n";
        }
        fehlermeldung += defekteDateien;
      }
      
      /* Wenn Fehler beim Import erfolgt sind muss eine Fehlermeldung
       * ausgegeben werden
       */
      if (bereitsImportierteDateien != null || defekteDateien != null) {
        JOptionPane.showMessageDialog(null, fehlermeldung,
            "Importfehler", JOptionPane.ERROR_MESSAGE);
      }
      bereitsImportierteDateien = null;
      defekteDateien = null;
      fireLadevorgangAbgeschlossen();
    }
  }
  
  /**
   * Fuegt einen <code>BildimportListener</code> der Liste an
   * Listenern hinzu.
   * 
   * @param l  der hinzuzufuegende Listener
   */
  public void addBildImportiertListener(BildimportListener l) {
    listener.add(l);
  }
  
  /**
   * Loescht einen <code>BildimportListener</code> aus der Liste
   * an Listenern hinzu.
   * 
   * @param l der zu loeschende Listener
   */
  public void removeBildImportiertListener(BildimportListener l) {
    if (l != null) {
      listener.remove(l);
    }
  }
}
