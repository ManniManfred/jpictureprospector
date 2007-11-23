package benutzermanager;

/**
 * Ein Objekt dieser Klasse stellt ein Recht dar. Ein Benutzer hat z.B. das 
 * Recht zu importieren.
 * 
 * @author Manfred Rosskamp
 */
public class Recht {

  /**
   * Gibt den Namen diesen Rechts an. Der name muss im System eindeutig sein.
   */
  private String name;
  
  /** 
   * Beschreibung zu diesem Recht.
   */
  private String beschreibung;
  
  /**
   * Erzeugt ein neues Recht mit dem uebergebenem Namen und einer leeren 
   * Beschreibung.
   * 
   * @param name Eindeutiger Name des Rechts. 
   */
  public Recht(String name) {
    this(name, "");
  }
  
  
  /**
   * Erzeugt ein neues Recht mit dem uebergebenem Namen und Beschreibung.
   * 
   * @param name Eindeutiger Name des Rechts. 
   * @param beschreibung Beschreibung dieses Rechts. 
   */
  public Recht(String name, String beschreibung) {
    this.name = name;
    this.beschreibung = beschreibung;
  }


  /**
   * Gibt den Namen dieses Rechts zurueck.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }


  /**
   * Gibt die Beschreibung dieses Rechts zurueck.
   * 
   * @return the beschreibung
   */
  public String getBeschreibung() {
    return beschreibung;
  }
  
  
  public String toString() {
    return this.getName() + "-Recht (" + getBeschreibung() + ")";
  }
  
}
