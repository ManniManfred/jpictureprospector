package jpp.webapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import jpp.core.BildDokument;
import jpp.core.Trefferliste;
import jpp.merkmale.DateipfadMerkmal;
import jpp.merkmale.Merkmal;
import jpp.merkmale.ThumbnailMerkmal;
import jpp.settings.CoreSettings;
import jpp.settings.ServerSettings;
import jpp.settings.SettingsManager;
import jpp.webapp.servlets.ThumbnailManager;

public class TrefferlisteParser {

  /**
   * Enthaelt den Index der zu verwendenen Größe. Wenn dieser index ungültig
   * ist, wird das orginale Bild verwendet.
   */
  public static int sizeIndex = -1;

  /**
   * Enthaelt das CoreSettings Objekt mit allen wichtigen Kern Einstellungen.
   */
  private static CoreSettings coreSettings = SettingsManager
      .getSettings(CoreSettings.class);

  /**
   * Enthaelt das ServerSettings Objekt mit allen wichtigen serverSettings
   * dieser Anwendung.
   */
  private static ServerSettings serverSettings = SettingsManager
      .getSettings(ServerSettings.class);

  /**
   * Gibt ein XML-Dokument der uebergebenen Trefferliste zurueck.
   * 
   * @param liste Trefferliste, aus der das XML-Dokument erzeugt wird.
   * @return XML-Dokument der Trefferliste
   */
  public static String getTrefferlisteDok(Trefferliste liste) {
    String ergebnis = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    // ergebnis +=
    // "<?xml-stylesheet type=\"text/xsl\" href=\"trefferDok.xsl\" ?>";
    ergebnis += trefferlisteToXml(liste);

    return ergebnis;
  }


  private static String trefferlisteToXml(Trefferliste liste) {

    String ergebnis = "<Trefferliste Anzahl=\""
        + liste.getGesamtAnzahlTreffer() + "\">\n";

    for (int i = 0; i < liste.getAnzahlTreffer(); i++) {
      ergebnis += bildDokumentToXml(liste.getBildDokument(i));
    }

    ergebnis += "</Trefferliste>";
    return ergebnis;
  }


  private static String bildDokumentToXml(BildDokument dok) {
    String ergebnis = "<BildDokument>\n";

    for (Merkmal m : dok.gibGrundMerkmale()) {
      ergebnis += merkmalToXml(m, dok);
    }

    ergebnis += "</BildDokument>\n";
    return ergebnis;
  }


  private static String merkmalToXml(Merkmal m, BildDokument dok) {
    return "<" + m.getClass().getName() + ">" + getXmlMerkmalsWert(m, dok)
        + "</" + m.getClass().getName() + ">\n";
  }

  private static String getXmlMerkmalsWert(Merkmal m, BildDokument dok) {
    String ergebnis;

    try {
      if (m instanceof DateipfadMerkmal) {
        if (sizeIndex >= 0 && sizeIndex < ThumbnailManager.groessen.length) {
          File thumbFile = new File(ThumbnailManager.getThumbnailFile(
              (URL) m.getWert(),
              ThumbnailManager.groessen[sizeIndex]));
          ergebnis = Mapping.wandleInWWW(thumbFile.toURL()).toString();
        } else {
          ergebnis = Mapping.wandleInWWW((URL) m.getWert()).toString();
        }
      } else if (m instanceof ThumbnailMerkmal) {

        File thumbFile = new File(ThumbnailManager.getThumbnailFile((URL) dok
            .getMerkmal(DateipfadMerkmal.FELDNAME).getWert(),
            ThumbnailManager.groessen[0]));


        ergebnis = Mapping.wandleInWWW(thumbFile.toURL()).toString();
      } else {
        ergebnis = m.getWert().toString();
      }
    } catch (MalformedURLException e1) {
      ergebnis = "Fehler: " + e1;
    }

    return ergebnis;
  }
}
