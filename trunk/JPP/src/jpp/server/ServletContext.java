package jpp.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import benutzermanager.Benutzer;
import benutzermanager.BenutzerManager;
import benutzermanager.RechteManager;

import jpp.core.JPPCore;
import jpp.core.exceptions.ErzeugeException;

public class ServletContext implements ServletContextListener {

  public void contextInitialized(ServletContextEvent event) {
    
    /* JPPCore initialisieren */
    String indexDir = event.getServletContext().getInitParameter("indexDir");
    
    try {
      event.getServletContext().setAttribute("JPPCore", new JPPCore(indexDir));
    } catch (ErzeugeException e) {
      event.getServletContext().setAttribute("JPPCore", null);
      System.out.println("Konnte den JPPCore nicht erzeugen." + e);
      Logger.getLogger("ServletContextListener").log(Level.WARNING,
        "Konnte den JPPCore nicht erzeugen.", e);
    }
    
    /* BenutzerManager initialisieren */
    BenutzerManager manager = new BenutzerManager();
    manager.oeffne("/tmp/webtmp/user.db");
    
    Benutzer admin = new Benutzer("admin");
    admin.setPasswort("allohaa");
    
    
    admin.addRecht(RechteManager.getRecht("suche"));
    admin.addRecht(RechteManager.getRecht("importiere"));
    admin.addRecht(RechteManager.getRecht("entferne"));
    admin.addRecht(RechteManager.getRecht("aendere"));

    //manager.fuegeBenutzerHinzu(admin);
    
    event.getServletContext().setAttribute("BenutzerManager", manager);
  }

  public void contextDestroyed(ServletContextEvent event) {
    BenutzerManager manager = (BenutzerManager) 
      event.getServletContext().getAttribute("BenutzerManager");
    manager.schliesse();
  }
}
