package benutzermanager;


import benutzermanager.exceptions.RechtExistiertNichtException;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class dbfoTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // Öffne eine Datenbank
    ObjectContainer db = Db4o.openFile("beispiel.db");
    try {
      
      Benutzer admin = new Benutzer("admin");
      Benutzer anno = new Benutzer("anno");
      
      admin.addRecht(RechteManager.getRecht("suche"));
      admin.addRecht(new Recht("importiere"));
      admin.addRecht(new Recht("entferne"));
      admin.addRecht(new Recht("aendere"));

      anno.addRecht(new Recht("suche"));
      
      db.set(admin);
      db.set(anno);
    
      // Iteriere über alle Benutzeren
      ObjectSet<Benutzer> result = db.get(Benutzer.class);
      while (result.hasNext()) {
        Benutzer b = result.next();
        System.out.println(b);
        
        
        db.delete(b);
      }

//      // Verändere eine Benutzer
//      result = db.get(new Benutzer("Clara Himmel"));
//      Benutzer found = (Benutzer) result.next();
//      found.setEmail("clara@himmel.de");
//      db.set(found);
      
//      // Lösche eine Benutzer
//      db.delete(found);
      
      
      // Schreibe die Änderungen fest
      db.commit();
    } catch (RechtExistiertNichtException e) {
      System.out.println(e);
    }
    finally {
       // Schließe die Datenbank
       db.close();
    }
  }

}
