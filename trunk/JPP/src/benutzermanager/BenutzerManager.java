package benutzermanager;


import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class BenutzerManager {

  /** Enthaelt die Datenbank */
  private ObjectContainer db; 
  
  private String dbDatei;
  
  /**
   * Erzeugt einen neuen BenutzerManager.
   */
  public BenutzerManager() {
  }
  
  public void oeffne(String datenbankdatei) {
    this.dbDatei = datenbankdatei;
  }
  
  public void schliesse() {
    if (db != null) {
      db.close();
    }
  }
  
  
  /**
   * Gibt den Benutzer mit dem loginname und passwort zurueck. Falls diese 
   * Kombination nicht existiert, wird null zurueckgegeben.
   * 
   * @param loginname Loginname des gesuchten Benutzers
   * @param passwort Passwort des gesuchten Benutzers
   * @return den Benutzer mit dem Loginname und Passwort, oder null, falls kein
   *    Benutzer mit der Name-Passwort-Kombination existiert.
   */
  public Benutzer getBenutzer(String loginname, String passwort) {
    if (db == null) {
      db = Db4o.openFile(dbDatei);
    }
    
    if (loginname == null) {
      return null;
    }
    
    Benutzer b = new Benutzer(loginname);
    b.setPasswort(passwort);
    ObjectSet<Benutzer> benutzer = db.get(b);
    
    if (benutzer.hasNext()) {
      return benutzer.next();
    }
    return null;
  }
  
  
  /**
   * Fuegt diesem Manager einen neuen Benutzer hinzu.
   * 
   * @param benutzer
   */
  public void fuegeBenutzerHinzu(Benutzer benutzer) {
    if (db == null) {
      db = Db4o.openFile(dbDatei);
    }
    
    /* Wenn es den Benutzer mit dem Loginname schon gibt eine Excpetion werfen 
     */
    Benutzer b = new Benutzer(benutzer.getLoginName());
    
    if (db.get(b).size() > 0 ) {
      throw new IllegalArgumentException("Der Benutzer existiert bereits.");
    }
    
    /* Angaben zu dem Benutzer pruefen:
     * - der Loginname muss mindestens aus zwei Zeichen bestehen. 
     */
    if (benutzer.getLoginName().length() <= 2) {
      throw new IllegalArgumentException("Der Loginname muss mindestens aus " +
      		"zwei Zeichen bestehen.");
    }
    
    /* TODO ... weitere Vorraussetzungen fuer Benutzer pruefen.
     */
    
    db.set(benutzer);
  }
  
  /**
   * Aendert die Benutzer Attribute in der Datenbank.
   * 
   * @param benutzer Benutzer, der geaendert wird
   */
  public void aendereBenutzer(Benutzer benutzer) {
    if (db == null) {
      db = Db4o.openFile(dbDatei);
    }
    
    ObjectSet<Benutzer> list = db.get(new Benutzer(benutzer.getLoginName()));
    
    if (list.size() > 0 && list.next() == benutzer ) {
      db.set(benutzer);
    } else {
      throw new IllegalArgumentException("Der uebergebene Benutzer existiert " +
      		"nicht");
    }
    
  }
  
  /**
   * Entfernt den uebergeben Benutzer.
   * 
   * @param benutzer Benutzer, der entfernt wird
   */
  public void entferneBenutzer(Benutzer benutzer) {
    if (db == null) {
      db = Db4o.openFile(dbDatei);
    }
    
    db.delete(benutzer);
  }
  
  
  /**
   * Gibt alle verfuegbaren Benutzer zurueck.
   * 
   * @return alle verfuegbaren Benutzer
   */
  public List<Benutzer> getAlleBenutzer() {
    if (db == null) {
      db = Db4o.openFile(dbDatei);
    }
    
    List<Benutzer> ergebnis = new ArrayList<Benutzer>();
    
    /* Wandle ObjectSet in HashSet um */
    ObjectSet<Benutzer> set = db.get(Benutzer.class);
    
    while (set.hasNext()) {
      ergebnis.add(set.next());
    }
    
    return ergebnis;
  }
  
  
}
