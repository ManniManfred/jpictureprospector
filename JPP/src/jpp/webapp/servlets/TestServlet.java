package jpp.webapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jpp.core.JPPCore;

public class TestServlet extends HttpServlet {

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
    } else {
      out.println("true");
    }
  }
}
