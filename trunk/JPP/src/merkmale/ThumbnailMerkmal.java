package merkmale;

import core.GeoeffnetesBild;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


/**
 * Ein Objekt dieser Klasse stellt das Thumbnail eines
 * Bilddokumentes dar.
 * @author Marion Mecking
 */
public class ThumbnailMerkmal extends Merkmal {
  
  /** Name des Lucene-Feldes fuer dieses Merkmal. Sollte die Konstante
   * geaendert werden, muss auch in der Klasse
   * <code>ThumbnailAnzeigePanel</code> die Konstante
   * <code>THUMBNAILMERKMALSNAME</code> entsprechend angepasst werden. */
  public static final String FELDNAME = "Thumbnail";
  
  /** Konstante zur Umrechnung von Byte in kB. */
  private BufferedImage thumbnail;
  
  /** Maximale Hoehe des Thumbnails in Pixeln. */
  private static final int MAXHOEHE = 256;
  
  /** Maximale Breite des Thumbnails in Pixeln. */
  private static final int MAXBREITE = 256;
  
  /** Format, in dem die Thumbnails gespeichert werden. */
  private static final String FORMAT = "jpeg";
  
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
    
    BufferedImage foto = bild.getBild();
    Image thumbImage = null;
    
    if (foto.getWidth() > foto.getHeight()) {
      
      /* Bild ist im Querformat, Thumbnail bekommt maximale Breite, Hoehe
       * wird proportional zur Breite berechnet
       */
      thumbImage = foto.getScaledInstance(MAXBREITE, -1, foto.SCALE_DEFAULT);
      thumbnail = this.wandleInBufferedImage(thumbImage);
    } else {
      
      /* Bild ist im Hochformat, Thumbnail bekommt maximale Hoehe, Breite
       * wird proportional zur Breite berechnet
       */
      thumbImage = foto.getScaledInstance(-1, MAXHOEHE, foto.SCALE_DEFAULT);
      thumbnail = this.wandleInBufferedImage(thumbImage);
    }
    
    /* Thumbnail in ein Byte-Array schreiben */
    
    try{
      /* oeffnen */
      ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
      /* schreiben */
      ImageIO.write(this.getThumbnail(), FORMAT, baos);
      /* "png" "jpeg" format desired, no "gif" yet. */
      /* schliessen */
      baos.flush();
      this.wert = baos.toByteArray();
      
      baos.close();
    } catch(IOException e) {
      // Exception werfen
    }
  }
  
  /**
   * Liest den Merkmalswert aus einem Lucene-Document und speichert diesen in
   * diesem Merkmal-Objekt.
   * @param doc  Lucene-Document, von dem ueber ein zu diesem Merkmal
   *    gehoerigen Field der Wert ausgelesen wird
   */
  public void leseMerkmalAusLuceneDocument(Document doc) {
    this.wert = doc.get(FELDNAME);

    
    try{
      this.thumbnail = 
	  ImageIO.read(new ByteArrayInputStream((byte[]) this.wert));
    } catch(IOException e) {
      // FEHLER FANGEN
    }
  }
  
  /**
   * Erzeuge aus diesem Merkmal ein entsprechendes Lucene-Field.
   * @return ein entsprechendes Lucene-Field
   */
  public Field erzeugeLuceneField() {
    return new Field(FELDNAME, (byte[]) this.getWert(), Field.Store.YES);
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
    return this.thumbnail;
  }
  
  /**
   * Wandelt das übergebene Image in ein BufferedImage um.
   *
   * @param  Image, das umgewandelt werden soll.
   * @return BufferedImage, das erzeugt wird.
   */
  public static BufferedImage wandleInBufferedImage(Image image) {
    
    BufferedImage bufferedImage = null;
    if(image instanceof BufferedImage) {
      
      /* Image ist eine Instanz von BufferedImage, einfaches casten. */
      bufferedImage = (BufferedImage) image;
    } else {
      
      /* BufferedImage neu erzeugen */
      bufferedImage = new BufferedImage(image.getWidth(null), 
	  image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
      // ARGB to support transparency if in original image
      
      /* Image an den Ursprung zeichnen. */
      Graphics2D g = bufferedImage.createGraphics();
      g.drawImage(image, 0, 0, null);
      g.dispose(); 
    }
    
    return bufferedImage;
  }
  
}
