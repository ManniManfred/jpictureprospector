package core;

/**
 * Ein Objekt dieser Klasse stellte eine ImportExcpetion dar, die bei einem
 * Fehler während eines Importvorgangs auftritt.
 * @author Manfred Rosskamp
 */
public class ImportException extends Exception {
  
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
