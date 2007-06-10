package ui;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Ein Objekt der Klasse stellt den Renderer der Tabellenzellen der
 * MerkmaleJTable dar. Mit diesem Renderer werden die Zellen gerendert, die
 * boolsche Werte enthalten.
 *
 * @author Marion Mecking
 */
public class CheckboxTableCellRenderer implements TableCellRenderer {
  
  /** Checkbox der Zelle. */
  private JCheckBox checkBox=new JCheckBox();
  
  /** Label der Zelle. */
  private JLabel label=new JLabel();
  
  /** Tabellenmodell, dem dieser Renderer zugeordnet ist. */
  private MerkmaleTableModel model;
  
  /** Farben der Zellen, abhaengig davon ob sie editierbar sind oder nicht. */
  private static final Color FARBE_EDIT_HINTERGRUND = Color.WHITE;
  private static final Color FARBE_NONEDIT_HINTERGRUND = Color.LIGHT_GRAY;
  private static final Color FARBE_GRAU = new Color(240, 240, 240);
  
  /**
   * Erzeugt eine neue Instanz des Renderers.
   * @param model  Tabellenmodell der entsprechenden Tabelle
   */
  public CheckboxTableCellRenderer(MerkmaleTableModel model) {
    checkBox.setEnabled(false);
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
  public Component getTableCellRendererComponent(JTable tabelle,
      Object wert,
      boolean istAusgewaehlt,
      boolean hatFokus,
      int zeile,
      int spalte) {
    
    /* Abhaengig davon, ob im Tabellenmodell angegebenen ist, dass diese
     * Zeile eine Checkbox enthaelt wird entweder eine Checkbox oder
     * ein Label erzeugt
     */
    if (!this.model.enthaeltZeileCheckbox(zeile)) {
      /* Checkbox wird nicht angezeigt, Appereance bestimmt Hintergrundfarbe */
      updateAppearance(label, istAusgewaehlt, zeile);
      return label;
    } else {
      
      /* Anzeige der Checkbox */
      checkBox.setSelected((Boolean) wert);
      checkBox.setHorizontalAlignment(SwingConstants.CENTER);
      updateAppearance(checkBox, istAusgewaehlt, zeile);
      return checkBox;
    }
  }
 
  /**
   * Setzt die Farbe fuer die uebergebene Komponente, abhaengig davon ob sie
   * ausgewaehlt ist oder nicht.
   *
   * @param component  Komponente, deren Farbe gesetzt wird.
   * @param istAusgewaehlt Angabe, ob die angegebene Zelle ausgewaehlt ist.
   * @param hatFokus  Gibt an, ob die angegebene Zelle fokussiert ist.
   * @param zeile  Zeile der Zelle, deren Komponente geliefert wird.
   */  
  private void updateAppearance(Component component,
      boolean istAusgewaehlt, int zeile) {
    
    /* Hintergrund ueberschreiben */
    if (component instanceof JLabel) {
      ((JLabel) component).setOpaque(true);
    } else if (component instanceof JCheckBox) {
      ((JCheckBox) component).setOpaque(true);
    }
    
    /* Farbe bestimmen, abhaengig davon, ob Zeile editierbar ist. */
    if (model.istZeileEditierbar(zeile)) {
      if (istAusgewaehlt) {
	component.setBackground(FARBE_GRAU);
      } else {
	component.setBackground(FARBE_EDIT_HINTERGRUND);
      }
    } else {
      component.setForeground(FARBE_NONEDIT_HINTERGRUND);
      component.setBackground(FARBE_NONEDIT_HINTERGRUND);
    }
     
  }
  
}
