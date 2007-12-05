package jpp.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JSeparator;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jpp.core.AbstractJPPCore;
import jpp.core.BildDokument;
import jpp.core.Trefferliste;
import jpp.core.exceptions.AendereException;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ErzeugeException;
import jpp.core.exceptions.ImportException;
import jpp.core.exceptions.SucheException;
import jpp.merkmale.DateipfadMerkmal;
import jpp.merkmale.Merkmal;

import org.xml.sax.SAXException;

public class JPPClient extends AbstractJPPCore {

  private static String meldungVomServer;
  
  
  /** Logger, der in dieser Klasse verwendet wird. */
  Logger logger = Logger.getLogger("JPPClient");
  
  
  private String jppServer;
  
  private String sessionId;
  
  public JPPClient(String jppServer, String user, String password) throws ErzeugeException {
    this.jppServer = jppServer;
    
    /* Beim JPPServer anmelden */
    String anfrage = jppServer + "anmelden?" 
      + "loginname=" + user 
      + "&passwort=" + password;
    
    
    try {
      URL page = new URL(anfrage);
      logger.log(Level.INFO, "Anfrage an Server: " + page);
      
      URLConnection conn = page.openConnection();
      
      InputStream in = conn.getInputStream();
      String ergebnis = readText(in);
      

      if (ergebnis.indexOf("true") == 0) {
        
        /* Lese die Sessionid aus */
        sessionId = ergebnis.substring(ergebnis.lastIndexOf("=") + 1).trim();
      } else {
        throw new ErzeugeException(ergebnis);
      }
      
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  @Override
  public void aendere(BildDokument bild) throws AendereException {
    /* Anfrage aufbauen */
    String anfrage = "aendere?bild=" 
      + bild.getMerkmal(DateipfadMerkmal.FELDNAME).getWert();
    
    Collection<Merkmal> liste = bild.gibGrundMerkmale();
    
    for (Merkmal m : liste) {
      if (m.istEditierbar()) {
        try {
          anfrage += "&" + m.getClass().getName() + "=" 
            + URLEncoder.encode(m.getWert().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    
    /* Anfrage senden */
    try {
      InputStream in = stelleAnfrage(anfrage);
      String ergebnis = readText(in);
      
      System.out.println("Ergebnis = " + ergebnis);
      
      
    } catch (MalformedURLException e) {
      throw new AendereException("Die URL ist ungueltig.", e);
    } catch (IOException e) {
      throw new AendereException("Verbindungsfehler.", e);
    }
  }

  @Override
  public String clearUpIndex() throws SucheException {
    String ergebnis;
    try {
      String anfrage = "clearUpIndex";
      
      InputStream in = stelleAnfrage(anfrage);
      ergebnis = readText(in);
      
      System.out.println("Ergebnis = " + ergebnis);
      
      
    } catch (MalformedURLException e) {
      throw new SucheException("Die URL ist ungueltig.", e);
    } catch (IOException e) {
      throw new SucheException("Verbindungsfehler.", e);
    }
    
    return ergebnis;
  }
  
  
  @Override
  public void entferne(URL datei, boolean auchVonFestplatte) throws EntferneException {

    try {
      String anfrage = "entferne?bild=" + datei 
          + "&vonPlatte=" + Boolean.toString(auchVonFestplatte);
      
      InputStream in = stelleAnfrage(anfrage);
      String ergebnis = readText(in);
      
      System.out.println("Ergebnis = " + ergebnis);
      
    } catch (MalformedURLException e) {
      throw new EntferneException("Die URL ist ungueltig.", e);
    } catch (IOException e) {
      throw new EntferneException("Verbindungsfehler.", e);
    }
  }

  @Override
  public void importiere(URL datei) throws ImportException {
    URL page;
    try {
      page = new URL(jppServer + "importiere");

      logger.log(Level.INFO, "Anfrage an Server: " + page);

      String boundary = MultiPartFormOutputStream.createBoundary();
      HttpURLConnection hpc = (HttpURLConnection) page.openConnection();
      hpc.setRequestMethod("POST");
      hpc.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
      hpc.setRequestProperty("Accept", "*/*");
      hpc.setRequestProperty("Content-Type", MultiPartFormOutputStream
          .getContentType(boundary));
      // set some other request headers...
      hpc.setRequestProperty("Connection", "Keep-Alive");
      hpc.setRequestProperty("Cache-Control", "no-cache");
      // no need to connect cuz getOutputStream() does it
      hpc.setDoOutput(true);
      hpc.setDoInput(true);
  
      MultiPartFormOutputStream out = new MultiPartFormOutputStream(
          hpc.getOutputStream(), boundary);
      
      // upload the file
      URLConnection conn = datei.openConnection();
      out.writeFile("bilddatei", conn.getContentType(), datei.getFile(),
          conn.getInputStream());
  
      out.close();
      
      
      // read response from server
      System.out.println("ergebnis = " + readText(hpc.getInputStream()));    

    } catch (MalformedURLException e) {
      throw new ImportException("Die URL ist ungueltig.", e);
    } catch (ProtocolException e) {
      throw new ImportException("Verbindungsfehler.", e);
    } catch (IOException e) {
      throw new ImportException("Verbindungsfehler.", e);
    }
  }

  @Override
  public Trefferliste suche(String suchtext, int offset, int maxanzahl) throws SucheException {
    Trefferliste ergebnis;
    try {
      String anfrage = "suche?suchtext=" + suchtext + "&offset=" + offset 
          + "&maxanzahl=" + maxanzahl + "&format=xml";
      
      InputStream result = stelleAnfrage(anfrage);
      
      
      /* erhaltene XML Datei in eine Trefferliste parsen */
      SAXParserFactory factory = SAXParserFactory.newInstance(); 
      try {
        SAXParser saxParser = factory.newSAXParser();
        
        TrefferlisteHandler handler = new TrefferlisteHandler();
        saxParser.parse(result, handler );
        
        ergebnis = handler.getTrefferliste();
      } catch (ParserConfigurationException e) {
        throw new SucheException("Konnte die empfange XML nicht parsen.", e);
      } catch (SAXException e) {
        throw new SucheException("Konnte die empfange XML nicht parsen.", e);
      }
      
      
    } catch (MalformedURLException e) {
      throw new SucheException("Die URL ist ungueltig.", e);
    } catch (IOException e) {
      throw new SucheException("Verbindungsfehler.", e);
    }
    
    
    return ergebnis;
  }

  
  private InputStream stelleAnfrage(String anfrage) throws IOException  {
    InputStream ergebnis;
    
    URL page = new URL(jppServer + anfrage);
    
    
    logger.log(Level.INFO, "Anfrage an Server: " + page);
    
    HttpURLConnection conn = (HttpURLConnection) page.openConnection();
    
    conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
    
    ergebnis = conn.getInputStream();  
    
    
    return ergebnis;
  }
  
  /**
   * Liest den komplett verfuegbaren Text vom Eingabestrom und gibt diesen 
   * zurueck.
   * @return Text vom Eingabestrom
   */
  public static String readText(InputStream in) {
    StringBuffer buffer = new StringBuffer();
    int zeichen;
    try {
        zeichen = in.read();
        while (zeichen != -1) {
            buffer.append((char)zeichen);
            zeichen = in.read();
        }
    } catch (IOException e) {
        Logger.getLogger("JPPClient").log(Level.WARNING, "Zeichen konnte nicht gelesen werden.", e);
    }
    return buffer.toString();
  }
  

  /**
   * Testet, ob der uebergebene JPPServer verfuegbar ist.
   */
  public static boolean istJPPServerVerfuegbar(String jppServer) {
    try {
      URL url = new URL(jppServer + "test?typ=verfuegbar");
      URLConnection conn = url.openConnection();
      
      String verfuegbar = JPPClient.readText(conn.getInputStream());
      meldungVomServer = verfuegbar;
      
      return (verfuegbar.indexOf("true") == 0);
    } catch (MalformedURLException e) {
      return false;
    } catch (IOException e) {
      return false;
    }
  }
  
  /**
   * Gibt die Meldung vom Server zurueck, die dieser bei der Verfuegbarkeits-
   * anfrage gesendet hat.
   * 
   * @return Meldung vom Server
   */
  public static String getMeldungVomServer() {
    return meldungVomServer;
  }

}
