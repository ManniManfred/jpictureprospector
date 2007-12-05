package jpp.xml;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import jpp.core.BildDokument;
import jpp.core.Einstellungen;
import jpp.core.Trefferliste;
import jpp.merkmale.DateipfadMerkmal;
import jpp.merkmale.Merkmal;
import jpp.merkmale.ThumbnailMerkmal;
import jpp.webapp.Mapping;

public class XMLParser {

  /**
   * Gibt ein XML-Dokument der uebergebenen Trefferliste zurueck.
   * 
   * @param liste Trefferliste, aus der das XML-Dokument erzeugt wird.
   * @return XML-Dokument der Trefferliste
   */
  public static String getTrefferlisteDok(Trefferliste liste) {
    String ergebnis =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    //ergebnis += "<?xml-stylesheet type=\"text/xsl\" href=\"trefferDok.xsl\" ?>";
    ergebnis += trefferlisteToXml(liste);
    
    return ergebnis;
  }
  
  
  private static String trefferlisteToXml(Trefferliste liste) {

    String ergebnis = "<Trefferliste Anzahl=\"" + liste.getGesamtAnzahlTreffer() 
      + "\">\n";
    
    for (int i = 0; i < liste.getAnzahlTreffer(); i++) {
      ergebnis += bildDokumentToXml(liste.getBildDokument(i));
    }
    
    ergebnis += "</Trefferliste>";
    return ergebnis;
  }
  
  
  private static String bildDokumentToXml(BildDokument dok) {
    String ergebnis = "<BildDokument>\n";
    
    for (Merkmal m : dok.gibGrundMerkmale()) {
      ergebnis += merkmalToXml(m, dok);
    }
    
    ergebnis += "</BildDokument>\n";
    return ergebnis;
  }
  
  
  private static String merkmalToXml(Merkmal m, BildDokument dok) {
    return "<" + m.getClass().getName() + ">"
      + getXmlMerkmalsWert(m, dok)
      + "</" + m.getClass().getName() + ">\n";
  }
  
  private static String getXmlMerkmalsWert(Merkmal m, BildDokument dok) {
    String ergebnis;
    
    try {
      if (m instanceof DateipfadMerkmal) {
        /* Mapping anwenden */
        ergebnis = Mapping.wandleInWWW((URL) m.getWert()).toString();
        
      } else if (m instanceof ThumbnailMerkmal) {
        URL url = Mapping.wandleInWWW(
            (URL) dok.getMerkmal(DateipfadMerkmal.FELDNAME).getWert());
        
        String thumbName = "th_" + url.getPath().replaceAll("/", "_") + "." 
          + Einstellungen.THUMB_FORMAT.getAusgewaehlt();
        
        File thumbFile = new File(Einstellungen.thumbnailOrdner + thumbName);
        
        if (!thumbFile.exists()) {
          /* Speichere Thumbnail */
          try {
            if (m.getWert() == null) {
              ergebnis = "Fehler: Wert ist null";
            }
            
            OutputStream thumbOut = new FileOutputStream(thumbFile);
            
            ImageIO.write((BufferedImage) m.getWert(), 
               Einstellungen.THUMB_FORMAT.getAusgewaehlt().toString(), thumbOut);
            thumbOut.close();
            
            ergebnis = Mapping.wandleInWWW(thumbFile.toURL()).toString();
          } catch (IOException e) {
            ergebnis = "Fehler: " + e;
          }
        } else {
          ergebnis = Mapping.wandleInWWW(thumbFile.toURL()).toString();
        }
          
      } else {
        ergebnis = m.getWert().toString();
      }
    } catch (MalformedURLException e1) {
      ergebnis = "Fehler: " + e1;
    }
    
    return ergebnis;
  }
}
