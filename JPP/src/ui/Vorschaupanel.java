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
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import core.BildDokument;

public class Vorschaupanel extends JPanel implements Observer {
  
  /**
   * Entspricht dem Merkmalsnamen aus der Klasse <code>DateipfadMerkmal</code>.
   * Sollte eine Aenderung erfolgen muss auch in der Klasse die Aenderung
   * erfolgen.
   */
  public static final String DATEIPFADMERKMALNAME = "Dateipfad";  //  @jve:decl-index=0:

  private static final long serialVersionUID = 1L;
  
  /** Enthaelt das Bild, was anzeigt werden soll. */
  private Image bild = null;  //  @jve:decl-index=0:
  
  /**
   * This is the default constructor
   */
  public Vorschaupanel() {
    super();
    initialize();
  }
  
  /**
   * Laedt das Vorschaubild anhand des entsprechenden <code>Observable</code>
   * neu.
   * 
   * @param o  das <code>Observable</code> dass sich geaendert hat
   * @param arg  das entsprechende <code>Object</code> was sich im 
   *        <code>Observable</code> geaendert hat
   */
  public void update(Observable o, Object arg) {
    
    if (arg instanceof BildDokument) {
      
      BildDokument dok = (BildDokument) arg;
      BildladeThread thread = new BildladeThread(dok);
      thread.start();
      try {
        thread.join();
      } catch (InterruptedException e) {
        System.out.println("Der Bildladevorgang wurde unerwartet beendet.\n" +
            e.getMessage());
      }
      bild = thread.getBild();
      repaint();
    }
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
        
        // Breite voll ausgefuellt, Hoehe muss neu berechnet werden
        g.drawImage(bild,
            0,
            (int) (dieseHoehe - hoeheBild * (dieseBreite / breiteBild)) / 2,
            (int) dieseBreite,
            (int) (hoeheBild * (dieseBreite / breiteBild)), this);
      } else {
        
        // Hoehe voll ausgefuellt, Breite muss neu berechnet werden
        g.drawImage(bild, 
            (int) (dieseBreite - breiteBild * (dieseHoehe / hoeheBild)) / 2,
            0,
            (int) (breiteBild * (dieseHoehe / hoeheBild)),
            (int) dieseHoehe, this);
      }
    }
  }
}

/**
 * Ein Objekt der Klasse stellt einen Thread dar, der ein Bild
 * aus einem Bilddokument laedt.
 */
class BildladeThread extends Thread {

  /** Enthaelt das Bilddokument aus dem das Bild geladen werden soll. */
  private BildDokument dok = null;
  
  /** Enthaelt das fertig geladene Bild. */
  private Image bild = null;
  
  public BildladeThread(BildDokument dok) {
    
    this.dok = dok;
  }
  
  /**
   * Startet diesen Thread.
   */
  @Override
  public void run() {
    
    String dateipfad = (String) dok.getMerkmal(Vorschaupanel.DATEIPFADMERKMALNAME).getWert();
    try {
      
      bild = ImageIO.read(new File(dateipfad));
    } catch (IOException e) {
      System.out.println("Das Bild konnte nicht geladen werden.\n" +
          e.getMessage());
    }
  }
  
  /**
   * Liefert das Bild dieses Objekts.
   * 
   * @return <code>Image</code> wenn das Bild geladen wurde ansonsten
   *         <code>null</code>
   */
  public Image getBild() {
    return this.bild;
  }
}
