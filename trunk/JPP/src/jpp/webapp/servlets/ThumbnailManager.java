package jpp.webapp.servlets;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import jpp.core.GeoeffnetesBild;
import jpp.core.exceptions.GeneriereException;
import jpp.core.thumbnail.SimpleThumbnailGeneriererFactory;
import jpp.core.thumbnail.ThumbnailGenerierer;
import jpp.merkmale.DateipfadMerkmal;
import jpp.settings.CoreSettings;
import jpp.settings.ServerSettings;
import jpp.settings.SettingsManager;
import jpp.webapp.Mapping;

public class ThumbnailManager {

  public static Dimension[] groessen = new Dimension[] {
      new Dimension(200, 200),
      new Dimension(800, 600),
      new Dimension(1024, 768) };

  /** Logger, der alle Fehler loggt. */
  private static Logger logger = Logger
      .getLogger("jpp.webapps.servlets.ThumbnailManager");


  public static String getThumbnailFile(URL origFile, Dimension size) {
    ServerSettings serverSettings = SettingsManager
        .getSettings(ServerSettings.class);

    CoreSettings coreSettings = SettingsManager.getSettings(CoreSettings.class);

    // URL url = Mapping.wandleInWWW(origFile);

    File thumbFolder = new File(ServerSettings.concatFolder(serverSettings
        .getThumbnailOrdner(), size.width + "x" + size.height));

    if (!(thumbFolder.exists() && thumbFolder.isDirectory())) {
      thumbFolder.mkdirs();
    }


    //+ f.getPath().replaceAll("/", "_").replaceAll("\\", "_").replaceAll(":", "-")
    String thumbName = "th_" + origFile.getPath()
        + "." + coreSettings.THUMB_FORMAT.getAusgewaehlt();

    thumbName = Mapping.replace(thumbName, "/", "_");
    thumbName = Mapping.replace(thumbName, "\\", "_");
    thumbName = Mapping.replace(thumbName, ":", "-");
    
    String thumbFile = ServerSettings.concatFolder(thumbFolder.getPath(),
        thumbName);

    return thumbFile;
  }

  /**
   * Erstelle aus dem uebergebenem Bild Thumbnails in verschieden Groessen.
   * 
   * @param file
   */
  public static void generateThumbnailsFor(File file) {

    ServerSettings serverSettings = SettingsManager
        .getSettings(ServerSettings.class);

    CoreSettings coreSettings = SettingsManager.getSettings(CoreSettings.class);


    GeoeffnetesBild bild;
    try {
      bild = new GeoeffnetesBild(file.toURL());
      ThumbnailGenerierer generierer = SimpleThumbnailGeneriererFactory
          .erzeugeThumbnailGenerierer(bild);

      try {
        for (int i = 0; i < groessen.length; i++) {
          String thumbFile = getThumbnailFile(file.toURL(), groessen[i]);
          OutputStream thumbOut = new FileOutputStream(thumbFile);

          BufferedImage image = generierer.generiereThumbnail(bild,
              groessen[i].width, groessen[i].height);

          ImageIO.write(image, coreSettings.THUMB_FORMAT.getAusgewaehlt()
              .toString(), thumbOut);
          thumbOut.close();
        }
      } catch (GeneriereException e) {
        logger.log(Level.WARNING, "Abbilder der konnten nicht erzeugt werden.",
            e);
      } catch (IOException e) {
        logger.log(Level.WARNING, "Abbilder der konnten nicht erzeugt werden.",
            e);
      }
    } catch (MalformedURLException e) {
      logger
          .log(Level.WARNING, "Abbilder der konnten nicht erzeugt werden.", e);
    } catch (IOException e) {
      logger
          .log(Level.WARNING, "Abbilder der konnten nicht erzeugt werden.", e);
    }
  }
}
