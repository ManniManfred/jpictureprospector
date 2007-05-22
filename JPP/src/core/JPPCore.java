
package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import merkmale.Merkmal;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

/**
 * Ein Objekt dieser Klasse stellt die Hauptaufgaben dieser JPictureProspector
 * Anwendung bereit.
 * @author Manfred Rosskamp
 */
public class JPPCore {
  
  /** Enthaelt den Pfad zu dem Index-Verzeichnis von Lucene. */
  private static final String INDEX_DIR = "imageIndex";

  /** Enthaelt den Parser, der alle Such-Anfragen in eine Query parst. */ 
  private MultiFieldQueryParser parser;
  
  
  /** 
   * Erstellt ein neues JPPCore-Objekt.
   */
  public JPPCore() {
    
    /* Namen aller möglichen Merkmale herausfinden */
    String[] namen = null;
    try {
      namen = getMerkmalsnamen();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    

    /* MultiFieldQueryParser erzeugen, der über alle Felder sucht und
     * den GermanAnalyzer verwendet.
     */
    parser = new MultiFieldQueryParser(namen, 
        new GermanAnalyzer());
  }
  
  /**
   * Gibt ein Array mit allen Namen der möglichen Merkmale zurueck.
   * @return Array mit allen Namen der möglichen Merkmale
   * TODO Folgendes sollte noch geaendert werden:
   * @throws Exception Falls irgendein Fehler auftritt
   */
  private String[] getMerkmalsnamen() throws Exception {
    ArrayList<String> namen = new ArrayList<String>();
    
    String merkmalsKlassenname = "";
    
    /* Datei mit der Merkmalsliste oeffnen. */
    BufferedReader reader = new BufferedReader(
        new FileReader(Einstellungen.MERKMAL_DATEI));
    
    /* Alle Merkmale aus der Merkmalsliste durchgehen */
    while ((merkmalsKlassenname = reader.readLine()) != null) {
      
      /* Ein Objekt des entsprechenden Merkmals erzeugen */
      Merkmal m = (Merkmal) Class.forName(merkmalsKlassenname)
          .newInstance();
      
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
    
    BildDokument dokument;
    try {
      dokument = BildDokument.erzeugeAusDatei(datei);
    } catch (ErzeugeBildDokumentException e1) {
      throw new ImportException("Konnte kein BildDokument aus der Datei \""
          + datei.getAbsolutePath() + "\" erzeugen.");
    }
    

    /* Wird verwendet, um Text vorzubearbeiten. Z.B werden
     * alle Woerter (Tokens) in klein Buchstaben umgewandelt,
     * oder es werden triviale Woerter weggelassen.
     */
    Analyzer analyzer = new GermanAnalyzer();
    
    try {
      /* Erzeuge einen IndexWriter, der den bestehenden Index verwendet. */
      IndexWriter writer = new IndexWriter(INDEX_DIR, analyzer);
  
      /* BildDokument dem Lucene-Index hinzufuegen */
      writer.addDocument(dokument.erzeugeLuceneDocument());
      
      /* IndexWriter schliessen */
      writer.close();
    } catch(IOException e) {
      throw new ImportException("Es konnte der Lucene-Index nicht erstellt "
          + "werden", e);
    }
    
    return dokument;
  }
  
  /**
   * Suche in allen importierten Bilder nach dem Suchtext und gibt eine 
   * entsprechende Trefferliste mit den Suchergebnissen zurueck.
   * @param suchtext  Suchtext, nach dem gesucht wird
   * @return Trefferliste mit den Suchergebnissen
   */
  public Trefferliste suche(String suchtext) {
    
    /* Anfrage aufbauen */
    Query anfrage = null;
    try {
      anfrage = parser.parse(suchtext);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    
    /* Suche durchfuehren */
    Trefferliste treffer = null;
    try {
      Searcher sucher = new IndexSearcher(INDEX_DIR);
      treffer = new Trefferliste(sucher.search(anfrage));
      sucher.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    /* Trefferliste aus dem Lucene SuchErgebnis erzeugen und zurueckgeben */
    return treffer;
  }
  
  
  /**
   * Uebernimmt die Aenderungen, die an dem BildDokument gemacht wurden, wie
   * z.B. das Hinzufuegen von Schluesselwoerter.
   * @param bild  Bilddokument, von dem die Aenderungen uebernommen werden
   */
  public void aendere(BildDokument bild) {
    /* TODO bilddokument wirklich aendern. */
    //IndexReader.deleteDocument  IndexWriter.addDocument
  }
  
  
  /**
   * Entfernt das uebergebene Bild aus dieser Anwendung und, wenn <code>
   * auchVonFestplatte</code> gesetzt ist, dann auch von der Festplatte.
   * @param bild  BildDokument, welches entfernt werden soll
   * @param auchVonFestplatte  gibt an, ob das Bild auch von der Festplatte
   *      entfernt werden soll
   */
  public void entferne(BildDokument bild, boolean auchVonFestplatte) {
    /* TODO bilddokument aus dieser Anwendung entfernen und evtl. auch von
     * der Festplatte
     */
    //IndexReader.deleteDocument(bild.getDocId);
    // oder IndexWriter.deleteDocuments(new Term("Dateipfad", bild.get("Dateipfad")))
    
    String pfad = null;
    
    /* Evtl. Bilddatei von der Festplatte entfernen. */
    if (auchVonFestplatte) {
      File datei = new File(pfad);
      if (!datei.delete()) {
        /* TODO Fehlerbehandlung, falls das Loeschen misslang */
        System.out.println("Entfernen von der Festplatte misslang.");
      }
      
    }
  }
  
}
