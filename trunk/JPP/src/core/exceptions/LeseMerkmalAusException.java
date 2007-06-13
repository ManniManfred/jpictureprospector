package core.exceptions;


/**
 * Eine LeseMerkmalAusException wird geworfen, wenn das Auslesen eines Merkmals
 * aus Lucene oder einem Bild fehlschlaegt. 
 * @author Manfred Rosskamp
 */
public class LeseMerkmalAusException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = -8307621349750778951L;

  
  
  /**
   * Erzeugt eine neue LeseMerkmalAusException.
   */
  public LeseMerkmalAusException() {
  }

  
  /**
   * Erzeugt eine neue LeseMerkmalAusException mit einer Meldung ueber den 
   * Grund.
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public LeseMerkmalAusException(String message) {
    super(message);
  }

  /**
   * Erzeugt eine neue LeseMerkmalAusException mit einer Exception, die diese
   * AendereException ausgeloest hat.
   * @param cause  Die Ausnahme, die diese LeseMerkmalAusException ausgeloesst 
   *      hat
   */
  public LeseMerkmalAusException(Throwable cause) {
    super(cause);
  }

  /**
   * Erzeugt eine neue LeseMerkmalAusException.
   * @param cause  Die Ausnahme, die diese LeseMerkmalAusException ausgeloesst 
   *      hat
   * @param message  Textuelle Meldung ueber Grund des Auftrettens dieser
   *      Exception
   */
  public LeseMerkmalAusException(String message, Throwable cause) {
    super(message, cause);
  }

}
