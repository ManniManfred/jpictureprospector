package jpp.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import jpp.core.exceptions.PostException;
import jpp.core.exceptions.SucheException;
import jpp.merkmale.DateipfadMerkmal;
import jpp.merkmale.Merkmal;

import org.xml.sax.SAXException;

public class ClientJPPCore extends AbstractJPPCore {

  private static String meldungVomServer;
  
  
  /** Logger, der in dieser Klasse verwendet wird. */
  Logger logger = Logger.getLogger("JPPClient");
  
  
  private String jppServer;
  
  private String sessionId;
  
  public ClientJPPCore(String jppServer, String user, String password) throws ErzeugeException {
    this.jppServer = jppServer;
    
    /* Beim JPPServer anmelden */
    String anfrage = jppServer + "anmelden?" 
      + "loginname=" + user 
      + "&passwort=" + password;
    
    System.out.println("anfrage = " + anfrage);
    try {
      URL page = new URL(anfrage);
      logger.log(Level.INFO, "Anfrage an Server: " + page);
      
      URLConnection conn = page.openConnection();
      
      InputStream in = conn.getInputStream();
      String ergebnis = readText(in);

      logger.log(Level.INFO, "Ergebnis: " + ergebnis);

      if (ergebnis.indexOf("true") == 0) {
        
        /* Lese die Sessionid aus */
        sessionId = ergebnis.substring(ergebnis.lastIndexOf("=") + 1).trim();
      } else {
        throw new ErzeugeException(ergebnis);
      }
      
    } catch (MalformedURLException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
    }
    
  }

  @Override
  public void aendere(BildDokument bild) throws AendereException {
    Map<String, String> fields = new HashMap<String, String>();
    fields.put("bild", bild.getMerkmal(DateipfadMerkmal.FELDNAME).getWert().toString());
    
    Collection<Merkmal> liste = bild.gibGrundMerkmale();
    
    for (Merkmal m : liste) {
      if (m.istEditierbar()) {
        fields.put(m.getClass().getName(), m.getWert().toString());
      }
    }

    String ergebnis;
    
    try {
      InputStream in = doPost(jppServer + "aendere", fields);
      ergebnis = readText(in);
      
      logger.log(Level.INFO, "Ergebnis: " + ergebnis);
    } catch (PostException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
      throw new AendereException("Verbindungsfehler.", e);
    }
    
  }

  @Override
  public String clearUpIndex() throws SucheException {

    Map<String, String> fields = new HashMap<String, String>();
    String ergebnis;
    
    try {
      InputStream in = doPost(jppServer + "clearUpIndex", fields);
      ergebnis = readText(in);
      
      logger.log(Level.INFO, "Ergebnis: " + ergebnis);
    } catch (PostException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
      throw new SucheException("Verbindungsfehler.", e);
    }
    
    return ergebnis;
    
  }
  
  
  @Override
  public void entferne(URL datei, boolean auchVonFestplatte) throws EntferneException {

    Map<String, String> fields = new HashMap<String, String>();
    fields.put("bild", datei.toString());
    fields.put("vonPlatte", Boolean.toString(auchVonFestplatte));
    
    try {
      InputStream in = doPost(jppServer + "entferne", fields);
      String ergebnis = readText(in);
      
      logger.log(Level.INFO, "Ergebnis: " + ergebnis);
    } catch (PostException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
      throw new EntferneException("Verbindungsfehler.", e);
    }
    
  }


  /**
   * Gibt eine Liste aller im Index vorhandenen Alben zurück, die zu einer 
   * bestimmten Gruppe gehört oder zu wenn die übergebene Gruppe null ist zu
   * allen Gruppen gehört.
   * @param gruppe Gruppe zu der alle Alben zurückgegeben werden. Wenn dieser
   *  Wert <code>null</code> ist, werden alle Alben zurückgegeben.
   * @return eine Liste aller Alben
   */
  public List<String> getAlben(String gruppe) {
    Map<String, String> fields = new HashMap<String, String>();
    fields.put("gruppe", gruppe);
    
    try {
      InputStream in = doPost(jppServer + "getAlben", fields);
      String ergebnis = readText(in);
      logger.log(Level.INFO, "Ergebnis: " + ergebnis);
      return Arrays.asList(ergebnis.split(";"));
    } catch (PostException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
      return new ArrayList<String>();
    }
  }

  
  @Override
  public void importiere(URL datei) throws ImportException {
    try {
      doPost(jppServer + "importiere", datei);
    } catch (PostException e1) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e1);
      throw new ImportException("Verbindungsfehler.", e1);
    }
  }

  @Override
  public Trefferliste suche(String suchtext, int offset, int maxanzahl) throws SucheException {
    Trefferliste ergebnis;

    Map<String, String> fields = new HashMap<String, String>();
    fields.put("suchtext", suchtext);
    fields.put("offset", "" + offset);
    fields.put("maxanzahl", "" + maxanzahl);
    fields.put("format", "xml");
    
    try {
      InputStream result = doPost(jppServer + "suche", fields);
      
      
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
      } catch (IOException e) {
        logger.log(Level.WARNING, "Verbindungsfehler.", e);
        throw new SucheException("Verbindungsfehler.", e);
      }
    } catch (PostException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
      throw new SucheException("Verbindungsfehler.", e);
    }
    
    
    return ergebnis;
  }
  
  public void rotate(BildDokument dok, double degree) {
	  // @TODO: implement
  }
  
  /**
   * Stellt eine HTTP Anfrage der Methode POST an die Adresse <code>to</code>
   * an sendet die Parameter (<code>fields</code>) mit.
   * 
   * @param to Adresse, an die die Anfrage geschickt wird
   * @param fields Schlüsselwerte Paare, die mitgeschickt werden
   */
  private InputStream doPost(String to, Map<String, String> fields) throws PostException {
    URL page;
    InputStream ergebnis;
    try {
      page = new URL(to);
      HttpURLConnection hpc = (HttpURLConnection) page.openConnection();
      hpc.setRequestMethod("POST");
      hpc.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
      hpc.setRequestProperty("Accept", "*/*");
      hpc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      // set some other request headers...
      hpc.setRequestProperty("Connection", "Keep-Alive");
      hpc.setRequestProperty("Cache-Control", "no-cache");
      // no need to connect cuz getOutputStream() does it
      hpc.setDoOutput(true);
      hpc.setDoInput(true);
      
      
      OutputStreamWriter out = new OutputStreamWriter(
          hpc.getOutputStream());

      StringBuffer logInfo = new StringBuffer();
      for (String key : fields.keySet()) {
        String value = fields.get(key);
        
        // wenn der Wert null ist, wird das Schlüsselwertepaar erstgarnicht
        // gesendet
        if (value != null) {
          out.write("&" + key + "=" + URLEncoder.encode(value, "UTF-8"));
          
          logInfo.append(key + ": " + value + "\n");
        }
      }

      logger.log(Level.INFO, "Anfrage an " + page 
          + "\n" + logInfo.toString());
      out.close();
      
      ergebnis = hpc.getInputStream();
      
    } catch (MalformedURLException e) {
      logger.log(Level.WARNING, "Die URL ist nicht korrekt.", e);
      throw new PostException("Die URL ist ungueltig.", e);
    } catch (ProtocolException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
      throw new PostException("Verbindungsfehler.", e);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
      throw new PostException("Verbindungsfehler.", e);
    }
    return ergebnis;
  }
  
  private void doPost(String to, URL zuSendendedatei) throws PostException {
    URL page;
    try {

      page = new URL(to);
      
      logger.log(Level.INFO, "Poste Datei \"" + zuSendendedatei 
          + "\" an Server: " + page);
      
      
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
      URLConnection conn = zuSendendedatei.openConnection();
      out.writeFile("bilddatei", conn.getContentType(), zuSendendedatei.getFile(),
          conn.getInputStream());
      
      out.close();
      
      
      // read response from server 
      logger.log(Level.INFO, "Ergebnis: " + readText(hpc.getInputStream()));
      
    } catch (MalformedURLException e) {
      logger.log(Level.WARNING, "Die URL ist nicht korrekt.", e);
      throw new PostException("Die URL ist ungueltig.", e);
    } catch (ProtocolException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
      throw new PostException("Verbindungsfehler.", e);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Verbindungsfehler.", e);
      throw new PostException("Verbindungsfehler.", e);
    }
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
      
      String verfuegbar = ClientJPPCore.readText(conn.getInputStream());
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
