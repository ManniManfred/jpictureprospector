package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * Ein Objekt der Klasse stellt ein Thumbnail eines Bilder dar.
 * 
 * @author Nils Verheyen
 */
public class Thumbnail extends JPanel {

  /** Enthaelt das Thumbnail. */
  private Image thumbnail = null;
  
  /** Enthaelt die Groesze in der das Thumbnail angezeigt werden soll. */
  private Dimension groesze = null;
  
  /**
   * Erzeugt ein neues Objekt der Klasse.
   * 
   * @param thumbnail  enthaelt das anzuzeigende Thumbnail
   */
  public Thumbnail(Image thumbnail) {
    
    init(thumbnail, new Dimension(75, 75));
  }
  
  public void setzeGroesze(Dimension groesze) {
    this.groesze = groesze;
  }
  
  public void init(Image thumbnail, Dimension groesze) {
    
    this.thumbnail = thumbnail;
    this.groesze = groesze;
    this.setLayout(new BorderLayout());
    this.setSize(this.groesze);
    this.repaint();
  }
  
  protected void paintComponent(Graphics g) {
    
    double hoeheBild = this.thumbnail.getHeight(this);
    double breiteBild = this.thumbnail.getWidth(this);
    double dieseBreite = getWidth();
    double dieseHoehe = getHeight();
    g.setColor(new Color(238, 238, 238));
    g.fillRect(0, 0, getWidth(), getHeight());
    
    // Anpassung der Größe an dieses Objekt
    if (Math.abs(dieseBreite / breiteBild) < Math.abs(dieseHoehe / hoeheBild)) {
      
      g.drawImage(this.thumbnail, 0, 0, (int) dieseBreite,
          (int) (hoeheBild * (dieseBreite / breiteBild)), this);
    } else {
      
      g.drawImage(this.thumbnail, 0, 0,
          (int) (breiteBild * (dieseHoehe / hoeheBild)),
          (int) dieseHoehe, this);
    }
  }
}
