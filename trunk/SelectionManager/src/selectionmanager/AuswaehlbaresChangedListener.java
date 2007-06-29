package selectionmanager;

/**
 * Wird von einem Auswaehlbaren Element aufgerufen und informiert damit
 * ueber eine Aenderung des eigenen Zustands. Z.B. wenn auf das Auswaehlbare
 * Element geklickt wird, wird eine wurdeAngeklickt ausgeloest.
 * 
 * @author Manfred Rosskamp
 */
public interface AuswaehlbaresChangedListener {
  
  /**
   * Wird vom Auswaehlbarem aufgerufen, wenn dieses ausgewaehlt 
   * (ueblich mit blauem Hintergrund) wurde.
   * 
   * @param a das ausgewaehlte Element
   */
  void wurdeAusgewaehlt(Auswaehlbar a);
  
  /**
   * Wird vom Auswaehlbarem aufgerufen, wenn dieses angeklickt bzw. markiert 
   * (ueblich mit einem grau gestrichelten Rahmen) wurde.
   * 
   * @param a das angeklickte auswaehlbare Element
   */
  void wurdeMarkiert(Auswaehlbar a);
}
