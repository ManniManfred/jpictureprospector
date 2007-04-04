package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;

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
  
  public void paintComponent(Graphics g) {
    
    Image image = Toolkit.getDefaultToolkit()
                         .getImage("src/ui/think_different.jpg");
    double hoeheBild = image.getHeight(this);
    double breiteBild = image.getWidth(this);
    double dieseBreite = getWidth();
    g.setColor(new Color(238, 238, 238));
    g.fillRect(0, 0, getWidth(), getHeight());
    
    g.drawImage(image, 5, 15, (int) dieseBreite - 10,
        (int) (hoeheBild * (dieseBreite / breiteBild) - 10), this);
  }
}
