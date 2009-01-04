package jpp.webapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import benutzermanager.Benutzer;
import benutzermanager.RechteManager;

import jpp.core.LuceneJPPCore;
import jpp.core.exceptions.SucheException;

public class GetAlbenServlet extends HttpServlet {

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

    HttpSession session = req.getSession();

    Benutzer user = (Benutzer) session.getAttribute("user");

    if (user == null || !user.hatRecht(RechteManager.getRecht("suche"))) {
      out.println("Sie haben nicht das Recht die Alben auszulesen.");
    } else {

      LuceneJPPCore kern = (LuceneJPPCore) getServletContext().getAttribute("JPPCore");

      if (kern == null) {
        out.println("JPPCore ist nicht vorhanden. Es ist vermutlich beim start"
            + "ein Fehler aufgetreten. Überprüfen Sie die Logfiles.");
      } else {
        getAlben(req, resp, kern);
      }
    }

  }
  
  private void getAlben(HttpServletRequest req, HttpServletResponse resp,
      LuceneJPPCore kern) throws ServletException, IOException {

    String gruppe = req.getParameter("gruppe");
    
    PrintWriter out = resp.getWriter(); 
    //out.print("Weihnachtsfeier");
    
    List<String> alben = kern.getAlben(gruppe);
    int size = alben.size();
    for (int i = 0; i < size; i++) {
      out.print(alben.get(i));
      if (i < size - 1) {
        out.print(";");
      }
    }
    out.println();
  }
}
