package jpp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import jpp.core.AbstractJPPCore;
import jpp.core.Einstellungen;
import jpp.core.Trefferliste;
import jpp.core.exceptions.SucheException;
import jpp.merkmale.AlbumMerkmal;
import jpp.ui.listener.AlbumListener;

/**
 * Ein Objekt der Klasse repraesentiert eine Ansicht in der alle
 * Alben des Lucene-Index dargestellt werden.
 */
public class AlbumPanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = -6914366654217158447L;

  /** Enthaelt den Kern der Anwendung mit dem Aktionen durchgefuehrt
   * werden.
   */
  private AbstractJPPCore core;

  /**
   * Enthaelt die Liste der verfuegbaren Alben:
   */
  private JList anzeigeListe;

  /**
   * Enthaelt alle Listener die bei Events benachrichtigt werden sollen.
   */
  private List<AlbumListener> listener;

  /**
   * Erzeugt ein neues Objekt der Klasse.
   * @param core  Enthaelt den Kern auf dem operiert werden soll.
   */
  public AlbumPanel(AbstractJPPCore core) {
    setVisible(true);
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(100, 200));
    this.core = core;
    this.listener = new ArrayList<AlbumListener>();
    initUI();
  }
  
  /**
   * Erneuert die Listenansicht des Objekt.
   */
  public void refreshUI() {
    setVisible(false);
    this.removeAll();
    revalidate();
    initUI();
    initAnzeigeListe();
    setVisible(true);
  }

  /**
   * Initialisiert die grafische Ansicht des Containers.
   */
  private void initUI() {
    JPanel pNorth = new JPanel();
    pNorth.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel label = new JLabel(new ImageIcon(getClass()
        .getResource("/jpp/ui/uiimgs/folder_photos.png")));
    pNorth.add(label);
    label = new JLabel("ALBEN");
    label.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
    pNorth.add(label);
    this.add(pNorth, BorderLayout.NORTH);
    initAnzeigeListe();
  }

  /**
   * Initialisiert die Liste mit den Alben, die angezeigt werden sollen.
   */
  private void initAnzeigeListe() {
    
    anzeigeListe = new JList();
    anzeigeListe.setModel(new DefaultListModel());
    anzeigeListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    anzeigeListe.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
    anzeigeListe.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        /* Bei Doppelklick soll im Kern gesucht werden */
        if (e.getClickCount() == 2) {
          if (anzeigeListe.getSelectedIndex() >= 0) {
            fireAlbumAusgewaehlt();
          }
        }
      }
    });
    /* Initiales generieren aller Alben. Im Kern werden alle Dokumente
     * nach Alben durchsucht und diese hinzugefuegt.
     */
    try {
      Trefferliste t = core.suche(Einstellungen.ALLEBILDER_SCHLUESSEL);
      for (int i = 0; i < t.getGesamtAnzahlTreffer(); i++) {
        String name = (String) t.getBildDokument(i).getMerkmal(
            AlbumMerkmal.FELDNAME).getWert();
        addAlbum(name);
      }
    } catch (SucheException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler",
          JOptionPane.ERROR_MESSAGE);
    }
    this.add(anzeigeListe, BorderLayout.CENTER);
  }

  /**
   * Fuegt einen Albumnamen dem Modell der Liste hinzu, wenn dieses
   * noch nicht vorhanden ist.
   * @param name  der Name des Albums
   */
  private void addAlbum(String name) {
    DefaultListModel model = (DefaultListModel) anzeigeListe.getModel();
    if (!model.contains(name)) {
      model.addElement(name);
    }
  }

  /**
   * Benachrichtigt alle Observer dieses Objekts.
   */
  private void fireAlbumAusgewaehlt() {
    String albumName = (String) anzeigeListe.getSelectedValue();
    for (AlbumListener listener : this.listener) {
      listener.albumSucheDurchgefuehrt(albumName);
    }
  }

  public void addAlbumListener(AlbumListener listener) {
    this.listener.add(listener);
  }

  public void removeAlbumListener(AlbumListener listener) {
    if (this.listener.contains(listener)) {
      this.listener.remove(listener);
    }
  }
}
