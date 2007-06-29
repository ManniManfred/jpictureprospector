package selectionmanager;

/**
 * Ein Objekt welches dieses Interface implementiert, gilt als Selektierbar.
 * 
 * @author Manfred Rosskamp
 */
public interface Auswaehlbar {

  /**
   * Waehlt das Auswaehlbare aus, wenn <code>istAusgewaehlt</code> ==
   * <code>true</code>. Im anderen Fall wird es abgewaehlt.
   * 
   * @param istAusgewaehlt ist true, wenn das Auswaehlbare ausgewaehlt werden
   *          soll
   */
  void setAusgewaehlt(boolean istAusgewaehlt);

  /**
   * Gibt zurueck, ob das Auswaehlbare ausgewaehlt ist.
   * 
   * @return <code>true</code>, wenn das Auswaehlbare ausgewaehlt ist
   */
  boolean istAusgewaehlt();



  /**
   * Setzt den Listener, der informiert werden möchte, wenn das Auswaehlbare
   * Objekt selektiert/ausgewaehlt worden ist.
   * 
   * @param l Listener, der informiert werden möchte, wenn das Selektierbar
   *          Objekt selektiert/ausgewaehlt worden ist
   */
  void setAuswaehlbaresChangedListener(AuswaehlbaresChangedListener l);


  /**
   * Gibt <code>true</code> zurueck, wenn das Auswaehlbare markiert/angeklickt
   * ist.
   * 
   * @return <code>true</code> zurueck, wenn das Auswaehlbare
   *         markiert/angeklickt ist
   */
  public boolean istMarkiert();

  /**
   * Markiert das Auswaehlbare Objekt oder nicht.
   * 
   * @param markiert ist <code>true</code>, wenn dies auswaehlbare Objekt
   *          markiert werden soll
   */
  public void setMarkiert(boolean markiert);



  /**
   * Gibt die x-Position dieses auswaehlbaren Objekts zurueck.
   * 
   * @return x-Position dieses auswaehlbaren Objekts
   */
  public int getX();

  /**
   * Gibt die x-Position dieses auswaehlbaren Objekts zurueck.
   * 
   * @return x-Position dieses auswaehlbaren Objekts
   */
  public int getY();

  /**
   * Gibt die Breite dieses auswaehlbaren Objekts zurueck.
   * 
   * @return Breite dieses auswaehlbaren Objekts
   */
  public int getWidth();

  /**
   * Gibt die Hoehe dieses auswaehlbaren Objekts zurueck.
   * 
   * @return Hoehe dieses auswaehlbaren Objekts
   */
  public int getHeight();
}
