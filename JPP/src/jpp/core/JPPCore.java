package jpp.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jpp.core.exceptions.AendereException;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ErzeugeBildDokumentException;
import jpp.core.exceptions.ErzeugeException;
import jpp.core.exceptions.ImportException;
import jpp.core.exceptions.SucheException;
import jpp.merkmale.DateipfadMerkmal;

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
public class JPPCore extends AbstractJPPCore {

  /** Enthaelt den Pfad zu dem Index-Verzeichnis von Lucene. */
  private String indexDir;

  /** Enthaelt den Parser, der alle Such-Anfragen in eine Query parst. */
  private MultiFieldQueryParser parser;
  


  /**
   * Erstellt ein neues JPPCore-Objekt.
   * 
   * @param indexDir Enthaelt den Pfad zu dem Index-Verzeichnis von Lucene
   * @throws ErzeugeException wird geworfen, falls beim Erzeugen dieses Objektes
   *           ein Fehler auftritt. Z.B wenn die Merkmalsdatei nicht existiert,
   *           oder die Klassen zu den Merkmals-Klassennamen nicht gefunden
   *           wurden.
   */
  public JPPCore(String indexDir) throws ErzeugeException {
    super();
    
    this.indexDir = indexDir;

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
   * Importiert eine Bilddatei in diese Anwendung.
   * 
   * @param datei Bilddatei, die importiert werden soll
   * @return die importierte Bilddatei als BildDokument
   * @throws ImportException wird geworfen, wenn das Importieren der Bilddatei
   *           nicht funktioniert, z.B. wenn das Bild schon importiert wurde.
   */
  public void importiere(URL datei) throws ImportException {

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
          + datei + "\" erzeugen.", e1);
    }

    importInLucene(dokument);

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
      IndexWriter writer = new IndexWriter(indexDir, analyzer);

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
    IndexReader reader = IndexReader.open(indexDir);

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
   * Gibt das BildDokument zu der entsprechenden URL der Bilddatei zurueck.
   * 
   * @param datei URL zu der das BildDokument zurueckgegeben werden soll
   * @return BildDokument zu der entsprechenden URL der Bilddatei oder null,
   *         falls dieses BildDokument keinmal oder mehr als einmal im Index
   *         befindet.
   * @throws ErzeugeBildDokumentException
   */
  public BildDokument getBildDokument(URL datei)
      throws ErzeugeBildDokumentException {
    try {
      Searcher sucher = new IndexSearcher(indexDir);

      /* Nach dem Dateipfad suchen */
      Hits treffer = sucher.search(new TermQuery(new Term(
          DateipfadMerkmal.FELDNAME, datei.toString())));

      /*
       * Falls mindestens ein Treffer erzielt wurde, befindet sich die Datei
       * bereits im Index.
       */
      if (treffer.length() == 1) {
        return BildDokument.erzeugeAusLucene(treffer.doc(0));
      }
      
      sucher.close();

    } catch (IOException e) {
      /*
       * Wenn z.B. der Index noch nicht vorhanden ist, befindet sich die Datei
       * auch noch nicht im Index. Deshalb wird hier false zurueckgegeben.
       */
    }
    return null;
  }

  /**
   * Gibt zurueck, ob sich die uebergebene Datei bereits im Index befindet.
   * 
   * @param datei Datei, die ueberprueft wird
   * @return true, falls die Datei im Index vorhanden ist
   */
  private boolean istDateiImIndex(URL datei) {

    try {
      Searcher sucher = new IndexSearcher(indexDir);

      /* Nach dem Dateipfad suchen */
      Hits treffer = sucher.search(new TermQuery(new Term(
          DateipfadMerkmal.FELDNAME, datei.toString())));
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
        treffer = gibAlleDokumentAdv(offset, maxanzahl);
      } else {

        /* Anfrage aufbauen */
        Query anfrage = parser.parse(suchtext);
  
        /* Suche durchfuehren */
        Searcher sucher = new IndexSearcher(indexDir);
        treffer = new Trefferliste(sucher.search(anfrage), offset, maxanzahl);
        sucher.close();
      }
      
    } catch (ParseException e) {
      /* Bei einer ParseException ein leere Trefferliste zurueckgeben */
      // TODO Aussage verbessern
      throw new SucheException("Die Anfrage ist nicht korrekt.", e);
    } catch (IOException e) {
      throw new SucheException("Konnte den Index nicht \u00d6ffnen.", e);
    } catch (ErzeugeException e) {
      throw new SucheException("Die Trefferliste konnte nicht erzeugt werden.",
          e);
    } catch (ErzeugeBildDokumentException e) {
      throw new SucheException("Das BildDokument konnte nicht erzeugt werden.",
          e);
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
   * Entfernt die entsprechende Datei aus dem Index
   * 
   * @param datei Datei, welches entfernt werden soll
   * @param auchVonFestplatte gibt an, ob das Bild auch von der Festplatte
   *          entfernt werden soll
   * @throws EntferneException wird geworfen, wenn das Entfernen des
   *           BildDokuments fehlgeschlagen ist
   */
  public void entferne(URL datei, boolean auchVonFestplatte)
      throws EntferneException {

    /* BildDokument aus dem Index entfernen */
    IndexReader reader;
    try {
      reader = IndexReader.open(indexDir);
      reader.deleteDocuments(new Term(DateipfadMerkmal.FELDNAME, datei
          .toString()));
      reader.close();
    } catch (IOException e) {
      throw new EntferneException("Konnte das BildDokument nicht entfernen.");
    }

    /*
     * Wenn das Flag auchVonFestplatte gesetzt ist, dann entferne die Bilddatei
     * von der Festplatte.
     */
    if (auchVonFestplatte && datei.getProtocol().equalsIgnoreCase("file")) {
      File zuEntfernen = new File(datei.getPath());
      if (!zuEntfernen.delete()) {
        /* Fehlerbehandlung, falls das Loeschen misslang */
        throw new EntferneException(
            "Das Entfernen von der Festplatte misslang.");
      }
    }
  }

  /**
   * Loescht alle Dokumente, die nicht mehr auf den Festplatte vorhanden sind
   * aus dem Index.
   * @return Meldungen, welche Dokumente aus dem Index entfernt wurden
   */
  public String clearUpIndex() throws SucheException {
    String meldung = "";
    
    try {
      /* Enthaelt alle Dokumente des Index */
      List<BildDokument> alleDoks = gibAlleDokumente();

      for (BildDokument dok : alleDoks) {

        /* Enthaelt den kompletten Dateipfad des BildDokumentes */
        URL pfad = (URL) dok.getMerkmal(DateipfadMerkmal.FELDNAME).getWert();

        /*
         * Wenn die Datei nicht existiert wird Sie aus dem Index entfernt
         */
        if (!urlExists(pfad)) {
          try {
            entferne(dok, false);
            meldung += "BildDokument " + pfad + " wurde entfernt.\n" ;
          }  catch (EntferneException e) {
            meldung += "BildDokument " + pfad 
              + " konnte nicht entfernt werden." + e + "\n";
          }
        }
      }
    } catch (IOException e) {
      throw new SucheException("Konnte den Index nicht \u00d6ffnen.", e);
    } catch (ErzeugeBildDokumentException e) {
      throw new SucheException("Die Trefferliste konnte nicht erzeugt werden.",
          e);
    }
    return meldung;
  }

  /**
   * Prueft, ob eine angegeben URL-Datei noch existiert.
   * 
   * @param url Url die geprueft wird
   * @return true, wenn diese URL noch existiert
   */
  private static boolean urlExists(URL url) {
    try {
      // note : you may also need
      // HttpURLConnection.setInstanceFollowRedirects(false)
      return url.getContent() != null;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  
  private Trefferliste gibAlleDokumentAdv(int offset, int maxanzahl) 
    throws IOException, ErzeugeBildDokumentException {
    
    List<BildDokument> ergebnis;
    IndexReader reader = IndexReader.open(indexDir);

    int anzahl = reader.maxDoc();
    ergebnis = new ArrayList<BildDokument>(anzahl);

    int anzahlBilder = 0;
    
    for (int i = 0; i < anzahl && (anzahlBilder < offset + maxanzahl); i++) {
      /* Die Documente, die als geloescht markiert sind nicht mehr auslesen. */
      if (!reader.isDeleted(i)) {
        if (anzahlBilder >= offset) {
          ergebnis.add(BildDokument.erzeugeAusLucene(reader.document(i)));
        }
        anzahlBilder++;
      }
    }
    reader.close();
    return new Trefferliste(ergebnis, anzahl);
  }
}
