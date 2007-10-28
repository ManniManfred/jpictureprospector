package jpp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class JPPServer {
  

  public JPPServer() throws IOException {
    System.out.print("Starte JPPServer ...");
    ServerSocket server = new ServerSocket(4567);
    System.out.println("  fertig.");
    
    while (true) {
      try {
        Verbindung v = new Verbindung(server.accept());
        v.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws IOException {
    Logger.getLogger("").getHandlers()[0].setFormatter(new Formatter() {
      @Override
      public String format(LogRecord rec) {
        String ergebnis = "";
        ergebnis = rec.getLoggerName() + " ";
        ergebnis += rec.getLevel().getName() + ": " + rec.getMessage() + "\n";
        return ergebnis;
      }
    });
    
    new JPPServer();
  }
}
