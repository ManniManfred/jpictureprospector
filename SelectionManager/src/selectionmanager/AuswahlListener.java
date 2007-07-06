package selectionmanager;

import java.util.Set;

/**
 * 
 * @author Manfred Rosskamp
 */
public interface AuswahlListener {
  
  /**
   * Wird aufgerufen, wenn sich die Auswahl geaendert hat, wenn z.B zur Auswahl
   * neue Elemente hinzugekommen sind.
   * 
   * @param ausgewaehlten Menge der ausgewaehlten Elemente
   */
  void auswahlGeaendert(Set<Auswaehlbar> ausgewaehlten);
  
  /**
   * Wird aufgerufen, wenn sich die Markierung (der graue Rahmen um ein Element)
   * von einem Element zum anderen, z.B. durch druecken der Pfeiltasten, bewegt
   * hat.
   * 
   * @param neueMarkierung Das markierte auswaehlbare Element
   */
  void markierungWurdeBewegt(Auswaehlbar neueMarkierung);
}
