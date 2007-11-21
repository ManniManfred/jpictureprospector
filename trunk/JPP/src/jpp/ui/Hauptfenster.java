package jpp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import jpp.client.JPPClient;
import jpp.core.AbstractJPPCore;
import jpp.core.BildDokument;
import jpp.core.CoreInterface;
import jpp.core.Einstellungen;
import jpp.core.JPPCore;
import jpp.core.Trefferliste;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ErzeugeException;
import jpp.core.exceptions.SucheException;
import jpp.ui.listener.AbortListener;
import jpp.ui.listener.BildimportListener;
import selectionmanager.ui.AuswaehlbaresJPanel;
import selectionmanager.ui.SelectionManagerComponent;
import settingsystem.core.SettingsDialog;
import settingsystem.reader.IniReader;
import settingsystem.writer.IniWriter;

import com.l2fprod.common.swing.JDirectoryChooser;

/**
 * Ein Objekt der Klasse stellt das Hauptanzeigefenster der Software zur
 * Verfuegung. Im Hauptfenster kann der Anwender alle Aktionen die zur Suche und
 * Anzeige der Bilder notwendig sind ausfuehren.
 * 
 * @author Nils Verheyen
 * @author Manfred Rosskamp
 */
public class Hauptfenster extends JFrame {

  /** Logger, der alle Fehler loggt. */
  Logger logger = Logger.getLogger("jpp.ui.Hauptfenster");

  /**
   * Gibt an welche Breite die Komponenten minimal im Bereich der
   * Bildinformationen haben duerfen.
   */
  private static final int MIN_BREITE_BILDINFO = 300;

  /** Gib an wie grosz ein Trennbalken einer Splitpane sein darf. */
  private static final int TRENNBALKEN_GROESZE = 7;

  /** Version UID der Software. */
  private static final long serialVersionUID = 1L;

  /** Enthaelt den Kern der Software mit dem operiert wird. */
  private AbstractJPPCore kern;
  
  /**
   * Der SelectionManager, der dafuer verantwortlich ist, dass die Bilder
   * richtig ausgewaehlt werden.
   */
  private SelectionManagerComponent sManager;

  /** Enthaelt die Inhaltsflaeche dieses Objekts. */
  private JPanel cpInhalt = null;

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

  private JLabel lLoeschen = null;

  private JMenuItem miLoeschen = null;

  private JSlider sGroesze = null;

  private JPanel pBildinformationen = null;

  private BilddetailsPanel pBilddetails = null;

  private JScrollPane spBilddetails = null;

  private MerkmaleJTable tBilddetails = null;

  private JScrollPane spThumbnails = null;

  private JMenuItem miAuswahlAufheben = null;

  private JMenuItem miAuswahlAlle = null;
  
  private JMenuItem miImportDir = null;

  private JMenuItem miAufraeumen = null;
  
  /**
   * Erstellt ein neues Objekt der Klasse.
   */
  public Hauptfenster(AbstractJPPCore kern) {
    super();

    /* Settings laden */
    loadSettings();

    /* JPPCore zuweisen */
    this.kern = kern;

    /* Graphic initialisieren */
    initialize();

  }

  /**
   * Laed alle Einstellungen aus der Datei settingFilename und speichert diese in
   * der Klasse Settings.
   */
  private void loadSettings() {
    IniReader loader = new IniReader();
    try {
      loader.loadSettings(Einstellungen.SETTING_FILENAME, Einstellungen.class);
    } catch (IOException e) {
      System.out.println("Konnte Einstellungen aus der" + " Datei "
          + Einstellungen.SETTING_FILENAME + " nicht laden.");
    }
  }

  /**
   * Speichert alle Einstellungen aus der Klasse Settings.
   */
  private void saveSettings() {
    IniWriter writer = new IniWriter();
    try {
      writer.writeSettings(Einstellungen.SETTING_FILENAME, new Einstellungen());
    } catch (IOException e) {
      System.out.println("Konnte Einstellungen in der " + " Datei "
          + Einstellungen.SETTING_FILENAME + " nicht speichern.");
    }
  }

  /**
   * Oeffnent den Einstellungs-Dialog.
   */
  private void oeffneEinstellungsDialog() {
    SettingsDialog settingsDialog = new SettingsDialog(this, "Einstellungen",
        true, new Einstellungen());
    settingsDialog.setVisible(true);

    if (settingsDialog.wurdenSettingsGeaendert()) {
      // werteDatenAus();
      // TODO irgendwelche aenderungen
    }
  }
  
  /**
   * Importiert in den Kern des Programms eine ausgewaehlte Anzahl
   * an Dateien.
   */
  private void dialogImportFiles() {
    JFileChooser dateiauswahl;
    
    if (Einstellungen.importStartOrdner != null) {
      dateiauswahl = new JFileChooser(Einstellungen.importStartOrdner);
    } else {
      dateiauswahl = new JFileChooser();
    }
    
    // Enthaelt den Filter fuer die zu importierenden Dateien
    FileFilter filter = new Bildfilter();
    File[] files;
    dateiauswahl.setMultiSelectionEnabled(true);
    dateiauswahl.setFileFilter(filter);
    final int ergebnis = dateiauswahl.showOpenDialog(cpInhalt);
    files = dateiauswahl.getSelectedFiles();
    
    if (ergebnis == JFileChooser.OPEN_DIALOG) {
      // Merke den Pfad zu Oberverzeichnis 
      if (files.length > 0) {
        Einstellungen.importStartOrdner = files[0].getParent();
      }
      importiere(files);
    }
  }
  
  /**
   * Wird aufgerufen, wenn ein Verzeichnis oder ein Verzeichnis mit
   * mehreren Unterverzeichnissen und die darin enthaltenen Bilddateien
   * in den Programmkern importiert werden sollen.
   */
  private void dialogImportDirectorys() {
    
    JDirectoryChooser chooser;
    if (Einstellungen.importStartOrdner != null) {
      chooser = new JDirectoryChooser(Einstellungen.importStartOrdner);
    } else {
      chooser = new JDirectoryChooser();
    }
    
    chooser.setMultiSelectionEnabled(true);
    chooser.setShowingCreateDirectory(false);
    File[] dirs;
    Set<File> files = new HashSet<File>();
    
    // Nur Verzeichnisse sollen ausgewaehlt werden
    final int ergebnis = chooser.showOpenDialog(this);
    
    
    // Enthaelt einen Loader der alle Dateinamen holt
    dirs = chooser.getSelectedFiles();
    
    // Merke den Pfad zu Oberverzeichnis 
    if (dirs.length > 0) {
      Einstellungen.importStartOrdner = dirs[0].getParent();
    }
    
    for (int i = 0; i < dirs.length; i++) {
      VerzeichnisLader loader = 
          new VerzeichnisLader(dirs[i].getAbsolutePath());
        files.addAll(loader.ladeVerzeichnis(false));
    }
    if (ergebnis == JFileChooser.OPEN_DIALOG) {
      importiere(files.toArray(new File[files.size()]));
    }
  }
  
  /**
   * Importiert eine bestimmte Anzahl an Dateien in den Systemkern.
   * @param files  die zu importierenden Dateien
   */
  private void importiere(File[] files) {
    
    final Bildimportierer importierer = new Bildimportierer(files, kern);
    int anzahlDateien = files == null ? 0 : files.length;
    final LadebalkenDialog ladebalken =
      new LadebalkenDialog(this, anzahlDateien);
    ladebalken.setzeAnzahl(0);
    
    ladebalken.addAbortListener(new AbortListener() {
      public void abort() {
        importierer.brecheVorgangAb();
        ladebalken.dispose();
      }
    });
    
    /* Listener hinzufuegen die entsprechende Aktionen ausfuehren, wenn
     * neue Bilder geladen wurden. */
    importierer.addBildImportiertListener(new BildimportListener() {
      public void bildImportiert() {
        // Um zu verhindern, dass es beim einem Import von sehr vielen Bildern
        // Heap Overflow kommt, werden keine Bilder angezeigt.
        ladebalken.setzeAnzahl(ladebalken.gibAnzahl() + 1);
      }
      public void ladevorgangAbgeschlossen() {
        ladebalken.dispose();
      }
    });
    
    importierer.start();
    ladebalken.setLocation((this.getWidth() - ladebalken.getWidth()) / 2,
        (this.getHeight() - ladebalken.getHeight()) / 2);
    ladebalken.setVisible(true);
  }

  /**
   * Fuehrt im Kern die Suche durch und zeigt das Ergebnis an.
   * 
   * @param suchtext
   */
  public void sucheNach(String suchtext) {
    try {
      /* TODO offset ueber UI steuern */
      Trefferliste trefferliste = kern.suche(suchtext, 0, Einstellungen.ANZAHL_ERGEBNISSE);

      /* Entferne alle vorher angezeigten TAPs */
      clearAnzeige();

      /* Erzeuge fuer alle Treffer ein TAP und zeige diesen an */
      for (int i = 0; i < trefferliste.getAnzahlTreffer(); i++) {
        erzeugeTAPundAdde(trefferliste.getBildDokument(i));
      }

    } catch (SucheException se) {
      verarbeiteFehler("Suche fehlgeschlagen", "Die Suche konnte "
          + "nicht erfolgreich ausgeführt werden.\n\n" + se.getMessage());
    }
  }


  /**
   * Erzeugt ein neue ThumbnailAnzeigePanel aus dem BildDokument und fuegt es
   * dem SelectionManager und dem AnzeigePanel hinzu
   * 
   * @param dok
   */
  public void erzeugeTAPundAdde(BildDokument dok) {

    /* Tap erzeugen und dem Anzeige Panel hinzufuegen */
    ThumbnailAnzeigePanel tap = new ThumbnailAnzeigePanel(dok, sGroesze
        .getValue());
    // tap.setzeGroesze(sGroesze.getValue());
    tap.setVisible(true);


    /* Neuzeichnen der Scrollpane da ansonsten Bildfragmente uebrig bleiben */
    spThumbnails.repaint();
    spThumbnails.revalidate();

    /* Dem SelectionManager hinzufuegen */
    getSManager().addAuswaehlbar(tap);
  }


  /**
   * Nimmt alle TAPs aus dem SelectionManager und der PThumbnails raus.
   */
  private void clearAnzeige() {

    /* Entferne alle Auswaehlbaren Elemente aus der SelectionManagerComponete */
    getSManager().removeAllAuswaehlbar();
    getSManager().revalidate();
    getSManager().repaint();
  }



  /**
   * Zeigt dem Benutzer eine Fehlermeldung an.
   * 
   * @param titel der Titel der Fehlermeldung
   * @param meldung die Fehlermeldung
   */
  private void verarbeiteFehler(String titel, String meldung) {
    verarbeiteFehler(titel, meldung, Level.WARNING, null);
  }

  /**
   * Zeigt dem Benutzer eine Fehlermeldung an.
   * 
   * @param titel der Titel der Fehlermeldung
   * @param meldung die Fehlermeldung
   */
  private void verarbeiteFehler(String titel, String meldung, Level level,
      Throwable fehler) {

    /* Alle Fehler mit loggen */
    logger.log(level, titel + ": " + meldung, fehler);

    /* Bei gravierenden Fehlern ein Nachricht an den User geben */
    if (level.intValue() > Level.WARNING.intValue()) {
      JOptionPane.showMessageDialog(this, meldung, titel,
          JOptionPane.ERROR_MESSAGE);
    } else if (level.intValue() == Level.WARNING.intValue()) {
      JOptionPane.showMessageDialog(this, meldung, titel,
          JOptionPane.WARNING_MESSAGE);
    } else {
      /*
       * Fehlerlevel ist zu gering, sodass hier keine Fehlermeldung geworfen
       * wird
       */
    }
  }

  /**
   * Wenn der Benutzer mehrere Bilder im Anzeigebereich der Thumbnails
   * ausgewaehlt hat wird er gefragt ob die die Bilder auch von der Festplatte
   * geloescht werden sollen oder nicht.
   */
  public void loescheBilder() {

    Set<AuswaehlbaresJPanel> ausgewaehlten = sManager.gibAlleAusgewaehlten();

    /*
     * Wenn Bilder ausgewaehlt wurden
     */
    if (ausgewaehlten != null && ausgewaehlten.size() > 0) {

      /* Frage nach, ob auch von der Festplatte entfernt werden soll */
      int ergebnis = 0;
      boolean auchVonFestplatte = false;


      ergebnis = JOptionPane.showConfirmDialog(this, "Wollen Sie die "
          + "Bilder auch von der Festplatte loeschen?", "L\u00f6schen",
          JOptionPane.YES_NO_CANCEL_OPTION);
      auchVonFestplatte = (ergebnis == JOptionPane.YES_OPTION)
          ? true
          : false;


      if (ergebnis != JOptionPane.CANCEL_OPTION) {

        /* Jetzt tatsaechlich alle BildDokumente loeschen */
        for (AuswaehlbaresJPanel p : ausgewaehlten) {
          try {

            /* Entferne diese aus dem Kern */
            ThumbnailAnzeigePanel tap = (ThumbnailAnzeigePanel) p;
            kern.entferne(tap.gibBildDokument(), auchVonFestplatte);


            /* Entferne diese aus dem SelectionManager */
            sManager.removeAuswaehlbar(p);

          } catch (EntferneException e) {
            verarbeiteFehler("Entferne Fehler", e.getMessage(), Level.WARNING, e);
          }
        }

        /* Ansicht aktualisieren */
        pVorschau.resetAnsicht();

        /* Neuzeichnen und neu Anordnen */
        sManager.repaint();
        sManager.revalidate();
        sManager.leereAuswahl();
      }

    }

  }

  /**
   * Liefert die Tabelle, in der die Grundmerkmale geladen sind.
   * 
   * @return die Tabelle fuer die Grundmerkmale
   */
  public MerkmaleJTable gibMerkmaleTable() {
    return this.tBilddetails;
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
      mDatei.add(getMiImportDir());
      mDatei.add(getMiBeenden());
    }
    return mDatei;
  }

  /**
   * Beendet dieses Anwendung nach einer Nachfrage beim Benutzer.
   */
  private void beende() {
    if (Einstellungen.SAVE_FENSTER_GROESSE) {
      Einstellungen.FENSTER_BREITE = this.getWidth();
      Einstellungen.FENSTER_HOEHE = this.getHeight();
    }

    if (Einstellungen.SAVE_FENSTER_POSITION) {
      Einstellungen.FENSTER_POSX = this.getX();
      Einstellungen.FENSTER_POSY = this.getY();
    }

    if (Einstellungen.SAVE_THUMB_SIZE) {
      Einstellungen.THUMB_SIZE = getSGroesze().getValue();
    }
    /* Einstellungen speichern */
    saveSettings();

    System.exit(0);
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
          beende();
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
          dialogImportFiles();
        }
      });
    }
    return miImport;
  }

  /**
   * This method initializes mBearbeiten
   * 
   * @return javax.swing.JMenu
   */
  private JMenu getMEinstellungen() {
    if (mEinstellungen == null) {
      mEinstellungen = new JMenu();
      mEinstellungen.setText("Bearbeiten");
      mEinstellungen.setMnemonic(KeyEvent.VK_B);
      mEinstellungen.add(getMiLoeschen());
      mEinstellungen.add(getMiAuswahlAufheben());
      mEinstellungen.add(getMiAufraeumen());
      mEinstellungen.add(getMiAuswahlAlle());
      mEinstellungen.add(new JSeparator());
      mEinstellungen.add(getMiEinstellungen());
    }
    return mEinstellungen;
  }

  private JMenuItem getMiEinstellungen() {
    JMenuItem einst = new JMenuItem("Einstellungen");
    einst.setMnemonic(KeyEvent.VK_E);
    einst.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        oeffneEinstellungsDialog();
      }

    });

    return einst;
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
          JOptionPane.showMessageDialog(cpInhalt, "Programmierprojekt "
              + "der FH Gelsenkirchen\n\n"
              + "JPictureProspector\n\n"
              + "Autoren:\n" 
              + "Nils Verheyen\n"
              + "Marion Mecking\n"
              + "Manfred Rosskamp\n\n"
              + "v1.0", "Info",
              JOptionPane.INFORMATION_MESSAGE);
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
      pVorschau.setMinimumSize(new Dimension(10, (int) Toolkit
          .getDefaultToolkit().getScreenSize().getHeight() / 4));
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
      lLoeschen.setIcon(new ImageIcon(getClass().getResource(
          "uiimgs/loeschenTrash.png")));
      lLoeschen.setSize(new Dimension(32, 32));
      lLoeschen.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent e) {
          loescheBilder();
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
          lLoeschen.removeAll();
          lLoeschen.setIcon(new ImageIcon(getClass().getResource(
              "uiimgs/loeschenTrash.png")));
        }

        public void mouseEntered(java.awt.event.MouseEvent e) {
          lLoeschen.removeAll();
          lLoeschen.setIcon(new ImageIcon(getClass().getResource(
              "uiimgs/loeschenTrashKlick.png")));
        }
      });
      
      tbWerkzeugleiste = new JToolBar();
      tbWerkzeugleiste.add(getBGroszanzeige());
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
          ThumbnailAnzeigePanel markiertesTAP = (ThumbnailAnzeigePanel) sManager
              .getMarkiert();

          if (markiertesTAP != null) {
            BildGroszanzeige anzeige = new BildGroszanzeige(
                (List<ThumbnailAnzeigePanel>) sManager.gibAlleAuswaehlbaren(),
                markiertesTAP.gibBildDokument());
            anzeige.setVisible(true);
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
      spVorschauBildinfo.setDividerLocation(250);
      spVorschauBildinfo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      spVorschauBildinfo.setDividerSize(TRENNBALKEN_GROESZE);
      spVorschauBildinfo
          .setMinimumSize(new Dimension(MIN_BREITE_BILDINFO, 245));
      spVorschauBildinfo.setPreferredSize(new Dimension(MIN_BREITE_BILDINFO,
          245));
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
      miLoeschen.setText("Ausgew\u00e4hlte Bilder l\u00f6schen");
      miLoeschen.setMnemonic(KeyEvent.VK_L);
      miLoeschen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0,
          false));
      miLoeschen.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          loescheBilder();
        }
      });
    }
    return miLoeschen;
  }

  /**
   * Initialisiert die Leiste fuer die Groesze der Thumbnails.
   * 
   * @return javax.swing.JSlider liefert den initialisierten Schieber
   */
  private JSlider getSGroesze() {
    if (sGroesze == null) {
      sGroesze = new JSlider();
      sGroesze.setMinimum(Einstellungen.SLIDER_MIN);
      sGroesze.setMaximum(Einstellungen.SLIDER_MAX);

      if (Einstellungen.SAVE_THUMB_SIZE) {
        sGroesze.setValue(Einstellungen.THUMB_SIZE);
      } else {
        sGroesze.setValue(Einstellungen.SLIDER_VALUE);
      }
      sGroesze.setMaximumSize(new Dimension(200, 16));
      sGroesze.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent e) {
          updateGroesze();
        }
      });
    }
    return sGroesze;
  }

  /**
   * Aktuallisiert bei allen TAPs die Groesze.
   */
  private void updateGroesze() {
    JComponent cont = sManager.getContainerLayer();

    int anzahl = cont.getComponentCount();

    for (int i = 0; i < anzahl; i++) {
      ThumbnailAnzeigePanel tap = (ThumbnailAnzeigePanel) cont.getComponent(i);
      tap.setzeGroesze(getSGroesze().getValue());
    }
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
  private BilddetailsPanel getPBilddetails() {

    if (pBilddetails == null) {

      pBilddetails = new BilddetailsPanel(this.getTBilddetails(), kern);
    }
    return pBilddetails;
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
  private MerkmaleJTable getTBilddetails() {
    if (tBilddetails == null) {

      tBilddetails = new MerkmaleJTable(this.kern);
    }
    return tBilddetails;
  }



  /**
   * This method initializes jScrollPane
   * 
   * @return javax.swing.JScrollPane
   */
  private JScrollPane getSpThumbnails() {
    if (spThumbnails == null) {
      spThumbnails = new JScrollPane();
      spThumbnails.setViewportView(getSManager());
      
      spThumbnails.getViewport().setBackground(new Color(240,240,240));
    }
    return spThumbnails;
  }

  /**
   * This method initializes miAuswahlAufheben
   * 
   * @return javax.swing.JMenuItem
   */
  private JMenuItem getMiAuswahlAufheben() {
    if (miAuswahlAufheben == null) {
      miAuswahlAufheben = new JMenuItem();
      miAuswahlAufheben.setText("Auswahl aufheben");
      miAuswahlAufheben.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
          Event.CTRL_MASK | Event.SHIFT_MASK, false));
      miAuswahlAufheben.setMnemonic(KeyEvent.VK_A);
      miAuswahlAufheben.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          sManager.leereAuswahl();
        }
      });
    }
    return miAuswahlAufheben;
  }
  
  /**
   * This method initializes miImportDir  
   *  
   * @return javax.swing.JMenuItem  
   */
  private JMenuItem getMiImportDir() {
    if (miImportDir == null) {
      miImportDir = new JMenuItem();
      miImportDir.setText("Verzeichnis importieren");
      miImportDir.setMnemonic(KeyEvent.VK_V);
      miImportDir.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          dialogImportDirectorys();
        }
      });
    }
    return miImportDir;
  }
  
  /**
   * This method initializes miAufraeumen 
   *  
   * @return javax.swing.JMenuItem  
   */
  private JMenuItem getMiAufraeumen() {
    if (miAufraeumen == null) {
      miAufraeumen = new JMenuItem();
      miAufraeumen.setText("Aufr\u00e4umen");
      miAufraeumen.setMnemonic(KeyEvent.VK_R);
      miAufraeumen.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          try {
            kern.clearUpIndex();
          } catch (SucheException se) {
            verarbeiteFehler("Fehler", se.getMessage());
          }
        }
      });
    }
    return miAufraeumen;
  }

  /**
   * This method initializes miAuswahlAufheben
   * 
   * @return javax.swing.JMenuItem
   */
  private JMenuItem getMiAuswahlAlle() {
    if (miAuswahlAlle == null) {
      miAuswahlAlle = new JMenuItem();
      miAuswahlAlle.setText("Waehle alle Bilder aus");
      miAuswahlAlle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
          Event.CTRL_MASK, false));
      miAuswahlAlle.setMnemonic(KeyEvent.VK_W);
      miAuswahlAlle.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          sManager.waehleAlleAus();
        }
      });
    }
    return miAuswahlAlle;
  }

  

  /**
   * Initialisiert das dieses Objekt.
   */
  private void initialize() {

    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    if (Einstellungen.FENSTER_BREITE == d.width && Einstellungen.FENSTER_HOEHE == d.height) {
      this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    } else { 
      this.setSize(new Dimension(Einstellungen.FENSTER_BREITE,
        Einstellungen.FENSTER_HOEHE));
    }
    this.setLocation(Einstellungen.FENSTER_POSX, Einstellungen.FENSTER_POSY);
    this.setJMenuBar(getHauptmenu());
    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        beende();
      }
    });

    this.setContentPane(getJContentPane());
    this.setTitle("JPictureProspector");
  }

  private SelectionManagerComponent getSManager() {
    if (sManager == null) {
      /* Den SelectionManager initialisieren */
      sManager = new SelectionManagerComponent();
      sManager.addAuswahlListener(getPVorschau());
      sManager.addAuswahlListener(getTBilddetails());
      sManager.addAuswahlListener(getPBilddetails());
    }
    return sManager;
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane() {
    if (cpInhalt == null) {
      cpInhalt = new JPanel();
      cpInhalt.setLayout(new BorderLayout());
      cpInhalt.add(getSuchPanel(), BorderLayout.NORTH);
      cpInhalt.add(getSpAnzeige(), BorderLayout.CENTER);
    }
    return cpInhalt;
  }

}