package core.exceptions;

/**
 * Diese SucheException tritt auf, falls ein Fehler bei der Suche auftritt.
 * 
 * @author Manfred Rosskamp
 */
public class SucheException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 7529939329131045986L;


  /**
   * Erzeugt eine neue SucheException.
   */
  public SucheException() {
  }


  /**
   * Erzeugt eine neue AendereException mit einer Meldung ueber den Grund.
   * 
   * @param message Textuelle Meldung ueber Grund des Auftrettens dieser
   *          Exception
   */
  public SucheException(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  /**
   * Erzeugt eine neue AendereException mit einer Exception, die diese
   * AendereException ausgeloest hat.
   * 
   * @param cause Die Ausnahme, die diese AendereException ausgeloesst hat
   */
  public SucheException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Erzeugt eine neue AendereException.
   * 
   * @param cause Die Ausnahme, die diese AendereException ausgeloesst hat
   * @param message Textuelle Meldung ueber Grund des Auftrettens dieser
   *          Exception
   */
  public SucheException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

}
