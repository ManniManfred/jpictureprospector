
package core;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import core.exceptions.ErzeugeBildDokumentException;
import core.exceptions.LeseMerkmalAusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import merkmale.AlleMerkmale;
import merkmale.ExifMerkmal;
import merkmale.Merkmal;

import org.apache.lucene.document.Document;

/**
 * Ein Objekt dieser Klasse stellt ein Dokument mit vielen Merkmalen zu einem
 * Bild dar.
 * @author Manfred Rosskamp
 */
public class BildDokument {
 
  
  
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
  public static BildDokument erzeugeAusDatei(File datei)
      throws ErzeugeBildDokumentException {
    return erzeugeAusDatei(datei, null);
  }

  
  /**
   * Erzeugt aus einem Document von Lucene ein entsprechendes BildDokument mit
   * all den entsprechenden Merkmalen.
   * @param doc  Document aus Lucene
   * @return das zum Lucene-Document entsprechende BildDokument 
   */
  public static BildDokument erzeugeAusLucene(Document doc) 
      throws ErzeugeBildDokumentException {
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

  public int hashCode() {
    return 23;
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof BildDokument) {
      BildDokument b2 = (BildDokument) obj;
      /* TODO Wann sind zwei BildDokumente gleich?
       * Entweder, wenn nur die Pfadangabe uebereinstimmt, oder wenn alle
       * Merkmale uebereinstimmen?
       */
      
      for (Merkmal m : merkmale.values()) {
        if (!m.equals(b2.getMerkmal(m.getName()))) {
          return false;
        }
      }
      return true;
    }
    return false;
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
   * @throws ErzeugeBildDokumentException 
   */
  private static BildDokument erzeugeAusDatei(File datei, Document doc) 
      throws ErzeugeBildDokumentException {
    
	  BildDokument neu = new BildDokument();
    

    /* Klassenname der jeweiligen Merkmale, die aus der Merkmalsdatei gelesen
     * wurde.
     */
    String merkmalsKlassenname = "";
    
    try {
      
      GeoeffnetesBild gBild = null;
      if (datei != null) {
        /* Ein geoffnetes Bild aus der Datei erzeugen */
        gBild = new GeoeffnetesBild(datei);
      }
      
      /* Alle Merkmale aus der Merkmalsliste durchgehen */
      for (Class klasse : JPPCore.getMerkmalsKlassen()) {
        
        /* Nur zu Informationszwecken bei einer Exception */
        merkmalsKlassenname = klasse.getName();
        
        /* Ein Objekt des entsprechenden Merkmals erzeugen */
        Merkmal m = (Merkmal) klasse.newInstance();
        
        if (datei != null) {
          /* den Merkmalswert aus dem geoeffnetem Bild vom Merkaml selber lesen
           * und setzten lassen.
           */
          m.leseMerkmalAus(gBild);
        } else {
          /* andernfalls annehmen, dass das Merkmal aus einem LuceneDocument
           * auslesen.
           */
          m.leseMerkmalAusLuceneDocument(doc);
        }
        
        /* das erzeugte Merkmal diesem BildDokument hinzufuegen */
        neu.addMerkmal(m);
        
      }
    } catch (FileNotFoundException e) {
      throw new ErzeugeBildDokumentException("Merkmalsdatei \"" 
          + Einstellungen.MERKMAL_DATEI
          + "\" existiert nicht.", e); 
    } catch (LeseMerkmalAusException e) {
      throw new ErzeugeBildDokumentException("Das Merkmal \"" 
          + merkmalsKlassenname
          + "\" konnte den Wert seines Merkmals nicht auslesen.", e); 
    } catch (IOException e) {
      throw new ErzeugeBildDokumentException("Bilddatei " 
          + datei.getAbsolutePath() + " konnte nicht gelesen werden.", e);
    } catch (InstantiationException e) {
      throw new ErzeugeBildDokumentException("Konnte keine Instance der "
          + "Merkmalsklasse \"" + merkmalsKlassenname + "\" erzeugen.", e);
    } catch (IllegalAccessException e) {
      throw new ErzeugeBildDokumentException("Konnte keine Instance der "
          + "Merkmalsklasse \"" + merkmalsKlassenname + "\" erzeugen.", e);
    } catch (ClassNotFoundException e) {
      throw new ErzeugeBildDokumentException("Konnte keine Instance von einer "
          + "Merkmalsklasse erzeugen.", e);
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
  
  /**
   * Liefert eine Liste aller Merkmale, die zu diesem Bild vorhanden sind,
   * einschliesslich der Exif-Merkmale bei JPGs.
   *
   * @return  Liste aller Merkmale
   */
  public List<AlleMerkmale> gibAlleMerkmale() {
    
    /* vorhandene Merkmale als String uebernehmen. */
    ArrayList<AlleMerkmale> alleMerkmale = (ArrayList) merkmale.values();
    
    String pfad =  this.getMerkmal("DateipfadMerkmal").getWert().toString();   
    File jpegFile = new File(pfad);
    
    /* f�r JPG die Exif-Daten hinzufuegen. */
    try{
      
      Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);      
      /* Iteration durch die Directories */
      Iterator directories = metadata.getDirectoryIterator();
      
      while (directories.hasNext()) {
	Directory directory = (Directory)directories.next();
	/* Iteration durch die Tags */
	Iterator tags = directory.getTagIterator();
	
	while (tags.hasNext()) {
	  Tag tag = (Tag)tags.next();
	  /* Werte setzen */
	  AlleMerkmale neuesMerkmal = 
	      new ExifMerkmal(tag.getTagName(), tag.getDescription());
	  alleMerkmale.add(neuesMerkmal);
	}
      }
    } catch (Exception e) {
      /* Bild ist kein JPG, es werden keine Merkmale zugefuegt. */
    }
    
    return alleMerkmale;
  }
  
  
}
