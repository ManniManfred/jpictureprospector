package jpp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import jpp.core.AbstractJPPCore;
import jpp.core.JPPCore;
import jpp.core.Trefferliste;
import jpp.core.exceptions.ErzeugeException;
import jpp.core.exceptions.SucheException;
import jpp.xml.XMLParser;

/**
 * Diese Klasse stellt eine Verbindung zum Client dar.
 * 
 * @author Manfred Rosskamp
 */
public class Verbindung extends Thread {

  private static final char STOPZEICHEN = '$';
  
  private static final String USERNAME = "manni";
  private static final String PASSWORT = "blablub";

  
  private Logger logger;
  
  private Socket conn;
  
  private PrintWriter out;
  private BufferedReader in;
  
  
  public Verbindung(Socket conn) {
    this.conn = conn;
  }
  
  public void run() {
    logger = Logger.getLogger("Verbindung" + conn.getLocalAddress());
    
    logger.log(Level.INFO, conn.getLocalAddress() + " hat eine Verbindung zu " +
        "diesem Sever aufgebaut.");
    
    try {
      out = new PrintWriter(conn.getOutputStream());
      in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

      String user = readln();
      String passwort = readln();
      if (istPasswortKorrekt(user, passwort)) {
        println("OK - Herzlich Willkommen bei unserem JPPServer");
        verarbeiteAnfragen();
      } else {
        println("NO - Sie haben keine Berechtigung");
        beende();
      }
    } catch (IOException e) {
      
      e.printStackTrace();
    }
  }
  
  private void println(String text) {
    out.println(text);
    out.flush();
  }
  
  private void print(String text) {
    out.print(text);
    out.flush();
  }
  
  private String readln() throws IOException {
    return in.readLine();
  }
  
  private void verarbeiteAnfragen() throws IOException{
    String befehl = "";
    List<String> args;
    String line;
    
    while(true) {
      print("" + STOPZEICHEN);
      try {
        line = readln();
        if (line == null) {
          continue;
        }
        args = getArgs(line);
      } catch(IOException e) {
        logger.log(Level.WARNING, "Verbindung wurde nicht korrekt beendet.");
        break;
      }
      befehl = args.get(0);
      logger.log(Level.INFO, "Fuehrt \"" + line + "\" aus");
  
      String ergebnis;
      if (befehl.equals("suche")) {
        ergebnis = suche(args);
      } else if (befehl.equals("importiere")){
        ergebnis = "TODO importiere";
      } else if (befehl.equals("aendere")){
        ergebnis = "TODO aendere";
      } else if (befehl.equals("entferne")){
        ergebnis = "TODO entferne";
      } else if (befehl.equals("exit"))  {
        beende();
        logger.log(Level.INFO, "Hat die Verbindung regulaer beendet.");
        break;
      } else {
        ergebnis = "\"" + befehl + "\" ist ein unbekannter Befehl.";
      }
      
      //logger.log(Level.INFO, "Ergebnis: " + ergebnis);
      println(ergebnis);
      
    }
    
  }
  
  /**
   * Wandelt den line-String um in eine Liste von Argumenten.
   * 
   * @param line String, der umgewandelt werden soll
   * @return Liste von Argumenten
   */
  private List<String> getArgs(String line) {
    List<String> args = new ArrayList<String>(3);
    
    StringTokenizer izer = new StringTokenizer(line, " \"", true);
    
    boolean isQuotted = false;
    String quote = "";
    
    while (izer.hasMoreTokens()) {
      String token = izer.nextToken();
      if (token.equals(" ")) {
        
      } else if (token.equals("\"")) {
        if (isQuotted) {
          quote = quote.substring(0, quote.length() - 1);
          args.add(quote);
          quote = "";
        }
        isQuotted = !isQuotted;
      } else {
        if (isQuotted) {
          quote += token + " ";
        } else {
          args.add(token);
        }
      }
    }
    
    return args;
  }

  private boolean istPasswortKorrekt(String username, String passwort) {
    return username != null && passwort != null
      && username.equals(Verbindung.USERNAME) 
      && passwort.equals(Verbindung.PASSWORT);
  }
  
  
  
  /**
   * Beendet diese Verbindung.
   */
  public void beende() {
    try {
      println("Tschuess");
    
      out.close();
      in.close();
      conn.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /****************************************************************************
   * Die angebotenen Dienste/Funktionen dieses Servers
   ****************************************************************************/

  private String suche(List<String> args) {
    String ergebnis;
    if (args.size() != 4) {
      ergebnis = "Suchbefehl muss folgenden Aufbau haben:\n";
      ergebnis += "\tsuche <Offset> <Maxanzahl> <suchstring>";
    } else {
      String suchtext = args.get(3);
      
      try {
        int offset = Integer.parseInt(args.get(1));
        int max = Integer.parseInt(args.get(2));

        try {
          AbstractJPPCore kern = new JPPCore("imageIndex");
          Trefferliste liste = kern.suche(suchtext, offset, max);
          
          ergebnis = XMLParser.getTrefferlisteDok(liste);
        } catch (ErzeugeException e) {
          logger.log(Level.WARNING, "Konnte JPPCore nicht erzeugen.", e);
          ergebnis = "Konnte JPPCore nicht erzeugen.";
        } catch (SucheException e) {
          logger.log(Level.WARNING, "Konnte die Suche nicht ausfuehren.", e);
          ergebnis = "Konnte die Suche nicht ausfuehren: " + e.getMessage();
        }
      } catch(NumberFormatException e) {
        ergebnis = "Offset oder Maxanzahl konnten nicht in eine Zahl " +
            "umgewandelt werden";
      }
    }
    return ergebnis;
  }
  
  private void importiere(String[] args) {
    
  }
  
  private void aendere(String[] args) {
    
  }
  
  private void entferne(String[] args) {
    
  }
}
