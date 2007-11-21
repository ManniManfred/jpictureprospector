
package jpp.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jpp.core.JPPCore;
import jpp.core.exceptions.EntferneException;
import jpp.server.Mapping;

public class EntferneServlet extends HttpServlet {

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


    PrintWriter out = resp.getWriter(); 
    
    JPPCore kern = (JPPCore) getServletContext().getAttribute("JPPCore");
    
    if (kern == null) {
      out.println("JPPCore ist nicht vorhanden. Es ist vermutlich beim start" +
          "ein Fehler aufgetreten. Überprüfen Sie die Logfiles.");
    }
    
    
    String bildurl = req.getParameter("bild");
    if (bildurl == null) {
      out.println("Bitte geben Sie ein Bild an, welches entfernt werden soll.");
    } else {
      
      String vonFestplatte = req.getParameter("vonPlatte");
      boolean auchVonPlatte;
      if (vonFestplatte == null) {
        auchVonPlatte = false;
      } else {
        auchVonPlatte = vonFestplatte.equals("true");
      }
      
      try {
        kern.entferne(Mapping.wandleInLokal(new URL(bildurl)), auchVonPlatte);
        out.println("Die Datei \"" + bildurl + "\" wurde entfernt.");
      } catch (EntferneException e) {
        out.println("Die Datei \"" + bildurl + "\" konnte nicht entfernt werden."
            + e);
      }
    }
  }
}
