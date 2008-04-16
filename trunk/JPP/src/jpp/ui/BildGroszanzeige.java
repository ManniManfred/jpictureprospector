package jpp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
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
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableColumn;

import jpp.core.BildDokument;
import jpp.merkmale.BildbreiteMerkmal;
import jpp.merkmale.BildhoeheMerkmal;
import jpp.merkmale.DateipfadMerkmal;
import jpp.ui.listener.BildGeladenListener;

public class BildGroszanzeige extends JFrame {

  /** Enthaelt den Wert fuer die Spalte der Exif-Bezeichnungen. */
  private static final int KEYSPALTE = 0;

  /** Enthaelt den Wert fuer die Spalte der Exif-Werte. */
  private static final int WERTSPALTE = 1;

  private static final long serialVersionUID = 1L;

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

/**
 * Ein Objekt der Klasse zeichnet aus einer Datei ein Bild in den eigenen
 * Anzeigebereich.
 */
class BildGroszanzeigeZeichner extends JLabel {

  /** Enthaelt das Dokument, dass den Pfad zur Datei enthaelt. */
  private BildDokument dok = null;

  /**
   * Enthaelt eine Liste an Listenern die darauf reagieren, wenn ein Bild
   * geladen wurde.
   */
  private List<BildGeladenListener> listener = null;

  /** Enthaehlt das anzuzeigende Bild. */
  private Image bild = null;

  /** Enthaelt die Skalierung die das Bild erfahren soll. */
  private double skalierungProzent;

  /**
   * Enthaelt die Information ob das Bild an die Groesze des Panels angepasst
   * werden muss.
   */
  private boolean mussAngepasstWerden;

  /**
   * Erzeugt ein neues Objekt der Klasse mit den entsprechenden Daten.
   * 
   * @param dok
   *          das Bilddokument, dass den Pfad zur Datei enthaelt
   * @param skalierung
   *          enthaelt die Skalierung in Prozent, die das Bild Bild erfahren
   *          soll
   */
  public BildGroszanzeigeZeichner(BildDokument dok, double skalierung,
      boolean mussAngepasstWerden) {
    this.dok = dok;
    this.listener = new ArrayList<BildGeladenListener>();
    this.skalierungProzent = skalierung;
    this.mussAngepasstWerden = mussAngepasstWerden;
    setzeDok(dok);
    setAutoscrolls(true);
    super.setIcon(new ImageIcon(bild));
    initializeMouseInputAdapter();
  }

  /**
   * Initialisiert den <code>MouseInputAdapter</code> der fuer das scrollen
   * innerhalb der ScrollPane verantwortlich ist, wenn die Maus
   * entsprechend bewegt wurde.
   */
  private void initializeMouseInputAdapter() {
    
    // Enthaelt den MouseInputAdapter fuer das Scrollen
    MouseInputAdapter mia = new MouseInputAdapter() {
      
      /** Enthaelt die letzte X-Position wenn die Maus gedrueckt wurde. */
      private int lastX;

      /** Enthaelt die letzte Y-Position wenn die Maus gedrueckt wurde. */
      private int lastY;

      /** Enthaelt das parent dieses Objektes. */
      private Container parent;

      public void mouseDragged(MouseEvent e) {
        
        parent = BildGroszanzeigeZeichner.this.getParent();
        if (parent instanceof JViewport) {
          JViewport jv = (JViewport) parent;
          Point p = jv.getViewPosition();
          int newX = p.x - (e.getX() - lastX);
          int newY = p.y - (e.getY() - lastY);
          int maxX = BildGroszanzeigeZeichner.this.getWidth() - jv.getWidth();
          int maxY = BildGroszanzeigeZeichner.this.getHeight() - jv.getHeight();
          if (newX < 0) {
            newX = 0;
          }
          if (newX > maxX) {
            newX = maxX;
          }
          if (newY < 0) {
            newY = 0;
          }
          if (newY > maxY) {
            newY = maxY;
          }
          jv.setViewPosition(new Point(newX, newY));
        }
      }

      public void mousePressed(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        lastX = e.getX();
        lastY = e.getY();
      }

      public void mouseReleased(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      }
    };

    addMouseMotionListener(mia);
    addMouseListener(mia);
  }

  /**
   * Fuegt einen <code>BildGeladenListener</code> zur Liste der Listener
   * hinzu.
   * 
   * @param l
   *          der hinzuzufuegende Listener
   */
  public void addBildGeladenListener(BildGeladenListener l) {
    listener.add(l);
  }

  /**
   * Loescht einen <code>BildGeladenListener</code> aus der Liste der
   * Listener.
   * 
   * @param l
   *          der zu loeschende Listener
   */
  public void removeBildGeladenListener(BildGeladenListener l) {
    if (l != null && listener.contains(l)) {
      listener.remove(l);
    }
  }

  /**
   * Wird aufgerufen, wenn das anzuzeigende Bild geladen wurde und
   * benachrichtigt alle Listener
   */
  public void fireBildGeladen() {
    for (BildGeladenListener l : listener) {
      l.bildWurdeGeladen();
    }
  }

  /**
   * Setzt das Dokument in der Anzeige neu.
   * 
   * @param dok
   *          das Dokument mit den benoetigten Informationen
   */
  public void setzeDok(BildDokument dok) {
    this.dok = dok;
    try {
      this.bild = ImageIO.read((URL) dok.getMerkmal(
          DateipfadMerkmal.FELDNAME).getWert());
      fireBildGeladen();
    } catch (IOException e) {
      System.out.println("Das Bild konnte nicht geladen werden.\n"
          + e.getMessage());
    }
    this.repaint();
  }

  /**
   * Setzt die Skalierung die das Bild erfahren soll neu und zeichnet das Bild
   * dementsprechend.
   * 
   * @param skalierung
   *          die Skalerierung in Prozent
   */
  public void setzeSkalierung(double skalierung) {

    this.skalierungProzent = skalierung;
    this.repaint();
  }

  /**
   * Setzt den Status ob das Bild der Fenstergroesze angepasst werden soll oder
   * nicht.
   * 
   * @param mussAngepasstWerden
   *          <code>true</code> wen das Bild der Fenstergroesze angepasst
   *          werden soll
   */
  public void setzeAnpassung(boolean mussAngepasstWerden) {

    this.mussAngepasstWerden = mussAngepasstWerden;
    this.repaint();
  }

  /**
   * Zeichnet das Bild in die Oberflaeche, mit den entsprechenden Abmessungen.
   */
  protected void paintComponent(Graphics g) {

    double originalBreite = (Integer) dok
        .getMerkmal(BildbreiteMerkmal.FELDNAME).getWert();
    double originalHoehe = (Integer) dok.getMerkmal(BildhoeheMerkmal.FELDNAME)
        .getWert();
    double hoeheBild = bild.getHeight(this);
    double breiteBild = bild.getWidth(this);
    double dieseBreite = getPreferredSize().width;
    double dieseHoehe = getPreferredSize().height;

    if (!mussAngepasstWerden) {

      breiteBild = originalBreite * skalierungProzent / 100;
      hoeheBild = originalHoehe * skalierungProzent / 100;

      g.drawImage(bild, (int) (dieseBreite - breiteBild) / 2,
          (int) (dieseHoehe - hoeheBild) / 2, (int) breiteBild,
          (int) hoeheBild, this);
    } else if (originalBreite <= dieseBreite && originalHoehe <= dieseHoehe) {

      g.drawImage(bild, (int) (dieseBreite - originalBreite) / 2,
          (int) (dieseHoehe - originalHoehe) / 2, (int) originalBreite,
          (int) originalHoehe, this);
    } else if (Math.abs(dieseBreite / breiteBild) < Math.abs(dieseHoehe
        / hoeheBild)) {
      // Anpassung der Größe an dieses Objekt

      // Breite voll ausgefuellt, Hoehe muss neu berechnet werden
      g.drawImage(bild, 0, (int) (dieseHoehe - hoeheBild
          * (dieseBreite / breiteBild)) / 2, (int) dieseBreite,
          (int) (hoeheBild * (dieseBreite / breiteBild)), this);
    } else {

      // Hoehe voll ausgefuellt, Breite muss neu berechnet werden
      g
          .drawImage(bild, (int) (dieseBreite - breiteBild
              * (dieseHoehe / hoeheBild)) / 2, 0,
              (int) (breiteBild * (dieseHoehe / hoeheBild)), (int) dieseHoehe,
              this);
    }
  }
}
