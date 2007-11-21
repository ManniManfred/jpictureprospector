package jpp.core;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jpp.core.exceptions.AendereException;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ErzeugeException;
import jpp.core.exceptions.ImportException;
import jpp.core.exceptions.SucheException;
import jpp.merkmale.DateipfadMerkmal;
import jpp.merkmale.Merkmal;


/**
 * Ein Objekt dieser Klasse stellt die Hauptaufgaben dieser JPictureProspector
 * Anwendung bereit. Diese Klasse ist ein Singelton.
 * 
 * @author Manfred Rosskamp
 */
public abstract class AbstractJPPCore {

  /** Enthaelt eine Liste mit den Klassen aller zu verwendenen Merkmale. */
  private static List<Class> merkmalsklassen;

  /** Enthaelt eine Liste mit den Namen aller zu verwendenen Merkmale. */
  private static List<String> merkmalsNamen;



  /**
   * Erstellt ein neues JPPCore-Objekt.
   * 
   * @param indexDir Enthaelt den Pfad zu dem Index-Verzeichnis von Lucene
   * @throws ErzeugeException wird geworfen, falls beim Erzeugen dieses Objektes
   *           ein Fehler auftritt. Z.B wenn die Merkmalsdatei nicht existiert,
   *           oder die Klassen zu den Merkmals-Klassennamen nicht gefunden
   *           wurden.
   */
  public AbstractJPPCore() throws ErzeugeException {
  }

  /**
   * Gibt eine Liste mit allen Merkmalsklassen zurueck, die in der Merkmalsdatei
   * angegeben sind.
   * 
   * @return Liste mit allen Merkmalsklassen
   * @throws ClassNotFoundException wenn ein Klasse, die in der Merkmalsdatei
   *           angegeben ist nicht gefunden wurde.
   * @throws IOException wenn die Merkamlsdatei nicht gelesen werden konnte
   */
  public static List<Class> getMerkmalsKlassen() throws ClassNotFoundException {
    if (merkmalsklassen == null) {
      merkmalsklassen = new ArrayList<Class>();



      String merkmalsKlassenname;

      /* Alle Merkmale aus der Merkmalsliste durchgehen */
      for (int i = 0; i < Einstellungen.MERKMALE.length; i++) {
        merkmalsKlassenname = Einstellungen.MERKMALE[i];
        merkmalsklassen.add(Class.forName(merkmalsKlassenname));
      }
    }
    return merkmalsklassen;
  }

  /**
   * Gibt ein Array mit allen Namen der möglichen Merkmale zurueck.
   * 
   * @return Array mit allen Namen der möglichen Merkmale
   * @throws Exception wenn die Klassennamen nicht erzeugt werden konnten
   */
  public static String[] getMerkmalsnamen() throws Exception {
    if (merkmalsNamen == null) {
      merkmalsNamen = new ArrayList<String>();

      for (Class klasse : getMerkmalsKlassen()) {
        Merkmal m = (Merkmal) klasse.newInstance();

        /* Den Namen aus dem Object holen und in der Liste einfuegen */
        merkmalsNamen.add(m.getName());
      }
    }

    return merkmalsNamen.toArray(new String[0]);
  }


  /**
   * Importiert eine Bilddatei in diese Anwendung.
   * 
   * @param datei Bilddatei, die importiert werden soll
   * @return die importierte Bilddatei als BildDokument
   * @throws ImportException wird geworfen, wenn das Importieren der Bilddatei
   *           nicht funktioniert, z.B. wenn das Bild schon importiert wurde.
   */
  public abstract void importiere(URL datei) throws ImportException;


  /**
   * Suche in allen importierten Bilder nach dem Suchtext und gibt eine
   * entsprechende Trefferliste mit den Suchergebnissen zurueck. Fuehrt
   * suche(String suchtext, 0, 20) aus.
   * 
   * @param suchtext Suchtext, nach dem gesucht wird
   * @return Trefferliste mit den Suchergebnissen
   * @throws SucheException wird geworfen, wenn die Suche nicht erfolgreich mit
   *           einer erzeugten Trefferliste beendet werden kann
   */
  public Trefferliste suche(String suchtext) throws SucheException {
    return suche(suchtext, 0, 20);
  }

  /**
   * Suche in allen importierten Bilder nach dem Suchtext und gibt eine
   * entsprechende Trefferliste mit den Suchergebnissen zurueck.
   * 
   * @param suchtext Suchtext, nach dem gesucht wird
   * @param offset Nummer des Bilddokumentes aller Treffer, ab der die Treffer
   *          in der Trefferliste aufgenommen werden sollen
   * @param maxanzahl Anzahl der Bilddokumente, die maximal in der Trefferliste
   *          aufgenommen werden sollen
   * @return Trefferliste mit den Suchergebnissen
   * @throws SucheException wird geworfen, wenn die Suche nicht erfolgreich mit
   *           einer erzeugten Trefferliste beendet werden kann
   */
  public abstract Trefferliste suche(String suchtext, int offset, int maxanzahl)
      throws SucheException;

  /**
   * Uebernimmt die Aenderungen, die an dem BildDokument gemacht wurden, wie
   * z.B. das Hinzufuegen von Schluesselwoerter.
   * 
   * @param bild Bilddokument, von dem die Aenderungen uebernommen werden
   * @throws AendereException wird geworfen, wenn das Aendern nicht funktioniert
   *           hat
   */
  public abstract void aendere(BildDokument bild) throws AendereException;

  /**
   * Entfernt ein <code>BildDokument</code> aus dem Index.
   * 
   * @param bild das <code>BildDokument</code> das entfernt werden soll
   * @throws EntferneException wird geworfen, wenn das Bild nicht geloescht
   *           werden konnte
   */
  public void entferne(BildDokument bild) throws EntferneException {
    entferne(bild, false);
  }

  /**
   * Entfernt das uebergebene Bild aus dieser Anwendung und, wenn <code>
   * auchVonFestplatte</code>
   * gesetzt ist, dann auch von der Festplatte.
   * 
   * @param bild BildDokument, welches entfernt werden soll
   * @param auchVonFestplatte gibt an, ob das Bild auch von der Festplatte
   *          entfernt werden soll
   * @throws EntferneException wird geworfen, wenn das Entfernen des
   *           BildDokuments fehlgeschlagen ist
   */
  public void entferne(BildDokument bild, boolean auchVonFestplatte)
      throws EntferneException {
    /*
     * Da die Pfadangabe eindeutig ist, entferne das Document mit dem Pfad, der
     * aus dem BildDokument gelesen wurde.
     */
    entferne((URL) bild.getMerkmal(DateipfadMerkmal.FELDNAME).getWert(),
        auchVonFestplatte);
  }

  /**
   * Entfernt die entsprechende Datei aus dem Index
   * 
   * @param datei Datei, welches entfernt werden soll
   * @param auchVonFestplatte gibt an, ob das Bild auch von der Festplatte
   *          entfernt werden soll
   * @throws EntferneException wird geworfen, wenn das Entfernen des
   *           BildDokuments fehlgeschlagen ist
   */
  public abstract void entferne(URL datei, boolean auchVonFestplatte)
      throws EntferneException;

  /**
   * Loescht alle Dokumente, die nicht mehr auf den Festplatte vorhanden sind
   * aus dem Index.
   * 
   * @return Meldungen, welche Dokumente aus dem Index entfernt wurden
   */
  public abstract String clearUpIndex() throws SucheException;

}
