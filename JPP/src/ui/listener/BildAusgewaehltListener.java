package ui.listener;

import ui.ThumbnailAnzeigePanel;

/**
 * Gibt die Vorgaben fuer Listener bei denen ein Bild ausgewaehlt wurde.
 */
public interface BildAusgewaehltListener {

  /**
   * Wird aufgerufen, wenn sich das zuletzt ausgewaehlte Bild aendern soll.
   * 
   * @param tap  das neue ausgewaehlte Bild
   */
  void setzeZuletztAusgewaehltesBild(ThumbnailAnzeigePanel tap, int index);
}
