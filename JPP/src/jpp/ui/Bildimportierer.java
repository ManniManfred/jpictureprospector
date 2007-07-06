package jpp.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import javax.swing.JOptionPane;

import jpp.core.BildDokument;
import jpp.core.JPPCore;
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
  private JPPCore kern;


  /**
   * Erzeugt ein neuen Objekt der Klasse mit den entsprechenden Daten
   * 
   * @param dateien die zu importierenden Dateien
   * @param kern der zu verwendende Programmkern
   */
  public Bildimportierer(File[] dateien, JPPCore kern) {

    this.listener = new ArrayList<BildimportListener>();
    this.dateien = dateien;
    this.kern = kern;
  }

  /**
   * Ruft den Event fuer alle Listener auf, wenn ein Bild erfolgreich importiert
   * wurde.
   */
  private void fireBildImportiert(BildDokument dok) {
    for (BildimportListener l : listener) {
      l.bildImportiert(dok);
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

  /**
   * Startet diesen Thread und fuehrt alle im Hintergrund auszufuehrenden
   * Operationen aus.
   */
  public void run() {

    /* Import der Dateien */
    if (dateien != null) {

      List<ImportException> fehler = new ArrayList<ImportException>();

      for (int i = 0; i < dateien.length; i++) {
        try {
          /* Zeit die verwendet wurde zum Import. */
          long zeit = Calendar.getInstance().getTimeInMillis();
          BildDokument dok = kern.importiere(dateien[i]);
          System.out.println("Datei importiert: "
              + dateien[i].getAbsolutePath());
          System.out.println("Benoetigte Zeit:  "
              + (Calendar.getInstance().getTimeInMillis() - zeit) + "ms");

          fireBildImportiert(dok);
        } catch (ImportException ie) {
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
  private void werteFehlerAus(List<ImportException> fehler) {

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
    for (ImportException e : fehler) {
      meldung += e.getMessage() + "\n";
    }
    JOptionPane.showMessageDialog(null, meldung, "Importfehler",
        JOptionPane.ERROR_MESSAGE);

    // /* Bereits importierte Dateien und defekt Dateien werden
    // * aufgezeichnet und spaeter ausgegeben
    // */
    // if (ie.getMessage().indexOf("bereits importiert") >= 0) {
    //    
    // if (bereitsImportierteDateien == null) {
    // bereitsImportierteDateien = new ArrayList<String>();
    // }
    // bereitsImportierteDateien.add(dateien[i].getName());
    // } else if (ie.getMessage().indexOf("kein BildDokument") >= 0) {
    //    
    // if (defekteDateien == null) {
    // defekteDateien = new ArrayList<String>();
    // }
    // defekteDateien.add(dateien[i].getName());
    // }

    // /* Erzeugung der Fehlermeldung, wenn bereits importierte oder
    // * defekte Dateien vorhanden sind
    // */
    // String fehlermeldung = "Die Datei(-en) konnten nicht " +
    // "importiert werden.\n\n";
    //  
    // if (bereitsImportierteDateien != null) {
    // String importierteDateien = "Diese Dateien wurden bereits importiert:\n";
    // for (String dateiname : bereitsImportierteDateien) {
    // importierteDateien += dateiname + "\n";
    // }
    // fehlermeldung += importierteDateien;
    // }
    // if (defekteDateien != null) {
    //    
    // String defekteDateien = "\nDiese Dateien sind defekt:\n";
    // for (String defekteDatei : this.defekteDateien) {
    // defekteDateien += defekteDatei + "\n";
    // }
    // fehlermeldung += defekteDateien;
    // }
    //  
    // /* Wenn Fehler beim Import erfolgt sind muss eine Fehlermeldung
    // * ausgegeben werden
    // */
    // if (bereitsImportierteDateien != null || defekteDateien != null) {
    // JOptionPane.showMessageDialog(null, fehlermeldung,
    // "Importfehler", JOptionPane.ERROR_MESSAGE);
    // }
    // bereitsImportierteDateien = null;
    // defekteDateien = null;

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
