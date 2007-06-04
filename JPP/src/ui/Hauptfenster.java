package ui;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observer;

import javax.swing.BorderFactory;
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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import core.BildDokument;
import core.EntferneException;
import core.ErzeugeException;
import core.ImportException;
import core.JPPCore;
import core.SucheException;
import core.Trefferliste;

/**
 * Ein Objekt der Klasse stellt das Hauptanzeigefenster der Software zur
 * Verfuegung. Im Hauptfenster kann der Anwender alle Aktionen die
 * zur Suche und Anzeige der Bilder notwendig sind ausfuehren.
 */
public class Hauptfenster extends JFrame {
  
  /** Enthaelt die Standardhoehe des Fensters. */
  private static final int STD_HOEHE = 600;
  
  /** Enthaelt die Standardbreite des Fensters. */
  private static final int STD_BREITE = 800;
  
  /** Enthaelt den Standardabstand den Komponenten ggfs. zueinander besitzen. */
  private static final int STD_ABSTAND = 10;
  
  /** Gibt an welche Breite die Komponenten minimal im Bereich der
   * Bildinformationen haben duerfen.
   */
  private static final int MIN_BREITE_BILDINFO = 300;
  
  /** Gib an wie grosz ein Trennbalken einer Splitpane sein darf. */
  private static final int TRENNBALKEN_GROESZE = 7;

  /** Version UID der Software. */
  private static final long serialVersionUID = 1L;
  
  /** Enthaelt den Kern der Software mit dem operiert wird. */
  private JPPCore kern;
  
  /** Enthaelt alle Observer fuer ThumbnailAnzeigePanel. */
  private List<Observer> tapObserver = null;  //  @jve:decl-index=0:
  
  /** Enthaelt die Groszanzeige fuer ein Bild. */
  private BildGroszanzeige groszanzeige = null;
  
  /** Enthaelt eine Liste an Paneln die zustaendig fuer die Anzeige der
   * Thumbnails sind.
   */
  private List<ThumbnailAnzeigePanel> listeAnzeigePanel = null;

  /** Enthaelt die Trefferliste nach einer ausgeführten Suche. */
  private Trefferliste trefferliste = null;
  
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

  private JPanel pThumbnailSteuerung = null;

  private JSplitPane spVorschauBildinfo = null;

  private JLabel lLetztesBild = null;

  private JLabel lNaechstesBild = null;

  private JLabel lLoeschen = null;

  private JMenuItem miLoeschen = null;

  private JSlider sGroesze = null;

  private JTextField tfSchluesselwoerter = null;

  private JPanel pBildinformationen = null;

  private JPanel pBilddetails = null;
  
  private JLabel lSchluesselwoerter = null;
  
  private JLabel lBildbeschreibung = null;
  
  private JTextPane taBildbeschreibung = null;
  
  private JScrollPane spBilddetails = null;
  
  private JTable tBilddetails = null;

  private JPanel pThumbnails = null;

  private JScrollPane spThumbnails = null;

  private JButton bAuswahlAufheben = null;

/**
   * Erstellt ein neues Objekt der Klasse.
   */
  public Hauptfenster() {
    super();
    initialize();
    try {
      kern = new JPPCore();
    } catch (ErzeugeException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Importiert in den Kern des Programms eine ausgewaehlte Anzahl
   * an Dateien. Wenn ein Fehler waehrend des Importiervorgangs entsteht
   * wird dem Benutzer eine entsprechende Fehlermeldung ausgegeben.
   */
  private void importiereDateien() {
    
    JFileChooser dateiauswahl = new JFileChooser();
    FileFilter filter = new Bildfilter();
    File[] files;
    dateiauswahl.setMultiSelectionEnabled(true);
    dateiauswahl.setFileFilter(filter);
    final int ergebnis = dateiauswahl.showOpenDialog(pInhaltsflaeche);
    files = dateiauswahl.getSelectedFiles();
    final Bildimportierer importierer = new Bildimportierer(files, kern,
        sGroesze.getValue(), tapObserver);
    int anzahlDateien = files == null ? 0 : files.length;
    final LadebalkenDialog ladebalken = 
          new LadebalkenDialog(this, anzahlDateien);
    
    // Neue liste fuer die Anzeigepanel erzeugen
    if (this.listeAnzeigePanel == null) {
      this.listeAnzeigePanel = new ArrayList<ThumbnailAnzeigePanel>();
    } else {
      // es existiert schon eine liste mit anzeigepaneln
      this.listeAnzeigePanel.clear();
      this.pThumbnails.removeAll();
    }
    
    /* Listener hinzufuegen die entsprechende Aktionen ausführen, wenn
     * neue Bilder geladen wurden. */
    importierer.addBildImportiertListener(new BildimportListener() {
      public void bildImportiert() {
        ladebalken.setzeAnzahl(ladebalken.gibAnzahl() + 1);
        listeAnzeigePanel = importierer.gibAnzeigePanel();
        erzeugeThumbnailansicht();
      }
      public void ladevorgangAbgeschlossen() {
        ladebalken.dispose();
        erzeugeThumbnailansicht();
      }
    });
    
    // Es sollen Bilder importiert werden
    if (ergebnis != JFileChooser.CANCEL_OPTION) {
      importierer.start();
      ladebalken.setLocation((this.getWidth() - ladebalken.getWidth()) / 2, 
          (this.getHeight() - ladebalken.getHeight()) / 2);
      ladebalken.setVisible(true);
    }
  }

  /**
   * Erzeugt alle notwendigen Daten, die zur Anzeige notwendig sind, wenn
   * der Benutzer nach einem Begriff gesucht hat.
   */
  public void erzeugeDatenNachSuche() {
    
    try {
      trefferliste = kern.suche(pSuche.gibSuchtext());
      if (listeAnzeigePanel == null) {
        listeAnzeigePanel = new ArrayList<ThumbnailAnzeigePanel>();
      } else {
        listeAnzeigePanel.clear();
      }
      for (int i = 0; i < trefferliste.getAnzahlTreffer(); i++) {
        listeAnzeigePanel.add(
            new ThumbnailAnzeigePanel(trefferliste.getBildDokument(i),
                sGroesze.getValue(), tapObserver));
      }
    } catch (SucheException se) {
      zeigeFehlermeldung("Suche fehlgeschlagen", "Die Suche konnte " +
          "nicht erfolgreich ausgeführt werden.\n\n" + se.getMessage());
    }
  }
  
  /**
   * Zeigt dem Benutzer eine Fehlermeldung an.
   * 
   * @param titel  der Titel der Fehlermeldung
   * @param meldung  die Fehlermeldung
   */
  private void zeigeFehlermeldung(String titel, String meldung) {
    
    JOptionPane.showMessageDialog(this.getParent(), meldung, titel,
        JOptionPane.ERROR_MESSAGE);
  }
  
  /**
   * Zeigt alle Thumbnails innerhalb des entsprechenden Anzeigebereichs
   * an, mit den entsprechenden Eigenschaften von oben nach unten scrollen
   * zu koennen und die Groesze der Thumbnails dynamisch anzupassen.
   */
  public void erzeugeThumbnailansicht() {
    
    if (listeAnzeigePanel != null) {

      /* Neuzeichnen der Vorschau da ansonsten evtl nicht vorhandene Bilder
      noch angezeigt werden */
      pVorschau.resetAnsicht();
      pThumbnails.removeAll();
      
      double thumbnailPanelBreite = spAnzeige.getWidth() -
          spAnzeige.getDividerLocation() - TRENNBALKEN_GROESZE -
          spThumbnails.getVerticalScrollBar().getWidth() - STD_ABSTAND;
      double anzahlThumbnailsProZeile = Math.floor(thumbnailPanelBreite /
          (sGroesze.getValue() + STD_ABSTAND));
      anzahlThumbnailsProZeile = anzahlThumbnailsProZeile == 0 ?
          1 : anzahlThumbnailsProZeile;
      double anzahlBenoetigteZeilen = listeAnzeigePanel.size() / 
          anzahlThumbnailsProZeile;
      double benoetigteBreite = anzahlThumbnailsProZeile * 
          (sGroesze.getValue() + STD_ABSTAND) + STD_ABSTAND;
      double benoetigteHoehe = anzahlBenoetigteZeilen * 
          (sGroesze.getValue() + STD_ABSTAND) + tbWerkzeugleiste.getHeight() +
          hauptmenu.getHeight() + pSuche.getHeight() + STD_ABSTAND;
      benoetigteHoehe += anzahlThumbnailsProZeile == 1 ? 150 : 50;
      
      /* MUSS GESETZT WERDEN!!! Ansonsten wird nur eine Zeile mit den
      Thumbnails angezeigt */
      pThumbnails.setPreferredSize(
          new Dimension((int) Math.ceil(benoetigteBreite),
              (int) Math.ceil(benoetigteHoehe)));
    
      for (ThumbnailAnzeigePanel tap : listeAnzeigePanel) {
        
        tap.setzeGroesze(sGroesze.getValue());
        tap.setVisible(true);
        pThumbnails.add(tap);
      }
      // Neuanordnung der Komponenten innerhalb pThumbnails
      pThumbnails.revalidate();
      
      // Neuzeichnen der Scrollpane da ansonsten Bildfragmente uebrig bleiben
      spThumbnails.repaint();
    }
  }
  
  /**
   * Waehlt aus der Liste der angezeigten Thumbnails das
   * letzte Bild ausgehend vom aktuell ausgewaehlten Bild aus. Wenn
   * kein Bild ausgewaehlt ist wird das letzte Bild der Liste ausgewaehlt.
   */
  public void waehleLetztesBildAus() {
    
    boolean gewaehltesBildGefunden = false;
    for (int i = 0; listeAnzeigePanel != null && !gewaehltesBildGefunden
                    && i < listeAnzeigePanel.size(); i++) {
      if (listeAnzeigePanel.get(i).istAusgewaehlt()) {
        gewaehltesBildGefunden = true;
        listeAnzeigePanel.get(i).setzeFokus(false);
        if (i == 0) {
          listeAnzeigePanel.get(listeAnzeigePanel.size() - 1).setzeFokus(true);
        } else {
          listeAnzeigePanel.get(i - 1).setzeFokus(true);
        }
      } else if (i == listeAnzeigePanel.size() - 1) {
        listeAnzeigePanel.get(listeAnzeigePanel.size() - 1).setzeFokus(true);
      }
    }
  }
  
  /**
   * Waehlt aus der Liste der angezeigten Thumbnails das naechste Bild
   * ausgehen vom aktuell ausgewaehlten Bild aus. Wenn kein Bild
   * ausgewaehlt ist wird das erste Bild der Liste ausgewaehlt.
   *
   */
  public void waehleNaechstesBildAus() {
    
    boolean gewaehltesBildGefunden = false;
    for (int i = 0; listeAnzeigePanel != null && !gewaehltesBildGefunden
                    && i < listeAnzeigePanel.size(); i++) {
      if (listeAnzeigePanel.get(i).istAusgewaehlt()) {
        gewaehltesBildGefunden = true;
        listeAnzeigePanel.get(i).setzeFokus(false);
        if (i == listeAnzeigePanel.size() - 1) {
          listeAnzeigePanel.get(0).setzeFokus(true);
        } else {
          listeAnzeigePanel.get(i + 1).setzeFokus(true);
        }
      } else if (i == listeAnzeigePanel.size() - 1) {
        listeAnzeigePanel.get(0).setzeFokus(true);
      }
    }
  }
  
  /**
   * Wenn der Benutzer mehrere Bilder im Anzeigebereich der Thumbnails
   * ausgewaehlt hat wird er gefragt ob die die Bilder auch von der
   * Festplatte geloescht werden sollen oder nicht.
   */
  public void loescheBilder() {
    
    int ergebnis = JOptionPane.showConfirmDialog(this, "Wollen Sie die " +
        "Bilder auch von der Festplatte loeschen?", "Löschen",
        JOptionPane.YES_NO_CANCEL_OPTION);
    boolean auchVonFestplatte = (ergebnis == JOptionPane.YES_OPTION) ? 
        true : false; 
    if (listeAnzeigePanel != null && ergebnis != JOptionPane.CANCEL_OPTION) {
      
      List<ThumbnailAnzeigePanel> zuLoeschendeBilder = 
        new ArrayList<ThumbnailAnzeigePanel>();
      
      for (ThumbnailAnzeigePanel tap : listeAnzeigePanel) {
        
        if (tap.istAusgewaehlt()) {
          
          try {
            zuLoeschendeBilder.add(tap);
            kern.entferne(tap.gibBildDokument(), auchVonFestplatte);
          } catch (EntferneException e) {
            zeigeFehlermeldung("Fehler beim Löschen", "Die Datei konnte " +
                "nicht von der Festplatte geloescht werden.\n" + 
                e.getMessage());
          }
        }
      }
      for (ThumbnailAnzeigePanel tap : zuLoeschendeBilder) {
        listeAnzeigePanel.remove(tap);
      }
    }
    erzeugeThumbnailansicht();
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
      spAnzeige.setDividerSize(TRENNBALKEN_GROESZE);
      spAnzeige.setRightComponent(getPThumbnailSteuerung());
      spAnzeige.setLeftComponent(getSpVorschauBildinfo());
      spAnzeige.addPropertyChangeListener("lastDividerLocation",
        new java.beans.PropertyChangeListener() {
          public void propertyChange(java.beans.PropertyChangeEvent e) {
            for (int i = 0; listeAnzeigePanel != null 
                            && i < listeAnzeigePanel.size(); i++) {
              ThumbnailAnzeigePanel tap = listeAnzeigePanel.get(i);
              tap.setzeGroesze(sGroesze.getValue());
            } 
          }
        });
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
      miImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
                                Event.CTRL_MASK, false));
      miImport.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          importiereDateien();
          if (listeAnzeigePanel != null && !listeAnzeigePanel.isEmpty()) {
            erzeugeThumbnailansicht();
            spThumbnails.revalidate();
            pThumbnails.revalidate();
          }
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
      pVorschau.setMinimumSize(new Dimension(10, 
          (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 4));
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
      pSuche = new SuchPanel(this);
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
      lLoeschen = new JLabel();
      lLoeschen.setText("");
      lLoeschen.setIcon(new ImageIcon(getClass().getResource("uiimgs/loeschen.png")));
      lLoeschen.setSize(new Dimension(32, 32));
      lLoeschen.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseClicked(java.awt.event.MouseEvent e) {    
      		loescheBilder();
      	}   
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
      	public void mouseClicked(java.awt.event.MouseEvent e) {    
      		waehleNaechstesBildAus();
      	}
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
      	public void mouseClicked(java.awt.event.MouseEvent e) {    
          waehleLetztesBildAus();
      	}   
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
      tbWerkzeugleiste.add(getBAuswahlAufheben());
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
      bGroszanzeige.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          if (listeAnzeigePanel != null) {
            boolean wurdeEinBildAusgewaehlt = false;
            int i = 0;
            while (!wurdeEinBildAusgewaehlt && i < listeAnzeigePanel.size()) {
              if (listeAnzeigePanel.get(i).istAusgewaehlt()) {
                wurdeEinBildAusgewaehlt = true;
              }
              i++;
            }
            if (wurdeEinBildAusgewaehlt) {
              groszanzeige.setExtendedState(JFrame.MAXIMIZED_BOTH);
              groszanzeige.setVisible(true);
            }
          }
        }
      });
    }
    return bGroszanzeige;
  }

  /**
   * This method initializes pThumbnailSteuerung	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPThumbnailSteuerung() {
    if (pThumbnailSteuerung == null) {
      pThumbnailSteuerung = new JPanel();
      pThumbnailSteuerung.setLayout(new BorderLayout());
      pThumbnailSteuerung.add(getTbWerkzeugleiste(), BorderLayout.NORTH);
      pThumbnailSteuerung.add(getSpThumbnails(), BorderLayout.CENTER);
    }
    return pThumbnailSteuerung;
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
      spVorschauBildinfo.setDividerSize(TRENNBALKEN_GROESZE);
      spVorschauBildinfo.setMinimumSize(new Dimension(MIN_BREITE_BILDINFO, 245));
      spVorschauBildinfo.setPreferredSize(new Dimension(MIN_BREITE_BILDINFO, 245));
      spVorschauBildinfo.setOneTouchExpandable(true);
      spVorschauBildinfo.setBottomComponent(getPBildinformationen());
      spVorschauBildinfo.setTopComponent(getPVorschau());
    }
    return spVorschauBildinfo;
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
      sGroesze.setMaximum(256);
      sGroesze.setValue(256);
      sGroesze.setMaximumSize(new Dimension(200, 16));
      sGroesze.setMinimum(48);
      sGroesze.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent e) {
          erzeugeThumbnailansicht();
        }
      });
    }
    return sGroesze;
  }

  /**
   * This method initializes tfSchluesselwoerter	
   * 	
   * @return javax.swing.JTextField	
   */
  private JTextField getTfSchluesselwoerter() {
    
    if (tfSchluesselwoerter == null) {
      tfSchluesselwoerter = new JTextField();
    }
    return tfSchluesselwoerter;
  }


  /**
   * This method initializes pBildinformationen	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPBildinformationen() {
    if (pBildinformationen == null) {
      pBildinformationen = new JPanel();
      pBildinformationen.setLayout(new BorderLayout());
      pBildinformationen.add(getPBilddetails(), BorderLayout.NORTH);
      pBildinformationen.add(getSpBilddetails(), BorderLayout.CENTER);
    }
    return pBildinformationen;
  }

  /**
   * This method initializes pBildinfoSchluesselBeschr	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPBilddetails() {
    
    if (pBilddetails == null) {
      
      // Bedingungen fuer den Textbereich zur Bildbeschreibung
      GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
      gridBagConstraints3.fill = GridBagConstraints.BOTH;
      gridBagConstraints3.gridx = 0;
      gridBagConstraints3.gridy = 3;
      gridBagConstraints3.ipadx = MIN_BREITE_BILDINFO;
      gridBagConstraints3.ipady = 50;
      gridBagConstraints3.weightx = 1.0;
      gridBagConstraints3.weighty = 1.0;
      gridBagConstraints3.insets = new Insets(0, 0, STD_ABSTAND, 0);
      
      // Bedingungen fuer das Label zur Bildbeschreibung
      GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
      gridBagConstraints2.insets = new Insets(0, STD_ABSTAND, STD_ABSTAND, 0);
      gridBagConstraints2.gridy = 2;
      gridBagConstraints2.ipadx = MIN_BREITE_BILDINFO;
      gridBagConstraints2.ipady = 5;
      gridBagConstraints2.anchor = GridBagConstraints.WEST;
      gridBagConstraints2.gridx = 0;
      
      // Bedingungen fuer das Textfeld zu den Schluesselwoertern
      GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
      gridBagConstraints1.fill = GridBagConstraints.BOTH;
      gridBagConstraints1.gridx = 0;
      gridBagConstraints1.gridy = 1;
      gridBagConstraints1.ipadx = MIN_BREITE_BILDINFO;
      gridBagConstraints1.ipady = 1;
      gridBagConstraints1.weightx = 1.0;
      gridBagConstraints1.anchor = GridBagConstraints.WEST;
      gridBagConstraints1.insets = new Insets(0, 0, STD_ABSTAND, 0);
      
      // Bedingungen fuer Label "Schluesselwoerter"
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(STD_ABSTAND, STD_ABSTAND,
          STD_ABSTAND, 0);
      gridBagConstraints.gridy = 0;
      gridBagConstraints.ipadx = MIN_BREITE_BILDINFO;
      gridBagConstraints.ipady = 5;
      gridBagConstraints.anchor = GridBagConstraints.WEST;
      gridBagConstraints.gridx = 0;
      
      // Erstellen der Komponenten und hinzufuegen zum Layout
      lBildbeschreibung = new JLabel();
      lBildbeschreibung.setText("Bildbeschreibung");
      lSchluesselwoerter = new JLabel();
      lSchluesselwoerter.setText("Schlüsselwörter");
      pBilddetails = new JPanel();
      pBilddetails.setLayout(new GridBagLayout());
      pBilddetails.setPreferredSize(new Dimension(MIN_BREITE_BILDINFO, 160));
      pBilddetails.add(lSchluesselwoerter, gridBagConstraints);
      pBilddetails.add(getTfSchluesselwoerter(), gridBagConstraints1);
      pBilddetails.add(lBildbeschreibung, gridBagConstraints2);
      pBilddetails.add(getTaBildbeschreibung(), gridBagConstraints3);
    }
    return pBilddetails;
  }

  /**
   * This method initializes tfBildbeschreibung	
   * 	
   * @return javax.swing.JTextPane	
   */
  private JTextPane getTaBildbeschreibung() {
    
    if (taBildbeschreibung == null) {
      taBildbeschreibung = new JTextPane();
      taBildbeschreibung.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }
    return taBildbeschreibung;
  }

  /**
 * This method initializes spBilddetails	
 * 	
 * @return javax.swing.JScrollPane	
 */
private JScrollPane getSpBilddetails() {
  if (spBilddetails == null) {
    spBilddetails = new JScrollPane();
    spBilddetails.setBorder(BorderFactory.createTitledBorder(null,
        "Bilddetails", TitledBorder.DEFAULT_JUSTIFICATION,
        TitledBorder.DEFAULT_POSITION, null, null));
    spBilddetails.setViewportView(getTBilddetails());
  }
  return spBilddetails;
}

/**
 * This method initializes tBilddetails	
 * 	
 * @return javax.swing.JTable	
 */
private JTable getTBilddetails() {
  if (tBilddetails == null) {
    tBilddetails = new JTable();
    tBilddetails.setModel(new BildinfoTabelModel());
    tBilddetails.setName("Bilddetails");
  }
  return tBilddetails;
}

  /**
   * This method initializes pThumbnails	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPThumbnails() {
    if (pThumbnails == null) {
      pThumbnails = new JPanel();
      pThumbnails.setLayout(new FlowLayout(FlowLayout.LEADING, STD_ABSTAND, STD_ABSTAND));
    }
    return pThumbnails;
  }

  /**
   * This method initializes jScrollPane	
   * 	
   * @return javax.swing.JScrollPane	
   */
  private JScrollPane getSpThumbnails() {
    if (spThumbnails == null) {
      JScrollBar jScrollBar = new JScrollBar();
      jScrollBar.setUnitIncrement(10);
      spThumbnails = new JScrollPane();
      spThumbnails.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      spThumbnails.setVerticalScrollBar(jScrollBar);
      spThumbnails.setViewportView(getPThumbnails());
    }
    return spThumbnails;
  }

  /**
   * This method initializes bAuswahlAufheben	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getBAuswahlAufheben() {
    if (bAuswahlAufheben == null) {
      bAuswahlAufheben = new JButton();
      bAuswahlAufheben.setText("Auswahl aufheben");
      bAuswahlAufheben.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          if (listeAnzeigePanel != null) {
            for (ThumbnailAnzeigePanel tap : listeAnzeigePanel) {
              tap.setzeFokus(false);
            }
          }
        }
      });
    }
    return bAuswahlAufheben;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

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
        hauptfenster.setExtendedState(JFrame.MAXIMIZED_BOTH);
      }
    });
  }

  /**
   * Initialisiert das dieses Objekt.
   */
  private void initialize() {
    this.setSize(new Dimension(STD_BREITE, STD_HOEHE));
    this.setJMenuBar(getHauptmenu());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setContentPane(getJContentPane());
    this.setTitle("JPictureProspector");
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent e) {
        erzeugeThumbnailansicht();
      }
    });
    groszanzeige = new BildGroszanzeige(this);
    groszanzeige.setVisible(false);
    tapObserver = new ArrayList<Observer>();
    tapObserver.add(pVorschau);
    tapObserver.add(groszanzeige.gibVorschaupanel());
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