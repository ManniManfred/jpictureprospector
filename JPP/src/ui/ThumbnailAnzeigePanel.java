package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;

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
  
  private ThumbnailAnzeige thumbnailAnzeige = null;
  
  private int groesze;
  
  private boolean istFokussiert;

  /**
   * This method initializes 
   * 
   */
  public ThumbnailAnzeigePanel(BildDokument dok, int groesze) {
  	super();
    this.groesze = groesze;
  	initialize();
    ThumbnailMerkmal m = (ThumbnailMerkmal) dok.getMerkmal(THUMBNAILMERKMALSNAME);
    this.thumbnailAnzeige = new ThumbnailAnzeige(m.getThumbnail());
  }
  
  /**
   * Setzt den Status fuer dieses Objekt, ob es fokussiert ist oder nicht.
   * 
   * @param istFokussiert  enthaelt den Status fuer den Fokus
   */
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
  public void setzeGroesze(int groesze) {
    this.setSize(groesze, groesze + 20);
    this.thumbnailAnzeige.setzeGroesze(groesze);
  }
  
  /**
   * Setzt den Text des Labels neu.
   * 
   * @param text  der Text des Textfeldes
   */
  public void setzeText(String text) {
    this.lDateiname.setText(text);
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
    this.add(lDateiname, BorderLayout.SOUTH);
    this.add(this.thumbnailAnzeige, BorderLayout.CENTER);
  }
}
