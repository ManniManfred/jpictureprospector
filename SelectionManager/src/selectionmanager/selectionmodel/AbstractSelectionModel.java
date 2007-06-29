package selectionmanager.selectionmodel;

import java.util.ArrayList;
import java.util.List;

import selectionmanager.AuswahlListener;

public abstract class AbstractSelectionModel implements SelectionModel {
  
  /** 
   * Enthaelt die Listener, die ueber eine Aenderung der Auswahl informiert
   * werden moechten.
   */
  private List<AuswahlListener> listener;
  
  public AbstractSelectionModel() {
    listener = new ArrayList<AuswahlListener>();
  }
  
  
  
  
  /** ************** AuswahlListener *********************** */


  /**
   * Loest das Event aus, dass sich die Auswahl geaendert hat.
   */
  public void fireAuswahlGeaendert() {
    for (AuswahlListener l : listener) {
      l.auswahlGeaendert();
    }
  }
  
  /**
   * Loest das Event aus, dass sich die Markierung bewegt hat.
   */
  protected void fireMarkierungWurdeBewegt() {
    for (AuswahlListener l : listener) {
      l.markierungWurdeBewegt();
    }
  }
  
  /**
   * Fuegt einen Listener hinzu, der informiert werden moechte, wenn sich die
   * Auswahl geaendert hat.
   * 
   * @param l
   */
  public void addAuswahlListener(AuswahlListener l) {
    listener.add(l);
  }

  /**
   * Entfernt den Listener wieder.
   * 
   * @param l
   */
  public void removeAuswahlListener(AuswahlListener l) {
    listener.remove(l);
  }


}
