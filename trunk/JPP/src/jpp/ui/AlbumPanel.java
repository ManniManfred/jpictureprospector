package jpp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import jpp.core.AbstractJPPCore;
import jpp.core.BildDokument;
import jpp.core.Trefferliste;
import jpp.core.exceptions.SucheException;
import jpp.merkmale.AlbumMerkmal;
import jpp.settings.CoreSettings;
import jpp.settings.SettingsManager;
import jpp.ui.listener.AlbumListener;

/**
 * Ein Objekt der Klasse repraesentiert eine Ansicht in der alle Alben des
 * Lucene-Index dargestellt werden.
 */
public class AlbumPanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = -6914366654217158447L;

  protected static CoreSettings coreSettings = 
    SettingsManager.getSettings(CoreSettings.class);
  
  /**
   * Enthaelt den Kern der Anwendung mit dem Aktionen durchgefuehrt werden.
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
   * 
   * @param core
   *          Enthaelt den Kern auf dem operiert werden soll.
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
    JLabel label = new JLabel(new ImageIcon(getClass().getResource(
        "/jpp/ui/uiimgs/folder_photos.png")));
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
    anzeigeListe.setCellRenderer(new AlbumPanelCellRenderer());
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
    nimmAlbenAuf();
    this.add(anzeigeListe, BorderLayout.CENTER);
  }

  /**
   * Nimmt alle verfuegbaren Alben in die Anzeigeliste auf. Sollte nur dann
   * ausgefuehrt werden wenn die Anzeigeliste neu initialisiert wird, da eine
   * zusaetzliche Suche im Kern durchgefuhert wird.
   */
  private void nimmAlbenAuf() {

    DefaultListModel model = (DefaultListModel) anzeigeListe.getModel();
    
    List<String> alben = core.getAlben();
    
    for (String album : alben) {
      model.addElement(album);
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

class AlbumPanelCellRenderer extends DefaultListCellRenderer {

  /**
   * 
   */
  private static final long serialVersionUID = -2748845393496213894L;

  @Override
  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {
    if (isSelected || cellHasFocus) {
      setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
          Color.GREEN, Color.GRAY));
      setBackground(Color.BLUE);
    } else {
      setBackground(Color.WHITE);
    }
    repaint();
    return super.getListCellRendererComponent(list, value, index, isSelected,
        cellHasFocus);
  }

}
