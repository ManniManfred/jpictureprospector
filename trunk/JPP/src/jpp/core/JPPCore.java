package jpp.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jpp.core.exceptions.AendereException;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ErzeugeBildDokumentException;
import jpp.core.exceptions.ErzeugeException;
import jpp.core.exceptions.ImportException;
import jpp.core.exceptions.SucheException;
import jpp.merkmale.DateipfadMerkmal;
import jpp.merkmale.Merkmal;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;


/**
 * Ein Objekt dieser Klasse stellt die Hauptaufgaben dieser JPictureProspector
 * Anwendung bereit. Diese Klasse ist ein Singelton.
 * 
 * @author Manfred Rosskamp
 */
public class JPPCore implements CoreInterface {

  /** Enthaelt eine Liste mit den Klassen aller zu verwendenen Merkmale. */
  private static List<Class> merkmalsklassen;

  /** Enthaelt eine Liste mit den Namen aller zu verwendenen Merkmale. */
  private static List<String> merkmalsNamen;

  /** Enthaelt den Pfad zu dem Index-Verzeichnis von Lucene. */
  private static final String INDEX_DIR = "imageIndex";

  /** Enthaelt den Parser, der alle Such-Anfragen in eine Query parst. */
  private MultiFieldQueryParser parser;

  /** Klassenvariable fuer das Singleton Object. */
  private static JPPCore core = null;

  /**
   * Gibt das Singleton Objekt zurueck.
   * @return das Singleton Objekt
   * @throws ErzeugeException
   */
  public synchronized static JPPCore getInstance() throws ErzeugeException {
    if (core == null) {
      core = new JPPCore();
    }
    return core;
  }

  
  /**
   * Erstellt ein neues JPPCore-Objekt.
   * 
   * @throws ErzeugeException wird geworfen, falls beim Erzeugen dieses Objektes
   *           ein Fehler auftritt. Z.B wenn die Merkmalsdatei nicht existiert,
   *           oder die Klassen zu den Merkmals-Klassennamen nicht gefunden
   *           wurden.
   */
  private JPPCore() throws ErzeugeException {

    /* Namen aller moeglichen Merkmale herausfinden */
    String[] namen = null;
    try {
      namen = getMerkmalsnamen();
    } catch (Exception e) {
      throw new ErzeugeException("Konnte die Merkmale nicht erzeugen.", e);
    }

    /*
     * MultiFieldQueryParser erzeugen, der über alle Felder sucht und den
     * GermanAnalyzer verwendet.
     */
    parser = new MultiFieldQueryParser(namen, new GermanAnalyzer());
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
  public static List<Class> getMerkmalsKlassen() throws IOException,
      ClassNotFoundException {
    if (merkmalsklassen == null) {
      merkmalsklassen = new ArrayList<Class>();

      /* Datei mit der Merkmalsliste oeffnen. */
      BufferedReader reader = new BufferedReader(new FileReader(
          Einstellungen.MERKMAL_DATEI));

      String merkmalsKlassenname;

      /* Alle Merkmale aus der Merkmalsliste durchgehen */
      while ((merkmalsKlassenname = reader.readLine()) != null) {
        merkmalsklassen.add(Class.forName(merkmalsKlassenname));
      }

      reader.close();
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
  public BildDokument importiere(File datei) throws ImportException {

    /*
     * ueberpruefen, ob das Bild bereits im Index vorhanden ist. Wenn ja, dann
     * eine ImportException werfen.
     */
    if (istDateiImIndex(datei)) {
      throw new ImportException("Die Datei \"" + datei 
          + "\" wurde bereits importiert.");
    }

    /* Lasse das BildDokument aus der Datei erzeugen */
    BildDokument dokument;
    try {
      dokument = BildDokument.erzeugeAusDatei(datei);
    } catch (ErzeugeBildDokumentException e1) {
      throw new ImportException("Konnte kein BildDokument aus der Datei \""
          + datei.getAbsolutePath() + "\" erzeugen.", e1);
    }

    importInLucene(dokument);

    return dokument;
  }

  /**
   * Fuegt das uebergebenen BildDokument dem Luceneindex hinzu.
   * 
   * @param dokument BildDokument, welches in den Lucene-Index aufgenommen
   *          werden soll
   * @throws ImportException wird geworfen, wenn das Bild nicht in den Lucene
   *           -Index aufgenommen werden konnte
   */
  private void importInLucene(BildDokument dokument) throws ImportException {

    /*
     * Wird verwendet, um Text vorzubearbeiten. Z.B werden alle Woerter (Tokens)
     * in klein Buchstaben umgewandelt, oder es werden triviale Woerter
     * weggelassen.
     */
    Analyzer analyzer = new GermanAnalyzer();

    try {
      /* Erzeuge einen IndexWriter, der den bestehenden Index verwendet. */
      IndexWriter writer = new IndexWriter(INDEX_DIR, analyzer);

      /* BildDokument dem Lucene-Index hinzufuegen */
      Document doc = dokument.erzeugeLuceneDocument();
      writer.addDocument(doc);

      /* IndexWriter schliessen */
      writer.close();
    } catch (IOException e) {
      throw new ImportException("Es konnte der Lucene-Index nicht erstellt "
          + "werden", e);
    }
  }

  /**
   * Gibt eine Liste mit allen BildDokumenten zurueck, die im Index dieser
   * Anwendung vorhanden sind.
   * 
   * @return Liste mit allen BildDokumenten, die im Index dieser Anwendung
   *         vorhanden sind.
   * @throws IOException wird geworfen, wenn der Index nicht geoeffnet oder die
   *           Documents nicht gelesen werden konnten
   * @throws ErzeugeBildDokumentException wird geworfen, wenn aus dem
   *           Lucene-Document kein BildDokument erzeugt werden konnte
   */
  private List<BildDokument> gibAlleDokumente()
      throws ErzeugeBildDokumentException, IOException {
    List<BildDokument> ergebnis;
    IndexReader reader = IndexReader.open(INDEX_DIR);

    int anzahl = reader.maxDoc();
    ergebnis = new ArrayList<BildDokument>(anzahl);

    for (int i = 0; i < anzahl; i++) {
      /* Die Documente, die als geloescht markiert sind nicht mehr auslesen. */
      if (!reader.isDeleted(i)) {
        ergebnis.add(BildDokument.erzeugeAusLucene(reader.document(i)));
      }
    }

    reader.close();
    return ergebnis;
  }


  /**
   * Gibt zurueck, ob sich die uebergebene Datei bereits im Index befindet.
   * 
   * @param datei Datei, die ueberprueft wird
   * @return true, falls die Datei im Index vorhanden ist
   */
  public boolean istDateiImIndex(File datei) {

    try {
      Searcher sucher = new IndexSearcher(INDEX_DIR);

      /* Nach dem Dateipfad suchen */
      Hits treffer = sucher.search(new TermQuery(new Term(
          DateipfadMerkmal.FELDNAME, datei.getAbsolutePath())));
      sucher.close();

      /*
       * Falls mindestens ein Treffer erzielt wurde, befindet sich die Datei
       * bereits im Index.
       */
      return treffer.length() > 0;

    } catch (IOException e) {
      /*
       * Wenn z.B. der Index noch nicht vorhanden ist, befindet sich die Datei
       * auch noch nicht im Index. Deshalb wird hier false zurueckgegeben.
       */
      return false;
    }
  }

  /**
   * Suche in allen importierten Bilder nach dem Suchtext und gibt eine
   * entsprechende Trefferliste mit den Suchergebnissen zurueck.
   * Fuehrt suche(String suchtext, 0, 20) aus.
   * 
   * @param suchtext Suchtext, nach dem gesucht wird
   * @return Trefferliste mit den Suchergebnissen
   * @throws SucheException wird geworfen, wenn die Suche nicht erfolgreich mit
   *           einer erzeugten Trefferliste beendet werden kann
   */
  public Trefferliste suche(String suchtext) 
      throws SucheException {
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
  public Trefferliste suche(String suchtext, int offset, int maxanzahl) 
      throws SucheException {

    Trefferliste treffer;
    try {
      /* wenn das Schluesselwort zur Anzeige aller Bilder angegeben wurde, */
      if (suchtext.equalsIgnoreCase(Einstellungen.ALLEBILDER_SCHLUESSEL)) {

        /* dann alle BildDokumente der Trefferliste uebergeben. */
        treffer = new Trefferliste(gibAlleDokumente());
      } else {

        /* Anfrage aufbauen */
        Query anfrage = parser.parse(suchtext);

        /* Suche durchfuehren */
        Searcher sucher = new IndexSearcher(INDEX_DIR);
        treffer = new Trefferliste(sucher.search(anfrage), offset, maxanzahl);
        sucher.close();
      }
    } catch (ParseException e) {
       /* Bei einer ParseException ein leere Trefferliste zurueckgeben */
       treffer = new Trefferliste();
    } catch (IOException e) {
      throw new SucheException("Konnte den Index nicht \u00d6ffnen.", e);
    } catch (ErzeugeException e) {
      throw new SucheException("Die Trefferliste konnte nicht erzeugt werden.",
          e);
    } catch (ErzeugeBildDokumentException e) {
      throw new SucheException("Ein BildDokument konnte nicht gelesen und "
          + "erzeugt werden.", e);
    }

    /* Trefferliste aus dem Lucene SuchErgebnis erzeugen und zurueckgeben */
    return treffer;
  }

  /**
   * Uebernimmt die Aenderungen, die an dem BildDokument gemacht wurden, wie
   * z.B. das Hinzufuegen von Schluesselwoerter.
   * 
   * @param bild Bilddokument, von dem die Aenderungen uebernommen werden
   * @throws AendereException wird geworfen, wenn das Aendern nicht funktioniert
   *           hat
   */
  public void aendere(BildDokument bild) throws AendereException {
    /* BildDokument durch entfernen und neu hinzufuegen aendern */
    try {
      entferne(bild, false);
      importInLucene(bild);
    } catch (EntferneException e) {
      throw new AendereException("\u00c4ndern hat nicht funktioniert da beim "
          + "Entfernen ein Fehler auftrat.", e);
    } catch (ImportException e) {
      throw new AendereException("\u00c4ndern hat nicht funktioniert.", e);
    }
  }
  
  /**
   * Entfernt ein <code>BildDokument</code> aus dem Index.
   * @param bild  das <code>BildDokument</code> das entfernt werden soll
   * @throws EntferneException  wird geworfen, wenn das Bild nicht
   *           geloescht werden konnte
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

    /* BildDokument aus dem Index entfernen */
    IndexReader reader;
    String pfad;
    try {
      reader = IndexReader.open(INDEX_DIR);

      /*
       * Da die Pfadangabe eindeutig ist, entferne das Document mit dem Pfad,
       * der aus dem BildDokument gelesen wurde.
       */
      pfad = bild.getMerkmal(DateipfadMerkmal.FELDNAME).getWert().toString();
      reader.deleteDocuments(new Term(DateipfadMerkmal.FELDNAME, pfad));
      reader.close();
    } catch (IOException e) {
      throw new EntferneException("Konnte das BildDokument nicht entfernen.");
    }

    /*
     * Wenn das Flag auchVonFestplatte gesetzt ist, dann entferne die Bilddatei
     * von der Festplatte.
     */
    if (auchVonFestplatte) {
      File datei = new File(pfad);

      if (!datei.delete()) {
        /* Fehlerbehandlung, falls das Loeschen misslang */
        throw new EntferneException(
            "Das Entfernen von der Festplatte misslang.");
      }
    }
  }
  
  /**
   * Loescht alle Dokumente, die nicht mehr auf den Festplatte vorhanden sind
   * aus dem Index.
   */
  public void clearUpIndex() throws SucheException {
      
    try {
      /* Enthaelt alle Dokumente des Index */
      Trefferliste treffer = new Trefferliste(gibAlleDokumente());
      for (int i = 0; i < treffer.getAnzahlTreffer(); i++) {
        BildDokument dok = treffer.getBildDokument(i);
            
        /* Enthaelt den kompletten Dateipfad des BildDokumentes */
        String pfad = dok.getMerkmal(
                DateipfadMerkmal.FELDNAME).getWert().toString();
             
        /* Wenn die Datei nicht existiert wird Sie aus dem Index entfernt 
         */
        if (!new File(pfad).exists()) {
            entferne(dok, false);
        }
      }
    } catch (IOException e) {
        throw new SucheException("Konnte den Index nicht \u00d6ffnen.", e);
    } catch (ErzeugeBildDokumentException e) {
        throw new SucheException("Die Trefferliste konnte nicht erzeugt werden.",
                e);
    } catch (EntferneException e) {
        throw new SucheException("Konnte das BildDokument nicht entfernen", e);
    }
  }
}
