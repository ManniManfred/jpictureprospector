
package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import merkmale.Merkmal;

import org.apache.lucene.document.Document;

/**
 * Ein Objekt dieser Klasse stellt ein Dokument mit vielen Merkmalen zu einem
 * Bild dar.
 * @author Manfred Rosskamp
 */
public class BildDokument {
 
  /**
   * Enthaelt den Pfad zur Merkmalsdatei, in der eine Liste mit allen
   * zu verwendenen Merkmalen steht.
   */
  private static final String MERKMAL_DATEI = "merkmale";
  
  
  /** 
   * Zuordnung des Merkmalsnamen zu einem Merkmal dieses BildDokumentes,
   * wie z.B die Bildhoehe oder Bildbreite. 
   */
  private Map<String, Merkmal> merkmale;
  
  
  /** 
   * Erzeugt ein neues BildDokument
   */
  private BildDokument() {
    merkmale = new HashMap<String, Merkmal>();
  }
  
  
  /**
   * Erzeugt aus einer Bilddatei ein neues BildDokument mit allen Merkmalen.
   * @param datei  Datei, aus der ein neues BildDokument erzeugt wird
   * @return das neu erzeugte BildDokument
   */
  public static BildDokument erzeugeAusDatei(File datei) {
    return erzeugeAusDatei(datei, null);
  }

  
  /**
   * Erzeugt aus einem Document von Lucene ein entsprechendes BildDokument mit
   * all den entsprechenden Merkmalen.
   * @param doc  Document aus Lucene
   * @return das zum Lucene-Document entsprechende BildDokument 
   */
  public static BildDokument erzeugeAusLucene(Document doc) {
    return erzeugeAusDatei(null, doc);
  }

  
  
  
  /**
   * Erzeugt aus diesem BildDokument ein entsprechendes Lucene Document.
   * @return ein zu diesem BildDokument entsprechendes Lucene Document
   */
  public Document erzeugeLuceneDocument() {
    Document doc = new Document();
    
    /* Alle Merkmal durchgehen, das Field vom Merkmal erzeugen lassen und
     * dann dem Lucene-Document hinzufuegen.
     */
    for (Merkmal m : merkmale.values()) {
      doc.add(m.erzeugeLuceneField());
    }
    
    return doc;
  }

  /**
   * Gibt das Merkmal mit dem <code>merkmalName</code> zu diesem BildDokument 
   * @param name
   * @return
   */
  public Merkmal getMerkmal(String merkmalName) {
    return merkmale.get(merkmalName);
  }
  
  
  
  /**
   * Erzeugt ein neues BildDokument, entweder aus einer Bilddatei oder aus
   * einem Lucene-Document. Falls datei null ist, wird das BildDokument
   * aus dem Lucene-Document erzeugt, welches dann nicht null sein darf.
   * Falls die datei nicht null ist, wird aus der datei das BildDokument
   * erzeugt.
   * @param datei  Bilddatei, aus der das BildDokument erzeugt wird
   * @param doc  Lucene-Document, aus der das BildDokument erzeugt wird
   * @return das entweder aus der Bilddatei oder dem Lucene-Document erzeugtem
   *    BildDokument
   */
  private static BildDokument erzeugeAusDatei(File datei, Document doc) {
    
	  BildDokument neu = new BildDokument();
    
    
    try {
      
      GeoeffnetesBild gBild = null;
      if (datei != null) {
        /* Ein geoffnetes Bild aus der Datei erzeugen */
        /* TODO Man sollte zwischen der IOException, die hier auftreten kann
         * mit der IOException, die beim Lesen der Merkmalsdatei auftreten kann
         * unterscheiden und entsprechend reagieren.
         */
        gBild = new GeoeffnetesBild(datei);
      }
      
      
      /* Datei mit der Merkmalsliste oeffnen. */
      BufferedReader reader = new BufferedReader(
          new FileReader(MERKMAL_DATEI));
      
      
      /* Alle Merkmale aus der Merkmalsliste durchgehen */
      String merkmalsKlassenname;
      while ((merkmalsKlassenname = reader.readLine()) != null) {
        
        /* Ein Objekt des entsprechenden Merkmals erzeugen */
        Merkmal m = (Merkmal) Class.forName(merkmalsKlassenname)
            .newInstance();
        
        if (datei != null) {
          /* den Merkmalswert aus dem geoeffnetem Bild vom Merkaml selber lesen
           * und setzten lassen.
           */
          m.leseMerkmalAus(gBild);
        } else {
          /* andernfalls annehmen, dass das Merkmal aus einem LuceneDocument
           * auslesen.
           */
          /* TODO evtl eine Pruefung, ob doc nicht evtl. null ist, obwohl dies
           * nicht sein darf.
           */
          m.leseMerkmalAusLuceneDocument(doc);
        }
        
        
        /* das erzeugte Merkmal diesem BildDokument hinzufuegen */
        neu.addMerkmal(m);
        
      }
      reader.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return neu;
  }
  
  /**
   * Fuegt das uebergebene Merkmal diesem BildDokument hinzu.
   * @param merkmal
   */
  private void addMerkmal(Merkmal merkmal) {
    merkmale.put(merkmal.getName(), merkmal);
  }
  
  
}
