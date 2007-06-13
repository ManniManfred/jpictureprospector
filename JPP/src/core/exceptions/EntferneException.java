package core.exceptions;

/**
 * Diese Ausnahme tritt auf, wenn ein BildDokument nicht korrekt entfernt
 * werden konnte.
 * @author Manfred Rosskamp
 */
public class EntferneException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 643099039820095440L;

  
  
  /**
   * Erzeugt eine neue EntferneException.
   */
  public EntferneException() {
  }
  
  
  /**
   * Erzeugt eine neue EntferneException mit einer Meldung ueber den Grund.
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public EntferneException(String message) {
    super(message);
  }
  
  /**
   * Erzeugt eine neue EntferneException mit einer Exception, die diese
   * EntferneException ausgeloest hat.
   * @param cause  Die Ausnahme, die diese EntferneException ausgeloesst hat
   */
  public EntferneException(Throwable cause) {
    super(cause);
  }

  /**
   * Erzeugt eine neue EntferneException.
   * @param cause  Die Ausnahme, die diese EntferneException ausgeloesst hat
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public EntferneException(String message, Throwable cause) {
    super(message, cause);
  }

}
