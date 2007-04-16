package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Vorschaupanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
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
    this.setSize(300, 300);
    this.setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createTitledBorder(null, 
        "Vorschau", TitledBorder.DEFAULT_JUSTIFICATION, 
        TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12),
        new Color(51, 51, 51)));
    this.repaint();
  }
  
  /**
   * Zeichnet Elemente auf dieses Objekt.
   * 
   * @param g
   */
  public void paintComponent(Graphics g) {
    
    Image image = Toolkit.getDefaultToolkit()
                         .getImage("src/ui/think_different.jpg");
    double hoeheBild = image.getHeight(this);
    double breiteBild = image.getWidth(this);
    double dieseBreite = getWidth();
    double dieseHoehe = getHeight();
    g.setColor(new Color(238, 238, 238));
    g.fillRect(0, 0, getWidth(), getHeight());
    
    // Anpassung der Größe an dieses Objekt
    if (Math.abs(dieseBreite / breiteBild) < Math.abs(dieseHoehe / hoeheBild)) {
      
      g.drawImage(image, 5, 20, (int) dieseBreite - 20,
          (int) (hoeheBild * (dieseBreite / breiteBild)) - 20, this);
    } else {
      
      g.drawImage(image, 5, 20,
          (int) (breiteBild * (dieseHoehe / hoeheBild)) - 20,
          (int) dieseHoehe - 20, this);
    }
  }
}
