package jpp.core.exceptions;

/**
 * Ein Objekt dieser Klasse stellte eine ImportExcpetion dar, die bei einem
 * Fehler während eines Importvorgangs auftritt.
 * 
 * @author Manfred Rosskamp
 */
public class PostException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 3315513817317413979L;

  
  /**
   * Erzeugt eine neue ImportException.
   */
  public PostException() {
    super();
  }

  /**
   * Erzeugt eine neue ImportException.
   * 
   * @param nachricht Meldung, warum der Importiervorgang fehlgeschlagen ist
   */
  public PostException(String nachricht) {
    super(nachricht);
  }

  /**
   * Erzeugt eine neue ImportException.
   * 
   * @param nachricht Meldung, warum der Importiervorgang fehlgeschlagen ist
   * @param grund Exception, durch die diese ImportException ausgelöst wurde
   */
  public PostException(String nachricht, Throwable grund) {
    super(nachricht, grund);
  }
}
