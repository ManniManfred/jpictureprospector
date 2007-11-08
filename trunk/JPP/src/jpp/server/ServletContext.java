package jpp.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jpp.core.JPPCore;
import jpp.core.exceptions.ErzeugeException;

public class ServletContext implements ServletContextListener {

  public void contextInitialized(ServletContextEvent event) {
    
    String indexDir = event.getServletContext().getInitParameter("indexDir");
    
    try {
      event.getServletContext().setAttribute("JPPCore", new JPPCore(indexDir));
    } catch (ErzeugeException e) {
      event.getServletContext().setAttribute("JPPCore", null);
      System.out.println("Konnte den JPPCore nicht erzeugen." + e);
      Logger.getLogger("ServletContextListener").log(Level.WARNING,
        "Konnte den JPPCore nicht erzeugen.", e);
    }
  }

  public void contextDestroyed(ServletContextEvent event) {
    // TODO Auto-generated method stub
    
  }
}
