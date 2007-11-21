package jpp.ui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.State;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import jpp.core.BildDokument;
import jpp.core.GeoeffnetesBild;
import jpp.core.exceptions.GeneriereException;
import jpp.core.thumbnail.SimpleThumbnailGeneriererFactory;
import jpp.core.thumbnail.ThumbnailGenerierer;
import jpp.merkmale.DateipfadMerkmal;
import jpp.ui.listener.VorschauBildListener;


/**
 * Ein Objekt der Klasse stellt einen Thread dar, der ein Bild
 * aus einem Bilddokument laedt.
 */
public class Vorschaubildlader implements Runnable {

  
  /** 
   * Liste mit allen Listenern, die informiert werden moechten,
   * wenn das Bild geladen ist.
   */
  private List<VorschauBildListener> listener;
  
  /** Enthaelt das fertig geladene Bild. */
  private Image bild;
  
  /** 
   * Wenn ein Bild geladen wurde, wird darin abgespeichert, um beim
   * naechsten mal sofort zur Verfuegung zu stehen.
   */
  private Map<String, Image> cache;
  

  /** Enthaelt den Pfad zu dem zu ladenen Bild. */
  private URL url;
  
  private int maxBreite;
  
  private int maxHoehe;
  
  private ExecutorService ausfuehrer;
  
  /**
   * Erzeugt ein neues Objekt der Klasse
   * @param dok  das zu ladende <code>BildDokument</code>
   */
  public Vorschaubildlader() {
    this.listener = new ArrayList<VorschauBildListener>();
    this.cache = new HashMap<String, Image>();

    ausfuehrer = Executors.newSingleThreadExecutor();
  }
  


  /**
   * Liefert das Bild dieses Objekts.
   * 
   * @return <code>Image</code> wenn das Bild geladen wurde ansonsten
   *         <code>null</code>
   */
  public Image getBild() {
    return this.bild;
  }
  
  /**
   * Es wird das Bild gesetzt, welches geladen werden soll.
   * Danach sollte start() aufgerufen werden, um das Laden tatsaechlich
   * zu starten.
   * 
   * @param dok
   */
  public void startLadeBild(URL url, int maxBreite, int maxHoehe) {
    
    /* key stellt den Schluessel dar, unter dem das Vorschaubild im Cache
     * abgelegt wird oder wurde.
     */
    String key = url.toString() + maxBreite + maxHoehe;
    
    this.url = url;
    this.maxBreite = maxBreite;
    this.maxHoehe = maxHoehe;
    
    Image bildCache = cache.get(key);
    
    if (bildCache != null) {
      this.bild = bildCache;
      fireBildGeladen();
      System.out.println("Cache Treffer.");
    } else {
      ausfuehrer.execute(this);
    }
  }
  
  
  public void run() {
    try {
      
//      this.bild = erzeugeVorschaubild(dateipfad, maxBreite, maxHoehe);
      this.bild = new GeoeffnetesBild(url).getBild();
      
      /* TODO sorge dafuer, dass der Cache auch wieder geloescht wird, 
       * da es ansonsten irgendwann zu einem OutOfMemoryError kommt.
       */
      //cache.put(key, this.bild);

      fireBildGeladen();
    } catch (IOException e) {
      System.out.println("Das URL-Bild konnte nicht geladen werden.\n" +
          e.getMessage());
    }
//    catch (GeneriereException e) {
//      System.out.println("Das Bild konnte nicht geladen werden.\n" +
//          e.getMessage());
//    }
  }
  
  private BufferedImage erzeugeVorschaubild(String pfad, int maxBreite, 
      int maxHoehe) throws GeneriereException, IOException {
    
    GeoeffnetesBild offenesBild = new GeoeffnetesBild(new File(pfad).toURL());
    ThumbnailGenerierer generierer = 
      SimpleThumbnailGeneriererFactory.erzeugeThumbnailGenerierer(
          offenesBild);
    
    return generierer.generiereThumbnail(offenesBild, maxBreite, maxHoehe);
  }
  
  
  /**
   * Wird aufgerufen, wenn das Vorschaubild fertig geladen wurde und
   * benachrichtigt alle Listener.
   */
  private void fireBildGeladen() {
    for (VorschauBildListener l : listener) {
      l.bildGeladen();
    }
  }
  
  /**
   * Fuegt einen Listener der Liste an bereits vorhandenen
   * Listenern hinzu
   * 
   * @param l  der hinzuzufuegende Listener
   */
  public void addVorschaubildListener(VorschauBildListener l) {
    this.listener.add(l);
  }
  
  /**
   * Loescht einen Listener aus der Liste an Listenern.
   * 
   * @param l  der zu loeschende Listener
   */
  public void removeVorschaubildListener(VorschauBildListener l) {
    this.listener.remove(l);
  }
}