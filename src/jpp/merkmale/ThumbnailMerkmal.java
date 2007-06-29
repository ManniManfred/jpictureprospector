package jpp.merkmale;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import jpp.core.Einstellungen;
import jpp.core.GeoeffnetesBild;
import jpp.core.exceptions.GeneriereException;
import jpp.core.exceptions.LeseMerkmalAusException;
import jpp.core.thumbnail.ThumbnailGenerierer;

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

  public boolean equals(Object obj) {
    if (obj instanceof ThumbnailMerkmal) {
      /* Da es keine Moeglichkeit gibt die BufferedImages der Thumbnails auf
       * gleichheit zu ueberpruefen, wird hier einfach true zurueckgegeben.
       */
      return true;
    }
    return false;
  }
  /**
   * Liest das Thumbnail aus dem uebergebenen geoeffneten
   * Bild und speichert diesen in diesem Merkmal-Objekt.
   * @param bild  Bild, aus dem der Merkmalswert gelesen wird
   * @throws LeseMerkmalAusException 
   */
  public void leseMerkmalAus(GeoeffnetesBild bild) throws LeseMerkmalAusException {

    /* Hole aus der Zuordnungstabelle fuer das Format den richtigen Generierer 
     */
    Map<String, ThumbnailGenerierer> zuordnung;
    try {
      zuordnung = getZuordnung();
    } catch (Exception e) {
      throw new LeseMerkmalAusException(
          "Konnte dieses ThumbnailMerkmal nicht auslesen.", e);
    }
    ThumbnailGenerierer generierer = zuordnung.get(bild.getFormat());

    /* Falls fuer dieses Format kein Generierer angegeben ist, verwende den
     * Generierer, der fuer alle restlichen Formate zustaendig ist.
     */
    if (generierer == null) {
      generierer = zuordnung.get("*");
    }

    /* Lasse das Thumbnail vom Generierer erzeugen */
    try {
      this.setWert(generierer.generiereThumbnail(bild, 
                Einstellungen.THUMB_MAXBREITE, Einstellungen.THUMB_MAXHOEHE));
    } catch (GeneriereException e) {
      throw new LeseMerkmalAusException(
          "Konnte kein Thumbnail erzeugen.", e);
    }
  }

  /**
   * Liest den Merkmalswert aus einem Lucene-Document und speichert diesen in
   * diesem Merkmal-Objekt.
   * @param doc  Lucene-Document, von dem ueber ein zu diesem Merkmal
   *    gehoerigen Field der Wert ausgelesen wird
   */
  public void leseMerkmalAusLuceneDocument(Document doc) throws LeseMerkmalAusException {
    byte[] bildInBytes = doc.getField(FELDNAME).binaryValue();

    try {
      setWert(ImageIO.read(new ByteArrayInputStream(bildInBytes)));
    } catch (IOException e) {
      /* Fehlerbehandlung */
      throw new LeseMerkmalAusException(
          "Konnte das Merkmal nicht aus dem Lucene-Dokument erzeugen", e);
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
      ImageIO.write((BufferedImage) this.getWert(), 
          Einstellungen.THUMB_FORMAT.getAusgewaehlt().toString(), 
          baos);
      
      baos.flush();
      bildInBytes = baos.toByteArray();

      baos.close();
    } catch (IOException e) {
      /* TODO Weiss noch nicht, nur so ist es nicht das Optimale.
       * Man koennte vielleicht eine richtige Exception werfen, oder vielleicht
       * garnichts machen, da dieser Fall "normal" :) nicht auftritt
       */
      throw new RuntimeException("Konnte das BufferedImage nicht in ein "
          + "ByteArrayOutputStream schreiben.", e);
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
  private static Map<String, ThumbnailGenerierer> getZuordnung() throws Exception {
    if (thumbZuordnung == null) {
      thumbZuordnung = new HashMap<String, ThumbnailGenerierer>();
      
      String klassenname = "";
      
      try {
        BufferedReader leser = new BufferedReader(new FileReader(
            Einstellungen.THUMB_ZUORDUNGSDATEI));

        String zeile;
        while ((zeile = leser.readLine()) != null) {
          int posGleich = zeile.indexOf("=");
          String formateStr = zeile.substring(0, posGleich);

          /* Formate einlesen, die der Generierer bearbeiten soll */
          String[] formate = formateStr.split(",");
          klassenname = zeile.substring(posGleich + 1).trim();

          /* Generierer-Object erzeugen */
          Class klasse = Class.forName(klassenname);
          ThumbnailGenerierer generierer = (ThumbnailGenerierer) klasse
              .newInstance();

          /* Die Zuordnung von den Formaten zu dem Generierer abspeichern */
          for (int i = 0; i < formate.length; i++) {
            thumbZuordnung.put(formate[i].trim().toLowerCase(), generierer);
          }

        }
      } catch (IOException e) {
        throw new Exception("Konnte die Datei " 
            + Einstellungen.THUMB_ZUORDUNGSDATEI
            + " nicht lesen.", e);
      } catch (ClassNotFoundException e) {
        throw new Exception("Konnte die Klasse \"" 
            + klassenname
            + "\" nicht finden.", e);
      } catch (InstantiationException e) {
        throw new Exception("Konnte kein Objekt der Klasse \"" 
            + klassenname
            + "\" erzeugen.", e);
      } catch (IllegalAccessException e) {
        throw new Exception("Konnte kein Objekt der Klasse \"" 
            + klassenname
            + "\" erzeugen.", e);
      }
    }
    return thumbZuordnung;
  }


}
