package jpp.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Ein Objekt der Klasse repraesentiert ein Feld in dem Werte zur Suche
 * von Bildern eingegeben werden koennen.
 */
public class SuchPanel extends JPanel {
  
  private static final long serialVersionUID = 1L;
  
  /** Enthaelt das Hauptfenster in dem das Panel gezeigt wird. */
  private Hauptfenster hauptfenster;
  
  private JTextField tfSuchFeld = null;
  
  private JLabel lSuchen = null;

  private JButton bSuchen = null;

  /**
   * Erstellt ein Objekt der Klasse mit den entsprechenden Elementen.
   */
  public SuchPanel(Hauptfenster hauptfenster) {
    super();
    this.hauptfenster = hauptfenster;
    initialize();
  }
  
  /**
   * Liefert den Suchtext den der Benutzer zur Suche eingegeben hat.
   * 
   * @return  der eingegebene Suchtext
   */
  public String gibSuchtext() {
    return tfSuchFeld.getText();
  }

  /**
   * Erzeugt alle Objekte, die dieses Panel beinhaltet und ordnet Sie
   * im Layout entsprechend an.
   */
  private void initialize() {
    
    // Label des Suchbegriffsfelds
    lSuchen = new JLabel();
    lSuchen.setText("Suchbegriff  ");
    lSuchen.setFont(new Font("Dialog", Font.BOLD, 18));
    
    // Dieses Panel
    this.setLayout(new FlowLayout());
    this.setBackground(new Color(238, 238, 238));
    this.setSize(new Dimension(500, 30));
    this.add(lSuchen, null);
    this.add(getTfSuchFeld(), null);
    this.add(getBSuchen(), null);
  }

  /**
   * This method initializes tfSuchFeld	
   * 	
   * @return javax.swing.JTextField	
   */
  private JTextField getTfSuchFeld() {
    if (tfSuchFeld == null) {
      tfSuchFeld = new JTextField();
      tfSuchFeld.setPreferredSize(new Dimension(300, 25));
      tfSuchFeld.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            hauptfenster.sucheNach(gibSuchtext());
            hauptfenster.erzeugeThumbnailansicht();
          }
        }
      });
    }
    return tfSuchFeld;
  }

  /**
   * This method initializes bSuchen	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getBSuchen() {
    if (bSuchen == null) {
      bSuchen = new JButton();
      bSuchen.setText("Suchen");
      bSuchen.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          hauptfenster.sucheNach(gibSuchtext());
          hauptfenster.erzeugeThumbnailansicht();
        }
      });
    }
    return bSuchen;
  }

}
