package ui;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import core.JPPCore;
import java.awt.Insets;
import javax.swing.JTextPane;
import javax.swing.JTable;

/**
 * Ein Objekt der Klasse stellt alle Objekte zur Verfuegung, die zur
 * Suche und Anzeige von Bildern auf dem System noetig sind.
 */
public class Hauptfenster extends JFrame {
  
  /** Enthaelt die Standardhoehe des Fensters. */
  private static final int STD_HOEHE = 600;
  
  /** Enthaelt die Standardbreite des Fensters. */
  private static final int STD_BREITE = 800;
  
  /** Enthaelt den Standardabstand den Komponenten ggfs. zueinander besitzen. */
  private static final int STD_INSETS = 10;

  /** Version UID der Software. */
  private static final long serialVersionUID = 1L;
  
  /** Enthaelt den Kern der Software mit dem operiert wird. */
  private JPPCore core;
  
  private List<ThumbnailPanel> thumbnails;

  /** Enthaelt die Inhaltsflaeche dieses Objekts. */
  private JPanel pInhaltsflaeche = null;

  /** Enthaelt das Hauptmenue dieses Objektes. */
  private JMenuBar hauptmenu = null;
  
  private JMenu mDatei = null;

  private JMenuItem miBeenden = null;

  private JSplitPane spAnzeige = null;

  private JMenuItem miImport = null;

  private JMenu mEinstellungen = null;

  private JMenu mHilfe = null;

  private JMenuItem miInfo = null;

  private Vorschaupanel pVorschau = null;

  private SuchPanel pSuche = null;

  private JToolBar tbWerkzeugleiste = null;

  private JButton bGroszanzeige = null;

  private JPanel pThumbnails = null;

  private JScrollPane spThumbnails = null;  //  @jve:decl-index=0:visual-constraint="328,135"

  private JSplitPane spVorschauBildinfo = null;

  private JLabel lLetztesBild = null;

  private JLabel lNaechstesBild = null;

  private JLabel lLoeschen = null;

  private JButton bSuchen = null;

  private JButton bNormalansicht = null;

  private JMenuItem miLoeschen = null;

  private JSlider sGroesze = null;

  private JScrollPane spBildinformationen = null;

  private JPanel pBildinformationen = null;

  private JLabel lSchluesselwoerter = null;

  private JTextField tfSchluesselwoerter = null;

  private JLabel lBildbeschreibung = null;

  private JTextPane tpBildbeschreibung = null;

  private JLabel lBilddaten = null;

  private JTable tBilddaten = null;

  /**
   * This is the default constructor
   */
  public Hauptfenster() {
    super();
    initialize();
    core = new JPPCore();
  }
  
  /**
   * Erstellt das Hauptmenue des Fensters.
   * 	
   * @return das Hauptmenu des Fensters, wenn es noch nicht erzeugt wurde
   */
  private JMenuBar getHauptmenu() {
    if (hauptmenu == null) {
      hauptmenu = new JMenuBar();
      hauptmenu.add(getMDatei());
      hauptmenu.add(getMEinstellungen());
      hauptmenu.add(getMHilfe());
    }
    return hauptmenu;
  }

  /**
   * This method initializes mDatei	
   * 	
   * @return javax.swing.JMenu	
   */
  private JMenu getMDatei() {
    if (mDatei == null) {
      mDatei = new JMenu();
      mDatei.setText("Datei");
      mDatei.setMnemonic(KeyEvent.VK_D);
      mDatei.add(getMiImport());
      mDatei.add(getMiBeenden());
    }
    return mDatei;
  }

  /**
   * This method initializes miBeenden	
   * 	
   * @return javax.swing.JMenuItem	
   */
  private JMenuItem getMiBeenden() {
    if (miBeenden == null) {
      miBeenden = new JMenuItem();
      miBeenden.setText("Beenden");
      miBeenden.setMnemonic(KeyEvent.VK_B);
      miBeenden.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          
          int ergebnis = JOptionPane.showConfirmDialog(pInhaltsflaeche, 
              "Wollen Sie das Programm beenden?", "Beenden", 
              JOptionPane.OK_CANCEL_OPTION);
          if (ergebnis == JOptionPane.OK_OPTION) {
            
            System.exit(0);
          }
        }
      });
    }
    return miBeenden;
  }

  /**
   * This method initializes jSplitPane	
   * 	
   * @return javax.swing.JSplitPane	
   */
  private JSplitPane getSpAnzeige() {
    if (spAnzeige == null) {
      spAnzeige = new JSplitPane();
      spAnzeige.setOneTouchExpandable(true);
      spAnzeige.setDividerSize(7);
      spAnzeige.setRightComponent(getPThumbnails());
      spAnzeige.setLeftComponent(getSpVorschauBildinfo());
    }
    return spAnzeige;
  }

  /**
   * This method initializes miImport	
   * 	
   * @return javax.swing.JMenuItem	
   */
  private JMenuItem getMiImport() {
    if (miImport == null) {
      miImport = new JMenuItem();
      miImport.setText("Importieren");
      miImport.setMnemonic(KeyEvent.VK_I);
      miImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK, false));
      miImport.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          JFileChooser dateiauswahl = new JFileChooser();
          FileFilter filter = new Bildfilter();
          dateiauswahl.setFileFilter(filter);
          dateiauswahl.showOpenDialog(pInhaltsflaeche);
        }
      });
    }
    return miImport;
  }

  /**
   * This method initializes mEinstellungen	
   * 	
   * @return javax.swing.JMenu	
   */
  private JMenu getMEinstellungen() {
    if (mEinstellungen == null) {
      mEinstellungen = new JMenu();
      mEinstellungen.setText("Einstellungen");
      mEinstellungen.setMnemonic(KeyEvent.VK_E);
      mEinstellungen.add(getMiLoeschen());
    }
    return mEinstellungen;
  }

  /**
   * This method initializes mHilfe	
   * 	
   * @return javax.swing.JMenu	
   */
  private JMenu getMHilfe() {
    if (mHilfe == null) {
      mHilfe = new JMenu();
      mHilfe.setText("Hilfe");
      mHilfe.setMnemonic(KeyEvent.VK_H);
      mHilfe.add(getMiInfo());
    }
    return mHilfe;
  }

  /**
   * This method initializes miInfo	
   * 	
   * @return javax.swing.JMenuItem	
   */
  private JMenuItem getMiInfo() {
    if (miInfo == null) {
      miInfo = new JMenuItem();
      miInfo.setText("Info");
      miInfo.setMnemonic(KeyEvent.VK_I);
      miInfo.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          JOptionPane.showMessageDialog(pInhaltsflaeche, "Programmierprojekt " +
              "der FH Gelsenkirchen\n\nJPictureProspector\n\nv0.1",
              "Info", JOptionPane.INFORMATION_MESSAGE);
        }
      });
    }
    return miInfo;
  }

  /**
   * This method initializes pVorschau	
   * 	
   * @return javax.swing.JPanel	
   */
  private Vorschaupanel getPVorschau() {
    if (pVorschau == null) {
      pVorschau = new Vorschaupanel();
      pVorschau.setLayout(new GridBagLayout());
      pVorschau.setName("pVorschau");
    }
    return pVorschau;
  }

  /**
   * This method initializes suchPanel	
   * 	
   * @return ui.SuchPanel	
   */
  private SuchPanel getSuchPanel() {
    if (pSuche == null) {
      pSuche = new SuchPanel();
      pSuche.setName("pSuche");
      pSuche.add(getBSuchen(), null);
    }
    return pSuche;
  }

  /**
   * This method initializes tbWerkzeugleiste	
   * 	
   * @return javax.swing.JToolBar	
   */
  private JToolBar getTbWerkzeugleiste() {
    if (tbWerkzeugleiste == null) {
      lLoeschen = new JLabel();
      lLoeschen.setText("");
      lLoeschen.setIcon(new ImageIcon(getClass().getResource("uiimgs/loeschen.png")));
      lLoeschen.setSize(new Dimension(32, 32));
      lLoeschen.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseExited(java.awt.event.MouseEvent e) {    
          lLoeschen.removeAll();
          lLoeschen.setIcon(new ImageIcon(getClass().getResource("uiimgs/loeschen.png")));
      	}
        public void mouseEntered(java.awt.event.MouseEvent e) {
          lLoeschen.removeAll();
          lLoeschen.setIcon(new ImageIcon(getClass().getResource("uiimgs/loeschenKlick.png")));
        }
      });
      lNaechstesBild = new JLabel();
      lNaechstesBild.setText("");
      lNaechstesBild.setIcon(new ImageIcon(getClass().getResource("uiimgs/pfeilrechts.png")));
      lNaechstesBild.setSize(new Dimension(32, 32));
      lNaechstesBild.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseExited(java.awt.event.MouseEvent e) {    
          lNaechstesBild.removeAll();
          lNaechstesBild.setIcon(new ImageIcon(getClass().getResource("uiimgs/pfeilrechts.png")));
      	}
        public void mouseEntered(java.awt.event.MouseEvent e) {
          lNaechstesBild.removeAll();
          lNaechstesBild.setIcon(new ImageIcon(getClass().getResource("uiimgs/pfeilrechtsKlick.png")));
        }
      });
      lLetztesBild = new JLabel();
      lLetztesBild.setText("");
      lLetztesBild.setIcon(new ImageIcon(getClass().getResource("/ui/uiimgs/pfeillinks.png")));
      lLetztesBild.setSize(new Dimension(32, 32));
      lLetztesBild.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseExited(java.awt.event.MouseEvent e) {    
          lLetztesBild.removeAll();
          lLetztesBild.setIcon(new ImageIcon(getClass().getResource("uiimgs/pfeillinks.png")));
      	}
        public void mouseEntered(java.awt.event.MouseEvent e) {
          lLetztesBild.removeAll();
          lLetztesBild.setIcon(new ImageIcon(getClass().getResource("uiimgs/pfeillinksKlick.png")));
        }
      });
      tbWerkzeugleiste = new JToolBar();
      tbWerkzeugleiste.add(getBNormalansicht());
      tbWerkzeugleiste.add(getBGroszanzeige());
      tbWerkzeugleiste.add(lLetztesBild);
      tbWerkzeugleiste.add(lNaechstesBild);
      tbWerkzeugleiste.add(lLoeschen);
      tbWerkzeugleiste.add(getSGroesze());
      
    }
    return tbWerkzeugleiste;
  }

  /**
   * This method initializes bGroszanzeige	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getBGroszanzeige() {
    if (bGroszanzeige == null) {
      bGroszanzeige = new JButton();
      bGroszanzeige.setText("Bild anzeigen");
    }
    return bGroszanzeige;
  }

  /**
   * This method initializes pThumbnails	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPThumbnails() {
    if (pThumbnails == null) {
      pThumbnails = new JPanel();
      pThumbnails.setLayout(new BorderLayout());
      pThumbnails.add(getTbWerkzeugleiste(), BorderLayout.NORTH);
    }
    return pThumbnails;
  }

  /**
   * This method initializes spThumbnails	
   * 	
   * @return javax.swing.JScrollPane	
   */
  private JScrollPane getSpThumbnails() {
    if (spThumbnails == null) {
      spThumbnails = new JScrollPane();
    }
    return spThumbnails;
  }

  /**
   * This method initializes spVorschauBildinfo	
   * 	
   * @return javax.swing.JSplitPane	
   */
  private JSplitPane getSpVorschauBildinfo() {
    if (spVorschauBildinfo == null) {
      spVorschauBildinfo = new JSplitPane();
      spVorschauBildinfo.setOrientation(JSplitPane.VERTICAL_SPLIT);
      spVorschauBildinfo.setDividerLocation(400);
      spVorschauBildinfo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      spVorschauBildinfo.setDividerSize(7);
      spVorschauBildinfo.setMinimumSize(new Dimension(300, 245));
      spVorschauBildinfo.setPreferredSize(new Dimension(300, 245));
      spVorschauBildinfo.setBottomComponent(getSpBildinformationen());
      spVorschauBildinfo.setTopComponent(getPVorschau());
    }
    return spVorschauBildinfo;
  }

  /**
   * This method initializes bSuchen	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getBSuchen() {
    if (bSuchen == null) {
      bSuchen = new JButton();
      bSuchen.setText("Suchen");
    }
    return bSuchen;
  }

  /**
   * This method initializes bNormalansicht	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getBNormalansicht() {
    if (bNormalansicht == null) {
      bNormalansicht = new JButton();
      bNormalansicht.setText("Normalansicht");
    }
    return bNormalansicht;
  }

  /**
   * This method initializes miLoeschen	
   * 	
   * @return javax.swing.JMenuItem	
   */
  private JMenuItem getMiLoeschen() {
    if (miLoeschen == null) {
      miLoeschen = new JMenuItem();
      miLoeschen.setText("Löschen");
    }
    return miLoeschen;
  }

  /**
   * Initialisiert die Leiste fuer die Groesze der Thumbnails.
   * 	
   * @return javax.swing.JSlider	liefert den initialisierten Schieber
   */
  private JSlider getSGroesze() {
    if (sGroesze == null) {
      sGroesze = new JSlider();
      sGroesze.setValue(100);
      sGroesze.setMaximum(250);
      sGroesze.setMaximumSize(new Dimension(200, 16));
      sGroesze.setMinimum(50);
      sGroesze.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent e) {
          if (thumbnails != null) {
            for (ThumbnailPanel tp : thumbnails) {
              tp.setzeGroesze(new Dimension(sGroesze.getValue(),
                  (int) (sGroesze.getValue() * (3.0 / 4))));
            }
          }
        }
      });
    }
    return sGroesze;
  }

  /**
   * This method initializes spBildinformationen	
   * 	
   * @return javax.swing.JScrollPane	
   */
  private JScrollPane getSpBildinformationen() {
    if (spBildinformationen == null) {
      spBildinformationen = new JScrollPane();
      spBildinformationen.setViewportView(getPBildinformationen());
    }
    return spBildinformationen;
  }

  /**
   * This method initializes pBildinformationen	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPBildinformationen() {
    if (pBildinformationen == null) {
      GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
      gridBagConstraints5.fill = GridBagConstraints.BOTH;
      gridBagConstraints5.gridy = 5;
      gridBagConstraints5.weightx = 1.0;
      gridBagConstraints5.weighty = 1.0;
      gridBagConstraints5.gridx = 1;
      GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
      gridBagConstraints4.gridx = 1;
      gridBagConstraints4.anchor = GridBagConstraints.WEST;
      gridBagConstraints4.insets = new Insets(0, 0, STD_INSETS, 0);
      gridBagConstraints4.gridy = 4;
      lBilddaten = new JLabel();
      lBilddaten.setText("Bilddaten");
      GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
      gridBagConstraints3.fill = GridBagConstraints.BOTH;
      gridBagConstraints3.gridy = 3;
      gridBagConstraints3.weightx = 1.0;
      gridBagConstraints3.weighty = 1.0;
      gridBagConstraints3.insets = new Insets(0, 0, STD_INSETS, 0);
      gridBagConstraints3.gridx = 1;
      GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
      gridBagConstraints2.gridx = 1;
      gridBagConstraints2.insets = new Insets(0, 0, STD_INSETS, 0);
      gridBagConstraints2.anchor = GridBagConstraints.WEST;
      gridBagConstraints2.gridy = 2;
      lBildbeschreibung = new JLabel();
      lBildbeschreibung.setText("Bildbeschreibung");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = GridBagConstraints.VERTICAL;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.insets = new Insets(0, 0, STD_INSETS, 0);
      gridBagConstraints.anchor = GridBagConstraints.WEST;
      gridBagConstraints.gridx = 1;
      GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
      gridBagConstraints1.gridx = 1;
      gridBagConstraints1.anchor = GridBagConstraints.WEST;
      gridBagConstraints1.insets = new Insets(STD_INSETS, 0, STD_INSETS, 0);
      gridBagConstraints1.gridy = 0;
      lSchluesselwoerter = new JLabel();
      lSchluesselwoerter.setText("Schlüsselwörter");
      pBildinformationen = new JPanel();
      pBildinformationen.setLayout(new GridBagLayout());
      pBildinformationen.add(lSchluesselwoerter, gridBagConstraints1);
      pBildinformationen.add(getTfSchluesselwoerter(), gridBagConstraints);
      pBildinformationen.add(lBildbeschreibung, gridBagConstraints2);
      pBildinformationen.add(getTpBildbeschreibung(), gridBagConstraints3);
      pBildinformationen.add(lBilddaten, gridBagConstraints4);
      pBildinformationen.add(getTBilddaten(), gridBagConstraints5);
    }
    return pBildinformationen;
  }

  /**
   * This method initializes tfSchluesselwoerter	
   * 	
   * @return javax.swing.JTextField	
   */
  private JTextField getTfSchluesselwoerter() {
    if (tfSchluesselwoerter == null) {
      tfSchluesselwoerter = new JTextField();
      tfSchluesselwoerter.setPreferredSize(new Dimension(200, 20));
    }
    return tfSchluesselwoerter;
  }

  /**
   * This method initializes tpBildbeschreibung	
   * 	
   * @return javax.swing.JTextPane	
   */
  private JTextPane getTpBildbeschreibung() {
    if (tpBildbeschreibung == null) {
      tpBildbeschreibung = new JTextPane();
      tpBildbeschreibung.setMaximumSize(new Dimension(100, 150));
    }
    return tpBildbeschreibung;
  }

  /**
   * This method initializes tBilddaten	
   * 	
   * @return javax.swing.JTable	
   */
  private JTable getTBilddaten() {
    if (tBilddaten == null) {
      tBilddaten = new JTable();
      tBilddaten.setModel(new BildinfoTabelModel());
    }
    return tBilddaten;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try { 
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception e) { 
          e.printStackTrace(); 
        }
        Hauptfenster hauptfenster = new Hauptfenster();
        hauptfenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hauptfenster.setVisible(true);
      }
    });
  }

  /**
   * Initialisiert das dieses Objekt.
   */
  private void initialize() {
    this.setSize(new Dimension(800, 600));
    this.setJMenuBar(getHauptmenu());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setContentPane(getJContentPane());
    this.setTitle("JPictureProspector");
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane() {
    if (pInhaltsflaeche == null) {
      pInhaltsflaeche = new JPanel();
      pInhaltsflaeche.setLayout(new BorderLayout());
      pInhaltsflaeche.add(getSuchPanel(), BorderLayout.NORTH);
      pInhaltsflaeche.add(getSpAnzeige(), BorderLayout.CENTER);
    }
    return pInhaltsflaeche;
  }

}
