package merkmale;

import core.Einstellungen;
import core.GeoeffnetesBild;
import core.thumbnail.ThumbnailGenerierer;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


/**
 * Ein Objekt dieser Klasse stellt das Thumbnail eines
 * Bilddokumentes dar.
 * @author Marion Mecking
 * @author Manfred Rosskamp
 */
public class ThumbnailMerkmal extends Merkmal {

  /** 
   * Name des Lucene-Feldes fuer dieses Merkmal. Sollte die Konstante
   * geaendert werden, muss auch in der Klasse
   * <code>ThumbnailAnzeigePanel</code> die Konstante
   * <code>THUMBNAILMERKMALSNAME</code> entsprechend angepasst werden. 
   */
  public static final String FELDNAME = "Thumbnail";

  /** 
   * Speichert die in der statischen Methode getZuordnung() einmal erstellte
   * Zuordnung von Format zu Generierer ab.
   */
  private static Map<String, ThumbnailGenerierer> thumbZuordnung;

  
  /**
   * Erzeugt ein neues ThumbnailMerkmal.
   */
  public ThumbnailMerkmal() {
    super(FELDNAME);
  }

  /**
   * Liest das Thumbnail aus dem uebergebenen geoeffneten
   * Bild und speichert diesen in diesem Merkmal-Objekt.
   * @param bild  Bild, aus dem der Merkmalswert gelesen wird
   */
  public void leseMerkmalAus(GeoeffnetesBild bild) {

    /* Hole aus der Zuordnungstabelle fuer das Format den richtigen Generierer 
     */
    Map<String, ThumbnailGenerierer> zuordnung = getZuordnung();
    ThumbnailGenerierer generierer = zuordnung.get(bild.getFormat());

    /* Falls fuer dieses Format kein Generierer angegeben ist, verwende den
     * Generierer, der fuer alle restlichen Formate zustaendig ist.
     */
    if (generierer == null) {
      generierer = zuordnung.get("*");
    }

    /* Lasse das Thumbnail vom Generierer erzeugen */
    this.setWert(generierer.generiereThumbnail(bild, 
              Einstellungen.THUMB_MAXBREITE, Einstellungen.THUMB_MAXHOEHE));

  }

  /**
   * Liest den Merkmalswert aus einem Lucene-Document und speichert diesen in
   * diesem Merkmal-Objekt.
   * @param doc  Lucene-Document, von dem ueber ein zu diesem Merkmal
   *    gehoerigen Field der Wert ausgelesen wird
   */
  public void leseMerkmalAusLuceneDocument(Document doc) {
    byte[] bildInBytes = doc.getField(FELDNAME).binaryValue();

    try {
      setWert(ImageIO.read(new ByteArrayInputStream(bildInBytes)));
    } catch (IOException e) {
      /* TODO Fehlerbehandlung */
      e.printStackTrace();
    }
  }

  /**
   * Erzeuge aus diesem Merkmal ein entsprechendes Lucene-Field.
   * @return ein entsprechendes Lucene-Field
   */
  public Field erzeugeLuceneField() {
    byte[] bildInBytes = null; 
      
    /* Thumbnail in ein Byte-Array schreiben */
    try {
      /* oeffnen */
      ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
      
      /* schreiben */
      ImageIO.write((BufferedImage) this.getWert(), Einstellungen.THUMB_FORMAT, 
          baos);
      
      baos.flush();
      bildInBytes = baos.toByteArray();

      baos.close();
    } catch (IOException e) {
      /* TODO Fehlerbehandlung */
      e.printStackTrace();
    }
    return new Field(FELDNAME, bildInBytes, Field.Store.YES);
  }

  /**
   * Gibt zurueck, ob dieses Merkmal vom Endbenutzer editierbar sein kann oder
   * ob ein sich nicht aenderndes festes Merkmal ist.
   * @return <code>true</code>, wenn dieses Merkmal editierbar sein soll
   */
  public boolean istEditierbar() {
    return false;
  }

  /**
   * Liefert das Thumbnail des Bilddokumentes.
   * @return  Thumbnail des Bilddokumentes
   */
  public BufferedImage getThumbnail() {
    return (BufferedImage) getWert();
  }
  
  

  /**
   * Liest aus der Datei "thumbnailGenerierer" ein, welcher ThumbnailGenerierer
   * bei welchem Format verwendet werden soll.
   * @return eine Zuordnung, vom Format zu dem richtigen ThumbnailGenerierer-
   *    Objekt. Alle Formate, die nicht zugeordnet sind, werden von dem 
   *    Generierer bearbeitet, der dem "*" zugeordnet ist.
   */
  private static Map<String, ThumbnailGenerierer> getZuordnung() {
    if (thumbZuordnung == null) {
      thumbZuordnung = new HashMap<String, ThumbnailGenerierer>();
      try {
        BufferedReader leser = new BufferedReader(new FileReader(
            Einstellungen.THUMB_ZUORDUNGSDATEI));

        String zeile;
        while ((zeile = leser.readLine()) != null) {
          int posGleich = zeile.indexOf("=");
          String formateStr = zeile.substring(0, posGleich);

          /* Formate einlesen, die der Generierer bearbeiten soll */
          String[] formate = formateStr.split(",");
          String klassenname = zeile.substring(posGleich + 1);

          /* Generierer-Object erzeugen */
          Class klasse = Class.forName(klassenname.trim());
          ThumbnailGenerierer generierer = (ThumbnailGenerierer) klasse
              .newInstance();

          /* Die Zuordnung von den Formaten zu dem Generierer abspeichern */
          for (int i = 0; i < formate.length; i++) {
            thumbZuordnung.put(formate[i].trim().toLowerCase(), generierer);
          }

        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InstantiationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return thumbZuordnung;
  }


}
