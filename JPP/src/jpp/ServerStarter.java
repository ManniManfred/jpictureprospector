package jpp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
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
    
    WebAppContext ctx = new WebAppContext("jpp.war", "/jpp");
    Server server = new Server(8080);
    server.setHandler(ctx);
    server.start();
  }

}
