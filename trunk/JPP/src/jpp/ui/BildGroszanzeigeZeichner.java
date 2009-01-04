package jpp.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;

import jpp.core.BildDokument;
import jpp.merkmale.BildbreiteMerkmal;
import jpp.merkmale.BildhoeheMerkmal;
import jpp.merkmale.DateipfadMerkmal;
import jpp.ui.listener.BildGeladenListener;
import jpp.utils.URLUtils;


/**
 * Ein Objekt der Klasse zeichnet aus einer Datei ein Bild in den eigenen
 * Anzeigebereich.
 */
class BildGroszanzeigeZeichner extends JLabel {

  /**
   * Generated uid.
   */
  private static final long serialVersionUID = -4860679070895335151L;


  /** Logger, der alle Fehler loggt. */
  Logger logger = Logger.getLogger("jpp.ui.BildGroszanzeige");


  /** Enthaelt das Dokument, dass den Pfad zur Datei enthaelt. */
  private BildDokument dok = null;

  /**
   * Enthaelt eine Liste an Listenern die darauf reagieren, wenn ein Bild
   * geladen wurde.
   */
  private List<BildGeladenListener> listener = null;

  /** Enthaehlt das anzuzeigende Bild. */
  private Image bild = null;

  /** Enthaelt die Skalierung die das Bild erfahren soll. */
  private double skalierungProzent;

  /**
   * Enthaelt die Information ob das Bild an die Groesze des Panels angepasst
   * werden muss.
   */
  private boolean mussAngepasstWerden;

  /**
   * Erzeugt ein neues Objekt der Klasse mit den entsprechenden Daten.
   * 
   * @param dok das Bilddokument, dass den Pfad zur Datei enthaelt
   * @param skalierung enthaelt die Skalierung in Prozent, die das Bild Bild
   *          erfahren soll
   */
  public BildGroszanzeigeZeichner(BildDokument dok, double skalierung,
      boolean mussAngepasstWerden) {
    this.dok = dok;
    this.listener = new ArrayList<BildGeladenListener>();
    this.skalierungProzent = skalierung;
    this.mussAngepasstWerden = mussAngepasstWerden;
    setzeDok(dok);
    setAutoscrolls(true);
    // super.setIcon(new ImageIcon(bild));
    initializeMouseInputAdapter();
  }

  /**
   * Initialisiert den <code>MouseInputAdapter</code> der fuer das scrollen
   * innerhalb der ScrollPane verantwortlich ist, wenn die Maus entsprechend
   * bewegt wurde.
   */
  private void initializeMouseInputAdapter() {

    // Enthaelt den MouseInputAdapter fuer das Scrollen
    MouseInputAdapter mia = new MouseInputAdapter() {

      /** Enthaelt die letzte X-Position wenn die Maus gedrueckt wurde. */
      private int lastX;

      /** Enthaelt die letzte Y-Position wenn die Maus gedrueckt wurde. */
      private int lastY;

      /** Enthaelt das parent dieses Objektes. */
      private Container parent;

      public void mouseDragged(MouseEvent e) {

        parent = BildGroszanzeigeZeichner.this.getParent();
        if (parent instanceof JViewport) {
          JViewport jv = (JViewport) parent;
          Point p = jv.getViewPosition();
          int newX = p.x - (e.getX() - lastX);
          int newY = p.y - (e.getY() - lastY);
          int maxX = BildGroszanzeigeZeichner.this.getWidth() - jv.getWidth();
          int maxY = BildGroszanzeigeZeichner.this.getHeight() - jv.getHeight();
          if (newX < 0) {
            newX = 0;
          }
          if (newX > maxX) {
            newX = maxX;
          }
          if (newY < 0) {
            newY = 0;
          }
          if (newY > maxY) {
            newY = maxY;
          }
          jv.setViewPosition(new Point(newX, newY));
        }
      }

      public void mousePressed(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        lastX = e.getX();
        lastY = e.getY();
      }

      public void mouseReleased(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      }
    };

    addMouseMotionListener(mia);
    addMouseListener(mia);
  }

  /**
   * Fuegt einen <code>BildGeladenListener</code> zur Liste der Listener
   * hinzu.
   * 
   * @param l der hinzuzufuegende Listener
   */
  public void addBildGeladenListener(BildGeladenListener l) {
    listener.add(l);
  }

  /**
   * Loescht einen <code>BildGeladenListener</code> aus der Liste der
   * Listener.
   * 
   * @param l der zu loeschende Listener
   */
  public void removeBildGeladenListener(BildGeladenListener l) {
    if (l != null && listener.contains(l)) {
      listener.remove(l);
    }
  }

  /**
   * Wird aufgerufen, wenn das anzuzeigende Bild geladen wurde und
   * benachrichtigt alle Listener
   */
  public void fireBildGeladen() {
    for (BildGeladenListener l : listener) {
      l.bildWurdeGeladen();
    }
  }

  /**
   * Setzt das Dokument in der Anzeige neu.
   * 
   * @param dok das Dokument mit den benoetigten Informationen
   */
  public void setzeDok(BildDokument dok) {
    this.dok = dok;
    try {
      URL url = (URL) dok.getMerkmal(DateipfadMerkmal.FELDNAME)
      .getWert();
      url = URLUtils.getEncodedURL(url);
      
      this.bild = ImageIO.read(url);
      fireBildGeladen();
    } catch (IOException e) {
      logger.log(Level.WARNING, "Das Bild konnte nicht geladen werden.", e);
      this.bild = null;
    }
    this.repaint();
  }

  /**
   * Setzt die Skalierung die das Bild erfahren soll neu und zeichnet das Bild
   * dementsprechend.
   * 
   * @param skalierung die Skalerierung in Prozent
   */
  public void setzeSkalierung(double skalierung) {

    this.skalierungProzent = skalierung;
    this.repaint();
  }

  /**
   * Setzt den Status ob das Bild der Fenstergroesze angepasst werden soll oder
   * nicht.
   * 
   * @param mussAngepasstWerden <code>true</code> wen das Bild der
   *          Fenstergroesze angepasst werden soll
   */
  public void setzeAnpassung(boolean mussAngepasstWerden) {

    this.mussAngepasstWerden = mussAngepasstWerden;
    this.repaint();
  }

  /**
   * Zeichnet das Bild in die Oberflaeche, mit den entsprechenden Abmessungen.
   */
  protected void paintComponent(Graphics g) {

    int dieseBreite = getPreferredSize().width;
    int dieseHoehe = getPreferredSize().height;

    if (bild == null) {
      // Add your drawing instructions here
      g.setColor(Color.red);
      String msg = "Das Bild "
          + ((URL) dok.getMerkmal(DateipfadMerkmal.FELDNAME).getWert())
              .toString() + " konnte nicht geladen werden.";

      // Create the font and pass it to the Graphics context
      g.setFont(new Font("Monospaced", Font.BOLD, 12));

      // Get measures needed to center the message
      FontMetrics fm = g.getFontMetrics();

      // How many pixels wide is the string
      int msg_width = fm.stringWidth(msg);

      // How far above the baseline can the font go?
      int ascent = fm.getMaxAscent();

      // How far below the baseline?
      int descent = fm.getMaxDescent();

      // Use the string width to find the starting point
      int msg_x = dieseBreite / 2 - msg_width / 2;

      // Use the vertical height of this font to find
      // the vertical starting coordinate
      int msg_y = dieseHoehe / 2 - descent / 2 + ascent / 2;

      g.drawString(msg, msg_x, msg_y);
    } else {
      double originalBreite = (Integer) dok.getMerkmal(
          BildbreiteMerkmal.FELDNAME).getWert();
      double originalHoehe = (Integer) dok
          .getMerkmal(BildhoeheMerkmal.FELDNAME).getWert();
      double hoeheBild = bild.getHeight(this);
      double breiteBild = bild.getWidth(this);

      if (!mussAngepasstWerden) {

        breiteBild = originalBreite * skalierungProzent / 100;
        hoeheBild = originalHoehe * skalierungProzent / 100;

        g.drawImage(bild, (int) (dieseBreite - breiteBild) / 2,
            (int) (dieseHoehe - hoeheBild) / 2, (int) breiteBild,
            (int) hoeheBild, this);
      } else if (originalBreite <= dieseBreite && originalHoehe <= dieseHoehe) {

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
    }
  }
}