package jpp.core.exceptions;

/**
 * Diese Exception wird geworfen, wenn aus einer Datei nicht das entsprechende
 * BildDokument erzeugt werden konnte.
 * @author Manfred Rosskamp
 */
public class ErzeugeBildDokumentException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = -8940207717706133735L;

  
  /**
   * Erzeugt eine neue ErzeugeBildDokumentException.
   */
  public ErzeugeBildDokumentException() {
  }

  
  /**
   * Erzeugt eine neue ErzeugeBildDokumentException mit einer Meldung ueber 
   * den Grund.
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public ErzeugeBildDokumentException(String message) {
    super(message);
  }

  /**
   * Erzeugt eine neue ErzeugeBildDokumentException mit einer Exception, die 
   * diese ErzeugeBildDokumentException ausgeloest hat.
   * @param cause  Die Ausnahme, die diese ErzeugeBildDokumentException 
   *     ausgeloesst hat
   */
  public ErzeugeBildDokumentException(Throwable cause) {
    super(cause);
  }

  /**
   * Erzeugt eine neue ErzeugeBildDokumentException.
   * @param cause  Die Ausnahme, die diese ErzeugeBildDokumentException 
   *      ausgeloesst hat
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public ErzeugeBildDokumentException(String message, Throwable cause) {
    super(message, cause);
  }

}
