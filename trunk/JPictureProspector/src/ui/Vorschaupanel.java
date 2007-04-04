package ui;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Vorschaupanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  private JLabel lVorschaubild = null;

  /**
   * This is the default constructor
   */
  public Vorschaupanel() {
    super();
    initialize();
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    lVorschaubild = new JLabel();
    lVorschaubild.setText("");
    this.setSize(300, 300);
    this.setLayout(new BorderLayout());
    this.add(lVorschaubild, BorderLayout.CENTER);
  }
  
  public void setzeBild(ImageIcon imageicon) {
    
    this.lVorschaubild = new JLabel(imageicon);
  }
}
