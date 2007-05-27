package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Vorschaupanel extends JPanel implements Observer {

  private static final long serialVersionUID = 1L;
  
  private Image bild = null;
  
  /**
   * This is the default constructor
   */
  public Vorschaupanel(Image bild) {
    super();
    initialize();
    this.bild = bild;
  }
  
  /**
   * Setzt und zeichnet das Vorschaubild neu.
   * 
   * @param bild  das Bild was neu gesetzt werden soll
   */
  public void setzeBild(Image bild) {
    this.bild = bild;
    this.repaint();
  }

  public void update(Observable o, Object arg) {
    if (arg instanceof Image) {
      setzeBild((Image) arg);
    }
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(300, 300);
    this.setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createEmptyBorder(0, 0, 0, 0), "Vorschau",
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
        new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
    this.repaint();
  }
  
  /**
   * Zeichnet Elemente auf dieses Objekt.
   * 
   * @param g
   */
  public void paintComponent(Graphics g) {
    
    if (bild != null) {
      double hoeheBild = bild.getHeight(this);
      double breiteBild = bild.getWidth(this);
      double dieseBreite = getWidth();
      double dieseHoehe = getHeight();
      g.setColor(new Color(238, 238, 238));
      g.fillRect(0, 0, getWidth(), getHeight());
    
      // Anpassung der Größe an dieses Objekt
    
      if (Math.abs(dieseBreite / breiteBild) < Math.abs(dieseHoehe / hoeheBild)) {
        
        g.drawImage(bild, 0, 20, (int) dieseBreite,
            (int) (hoeheBild * (dieseBreite / breiteBild)), this);
      } else {
        
        g.drawImage(bild, 0, 20,
            (int) (breiteBild * (dieseHoehe / hoeheBild)),
            (int) dieseHoehe, this);
      }
    }
  }
}
