package jpp.server;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jpp.core.JPPCore;
import jpp.core.Trefferliste;
import jpp.core.exceptions.SucheException;

public class JPPCoreServlet extends HttpServlet {

  private static final long serialVersionUID = 7017607629370760883L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    handleRequest(req, resp);
    
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    handleRequest(req, resp);
  }
  
  private void handleRequest(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {

    
    PrintStream out = new PrintStream(resp.getOutputStream());
    
    JPPCore kern = (JPPCore) getServletContext().getAttribute("JPPCore");
    
    if (kern == null) {
      out.println("JPPCore ist nicht vorhanden. Es ist vermutlich beim start" +
      		"ein Fehler aufgetreten. Überprüfen Sie die Logfiles.");
    }
    
    try {
      String suchtext = req.getParameter("suchtext");
      int offset = Integer.parseInt(req.getParameter("offset"));
      int maxAnzahl = Integer.parseInt(req.getParameter("maxanzahl"));

      try {
        Trefferliste liste = kern.suche(suchtext, offset, maxAnzahl);

        resp.setContentType("text/xml");
        out.println("<?xml version=\"1.0\" ?>");
        out.println("<?xml-stylesheet type=\"text/xsl\" href=\"trefferDok.xsl\" ?>");
        out.println(liste.toXml());
      } catch (SucheException e) {
        out.println("Fehler beim suchen: " + e.getCause());
      }
    } catch(Exception e) {
      out.println("Anfrage Parameter nicht korrekt.");
    }
      
      
    
    
  }
}
