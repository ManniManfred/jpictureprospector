
package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Ein Objekt dieser Klasse stellt ein Dokument mit vielen Merkmalen zu einem
 * Bild dar.
 * @author Manfred Rosskamp
 */
public class BildDokument {
 
  public static final String DATEIGROESSE = "dateigroesse";
  public static final String BESCHREIBUNG = "beschreibung";
  public static final String SCHLUESSELWOERTER = "schluesselworter";
  public static final String BILDHOEHE = "bildhoehe";
  public static final String BILDBREITE = "bildbreite";
  public static final String BILDTYP = "bildtyp";
  
  /** 
   * Zuordnung des Merkmalsnamen zu einem Merkmal dieses BildDokumentes,
   * wie z.B die Bildhoehe oder Bildbreite. 
   */
  private Map<String, Merkmal> merkmale;
  
  
  /** 
   * Erzeugt ein neues BildDokument
   */
  private BildDokument() {
  }
  
  /**
   * Erzeugt aus einer Bilddatei ein neues BildDokument mit allen Merkmalen.
   * @param datei  Datei, aus der ein neues BildDokument erzeugt wird
   * @return das neu erzeugte BildDokument
   */
  public static BildDokument erzeugeAusDatei(File datei) {
    
    return new BildDokument();
    
//    
//    try {
//      BufferedReader reader = new BufferedReader(new FileReader(
//          "merkmale"));
//      anzahlMerkmale = 0;
//      String merkmalsKlassenname;
//      while ((merkmalsKlassenname = reader.readLine()) != null) {
//        Merkmal m = (Merkmal) Class.forName(merkmalsKlassenname)
//            .newInstance();
//        Date vorher = new Date();
//        m.liesMerkmal(bilddatei);
//        Date nachher = new Date();
//        
//        if (wertung.get(m.getName()) == null) {
//          wertung.put(m.getName(), (double) (nachher.getTime() - vorher.getTime()));
//        } else {
//          wertung.put(m.getName(), (double) (wertung.get(m.getName()) + nachher.getTime() - vorher.getTime()));
//        }
//          
//        anzahlMerkmale++;
//      }
//      reader.close();
//    } catch (FileNotFoundException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (InstantiationException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (IllegalAccessException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (ClassNotFoundException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }

  }
  
  /**
   * Erzeugt aus einem Document von Lucene ein entsprechendes BildDokument mit
   * all den entsprechenden Merkmalen.
   * @param doc  Document aus Lucene
   * @return das zum Lucene-Document entsprechende BildDokument 
   */
  public static BildDokument erzeugeAusLucene(org.apache.lucene.document.Document doc) {
    return new BildDokument();
  }

  
  /**
   * Erzeugt aus diesem BildDokument ein entsprechendes Lucene Document.
   * @return ein zu diesem BildDokument entsprechendes Lucene Document
   */
  public org.apache.lucene.document.Document erzeugeLuceneDocument() {
    return new org.apache.lucene.document.Document();
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
   * Liefert zu diesem <code>BildDokument</code> das geoeffnete Bild.
   * 
   * @return  das geoeffnete Bild
   * @throws IOException  wirft eine Fehlermeldung, wenn das
   *         geoeffnete Bild ungueltig ist
   */
  public GeoeffnetesBild getGeoeffnetesBild() throws IOException {
    /* TODO muss noch vernuenftig implementiert werden */
    return new GeoeffnetesBild(new File("/test/img/kuchen.bmp"));
  }
}
