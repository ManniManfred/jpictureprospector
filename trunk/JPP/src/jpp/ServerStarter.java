package jpp;

import java.net.URL;

import jpp.settings.ServerSettings;
import jpp.settings.SettingsManager;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Starter-Klasse fuer den Jetty-Server, indem dann der JPP-Webapplikation ausgefuehrt
 * wird.
 */
public class ServerStarter {

  /**
   * Startet den Jetty-Webserver mit der JPP-Webapplikation.
   *
   * @param args
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception {
    
    
    ServerSettings serverSettings = 
      SettingsManager.getSettings(ServerSettings.class);
    
    
    


    
    /* Den Webserver Jetty starten */
    WebAppContext ctx = new WebAppContext("webapp/",
        serverSettings.contextPath);
    
    
    Server server = new Server(serverSettings.port);
    server.setHandler(ctx);
    server.start();
  }

}
