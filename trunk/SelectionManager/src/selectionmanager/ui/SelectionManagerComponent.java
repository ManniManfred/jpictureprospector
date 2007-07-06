package selectionmanager.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import selectionmanager.Auswaehlbar;
import selectionmanager.AuswahlListener;
import selectionmanager.Einstellungen;
import selectionmanager.EventLayer;
import selectionmanager.SelectionManager;

/**
 * Ein Objekt SelectionManagerComponent stellt die graphische Komponente
 * des SelectionManagers dar, in dem alle Funktionalität enthalten ist.
 * 
 * @author Manfred Rosskamp
 */
public class SelectionManagerComponent extends JLayeredPane 
    implements Scrollable {
  
  

  /** Enthaelt den Container, in dem die Selectables vorhanden sind. */
  private JComponent containerLayer;

  /** Enthaelt den Layer, um events abzufangen. */
  private JComponent eventLayer;

  /** Enthaelt den Layer, in dem z.B das Rechteck gezeichnet wird. */
  private PaintLayer paintLayer;

  
  
  /** 
   * Enthaelt den richtigen SelectionManager, an dem die meisten Aufrufe
   * weitergeleitet werden.
   */
  private SelectionManager manager;
  
  /**
   * Erzeugt eine neue SelectionManagerComponent.
   */
  public SelectionManagerComponent() {
    manager = new SelectionManager();
    
    init();
  }
  
  /**
   * Initialisiert diese Komponente.
   */
  private void init() {
    
    this.setLayout(new OverAllLayout());
    
    /*
     * Container, in dem die ganzen auswaehlbaren Elemente vorhanden sind, wird
     * der LayeredPane in der StandardEbene hinzugefuegt
     */
    containerLayer = new ScrollableFlowPanel();
    containerLayer.setBackground(Einstellungen.containerBG);
    containerLayer.setLayout(new FlowLayout(FlowLayout.LEADING, 
        Einstellungen.xAbstand,
        Einstellungen.yAbstand));
    this.add(containerLayer);


    /* PaintLayer erzeugen */
    paintLayer = new PaintLayer();
    paintLayer.setOpaque(false);
    this.add(paintLayer, new Integer(JLayeredPane.DEFAULT_LAYER + 5));


    /* Eventlayer erzeugen */
    eventLayer = new EventLayer(manager.getSelectionModel(), containerLayer, 
        paintLayer);
    eventLayer.setOpaque(false);
    eventLayer.setFocusable(true);
    this.add(eventLayer, new Integer(JLayeredPane.DEFAULT_LAYER + 10));

    
  }

  /**
   * ContainerLayer, indem alle auswaehlbaren Elemente angezeigt werden. 
   * @return ContainerLayer, indem alle auswaehlbaren Elemente angezeigt werden. 
   */
  public JComponent getContainerLayer() {
    return containerLayer;
  }
  
  /**
   * Fuegt ein auswaehlbares/selektierbares Element diesem Mangager hinzu.
   * 
   * @param a das auswaehlbare Element
   */
  public void addAuswaehlbar(AuswaehlbaresJPanel a) {
    manager.addAuswaehlbar(a);
    containerLayer.add(a);
    containerLayer.revalidate();
  }

  /**
   * Entfernt das auswaehlbare Element wieder.
   * 
   * @param a das auswaehlbare Element
   */
  public void removeAuswaehlbar(AuswaehlbaresJPanel a) {
    manager.removeAuswaehlbar(a);
    containerLayer.remove(a);
  }

  
  /**
   * Entfernt alle auswaehlbaren Elemente aus diesem SelectionManager.
   */
  public void removeAllAuswaehlbar() {
    int anzahl = containerLayer.getComponentCount();
    
    for (int i = 0; i < anzahl; i++) {
      AuswaehlbaresJPanel a = 
        (AuswaehlbaresJPanel) containerLayer.getComponent(i);
      manager.removeAuswaehlbar(a);
    }
    
    containerLayer.removeAll();
  }

  /** ********* Methodenaufrufe, die weiter zum Manager gereicht werden ******* */

  /**
   * Gibt die Liste mit allen Auswaehlbaren Elementen zurueck.
   * 
   * @return Liste mit allen Auswaehlbaren Elementen
   */
  public List<? extends AuswaehlbaresJPanel> gibAlleAuswaehlbaren() {
    return (List<? extends AuswaehlbaresJPanel>) manager.gibAlleAuswaehlbaren();
  }
  
  
  /**
   * Gibt die aktuell ausgewaehlten Elemente zurueck.
   * 
   * @return Menge der aktuell ausgewaehlten Elemente
   */
  public Set<AuswaehlbaresJPanel> gibAlleAusgewaehlten() {
    return (Set<AuswaehlbaresJPanel>) manager.gibAlleAusgewaehlten();
  }
  

  /**
   * Entfernt oder leert die aktuelle Auswahl.
   */
  public void leereAuswahl() {
    manager.leereAuswahl();
  }

  /**
   * Waehlt alle auswaehlbaren Elemente aus.
   */
  public void waehleAlleAus() {
    manager.waehleAlleAus();
  }

  
  /**
   * Gibt das Element, welches markiert ist, zurueck.
   * 
   * @return Element, welches markiert ist
   */
  public Auswaehlbar getMarkiert() {
    return manager.getMarkiert();
  }

  /**
   * Fuegt einen Listener hinzu, der informiert werden moechte, wenn sich die
   * Auswahl geaendert hat.
   * 
   * @param l
   */
  public void addAuswahlListener(AuswahlListener l) {
    manager.addAuswahlListener(l);
  }

  /**
   * Entfernt den Listener wieder.
   * 
   * @param l
   */
  public void removeAuswahlListener(AuswahlListener l) {
    manager.removeAuswahlListener(l);
  }

  
  
  
  
  
  
  
  
  
  /********************************************************************
   * Methoden, um diese Komponente vernuenftig in einer 
   * JScrollPane darstellen zu koennen.
   */
  
  
  
  public Dimension getPreferredScrollableViewportSize() {
    return super.getPreferredSize();
  }

  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation,
      int direction) {
    int hundredth = (orientation == SwingConstants.VERTICAL
        ? getParent().getHeight()
        : getParent().getWidth()) / 100;
    return (hundredth == 0
        ? 1
        : hundredth);
  }

  public int getScrollableBlockIncrement(Rectangle visibleRect,
      int orientation, int direction) {
    return orientation == SwingConstants.VERTICAL
        ? getParent().getHeight()
        : getParent().getWidth();
  }

  public boolean getScrollableTracksViewportWidth() {
    return true;
  }

  public boolean getScrollableTracksViewportHeight() {
    return false;
  }
}
