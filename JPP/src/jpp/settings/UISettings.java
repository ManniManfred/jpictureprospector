package jpp.settings;

import settingsystem.annotations.Property;

public class UISettings {

  
  /******************* Einstellungen fuer die Suche ****************/

  /** Maximale Anzahl angezeigter Such-Ergebnisse */
//  @Property(name = "Anzahl Ergebnisse",
//      desc = "Gibt die Anzahl der Suchergebnisse an",
//      constraint = "[1;100]",
//      group = "Suche")
//  public int ANZAHL_ERGEBNISSE = 20;
  
  @Property(name = "Schluesselwort",
      desc = "Schuesselwort, welches bei der Suche eingegeben werden "
        + "kann, um alle Bilder als Treffer zu erhalten.",
      group = "Suche")
  public String ALLEBILDER_SCHLUESSEL = "ALLEBILDER"; 
  
  
  
  

  /******************* Einstellungen fuer den Slider ****************/

  @Property(name = "Minimale Groesse",
      desc = "Minmalwert fuer den Slider und somit der Minmalwert"
    + " fuer die Thumbnail-Anzeige",
      constraint = "[0;]",
      group = "Thumbnail.Anzeige")
  public int SLIDER_MIN = 48;
  
  @Property(name = "Standard Groesse",
      desc = "Minmalwert fuer den Slider und somit der Minmalwert"
    + " fuer die Thumbnail-Anzeige",
    constraint = "[0;]",
    group = "Thumbnail.Anzeige")
  public int SLIDER_VALUE = 150;
  
  @Property(name = "Maximale Groesse",
      desc = "Maximalwert fuer den Slider und somit der Maximalwert"
    + " fuer die Thumbnail-Anzeige",
      constraint = "[1;]",
      group = "Thumbnail.Anzeige")
  public int SLIDER_MAX = 256;
  
  

  @Property(name = "Groesse speichern",
      desc = "Gibt an, ob die aktuell am Schiebebalken ausgewaehlte "
          + "Groesse gespeichert werden soll.",
      group = "Thumbnail.Anzeige")
  public boolean SAVE_THUMB_SIZE = true;
  
  
  @Property(desc = "Speichert den Wert des Sliders "
          + "bzw. die Groesse des Thumbnail.",
          visibility = false)
  public int THUMB_SIZE = SLIDER_VALUE;
  
  
  /****************** Haupt-Fenster Einstellungen *****************/
  
  @Property(desc = "Hoehe auf die das Hauptfenster beim starten gesetzt wird",
      visibility = false)
  public int FENSTER_HOEHE = 700;
  
  @Property(desc = "Breite auf die das Hauptfenster beim starten gesetzt wird",
      visibility = false)
  public int FENSTER_BREITE = 1000;
  
  @Property(desc = "x-Position, an der das Hauptfenster angezeigt wird",
      visibility = false)
  public int FENSTER_POSX = 0;

  @Property(desc = "y-Position, an der das Hauptfenster angezeigt wird",
      visibility = false)
  public int FENSTER_POSY = 0;
  
  @Property(name = "Speichere Fenstergroesse", 
      desc = "Gibt an, ob die Fenstergroesse beim beenden gespeichert werden"
        + " werden soll.",
        group = "Fenster")
  public boolean SAVE_FENSTER_GROESSE = true;
  
  @Property(name = "Speichere Fensterposition", 
      desc = "Gibt an, ob die Fensterposition beim beenden gespeichert werden"
        + " werden soll.",
        group = "Fenster")
  public boolean SAVE_FENSTER_POSITION = true;
  
  
  
  /************  Einstellung zum merken der Pfadangabe *******************/
  
  @Property(visibility = false, desc = "Ordner der zuerst bei der Auswahl" +
      "von zu importierenden Bildern angezeigt werden soll.")
  public String importStartOrdner = ""; //System.getProperty("user.home");
  
  
}
