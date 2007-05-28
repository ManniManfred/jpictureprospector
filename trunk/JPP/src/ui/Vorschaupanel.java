package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import core.BildDokument;

public class Vorschaupanel extends JPanel implements Observer {
  
  private static final String DATEIPFADMERKMALNAME = "Dateipfad";  //  @jve:decl-index=0:

  private static final long serialVersionUID = 1L;
  
  private Image bild = null;
  
  /**
   * This is the default constructor
   */
  public Vorschaupanel() {
    super();
    initialize();
  }
  
  public void update(Observable o, Object arg) {
    if (arg instanceof BildDokument) {
      BildDokument dok = (BildDokument) arg;
      String dateipfad = (String) dok.getMerkmal(DATEIPFADMERKMALNAME).getWert();
      try {
        bild = readImage(new File(dateipfad));
        repaint();
      } catch (IOException e) {
        
      }
    }
  }
  
  public static BufferedImage readImage(Object source)
    throws IOException {
  
    ImageInputStream stream = 
        ImageIO.createImageInputStream(source);
    ImageReader reader = 
        (ImageReader) ImageIO.getImageReaders(stream).next();
    reader.setInput(stream);
    ImageReadParam param = reader.getDefaultReadParam();
  
    ImageTypeSpecifier typeToUse = null;
    for (Iterator i = reader.getImageTypes(0); i.hasNext(); ) {
        ImageTypeSpecifier type = (ImageTypeSpecifier) i.next();
        if( type.getColorModel().getColorSpace().isCS_sRGB() ) {
            typeToUse = type;
        }
    }
  
    if (typeToUse!=null) param.setDestinationType(typeToUse);
  
    BufferedImage b = reader.read(0, param);
  
    reader.dispose();
    stream.close();
    return b;
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
