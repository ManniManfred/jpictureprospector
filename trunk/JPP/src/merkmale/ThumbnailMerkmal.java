package merkmale;

import core.GeoeffnetesBild;
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
    
    if (foto.getWidth() > foto.getHeight()) {
      
      /* Bild ist im Querformat, Thumbnail bekommt maximale Breite, Hoehe
       * wird proportional zur Breite berechnet
       */
      thumbnail = (BufferedImage) 
        foto.getScaledInstance(MAXBREITE, -1, foto.SCALE_DEFAULT);
    } else {
      
      /* Bild ist im Hochformat, Thumbnail bekommt maximale Hoehe, Breite
       * wird proportional zur Breite berechnet
       */
      thumbnail = (BufferedImage) 
        foto.getScaledInstance(-1, MAXHOEHE, foto.SCALE_DEFAULT);
    }
    
    /* Thumbnail in ein Byte-Array schreiben */
    

    try{
    // O P E N
    ByteArrayOutputStream baos = new ByteArrayOutputStream( 1000 );   
    // W R I T E
    ImageIO.write(this.getThumbnail(), "jpeg", baos ); 
    /* "png" "jpeg" format desired, no "gif" yet. */ 
    // C L O S E
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
      //FEHLERBEHANDLUNG
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
}
