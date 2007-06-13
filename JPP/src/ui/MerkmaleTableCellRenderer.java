/*
 * MerkmaleTableCellRenderer.java
 *
 * Created on 4. Juni 2007, 20:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Ein Objekt der Klasse stellt den Renderer der Tabellenzellen der
 * MerkmaleJTable dar. Mit diesem Renderer werden die Zellen gerendert, die
 * Strings enthalten.
 *
 * @author Marion Mecking
 */
public class MerkmaleTableCellRenderer
    implements TableCellRenderer {
  
  /** Tabellenmodell, das die Daten enthaelt. */
  private MerkmaleTableModel model;
  
  /** Farben der Zellen, abhaengig davon ob sie editierbar sind oder nicht. */
  private static final Color FARBE_EDIT_HINTERGRUND = Color.WHITE;
  private static final Color FARBE_NONEDIT_HINTERGRUND = Color.LIGHT_GRAY;
  private static final Color FARBE_GRAU = new Color(240, 240, 240);
  
 
  /**
   * Erzeugt eine neue Instanz des Renderers.
   * @param model  Tabellenmodell der entsprechenden Tabelle
   */
  public MerkmaleTableCellRenderer(MerkmaleTableModel model) {
    
    this.model = model;
  }
  
  /**
   * Liefert die Komponente der angegebenen Zelle.
   *
   * @param tabelle  Tabelle, die den Renderer aufruft
   * @param wert   Inhalt der Tabellenzelle
   * @param istAusgewaehlt Gibt an, ob die angegebene Zelle ausgewaehlt ist.
   * @param hatFokus  Gibt an, ob die angegebene Zelle fokussiert ist.
   * @param zeile  Zeile der Zelle, deren Komponente geliefert wird.
   * @param spalte  Spalte der Zelle, deren Komponente geliefert wird.
   * @return  Komponente in der angegebenen Zelle. 
   */
  public Component getTableCellRendererComponent(JTable tabelle, Object wert,
      boolean istAusgewaehlt, boolean hatFocus, int zeile, int spalte) {
    
    /* Label erzeugen. */
    JLabel label = new JLabel((String) wert);
    label.setOpaque(true);
    
    /* Hintergrund setzen, abhaengig davon, ob die entsprechende Zeile 
     * editierbar und ausgewaehlt ist. */
    if (model.istZeileEditierbar(zeile)) {
      if (istAusgewaehlt) {	
	label.setBackground(FARBE_GRAU);
      } else {
	label.setBackground(FARBE_EDIT_HINTERGRUND);
      }     
    } else {
      label.setBackground(FARBE_NONEDIT_HINTERGRUND);
    }
  
    return label;  
  }
}
