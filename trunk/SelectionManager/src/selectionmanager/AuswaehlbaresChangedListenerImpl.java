package selectionmanager;

import selectionmanager.selectionmodel.SelectionModel;

/**
 * Ein Objekt dieser Klasse wird dazu verwendet, um die Events, die von 
 * einzelnen auswaehlbaren Objekten ausgeloesst werden, umzusetzten in 
 * Aenderungen am SelectionModel. Man koennte diese Klasse auch als den
 * Controller eines MVC Patterns bezeichnen.
 * 
 * @author Manfred Rosskamp
 */
public class AuswaehlbaresChangedListenerImpl implements
    AuswaehlbaresChangedListener {
  
  /**
   * Model in dem gespeichert ist, welche Elemente ausgewählt oder markiert
   * sind. An diesem Model werden die Aenderungen durchgefuehrt.
   */
  private SelectionModel model;
  
  /**
   * Erzeugt ein neues AuswaehlbaresChangedListenerImpl.
   * 
   * @param m
   */
  public AuswaehlbaresChangedListenerImpl(SelectionModel model) {
    this.model = model;
  }
  
  /* (non-Javadoc)
   * @see selectionmanager.AuswaehlbaresChangedListener#wurdeAusgewaehlt(selectionmanager.Auswaehlbar)
   */
  public void wurdeAusgewaehlt(Auswaehlbar a) {
    /* Vorherige Auswahl aufheben */
    if (a.resetAuswahl()) {
      for (Auswaehlbar iter : model.gibAlleAusgewaehlten()) {
        if (a != iter) {
          model.removeAusgewaehlt(iter);
          iter.setAusgewaehlt(false);
        }
      }
    }
    
    /* Das Element a an oder abwaehlen */
    if (a.istAusgewaehlt()) {
      model.addAusgewaehlt(a);
    } else {
      model.removeAusgewaehlt(a);
    }
    model.fireAuswahlGeaendert();
  }

  /* (non-Javadoc)
   * @see selectionmanager.AuswaehlbaresChangedListener#wurdeMarkiert(selectionmanager.Auswaehlbar)
   */
  public void wurdeMarkiert(Auswaehlbar a) {
    model.setMarkiert(a);
  }

}
