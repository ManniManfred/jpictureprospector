package selectionmanager.selectionmodel;

import java.util.List;
import java.util.Set;

import selectionmanager.Auswaehlbar;
import selectionmanager.AuswahlListener;


/**
 * Ein SelectionModel enthaelt alle Informationen ueber eine Auswahl. Dies sind
 * unter anderem alle Auswaehlbaren Elemente, die aktuelle Auswahl und das
 * gerade markierte Element.
 * 
 * @author Manfred Rosskamp
 */
public interface SelectionModel {


  /** *** Liste mit den auswaehlbaren Elementen ************ */

  /**
   * Gibt die Liste mit allen Auswaehlbaren Elementen zurueck.
   * 
   * @return Liste mit allen Auswaehlbaren Elementen
   */
  public List<Auswaehlbar> gibAlleAuswaehlbaren();

  /**
   * Gibt die Anzahl aller auswaehlbaren Elemente zurueck.
   * 
   * @return Anzahl aller auswaehlbaren Elemente
   */
  public int getAnzahlAuswaehlbarer();


  /**
   * Gibt das auswaehlbare Element mit dem Index <code>auswaehlbarIndex</code>
   * zurueck.
   * 
   * @param auswaehlbarIndex Index des auswaehlbaren Elements, welches
   *          zurueckgegeben wird
   * @return Das auswaehlbare Element mit dem Index
   *         <code>auswaehlbarIndex</code>
   */
  public Auswaehlbar getAuswaehlbar(int auswaehlbarIndex);

  /**
   * Fuegt ein Auswaehlbares Element diesem Modell hinzu.
   * 
   * @param a Das auswaehlbare Element, welches hinzugefuegt wird
   */
  public void addAuswaehlbar(Auswaehlbar a);

  /**
   * Entfernt das uebergebene auwaehlbare Element von dem Model.
   * 
   * @param a Das auswaehlbare Element, welches entfernt wird
   */
  public void removeAuswaehlbar(Auswaehlbar a);




  /** ***************** Aktuelle Auswahl ************************* */

  /**
   * Gibt die aktuell ausgewaehlten Elemente zurueck.
   * 
   * @return Menge der aktuell ausgewaehlten Elemente
   */
  public Set<Auswaehlbar> gibAlleAusgewaehlten();

  /**
   * Fuegt das uebergebene auswaehlbare Element der Menge der ausgewaehlten
   * Elemente hinzu.
   * 
   * @param a Das auswaehlbare Element, welches ausgewaehlt wurde
   */
  public void addAusgewaehlt(Auswaehlbar a);

  /**
   * Entfernt das uebergebene auswaehlbare Element aus der Menge der
   * ausgewaehlten Elemente.
   * 
   * @param a Das auswaehlbare Element, welches entfernt werden soll
   */
  public void removeAusgewaehlt(Auswaehlbar a);

  /**
   * Wenn das Element ausgewaehlt ist, wird es abgewaehlt. Wenn das Element noch
   * nicht ausgewaehlt ist, wird es ausgewaehlt.
   * 
   * @param auswaehlbarIndex Index des auswaehlbaren Elements, welches
   *          ausgewaehlt oder abgewaehlt wird
   */
  public void toggleAusgewaehlt(int auswaehlbarIndex);


  /**
   * Entfernt oder leert die aktuelle Auswahl.
   */
  public void leereAuswahl();

  /**
   * Waehlt alle auswaehlbaren Elemente aus.
   */
  public void waehleAlleAus();


  /**
   * Loest das Event aus, dass sich die Auswahl geaendert hat.
   */
  public void fireAuswahlGeaendert();

  /** ************* Markierung aendern ******************** */

  /**
   * Gibt den Index des aktuell markierten/angeklickten (ueblicherweise grau
   * gestrichelten umrahmten) Elements zurueck.
   * 
   * @return Index des aktuell markierten Elements
   */
  public int getMarkiertIndex();

  /**
   * Setzt das Element mit dem uebergebenem Index als Markiert.
   * 
   * @param auswaehlbarIndex Index des auswaehlbaren Elements, welches markiert
   *          gesetzt wird
   */
  public void setMarkiert(int auswaehlbarIndex);

  /**
   * Setzt das Element <code>a</code> als Markiert.
   * 
   * @param a Das auswaehlbare Element, welches markiert werden soll
   */
  public void setMarkiert(Auswaehlbar a);

  /**
   * Gibt das Element, welches markiert ist, zurueck.
   * 
   * @return Element, welches markiert ist
   */
  public Auswaehlbar getMarkiert();



  /** ************** AuswahlListener *********************** */


  /**
   * Fuegt einen Listener hinzu, der informiert werden moechte, wenn sich die
   * Auswahl geaendert hat.
   * 
   * @param l
   */
  public void addAuswahlListener(AuswahlListener l);

  /**
   * Entfernt den Listener wieder.
   * 
   * @param l
   */
  public void removeAuswahlListener(AuswahlListener l);


}
