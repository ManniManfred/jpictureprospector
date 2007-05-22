package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class ThumbnailPanel extends JPanel {

  private JLabel lDateiname = null;
  
  private Thumbnail thumbnail = null;
  
  private boolean istFokussiert;

  /**
   * This method initializes 
   * 
   */
  public ThumbnailPanel(Image thumbnail, String dateiname) {
  	super();
    this.thumbnail = new Thumbnail(thumbnail);
    this.lDateiname.setText(dateiname);
  	initialize();
  }
  
  public void setzeFokus(boolean istFokussiert) {
    
    this.istFokussiert = istFokussiert;
    if (this.istFokussiert) {
      this.setBorder(new LineBorder(Color.GREEN, 2, true));
    } else {
      this.setBorder(null);
    }
  }
  
  /**
   * Setzt die Groesze dieses Feldes.
   * 
   * @param groesze  die Groesze die das Feld haben soll
   */
  public void setzeGroesze(Dimension groesze) {
    this.setSize(new Dimension((int) groesze.getWidth(),
                     (int) groesze.getHeight() + 20));
    this.thumbnail.setzeGroesze(groesze);
  }

  /**
   * This method initializes this
   * 
   */
  private void initialize() {
        lDateiname = new JLabel();
        lDateiname.setText("");
        lDateiname.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(200, 200));
        this.add(lDateiname, BorderLayout.SOUTH);
        this.add(this.thumbnail, BorderLayout.CENTER);
  		
  }
}
