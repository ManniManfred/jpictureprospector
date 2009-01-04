package jpp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumn;

import jpp.core.BildDokument;
import jpp.merkmale.BildbreiteMerkmal;
import jpp.merkmale.BildhoeheMerkmal;
import jpp.ui.listener.BildGeladenListener;

public class BildGroszanzeige extends JFrame {

  /**
   * Generated uid.
   */
  private static final long serialVersionUID = 8618593288685991915L;

  /** Enthaelt den Wert fuer die Spalte der Exif-Bezeichnungen. */
  private static final int KEYSPALTE = 0;

  /** Enthaelt den Wert fuer die Spalte der Exif-Werte. */
  private static final int WERTSPALTE = 1;

 
  /**
   * Enthaelt das BildDokument zu dem die Informationen angezeigt werden sollen.
   */
  private BildDokument dok = null;

  /**
   * Enthaelt die Information um die preferedSize des Zeichnungsbereichs zu
   * setzen.
   */
  private Dimension prefSizeImage = null;

  /**
   * Enthaehlt das Tabellenmodell fuer die Tabelle in denen Zusatzdetails
   * angezeigt werden.
   */
  private BGATabellenModell detailsTableModel = null;

  private List<ThumbnailAnzeigePanel> listeAnzeigePanel = null;

  private JPanel cpInhalt = null;

  private JPanel pToolbar = null;

  private JLabel lLetztesBild = null;

  private JLabel lNaechstesBild = null;

  private JButton bSchlieszen = null;

  private JTabbedPane tpGroszanzeige = null;

  private BildGroszanzeigeZeichner pGroszanzeige = null;

  private JScrollPane spZusatzdetails = null;

  private JTable tZusatzdetails = null;

  private JToggleButton tbGroeszeAnpassen = null;

  private JComboBox cbGroesze = null;

  private JScrollPane spGroszanzeige = null;

  /**
   * This is the default constructor
   */
  public BildGroszanzeige(List<ThumbnailAnzeigePanel> taps, BildDokument dok) {
    super();
    this.listeAnzeigePanel = taps;
    this.dok = dok;
    this.prefSizeImage = new Dimension();
    initialize();
  }

  /**
   * Liefert das Tabellenmodell der Tabelle.
   * 
   * @return das entsprechenden Tabellenmodell.
   */
  public BGATabellenModell gibTabellenModell() {
    return this.detailsTableModel;
  }

  /**
   * Die Groesze der Spaltenbreite wird automatisch angepasst an die Laenge der
   * darin enthaltenen Strings.
   */
  private void updateTable() {

    TableColumn keySpalte = tZusatzdetails.getColumnModel()
        .getColumn(KEYSPALTE);
    TableColumn wertSpalte = tZusatzdetails.getColumnModel().getColumn(
        WERTSPALTE);
    Font font = tZusatzdetails.getFont();
    FontMetrics metrics = getFontMetrics(font);
    int maxBreiteKey = 0;
    int maxBreiteWert = 0;
    for (int i = 0; i < tZusatzdetails.getRowCount(); i++) {

      if (maxBreiteKey < metrics.stringWidth((String) tZusatzdetails
          .getValueAt(i, KEYSPALTE))) {
        maxBreiteKey = metrics.stringWidth((String) tZusatzdetails.getValueAt(
            i, KEYSPALTE));
      }
      if (maxBreiteWert < metrics.stringWidth((String) tZusatzdetails
          .getValueAt(i, WERTSPALTE))) {
        maxBreiteWert = metrics.stringWidth((String) tZusatzdetails.getValueAt(
            i, WERTSPALTE));
      }
    }
    keySpalte.setPreferredWidth(maxBreiteKey + 20);
    wertSpalte.setPreferredWidth(maxBreiteWert + 20);
  }

  /**
   * Laedt die Komponten die zur Anzeige des Bildes vorhanden sind neu, so dass
   * die Ansicht entsprechend angepasst wird.
   */
  public void updatePicture() {

    JViewport viewport = spGroszanzeige.getViewport();
    prefSizeImage.width = viewport.getWidth();
    prefSizeImage.height = viewport.getHeight();
    if (!tbGroeszeAnpassen.isSelected()) {
      int skalierung = 100;
      if (cbGroesze.getSelectedItem().toString().matches("[0-9]+")) {
        try {
          skalierung = Integer.parseInt(cbGroesze.getSelectedItem().toString());
        } catch (NumberFormatException e) {
        }
      }
      prefSizeImage.width = ((Integer) dok.getMerkmal(
          BildbreiteMerkmal.FELDNAME).getWert())
          * skalierung / 100;
      prefSizeImage.height = ((Integer) dok.getMerkmal(
          BildhoeheMerkmal.FELDNAME).getWert())
          * skalierung / 100;
      if (prefSizeImage.width < viewport.getWidth()) {
        prefSizeImage.width = viewport.getWidth();
      }
      if (prefSizeImage.height < viewport.getHeight()) {
        prefSizeImage.height = viewport.getHeight();
      }
    }
    pGroszanzeige.setPreferredSize(prefSizeImage);
    spGroszanzeige.setViewportView(pGroszanzeige);
    pGroszanzeige.repaint();
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(800, 600);
    this.setLocation(100, 75);
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.setContentPane(getCpInhalt());
    this.setFocusable(true);
    this.setTitle("Gro\u00dfanzeige - JPictureProspector");
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent e) {
        updatePicture();
      }
    });
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          dispose();
        }
      }
    });
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        int unitsToScroll = e.getUnitsToScroll();
        double curScalingRate = Double.parseDouble(
            (String) cbGroesze.getSelectedItem());
        if (e.getWheelRotation() < 0) {
          cbGroesze.setSelectedItem(curScalingRate + unitsToScroll);
        } else {
          cbGroesze.setSelectedItem(curScalingRate - unitsToScroll);
        }
      }
    });
    updateTable();
    updatePicture();
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getCpInhalt() {
    if (cpInhalt == null) {
      cpInhalt = new JPanel();
      cpInhalt.setLayout(new BorderLayout());
      cpInhalt.add(getPToolbar(), BorderLayout.SOUTH);
      cpInhalt.add(getTpGroszanzeige(), BorderLayout.CENTER);
    }
    return cpInhalt;
  }

  /**
   * This method initializes pToolbar
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getPToolbar() {
    if (pToolbar == null) {
      FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 15, 15);
      flowLayout.setAlignment(java.awt.FlowLayout.CENTER);
      lNaechstesBild = new JLabel();
      lNaechstesBild.setIcon(new ImageIcon(getClass().getResource(
          "uiimgs/pfeilrechts.png")));
      lNaechstesBild.setText("");
      lNaechstesBild.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent e) {

          /*
           * Waehlt das naechste Bild in der Liste aus oder das erste wenn das
           * letzte Bild der Liste angezeigt wird
           */
          boolean neuesBildGewaehlt = false;
          for (int i = 0; i < listeAnzeigePanel.size() && !neuesBildGewaehlt; i++) {
            if (dok.equals(listeAnzeigePanel.get(i).gibBildDokument())
                && i < listeAnzeigePanel.size() - 1) {
              dok = listeAnzeigePanel.get(i + 1).gibBildDokument();
              pGroszanzeige.setzeDok(dok);
              detailsTableModel.setzeDokument(dok);
              neuesBildGewaehlt = true;
            } else if (i == listeAnzeigePanel.size() - 1) {
              dok = listeAnzeigePanel.get(0).gibBildDokument();
              pGroszanzeige.setzeDok(dok);
              detailsTableModel.setzeDokument(dok);
              neuesBildGewaehlt = true;
            }
          }
          updatePicture();
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
          lNaechstesBild.removeAll();
          lNaechstesBild.setIcon(new ImageIcon(getClass().getResource(
              "uiimgs/pfeilrechts.png")));
        }

        public void mouseEntered(java.awt.event.MouseEvent e) {
          lNaechstesBild.removeAll();
          lNaechstesBild.setIcon(new ImageIcon(getClass().getResource(
              "uiimgs/pfeilrechtsKlick.png")));
        }
      });
      lLetztesBild = new JLabel();
      lLetztesBild.setIcon(new ImageIcon(getClass().getResource(
          "uiimgs/pfeillinks.png")));
      lLetztesBild.setText("");
      lLetztesBild.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent e) {

          /*
           * Waehlt das vorige Bild in der Liste aus und das letzte Bild wenn
           * das erste Bild der Liste ausgewaehlt ist
           */
          boolean neuesBildGewaehlt = false;
          for (int i = 0; i < listeAnzeigePanel.size() && !neuesBildGewaehlt; i++) {
            if (dok.equals(listeAnzeigePanel.get(i).gibBildDokument()) && i > 0) {
              dok = listeAnzeigePanel.get(i - 1).gibBildDokument();
              pGroszanzeige.setzeDok(dok);
              detailsTableModel.setzeDokument(dok);
              neuesBildGewaehlt = true;
            } else if (dok.equals(listeAnzeigePanel.get(i).gibBildDokument())
                && i == 0) {
              dok = listeAnzeigePanel.get(listeAnzeigePanel.size() - 1)
                  .gibBildDokument();
              pGroszanzeige.setzeDok(dok);
              detailsTableModel.setzeDokument(dok);
              neuesBildGewaehlt = true;
            }
          }
          updatePicture();
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
          lLetztesBild.removeAll();
          lLetztesBild.setIcon(new ImageIcon(getClass().getResource(
              "uiimgs/pfeillinks.png")));
        }

        public void mouseEntered(java.awt.event.MouseEvent e) {
          lLetztesBild.removeAll();
          lLetztesBild.setIcon(new ImageIcon(getClass().getResource(
              "uiimgs/pfeillinksKlick.png")));
        }
      });

      pToolbar = new JPanel();
      pToolbar.setLayout(flowLayout);
      pToolbar.add(lLetztesBild, null);
      pToolbar.add(lNaechstesBild, null);
      pToolbar.add(new JLabel("Groeße in %:"), null);
      pToolbar.add(getCbGroesze(), null);
      pToolbar.add(getTbGroeszeAnpassen(), null);
      pToolbar.add(getBSchlieszen(), null);
    }
    return pToolbar;
  }

  /**
   * This method initializes jButton
   * 
   * @return javax.swing.JButton
   */
  private JButton getBSchlieszen() {
    if (bSchlieszen == null) {
      bSchlieszen = new JButton();
      bSchlieszen.setText("Schlie\u00dfen");
      bSchlieszen.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          setVisible(false);
        }
      });
    }
    return bSchlieszen;
  }

  /**
   * This method initializes tpGroszanzeige
   * 
   * @return javax.swing.JTabbedPane
   */
  private JTabbedPane getTpGroszanzeige() {
    if (tpGroszanzeige == null) {
      tpGroszanzeige = new JTabbedPane();
      tpGroszanzeige.setBackground(new Color(238, 238, 238));
      tpGroszanzeige
          .addTab("Gro\u00dfanzeige", null, getSpGroszanzeige(), null);
      tpGroszanzeige.addTab("Zusatzdetails", null, getSpZusatzdetails(), null);
    }
    return tpGroszanzeige;
  }

  /**
   * This method initializes pGroszanzeige
   * 
   * @return javax.swing.JPanel
   */
  private BildGroszanzeigeZeichner getPGroszanzeige() {
    if (pGroszanzeige == null) {
      double skalierungPro = 100.0;
      if (((String) cbGroesze.getSelectedItem()).matches("[0-9]+")) {

        skalierungPro = Double
            .parseDouble((String) cbGroesze.getSelectedItem());
      }
      pGroszanzeige = new BildGroszanzeigeZeichner(dok, skalierungPro,
          tbGroeszeAnpassen.isEnabled());
      pGroszanzeige.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      pGroszanzeige.setLayout(new FlowLayout());
      pGroszanzeige.addBildGeladenListener(new BildGeladenListener() {
        public void bildWurdeGeladen() {
          updateTable();
          updatePicture();
        }
      });
    }
    return pGroszanzeige;
  }

  /**
   * This method initializes spZusatzdetails
   * 
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getSpZusatzdetails() {
    if (spZusatzdetails == null) {
      spZusatzdetails = new JScrollPane();
      spZusatzdetails.setViewportView(getTZusatzdetails());
    }
    return spZusatzdetails;
  }

  /**
   * This method initializes tZusatzdetails
   * 
   * @return javax.swing.JTable
   */
  private JTable getTZusatzdetails() {
    if (tZusatzdetails == null) {
      tZusatzdetails = new JTable();
      tZusatzdetails.setRowHeight(20);
      tZusatzdetails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      detailsTableModel = new BGATabellenModell(this.dok);
      tZusatzdetails.setModel(detailsTableModel);
      detailsTableModel.addBildGeladenListener(new BildGeladenListener() {
        public void bildWurdeGeladen() {
          updateTable();
        }
      });
    }
    return tZusatzdetails;
  }

  /**
   * This method initializes tbGroeszeAnpassen
   * 
   * @return javax.swing.JToggleButton
   */
  private JToggleButton getTbGroeszeAnpassen() {
    if (tbGroeszeAnpassen == null) {
      tbGroeszeAnpassen = new JToggleButton();
      tbGroeszeAnpassen.setText("Anpassen");
      tbGroeszeAnpassen.setSelected(true);
      tbGroeszeAnpassen.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          if (tbGroeszeAnpassen.isSelected()) {
            pGroszanzeige.setzeAnpassung(true);
            spGroszanzeige
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            spGroszanzeige
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
          } else {
            pGroszanzeige.setzeAnpassung(false);
            spGroszanzeige
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            spGroszanzeige
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
          }
          updatePicture();
        }
      });
    }
    return tbGroeszeAnpassen;
  }

  /**
   * This method initializes cbGroesze
   * 
   * @return javax.swing.JComboBox
   */
  private JComboBox getCbGroesze() {
    if (cbGroesze == null) {
      cbGroesze = new JComboBox();
      cbGroesze.setEditable(true);
      cbGroesze.setPreferredSize(new Dimension(75, 20));
      cbGroesze.addItem("50");
      cbGroesze.addItem("100");
      cbGroesze.addItem("200");
      cbGroesze.setSelectedItem("100");
      cbGroesze.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent e) {

          if (cbGroesze.getSelectedItem().toString().trim().matches("[0-9]+")) {

            pGroszanzeige.setzeSkalierung(Double.parseDouble(cbGroesze
                .getSelectedItem().toString()));
          } else {

            String skalierung = cbGroesze.getSelectedItem().toString().trim();
            pGroszanzeige.setzeSkalierung(Double.parseDouble(skalierung
                .substring(0, skalierung.length() - 1)));
          }
          updatePicture();
        }
      });
    }
    return cbGroesze;
  }

  /**
   * This method initializes spGroszanzeige
   * 
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getSpGroszanzeige() {
    if (spGroszanzeige == null) {
      spGroszanzeige = new JScrollPane();
      spGroszanzeige
          .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      spGroszanzeige
          .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      spGroszanzeige.setViewportView(getPGroszanzeige());
    }
    return spGroszanzeige;
  }
}

