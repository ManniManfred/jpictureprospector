package benutzermanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dieser Manager verwaltet alle verfügbaren Rechte dieser Anwendung.
 * 
 * @author Manfred Rosskamp
 */
public class RechteManager {

  private static Map<String, Recht> rechte = new HashMap<String, Recht>();

  static {
    
    /* Alle Rechte initialisieren */
    List<Recht> rechtListe = new ArrayList<Recht>();
    rechtListe.add(new Recht("suche", "Dieses Recht erlaubt die Such im Index."));
    rechtListe.add(new Recht("importiere",
        "Dieses Recht erlaubt Bilder zu importieren im Index."));
    rechtListe.add(new Recht("aendere",
        "Dieses Recht erlaubt BildDokumente zu veraendern."));
    rechtListe.add(new Recht("entferne",
        "Dieses Recht erlaubt Bilder wieder zu entfernen."));
    rechtListe.add(new Recht("clearUpIndex",
        "Dieses Recht erlaubt den Index zu saeubern."));
    
    rechtListe.add(new Recht("Benutzerverwaltung",
        "Dieser Recht erlaubt die Verwaltung der Benutzer und ihrer Rechte."));
    
    for (Recht r : rechtListe) {
      rechte.put(r.getName(), r);
    }
  }

  public static Collection<Recht> getAlleRechte() {
    return rechte.values();
  }

  /**
   * Gibt das entsprechend zu dem Namen gehoerende Rechte Objekt zurueck.
   * 
   * @param name Name des Rechts
   * @return das entsprechend zu dem Namen gehoerende Rechte Objekt
   */
  public static Recht getRecht(String name) {
    return rechte.get(name);
  }
  
  /**
   * Ueberprueft, ob es das entsprechende Recht in diesem Manager gibt.
   * 
   * @param r Recht das ueberprueft wird.
   * @return true, wenn das Recht definiert ist
   */
  public static boolean existiert(Recht r) {
    return rechte.containsKey(r.getName());
  }
}
