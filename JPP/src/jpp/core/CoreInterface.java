package jpp.core;

import java.io.File;

import jpp.core.exceptions.AendereException;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ImportException;
import jpp.core.exceptions.SucheException;

/**
 * Eine Klasse die diese Schnittstelle implementiert stellt alle
 * Kernfunktionalitaeten der Anwendung zur Verfuegung.
 */
public interface CoreInterface {

  /**
   * Importiert eine Datei in den Index der Anwendung, so dass entsprechende
   * Aktionen durchgefuehrt werden koennen.
   * @param f  die Datei die importiert werden soll
   * @return  das BildDokument, das beim Import erzeugt wird
   * @throws ImportException  wird geworfen, wenn das Bild nicht
   *           importiert werden konnte.
   */
  BildDokument importiere(File f) throws ImportException;
  
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
   * Entfernt ein <code>BildDokument</code> aus dem Lucene-Index, so
   * dass entsprechende Aktionen nicht mehr durchgefuehrt werden koennen.
   * @param dokument  das zu entfernende <code>BildDokument</code>
   * @throws EntferneException  wird geworfen, wenn das Bild nicht
   *           aus dem Index entfernt werden konnte
   */
  void entferne(BildDokument dokument) throws EntferneException;
  
  /**
   * Aendert ein <code>BildDokument</code> innerhalb des Lucene-Index
   * mit den entsprechenden Merkmalen, die zum <code>BildDokument</code>
   * getaetigt wurden.
   * @param dokument  das geanderte <code>BildDokument</code>
   * @throws AendereException  wird geworfen, wenn das <code>BildDokument</code>
   *           nicht geandert werden konnte.
   */
  void aendere(BildDokument dokument) throws AendereException;
}
