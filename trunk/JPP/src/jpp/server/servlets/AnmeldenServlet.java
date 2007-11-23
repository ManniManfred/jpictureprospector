
package jpp.server.servlets;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import benutzermanager.Benutzer;
import benutzermanager.BenutzerManager;

public class AnmeldenServlet extends HttpServlet {

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

    HttpSession session = req.getSession();
    
    /* Benutzer ueberpruefen */
    String loginname = req.getParameter("loginname");
    String passwort = req.getParameter("passwort");
    
    BenutzerManager manager = 
      (BenutzerManager) getServletContext().getAttribute("BenutzerManager");
    Benutzer user = manager.getBenutzer(loginname, passwort);
    
    if (user == null) {
      out.println("false - Passwort-Benutzer Kombination falsch!");
    } else {
      out.println("true - Sie sind jetzt angemeldet.");
      session.setAttribute("user", user);
    }
    
    
    
  }
}
