package jpp.webapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jpp.core.BildDokument;
import jpp.core.JPPCore;
import jpp.core.exceptions.AendereException;
import jpp.core.exceptions.ErzeugeBildDokumentException;
import jpp.merkmale.Merkmal;
import jpp.webapp.Mapping;
import benutzermanager.Benutzer;
import benutzermanager.RechteManager;

public class AendereServlet extends HttpServlet {

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

    if (user == null || !user.hatRecht(RechteManager.getRecht("aendere"))) {
      out.println("Sie haben nicht das Recht etwas zu aendern");
    } else {

      JPPCore kern = (JPPCore) getServletContext().getAttribute("JPPCore");

      if (kern == null) {
        out.println("JPPCore ist nicht vorhanden. Es ist vermutlich beim start"
            + "ein Fehler aufgetreten. Überprüfen Sie die Logfiles.");
      } else {
        aendere(req, resp, kern);
      }
    }

  }

  private void aendere(HttpServletRequest req, HttpServletResponse resp, 
      JPPCore kern) throws ServletException, IOException {

    PrintWriter out = resp.getWriter();
    
    String bildurl = req.getParameter("bild");
    if (bildurl == null) {
      out.println("Bitte geben Sie ein Bild an, welches geaendert " +
          "werden soll.");
    } else {
      /* BildDokument aus dem Index holen */
      BildDokument dok;
      try {
        dok = kern.getBildDokument(Mapping.wandleInLokal(new URL(bildurl)));
  
        if (dok == null) {
          out.println("Konnte das entsprechende BildDokument nicht finden.");
        } else {
          int anzahlAenderungen = 0;
          /* Alle Merkmale durchgehen und Aenderungen setzten */
          Collection<Merkmal> merkmale = dok.gibGrundMerkmale();
  
          for (Merkmal m : merkmale) {
            String neuerWert = req.getParameter(m.getClass().getName());
  
            if (neuerWert != null) {
              /* Nur aendern, wenn das Merkmal editierbar ist */
              if (m.istEditierbar()) {
                m.setWert(neuerWert);
                anzahlAenderungen++;
              } else {
                out.println("Das Merkmal " + m.getClass().getName()
                    + " ist nicht veraenderbar.");
              }
            }
  
          }
  
          /*
           * Aender Methode des Kerns nur aufrufen, wenn wirklich Aenderungen
           * vorliegen.
           */
          if (anzahlAenderungen > 0) {
            try {
              kern.aendere(dok);
              out.println("Die " + anzahlAenderungen + " Aenderungen wurden "
                  + "erfolgreich uebernommen.");
            } catch (AendereException e) {
              out.println("Die " + anzahlAenderungen + " Aenderungen konnten "
                  + "nicht uebernommen werden. " + e);
            }
          }
  
        }
      } catch (ErzeugeBildDokumentException e) {
        out.println("Konnte das BildDokument nicht laden. " + e);
      }
    }
  }
}
