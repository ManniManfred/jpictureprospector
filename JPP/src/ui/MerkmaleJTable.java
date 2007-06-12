package ui;

import core.BildDokument;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTable;

/**
 * Ein Objekt der Klasse stellt die Tablle der Merkmale zu den ausgewaehlten
 * Bilddokumenten dar.
 * 
 * @author Marion Mecking
 */
public class MerkmaleJTable extends JTable implements Observer {
  
  /** Bilddokumente, zu denen die Merkmale angezeigt werden. */
  private List<BildDokument> bilddokumente = new ArrayList();

  /** Tabellenmodell, in dem die Daten verwaltet werden. */
  private MerkmaleTableModel tabellenmodell = null;

  /** Spaltenbreite 1. Spalte Merkmal */
  private static final int SPALTENBREITE_MERKMAL = 140;

  /** Spaltenbreite 2. Spalte Merkmalswert */
  private static final int SPALTENBREITE_WERT = 120;

  /** Spaltenbreite 3. Spalte Checkbox */
  private static final int SPALTENBREITE_BOX = 45;

  /**
   * Erzeugt eine neue Instanz der Tabelle.
   */
  public MerkmaleJTable() {
    super();

    /* Anfangs ist kein Bilddokumente ausgewaehlt. */
    bilddokumente = new ArrayList();

    /* Tabellenmodell zuweisen. */
    this.tabellenmodell = new MerkmaleTableModel(this.bilddokumente);
    this.setModel(tabellenmodell);
    this.setName("Bilddetails");

    /* Renderer und Spaltenbreite setzen. */
    this.setDefaultRenderer(String.class, new MerkmaleTableCellRenderer(
        tabellenmodell));
    this.setzeRenderer();
    this.setzeSpaltenBreite();
  }

  /**
   * Setzt die Renderer dieser Tabelle.
   */
  private void setzeRenderer() {

    /*
     * DefaultRenderer ist MerkmaleTableCellRenderer, mit dem Zellen gerendert
     * werden, die Strings enthalten.
     */
    this.setDefaultRenderer(String.class, new MerkmaleTableCellRenderer(
        tabellenmodell));

    /*
     * Wird eine dritte Spalte angezeigt, enthaelt sie Checkboxen und wird mit
     * dem CheckboxTableCellRenderer gerendet
     */
    if (tabellenmodell.getColumnCount() > 2) {
      this.getColumnModel().getColumn(2).setCellRenderer(
          new CheckboxTableCellRenderer(tabellenmodell));
    }
  }

  /**
   * Setzt die Spaltenbreite dieser Tabelle.
   */
  private void setzeSpaltenBreite() {
    this.getColumnModel().getColumn(0).setPreferredWidth(SPALTENBREITE_MERKMAL);
    this.getColumnModel().getColumn(1).setPreferredWidth(SPALTENBREITE_WERT);
    if (tabellenmodell.getColumnCount() > 2) {
      this.getColumnModel().getColumn(2).setPreferredWidth(SPALTENBREITE_BOX);
    }
  }

  /**
   * Laedt die Tabelle anhand des entsprechenden <code>Observable</code> neu.
   * 
   * @param o
   *          das <code>Observable</code> dass sich geaendert hat
   * @param arg
   *          das entsprechende <code>Object</code> was sich im
   *          <code>Observable</code> geaendert hat
   */
  public void update(Observable o, Object arg) {

    if (arg instanceof ThumbnailAnzeigePanel) {
      
      BildDokument dok = ((ThumbnailAnzeigePanel) arg).gibBildDokument();
      
      if (bilddokumente.contains(dok)) {
        bilddokumente.remove(dok);
      } else {
        this.bilddokumente.add(dok);
      }

      this.tabellenmodell.aktualisiereBilddokumente(bilddokumente);
      this.setzeRenderer();
      this.setzeSpaltenBreite();
    }
  }
}
