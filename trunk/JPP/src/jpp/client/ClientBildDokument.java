package jpp.client;

import jpp.core.BildDokument;
import jpp.merkmale.Merkmal;

public class ClientBildDokument extends BildDokument {
  
  public ClientBildDokument() {
    
  }
  
  /**
   * Fuegt das uebergebene Merkmal diesem BildDokument hinzu.
   * 
   * @param merkmal
   *          Merkmal, welches diesem BildDokument hinzugefuegt wird
   */
  public void addMerkmal(Merkmal m) {
    super.addMerkmal(m);
  }
}
