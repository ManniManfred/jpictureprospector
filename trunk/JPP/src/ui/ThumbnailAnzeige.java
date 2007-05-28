package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * Ein Objekt der Klasse stellt ein Thumbnail eines Bildes dar.
 * 
 * @author Nils Verheyen
 */
public class ThumbnailAnzeige extends JPanel {
  
  private static final int STD_GROESZE = 150;

  /** Enthaelt das Thumbnail. */
  private Image thumbnail = null;
  
  /** Enthaelt die Groesze in der das Thumbnail angezeigt werden soll. */
  private int groesze;
  
  /**
   * Erzeugt ein neues Objekt der Klasse.
   * 
   * @param thumbnail  enthaelt das anzuzeigende Thumbnail
   */
  public ThumbnailAnzeige(Image thumbnail) {
    
    init(thumbnail, STD_GROESZE);
  }
  
  /**
   * Setzt die Hoehe und Breite dieses Objektes neu. Die Groesze dieses Objektes
   * soll immer quadratisch sein.
   * 
   * @param groesze  Hoehe und Breite des Objektes
   */
  public void setzeGroesze(int groesze) {
    this.groesze = groesze;
    this.setSize(groesze, groesze);
    this.repaint();
  }
  
  public void init(Image thumbnail, int groesze) {
    
    this.thumbnail = thumbnail;
    this.groesze = groesze;
    this.setLayout(null);
    this.setSize(groesze, groesze);
    this.repaint();
  }
  
  public Image gibBild() {
    return this.thumbnail;
  }
  
  protected void paintComponent(Graphics g) {
    
    double hoeheBild = this.thumbnail.getHeight(this);
    double breiteBild = this.thumbnail.getWidth(this);
    double dieseBreite = groesze;
    double dieseHoehe = groesze;
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
