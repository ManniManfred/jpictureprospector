package selectionmanager;


import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import selectionmanager.selectionmodel.MultipleSelectionModel;
import selectionmanager.selectionmodel.SelectionModel;
import selectionmanager.ui.OverAllLayout;
import selectionmanager.ui.PaintLayer;


/**
 * Ein Objekt dieser Klasse stellt ein SelectionManager dar.
 * 
 * @author Manfred Rosskamp
 */
public class SelectionManager {



  /** Enthaelt den Container, in dem die Selectables vorhanden sind. */
  private JComponent containerLayer;

  /** Enthaelt den Layer, um events abzufangen. */
  private JComponent eventLayer;

  /** Enthaelt den Layer, in dem z.B das Rechteck gezeichnet wird. */
  private PaintLayer paintLayer;

  /** Enthaelt die Layered Pane */
  private JLayeredPane layeredPane;

  /**
   * Model in dem gespeichert ist, welche Elemente ausgewählt oder markiert
   * sind.
   */
  private SelectionModel model;


  /**
   * Controller, der die Events von den einzelnen auswaehlbaren Element in
   * Aenderungen am Model umwandelt.
   */
  private AuswaehlbaresChangedListenerImpl controller;


  /**
   * Erzeugt ein neues DefaultSelectionModel mit dem type SINGLESELECTION.
   */
  public SelectionManager() {
    
    this.model = new MultipleSelectionModel();

    controller = new AuswaehlbaresChangedListenerImpl(model);
  }


  /**
   * Fuegt ein auswaehlbares/selektierbares Element diesem Mangager hinzu.
   * 
   * @param a das auswaehlbare Element
   */
  public void addAuswaehlbar(Auswaehlbar a) {
    model.addAuswaehlbar(a);
    a.setAuswaehlbaresChangedListener(controller);
  }

  /**
   * Entfernt das auswaehlbare Element wieder.
   * 
   * @param a das auswaehlbare Element
   */
  public void removeAuswaehlbar(Auswaehlbar a) {
    a.setAuswaehlbaresChangedListener(null);
    model.removeAuswaehlbar(a);
  }



  /**
   * Setzt einen Container, in dem die ganzen auswaehlbaren Element vorhanden
   * sind, um Funktionalitaeten wie ein Rahmen ziehen usw. zu haben.
   * 
   * @param p Container, in dem die auswaehlbaren Elemente vorhanden sind
   */
  public void setContainerMitAuswaehlbaren(JComponent comp) {

    /* LayeredPane erzeugen */
    layeredPane = new JLayeredPane();
    layeredPane.setLayout(new OverAllLayout());

    /*
     * Container, in dem die ganzen auswaehlbaren Elemente vorhanden sind, wird
     * der LayeredPane in der StandardEbene hinzugefuegt
     */
    containerLayer = comp;
    layeredPane.add(containerLayer);


    /* PaintLayer erzeugen */
    paintLayer = new PaintLayer();
    paintLayer.setOpaque(false);
    layeredPane.add(paintLayer, new Integer(JLayeredPane.DEFAULT_LAYER + 5));


    /* Eventlayer erzeugen */
    eventLayer = new EventLayer(model, containerLayer, paintLayer);
    eventLayer.setOpaque(false);
    eventLayer.setFocusable(true);
    layeredPane.add(eventLayer, new Integer(JLayeredPane.DEFAULT_LAYER + 10));

  }


  /**
   * Gibt die Komponente, die die Ansicht mit den auswaehlbaren Elementen
   * enthaelt zurueck.
   * 
   * @return Komponente, die die Ansicht mit den auswaehlbaren Elementen
   *         enthaelt
   */
  public JComponent getAnzeigeKomponente() {
    return layeredPane;
  }



  /** ********* Methodenaufrufe, die weiter zum Model gereicht werden ******* */

  
  /**
   * Gibt die aktuell ausgewaehlten Elemente zurueck.
   * 
   * @return Menge der aktuell ausgewaehlten Elemente
   */
  public Set<Auswaehlbar> gibAlleAusgewaehlten() {
    return model.gibAlleAusgewaehlten();
  }
  

  /**
   * Entfernt oder leert die aktuelle Auswahl.
   */
  public void leereAuswahl() {
    model.leereAuswahl();
  }

  /**
   * Waehlt alle auswaehlbaren Elemente aus.
   */
  public void waehleAlleAus() {
    model.waehleAlleAus();
  }

  
  /**
   * Gibt das Element, welches markiert ist, zurueck.
   * 
   * @return Element, welches markiert ist
   */
  public Auswaehlbar getMarkiert() {
    return model.getMarkiert();
  }

  /**
   * Fuegt einen Listener hinzu, der informiert werden moechte, wenn sich die
   * Auswahl geaendert hat.
   * 
   * @param l
   */
  public void addAuswahlListener(AuswahlListener l) {
    model.addAuswahlListener(l);
  }

  /**
   * Entfernt den Listener wieder.
   * 
   * @param l
   */
  public void removeAuswahlListener(AuswahlListener l) {
    model.removeAuswahlListener(l);
  }

}
