package jpp.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jpp.core.exceptions.ErzeugeBildDokumentException;
import jpp.core.exceptions.LeseMerkmalAusException;
import jpp.merkmale.AlleMerkmale;
import jpp.merkmale.BildtypMerkmal;
import jpp.merkmale.DateipfadMerkmal;
import jpp.merkmale.ExifMerkmal;
import jpp.merkmale.Merkmal;


import org.apache.lucene.document.Document;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;


/**
 * Ein Objekt dieser Klasse stellt ein Dokument mit vielen Merkmalen zu einem
 * Bild dar.
 * 
 * @author Manfred Rosskamp
 */
public final class BildDokument {

  /**
   * Zuordnung des Merkmalsnamen zu einem Merkmal dieses BildDokumentes, wie z.B
   * die Bildhoehe oder Bildbreite.
   */
  private Map<String, Merkmal> merkmale;

  /**
   * Erzeugt ein neues BildDokument.
   */
  private BildDokument() {
    merkmale = new HashMap<String, Merkmal>();
  }


  /**
   * Erzeugt aus einer Bilddatei ein neues BildDokument mit allen Merkmalen.
   * 
   * @param datei Datei, aus der ein neues BildDokument erzeugt wird
   * @return das neu erzeugte BildDokument
   * @throws ErzeugeBildDokumentException wird geworfen, wenn das BildDokument
   *           nicht aus der Datei erzeugt werden konnte
   */
  public static BildDokument erzeugeAusDatei(File datei)
      throws ErzeugeBildDokumentException {
    return erzeugeBildDokumentAus(datei, null);
  }


  /**
   * Erzeugt aus einem Document von Lucene ein entsprechendes BildDokument mit
   * all den entsprechenden Merkmalen.
   * 
   * @param doc Document aus Lucene
   * @return das zum Lucene-Document entsprechende BildDokument
   * @throws ErzeugeBildDokumentException wird geworfen, wenn das BildDokument
   *           nicht aus dem Lucene-Document erzeugt werden konnte
   */
  public static BildDokument erzeugeAusLucene(Document doc)
      throws ErzeugeBildDokumentException {
    return erzeugeBildDokumentAus(null, doc);
  }




  /**
   * Erzeugt aus diesem BildDokument ein entsprechendes Lucene Document.
   * 
   * @return ein zu diesem BildDokument entsprechendes Lucene Document
   */
  public Document erzeugeLuceneDocument() {
    Document doc = new Document();

    /*
     * Alle Merkmal durchgehen, das Field vom Merkmal erzeugen lassen und dann
     * dem Lucene-Document hinzufuegen.
     */
    for (Merkmal m : merkmale.values()) {
      doc.add(m.erzeugeLuceneField());
    }

    return doc;
  }

  /**
   * Berechnet den Hashwert dieses BildDokuments.
   * 
   * @return Hashwert dieses BildDokuments
   */
  public int hashCode() {

    /* Berechne den Hashwert ueber das SchluesselMerkmal "Dateipfad" */
    return getMerkmal(DateipfadMerkmal.FELDNAME).getWert().hashCode();
  }

  /**
   * Gibt true zurueck wenn dieses BildDokument gleich dem uebergebenem ist.
   * Zwei BildDokumente sind gleich, wenn all ihre Merkmale uebereinstimmen.
   * 
   * @param obj das Object mit dem verglichen wird
   * @return <code>true</code>, wenn dieses BildDokument gleich dem
   *         uebergebenem ist
   */
  public boolean equals(Object obj) {

    if (obj instanceof BildDokument) {
      BildDokument b2 = (BildDokument) obj;

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
   * Gibt das Merkmal mit dem <code>merkmalName</code> dieses BildDokuments
   * zurueck.
   * 
   * @param merkmalName Name des Merkmals, welches zurueckgegeben werden soll
   * @return Merkmal dieses BildDokuments mit dem <code>merkmalName</code>
   */
  public Merkmal getMerkmal(String merkmalName) {
    return merkmale.get(merkmalName);
  }



  /**
   * Erzeugt ein neues BildDokument, entweder aus einer Bilddatei oder aus einem
   * Lucene-Document. Falls datei null ist, wird das BildDokument aus dem
   * Lucene-Document erzeugt, welches dann nicht null sein darf. Falls die datei
   * nicht null ist, wird aus der datei das BildDokument erzeugt.
   * 
   * @param datei Bilddatei, aus der das BildDokument erzeugt wird
   * @param doc Lucene-Document, aus der das BildDokument erzeugt wird
   * @return das entweder aus der Bilddatei oder dem Lucene-Document erzeugtem
   *         BildDokument
   * @throws ErzeugeBildDokumentException wird geworfen, wenn das Erzeugen des
   *           BildDokuments nicht funktioniert hat
   */
  private static BildDokument erzeugeBildDokumentAus(File datei, Document doc)
      throws ErzeugeBildDokumentException {

    BildDokument neu = new BildDokument();


    /*
     * Klassenname der jeweiligen Merkmale, die aus der Merkmalsdatei gelesen
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
          /*
           * den Merkmalswert aus dem geoeffnetem Bild vom Merkaml selber lesen
           * und setzten lassen.
           */
          m.leseMerkmalAus(gBild);
        } else {
          /*
           * andernfalls annehmen, dass das Merkmal aus einem LuceneDocument
           * auslesen.
           */
          m.leseMerkmalAusLuceneDocument(doc);
        }

        /* das erzeugte Merkmal diesem BildDokument hinzufuegen */
        neu.addMerkmal(m);

      }
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
   * 
   * @param merkmal Merkmal, welches diesem BildDokument hinzugefuegt wird
   */
  private void addMerkmal(Merkmal merkmal) {
    merkmale.put(merkmal.getName(), merkmal);
  }

  /**
   * Gibt eine Collection mit den Grundmerkmalen zurueck. Grundmerkmale sind
   * die, die in Lucene abgelegt werden und nach den gesucht werden kann.
   * 
   * @return Collection mit den Grundmerkmalen
   */
  public Collection<Merkmal> gibGrundMerkmale() {
    return merkmale.values();
  }

  /**
   * Liefert eine Liste aller Merkmale, die zu diesem Bild vorhanden sind,
   * einschliesslich der Exif-Merkmale bei JPGs.
   * 
   * @return Liste aller Merkmale
   */
  public List<AlleMerkmale> gibAlleMerkmale() {

    ArrayList<AlleMerkmale> alleMerkmale = new ArrayList<AlleMerkmale>();

    /* Grundmerkmal der Liste alleMerkmale hinzufuegen */
    alleMerkmale.addAll(gibGrundMerkmale());


    /* Falls das Bild ein JPG ist, die Exif-Daten hinzufuegen. */
    if (this.getMerkmal(BildtypMerkmal.FELDNAME).equals("jpg")) {

      /* Den Pfad holen */
      String pfad = this.getMerkmal(DateipfadMerkmal.FELDNAME).getWert()
          .toString();
      File jpegFile = new File(pfad);

      /* Mit dem JpegMetadataReader die Metadaten aus dem jpegFile lesen */
      try {
        Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);

        /* Iteration durch die Directories */
        Iterator directories = metadata.getDirectoryIterator();

        while (directories.hasNext()) {
          Directory directory = (Directory) directories.next();

          /* Iteration durch die Tags */
          Iterator tags = directory.getTagIterator();

          while (tags.hasNext()) {
            Tag tag = (Tag) tags.next();
            try {

              /* ExifMerkmal aus dem tag erzeugen und der Liste hinzufuegen */
              AlleMerkmale neuesMerkmal = new ExifMerkmal(tag.getTagName(), tag
                  .getDescription());
              alleMerkmale.add(neuesMerkmal);

            } catch (MetadataException e) {
              /*
               * Nichts, falls ein Exifmerkmal nicht gelesen werden konnte. Fuer
               * eine Version 2, koennt man hier z.B wenigstens eine Log-Ausgabe
               * machen.
               */
              System.out.println("Es konnte eine Metainformation der "
                  + "Exif-Merkmale nicht ausgelesen werden.");
            }
          }
        }
      } catch (JpegProcessingException e) {
        /*
         * Nichts weiter, da es fuer die Anwendung nicht weiter tragisch ist.
         * Fuer eine Version 2, koennt man hier auch wenigstens eine Log-Ausgabe
         * machen.
         */
        System.out.println("Es konnten keine Metainformation zu dem Bild \""
            + pfad + "\" ausgelesen werden.");
      }

    }

    return alleMerkmale;
  }


}
