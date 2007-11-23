package unittests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import benutzermanager.Benutzer;
import benutzermanager.BenutzerManager;
import benutzermanager.RechteManager;
import benutzermanager.exceptions.RechtExistiertNichtException;

import com.db4o.foundation.Cool;



import static org.junit.Assert.assertEquals;

public class BenutzerManagerTest {

  BenutzerManager manager;
  
  @Before
  public void setUp() {
    manager = new BenutzerManager();
    manager.oeffne("test.db");
  }
  
  @After
  public void tearDown() {
    manager.schliesse();
  }
  
  @Test
  public void crudUser() {

    /* Create User */
    Benutzer admin = new Benutzer("administrator");
    admin.setPasswort("alloha");
    
    Benutzer anno = new Benutzer("annonym");
    anno.setPasswort("");
    
    admin.addRecht(RechteManager.getRecht("suche"));
    admin.addRecht(RechteManager.getRecht("importiere"));
    admin.addRecht(RechteManager.getRecht("entferne"));
    admin.addRecht(RechteManager.getRecht("aendere"));

    anno.addRecht(RechteManager.getRecht("suche"));
    
    manager.fuegeBenutzerHinzu(admin);
    manager.fuegeBenutzerHinzu(anno);
    
    
    /* Read Users (login) */
    Benutzer adminRead = manager.getBenutzer("administrator", "alloha");
    Benutzer annoRead = manager.getBenutzer("annonym", "");
    
    assertEquals(admin, adminRead);
    assertEquals(anno, annoRead);
    
    
    /* Read all Useres */
    List<Benutzer> l = manager.getAlleBenutzer();
        
    assertEquals(true, l.contains(admin));
    assertEquals(true, l.contains(anno));
    
    /* Change User */
    admin.setEmail("blablub@wdr.de");
    admin.setVorname("Karl");
    
    manager.aendereBenutzer(admin);
    
    adminRead = manager.getBenutzer("administrator", "alloha");
    assertEquals(admin, adminRead);
    
    
    /* Delete Users */
    manager.entferneBenutzer(admin);
    manager.entferneBenutzer(anno);

    adminRead = manager.getBenutzer("administrator", "alloha");
    assertEquals(null, adminRead);
  }
  
}
