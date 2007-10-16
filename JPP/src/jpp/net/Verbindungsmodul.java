package jpp.net;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Member;
import java.net.Socket;
import java.net.UnknownHostException;

import jpp.core.BildDokument;
import jpp.core.CoreInterface;
import jpp.core.Trefferliste;
import jpp.core.exceptions.AendereException;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ImportException;
import jpp.core.exceptions.SucheException;
import jpp.xml.BildDokumentXMLWriter;

/**
 * Ein Objekt der Klasse stellt einen Client dar, der auf einem
 * Server die Kernfunktionalitaeten der Anwendung ausfuehrt.
 * Das Objekt ist nur einmal pro Applikationsinstanz verfuegbar und
 * dementsprechend realisiert.
 */
public class Verbindungsmodul implements CoreInterface {
  
  /** Dieses Objekt, was sich selbst verwaltet. */
  private static Verbindungsmodul self;
  
  private static final String MSG_SOCKET_CLOSE_ERROR = "Die Verbindung konnte" +
      " nicht geschlossen werden.";
  
  /** Enthaelt die Hostadresse auf den sich dieses Objekt verbindet. */
  private String host;
  
  /** Enthaelt den Port mit dem sich dieses Objekt verbindet. */
  private int port;
  
  /**
   * Erstellt ein neues Objekt der Klasse, kann allerdings nur
   * aus dieser Klasse aufgerufen werden.
   */
  private Verbindungsmodul() {
  }
  
  /**
   * Liefert dieses Objekt, wenn es eins gibt, ansonsten wird eins
   * erstellt und zurückgegeben.
   * @return  das <code>Verbindungsmodul</code>
   */
  public static Verbindungsmodul getVerbindungsmodul() {
    
    if (self == null) {
      self = new Verbindungsmodul();
    }
    return self;
  }
  
  /**
   * Aendert ein <code>BildDokument</code> innerhalb des Lucene-Index
   * mit den entsprechenden Merkmalen, die zum <code>BildDokument</code>
   * getaetigt wurden.
   * @param dokument  das geanderte <code>BildDokument</code>
   * @throws AendereException  wird geworfen, wenn das <code>BildDokument</code>
   *           nicht geandert werden konnte.
   */
  public void aendere(BildDokument dokument) throws AendereException {
    
    Socket socket = null;
    try {
      socket = new Socket(host, port);
      OutputStream serverOutput = socket.getOutputStream();
      BildDokumentXMLWriter.writeBildDokument(serverOutput, dokument,
          getExecutingMethodName());
    } catch (UnknownHostException e) {
      throw new AendereException("Die \u00c4nderung konnte nicht " +
          "durchgef\u00fchrt werden.\n" + e.getMessage());
    } catch (IOException e) {
      throw new AendereException("Die \u00c4nderung konnte nicht " +
          "durchgef\u00fchrt werden.\n" + e.getMessage());
    } finally {
      try {
        if (socket != null) socket.close();
      } catch (IOException e2) {
        System.out.println(MSG_SOCKET_CLOSE_ERROR);
      }
    }
  }

  /**
   * Entfernt ein <code>BildDokument</code> aus dem Lucene-Index, so
   * dass entsprechende Aktionen nicht mehr durchgefuehrt werden koennen.
   * @param dokument  das zu entfernende <code>BildDokument</code>
   * @throws EntferneException  wird geworfen, wenn das Bild nicht
   *           aus dem Index entfernt werden konnte
   */
  public void entferne(BildDokument dokument) throws EntferneException {
    
    Socket socket = null;
    try {
      socket = new Socket(host, port);
      OutputStream serverOutput = socket.getOutputStream();
      BildDokumentXMLWriter.writeBildDokument(serverOutput, dokument,
          getExecutingMethodName());
    } catch (UnknownHostException e) {
      throw new EntferneException("Das Dokument konnte nicht enfernt" +
          " werden.\n" + e.getMessage());
    } catch (IOException e) {
      throw new EntferneException("Das Dokument konnte nicht enfernt" +
          " werden.\n" + e.getMessage());
    } finally {
      try { 
        if (socket != null) socket.close();
      } catch (IOException e2) {
        System.out.println(MSG_SOCKET_CLOSE_ERROR);
      }
      
    }
  }

  public BildDokument importiere(File f) throws ImportException {
    // TODO Auto-generated method stub
    return null;
  }

  public Trefferliste suche(String suchString, int offset, int maxanzahl)
      throws SucheException {
    // TODO Auto-generated method stub
    return null;
  }
  
  /**
   * Liefert den Namen der zuletzt ausgefuehrten Methode vor dieser Methode.
   * @return  den Namen der zuletzt ausgefuehrten Methode
   */
  private static String getExecutingMethodName() {
    Throwable t = new Throwable();
    StackTraceElement[] es = t.getStackTrace();
    return es[1].getMethodName();
  }

  /**
   * Liefert die Adresse des Hosts, auf den sich dieses Objekt verbindet.
   * @return  die Adresse des Hosts
   */
  public String getHost() {
    return host;
  }

  /**
   * Setzt die Adresse des Hosts, auf den sich dieses Objekt verbindet.
   * @param host
   */
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * Liefert den Port fuer die Verbindung, die das Objekt durchfuehrt.
   * @return  der Port der Verbindung
   */
  public int getPort() {
    return port;
  }

  /**
   * Setzt den Port fuer die Verbindung, die das Objekt durchfuehrt.
   * @param port  der zu setzende Port.
   */
  public void setPort(int port) {
    this.port = port;
  }
  
}
