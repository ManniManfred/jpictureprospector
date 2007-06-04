package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import merkmale.DateipfadMerkmal;
import merkmale.Merkmal;
import merkmale.ThumbnailMerkmal;

import core.BildDokument;

public class ThumbnailAnzeigePanel extends JPanel {

  /** Enthaelt das Label fuer den Dateinamen des Bildes. */
  private JLabel lDateiname = null;
  
  /** Enthaelt das Panel fuer die Anzeige des Thumbnails. */
  private ThumbnailAnzeige thumbnailAnzeige = null;
  
  /** Enthaelt alle Observer, die Aenderungen an diesem Objekt beobachten. */
  protected ObservableJPP observable = null;  //  @jve:decl-index=0:
  
  /** Enthaelt die Information ob dieses Objekt ausgewaehlt ist. */
  private boolean istAusgewaehlt;
  
  /** Enthaelt das anzuzeigende BildDokument. */
  private BildDokument dok = null;
  
  /** Enthaelt den Dateinamen fuer das Bilddokument. */
  private String dateiname = null;

  /**
   * This method initializes 
   * 
   */
  public ThumbnailAnzeigePanel(BildDokument dok, int groesze,
      List<Observer> observer) {
  	super();
    initialize(dok, groesze, observer);
  }
  
  /**
   * Setzt den Status fuer dieses Objekt, ob es fokussiert ist oder nicht.
   * 
   * @param istFokussiert  enthaelt den Status fuer den Fokus
   */
  public void setzeFokus(boolean istFokussiert) {
    
    this.istAusgewaehlt = istFokussiert;
    if (this.istAusgewaehlt) {
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
    this.setzeDateinamen(this.dateiname, groesze);
    this.revalidate();
  }
  
  /**
   * Setzt den Text des Labels neu.
   * 
   * @param text  der Text des Textfeldes
   */
  public void setzeDateinamen(String dateiname, int groesze) {
    
    this.dateiname = dateiname;
    Font font = getFont();
    FontMetrics metrics = getFontMetrics(font);
    int dateinameBreite = metrics.stringWidth(dateiname);
    String dateinameNeu = "";
    String dateiEndung = "...";
    if (dateinameBreite > groesze) {
      int i = 0;
      while (metrics.stringWidth(dateinameNeu + dateiEndung) < 
               groesze - 3 * metrics.stringWidth(dateiEndung)) {
        dateinameNeu += dateiname.substring(i, i + 1);
        i++;
      }
      this.lDateiname.setText(dateinameNeu + dateiEndung);
    } else {
      this.lDateiname.setText(dateiname.substring(0, dateiname.length() - 4));
    }
  }
  
  /**
   * Liefert das <code>BildDokument</code> dieses Objekts.
   * 
   * @return  das das <code>BildDokument</code> dieses Objekts
   */
  public BildDokument gibBildDokument() {
    return this.dok;
  }
  
  /**
   * Liefert das angezeigte <code>Image</code> des Objekts.
   * 
   * @return  das angezeigte <code>Image</code>des Objekts.
   */
  public Image gibBild() {
    return this.thumbnailAnzeige.gibBild();
  }
  
  /**
   * Liefert die Information ob das Objekt ausgewaehlt ist oder nicht.
   * 
   * @return <code>true</code> wenn das Objekt ausgewaehlt ist.
   */
  public boolean istAusgewaehlt() {
    return this.istAusgewaehlt;
  }

  /**
   * This method initializes this
   * 
   */
  private void initialize(BildDokument dok, int groesze, List<Observer> observer) {

    this.dok = dok;
    dateiname = (String) dok.getMerkmal(DateipfadMerkmal.FELDNAME).getWert();
    ThumbnailMerkmal m = 
      (ThumbnailMerkmal) dok.getMerkmal(ThumbnailMerkmal.FELDNAME);
    this.thumbnailAnzeige = new ThumbnailAnzeige(m.getThumbnail(), dok);
    this.observable = new ObservableJPP();
    for (Observer o : observer) {
      observable.addObserver(o);
    }
    lDateiname = new JLabel();
    lDateiname.setText("");
    lDateiname.setHorizontalAlignment(SwingConstants.CENTER);
    this.setLayout(new BorderLayout());
    this.setSize(new Dimension(groesze, groesze + 20));
    this.setBorder(new LineBorder(Color.GRAY, 1));
    this.setFocusable(true);
    this.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent e) {
        if (istAusgewaehlt) {
          setzeFokus(false);
        } else {
          setzeFokus(true);
          requestFocusInWindow();
        }
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
