package jpp.webapp.servlets;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AbmeldenServlet extends HttpServlet {

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
    
    session.setAttribute("user", null);
    
    out.print("Sie sind jetzt abgemeldet.");
  }
}
