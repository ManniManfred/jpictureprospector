package jpp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import jpp.settings.ServerSettings;
import jpp.settings.SettingsManager;
import jpp.ui.ServerStartDialog;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.webapp.WebAppContext;


/**
 * Starter-Klasse fuer den Jetty-Server, indem dann der JPP-Webapplikation
 * ausgefuehrt wird.
 */
public class ServerStarter {

  private Server server;

  /** Logger, der alle Fehler loggt. */
  private static Logger logger = Logger.getLogger("jpp.ServerStarter");

  public void startServer(ServerSettings serverSettings) {

    /* Den Webserver Jetty starten */
    WebAppContext mainCtx = new WebAppContext("webapp/", serverSettings.contextPath);

    Context resCtx = new Context();
    resCtx.setContextPath("/jppdata");
    resCtx.setResourceBase(serverSettings.jppDataDir);
    resCtx.addServlet(DefaultServlet.class, "/");
    //ctx.setResourceBase(serverSettings.jppDataDir);
    

    server = new Server(serverSettings.port);
    server.addHandler(mainCtx);
    server.addHandler(resCtx);
    try {
      server.start();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void stopServer() {
    try {
      server.stop();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static ServerSettings startGUI() {

    /* Look and Feel setzten */
    try {
      UIManager.setLookAndFeel(new com.lipstikLF.LipstikLookAndFeel());
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Laed die abgespeicherten Einstellungen
    ServerSettings serverSettings = SettingsManager
        .getSettings(ServerSettings.class);


    ServerStartDialog dialog = new ServerStartDialog(new javax.swing.JFrame(),
        true);
    dialog.setServerSettings(serverSettings);


    dialog.setVisible(true);

    if (dialog.isOk()) {
      serverSettings = dialog.getServerSettings();
      SettingsManager.saveSettings(serverSettings);
      return serverSettings;
    }

    return null;

  }

  private static boolean containsValue(String[] args, int index, String name) {
    return args[index].equals("-" + name) && args.length > index + 1;
  }


  /**
   * Laed die gespeicherten Werte und überschreibt diese mit den in args
   * übergebenen Werten und speichert die neuen wieder ab.
   * 
   * @return die serverSettings
   */
  private static ServerSettings settingsFromArgs(String[] args) {
    
    // Laed die abgespeicherten Einstellungen
    ServerSettings serverSettings = SettingsManager
        .getSettings(ServerSettings.class);

    for (int i = 0; i < args.length; i++) {

      if (containsValue(args, i, "port")) {
        try {
          serverSettings.port = Integer.parseInt(args[i + 1]);
        } catch (NumberFormatException e) {
          logger
              .warning("Der angegebene Port kann nicht in eine Zahl umgewandelt werden.");
        }
      } else if (containsValue(args, i, "contextPath")) {
        serverSettings.contextPath = args[i + 1];
      } else if (containsValue(args, i, "jppDataDir")) {
        serverSettings.jppDataDir = args[i + 1];
      } else if (containsValue(args, i, "jppDataURL")) {
        serverSettings.jppDataURL = args[i + 1];
      }

    }

    SettingsManager.saveSettings(serverSettings);

    return serverSettings;
  }


  /**
   * Startet den Jetty-Webserver mit der JPP-Webapplikation.
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) {
    SettingsManager.open(true);
    ServerSettings serverSettings = null;

    boolean startGui = false;
    boolean showHelp = false;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-gui")) {
        startGui = true;
        break;
      } else if (args[i].equals("--help")) {
        showHelp = true;
        break;
      }
    }

    if (showHelp) {
      System.out.println("JPPServer\n\n Parameter:");
      System.out.println("-gui\t\tStartet eine grafische Benutzeroberfläche zum setzten einiger Parameter");
      System.out.println("");
      System.out.println("-port\t\tPort auf den dieser Server lauschen soll");
      System.out.println("-contextPath\tPfad unter der diese Webapplikation zu finden ist");
      System.out.println("-jppDataDir\tVerzeichnis in dem die JPP-Daten liegen sollen (Bilder, Thumbnails und Index)");
      System.out.println("-jppDataURL\tURL unter der die Bilder zu finden sind.");
      
    } else {
      if (startGui) {
        serverSettings = startGUI();
      } else {
        serverSettings = settingsFromArgs(args);
      }
    }
    // TODO Parameter aus args lesen und in den Settings speichern.
    // serverSettings =
    // SettingsManager.getSettings(ServerSettings.class);

    if (serverSettings != null) {
      ServerStarter st = new ServerStarter();
      st.startServer(serverSettings);
      
      System.out.print("\ntype \"exit\" to stop this server> ");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      try {
        while (!"exit".equals(reader.readLine())) {
        }
      } catch (IOException e) {
        logger.log(Level.INFO, "Ein-/Ausgabefehler", e);
      } finally {
        st.stopServer();
        SettingsManager.close();
      }
      
    }

  }

}
