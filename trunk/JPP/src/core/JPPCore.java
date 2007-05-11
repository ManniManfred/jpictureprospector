
package core;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
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


  /** 
   * Erstellt ein neues JPPCore-Objekt.
   */
  public JPPCore() {
  }
  
  /**
   * Importiert eine Bilddatei in diese Anwendung.
   * @param datei  Bilddatei, die importiert werden soll
   * @return die importierte Bilddatei als BildDokument 
   */
  public BildDokument importiere(File datei) {
    BildDokument dokument = BildDokument.erzeugeAusDatei(datei);
    
    

    /* Wird verwendet, um Text vorzubearbeiten. Z.B werden
     * alle Woerter (Tokens) in klein Buchstaben umgewandelt,
     * oder es werden triviale Woerter weggelassen.
     */
    Analyzer analyzer = new StandardAnalyzer();
    
    
    try {
      /* Erzeuge einen IndexWriter, der den bestehenden Index verwendet. */
      IndexWriter writer = new IndexWriter(INDEX_DIR, analyzer, false);

      /* BildDokument dem Lucene-Index hinzufuegen */
      writer.addDocument(dokument.erzeugeLuceneDocument());
      
      /* IndexWriter schliessen */
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
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
      /* TODO nachschauen wie das mit der Suche ueber alle Felder ist */
      QueryParser parser = new QueryParser(null, new StandardAnalyzer());
      anfrage = parser.parse(suchtext);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    
    /* Suche durchfuehren */
    Hits treffer = null;
    try {
      Searcher sucher = new IndexSearcher(INDEX_DIR);
      treffer = sucher.search(anfrage);
      sucher.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    /* Trefferliste aus dem Lucene SuchErgebnis erzeugen und zurueckgeben */
    return new Trefferliste(treffer);
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
