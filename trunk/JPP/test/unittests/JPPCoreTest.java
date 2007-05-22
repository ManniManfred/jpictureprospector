package unittests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.BildDokument;
import core.ErzeugeBildDokumentException;
import core.ImportException;
import core.JPPCore;
import core.Trefferliste;

/**
 * Ein Objekt der Klasse testet Objekte der Klasse <code>JPPCore</code> auf
 * ihre Funktionalitaet.
 * 
 * @author Nils Verheyen
 */
public class JPPCoreTest {
  
  /** Enthalten die Dateien fuer die Tests. */
  private static final String DATEI_KUCHEN = "kuchen.bmp";
  private static final String DATEI_LANDSCHAFT = "landschaft.png";
  private static final String DATEI_SPRUNG = "sprung.jpg";
  private static final String DATEI_WAND = "wand.gif";
  private static final String DATEI_ZZOLLVEREIN = "zeche_zollverein.jpg";
  private static final String DATEI_LINUX = "zeche_zollverein.jpg";//"THINK_LINUXII.tiff";
  
  /** Pfad zu den Bilddateien. */
  private static final String PFAD = "test" + File.separator + "img" 
                                        + File.separator;

  /** Enthaelt den zu testenden Programmkern. */
  private JPPCore core;
  
  /**
   * Erzeugt die Objekte, die fuer den test benoetigt werden.
   * 
   * @throws Exception  wirft eine Exception, wenn die Testdaten nicht
   *         aufgebaut werden konnten
   */
  @Before
  public void setUp() throws Exception {
    
    core = new JPPCore();
    importiereDateien();
  }

  /**
   * Zerstoert alle Testdaten, die im Test verwendet wurden.
   * 
   * @throws Exception  wirft eine Exception, wenn die Testdaten nicht
   *         abgebaut werden konnten
   */
  @After
  public void tearDown() throws Exception {
    
    core = null;
  }
  
  /**
   * Imporiert eine Anzahl an Dateien in den Kern des Programms.
   */
  private void importiereDateien() throws ImportException {
    
    //try {
      
      core.importiere(new File(PFAD + DATEI_KUCHEN));
      core.importiere(new File(PFAD + DATEI_LANDSCHAFT));
      core.importiere(new File(PFAD + DATEI_SPRUNG));
      core.importiere(new File(PFAD + DATEI_WAND));
      core.importiere(new File(PFAD + DATEI_ZZOLLVEREIN));
      core.importiere(new File(PFAD + DATEI_LINUX));
      
//    } catch (ImportException e) {
//      //System.out.println(e);
//      e.printStackTrace();
//    }
  }
  
  /**
   * Testet die Methode <code>importiere</code> des Kernobjektes.
   */
  @Test
  public void testImportiere() {
    
    try {
      
      core.importiere(new File(""));
      fail("Es wurde keine Exception geworfen fuer den Import einer" +
          "ungueltigen Datei");
    } catch (ImportException e) {
      System.out.println(e);
    }
  }

  /**
   * Testet die Methode <code>suche</code> des Kernobjekts.
   */
  @Test
  public void testSuche() {
    
    Trefferliste trefferliste;
    trefferliste = core.suche("437");
    int anzahlTreffer = trefferliste.getAnzahlTreffer();
    System.out.println("Es wurden " + anzahlTreffer + " gefunden.");
    if (anzahlTreffer < 1) {
      fail("Es wurde kein Treffer gefunden zu \"kuchen*\"");
    } else {
      try {
        assertEquals(BildDokument.erzeugeAusDatei(new File(PFAD + DATEI_KUCHEN)),
            trefferliste.getBildDokument(0));
      } catch (ErzeugeBildDokumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    trefferliste = core.suche("rubbeldiekatz");
    assertEquals(0, trefferliste.getAnzahlTreffer());
    assertEquals(null, trefferliste.getBildDokument(0));
    
    trefferliste = core.suche("");
    assertEquals(0, trefferliste.getAnzahlTreffer());
    assertEquals(null, trefferliste.getBildDokument(0));
  }

  /**
   * Testet die Methode <code>aendere</code> des Kernobjekts.
   */
  @Test
  public void testAendere() {

    /* Muss noch vernuenftig implementiert werden, da Aenderungen ueber
     * die GUI getaetigt werden */
    BildDokument dok;
    try {
      dok = BildDokument.erzeugeAusDatei(new File(PFAD + DATEI_LINUX));
      core.aendere(dok);
    } catch (ErzeugeBildDokumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  /**
   * Testet die Methode <code>entferne</code> des Kernobjekts.
   */
  @Test
  public void testEntferne() {

    BildDokument dok;
    try {
      dok = BildDokument.erzeugeAusDatei(new File(PFAD + DATEI_LANDSCHAFT));

      Trefferliste trefferliste;
      
      core.entferne(dok, false);
      
      // Wird leider unfreiwillig mitgetestet
      trefferliste = core.suche("landschaft");
      
      assertEquals(0, trefferliste.getAnzahlTreffer());
      
      try {
        core.importiere(new File(PFAD + DATEI_LANDSCHAFT));
        core.entferne(dok, true);
        core.importiere(new File(PFAD + DATEI_LANDSCHAFT));
        fail("Keine Exception geworfen fuer den Import einer ungueltigen Datei.");
      } catch (ImportException e) {
        System.out.println(e);
      }
    } catch (ErzeugeBildDokumentException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    
  }

}
