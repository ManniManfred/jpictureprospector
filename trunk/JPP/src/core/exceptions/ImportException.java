package core.exceptions;

/**
 * Ein Objekt dieser Klasse stellte eine ImportExcpetion dar, die bei einem
 * Fehler während eines Importvorgangs auftritt.
 * @author Manfred Rosskamp
 */
public class ImportException extends Exception {
  
  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 2725408003019234287L;

  
  
  
  /**
   * Erzeugt eine neue ImportException.
   */
  public ImportException() {
    super();
  }
  
  /**
   * Erzeugt eine neue ImportException.
   * @param nachricht  Meldung, warum der Importiervorgang fehlgeschlagen ist
   */
  public ImportException(String nachricht) {
    super(nachricht);
  }
  
  /**
   * Erzeugt eine neue ImportException.
   * @param nachricht  Meldung, warum der Importiervorgang fehlgeschlagen ist
   * @param grund  Exception, durch die diese ImportException ausgelöst wurde
   */
  public ImportException(String nachricht, Throwable grund) {
    super(nachricht, grund);
  }
}
