package merkmale;

import core.GeoeffnetesBild;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


/**
 * Ein Objekt dieser Klasse stellt das Thumbnail eines 
 * Bilddokumentes dar.
 * @author Marion Mecking
 */
public class ThumbnailMerkmal extends Merkmal {
   
  /** Name des Lucene-Feldes für dieses Merkmal. */
  private static final String FELDNAME = "Thumbnail";
  
  /** Konstante zur Umrechnung von Byte in kB. */
  private Image thumbnail;
  
  /** Maximale Höhe des Thumbnails in Pixeln. */
  private static final int MAXHOEHE = 100;

  /** Maximale Breite des Thumbnails in Pixeln. */
  private static final int MAXBREITE = 100;
  
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
      
      /* Bild ist im Querformat, Thumbnail bekommt maximale Breite, Höhe
       * wird proportional zur Breite berechnet
       */
      thumbnail = foto.getScaledInstance(MAXBREITE, -1, foto.SCALE_DEFAULT);
    } else {
      
      /* Bild ist im Hochformat, Thumbnail bekommt maximale Höhe, Breite
       * wird proportional zur Breite berechnet
       */
      thumbnail = foto.getScaledInstance(-1, MAXHOEHE, foto.SCALE_DEFAULT);
    }
    
    /* Thumbnail in ein Byte-Array schreiben */   
    //ByteArrayOutputStream bas = new ByteArrayOutputStream();
    //ImageIO.write(thumbnail, "pnm", bas);
    //byte[] data = bas.toByteArray();
    //this.wert =  ??
  }

  /**
   * Liest den Merkmalswert aus einem Lucene-Document und speichert diesen in 
   * diesem Merkmal-Objekt.
   * @param doc  Lucene-Document, von dem ueber ein zu diesem Merkmal 
   *    gehoerigen Field der Wert ausgelesen wird
   */
  public void leseMerkmalAusLuceneDocument(Document doc) {   
    this.wert = doc.get(FELDNAME);  
  }
    
  /**
   * Erzeuge aus diesem Merkmal ein entsprechendes Lucene-Field.
   * @return ein entsprechendes Lucene-Field
   */
  public Field erzeugeLuceneField() {
    return new Field(FELDNAME, this.getWert().toString(), Field.Store.YES, 
      Field.Index.UN_TOKENIZED);
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
  public Image getThumbnail() {
    return this.thumbnail;
  }
}
