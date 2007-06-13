package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import merkmale.BildbreiteMerkmal;
import merkmale.BildhoeheMerkmal;
import core.BildDokument;

/**
 * Ein Objekt der Klasse stellt ein Thumbnail eines Bildes dar.
 * 
 * @author Nils Verheyen
 */
public class ThumbnailAnzeige extends JPanel {
  
  /** Enthaelt die Standardbreite und -hoehe eines Thumbnails. */
  private static final int STD_GROESZE = 256;

  /** Enthaelt das Thumbnail. */
  private Image thumbnail = null;
  
  /** Enthaelt das anzuzeigende Bilddokument. */
  private BildDokument dok = null;
  
  /** Enthaelt die Groesze in der das Thumbnail angezeigt werden soll. */
  private int groesze;
  
  /**
   * Erzeugt ein neues Objekt der Klasse.
   * 
   * @param thumbnail  enthaelt das anzuzeigende Thumbnail
   */
  public ThumbnailAnzeige(Image thumbnail, BildDokument dok) {
    
    init(dok, thumbnail, STD_GROESZE);
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
  
  /**
   * Initialisiert dieses Objekt mit den entsprechenden Dokumenten.
   * 
   * @param dok  das <code>BildDokument</code>
   * @param thumbnail  das anzuzeigende Thumbnail
   * @param groesze  die Groesze mit der das Thumbnails gezeichnet werden soll
   */
  public void init(BildDokument dok, Image thumbnail, int groesze) {
    
    this.dok = dok;
    this.thumbnail = thumbnail;
    this.groesze = groesze;
    this.setLayout(null);
    this.setSize(groesze, groesze);
    this.repaint();
  }
  
  /**
   * Liefert das im Panel gezeichnete Thumbnail
   * 
   * @return  das Thumbnails des Objekts
   */
  public Image gibBild() {
    return this.thumbnail;
  }
  
  /**
   * Zeichnet das Thumbnail in diese Komponente. Die Groesze des Thumbnails
   * wird entsprechend der Groesze des Panels skaliert, dass die Verhaeltnisse
   * des Originals erhalten bleiben.
   * @param g  das Element mit dem gezeichnet wird
   */
  protected void paintComponent(Graphics g) {
    
    double originalBreite = (Integer) 
                        dok.getMerkmal(BildbreiteMerkmal.FELDNAME).getWert();
    double originalHoehe = (Integer)
                        dok.getMerkmal(BildhoeheMerkmal.FELDNAME).getWert();
    double hoeheBild = this.thumbnail.getHeight(this);
    double breiteBild = this.thumbnail.getWidth(this);
    double dieseBreite = groesze;
    double dieseHoehe = groesze;
    g.setColor(new Color(238, 238, 238));
    g.fillRect(0, 0, getWidth(), getHeight());
    
    if (originalBreite <= dieseBreite && originalHoehe <= dieseHoehe) {
      
      g.drawImage(thumbnail, 
          (int) (dieseBreite - originalBreite) / 2,
          (int) (dieseHoehe - originalHoehe) / 2,
          (int) originalBreite, (int) originalHoehe, this);
    } else if (Math.abs(dieseBreite / breiteBild) < Math.abs(dieseHoehe / hoeheBild)) {
      // Anpassung der Größe an dieses Objekt
      
      // Breite voll ausgefuellt, Hoehe muss neu berechnet werden
      g.drawImage(thumbnail,
          0,
          (int) (dieseHoehe - hoeheBild * (dieseBreite / breiteBild)) / 2,
          (int) dieseBreite,
          (int) (hoeheBild * (dieseBreite / breiteBild)), this);
    } else {
      
      // Hoehe voll ausgefuellt, Breite muss neu berechnet werden
      g.drawImage(thumbnail, 
          (int) (dieseBreite - breiteBild * (dieseHoehe / hoeheBild)) / 2,
          0,
          (int) (breiteBild * (dieseHoehe / hoeheBild)),
          (int) dieseHoehe, this);
    }
  }
}
