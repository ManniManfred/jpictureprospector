package jpp.webapp.servlets;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;


import jpp.core.GeoeffnetesBild;
import jpp.core.LuceneJPPCore;
import jpp.core.exceptions.GeneriereException;
import jpp.core.exceptions.ImportException;
import jpp.core.thumbnail.SimpleThumbnailGeneriererFactory;
import jpp.core.thumbnail.ThumbnailGenerierer;
import jpp.settings.CoreSettings;
import jpp.settings.ServerSettings;
import jpp.settings.SettingsManager;
import benutzermanager.Benutzer;
import benutzermanager.RechteManager;

import com.oreilly.servlet.MultipartRequest;


public class ImportiereServlet extends HttpServlet {

  private static final long serialVersionUID = 7017607629370760883L;

  /** Logger, der alle Fehler loggt. */
  private static Logger logger = Logger.getLogger("jpp.webapps.servlets.ImportiereServlet");
  
  
  /**
   * Enthaelt das ServerSettings Objekt mit allen wichtigen serverSettings 
   * dieser Anwendung.
   */
  private ServerSettings serverSettings = 
    SettingsManager.getSettings(ServerSettings.class);

  private CoreSettings coreSettings = 
    SettingsManager.getSettings(CoreSettings.class);
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    PrintWriter out = resp.getWriter(); 
    out.println("Hallo? Bist du sicher, dass du hier richtig bist?");
  }
  
  
  

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {


    PrintWriter out = resp.getWriter();

    HttpSession session = req.getSession();

    Benutzer user = (Benutzer) session.getAttribute("user");

    if (user == null || !user.hatRecht(RechteManager.getRecht("importiere"))) {
      out.println("Sie haben nicht das Recht ein Bild zu importieren.");
    } else {

      LuceneJPPCore kern = (LuceneJPPCore) getServletContext().getAttribute("JPPCore");

      if (kern == null) {
        out.println("JPPCore ist nicht vorhanden. Es ist vermutlich beim start"
            + "ein Fehler aufgetreten. Überprüfen Sie die Logfiles.");
      } else {
        importiere(req, resp, kern);
      }
    }

  }

  private void importiere(HttpServletRequest req, HttpServletResponse resp, 
      LuceneJPPCore kern) throws ServletException, IOException {
    
    PrintWriter out = resp.getWriter(); 
        
    MultipartRequest multi = new MultipartRequest(req, serverSettings.getUploadOrdner(), serverSettings.maxContentSize); 
    Enumeration files = multi.getFileNames(); 
    while ( files.hasMoreElements() ) 
    { 
      String name = (String) files.nextElement(); 
      File f = multi.getFile(name);
      if (f != null) {
        try {
          kern.importiere(f.toURL());
          
          
          ThumbnailManager.generateThumbnailsFor(f);
          logger.log(Level.INFO, "Bild \"" + f.getName() + "\" wurde erfolgreich importiert.");
        } catch (ImportException e) {
          logger.log(Level.WARNING, "Bild \"" + f.getName() + "\" konnte nicht importiert werden.", e);
        }
      }
    }
    
    
  }


}