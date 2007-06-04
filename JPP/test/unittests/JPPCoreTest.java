package unittests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.AendereException;
import core.BildDokument;
import core.EntferneException;
import core.ErzeugeBildDokumentException;
import core.ImportException;
import core.JPPCore;
import core.SucheException;
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
  private static final String DATEI_LINUX = "THINK_LINUXII.tiff";
  
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
   * Testet die Methode <code>importiere</code> des Kernobjektes.
   * @throws ImportException 
   */
  @Test
  public void testImportiere() throws ImportException {

    /* importiere einige gueltige Dateien */
    Verteilung v = new Verteilung("Zeitverteilung der einzelnen Dateien.");
    long vorher, nachher;
    
    vorher = System.currentTimeMillis();
    core.importiere(new File(PFAD + DATEI_KUCHEN));
    nachher = System.currentTimeMillis();
    v.setze(DATEI_KUCHEN, (int) (nachher - vorher));

    
    vorher = System.currentTimeMillis();
    core.importiere(new File(PFAD + DATEI_LANDSCHAFT));
    nachher = System.currentTimeMillis();
    v.setze(DATEI_LANDSCHAFT, (int) (nachher - vorher));
    

    vorher = System.currentTimeMillis();
    core.importiere(new File(PFAD + DATEI_SPRUNG));
    nachher = System.currentTimeMillis();
    v.setze(DATEI_SPRUNG, (int) (nachher - vorher));
    

    vorher = System.currentTimeMillis();
    core.importiere(new File(PFAD + DATEI_WAND));
    nachher = System.currentTimeMillis();
    v.setze(DATEI_WAND, (int) (nachher - vorher));
    
    
    vorher = System.currentTimeMillis();
    core.importiere(new File(PFAD + DATEI_ZZOLLVEREIN));
    nachher = System.currentTimeMillis();
    v.setze(DATEI_ZZOLLVEREIN, (int) (nachher - vorher));
    
    
    vorher = System.currentTimeMillis();
    core.importiere(new File(PFAD + DATEI_LINUX));
    nachher = System.currentTimeMillis();
    v.setze(DATEI_LINUX, (int) (nachher - vorher));
    
    System.out.println(v);
    
    /* importiere eine Tiff-Datei nur, wenn das 
     * zusaetliche Java Advanced Imaging Image I/O installiert wurde
     */
    String[] bildtypen = ImageIO.getReaderFormatNames();
    
    if (Arrays.asList(bildtypen).contains("tiff")) {
      //core.importiere(new File(PFAD + DATEI_LINUX));
      System.out.println("Es koennen auch tiff-Bilder importiert werden.");
    } else {
      System.out.println("Tiff-Bilder werden nicht unterstuetzt.");
    }
    
    try {
      core.importiere(new File(""));
      fail("Es wurde keine Exception geworfen fuer den Import einer" +
          "ungueltigen Datei");
    } catch (ImportException e) {
      /* Alles ok, die Exception wud richtig geworfen */
    }
    
  }

  /**
   * Testet die Methode <code>suche</code> des Kernobjekts.
   * @throws SucheException 
   */
  @Test
  public void testSuche() throws SucheException {
    
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
   * @throws AendereException 
   * @throws SucheException 
   * @throws ImportException 
   */
  @Test
  public void testAendere() throws AendereException, SucheException, ImportException {

    /* Importiere ein Bild */
    BildDokument dok = core.importiere(new File(PFAD + DATEI_WAND));
    
    /* Aendere dieses Bild, indem einige Keywords hinzugefuegt werden */
    dok.getMerkmal("Schl\u00fcsselw\u00f6rter").setWert("Urlaub 2007 Wild");
    
    core.aendere(dok);
    
//    String anfrage = PFAD + DATEI_WAND;
//    System.out.println("Anfrage= " + anfrage);
//    anfrage = anfrage.replaceAll("\\\\", "\\\\\\\\");
//    System.out.println("Anfrage= " + anfrage);
    
    /* Aenderungen ueberpruefen */
    BildDokument dok2 = core.suche("Urlaub").getBildDokument(0);
    assertEquals(dok, dok2);
    
  }

  /**
   * Testet die Methode <code>entferne</code> des Kernobjekts.
   * @throws ImportException 
   * @throws EntferneException 
   * @throws SucheException 
   */
  @Test
  public void testEntferne() throws ImportException, EntferneException, SucheException {
    
    /* Erstelle Kopie einer Datei, die dann spaeter entfernt wird. */
    File datei = new File(PFAD + DATEI_ZZOLLVEREIN);
    File kopie = new File(PFAD + "zollkopie.jpg");
    try {
      copyFile(datei, kopie, 1024, true);
      
      /* fuege die Kopie der Anwendung hinzu */
      BildDokument doc = core.importiere(kopie);
      
      /* und entferne diese wieder */
      core.entferne(doc, true);
      
      /* ueberpruefe, ob die Datei noch existiert */
      assertEquals(false, kopie.isFile());
      
      /* ueberpruefe, ob das Bild noch in der Anwendung ist. */
      Trefferliste treffer = core.suche(PFAD + "zollkopie.jpg");
      assertEquals(0, treffer.getAnzahlTreffer());
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
  }

  private static void copyFile(File src, File dest, int bufSize,
      boolean force) throws IOException {
  if(dest.exists()) {
      if(force) {
          dest.delete();
      } else {
          throw new IOException(
                  "Cannot overwrite existing file: " + dest.getName());
      }
  }
  byte[] buffer = new byte[bufSize];
  int read = 0;
  InputStream in = null;
  OutputStream out = null;
  try {
      in = new FileInputStream(src);
      out = new FileOutputStream(dest);
      while(true) {
          read = in.read(buffer);
          if (read == -1) {
              //-1 bedeutet EOF
              break;
          }
          out.write(buffer, 0, read);
      }
  } finally {
      // Sicherstellen, dass die Streams auch
      // bei einem throw geschlossen werden.
      // Falls in null ist, ist out auch null!
      if (in != null) {
          //Falls tats�chlich in.close() und out.close()
          //Exceptions werfen, die jenige von 'out' geworfen wird.
          try {
              in.close();
          }
          finally {
              if (out != null) {
                  out.close();
              }
          }
      }
  }
}
}
