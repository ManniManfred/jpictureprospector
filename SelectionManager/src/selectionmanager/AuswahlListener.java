package selectionmanager;

/**
 * 
 * @author Manfred Rosskamp
 */
public interface AuswahlListener {
  
  /**
   * Wird aufgerufen, wenn sich die Auswahl geaendert hat, wenn z.B zur Auswahl
   * neue Elemente hinzugekommen sind.
   */
  void auswahlGeaendert();
  
  /**
   * Wird aufgerufen, wenn sich die Markierung (der graue Rahmen um ein Element)
   * von einem Element zum anderen, z.B. durch druecken der Pfeiltasten, bewegt
   * hat.
   */
  void markierungWurdeBewegt();
}
