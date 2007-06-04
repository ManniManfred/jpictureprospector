package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import javax.swing.JOptionPane;

import core.BildDokument;
import core.ImportException;
import core.JPPCore;

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
    if (dateien != null) {
      for (int i = 0; i < dateien.length; i++) {
        try {
          long zeit = Calendar.getInstance().getTimeInMillis();
          BildDokument dok = kern.importiere(dateien[i]);
          System.out.println("Datei importiert: " + dateien[i].getAbsolutePath());
          System.out.println("Benoetigte Zeit:  " + 
              (Calendar.getInstance().getTimeInMillis() - zeit) + "ms");
          fireBildImportiert();
          ThumbnailAnzeigePanel tap = new ThumbnailAnzeigePanel(dok,
              tapGroesze, tapObserver);
          tap.setzeDateinamen(dateien[i].getName(), tapGroesze);
          listeAnzeigePanel.add(tap);
        } catch (ImportException ie) {
          JOptionPane.showMessageDialog(null, "Die Datei(-en) konnten nicht" +
              " importiert werden.\n" + ie.getMessage(),
              "Importfehler", JOptionPane.ERROR_MESSAGE);
        }
      }
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
