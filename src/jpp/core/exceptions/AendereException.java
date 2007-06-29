package jpp.core.exceptions;

/**
 * Diese Exception wird geworfen, wenn beim Aendern bzw. Updaten ein
 * Fehler auftritt.
 * @author Manfred Rosskamp
 */
public class AendereException extends Exception {
  
  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 7290526639092575430L;

  
  
  /**
   * Erzeugt eine neue AendereException.
   */
  public AendereException() {
  }

  /**
   * Erzeugt eine neue AendereException mit einer Meldung ueber den Grund.
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public AendereException(String message) {
    super(message);
  }

  /**
   * Erzeugt eine neue AendereException mit einer Exception, die diese
   * AendereException ausgeloest hat.
   * @param cause  Die Ausnahme, die diese AendereException ausgeloesst hat
   */
  public AendereException(Throwable cause) {
    super(cause);
  }

  /**
   * Erzeugt eine neue AendereException.
   * @param cause  Die Ausnahme, die diese AendereException ausgeloesst hat
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public AendereException(String message, Throwable cause) {
    super(message, cause);
  }

}
