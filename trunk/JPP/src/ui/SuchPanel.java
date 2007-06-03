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
