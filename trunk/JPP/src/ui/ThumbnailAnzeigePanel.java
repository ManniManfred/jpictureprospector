package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Observable;
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

  /** Enthaelt das Label fuer den Dateinamen des Bildes. */
  private JLabel lDateiname = null;
  
  /** Enthaelt das Panel fuer die Anzeige des Thumbnails. */
  private ThumbnailAnzeige thumbnailAnzeige = null;
  
  /** Enthaelt alle Observer, die Aenderungen an diesem Objekt beobachten. */
  protected ObservableJPP observable = null;
  
  /** Enthaelt die Information ob dieses Objekt ausgewaehlt ist. */
  private boolean istFokussiert;
  
  /** Enthaelt das anzuzeigende BildDokument. */
  private BildDokument dok = null;

  /**
   * This method initializes 
   * 
   */
  public ThumbnailAnzeigePanel(BildDokument dok, int groesze, Observer observer) {
  	super();
    initialize(dok, groesze, observer);
  }
  
  /**
   * Setzt den Status fuer dieses Objekt, ob es fokussiert ist oder nicht.
   * 
   * @param istFokussiert  enthaelt den Status fuer den Fokus
   */
  public void setzeFokus(boolean istFokussiert) {
    
    this.istFokussiert = istFokussiert;
    if (this.istFokussiert) {
      this.setBorder(new LineBorder(Color.BLUE, 2, true));
      this.observable.setChanged();
      this.observable.notifyObservers(dok);
      this.observable.clearChanged();
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
    this.lDateiname.setText(dateiname);
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
  private void initialize(BildDokument dok, int groesze, Observer observer) {
    
    ThumbnailMerkmal m = (ThumbnailMerkmal) dok.getMerkmal(THUMBNAILMERKMALSNAME);
    this.thumbnailAnzeige = new ThumbnailAnzeige(m.getThumbnail());
    this.observable = new ObservableJPP();
    observable.addObserver(observer);
    this.dok = dok;
    lDateiname = new JLabel();
    lDateiname.setText("");
    lDateiname.setHorizontalAlignment(SwingConstants.CENTER);
    this.setLayout(new BorderLayout());
    this.setSize(new Dimension(groesze, groesze + 20));
    this.setBorder(new LineBorder(Color.GRAY, 1));
    this.setFocusable(true);
    this.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent e) {
        if (istFokussiert) {
          setzeFokus(false);
        } else {
          setzeFokus(true);
          requestFocusInWindow();
        }
      }
    });
    this.addFocusListener(new java.awt.event.FocusAdapter() {   
    	public void focusLost(java.awt.event.FocusEvent e) {    
    		setzeFokus(false);
    	}
    });
    this.add(this.thumbnailAnzeige, BorderLayout.CENTER);
    this.add(lDateiname, BorderLayout.SOUTH);
  }
}

/**
 * Stellt einen Workaround dar, da in der Klasse <code>Observable</code>
 * die Methoden fuer <code>setChanged</code> und <code>clearChanged</code>
 * protected sind und das Anzeigepanel nicht von Observable abgeleitet
 * werden kann.
 * 
 * @author nils verheyen
 *
 */
class ObservableJPP extends Observable {
  
  public void setChanged() {
    super.setChanged();
  }
  
  public void clearChanged() {
    super.clearChanged();
  }
}
