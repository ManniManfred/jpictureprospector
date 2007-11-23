package benutzermanager.exceptions;

import benutzermanager.Recht;


/**
 * Diese Exception wird geworfen, wenn ein Recht nicht im Rechtemanager
 * exisitiert.
 * 
 * @author Manfred Rosskamp
 */
public class RechtExistiertNichtException extends RuntimeException {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 6015503182533840160L;

  
  /**
   * Erzeugt eine neue RechtExistiertNichtException.
   */
  public RechtExistiertNichtException() {
  }

  /**
   * Erzeugt eine neue RechtExistiertNichtException mit dem Recht, welches nicht
   * definiert ist.
   * 
   * @param message Textuelle Meldung ueber Grund des Auftrettens dieser
   *          Exception
   */
  public RechtExistiertNichtException(Recht r) {
    super("Das Recht " + r + " ist fuer diese Anwendung nicht"
        + " definiert.");
  }
  
  /**
   * Erzeugt eine neue RechtExistiertNichtException mit einer Meldung ueber den
   * Grund.
   * 
   * @param message Textuelle Meldung ueber Grund des Auftrettens dieser
   *          Exception
   */
  public RechtExistiertNichtException(String message) {
    super(message);
  }

  /**
   * Erzeugt eine neue RechtExistiertNichtException mit einer Exception, die
   * diese AendereException ausgeloest hat.
   * 
   * @param cause Die Ausnahme, die diese AendereException ausgeloesst hat
   */
  public RechtExistiertNichtException(Throwable cause) {
    super(cause);
  }

  /**
   * Erzeugt eine neue RechtExistiertNichtExceptionF.
   * 
   * @param cause Die Ausnahme, die diese AendereException ausgeloesst hat
   * @param message Textuelle Meldung ueber Grund des Auftrettens dieser
   *          Exception
   */
  public RechtExistiertNichtException(String message, Throwable cause) {
    super(message, cause);
  }

}
