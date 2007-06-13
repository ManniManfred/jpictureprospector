package core.exceptions;

/**
 * Diese ErzeugeException wird geworfen, wenn beim Erzeugen ein Fehler auftritt.
 * @author Manfred Rosskamp
 */
public class ErzeugeException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 8308415860660352717L;

  
  
  /**
   * Erzeugt eine neue ErzeugeException.
   */
  public ErzeugeException() {
  }

  
  /**
   * Erzeugt eine neue ErzeugeException mit einer Meldung ueber den Grund.
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public ErzeugeException(String message) {
    super(message);
  }

  /**
   * Erzeugt eine neue ErzeugeException mit einer Exception, die diese
   * AendereException ausgeloest hat.
   * @param cause  Die Ausnahme, die diese ErzeugeException ausgeloesst hat
   */
  public ErzeugeException(Throwable cause) {
    super(cause);
  }

  /**
   * Erzeugt eine neue ErzeugeException.
   * @param cause  Die Ausnahme, die diese ErzeugeException ausgeloesst hat
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public ErzeugeException(String message, Throwable cause) {
    super(message, cause);
  }

}
