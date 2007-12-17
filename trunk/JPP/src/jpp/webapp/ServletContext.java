package jpp.webapp;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import benutzermanager.Benutzer;
import benutzermanager.BenutzerManager;
import benutzermanager.RechteManager;

import jpp.settings.SettingsManager;
import jpp.settings.ServerSettings;
import jpp.core.JPPCore;
import jpp.core.exceptions.ErzeugeException;

public class ServletContext implements ServletContextListener {

  private BenutzerManager manager;
  
  /**
   * Enthaelt das ServerSettings Objekt mit allen wichtigen serverSettings 
   * dieser Anwendung.
   */
  private static ServerSettings serverSettings = 
    SettingsManager.getSettings(ServerSettings.class);
  
  
  public void contextInitialized(ServletContextEvent event) {
    
    /* JPPCore initialisieren */
    String indexDir = serverSettings.indexDir;
    System.out.println("IndexDir = " + indexDir);
    try {
      event.getServletContext().setAttribute("JPPCore", new JPPCore(indexDir));
    } catch (ErzeugeException e) {
      event.getServletContext().setAttribute("JPPCore", null);
      System.out.println("Konnte den JPPCore nicht erzeugen." + e);
      Logger.getLogger("ServletContextListener").log(Level.WARNING,
        "Konnte den JPPCore nicht erzeugen.", e);
    }
    
    /* BenutzerManager initialisieren */
    manager = new BenutzerManager();
    manager.oeffne("user.yap");
    
    if (!manager.hatBenutzer("admin")) {
      Benutzer admin = new Benutzer("admin");
      admin.setPasswort("allohaa");
      
      
      admin.addRecht(RechteManager.getRecht("suche"));
      admin.addRecht(RechteManager.getRecht("importiere"));
      admin.addRecht(RechteManager.getRecht("entferne"));
      admin.addRecht(RechteManager.getRecht("aendere"));

      manager.fuegeBenutzerHinzu(admin);
    }
    
    event.getServletContext().setAttribute("BenutzerManager", manager);
  }

  
  public void contextDestroyed(ServletContextEvent event) {
    if (manager == null) {
      System.out.println("Fehler");
    } else {
      manager.schliesse();
    }
  }
}
