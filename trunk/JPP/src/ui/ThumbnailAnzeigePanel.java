package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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

import ui.listener.BildAusgewaehltListener;

import merkmale.DateipfadMerkmal;
import merkmale.Merkmal;
import merkmale.ThumbnailMerkmal;

import core.BildDokument;

/**
 * Ein Objekt der Klasse stellt eine Anzeige in einem Fenster dar
 * in dem ein Thumbnail und der dazugehoerige Dateiname angezeigt wird.
 * Die Groesze dieses Objektes ist variabel.
 */
public class ThumbnailAnzeigePanel extends JPanel {

  /** Enthaelt den Systemseperator fuer das Trennzeichen von Dateien. */ 
  private static final String PFADSEPARATOR = System.getProperty("file.separator");
  
  private List<BildAusgewaehltListener> listener = null;
  
  /** Enthaelt das Label fuer den Dateinamen des Bildes. */
  private JLabel lDateiname = null;
  
  /** Enthaelt den Listenindex den dieses Objekt in der Liste von
   * Anzeigepaneln hat.
   */
  private int listenindex;
  
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
      List<Observer> observer, int index) {
  	super();
    initialize(dok, groesze, observer, index);
  }
  
  /**
   * Setzt den Status fuer dieses Objekt, ob es fokussiert ist oder nicht.
   * 
   * @param istFokussiert  enthaelt den Status fuer den Fokus
   */
  public void setzeFokus(boolean istFokussiert) {
    
    this.istAusgewaehlt = istFokussiert;
    if (this.istAusgewaehlt) {
      this.setBorder(new LineBorder(new Color(80, 200, 80), 2));
      fireBildAusgewaehlt();
    } else {
      this.setBorder(new LineBorder(new Color(255, 255, 255), 2));
    }
    
    this.observable.setChanged();
    this.observable.notifyObservers(this);
    this.observable.clearChanged();
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
      if (dateiname.length() > 4) {
        this.lDateiname.setText(dateiname.substring(0, dateiname.length() - 4));
      } else {
        this.lDateiname.setText("currywurst");
      }
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
  
  private static String gibDateinamen(String pfad) {
    
    String dateiname = "";
    int posLetzterSeparator = pfad.lastIndexOf(PFADSEPARATOR);
    if (posLetzterSeparator >= 0) {
      dateiname = pfad.substring(posLetzterSeparator + 1);
    } else {
      dateiname = pfad.substring(0);
    }
    return dateiname;
  }

  /**
   * This method initializes this
   * 
   */
  private void initialize(BildDokument dok, int groesze,
      List<Observer> observer, int index) {

    this.listenindex = index;
    this.listener = new ArrayList<BildAusgewaehltListener>();
    this.dok = dok;
    dateiname = gibDateinamen((String)
        dok.getMerkmal(DateipfadMerkmal.FELDNAME).getWert());
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
    this.setBackground(Color.WHITE);
    this.setBorder(new LineBorder(new Color(255, 255, 255), 2));
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
  
  public void addBildAusgewaehltListener(BildAusgewaehltListener l) {
    this.listener.add(l);
  }
  
  public void removeBildAusgewaehltListener(BildAusgewaehltListener l) {
    this.listener.remove(l);
  }
  
  public void fireBildAusgewaehlt() {
    for (BildAusgewaehltListener l : listener) {
      l.setzeZuletztAusgewaehltesBild(this, this.listenindex);
    }
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
