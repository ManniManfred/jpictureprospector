package jpp.ui;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import jpp.core.BildDokument;
import jpp.merkmale.DateipfadMerkmal;
import jpp.ui.listener.VorschauBildListener;


/**
 * Ein Objekt der Klasse stellt einen Thread dar, der ein Bild
 * aus einem Bilddokument laedt.
 */
public class Vorschaubildlader extends Thread {

  /** Enthaelt das Bilddokument aus dem das Bild geladen werden soll. */
  private BildDokument dok = null;
  
  private List<VorschauBildListener> listener = null;
  
  /** Enthaelt das fertig geladene Bild. */
  private Image bild = null;
  
  /**
   * Erzeugt ein neues Objekt der Klasse
   * @param dok  das zu ladende <code>BildDokument</code>
   */
  public Vorschaubildlader(BildDokument dok) {
    
    this.dok = dok;
    this.listener = new ArrayList<VorschauBildListener>();
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
   * Startet diesen Thread.
   */
  @Override
  public void run() {
    
    String dateipfad = (String) dok.getMerkmal(DateipfadMerkmal.FELDNAME).getWert();
    try {
      
      bild = ImageIO.read(new File(dateipfad));
      fireBildGeladen();
    } catch (IOException e) {
      System.out.println("Das Bild konnte nicht geladen werden.\n" +
          e.getMessage());
    }
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