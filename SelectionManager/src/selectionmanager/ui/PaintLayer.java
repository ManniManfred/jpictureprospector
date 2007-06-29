package selectionmanager.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class PaintLayer extends JComponent {
  
  /**
   * generated serialVersionUID.
   */
  private static final long serialVersionUID = -5278147380427209972L;

  private static final float[] DOTTED = new float[] { 2f, 1f };

  private Rectangle r;
  
  
  public PaintLayer() {
    
  }
  
  public void zeichneRechteck(Rectangle r) {
    this.r = r;
    repaint();
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    if (r != null) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
          BasicStroke.JOIN_MITER, 10.0f, DOTTED, 0f));
      g2d.draw(r);
      
      g2d.setColor(new Color(50, 70, 255, 100));
      g2d.fill(r);
    }
  }
}
