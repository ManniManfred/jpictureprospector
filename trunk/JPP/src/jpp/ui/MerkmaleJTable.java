package jpp.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JTable;

import selectionmanager.Auswaehlbar;
import selectionmanager.AuswahlListener;

import jpp.core.AbstractJPPCore;
import jpp.core.BildDokument;
import jpp.core.JPPCore;
import jpp.core.LuceneJPPCore;


/**
 * Ein Objekt der Klasse stellt die Tablle der Merkmale zu den ausgewaehlten
 * Bilddokumenten dar.
 * 
 * @author Marion Mecking
 */
public class MerkmaleJTable extends JTable implements AuswahlListener {
  
  /** Bilddokumente, zu denen die Merkmale angezeigt werden. */
  private List<BildDokument> bilddokumente = new ArrayList<BildDokument>();

  /** Tabellenmodell, in dem die Daten verwaltet werden. */
  private MerkmaleTableModel tabellenmodell = null;

  /** Spaltenbreite 1. Spalte Merkmal. */
  private static final int SPALTENBREITE_MERKMAL = 140;

  /** Spaltenbreite 2. Spalte Merkmalswert. */
  private static final int SPALTENBREITE_WERT = 120;

  /** Spaltenbreite 3. Spalte Checkbox */
  private static final int SPALTENBREITE_BOX = 45;
  
  /** Kern der Anwendung. */
  private AbstractJPPCore kern;

  /**
   * Erzeugt eine neue Instanz der Tabelle.
   *
   * @param  Kern der Anwendung.
   */
  public MerkmaleJTable(AbstractJPPCore kern) {
    super();
    this.kern = kern;

    /* Anfangs ist kein Bilddokumente ausgewaehlt. */
    bilddokumente = new ArrayList<BildDokument>();

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
    
    /* DefaultRenderer ist MerkmaleTableCellRenderer, mit dem Zellen gerendert
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
   * Aendert die Daten der Bilddokumente.
   * @return  Liste der geaenderten Bilddokumente.
   */
  public List<BildDokument> aendereDaten() {
    return this.tabellenmodell.aendereDaten();
  }

  
  public void auswahlGeaendert(Set<Auswaehlbar> ausgewaehlten) {
    
    /* Passe die Tabelle der Auswahl an */
    if (ausgewaehlten != null) {
      
      /* erstelle eine neue Liste mit den ausgewaehlten BildDokumenten */
      bilddokumente = new ArrayList<BildDokument>();
      
      for (Auswaehlbar a : ausgewaehlten) {
        BildDokument dok = ((ThumbnailAnzeigePanel) a).gibBildDokument();
        bilddokumente.add(dok);
      }
      
      
      this.tabellenmodell.aktualisiereBilddokumente(bilddokumente);
      this.setzeRenderer();
      this.setzeSpaltenBreite();
    }
  }

  public void markierungWurdeBewegt(Auswaehlbar neueMarkierung) {
    /* Wenn die Markierung geaendert wurde, veraendere hier nichts. */
  }
}
