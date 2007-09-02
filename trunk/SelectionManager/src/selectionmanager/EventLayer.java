package selectionmanager;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import selectionmanager.selectionmodel.SelectionModel;
import selectionmanager.ui.PaintLayer;


public class EventLayer extends JComponent {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 6396080444687302058L;

  // /**
  // * Alle Listener die informiert werden, wenn sich die Auswahl oder
  // * Markierung aendern soll.
  // */
  // private List<EventLayerListener> listeners;


  private static final int LEFT = 0;

  private static final int CENTER = 1;

  private static final int RIGHT = 2;

  private int align = LEFT;


  private Point start;

  /** Enthaelt den Container, in dem die Selectables vorhanden sind. */
  private JComponent containerLayer;


  /**
   * Enthaelt den Layer, um events abzufangen. Dabei handelt es sich um das
   * Objekt <code>this</code>
   */
  private EventLayer eventLayer;


  /** Enthaelt den Layer, in dem z.B das Rechteck gezeichnet wird. */
  private PaintLayer paintLayer;

  /**
   * Model in dem gespeichert ist, welche Elemente ausgewählt oder markiert
   * sind.
   */
  private SelectionModel model;

  /**
   * Erzeugt einen neuen EventLayer.
   */
  public EventLayer(SelectionModel model, JComponent containerLayer,
      PaintLayer paintLayer) {

    this.model = model;
    this.containerLayer = containerLayer;
    this.eventLayer = this;
    this.paintLayer = paintLayer;


    // listeners = new ArrayList<EventLayerListener>();
    init();
  }

  private void init() {

    /* Listener hinzufuegen */
    eventLayer.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        containerKeyPressed(e);
      }
    });

    eventLayer.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        myMousePressed(e);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        myMouseReleased(e);
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        eventLayer.requestFocus();
        containerLayer.getComponentAt(e.getPoint()).dispatchEvent(e);
      }
    });

    eventLayer.addMouseMotionListener(new MouseMotionListener() {

      public void mouseDragged(MouseEvent e) {
        myMouseDragged(e);
      }

      public void mouseMoved(MouseEvent e) {

      }

    });
  }




  private void myMousePressed(MouseEvent e) {
    start = e.getPoint();
  }

  private void myMouseDragged(MouseEvent e) {
    Point ende = e.getPoint();
    Rectangle r = new Rectangle(Math.min(start.x, ende.x), Math.min(start.y,
        ende.y), Math.abs(ende.x - start.x), Math.abs(ende.y - start.y));
    paintLayer.zeichneRechteck(r);

  }

  private void myMouseReleased(MouseEvent e) {
    Point ende = e.getPoint();
    Rectangle mausRect = new Rectangle(Math.min(start.x, ende.x), Math.min(
        start.y, ende.y), Math.abs(ende.x - start.x), Math
        .abs(ende.y - start.y));

    if (!(mausRect.width == 0 || mausRect.height == 0)) {

      /*
       * Alle Elemente, die sich mit dem Rechteck schneiden, werden ausgewaehlt.
       */
      for (Auswaehlbar s : model.gibAlleAuswaehlbaren()) {
        Rectangle r = new Rectangle(s.getX(), s.getY(), s.getWidth(), s
            .getHeight());
        if (mausRect.intersects(r)) {
          model.addAusgewaehlt(s);
          s.setAusgewaehlt(true);
        } else {
          if (!e.isControlDown()) {
            model.removeAusgewaehlt(s);
            s.setAusgewaehlt(false);
          }
        }
      }
      model.fireAuswahlGeaendert();
    }

    paintLayer.zeichneRechteck(null);
  }


  /**
   * Wird aufgerufen, wenn in dem Container eine Taste gedrueckt wurde.
   * 
   * @param e
   */
  private void containerKeyPressed(KeyEvent e) {
    int neuesAngeklickt = model.getMarkiertIndex();
    if (neuesAngeklickt < 0) {
      neuesAngeklickt = 0;
    } else {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          neuesAngeklickt--;
          break;
        case KeyEvent.VK_RIGHT:
          neuesAngeklickt++;
          break;
        case KeyEvent.VK_UP:
          neuesAngeklickt = getNextVerticalElement(neuesAngeklickt, false);
          break;
        case KeyEvent.VK_DOWN:
          neuesAngeklickt = getNextVerticalElement(neuesAngeklickt, true);
          break;
        case KeyEvent.VK_SPACE:
          model.toggleAusgewaehlt(neuesAngeklickt);
          break;

      }
    }
    
    int anzahl = model.getAnzahlAuswaehlbarer();
    
    if (anzahl != 0) {
      model.setMarkiert((neuesAngeklickt + model.getAnzahlAuswaehlbarer())
          % model.getAnzahlAuswaehlbarer());      
    }
  }



  /**
   * Gibt das unter dem Element mit dem Index <code>von</code> liegende
   * Element falls <code>darunterliegendes</code> == <code>true</code> ist,
   * ansonsten das darueberliegende.
   * 
   * @param von
   * @param darunterliegendes
   * @return
   */
  private int getNextVerticalElement(int von, boolean darunterliegendes) {

    /*
     * Falls kein darunterliegendes Element gefunden wurde, bleibe auf der
     * position.
     */
    int ergebnis = von;

    Auswaehlbar s = model.getAuswaehlbar(von);

    if (s == null) {
      return -1;
    }
    
    /*
     * hole die gedachte vertikale Linie. Ihr Wert gibt die x-Koordinate an.
     */
    int linie;
    if (align == LEFT) {
      linie = s.getX() + Einstellungen.rahmenDicke;
    } else if (align == RIGHT) {
      linie = s.getX() + s.getWidth() - Einstellungen.rahmenDicke;
    } else {
      linie = s.getX() + s.getWidth() / 2;
    }

    int untererRand = s.getY();

    int minAbstand = Integer.MAX_VALUE;
    int maxAbstand = Integer.MIN_VALUE;
    int index = 0;

    int maxIndex = von;

    for (Auswaehlbar element : model.gibAlleAuswaehlbaren()) {

      /* Das Element, von dem das untere gesucht ist, ueberspringen */
      if (index != von) {

        /* Wenn das Element die gedachte vertikale Linie schneidet */
        if (linie >= element.getX()
            && linie < element.getX() + element.getWidth()) {
          boolean liegtDarunter = element.getY() >= untererRand;
          int yAbstand = Math.abs(element.getY() - untererRand);

          /*
           * XOR Verknuepfung von liegtDrunter und drunterliegendes.
           */
          boolean verwende = (liegtDarunter & darunterliegendes)
              | ((!liegtDarunter) & (!darunterliegendes));

          /* Berechne das am naechsten liegenden Element */
          if (minAbstand >= yAbstand && verwende) {
            ergebnis = index;
            minAbstand = yAbstand;
          }

          /* Berechne das am entferntesten liegende Element */
          if (maxAbstand < yAbstand && !verwende) {
            maxIndex = index;
            maxAbstand = yAbstand;
          }
        }
      }
      index++;
    }

    /*
     * Wenn kein darunterliegendes gefunden wurde nehme das oberste
     * darueberliegende.
     */
    return (ergebnis == von)
        ? maxIndex
        : ergebnis;
  }



}
