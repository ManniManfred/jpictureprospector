package jpp.client;

import jpp.core.BildDokument;
import jpp.core.Trefferliste;

public class ClientTrefferliste extends Trefferliste {

  public ClientTrefferliste() {
    
  }
  
  /**
   * Setzt die Anzahl aller gefundenen Treffer. Dieser kann von der Anzahl der
   * uebertragenen Treffer unterschiedlich sein, wenn ein maxanzahl gesetzt 
   * wurde.
   * 
   * @param anzahl Anzahl aller gefundenen Treffer
   */
  public void setGesamtAnzahlTreffer(int anzahl) {
    this.anzahlTreffer = anzahl;
  }
  
  /**
   * Fuegt dieser Trefferliste ein BildDokument als Treffer hinzu.
   * @param dok
   */
  public void addBildDokument(BildDokument dok, float score) {
    this.bildDokumente.add(dok);
    this.score.add(score);
  }
}
