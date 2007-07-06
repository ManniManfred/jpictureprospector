package selectionmanager.selectionmodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import selectionmanager.Auswaehlbar;
import selectionmanager.AuswahlListener;

/**
 * @author Manfred Rosskamp
 */
public class MultipleSelectionModel extends AbstractSelectionModel {


  /** Liste aller selektierbaren Elemente. */
  private List<Auswaehlbar> liste;
  
  /** Liste aller selektierten Elemente. */
  private HashSet<Auswaehlbar> ausgewaehlten;

  
  /** Die Nummer des angeklicktem/markiertem Selektierbaren. */
  private int aktuellMarkiertIndex = -1;


  public MultipleSelectionModel() {
    super();
    
    liste = new ArrayList<Auswaehlbar>();
    ausgewaehlten = new HashSet<Auswaehlbar>();
  }
  
  
  
  
  

  /** *** Liste mit den auswaehlbaren Elementen ************ */

  /**
   * Gibt die Liste mit allen Auswaehlbaren Elementen zurueck.
   * 
   * @return Liste mit allen Auswaehlbaren Elementen
   */
  public List<Auswaehlbar> gibAlleAuswaehlbaren() {
    return liste;
  }

  /**
   * Gibt die Anzahl aller auswaehlbaren Elemente zurueck.
   * 
   * @return Anzahl aller auswaehlbaren Elemente
   */
  public int getAnzahlAuswaehlbarer() {
    return liste.size();
  }


  /**
   * Gibt das auswaehlbare Element mit dem Index <code>auswaehlbarIndex</code>
   * zurueck.
   * 
   * @param auswaehlbarIndex Index des auswaehlbaren Elements, welches
   *          zurueckgegeben wird
   * @return Das auswaehlbare Element mit dem Index
   *         <code>auswaehlbarIndex</code>
   */
  public Auswaehlbar getAuswaehlbar(int auswaehlbarIndex) {
    if (auswaehlbarIndex < 0 || auswaehlbarIndex >= liste.size()) {
      return null;
    }
    return liste.get(auswaehlbarIndex);
  }

  /**
   * Fuegt ein Auswaehlbares Element diesem Modell hinzu.
   * 
   * @param a Das auswaehlbare Element, welches hinzugefuegt wird
   */
  public void addAuswaehlbar(Auswaehlbar a) {
    liste.add(a);
  }

  /**
   * Entfernt das uebergebene auwaehlbare Element von dem Model.
   * 
   * @param a Das auswaehlbare Element, welches entfernt wird
   */
  public void removeAuswaehlbar(Auswaehlbar a) {
    liste.remove(a);
    ausgewaehlten.remove(a);
  }




  /** ***************** Aktuelle Auswahl ************************* */

  /**
   * Gibt die aktuell ausgewaehlten Elemente zurueck.
   * 
   * @return Menge der aktuell ausgewaehlten Elemente
   */
  public Set<Auswaehlbar> gibAlleAusgewaehlten() {
    return (Set<Auswaehlbar>) ausgewaehlten.clone();
  }

  /**
   * Fuegt das uebergebene auswaehlbare Element der Menge der ausgewaehlten
   * Elemente hinzu.
   * 
   * @param a Das auswaehlbare Element, welches ausgewaehlt wurde
   */
  public void addAusgewaehlt(Auswaehlbar a) {
    ausgewaehlten.add(a);
  }

  /**
   * Entfernt das uebergebene auswaehlbare Element aus der Menge der
   * ausgewaehlten Elemente.
   * 
   * @param a Das auswaehlbare Element, welches entfernt werden soll
   */
  public void removeAusgewaehlt(Auswaehlbar a) {
    ausgewaehlten.remove(a);
  }

  /**
   * Wenn das Element ausgewaehlt ist, wird es abgewaehlt. Wenn das Element noch
   * nicht ausgewaehlt ist, wird es ausgewaehlt.
   * 
   * @param auswaehlbarIndex Index des auswaehlbaren Elements, welches
   *          ausgewaehlt oder abgewaehlt wird
   */
  public void toggleAusgewaehlt(int auswaehlbarIndex) {
    
    Auswaehlbar zuToggeln = getAuswaehlbar(auswaehlbarIndex);
    zuToggeln.setAusgewaehlt(!zuToggeln.istAusgewaehlt());
    
    if (zuToggeln.istAusgewaehlt()) {
      ausgewaehlten.add(zuToggeln);
    } else {
      ausgewaehlten.remove(zuToggeln);
    }
    
    fireAuswahlGeaendert();
  }


  /**
   * Entfernt oder leert die aktuelle Auswahl.
   */
  public void leereAuswahl() {
    for (Auswaehlbar s : ausgewaehlten) {
      s.setAusgewaehlt(false);
    }
    ausgewaehlten = new HashSet<Auswaehlbar>();
    
    fireAuswahlGeaendert();
  }

  /**
   * Waehlt alle auswaehlbaren Elemente aus.
   */
  public void waehleAlleAus() {
    for (Auswaehlbar s : liste) {
      s.setAusgewaehlt(true);
      ausgewaehlten.add(s);
    }
    fireAuswahlGeaendert();
  }
  




  /** ************* Markierung aendern ******************** */

  /**
   * Gibt den Index des aktuell markierten/angeklickten (ueblicherweise grau
   * gestrichelten umrahmten) Elements zurueck.
   * 
   * @return Index des aktuell markierten Elements
   */
  public int getMarkiertIndex() {
    return aktuellMarkiertIndex;
  }

  /**
   * Setzt das Element mit dem uebergebenem Index als Markiert.
   * 
   * @param auswaehlbarIndex Index des auswaehlbaren Elements, welches markiert
   *          gesetzt wird
   */
  public void setMarkiert(int auswaehlbarIndex) {
    if (auswaehlbarIndex != aktuellMarkiertIndex 
        && auswaehlbarIndex >= 0 && auswaehlbarIndex < liste.size()) {
      
      /* Den vorher Markierten "demarkieren" und den neuen markieren */
      if (aktuellMarkiertIndex >= 0 && aktuellMarkiertIndex < liste.size()) {
        getAuswaehlbar(aktuellMarkiertIndex).setMarkiert(false);
      }
      
      getAuswaehlbar(auswaehlbarIndex).setMarkiert(true);
      
      aktuellMarkiertIndex = auswaehlbarIndex;
      
      fireMarkierungWurdeBewegt();
    }
  }

  /**
   * Setzt das Element <code>a</code> als Markiert.
   * 
   * @param a Das auswaehlbare Element, welches markiert werden soll
   */
  public void setMarkiert(Auswaehlbar a) {
    setMarkiert(liste.indexOf(a));
  }

  /**
   * Gibt das Element, welches markiert ist, zurueck.
   * 
   * @return Element, welches markiert ist
   */
  public Auswaehlbar getMarkiert() {
    return getAuswaehlbar(aktuellMarkiertIndex);
  }

}
