package jpp.core.exceptions;

/**
 * Diese Ausnahme tritt auf, wenn das Generieren von irgendetwas (in dem Fall
 * dieser JPP-Anwendung das Generieren von Thumbnail) fehlschlaegt. 
 * @author Manfred Rosskamp
 */
public class GeneriereException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 4841354871430149506L;

  
  
  /**
   * Erzeugt eine neue GeneriereException.
   */
  public GeneriereException() {
  }

  
  /**
   * Erzeugt eine neue GeneriereException mit einer Meldung ueber den Grund.
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public GeneriereException(String message) {
    super(message);
  }

  /**
   * Erzeugt eine neue GeneriereException mit einer Exception, die diese
   * AendereException ausgeloest hat.
   * @param cause  Die Ausnahme, die diese GeneriereException ausgeloesst hat
   */
  public GeneriereException(Throwable cause) {
    super(cause);
  }

  /**
   * Erzeugt eine neue GeneriereException.
   * @param cause  Die Ausnahme, die diese GeneriereException ausgeloesst hat
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public GeneriereException(String message, Throwable cause) {
    super(message, cause);
  }

}
