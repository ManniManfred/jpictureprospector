package jpp.ui.listener;

/**
 * Gibt die Vorgaben fuer einen Listener, der den Ladevorgang eines
 * Vorschaubildes ueberwacht.
 */
public interface VorschauBildListener {

  /**
   * Wird aufgerufen, wenn das Vorschaubild fertig geladen wurde.
   */
  void bildGeladen();
}
