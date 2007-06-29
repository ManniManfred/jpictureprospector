package selectionmanager.ui;

import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import selectionmanager.Auswaehlbar;
import selectionmanager.AuswaehlbaresChangedListener;
import selectionmanager.Einstellungen;

/**
 * Ein Objekt dieser Klasse repraesentiert ein Selektierbares JPanel.
 * 
 * @author Manfred Rosskamp
 */
public class SelectableJPanel extends JPanel implements Auswaehlbar {



  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = -6966084054057549946L;


  /**
   * Der Listener, der Informiert werden muss, wenn sich was geaendert hat.
   */
  private AuswaehlbaresChangedListener sListener;

  /**
   * Ist <code>true</code>, wenn diese JPanel ausgewaehlt ist.
   */
  private boolean istAusgewaehlt;

  /**
   * Ist <code>true</code>, wenn dieses JPanel angeklickt ist.
   */
  private boolean istAngeklickt;

  /**
   * Erzeugt ein neues SelectableJPanel.
   */
  public SelectableJPanel() {
    init();
  }

  /**
   * Erzeugt ein neues SelectableJPanel.
   */
  public SelectableJPanel(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
    init();
  }

  /**
   * Erzeugt ein neues SelectableJPanel.
   */
  public SelectableJPanel(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
    init();
  }

  /**
   * Erzeugt ein neues SelectableJPanel.
   */
  public SelectableJPanel(LayoutManager layout) {
    super(layout);
    init();
  }

  /**
   * Initialiesiert dieses SelectableJPanel.
   */
  private void init() {
    this.setBackground(Einstellungen.farbeNormal);
    setBorder(new EmptyBorder(Einstellungen.rahmenDicke,
        Einstellungen.rahmenDicke, Einstellungen.rahmenDicke,
        Einstellungen.rahmenDicke));
    
    /* Das JPanel ist am Anfag weder ausgewaehlt noch angeklickt. */
    istAusgewaehlt = false;
    istAngeklickt = false;

    /* MouseListener hinzufuegen */
    this.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {

        /* Wenn beim Klicken die CRTL-Taste gedrueckt wurde */
        // if (e.isControlDown()) {
        /* dann ist dieser JPanel ausgewaehlt */
        setAusgewaehlt(!istAusgewaehlt);
        fireWurdeAusgewaehlt();
        // }

        /* Dieses JPanel wurde angeklickt */
        setMarkiert(true);
        fireWurdeAngeklickt();
      }
    });
  }




  /**
   * Gibt zurueck, ob dieses JPanel angeklickt oder abgeklickt ist
   * 
   * @return <code>true</code>, wenn dieses JPanel angeklickt ist
   */
  public boolean istMarkiert() {
    return istAngeklickt;
  }

  /**
   * Setzt dieses JPanel als Angeklickt, d.h es wird ein Rahmen um dies JPanel
   * erzeugt.
   * 
   * @param istAngeklickt gibt an, ob dieses JPanel angeklickt oder abgeklickt
   *          werden soll
   */
  public void setMarkiert(boolean isAngeklickt) {
    this.istAngeklickt = isAngeklickt;

    if (istAngeklickt) {
      setBorder(new DottedLineBorder(Einstellungen.rahmenFarbe,
          Einstellungen.rahmenDicke, false));
    } else {
      setBorder(new EmptyBorder(Einstellungen.rahmenDicke,
          Einstellungen.rahmenDicke, Einstellungen.rahmenDicke,
          Einstellungen.rahmenDicke));
    }

  }




  /**
   * Gibt <code>true</code> zurueck, wenn dieses JPanel angeklickt ist.
   * 
   * @return <code>true</code>, wenn dieses JPanel angeklickt ist.
   */
  public boolean istAusgewaehlt() {
    return istAusgewaehlt;
  }

  /**
   * Setzt dieses JPanel als angeklickt, d.h. es wird eine Hintergrundfarbe
   * gesetzt.
   * 
   * @param istAusgewaehlt
   */
  public void setAusgewaehlt(boolean istAusgewaehlt) {
    this.istAusgewaehlt = istAusgewaehlt;
    this.setBackground(istAusgewaehlt
        ? Einstellungen.farbeAusgewaehlt
        : Einstellungen.farbeNormal);
  }




  /**
   * Informiert den Listener darueber, dass dieser JPanel ausgewaehlt wurde.
   */
  protected void fireWurdeAusgewaehlt() {
    sListener.wurdeAusgewaehlt(this);
  }

  /**
   * Informiert den Listener darueber, dass dieser JPanel angeklickt wurde.
   */
  protected void fireWurdeAngeklickt() {
    sListener.wurdeMarkiert(this);
  }

  /**
   * Setzt den Listener, der ueber Aenderungen informiert werden muss.
   * 
   * @param l Listener, der ueber Aenderungen informiert werden muss
   */
  public void setAuswaehlbaresChangedListener(AuswaehlbaresChangedListener l) {
    this.sListener = l;
  }
}
