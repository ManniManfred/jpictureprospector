package jpp.core;

import java.net.URL;
import java.util.List;

import jpp.core.exceptions.AendereException;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ImportException;
import jpp.core.exceptions.SucheException;

/**
 * Eine Klasse die diese Schnittstelle implementiert stellt alle
 * Kernfunktionalitaeten der Anwendung zur Verfuegung.
 */
public interface JPPCore {

  /**
   * Importiert eine Datei in den Index der Anwendung, so dass entsprechende
   * Aktionen durchgefuehrt werden koennen.
   * @param f  die Datei die importiert werden soll
   * @return  das BildDokument, das beim Import erzeugt wird
   * @throws ImportException  wird geworfen, wenn das Bild nicht
   *           importiert werden konnte.
   */
  void importiere(URL f) throws ImportException;
  
  /**
   * Durchsucht den Lucene-Index nach einem bestimmten Suchtext. Dieser
   * Suchtext kann sich dabei sowohl auf bestimmte textuelle Merkmale
   * des Bildes beziehen, als auch auf numerische Werte wie z.B.
   * Bildhoehe.
   * @param suchString  der Text nach dem gesucht werden soll
   * @param offset Nummer des Bilddokumentes aller Treffer, ab der die Treffer
   *          in der Trefferliste aufgenommen werden sollen
   * @param maxanzahl Anzahl der Bilddokumente, die maximal in der Trefferliste
   *          aufgenommen werden sollen
   * @return  eine Trefferliste, die eine bestimmte Anzahl an Bilddokumenten
   *          enthaelt
   * @throws SucheException  wird geworfen wenn die Suche nicht erfolgreich
   *           durchgefuehrt werden konnte
   */
  Trefferliste suche(String suchString, int offset, int maxanzahl)  
      throws SucheException;

  /**
   * Entfernt das uebergebene Bild aus dieser Anwendung und, wenn <code>
   * auchVonFestplatte</code>
   * gesetzt ist, dann auch von der Festplatte.
   * 
   * @param datei Url des Bildes, welches entfernt werden soll
   * @param auchVonFestplatte gibt an, ob das Bild auch von der Festplatte
   *          entfernt werden soll
   * @throws EntferneException wird geworfen, wenn das Entfernen des
   *           BildDokuments fehlgeschlagen ist
   */
  public void entferne(URL datei, boolean auchVonFestplatte)
    throws EntferneException;
  
  
  /**
   * Aendert ein <code>BildDokument</code> innerhalb des Lucene-Index
   * mit den entsprechenden Merkmalen, die zum <code>BildDokument</code>
   * getaetigt wurden.
   * @param dokument  das geanderte <code>BildDokument</code>
   * @throws AendereException  wird geworfen, wenn das <code>BildDokument</code>
   *           nicht geandert werden konnte.
   */
  void aendere(BildDokument dokument) throws AendereException;
  
  
  /**
   * Gibt eine Liste aller im Index vorhandenen Alben zurück, die zu einer 
   * bestimmten Gruppe gehört oder zu wenn die übergebene Gruppe null ist zu
   * allen Gruppen gehört.
   * @param gruppe Gruppe zu der alle Alben zurückgegeben werden. Wenn dieser
   *  Wert <code>null</code> ist, werden alle Alben zurückgegeben.
   * @return eine Liste aller Alben
   */
  List<String> getAlben(String gruppe);
  
  /**
   * Loescht alle Dokumente, die nicht mehr auf den Festplatte vorhanden sind
   * aus dem Index.
   * @return Meldungen, welche Dokumente aus dem Index entfernt wurden
   */
  String clearUpIndex() throws SucheException;
}
