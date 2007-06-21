package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import merkmale.BildbreiteMerkmal;
import merkmale.BildhoeheMerkmal;
import ui.listener.VorschauBildListener;
import core.BildDokument;

public class Vorschaupanel extends JPanel implements Observer {

  private static final long serialVersionUID = 1L;
  
  /** Enthaelt das Bild, was anzeigt werden soll. */
  private Image bild = null;
  
  /** Enthaelt das <code>BildDokument</code> was die Grundlage fuer die
   * Zeichnung des Bildes bildet.
   */
  private BildDokument dok = null;
  
  /**
   * This is the default constructor
   */
  public Vorschaupanel() {
    super();
    initialize();
  }
  
  /**
   * Liefert das <code>BildDokument</code> dieses Objekts
   * @return
   */
  public BildDokument gibBildDokument() {
    return this.dok;
  }
  
  /**
   * Laedt die Ansicht dieses Objekts neu.
   */
  public void resetAnsicht() {
    this.bild = null;
    this.dok = null;
    repaint();
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
    
    if (arg instanceof ThumbnailAnzeigePanel) {
      
      ThumbnailAnzeigePanel tap = (ThumbnailAnzeigePanel) arg;
      if (tap.istAusgewaehlt()) {
        dok = tap.gibBildDokument();
        final Vorschaubildlader bildlader = new Vorschaubildlader(dok);
        bildlader.addVorschaubildListener(new VorschauBildListener() {
          public void bildGeladen() {
            bild = bildlader.getBild();
            repaint();
          }
        });
        bildlader.start();
      }
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
    this.setBackground(Color.WHITE);
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
    
    if (bild != null && dok != null) {
      double originalBreite = 
        (Integer) dok.getMerkmal(BildbreiteMerkmal.FELDNAME).getWert();
      double originalHoehe = 
        (Integer) dok.getMerkmal(BildhoeheMerkmal.FELDNAME).getWert();
      double hoeheBild = bild.getHeight(this);
      double breiteBild = bild.getWidth(this);
      double dieseBreite = getWidth();
      double dieseHoehe = getHeight();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, getWidth(), getHeight());
    
      if (originalBreite <= dieseBreite && originalHoehe <= dieseHoehe) {
        
        g.drawImage(bild, 
            (int) (dieseBreite - originalBreite) / 2,
            (int) (dieseHoehe - originalHoehe) / 2,
            (int) originalBreite, (int) originalHoehe, this);
      } else if (Math.abs(dieseBreite / breiteBild) < Math.abs(dieseHoehe / hoeheBild)) {
        // Anpassung der Größe an dieses Objekt
        
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
    } else {
      g.setColor(new Color(255, 255, 255));
      g.fillRect(0, 0, getWidth(), getHeight());
    }
  }
}