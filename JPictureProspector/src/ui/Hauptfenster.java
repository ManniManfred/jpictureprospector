package ui;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

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
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

/**
 * Ein Objekt der Klasse stellt alle Objekte zur Verfuegung, die zur
 * Suche und Anzeige von Bildern auf dem System noetig sind.
 */
public class Hauptfenster extends JFrame {
  
  /** Enthaelt die Standardhoehe des Fensters. */
  private static final int STD_HOEHE = 600;
  
  /** Enthaelt die Standardbreite des Fensters. */
  private static final int STD_BREITE = 800;

  /** Version UID der Software. */
  private static final long serialVersionUID = 1L;

  /** Enthaelt die Inhaltsflaeche dieses Objekts. */
  private JPanel inhaltsflaeche = null;

  /** Enthaelt das Hauptmenue dieses Objektes. */
  private JMenuBar hauptmenu = null;
  
  private JMenu mDatei = null;

  private JMenuItem miBeenden = null;

  private JTabbedPane pInformationen = null;

  private JPanel pBildinformationen = null;

  private JTable tBildinformationen = null;

  private JSplitPane jSplitPane = null;

  private JMenuItem miImport = null;

  private JMenu mEinstellungen = null;

  private JMenu mHilfe = null;

  private JMenuItem miInfo = null;

  private JMenuItem miGrundeinstellungen = null;

  private Vorschaupanel pVorschau = null;

  private SuchPanel pSuche = null;

  private JToolBar tbWerkzeugleiste = null;

  private JButton bNormalansicht = null;

  private JButton bGroszanzeige = null;

  private JTextPane tpKommentar = null;

  private JPanel pThumbnails = null;

  private JScrollPane spThumbnails = null;

  private JSplitPane spVorschauBildinfo = null;

  private JLabel lLetztesBild = null;

  private JLabel lNaechstesBild = null;

  /**
   * This is the default constructor
   */
  public Hauptfenster() {
    super();
    initialize();
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
      miBeenden.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          
          int ergebnis = JOptionPane.showConfirmDialog(inhaltsflaeche, 
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
   * This method initializes informationsPanel	
   * 	
   * @return javax.swing.JTabbedPane	
   */
  private JTabbedPane getPInformationen() {
    if (pInformationen == null) {
      pInformationen = new JTabbedPane();
      pInformationen.setName("pInformationen");
      pInformationen.addTab("Bildinformation", null,
          getPBildinformationen(), "Enthält alle physischen Daten des Bildes.");
      pInformationen.addTab("Kommentar", null, getTpKommentar(), "Diese " +
          "Informationen werden verwendet, um eine Suche gewährleisten " +
          "zu können.");
    }
    return pInformationen;
  }

  /**
   * This method initializes pBildinformationen	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPBildinformationen() {
    if (pBildinformationen == null) {
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = GridBagConstraints.BOTH;
      gridBagConstraints.weighty = 1.0;
      gridBagConstraints.weightx = 1.0;
      pBildinformationen = new JPanel();
      pBildinformationen.setLayout(new GridBagLayout());
      pBildinformationen.add(getTBildinformationen(), gridBagConstraints);
    }
    return pBildinformationen;
  }

  /**
   * This method initializes tBildinformationen	
   * 	
   * @return javax.swing.JTable	
   */
  private JTable getTBildinformationen() {
    if (tBildinformationen == null) {
      tBildinformationen = new JTable();
      tBildinformationen.setModel(new BildinfoTabelModel());
    }
    return tBildinformationen;
  }

  /**
   * This method initializes jSplitPane	
   * 	
   * @return javax.swing.JSplitPane	
   */
  private JSplitPane getJSplitPane() {
    if (jSplitPane == null) {
      jSplitPane = new JSplitPane();
      jSplitPane.setOneTouchExpandable(true);
      jSplitPane.setDividerSize(7);
      jSplitPane.setRightComponent(getPThumbnails());
      jSplitPane.setLeftComponent(getSpVorschauBildinfo());
    }
    return jSplitPane;
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
      miImport.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          JFileChooser dateiauswahl = new JFileChooser();
          FileFilter filter = new Bildfilter();
          dateiauswahl.setFileFilter(filter);
          dateiauswahl.showOpenDialog(inhaltsflaeche);
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
      mEinstellungen.add(getMiGrundeinstellungen());
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
      miInfo.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          JOptionPane.showMessageDialog(inhaltsflaeche, "Programmierprojekt " +
              "der FH Gelsenkirchen\n\nJPictureProspector\n\nv0.1",
              "Info", JOptionPane.INFORMATION_MESSAGE);
        }
      });
    }
    return miInfo;
  }

  /**
   * This method initializes miGrundeinstellungen	
   * 	
   * @return javax.swing.JMenuItem	
   */
  private JMenuItem getMiGrundeinstellungen() {
    if (miGrundeinstellungen == null) {
      miGrundeinstellungen = new JMenuItem();
      miGrundeinstellungen.setText("Grundeinstellungen");
    }
    return miGrundeinstellungen;
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
      lNaechstesBild = new JLabel();
      lNaechstesBild.setText("");
      lNaechstesBild.setIcon(new ImageIcon(getClass().getResource("/imgs/pfeilrechts.png")));
      lNaechstesBild.setSize(new Dimension(32, 32));
      lNaechstesBild.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseExited(java.awt.event.MouseEvent e) {    
          lNaechstesBild.removeAll();
          lNaechstesBild.setIcon(new ImageIcon(getClass().getResource("/imgs/pfeilrechts.png")));
      	}
        public void mouseEntered(java.awt.event.MouseEvent e) {
          lNaechstesBild.removeAll();
          lNaechstesBild.setIcon(new ImageIcon(getClass().getResource("/imgs/pfeilrechtsKlick.png")));
        }
      });
      lLetztesBild = new JLabel();
      lLetztesBild.setText("");
      lLetztesBild.setIcon(new ImageIcon(getClass().getResource("/imgs/pfeillinks.png")));
      lLetztesBild.setSize(new Dimension(32, 32));
      lLetztesBild.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseExited(java.awt.event.MouseEvent e) {    
          lLetztesBild.removeAll();
          lLetztesBild.setIcon(new ImageIcon(getClass().getResource("/imgs/pfeillinks.png")));
      	}
        public void mouseEntered(java.awt.event.MouseEvent e) {
          lLetztesBild.removeAll();
          lLetztesBild.setIcon(new ImageIcon(getClass().getResource("/imgs/pfeillinksKlick.png")));
        }
      });
      tbWerkzeugleiste = new JToolBar();
      tbWerkzeugleiste.add(getBNormalansicht());
      tbWerkzeugleiste.add(getBGroszanzeige());
      tbWerkzeugleiste.add(lLetztesBild);
      tbWerkzeugleiste.add(lNaechstesBild);
      
    }
    return tbWerkzeugleiste;
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
   * This method initializes tpKommentar	
   * 	
   * @return javax.swing.JTextPane	
   */
  private JTextPane getTpKommentar() {
    if (tpKommentar == null) {
      tpKommentar = new JTextPane();
    }
    return tpKommentar;
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
      pThumbnails.add(getSpThumbnails(), BorderLayout.CENTER);
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
      spVorschauBildinfo.setBottomComponent(getPInformationen());
      spVorschauBildinfo.setTopComponent(getPVorschau());
    }
    return spVorschauBildinfo;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Hauptfenster hauptfenster = new Hauptfenster();
        hauptfenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hauptfenster.setVisible(true);
      }
    });
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(new Dimension(STD_BREITE, STD_HOEHE));
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
    if (inhaltsflaeche == null) {
      inhaltsflaeche = new JPanel();
      inhaltsflaeche.setLayout(new BorderLayout());
      inhaltsflaeche.add(getJSplitPane(), BorderLayout.CENTER);
      inhaltsflaeche.add(getSuchPanel(), BorderLayout.NORTH);
    }
    return inhaltsflaeche;
  }

}
