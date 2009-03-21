package jpp.webapp.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import jpp.core.LuceneJPPCore;
import jpp.core.Trefferliste;
import jpp.core.exceptions.SucheException;
import jpp.webapp.TrefferlisteParser;
import benutzermanager.Benutzer;
import benutzermanager.RechteManager;

public class SucheServlet extends HttpServlet {

  private static final long serialVersionUID = 7017607629370760883L;

  private String FORMAT_XML = "xml";

  private String FORMAT_HTML_DOKUMENT = "html-dokument"; // Ist Default

  private String FORMAT_HTML_ELEMENTE = "html-elemente";

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
      out.println("Sie haben nicht das Recht etwas zu suchen.");
    } else {

      LuceneJPPCore kern = (LuceneJPPCore) getServletContext().getAttribute(
          "JPPCore");

      if (kern == null) {
        out.println("JPPCore ist nicht vorhanden. Es ist vermutlich beim start"
            + "ein Fehler aufgetreten. Überprüfen Sie die Logfiles.");
      } else {
        suche(req, resp, kern);
      }
    }

  }

  private void suche(HttpServletRequest req, HttpServletResponse resp,
      LuceneJPPCore kern) throws ServletException, IOException {
    PrintWriter out = resp.getWriter();

    try {
      String suchtext = req.getParameter("suchtext");

      // get parameter
      int offset;
      int maxAnzahl;
      int sizeIndex;

      try {
        offset = Integer.parseInt(req.getParameter("offset"));
      } catch (Exception e) {
        offset = 0;
      }

      try {
        maxAnzahl = Integer.parseInt(req.getParameter("maxanzahl"));
      } catch (Exception e) {
        maxAnzahl = 10;
      }

      try {
        sizeIndex = Integer.parseInt(req.getParameter("sizeIndex"));;
      } catch (Exception e) {
        sizeIndex = -1;
      }
      TrefferlisteParser.sizeIndex = sizeIndex;
      
      try {
        Trefferliste liste = kern.suche(suchtext, offset, maxAnzahl);

        String xml = TrefferlisteParser.getTrefferlisteDok(liste);

        String format = req.getParameter("format");
        if (format != null && format.equals(FORMAT_XML)) {
          resp.setContentType("text/xml");

          out.println(xml);
        } else {
          String stylesheet;
          resp.setContentType("text/html");

          if (format != null && format.equals(FORMAT_HTML_ELEMENTE)) {
            stylesheet = "trefferliste.xsl";
          } else {
            stylesheet = "trefferDok.xsl";
          }

          /* In HTML umwandeln */
          Source input = new StreamSource(new ByteArrayInputStream(xml
              .getBytes()));
          Result output = new StreamResult(out);

          Transformer transformer = TransformerFactory.newInstance()
              .newTransformer(
                  new StreamSource(this.getServletContext().getRealPath(
                      stylesheet)));

          transformer.transform(input, output);
        }

      } catch (SucheException e) {
        out.println("Fehler beim suchen: " + e.getCause());
      } catch (TransformerConfigurationException e) {
        out.println("fehler: " + e);
      } catch (TransformerFactoryConfigurationError e) {
        out.println("fehler: " + e);
      } catch (TransformerException e) {
        out.println("fehler: " + e);
      }
    } catch (NumberFormatException e) {
      out.println("Anfrage Parameter nicht korrekt." + e);
    }




  }
}
