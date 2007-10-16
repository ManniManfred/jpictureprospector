package jpp.core;

import javax.imageio.ImageIO;

import settingsystem.annotations.Property;
import settingsystem.core.Auswahl;

/**
 * Diese Klasse enthaelt globale Einstellungen zu diesem JPictureProspector.
 * 
 * @author Manfred Rosskamp
 */
public class Einstellungen {


  /**** Nicht veraenderbare Einstellungen ****/
  /**
   * Enthält den Dateinamen zu der Datei, in der die Einstellungen
   * gespeichert werden. 
   */
  public static final String SETTING_FILENAME = "settings.ini";
  
  /**
   * Enthaelt den Pfad zur Merkmalsdatei, in der eine Liste mit allen zu
   * verwendenen Merkmalen steht.
   */
  public static final String MERKMAL_DATEI = "merkmale";

  /**
   * Die Datei, in der die fuer die Formate entsprechenden Thumbnailgenerierer
   * stehen.
   */
  public static final String THUMB_ZUORDUNGSDATEI = "thumbnailGenerierer";
  
  
  /**** Veraenderbare Einstellungen ****/
  
  
  /**
   * Enthaelt das Schuesselwort, welches bei der Suche eingegeben werden kann,
   * um alle Bilder als Treffer zu erhalten.
   */
  @Property(name = "Schluesselwort",
      desc = "Schuesselwort, welches bei der Suche eingegeben werden "
      + "kann, um alle Bilder als Treffer zu erhalten.")
  public static String ALLEBILDER_SCHLUESSEL = "ALLEBILDER";

  
  /******************* Einstellungen fuer die Suche ****************/

  /** Maximale Anzahl angezeigter Such-Ergebnisse */
  @Property(name = "Anzahl Ergebnisse",
      desc = "Gibt die Anzahl der Suchergebnisse an",
      constraint = "[1;100]",
      group = "Suche")
  public static int ANZAHL_ERGEBNISSE = 20;
  
  
  /******************* Einstellungen fuer Thumbnails ****************/


  
  /** Maximale Hoehe des Thumbnails in Pixeln. */
  @Property(name = "Hoehe",
      desc = "Maximale Hoehe des Thumbnails in Pixeln",
      constraint = "[1;]",
      group = "Thumbnail.Erzeugung")
  public static int THUMB_MAXHOEHE = 256;

  /** Maximale Breite des Thumbnails in Pixeln. */
  @Property(name = "Breite",
      desc = "Maximale Breite des Thumbnails in Pixeln",
      constraint = "[1;]",
      group = "Thumbnail.Erzeugung")
  public static int THUMB_MAXBREITE = 256;

  /** Format, in dem die Thumbnails gespeichert werden. */
  @Property(name = "Format",
      desc = "Format, indem das Thumbnail erzeugt wird",
      group = "Thumbnail.Erzeugung")
  public static Auswahl THUMB_FORMAT = 
    new Auswahl(ImageIO.getReaderFormatNames(), "jpg");


  /******************* Einstellungen fuer den Slider ****************/

  @Property(name = "Minimale Groesse",
      desc = "Minmalwert fuer den Slider und somit der Minmalwert"
    + " fuer die Thumbnail-Anzeige",
      constraint = "[0;]",
      group = "Thumbnail.Anzeige")
  public static int SLIDER_MIN = 48;
  
  @Property(name = "Standard Groesse",
      desc = "Minmalwert fuer den Slider und somit der Minmalwert"
    + " fuer die Thumbnail-Anzeige",
    constraint = "[0;]",
    group = "Thumbnail.Anzeige")
  public static int SLIDER_VALUE = 150;
  
  @Property(name = "Maximale Groesse",
      desc = "Maximalwert fuer den Slider und somit der Maximalwert"
    + " fuer die Thumbnail-Anzeige",
      constraint = "[1;]",
      group = "Thumbnail.Anzeige")
  public static int SLIDER_MAX = 256;
  
  

  @Property(name = "Groesse speichern",
      desc = "Gibt an, ob die aktuell am Schiebebalken ausgewaehlte "
          + "Groesse gespeichert werden soll.",
      group = "Thumbnail.Anzeige")
  public static boolean SAVE_THUMB_SIZE = true;
  
  
  @Property(desc = "Speichert den Wert des Sliders "
          + "bzw. die Groesse des Thumbnail.",
          visibility = false)
  public static int THUMB_SIZE = SLIDER_VALUE;
  
  
  /****************** Haupt-Fenster Einstellungen *****************/
  
  @Property(desc = "Hoehe auf die das Hauptfenster beim starten gesetzt wird",
      visibility = false)
  public static int FENSTER_HOEHE = 700;
  
  @Property(desc = "Breite auf die das Hauptfenster beim starten gesetzt wird",
      visibility = false)
  public static int FENSTER_BREITE = 1000;
  
  @Property(desc = "x-Position, an der das Hauptfenster angezeigt wird",
      visibility = false)
  public static int FENSTER_POSX = 0;

  @Property(desc = "y-Position, an der das Hauptfenster angezeigt wird",
      visibility = false)
  public static int FENSTER_POSY = 0;
  
  @Property(name = "Speichere Fenstergroesse", 
      desc = "Gibt an, ob die Fenstergroesse beim beenden gespeichert werden"
        + " werden soll.",
        group = "Fenster")
  public static boolean SAVE_FENSTER_GROESSE = true;
  
  @Property(name = "Speichere Fensterposition", 
      desc = "Gibt an, ob die Fensterposition beim beenden gespeichert werden"
        + " werden soll.",
        group = "Fenster")
  public static boolean SAVE_FENSTER_POSITION = true;
  
  
  
  /************  Einstellung zum merken der Pfadangabe *******************/
  
  @Property(visibility = false)
  public static String importStartOrdner = System.getProperty("user.home");
  
  
  
}
