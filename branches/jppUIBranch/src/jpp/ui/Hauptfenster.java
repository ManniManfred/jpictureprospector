package jpp.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import jpp.core.Einstellungen;
import jpp.core.JPPCore;
import jpp.core.Trefferliste;
import jpp.core.exceptions.EntferneException;
import jpp.core.exceptions.ErzeugeException;
import jpp.core.exceptions.SucheException;
import jpp.ui.listener.BildAusgewaehltListener;
import jpp.ui.listener.BildimportListener;
import settingsystem.core.SettingsDialog;
import settingsystem.reader.IniReader;
import settingsystem.writer.IniWriter;

/**
 * Ein Objekt der Klasse stellt das Hauptanzeigefenster der Software zur
 * Verfuegung. Im Hauptfenster kann der Anwender alle Aktionen die
 * zur Suche und Anzeige der Bilder notwendig sind ausfuehren.
 */
public class Hauptfenster extends JFrame {
  
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
  
  /** Enthaelt eine Liste an Paneln die zustaendig fuer die Anzeige der
   * Thumbnails sind.
   */
  private List<ThumbnailAnzeigePanel> listeAnzeigePanel = null;  //  @jve:decl-index=0:
  
  /** Enthaelt die Trefferliste nach einer ausgeführten Suche. */
  private Trefferliste trefferliste = null;
  
  /** Enthaelt das zuletzte gewaehlte Panel. */
  private ThumbnailAnzeigePanel zuletztGewaehltesPanel = null;
  
  /** Enthaelt den Index des zuletzt gewaehlten Panels. */
  private int indexZuletztGewaehltesPanel = -1;
  
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
  
  private JLabel lLetztesBild = null;
  
  private JLabel lNaechstesBild = null;
  
  private JLabel lLoeschen = null;
  
  private JMenuItem miLoeschen = null;
  
  private JSlider sGroesze = null;  
  private JPanel pBildinformationen = null;
  
  private JPanel pBilddetails = null;
  
  private JScrollPane spBilddetails = null;
  
  private MerkmaleJTable tBilddetails = null;
  
  private ScrollableFlowPanel pThumbnails = null;
  
  private JScrollPane spThumbnails = null;
  
  private JButton bAuswahlAufheben = null;

  private JMenuItem miAuswahlAufheben = null;

  private JMenuItem miImportDir = null;
  
  /**
   * Erstellt ein neues Objekt der Klasse.
   */
  public Hauptfenster() {
    super();

    /* Settings laden */
    loadSettings();
    
    /* JPPCore erzeugen */
    try {
      kern = new JPPCore();
    } catch (ErzeugeException e) {
      e.printStackTrace();
    }

    /* Graphic initialisieren */
    initialize();
    
  }
  /**
   * Läd alle Einstellungen aus der Datei settingFilename
   * und speichert diese in der Klasse Settings.
   */
  private void loadSettings() {
    IniReader loader = new IniReader();
    try {
      loader.loadSettings(Einstellungen.SETTING_FILENAME, Einstellungen.class);
    } catch (IOException e) {
      System.out.println("Konnte Einstellungen aus der"
          + " Datei " + Einstellungen.SETTING_FILENAME + " nicht laden.");
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
      System.out.println("Konnte Einstellungen in der "
          + " Datei " + Einstellungen.SETTING_FILENAME + " nicht speichern.");
    }
  }
  
  /**
   * Oeffnent den Einstellungs-Dialog.
   */
  private void oeffneEinstellungsDialog() {
    SettingsDialog settingsDialog = new SettingsDialog(this, 
        "Einstellungen", true, new Einstellungen());
    settingsDialog.setVisible(true);
    
    if (settingsDialog.wurdenSettingsGeaendert()) {
      //werteDatenAus();
      //TODO irgendwelche aenderungen
    }
  }
  
  /**
   * Importiert in den Kern des Programms eine ausgewaehlte Anzahl
   * an Dateien.
   */
  private void dialogImportFiles() {
    
    JFileChooser dateiauswahl = new JFileChooser();
    
    // Enthaelt den Filter fuer die zu importierenden Dateien
    FileFilter filter = new Bildfilter();
    File[] files;
    dateiauswahl.setMultiSelectionEnabled(true);
    dateiauswahl.setFileFilter(filter);
    final int ergebnis = dateiauswahl.showOpenDialog(cpInhalt);
    files = dateiauswahl.getSelectedFiles();
    
    if (ergebnis == JFileChooser.OPEN_DIALOG) {
      importiere(files);
    }
  }
  
  /**
   * Wird aufgerufen, wenn ein Verzeichnis oder ein Verzeichnis mit
   * mehreren Unterverzeichnissen und die darin enthaltenen Bilddateien
   * in den Programmkern importiert werden sollen.
   */
  private void dialogImportDirectorys() {
    
    // Enthaelt das Accessory fuer den FileChooser
    JCheckBox cbAccessory = new JCheckBox("inklusive Unterverzeichnisse", false);
    JFileChooser dateiauswahl = new JFileChooser();
    File[] files;
    dateiauswahl.setMultiSelectionEnabled(false);
    
    // Nur Verzeichnisse sollen ausgewaehlt werden
    dateiauswahl.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    dateiauswahl.setAccessory(cbAccessory);
    final int ergebnis = dateiauswahl.showOpenDialog(cpInhalt);
    
    // Enthaelt einen Loader der alle Dateinamen holt
    VerzeichnisLader loader = 
      new VerzeichnisLader(dateiauswahl.getSelectedFile().getAbsolutePath());
    boolean mitSubDir = ((JCheckBox) dateiauswahl.getAccessory()).isSelected();
    files = loader.ladeVerzeichnis(mitSubDir);
    
    if (ergebnis == JFileChooser.OPEN_DIALOG) {
      importiere(files);
    }
  }
  
  /**
   * Importiert eine bestimmte Anzahl an Dateien in den Systemkern.
   * @param files  die zu importierenden Dateien
   */
  private void importiere(File[] files) {
    
    final Bildimportierer importierer = new Bildimportierer(files, kern,
        sGroesze.getValue(), tapObserver);
    int anzahlDateien = files == null ? 0 : files.length;
    final LadebalkenDialog ladebalken =
      new LadebalkenDialog(this, anzahlDateien);
    ladebalken.setzeAnzahl(0);
    resetAnsicht();
    
    /* Listener hinzufuegen die entsprechende Aktionen ausführen, wenn
     * neue Bilder geladen wurden. */
    importierer.addBildImportiertListener(new BildimportListener() {
      public void bildImportiert() {
        listeAnzeigePanel = importierer.gibAnzeigePanel();
        erzeugeThumbnailansicht();
        ladebalken.setzeAnzahl(ladebalken.gibAnzahl() + 1);
      }
      public void ladevorgangAbgeschlossen() {
        ladebalken.dispose();
        setzeBildAusgewaehltListener();
      }
    });
    
    importierer.start();
    ladebalken.setLocation((this.getWidth() - ladebalken.getWidth()) / 2,
        (this.getHeight() - ladebalken.getHeight()) / 2);
    ladebalken.setVisible(true);
  }
  
  /**
   * Erzeugt alle notwendigen Daten, die zur Anzeige notwendig sind, wenn
   * der Benutzer nach einem Begriff gesucht hat.
   */
  public void erzeugeDatenNachSuche() {
    
    try {
      trefferliste = kern.suche(pSuche.gibSuchtext());
      resetAnsicht();
      for (int i = 0; i < trefferliste.getAnzahlTreffer(); i++) {
        ThumbnailAnzeigePanel tap = 
          new ThumbnailAnzeigePanel(trefferliste.getBildDokument(i),
                sGroesze.getValue(), tapObserver, i);
        listeAnzeigePanel.add(tap);
      }
      setzeBildAusgewaehltListener();
    } catch (SucheException se) {
      zeigeFehlermeldung("Suche fehlgeschlagen", "Die Suche konnte " +
          "nicht erfolgreich ausgeführt werden.\n\n" + se.getMessage());
    }
  }
  
  /**
   * Wird aufgerufen, wenn eine Suche oder Importvorgang durchgefuehrt
   * wurde und stellt die Grundansicht des Fensters widerher.
   */
  private void resetAnsicht() {
    
    if (listeAnzeigePanel == null) {
      listeAnzeigePanel = new ArrayList<ThumbnailAnzeigePanel>();
    } else {
      for (ThumbnailAnzeigePanel tap : listeAnzeigePanel) {
        if (tap.istAusgewaehlt()) {
          tap.setzeFokus(false);
        }
      }
      listeAnzeigePanel.clear();
      pThumbnails.removeAll();
      pVorschau.resetAnsicht();
    }
  }
  
  /**
   * Zeigt dem Benutzer eine Fehlermeldung an.
   *
   * @param titel  der Titel der Fehlermeldung
   * @param meldung  die Fehlermeldung
   */
  private void zeigeFehlermeldung(String titel, String meldung) {
    
    JOptionPane.showMessageDialog(this, meldung, titel,
        JOptionPane.ERROR_MESSAGE);
  }
  
  /**
   * Zeigt alle Thumbnails innerhalb des entsprechenden Anzeigebereichs
   * an, mit den entsprechenden Eigenschaften von oben nach unten scrollen
   * zu koennen und die Groesze der Thumbnails dynamisch anzupassen.
   */
  public void erzeugeThumbnailansicht() {
    
    
    if (listeAnzeigePanel != null) {
      
      pThumbnails.removeAll();
      
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
    
    if (listeAnzeigePanel != null) {
      for (ThumbnailAnzeigePanel tap : listeAnzeigePanel) {
        if (tap != zuletztGewaehltesPanel && tap.istAusgewaehlt()) {
          tap.setzeFokus(false);
        }
      }
      if (indexZuletztGewaehltesPanel == -1) {
        listeAnzeigePanel.get(listeAnzeigePanel.size() - 1).setzeFokus(true);
      } else if (indexZuletztGewaehltesPanel == 0) {
        listeAnzeigePanel.get(0).setzeFokus(false);
        listeAnzeigePanel.get(listeAnzeigePanel.size() - 1).setzeFokus(true);
      } else {
        listeAnzeigePanel.get(indexZuletztGewaehltesPanel).setzeFokus(false);
        listeAnzeigePanel.get(indexZuletztGewaehltesPanel - 1).setzeFokus(true);
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
    
    if (listeAnzeigePanel != null) {
      for (ThumbnailAnzeigePanel tap : listeAnzeigePanel) {
        if (tap != zuletztGewaehltesPanel && tap.istAusgewaehlt()) {
          tap.setzeFokus(false);
        }
      }
      if (indexZuletztGewaehltesPanel == -1) {
        listeAnzeigePanel.get(0).setzeFokus(true);
      } else if (indexZuletztGewaehltesPanel == listeAnzeigePanel.size() - 1) {
        listeAnzeigePanel.get(listeAnzeigePanel.size() - 1).setzeFokus(false);
        listeAnzeigePanel.get(0).setzeFokus(true);
      } else {
        listeAnzeigePanel.get(indexZuletztGewaehltesPanel).setzeFokus(false);
        listeAnzeigePanel.get(indexZuletztGewaehltesPanel + 1).setzeFokus(true);
      }
    }
  }
  
  /**
   * Wenn der Benutzer mehrere Bilder im Anzeigebereich der Thumbnails
   * ausgewaehlt hat wird er gefragt ob die die Bilder auch von der
   * Festplatte geloescht werden sollen oder nicht.
   */
  public void loescheBilder() {
    
    int ergebnis = 0;
    boolean auchVonFestplatte = false;
    if (listeAnzeigePanel !=  null) {
      ergebnis = JOptionPane.showConfirmDialog(this, "Wollen Sie die " +
          "Bilder auch von der Festplatte loeschen?", "Löschen",
          JOptionPane.YES_NO_CANCEL_OPTION);
      auchVonFestplatte = (ergebnis == JOptionPane.YES_OPTION) ?
        true : false;
    }
    
    if (listeAnzeigePanel != null && ergebnis != JOptionPane.CANCEL_OPTION) {
      
      ArrayList<ThumbnailAnzeigePanel> zuLoeschendeBilder 
        = new ArrayList<ThumbnailAnzeigePanel>();
      for (ThumbnailAnzeigePanel tap : listeAnzeigePanel) {
	
      	if (tap.istAusgewaehlt()) {
      	  
      	  try {
      	    tap.setzeFokus(false);
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
    pVorschau.resetAnsicht();
    erzeugeThumbnailansicht();
  }
  
  /**
   * Setzt fuer alle <code>ThumbnailAnzeigePanel</code> die entsprechenden
   * Listener.
   */
  private void setzeBildAusgewaehltListener() {
    
    if (listeAnzeigePanel != null) {
      for (ThumbnailAnzeigePanel tap : listeAnzeigePanel) {
        tap.addBildAusgewaehltListener(new BildAusgewaehltListener() {
          public void setzeZuletztAusgewaehltesBild(ThumbnailAnzeigePanel tap,
              int index) {
            if (tap.istAusgewaehlt()) {
              zuletztGewaehltesPanel = tap;
              indexZuletztGewaehltesPanel = index;
            }
          }
        });
      }
    }
  }
  
  /**
   * Setzt das zuletzt ausgewaehlte <code>ThumbnailAnzeigePanel</code>.
   * @param tap  das zuletzt ausgewaehlte <code>ThumbnailAnzeigePanel</code>
   */
  public void setzeZuletztAusgewaehltesBild(ThumbnailAnzeigePanel tap) {
    this.zuletztGewaehltesPanel = tap;
  }
  
  /**
   * Liefert die Tabelle, in der die Grundmerkmale geladen sind.
   * @return  die Tabelle fuer die Grundmerkmale
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
   *
   */
  private void beende() {
//    int ergebnis = JOptionPane.showConfirmDialog(cpInhalt,
//        "Wollen Sie das Programm beenden?", "Beenden",
//        JOptionPane.OK_CANCEL_OPTION);
//    
//    if (ergebnis == JOptionPane.OK_OPTION) {
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
//    }
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
      miImport.setText("Dateien Importieren");
      miImport.setMnemonic(KeyEvent.VK_I);
      miImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
          Event.CTRL_MASK, false));
      miImport.addActionListener(new java.awt.event.ActionListener() {
      	public void actionPerformed(java.awt.event.ActionEvent e) {
      	  dialogImportFiles();
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
      mEinstellungen.setText("Bearbeiten");
      mEinstellungen.setMnemonic(KeyEvent.VK_B);
      mEinstellungen.add(getMiLoeschen());
      mEinstellungen.add(getMiAuswahlAufheben());
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
      	  JOptionPane.showMessageDialog(cpInhalt, "Programmierprojekt " +
      	      "der FH Gelsenkirchen\n\nJPictureProspector\n\nv1.0",
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
      lLoeschen.setIcon(new ImageIcon(getClass().getResource("uiimgs/loeschenTrash.png")));
      lLoeschen.setSize(new Dimension(32, 32));
      lLoeschen.addMouseListener(new java.awt.event.MouseAdapter() {
      	public void mouseClicked(java.awt.event.MouseEvent e) {
      	  loescheBilder();
      	}
      	public void mouseExited(java.awt.event.MouseEvent e) {
      	  lLoeschen.removeAll();
      	  lLoeschen.setIcon(new ImageIcon(getClass().getResource("uiimgs/loeschenTrash.png")));
      	}
      	public void mouseEntered(java.awt.event.MouseEvent e) {
      	  lLoeschen.removeAll();
      	  lLoeschen.setIcon(new ImageIcon(getClass().getResource("uiimgs/loeschenTrashKlick.png")));
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
      lLetztesBild.setIcon(new ImageIcon(getClass().getResource("uiimgs/pfeillinks.png")));
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
          if (zuletztGewaehltesPanel != null) {
            BildGroszanzeige anzeige = new BildGroszanzeige(listeAnzeigePanel,
                zuletztGewaehltesPanel.gibBildDokument());
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
      miLoeschen.setText("Ausgewählte Bilder löschen");
      miLoeschen.setMnemonic(KeyEvent.VK_L);
      miLoeschen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false));
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
   * @return javax.swing.JSlider	liefert den initialisierten Schieber
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
      	  erzeugeThumbnailansicht();
      	}
      });
    }
    return sGroesze;
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
   * This method initializes pThumbnails
   *
   * @return javax.swing.JPanel
   */
  private ScrollableFlowPanel getPThumbnails() {
    if (pThumbnails == null) {
      pThumbnails = new ScrollableFlowPanel();
      pThumbnails.setLayout(new FlowLayout(FlowLayout.LEADING, STD_ABSTAND, STD_ABSTAND));
      pThumbnails.setBackground(Color.WHITE);
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
      spThumbnails = new JScrollPane();
      spThumbnails.setViewportView(getPThumbnails());
      spThumbnails.getViewport().setBackground(Color.WHITE);
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
              if (tap.istAusgewaehlt()) {
                tap.setzeFokus(false);
              }
      	    }
            pVorschau.resetAnsicht();
      	  }
      	}
      });
    }
    return bAuswahlAufheben;
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
      miAuswahlAufheben.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK | Event.SHIFT_MASK, false));
      miAuswahlAufheben.setMnemonic(KeyEvent.VK_A);
      miAuswahlAufheben.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          if (listeAnzeigePanel != null) {
            for (ThumbnailAnzeigePanel tap : listeAnzeigePanel) {
              if (tap.istAusgewaehlt()) {
                tap.setzeFokus(false);
              }
            }
            pVorschau.resetAnsicht();
          }
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
   * @param args
   */
  public static void main(String[] args) {
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try { 
          UIManager.setLookAndFeel(new com.lipstikLF.LipstikLookAndFeel());
        } catch (Exception e) { 
          e.printStackTrace(); 
        }
        Hauptfenster hauptfenster = new Hauptfenster();
        hauptfenster.setVisible(true);
      }
    });
  }
  
  /**
   * Initialisiert das dieses Objekt.
   */
  private void initialize() {
    this.setSize(new Dimension(Einstellungen.FENSTER_BREITE, Einstellungen.FENSTER_HOEHE));
    this.setLocation(Einstellungen.FENSTER_POSX, Einstellungen.FENSTER_POSY);
    this.setJMenuBar(getHauptmenu());
    setDefaultCloseOperation(
        javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        beende();
      }
    });
    
    this.setContentPane(getJContentPane());
    this.setTitle("JPictureProspector");
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent e) {
        erzeugeThumbnailansicht();
      }
    });
    tapObserver = new ArrayList<Observer>();
    tapObserver.add(pVorschau);
    tapObserver.add((Observer) pBilddetails);
    tapObserver.add(tBilddetails);

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
