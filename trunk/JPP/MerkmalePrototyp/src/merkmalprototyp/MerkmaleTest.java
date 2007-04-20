/*
 * MerkmaleTest.java
 *
 * Created on 20. April 2007, 13:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package merkmalprototyp;

import java.io.File;
import merkmalprototyp.merkmale.BildbreiteMerkmal;
import merkmalprototyp.merkmale.BildhoeheMerkmal;
import merkmalprototyp.merkmale.DateigroesseMerkmal;
import merkmalprototyp.merkmale.DateinameMerkmal;
import merkmalprototyp.merkmale.DateitypMerkmal;
import merkmalprototyp.merkmale.LetzterZugriffMerkmal;
import merkmalprototyp.merkmale.ThumbnailMerkmal;

/**
 *
 * @author Besitzer
 */
public class MerkmaleTest {
  
  // PFAD DER TESTDATEI!
  private static final String DATEIPFAD = 
      "F:\\Studium\\4 Semester\\PPR\\JPP\\src\\merkmalprototyp\\zollverein.jpg";
  
  /** Creates a new instance of MerkmaleTest */
  public MerkmaleTest() {
    
  }
  
  public static void main(String[] args) {
    File bilddatei = new File(DATEIPFAD);
    
    // Dateiname
    Merkmal dateiname = new DateinameMerkmal();
    dateiname.liesMerkmal(bilddatei);    
    System.out.println("Name: " + dateiname.getName() + "\t" + 
	               "Wert: " + dateiname.getWert());
    
    // Dateigröße in kB
    Merkmal dateigroesse = new DateigroesseMerkmal();
    dateigroesse.liesMerkmal(bilddatei);    
    System.out.println("Name: " + dateigroesse.getName() + "\t" + 
	               "Wert: " + dateigroesse.getWert());    
    
    // Letzter Zugriff
    Merkmal letzterZugriff = new LetzterZugriffMerkmal();
    letzterZugriff.liesMerkmal(bilddatei);    
    System.out.println("Name: " + letzterZugriff.getName() + "\t" + 
	               "Wert: " + letzterZugriff.getWert());   
    
    // Dateityp
    Merkmal dateityp = new DateitypMerkmal();
    dateityp.liesMerkmal(bilddatei);    
    System.out.println("Name: " + dateityp.getName() + "\t" + 
	               "Wert: " + dateityp.getWert());    
    
    // Bildhöhe
    Merkmal hoehe = new BildhoeheMerkmal();
    hoehe.liesMerkmal(bilddatei);    
    System.out.println("Name: " + hoehe.getName() + "\t" + 
	               "Wert: " + hoehe.getWert());   
    
    // Bildbreite
    Merkmal breite = new BildbreiteMerkmal();
    breite.liesMerkmal(bilddatei);    
    System.out.println("Name: " + breite.getName() + "\t" + 
	               "Wert: " + breite.getWert());   
    
    // Thumbnail
    System.out.println("Thumbnail erzeugen...");
    Merkmal thumbnail = new ThumbnailMerkmal();
    thumbnail.liesMerkmal(bilddatei);  
    System.out.println("Fertig.");   
  }
  
}
