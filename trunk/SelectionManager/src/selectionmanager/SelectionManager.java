package selectionmanager;


import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import selectionmanager.selectionmodel.MultipleSelectionModel;
import selectionmanager.selectionmodel.SelectionModel;
import selectionmanager.ui.OverAllLayout;
import selectionmanager.ui.PaintLayer;
import selectionmanager.ui.SelectionManagerComponent;


/**
 * Ein Objekt dieser Klasse stellt ein SelectionManager dar.
 * 
 * @author Manfred Rosskamp
 */
public class SelectionManager {

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
    
    this.setSelectionModel(new MultipleSelectionModel());

    controller = new AuswaehlbaresChangedListenerImpl(getSelectionModel());
  }


  
  /**
   * Fuegt ein auswaehlbares/selektierbares Element diesem Mangager hinzu.
   * 
   * @param a das auswaehlbare Element
   */
  public void addAuswaehlbar(Auswaehlbar a) {
    getSelectionModel().addAuswaehlbar(a);
    a.setAuswaehlbaresChangedListener(controller);
  }

  /**
   * Entfernt das auswaehlbare Element wieder.
   * 
   * @param a das auswaehlbare Element
   */
  public void removeAuswaehlbar(Auswaehlbar a) {
    a.setAuswaehlbaresChangedListener(null);
    getSelectionModel().removeAuswaehlbar(a);
  }


  /**
   * @param model the model to set
   */
  public void setSelectionModel(SelectionModel model) {
    this.model = model;
  }



  /**
   * @return the model
   */
  public SelectionModel getSelectionModel() {
    return model;
  }
  



  /** ********* Methodenaufrufe, die weiter zum Model gereicht werden ******* */

  
  /**
   * Gibt die Liste mit allen Auswaehlbaren Elementen zurueck.
   * 
   * @return Liste mit allen Auswaehlbaren Elementen
   */
  public List<? extends Auswaehlbar> gibAlleAuswaehlbaren() {
    return getSelectionModel().gibAlleAuswaehlbaren();
  }
  
  /**
   * Gibt die aktuell ausgewaehlten Elemente zurueck.
   * 
   * @return Menge der aktuell ausgewaehlten Elemente
   */
  public Set<? extends Auswaehlbar> gibAlleAusgewaehlten() {
    return getSelectionModel().gibAlleAusgewaehlten();
  }
  

  /**
   * Entfernt oder leert die aktuelle Auswahl.
   */
  public void leereAuswahl() {
    getSelectionModel().leereAuswahl();
  }

  /**
   * Waehlt alle auswaehlbaren Elemente aus.
   */
  public void waehleAlleAus() {
    getSelectionModel().waehleAlleAus();
  }

  
  /**
   * Gibt das Element, welches markiert ist, zurueck.
   * 
   * @return Element, welches markiert ist
   */
  public Auswaehlbar getMarkiert() {
    return getSelectionModel().getMarkiert();
  }

  /**
   * Fuegt einen Listener hinzu, der informiert werden moechte, wenn sich die
   * Auswahl geaendert hat.
   * 
   * @param l
   */
  public void addAuswahlListener(AuswahlListener l) {
    getSelectionModel().addAuswahlListener(l);
  }

  /**
   * Entfernt den Listener wieder.
   * 
   * @param l
   */
  public void removeAuswahlListener(AuswahlListener l) {
    getSelectionModel().removeAuswahlListener(l);
  }




}
