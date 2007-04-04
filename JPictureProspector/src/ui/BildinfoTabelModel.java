package ui;

import javax.swing.table.DefaultTableModel;

public class BildinfoTabelModel extends DefaultTableModel {

  /** Version UID der Software. */
  private static final long serialVersionUID = 1L;
  
  /** Enthaelt die Zeilenanzahl des Modells. */
  private int zeilenAnzahl;
  
  /** Enthaelt die Spaltenanzahl des Modells. */
  private int spaltenAnzahl;
  
  public BildinfoTabelModel() {
    
    this(10, 2);
  }
  
  public BildinfoTabelModel(int zeilenAnzahl, int spaltenAnzahl) {
    
    this.zeilenAnzahl = zeilenAnzahl;
    this.spaltenAnzahl = spaltenAnzahl;
    this.setRowCount(this.zeilenAnzahl);
    this.setColumnCount(this.spaltenAnzahl);
  }
  
  public boolean isCellEditable(int zeile, int spalte) {
    
    return false;
  }
}
