package core;

/**
 * Diese Klasse enthaelt globale Einstellungen zu diesem JPictureProspector.
 * @author Manfred Rosskamp
 */
public class Einstellungen {


  /**
   * Enthaelt den Pfad zur Merkmalsdatei, in der eine Liste mit allen
   * zu verwendenen Merkmalen steht.
   */
  public static final String MERKMAL_DATEI = "merkmale";
  
  
  
  /******************* Einstellungen fuer Thumbnails ****************/
  
  
  /** Maximale Hoehe des Thumbnails in Pixeln. */
  public static final int THUMB_MAXHOEHE = 256;

  /** Maximale Breite des Thumbnails in Pixeln. */
  public static final int THUMB_MAXBREITE = 256;

  /** Format, in dem die Thumbnails gespeichert werden. */
  public static final String THUMB_FORMAT = "jpeg";

  /** 
   * Die Datei, in der die fuer die Formate entsprechenden Thumbnailgenerierer
   * stehen.
   */
  public static final String THUMB_ZUORDUNGSDATEI = "thumbnailGenerierer";

}
