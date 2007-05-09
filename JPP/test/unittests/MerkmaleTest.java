package unittests;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import core.BildDokument;

/**
 * Testklasse fuer Methoden der Klasse BildDocument
 *
 * @author Marion Mecking
 */
public class MerkmaleTest extends TestCase {
  
  /** Mehrere Beispiel-Testbilder. */
  private BildDokument bildJPGmitDaten;
  private BildDokument bildJPGgross;
  private BildDokument bildGIF;
  private BildDokument bildBMP;
  private BildDokument bildPNG;
  
  /** Dateinamen der einzelnen Bilder. */
  private static final String nameJPGmitDaten = "zeiche_zollverein.jpg";
  private static final String nameJPGgross = "sprung.jpg";
  private static final String DATEI_WAND = "wand.gif";
  private static final String DATEI_KUCHEN = "kuchen.bmp";
  private static final String DATEI_LANDSCHAFT = "landschaft.png";
  
  /** Pfad zu den Bilddateien. */
  private static final String PFAD = "test\\img\\";
  
  
  /**
   * Erzeugt ein Objekt dieser Klasse mit dem angegebenen Namen.
   *
   * @param name  Name dieses Tests
   */
  public MerkmaleTest(String name) {
    
    super(name);
  }
  
  /**
   * Liefert Suite der Testmethoden dieser Klasse.
   *
   * @return Testmethoden dieser Klasse
   */
  public static Test suite() {
    
    return new TestSuite(MerkmaleTest.class);
  }

  
  /**
   * Test Objekte werden erzeugt (Aufbau der Testdaten).
   */
  protected void setUp() {
    
    
    
    bildJPGmitDaten = 
      BildDokument.erzeugeAusDatei(new File(PFAD + nameJPGmitDaten));
    
    bildJPGgross = BildDokument.erzeugeAusDatei(new File(PFAD + nameJPGgross));
    
    bildGIF = BildDokument.erzeugeAusDatei(new File(PFAD + DATEI_WAND));
    
    bildBMP = BildDokument.erzeugeAusDatei(new File(PFAD + DATEI_KUCHEN));
    
    bildPNG = BildDokument.erzeugeAusDatei(new File(PFAD + DATEI_LANDSCHAFT));
  }
 
  /**
   * Testet die Methode getMerkmal der Klasse BildDocument.
   */
  public void testGetMerkmal() {
    
    /* Teste DATEIGROESSE */
    assertEquals(96, bildJPGmitDaten.getMerkmal(BildDokument.DATEIGROESSE).getWert());
    assertEquals(828, bildJPGgross.getMerkmal(BildDokument.DATEIGROESSE).getWert());
    assertEquals(808, bildGIF.getMerkmal(BildDokument.DATEIGROESSE).getWert());
    assertEquals(697, bildBMP.getMerkmal(BildDokument.DATEIGROESSE).getWert());
    assertEquals(3583, bildPNG.getMerkmal(BildDokument.DATEIGROESSE).getWert());
    assertEquals("Dateigroesse", bildJPGmitDaten.getMerkmal(BildDokument.DATEIGROESSE).getName());
    assertEquals(false, bildJPGmitDaten.getMerkmal(BildDokument.DATEIGROESSE).istEditierbar());
  
    /* Teste BESCHREIBUNG */
    assertEquals("", bildJPGmitDaten.getMerkmal(BildDokument.BESCHREIBUNG));
    assertEquals("", bildJPGgross.getMerkmal(BildDokument.BESCHREIBUNG));
    assertEquals("", bildGIF.getMerkmal(BildDokument.BESCHREIBUNG));
    assertEquals("", bildBMP.getMerkmal(BildDokument.BESCHREIBUNG));
    assertEquals("", bildPNG.getMerkmal(BildDokument.BESCHREIBUNG));
    assertEquals("Beschreibung", bildJPGmitDaten.getMerkmal(BildDokument.BESCHREIBUNG).getName());
    assertEquals(true, bildJPGmitDaten.getMerkmal(BildDokument.BESCHREIBUNG).istEditierbar());
    
    /* Teste SCHLUESSELWOERTER */
    assertEquals("Kokerei, Stahl, Weltkulturerbe", bildJPGmitDaten.getMerkmal(BildDokument.SCHLUESSELWOERTER));
    assertEquals(828, bildJPGgross.getMerkmal(BildDokument.SCHLUESSELWOERTER));
    assertEquals(808, bildGIF.getMerkmal(BildDokument.SCHLUESSELWOERTER));
    assertEquals("Schluesselwoerter", bildJPGmitDaten.getMerkmal(BildDokument.SCHLUESSELWOERTER).getName());
    assertEquals(true, bildJPGmitDaten.getMerkmal(BildDokument.SCHLUESSELWOERTER).istEditierbar());
     
    /* Teste BILDHOEHE */
    assertEquals(427, bildJPGmitDaten.getMerkmal(BildDokument.BILDHOEHE));
    assertEquals(750, bildJPGgross.getMerkmal(BildDokument.BILDHOEHE));
    assertEquals(1100, bildGIF.getMerkmal(BildDokument.BILDHOEHE));
    assertEquals(544, bildBMP.getMerkmal(BildDokument.BESCHREIBUNG));
    assertEquals(2022, bildPNG.getMerkmal(BildDokument.BESCHREIBUNG));
    assertEquals("Bildhoehe", bildJPGmitDaten.getMerkmal(BildDokument.BILDHOEHE).getName());
    assertEquals(false, bildJPGmitDaten.getMerkmal(BildDokument.BILDHOEHE).istEditierbar());
  
    /* Teste BILDBREITE */
    assertEquals(640, bildJPGmitDaten.getMerkmal(BildDokument.BILDBREITE));
    assertEquals(1000, bildJPGgross.getMerkmal(BildDokument.BILDBREITE));
    assertEquals(1650, bildGIF.getMerkmal(BildDokument.BILDBREITE));
    assertEquals(437, bildBMP.getMerkmal(BildDokument.BILDBREITE));
    assertEquals(1400, bildPNG.getMerkmal(BildDokument.BILDBREITE));
    assertEquals("Bildbreite", bildJPGmitDaten.getMerkmal(BildDokument.BILDBREITE).getName());
    assertEquals(false, bildJPGmitDaten.getMerkmal(BildDokument.BILDBREITE).istEditierbar());
    
    /* Teste BILDTYP */        
    assertEquals("jpg", bildJPGmitDaten.getMerkmal(BildDokument.BILDTYP));
    assertEquals("jpg", bildJPGgross.getMerkmal(BildDokument.BILDTYP));
    assertEquals("gif", bildGIF.getMerkmal(BildDokument.BILDTYP));
    assertEquals("bmp", bildBMP.getMerkmal(BildDokument.BILDTYP));
    assertEquals("png", bildPNG.getMerkmal(BildDokument.BILDTYP));
    assertEquals("Bildtyp", bildJPGmitDaten.getMerkmal(BildDokument.BILDTYP).getName());
    assertEquals(false, bildJPGmitDaten.getMerkmal(BildDokument.BILDTYP).istEditierbar()); 
    
  }
  
  /**
   * Testet die Methode getFormat der Klasse Geoeffnetes Bild
   */
  public void testGeFormat() {
    
    try {
      assertEquals("jpg", bildJPGmitDaten.getGeoeffnetesBild().getFormat());
      assertEquals("jpg", bildJPGgross.getGeoeffnetesBild().getFormat());
      assertEquals("gif", bildGIF.getGeoeffnetesBild().getFormat());
      assertEquals("bmp", bildBMP.getGeoeffnetesBild().getFormat());
      assertEquals("png", bildPNG.getGeoeffnetesBild().getFormat());
    } catch (IOException e) {
      e.getMessage();
    }
          
  }  
    
    /**
     * Startet den Test.
     *
     * @param args  wird nicht verwendet
     */
    public static void main(String[] args) {
      
      /* textueller TestRunner */
      junit.textui.TestRunner.run(suite());
      
    }
    
  }