package unittests;

import static org.junit.Assert.assertEquals;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import jpp.core.BildDokument;
import jpp.core.JPPCore;
import jpp.core.exceptions.ImportException;
import jpp.merkmale.BildbreiteMerkmal;
import jpp.merkmale.BildhoeheMerkmal;
import jpp.merkmale.BildtypMerkmal;
import jpp.merkmale.DateigroesseMerkmal;


/**
 * Testklasse fuer die verschiedenen Merkmale
 * 
 * @author Marion Mecking
 * @author Manfred Rosskamp
 */
public class MerkmaleTest {


  /** Dateinamen der einzelnen Bilder. */
  private static final String nameJPGmitDaten = "zeche_zollverein.jpg";

  private static final String nameJPGgross = "sprung.jpg";

  private static final String DATEI_WAND = "wand.gif";

  private static final String DATEI_KUCHEN = "kuchen.bmp";

  private static final String DATEI_LANDSCHAFT = "landschaft.png";

  
  /** Pfad zu den Bilddateien. */
  private static final String PFAD = "test" + File.separator + "img"
      + File.separator;
  

  /** Core-Object, welches das Bild importiert. */
  private JPPCore core;

  /** BildDokumente an denen die Merkmalswerte ueberprueft werden */
  private BildDokument bildJPGmitDaten;
  private BildDokument bildJPGgross;
  private BildDokument bildGIF;
  private BildDokument bildBMP;
  private BildDokument bildPNG;
  
  
  /**
   * Test Objekte werden erzeugt (Aufbau der Testdaten).
   * 
   */
  @Before
  public void setUp() throws Exception {
    System.out.println("setUp gestartet");
    core = new JPPCore();

    try {
      bildJPGmitDaten = core.importiere(new File(PFAD
          + nameJPGmitDaten));
  
      bildJPGgross = core.importiere(new File(PFAD + nameJPGgross));
  
      bildGIF = core.importiere(new File(PFAD + DATEI_WAND));
  
      bildBMP = core.importiere(new File(PFAD + DATEI_KUCHEN));
  
      bildPNG = core.importiere(new File(PFAD + DATEI_LANDSCHAFT));
    } catch(ImportException e) {
      e.printStackTrace();
    }
    System.out.println("setUp beendet");
  }
  
  
  /**
   * Zerstoert alle Testdaten, die im Test verwendet wurden.
   * 
   * @throws Exception  wirft eine Exception, wenn die Testdaten nicht
   *         abgebaut werden konnten
   */
  @After
  public void tearDown() throws Exception {
    core.entferne(bildJPGmitDaten, false);
    core.entferne(bildJPGgross, false);
    core.entferne(bildGIF, false);
    core.entferne(bildBMP, false);
    core.entferne(bildPNG, false);
    
    core = null;
  }
  
  
  /**
   * Testet die Methode getMerkmal der Klasse BildDocument.
   * @throws ImportException 
   */
  @Test
  public void testDateigroesseMerkmal() {


    /* Teste DATEIGROESSE */
    assertEquals(96, bildJPGmitDaten.getMerkmal(DateigroesseMerkmal.FELDNAME)
        .getWert());
    assertEquals(828, bildJPGgross.getMerkmal(DateigroesseMerkmal.FELDNAME)
        .getWert());
    assertEquals(808, bildGIF.getMerkmal(DateigroesseMerkmal.FELDNAME).getWert());
    assertEquals(697, bildBMP.getMerkmal(DateigroesseMerkmal.FELDNAME).getWert());
    assertEquals(3583, bildPNG.getMerkmal(DateigroesseMerkmal.FELDNAME).getWert());
    assertEquals(false, bildJPGmitDaten.getMerkmal(DateigroesseMerkmal.FELDNAME)
        .istEditierbar());
  }
  
//  @Test
//  public void testBeschreibungMerkmal() {
//    /* Teste BESCHREIBUNG */
//    assertEquals("", bildJPGmitDaten.getMerkmal("Beschreibung"));
//    assertEquals("", bildJPGgross.getMerkmal("Beschreibung"));
//    assertEquals("", bildGIF.getMerkmal("Beschreibung"));
//    assertEquals("", bildBMP.getMerkmal("Beschreibung"));
//    assertEquals("", bildPNG.getMerkmal("Beschreibung"));
//    assertEquals(true, bildJPGmitDaten.getMerkmal("Beschreibung")
//        .istEditierbar());
//  }
//  
//  @Test
//  public void testSchluesselwoerterMerkmal() {
//    /* Teste SCHLUESSELWOERTER */
//    assertEquals("Kokerei, Stahl, Weltkulturerbe", bildJPGmitDaten
//        .getMerkmal("Schl\u00fcsselw\u00f6rter"));
//    assertEquals(828, bildJPGgross.getMerkmal("Schl\u00fcsselw\u00f6rter"));
//    assertEquals(808, bildGIF.getMerkmal("Schl\u00fcsselw\u00f6rter"));
//    assertEquals(true, bildJPGmitDaten.getMerkmal(
//        "Schl\u00fcsselw\u00f6rter").istEditierbar());
//
//  }
  
  @Test
  public void testBildhoeheMerkmal() {
    
    /* Teste BILDHOEHE */
    assertEquals(427, bildJPGmitDaten.getMerkmal(BildhoeheMerkmal.FELDNAME).getWert());
    assertEquals(750, bildJPGgross.getMerkmal(BildhoeheMerkmal.FELDNAME).getWert());
    assertEquals(1100, bildGIF.getMerkmal(BildhoeheMerkmal.FELDNAME).getWert());
    assertEquals(544, bildBMP.getMerkmal(BildhoeheMerkmal.FELDNAME).getWert());
    assertEquals(2022, bildPNG.getMerkmal(BildhoeheMerkmal.FELDNAME).getWert());
    assertEquals(false, bildJPGmitDaten.getMerkmal(BildhoeheMerkmal.FELDNAME)
        .istEditierbar());
  }

  @Test
  public void testBildbreiteMerkmal() {

    /* Teste BILDBREITE */
    assertEquals(640, bildJPGmitDaten.getMerkmal(BildbreiteMerkmal.FELDNAME).getWert());
    assertEquals(1000, bildJPGgross.getMerkmal(BildbreiteMerkmal.FELDNAME).getWert());
    assertEquals(1650, bildGIF.getMerkmal(BildbreiteMerkmal.FELDNAME).getWert());
    assertEquals(437, bildBMP.getMerkmal(BildbreiteMerkmal.FELDNAME).getWert());
    assertEquals(1400, bildPNG.getMerkmal(BildbreiteMerkmal.FELDNAME).getWert());
    assertEquals(false, bildJPGmitDaten.getMerkmal(BildbreiteMerkmal.FELDNAME)
        .istEditierbar());
  }

  @Test
  public void testBildtypMerkmal() {

    /* Teste BILDTYP */
    assertEquals("jpg", bildJPGmitDaten.getMerkmal(BildtypMerkmal.FELDNAME).getWert());
    assertEquals("jpg", bildJPGgross.getMerkmal(BildtypMerkmal.FELDNAME).getWert());
    assertEquals("gif", bildGIF.getMerkmal(BildtypMerkmal.FELDNAME).getWert());
    assertEquals("bmp", bildBMP.getMerkmal(BildtypMerkmal.FELDNAME).getWert());
    assertEquals("png", bildPNG.getMerkmal(BildtypMerkmal.FELDNAME).getWert());
    assertEquals(false, bildJPGmitDaten.getMerkmal(BildtypMerkmal.FELDNAME)
        .istEditierbar());

  }

}