package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import merkmale.Merkmal;
import merkmale.ThumbnailMerkmal;

import core.BildDokument;

public class ThumbnailAnzeigePanel extends JPanel {
  
  /** Entspricht der Konstante <code>FELDNAME</code> in der Klasse
   * <code>ThumbnailMerkmal</code>.
   */
  private static final String THUMBNAILMERKMALSNAME = "Thumbnail";

  private JLabel lDateiname = null;
  
  private String dateiname = null;
  
  private ThumbnailAnzeige thumbnailAnzeige = null;
  
  private int groesze;
  
  private boolean istFokussiert;

  /**
   * This method initializes 
   * 
   */
  public ThumbnailAnzeigePanel(BildDokument dok, int groesze, Observer observer) {
  	super();
    this.groesze = groesze;
    ThumbnailMerkmal m = (ThumbnailMerkmal) dok.getMerkmal(THUMBNAILMERKMALSNAME);
    this.thumbnailAnzeige = new ThumbnailAnzeige(m.getThumbnail(), observer);
    initialize();
  }
  
  /**
   * Setzt den Status fuer dieses Objekt, ob es fokussiert ist oder nicht.
   * 
   * @param istFokussiert  enthaelt den Status fuer den Fokus
   */
  public void setzeFokus(boolean istFokussiert) {
    
    this.istFokussiert = istFokussiert;
    if (this.istFokussiert) {
      this.setBorder(new LineBorder(Color.GREEN, 2));
    } else {
      this.setBorder(new LineBorder(Color.GRAY, 1));
    }
    this.thumbnailAnzeige.repaint();
  }
  
  /**
   * Setzt die Groesze dieses Feldes.
   * 
   * @param groesze  die Groesze die das Feld haben soll
   */
  public void setzeGroesze(int groesze) {
    this.setSize(groesze, groesze + 20);
    this.thumbnailAnzeige.setPreferredSize(new Dimension(groesze, groesze));
    this.thumbnailAnzeige.setzeGroesze(groesze);
    this.revalidate();
  }
  
  /**
   * Setzt den Text des Labels neu.
   * 
   * @param text  der Text des Textfeldes
   */
  public void setzeDateinamen(String dateiname) {
    this.dateiname = dateiname;
    this.lDateiname.setText(this.dateiname);
  }
  
  public Image gibBild() {
    return this.thumbnailAnzeige.gibBild();
  }
  
  public boolean istFokussiert() {
    return this.istFokussiert;
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
    this.setSize(new Dimension(groesze, groesze + 20));
    this.setBorder(new LineBorder(Color.GRAY, 1));
    this.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent e) {
        if (istFokussiert) {
          setzeFokus(false);
        } else {
          setzeFokus(true);
        }
      }
    });
    this.add(this.thumbnailAnzeige, BorderLayout.CENTER);
    this.add(lDateiname, BorderLayout.SOUTH);
  }
}
