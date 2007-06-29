package selectionmanager.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.LineBorder;

public class DottedLineBorder extends LineBorder {

  private static final long serialVersionUID = -8871752061140226168L;
  
  private static final float[] DOTTED = new float[] { 5f, 2f };


  public DottedLineBorder(Color color, int thickness, boolean roundedCorners) {
    super(color, thickness, roundedCorners);
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width,
      int height) {
    if (g instanceof Graphics2D) {
      Graphics2D g2d = (Graphics2D) g;
      Color oldColor = g2d.getColor();
      Stroke oldStroke = g2d.getStroke();
      g2d.setColor(lineColor);
      g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT,
          BasicStroke.JOIN_MITER, 10.0f, DOTTED, 0f));
      if (roundedCorners) {
        g2d.draw(new RoundRectangle2D.Float(x + thickness / 2f, y + thickness
            / 2f, width - thickness, height - thickness, thickness, thickness));
      } else {
        g2d.draw(new Rectangle2D.Float(x + thickness / 2f, y + thickness / 2f,
            width - thickness, height - thickness));
      }
      g2d.setStroke(oldStroke);
      g2d.setColor(oldColor);
    } else {
      // if not Graphics2D, then there is no stroke support
      // so fallback to default behavior of super class
      super.paintBorder(c, g, x, y, width, height);
    }
  }
}
