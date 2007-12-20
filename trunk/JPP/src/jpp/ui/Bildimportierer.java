package jpp.ui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import jpp.core.AbstractJPPCore;
import jpp.core.BildDokument;
import jpp.core.exceptions.ImportException;
import jpp.ui.listener.BildimportListener;


/**
 * Ein Objekt der Klasse importiert in den Programmkern eine Anzahl Dateien als
 * eigenständiger Vorgang. Innerhalb der Klasse werden Methoden aufgerufen, die
 * zwischenzeitliche Ergebnisse hinsichtlich des Bildimportiervorgangs liefern.
 */
public class Bildimportierer extends Thread {

  /** Enthaelt alle Listener die auf Zwischenergebnisse warten. */
  private List<BildimportListener> listener;

  /** Enthaelt alle Dateien, die importiert werden sollen. */
  private File[] dateien;

  /** Enthaelt den Kern des Programms in den importiert wird. */
  private AbstractJPPCore kern;

  /**
   * Gibt an, ob der Importiervorgang abgebrochen werden soll.
   */
  private boolean abort;

  /**
   * Erzeugt ein neuen Objekt der Klasse mit den entsprechenden Daten
   * 
   * @param dateien die zu importierenden Dateien
   * @param kern der zu verwendende Programmkern
   */
  public Bildimportierer(File[] dateien, AbstractJPPCore kern) {

    this.listener = new ArrayList<BildimportListener>();
    this.dateien = dateien;
    this.kern = kern;
    abort = false;
  }

  /**
   * Ruft den Event fuer alle Listener auf, wenn ein Bild erfolgreich importiert
   * wurde.
   */
  private void fireBildImportiert() {
    for (BildimportListener l : listener) {
      l.bildImportiert();
    }
  }

  /**
   * Ruft den Event fuer alle Listener auf, wenn der Importvorgang abgeschlossen
   * wurde.
   */
  private void fireLadevorgangAbgeschlossen() {
    for (BildimportListener l : listener) {
      l.ladevorgangAbgeschlossen();
    }
  }

  public void brecheVorgangAb() {
    this.abort = true;
  }
  
  /**
   * Startet diesen Thread und fuehrt alle im Hintergrund auszufuehrenden
   * Operationen aus.
   */
  public void run() {

    /* Import der Dateien */
    if (dateien != null) {
      
      List<Exception> fehler = new ArrayList<Exception>();

      for (int i = 0; i < dateien.length && !abort; i++) {
        try {
          /* Zeit die verwendet wurde zum Import. */
          long zeit = Calendar.getInstance().getTimeInMillis();
          kern.importiere(dateien[i].toURL());
          System.out.println("Datei importiert: "
              + dateien[i].getAbsolutePath());
          System.out.println("Benoetigte Zeit:  "
              + (Calendar.getInstance().getTimeInMillis() - zeit) + "ms");

          fireBildImportiert();
        } catch (ImportException ie) {
          fehler.add(ie);
        } catch (MalformedURLException ie) {
          fehler.add(ie);
        }
      }
      werteFehlerAus(fehler);
      fireLadevorgangAbgeschlossen();
    }
  }

  /**
   * Wertet die aufgetretenen Fehler aus, indem ein zusammenfassende Meldung
   * angezeigt wird.
   * 
   * @param e
   */
  private void werteFehlerAus(List<Exception> fehler) {

    /* Wenn kein Fehler auftrat, ist man mit der Fehlerbehandlung fertig. */
    if (fehler == null || fehler.size() == 0) {
      return;
    }
    
    
    String meldung;
    if (fehler.size() == 1) {
      meldung = "Es trat beim Importiern folgender Fehler auf: \n";
    } else {
      meldung = "Es traten beim Importiern folgende Fehler auf: \n";
    }
    for (Exception e : fehler) {
      meldung += e.getMessage() + "\n";
    }
    JOptionPane.showMessageDialog(null, meldung, "Importfehler",
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Fuegt einen <code>BildimportListener</code> der Liste an Listenern hinzu.
   * 
   * @param l der hinzuzufuegende Listener
   */
  public void addBildImportiertListener(BildimportListener l) {
    listener.add(l);
  }

  /**
   * Loescht einen <code>BildimportListener</code> aus der Liste an Listenern
   * hinzu.
   * 
   * @param l der zu loeschende Listener
   */
  public void removeBildImportiertListener(BildimportListener l) {
    if (l != null) {
      listener.remove(l);
    }
  }
}
