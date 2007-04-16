package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class Thumbnail extends JPanel {

private Image thumbnail = null;
  
  private Dimension groesze = null;
  
  public Thumbnail(Image thumbnail) {
    
    init(thumbnail, new Dimension(75, 75));
  }
  
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
