package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import ui.listener.BildGeladenListener;

import merkmale.AlleMerkmale;
import merkmale.DateipfadMerkmal;
import core.BildDokument;

/**
 * Ein Objekt der Klasse stellt das Tabellenmodell dar, was zur Tabelle
 * in der Groszansicht verwendet wird. Wenn ein neues Bild im Programm
 * ausgewaehlt wird, sollen sich die Informationen entsprechend aendern.
 */
public class BGATabellenModell extends DefaultTableModel implements Observer {
  
  /**
   * Enthaelt das Bilddokument das die darzustellenden Informationen enthaelt.
   */
  private BildDokument dok = null;
  
  /** Enthaelt alle Listener die darauf hoeren ob ein Bild geladen wurde. */
  private List<BildGeladenListener> listener = null;
  
  /**
   * Erstellt ein neues Objekt der Klasse.
   * @param dok  das anfangs darzustellende <code>BildDokument</code>.
   */
  public BGATabellenModell(BildDokument dok) {
    
    this.dok = dok;
    this.setRowCount(0);
    this.addColumn("Bezeichnung");
    this.addColumn("Wert");
    this.listener = new ArrayList<BildGeladenListener>();
  }
  
  /**
   * Gibt an ob eine Zelle innerhalb der Tabelle editierbar ist oder nicht.
   */
  public boolean isCellEditable(int zeile, int spalte) {
    return false;
  }

  /**
   * Wird aufgerufen, wenn ein neues Bild in der Oberflaeche ausgewaehlt
   * wurde.
   * 
   * @param o  das zugehoerige <code>Oberservable</code>
   * @param arg  das Objekt was sich geaendert hat.
   */
  public void update(Observable o, Object arg) {
    
    if (arg instanceof BildDokument) {
      dok = (BildDokument) arg;
      
      List<AlleMerkmale> alleMerkmale = dok.gibAlleMerkmale();
      this.setRowCount(0);
      for (AlleMerkmale merkmal : alleMerkmale) {
        Object[] daten = new Object[]{merkmal.getName(),
            merkmal.getWert().toString()};
        this.addRow(daten);
      }
      fireBildGeladen();
    }
    
  }
  
  private void fireBildGeladen() {
    for (BildGeladenListener l : listener) {
      l.bildWurdeGeladen();
    }
  }
  
  public void addBildGeladenListener(BildGeladenListener l) {
    listener.add(l);
  }
  
  public void removeBildGeladenListener(BildGeladenListener l) {
    if (listener.contains(l)) {
      listener.remove(l);
    }
  }
}
