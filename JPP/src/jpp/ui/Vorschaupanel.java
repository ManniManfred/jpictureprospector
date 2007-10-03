package jpp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import jpp.core.BildDokument;
import jpp.merkmale.BildbreiteMerkmal;
import jpp.merkmale.BildhoeheMerkmal;
import jpp.merkmale.DateipfadMerkmal;
import jpp.ui.listener.VorschauBildListener;
import selectionmanager.Auswaehlbar;
import selectionmanager.AuswahlListener;


public class Vorschaupanel extends JPanel implements AuswahlListener {

  private static final long serialVersionUID = 1L;

  /** Enthaelt das Bild, was anzeigt werden soll. */
  private Image bild = null;

  /**
   * Enthaelt das <code>BildDokument</code> was die Grundlage fuer die
   * Zeichnung des Bildes bildet.
   */
  private BildDokument dok = null;

  /** 
   * BildLader, der in einem anderen Thread das Bild, welches in diesem
   * Panel angezeigt werden soll, läd.
   */
  private Vorschaubildlader bildLader;

  
  /**
   * This is the default constructor
   */
  public Vorschaupanel() {
    super();
    initialize();
    
    bildLader = new Vorschaubildlader();
    bildLader.addVorschaubildListener(new VorschauBildListener() {
      public void bildGeladen() {
        bild = bildLader.getBild();
        repaint();
      }
    });
  }

  /**
   * Liefert das <code>BildDokument</code> dieses Objekts
   * 
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
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(300, 300);
    this.setLayout(new BorderLayout());
    this.setBackground(Color.WHITE);
    this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
    this.repaint();
  }

  
  /**
   * Zeichnet Elemente auf dieses Objekt.
   * 
   * @param g
   */
  public void paintComponent(Graphics g) {
    
    /* Hintergrund auf weiss setzten */
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
    
    if (bild != null && dok != null) {
      double originalBreite = (Integer) dok.getMerkmal(
          BildbreiteMerkmal.FELDNAME).getWert();
      double originalHoehe = (Integer) dok
          .getMerkmal(BildhoeheMerkmal.FELDNAME).getWert();
      double hoeheBild = bild.getHeight(this);
      double breiteBild = bild.getWidth(this);
      double dieseBreite = getWidth();
      double dieseHoehe = getHeight();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, getWidth(), getHeight());

      if (originalBreite <= dieseBreite && originalHoehe <= dieseHoehe) {

        g.drawImage(bild, (int) (dieseBreite - originalBreite) / 2,
            (int) (dieseHoehe - originalHoehe) / 2, (int) originalBreite,
            (int) originalHoehe, this);
      } else if (Math.abs(dieseBreite / breiteBild) < Math.abs(dieseHoehe
          / hoeheBild)) {
        // Anpassung der Größe an dieses Objekt

        // Breite voll ausgefuellt, Hoehe muss neu berechnet werden
        g.drawImage(bild, 0, (int) (dieseHoehe - hoeheBild
            * (dieseBreite / breiteBild)) / 2, (int) dieseBreite,
            (int) (hoeheBild * (dieseBreite / breiteBild)), this);
      } else {

        // Hoehe voll ausgefuellt, Breite muss neu berechnet werden
        g.drawImage(bild, (int) (dieseBreite - breiteBild
            * (dieseHoehe / hoeheBild)) / 2, 0,
            (int) (breiteBild * (dieseHoehe / hoeheBild)), (int) dieseHoehe,
            this);
      }
    } else {
      g.setColor(new Color(255, 255, 255));
      g.fillRect(0, 0, getWidth(), getHeight());
    }
  }

  
  
  public void auswahlGeaendert(Set<Auswaehlbar> ausgewaehlten) {
    /* Nix aendern */
  }


  /**
   * Laedt das Vorschaubild neu.
   * 
   * @param neueMarkierung das entsprechende TAP
   */
  public void markierungWurdeBewegt(Auswaehlbar neueMarkierung) {
    
    if (neueMarkierung instanceof ThumbnailAnzeigePanel) {

      ThumbnailAnzeigePanel tap = (ThumbnailAnzeigePanel) neueMarkierung;
      dok = tap.gibBildDokument();
      
      String dateipfad = tap.gibBildDokument().getMerkmal(
                          DateipfadMerkmal.FELDNAME).getWert().toString();
      
      bildLader.startLadeBild(dateipfad, this.getWidth(), this.getHeight());
    }
  }

}