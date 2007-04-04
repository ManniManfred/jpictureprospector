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

  private JLabel lGroesse = null;

  private JComboBox cbGroesze = null;

  private JLabel lBildtyp = null;

  private JList lDateiformate = null;

  /**
   * Erstellt ein Objekt der Klasse mit den entsprechenden Elementen.
   */
  public SuchPanel() {
    super();
    initialize();
  }

  /**
   * Erzeugt alle Objekte, die dieses Panel beinhaltet und ordnet Sie
   * im Layout entsprechend an.
   */
  private void initialize() {
    
    GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
    gridBagConstraints5.fill = GridBagConstraints.BOTH;
    gridBagConstraints5.gridy = 2;
    gridBagConstraints5.weightx = 1.0;
    gridBagConstraints5.weighty = 1.0;
    gridBagConstraints5.insets = new Insets(STD_ABS_OBEN, STD_ABS_LINKS, 
        STD_ABS_UNTEN, STD_ABS_RECHTS);
    gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints5.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints5.gridx = 1;
    
    // Label für die Dateiartauswahl
    GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
    gridBagConstraints4.gridx = 0;
    gridBagConstraints4.gridy = 2;
    gridBagConstraints4.anchor = GridBagConstraints.NORTH;
    gridBagConstraints4.insets = new Insets(STD_ABS_OBEN, STD_ABS_LINKS, 
        STD_ABS_UNTEN, STD_ABS_RECHTS);
    lBildtyp = new JLabel();
    lBildtyp.setText("Dateiart");
    
    // Combobox für die Bildgröße
    GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
    gridBagConstraints3.fill = GridBagConstraints.BOTH;
    gridBagConstraints3.gridy = 1;
    gridBagConstraints3.weightx = 1.0;
    gridBagConstraints3.insets = new Insets(STD_ABS_OBEN, STD_ABS_LINKS, 
        STD_ABS_UNTEN, STD_ABS_RECHTS);
    gridBagConstraints3.anchor = GridBagConstraints.WEST;
    gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints3.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints3.gridx = 1;
    
    // Label für das Feld der Größe
    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
    gridBagConstraints2.gridx = 0;
    gridBagConstraints2.gridy = 1;
    gridBagConstraints2.insets = new Insets(STD_ABS_OBEN, STD_ABS_LINKS, 
        STD_ABS_UNTEN, STD_ABS_RECHTS);
    lGroesse = new JLabel();
    lGroesse.setText("Größe");
    
    // Textfeld für den Suchbegriff
    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
    gridBagConstraints1.fill = GridBagConstraints.BOTH;
    gridBagConstraints1.gridx = 1;
    gridBagConstraints1.gridy = 0;
    gridBagConstraints1.weightx = 1.0;
    gridBagConstraints1.anchor = GridBagConstraints.WEST;
    gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints1.insets = new Insets(STD_ABS_OBEN, STD_ABS_LINKS, 
        STD_ABS_UNTEN, STD_ABS_RECHTS);
    
    // Label des Suchbegriffsfelds
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.insets = new Insets(STD_ABS_OBEN, STD_ABS_LINKS, 
        STD_ABS_UNTEN, STD_ABS_RECHTS);
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridx = 0;
    lSuchen = new JLabel();
    lSuchen.setText("Suchbegriff");
    
    // Dieses Panel
    this.setSize(new Dimension(316, 147));
    this.setLayout(new GridBagLayout());
    this.setBorder(BorderFactory.createTitledBorder(null, "Bildsuche", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
    this.setBackground(new Color(238, 238, 238));
    this.add(lSuchen, gridBagConstraints);
    this.add(getTfSuchFeld(), gridBagConstraints1);
    this.add(lGroesse, gridBagConstraints2);
    this.add(getCbGroesze(), gridBagConstraints3);
    this.add(lBildtyp, gridBagConstraints4);
    this.add(getLDateiformate(), gridBagConstraints5);
  }

  /**
   * This method initializes tfSuchFeld	
   * 	
   * @return javax.swing.JTextField	
   */
  private JTextField getTfSuchFeld() {
    if (tfSuchFeld == null) {
      tfSuchFeld = new JTextField();
      tfSuchFeld.setPreferredSize(new Dimension(200, 20));
    }
    return tfSuchFeld;
  }

  /**
   * This method initializes cbGroesze	
   * 	
   * @return javax.swing.JComboBox	
   */
  private JComboBox getCbGroesze() {
    if (cbGroesze == null) {
      cbGroesze = new JComboBox();
      cbGroesze.setPreferredSize(new Dimension(200, 25));
      cbGroesze.addItem("");
      cbGroesze.addItem("klein");
      cbGroesze.addItem("mittel");
      cbGroesze.addItem("groß");
    }
    return cbGroesze;
  }

  /**
   * This method initializes lDateiformate	
   * 	
   * @return javax.swing.JList	
   */
  private JList getLDateiformate() {
    
    Object[] dateiformate = new Object[]{"bmp", "jpg", "tiff", "png", "gif"};
    if (lDateiformate == null) {
      lDateiformate = new JList();
      lDateiformate.setBorder(BorderFactory.createLineBorder(Color.black, 1));
      lDateiformate.setListData(dateiformate);
    }
    return lDateiformate;
  }

}
