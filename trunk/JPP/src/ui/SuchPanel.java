package ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.JList;
import java.awt.FlowLayout;

/**
 * Ein Objekt der Klasse repraesentiert ein Feld in dem Werte zur Suche
 * von Bildern eingegeben werden koennen.
 */
public class SuchPanel extends JPanel {

  /** Beschreibt die Standardbreite dieses Feldes. */
  private static final int STD_BREITE = 400;
  
  /** Beschreibt die Standardbreite dieses Feldes. */
  private static final int STD_HOEHE = 500;
  
  /** Enthält den standardabstand den ein Objekt am linken Rand haben muss. */
  private static final int STD_ABS_LINKS = 10;
  
  /** Enthält den standardabstand den ein Objekt am rechten Rand haben muss. */
  private static final int STD_ABS_RECHTS = 10;
  
  /** Enthält den standardabstand den ein Objekt am oberen Rand haben muss. */
  private static final int STD_ABS_OBEN = 10;
  
  /** Enthält den standardabstand den ein Objekt am unteren Rand haben muss. */
  private static final int STD_ABS_UNTEN = 10;
  
  private static final long serialVersionUID = 1L;
  
  private JTextField tfSuchFeld = null;
  
  private JLabel lSuchen = null;

  /**
   * Erstellt ein Objekt der Klasse mit den entsprechenden Elementen.
   */
  public SuchPanel() {
    super();
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
    }
    return tfSuchFeld;
  }

}  //  @jve:decl-index=0:visual-constraint="10,10"
