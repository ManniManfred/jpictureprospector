package ui;

import java.awt.BorderLayout;
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
  
  /**
   * Erzeugt ein neues Objekt der Klasse.
   * 
   * @param thumbnail  enthaelt das anzuzeigende Thumbnail
   * @param groesze  enthaelt die Groesze in der das Thumbnail angezeigt
   *        werden soll
   */
  public Thumbnail(Image thumbnail, Dimension groesze) {
    
    init(thumbnail, groesze);
  }
  
  public void init(Image thumbnail, Dimension groesze) {
    
    this.thumbnail = thumbnail;
    this.groesze = groesze;
    this.setLayout(new BorderLayout());
    this.setSize(this.groesze);
    this.repaint();
  }
  
  protected void paintComponent(Graphics g) {
    
    if (thumbnail != null) {
      
      g.drawImage(this.thumbnail, 
          (int) (this.getWidth() - this.thumbnail.getWidth(this)) / 2, 
          (int) (this.getHeight() - this.thumbnail.getHeight(this)) / 2,
          (int) groesze.getWidth(),
          (int) groesze.getHeight(),
          this);
    }
  }
}
