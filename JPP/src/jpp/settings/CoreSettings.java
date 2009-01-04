package jpp.settings;

import javax.imageio.ImageIO;

import settingsystem.annotations.Property;
import settingsystem.core.Auswahl;

public class CoreSettings {

  
  @Property(visibility = false,
      desc = "Enthaelt alle Klassennamen der zu verwendenen Merkmale."  +
          " Diese Einstellung sollte nur dann veraendert werden, wenn es " +
          "noch keinen Lucene Index gibt bzw. wenn noch keine Bilder importiert" +
          "wurden. Ansonsten kann es auf Grund der Unstimmigkeiten zwischen " +
          "dem Programm und dem Lucene-Index zu schwerwiegenden Fehlern kommen." +
          " Zudem sind fuer das Programm die Merkmale" +
          " Dateiname, Dateipfad, Thumbnail, Bildbreite, Bildhoehe," +
          " SchuesselWoerter und Beschreibung" +
          " unbedingt notwendig.")
  public String[] MERKMALE = new String[] {
    "jpp.merkmale.BeschreibungMerkmal",
    "jpp.merkmale.BildbreiteMerkmal",
    "jpp.merkmale.BildhoeheMerkmal",
    "jpp.merkmale.BildtypMerkmal",
    "jpp.merkmale.DateigroesseMerkmal",
    "jpp.merkmale.DateipfadMerkmal",
    "jpp.merkmale.DateinameMerkmal",
    "jpp.merkmale.LetzterZugriffMerkmal",
    "jpp.merkmale.SchluesselWoerterMerkmal",
    "jpp.merkmale.ThumbnailMerkmal",
    "jpp.merkmale.OrtMerkmal",
    "jpp.merkmale.AlbumMerkmal",
    "jpp.merkmale.GruppeMerkmal"
  };
  
  
  
  /**
   * Gibt an, welche Thumbnailgenerierer bei welchen Formaten verwendeter werden
   * sollen.
   */
  @Property(visibility = false, 
      desc = "Gibt an, welche Thumbnailgenerierer bei welchen Formaten " +
          "verwendeter werden sollen.")
  public String[] THUMB_ZUORDUNGEN = new String[]{
    "jpg, gif, png = jpp.core.thumbnail.SchnellerGenerierer",
     "* = jpp.core.thumbnail.ExternalGenerierer"};
  
  
  
  
  
  

  /**
   * Enthaelt das Schuesselwort, welches bei der Suche eingegeben werden kann,
   * um alle Bilder als Treffer zu erhalten.
   */
  @Property(name = "Schluesselwort",
      desc = "Schuesselwort, welches bei der Suche eingegeben werden "
      + "kann, um alle Bilder als Treffer zu erhalten.")
  public String ALLEBILDER_SCHLUESSEL = "ALLEBILDER";

  
  
  
  
  
  

  /******************* Einstellungen fuer Thumbnails ****************/

  
  /** Maximale Hoehe des Thumbnails in Pixeln. */
  @Property(name = "Hoehe",
      desc = "Maximale Hoehe des Thumbnails in Pixeln",
      constraint = "[1;]",
      group = "Thumbnail.Erzeugung")
  public int THUMB_MAXHOEHE = 256;

  /** Maximale Breite des Thumbnails in Pixeln. */
  @Property(name = "Breite",
      desc = "Maximale Breite des Thumbnails in Pixeln",
      constraint = "[1;]",
      group = "Thumbnail.Erzeugung")
  public int THUMB_MAXBREITE = 256;

  /** Format, in dem die Thumbnails gespeichert werden. */
  @Property(name = "Format",
      desc = "Format, indem das Thumbnail erzeugt wird",
      group = "Thumbnail.Erzeugung")
  public Auswahl THUMB_FORMAT = 
    new Auswahl(ImageIO.getReaderFormatNames(), "jpg");
  
  
  
  
  
  
}
