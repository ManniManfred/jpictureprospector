package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ThumbnailPanel extends JPanel {

  private JLabel lDateiname = null;
  
  private JLabel lTreffgenauigkeit = null;
  
  private Image thumbnail = null;

  /**
   * This method initializes 
   * 
   */
  public ThumbnailPanel(Image thumbnail) {
  	super();
    this.thumbnail = thumbnail;
  	initialize();
  }

  /**
   * This method initializes this
   * 
   */
  private void initialize() {
        lTreffgenauigkeit = new JLabel();
        lTreffgenauigkeit.setText("Treffergenauigkeit");
        lTreffgenauigkeit.setHorizontalAlignment(SwingConstants.CENTER);
        lDateiname = new JLabel();
        lDateiname.setText("Dateiname");
        lDateiname.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(200, 200));
        this.add(lDateiname, BorderLayout.SOUTH);
        this.add(new Thumbnail(this.thumbnail), BorderLayout.CENTER);
        this.add(lTreffgenauigkeit, BorderLayout.NORTH);
  		
  }
}
