package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumn;

import merkmale.AlleMerkmale;
import merkmale.DateipfadMerkmal;

import core.BildDokument;
import javax.swing.JScrollPane;

import ui.listener.BildGeladenListener;

public class BildGroszanzeige extends JFrame {
  
  /** Enthaelt den Wert fuer die Spalte der Exif-Bezeichnungen. */
  private static final int KEYSPALTE = 0;
  
  /** Enthaelt den Wert fuer die Spalte der Exif-Werte. */
  private static final int WERTSPALTE = 1;
  
  /** Enthaelt den Standardabstand fuer Komponenten. */
  private static final int STD_ABSTAND = 15;
  
  private static final long serialVersionUID = 1L;
  
  /** Enthaelt das zugehoerige Hauptfenster. */
  private Hauptfenster hauptfenster = null;
  
  /** Enthaelt das BildDokument zu dem die Informationen angezeigt werden
   * sollen. */
  private BildDokument dok = null;
  
  /** Enthaehlt das Tabellenmodell fuer die Tabelle in denen Zusatzdetails
   * angezeigt werden.
   */
  private BGATabellenModell detailsTableModel= null;

  private JPanel jContentPane = null;

  private JPanel pToolbar = null;
  
  private JLabel lLetztesBild = null;
  
  private JLabel lNaechstesBild = null;
  
  private JLabel lLoeschen = null;
  
  private JButton bSchlieszen = null;

  private JTabbedPane tpGroszanzeige = null;

  private Vorschaupanel pGroszanzeige = null;

  private JScrollPane spZusatzdetails = null;

  private JTable tZusatzdetails = null;

  /**
   * This is the default constructor
   */
  public BildGroszanzeige(Hauptfenster hauptfenster, BildDokument dok) {
    super();
    this.hauptfenster = hauptfenster;
    this.dok = dok;
    initialize();
  }
  
  /**
   * Liefert das Panel in dem die Groszansicht zum Tragen kommt.
   * 
   * @return  das entsprechende Panel was das Bild grosz anzeigt
   */
  public Vorschaupanel gibVorschaupanel() {
    return this.pGroszanzeige;
  }
  
  /**
   * Liefert das Tabellenmodell der Tabelle.
   * 
   * @return  das entsprechenden Tabellenmodell.
   */
  public BGATabellenModell gibTabellenModell() {
    return this.detailsTableModel;
  }

  /**
   * Die Groesze der Spaltenbreite wird automatisch angepasst an die
   * Laenge der darin enthaltenen Strings.
   */
  private void updateAnsicht() {

    TableColumn keySpalte = tZusatzdetails.getColumnModel().getColumn(KEYSPALTE);
    TableColumn wertSpalte = tZusatzdetails.getColumnModel().getColumn(WERTSPALTE);
    Font font = tZusatzdetails.getFont();
    FontMetrics metrics = getFontMetrics(font);
    int maxBreiteKey = 0;
    int maxBreiteWert = 0;
    for (int i = 0; i < tZusatzdetails.getRowCount(); i++) {
      
      if (maxBreiteKey < metrics.stringWidth((String)
                           tZusatzdetails.getValueAt(i, KEYSPALTE))) {
        maxBreiteKey = metrics.stringWidth((String) 
                         tZusatzdetails.getValueAt(i, KEYSPALTE));
      }
      if (maxBreiteWert < metrics.stringWidth((String) 
                            tZusatzdetails.getValueAt(i, WERTSPALTE))) {
        maxBreiteWert = metrics.stringWidth((String) 
                          tZusatzdetails.getValueAt(i, WERTSPALTE));
      }
    }
    keySpalte.setPreferredWidth(maxBreiteKey + 20);
    wertSpalte.setPreferredWidth(maxBreiteWert + 20);
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(800, 600);
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.setContentPane(getJContentPane());
    this.setTitle("Groszanzeige - JPictureProspector");
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane() {
    if (jContentPane == null) {
      jContentPane = new JPanel();
      jContentPane.setLayout(new BorderLayout());
      jContentPane.add(getPToolbar(), BorderLayout.SOUTH);
      jContentPane.add(getTpGroszanzeige(), BorderLayout.CENTER);
    }
    return jContentPane;
  }

  /**
   * This method initializes pToolbar	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPToolbar() {
    if (pToolbar == null) {
      lLoeschen = new JLabel();
      lLoeschen.setIcon(new ImageIcon(getClass().getResource("uiimgs/loeschen.png")));
      lLoeschen.setText("");
      lLoeschen.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseClicked(java.awt.event.MouseEvent e) {    
      		hauptfenster.loescheBilder();
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
      lNaechstesBild.setIcon(new ImageIcon(getClass().getResource("uiimgs/pfeilrechts.png")));
      lNaechstesBild.setText("");
      lNaechstesBild.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseClicked(java.awt.event.MouseEvent e) {    
      		hauptfenster.waehleNaechstesBildAus();
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
      lLetztesBild.setIcon(new ImageIcon(getClass().getResource("/ui/uiimgs/pfeillinks.png")));
      lLetztesBild.setText("");
      lLetztesBild.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseClicked(java.awt.event.MouseEvent e) {    
      		hauptfenster.waehleLetztesBildAus();
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
      
      pToolbar = new JPanel();
      pToolbar.setLayout(new FlowLayout(FlowLayout.CENTER, STD_ABSTAND, STD_ABSTAND));
      pToolbar.add(lLetztesBild, null);
      pToolbar.add(lNaechstesBild, null);
      pToolbar.add(lLoeschen, null);
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
      bSchlieszen.setText("Schließen");
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
      tpGroszanzeige.addTab("Groszanzeige", null, getPGroszanzeige(), null);
      tpGroszanzeige.addTab("Zusatzdetails", null, getSpZusatzdetails(), null);
    }
    return tpGroszanzeige;
  }

  /**
   * This method initializes pGroszanzeige	
   * 	
   * @return javax.swing.JPanel	
   */
  private Vorschaupanel getPGroszanzeige() {
    if (pGroszanzeige == null) {
      pGroszanzeige = new Vorschaupanel();
      pGroszanzeige.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
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
      tZusatzdetails.setIntercellSpacing(new Dimension(10, 10));
      detailsTableModel = new BGATabellenModell(this.dok);
      tZusatzdetails.setModel(detailsTableModel);
      detailsTableModel.addBildGeladenListener(new BildGeladenListener() {
        public void bildWurdeGeladen() {
          updateAnsicht();
        }
      });
    }
    return tZusatzdetails;
  }
}
