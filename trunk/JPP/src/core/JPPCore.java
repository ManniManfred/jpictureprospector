
package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import merkmale.DateipfadMerkmal;
import merkmale.Merkmal;

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

import core.exceptions.AendereException;
import core.exceptions.EntferneException;
import core.exceptions.ErzeugeBildDokumentException;
import core.exceptions.ErzeugeException;
import core.exceptions.ImportException;
import core.exceptions.SucheException;

/**
 * Ein Objekt dieser Klasse stellt die Hauptaufgaben dieser JPictureProspector
 * Anwendung bereit.
 * @author Manfred Rosskamp
 */
public class JPPCore {
  
  /** Enthaelt eine Liste mit den Klassen aller zu verwendenen Merkmale. */
  private static List<Class> merkmalsklassen;
  
  /** Enthaelt den Pfad zu dem Index-Verzeichnis von Lucene. */
  private static final String INDEX_DIR = "imageIndex";

  /** Enthaelt den Parser, der alle Such-Anfragen in eine Query parst. */ 
  private MultiFieldQueryParser parser;
  
  
  /** 
   * Erstellt ein neues JPPCore-Objekt.
   */
  public JPPCore() throws ErzeugeException {
    
    
    /* Namen aller möglichen Merkmale herausfinden */
    String[] namen = null;
    try {
      namen = getMerkmalsnamen();
    } catch (Exception e) {
      throw new ErzeugeException("Konnte die Merkmale nicht erzeugen.", e);
    }
    

    /* MultiFieldQueryParser erzeugen, der über alle Felder sucht und
     * den GermanAnalyzer verwendet.
     */
    parser = new MultiFieldQueryParser(namen, 
        new GermanAnalyzer());
  }

  /**
   * Gibt eine Liste mit allen Merkmalsklassen zurueck, die in der Merkmalsdatei
   * angegeben sind.
   * @return Liste mit allen Merkmalsklassen
   * @throws ClassNotFoundException  wenn ein Klasse, die in der Merkmalsdatei
   *    angegeben ist nicht gefunden wurde.
   * @throws IOException  wenn die Merkamlsdatei nicht gelesen werden konnte
   */
  public static List<Class> getMerkmalsKlassen() throws IOException, 
          ClassNotFoundException {
    if (merkmalsklassen == null) {
      merkmalsklassen = new ArrayList<Class>();
      
      /* Datei mit der Merkmalsliste oeffnen. */
      BufferedReader reader = new BufferedReader(
          new FileReader(Einstellungen.MERKMAL_DATEI));
      
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
   * @return Array mit allen Namen der möglichen Merkmale
   * @throws Exception, wenn die Klassennamen nicht erzeugt werden konnten
   */
  public String[] getMerkmalsnamen() throws Exception {
    ArrayList<String> namen = new ArrayList<String>();
    
    for (Class klasse : getMerkmalsKlassen()) {
      Merkmal m = (Merkmal) klasse.newInstance();
      
      /* Den Namen aus dem Object holen und in der Liste einfuegen */
      namen.add(m.getName());
    }
    
    return namen.toArray(new String[0]);
  }
  
  /**
   * Importiert eine Bilddatei in diese Anwendung.
   * @param datei  Bilddatei, die importiert werden soll
   * @return die importierte Bilddatei als BildDokument 
   */
  public BildDokument importiere(File datei) throws ImportException {
    
    /* ueberpruefen, ob das Bild bereits im Index vorhanden ist. 
     * Wenn ja, dann eine ImportException werfen.
     */
    if (istDateiImIndex(datei)) {
      throw new ImportException("Die Datei wurde bereits importiert.");
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
   * @param dokument
   * @throws ImportException
   */
  private void importInLucene(BildDokument dokument) throws ImportException {
    
    /* Wird verwendet, um Text vorzubearbeiten. Z.B werden
     * alle Woerter (Tokens) in klein Buchstaben umgewandelt,
     * oder es werden triviale Woerter weggelassen.
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
    } catch(IOException e) {
      throw new ImportException("Es konnte der Lucene-Index nicht erstellt "
          + "werden", e);
    }
  }
  
  /**
   * Gibt zurueck, ob sich die uebergebene Datei bereits im Index befindet.
   * @param datei  Datei, die ueberprueft wird
   * @return true, falls die Datei im Index vorhanden ist
   */
  public boolean istDateiImIndex(File datei) {
    
    try {
      Searcher sucher = new IndexSearcher(INDEX_DIR);
      
      /* Nach dem Dateipfad suchen */
      Hits treffer = 
        sucher.search(new TermQuery(
            new Term(DateipfadMerkmal.FELDNAME, datei.getAbsolutePath())));
      sucher.close();
      
      /* Falls mindestens ein Treffer erzielt wurde, befindet sich die Datei
       * bereits im Index.
       */
      return treffer.length() > 0;
      
    } catch (IOException e) {
      /* Wenn z.B. der Index noch nicht vorhanden ist, befindet sich die Datei
       * auch noch nicht im Index. Deshalb wird hier false zurueckgegeben.
       */
      return false;
    }
  }
  
  /**
   * Suche in allen importierten Bilder nach dem Suchtext und gibt eine 
   * entsprechende Trefferliste mit den Suchergebnissen zurueck.
   * @param suchtext  Suchtext, nach dem gesucht wird
   * @return Trefferliste mit den Suchergebnissen
   * @throws SucheException 
   */
  public Trefferliste suche(String suchtext) throws SucheException {

    Trefferliste treffer;
    try {

      /* Anfrage aufbauen */
      Query anfrage = parser.parse(suchtext);

      /* Suche durchfuehren */
      Searcher sucher = new IndexSearcher(INDEX_DIR);
      treffer = new Trefferliste(sucher.search(anfrage));
      sucher.close();
      
    } catch (ParseException e) {
      /* Bei einer ParseException ein leere Trefferliste zurueckgeben */
      treffer = new Trefferliste();
    } catch (IOException e) {
      throw new SucheException("Konnte den Index nicht öffnen.", e);
    }    
    
    /* Trefferliste aus dem Lucene SuchErgebnis erzeugen und zurueckgeben */
    return treffer;
  }
  
  
  /**
   * Uebernimmt die Aenderungen, die an dem BildDokument gemacht wurden, wie
   * z.B. das Hinzufuegen von Schluesselwoerter.
   * @param bild  Bilddokument, von dem die Aenderungen uebernommen werden
   * @throws AendereException 
   */
  public void aendere(BildDokument bild) throws AendereException {
    /* BildDokument durch entfernen und neu hinzufuegen aendern */
    try {
      entferne(bild, false);
      importInLucene(bild);
    } catch (EntferneException e) {
      throw new AendereException("Ändern hat nicht funktioniert.", e);
    } catch (ImportException e) {
      throw new AendereException("Ändern hat nicht funktioniert.", e);
    }
  }
  
  
  /**
   * Entfernt das uebergebene Bild aus dieser Anwendung und, wenn <code>
   * auchVonFestplatte</code> gesetzt ist, dann auch von der Festplatte.
   * @param bild  BildDokument, welches entfernt werden soll
   * @param auchVonFestplatte  gibt an, ob das Bild auch von der Festplatte
   *      entfernt werden soll
   * @throws EntferneException 
   */
  public void entferne(BildDokument bild, boolean auchVonFestplatte) 
          throws EntferneException {
    
    /* BildDokument aus dem Index entfernen  */
    IndexReader reader;
    String pfad;
    try {
      reader = IndexReader.open(INDEX_DIR);
      
      /* Da die Pfadangabe eindeutig ist, entferne das Document mit dem Pfad,
       * der aus dem BildDokument gelesen wurde.
       */
      pfad = bild.getMerkmal(DateipfadMerkmal.FELDNAME).getWert().toString();
      reader.deleteDocuments(new Term(DateipfadMerkmal.FELDNAME, pfad));
      reader.close();
    } catch (IOException e) {
      throw new EntferneException("Konnte das BildDokument nicht entfernen.");
    }
    
    
    
    /* Wenn das Flag auchVonFestplatte gesetzt ist, dann entferne die Bilddatei
     * von der Festplatte. 
     */
    if (auchVonFestplatte) {
      File datei = new File(pfad);
      
      if (!datei.delete()) {
        /* Fehlerbehandlung, falls das Loeschen misslang */
        throw new EntferneException("Das Entfernen von der Festplatte misslang.");
      }
    }
  }
  
}
