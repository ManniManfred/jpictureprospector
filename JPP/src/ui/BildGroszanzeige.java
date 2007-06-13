package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumn;

import merkmale.BildbreiteMerkmal;
import merkmale.BildhoeheMerkmal;
import merkmale.DateipfadMerkmal;
import ui.listener.BildGeladenListener;
import core.BildDokument;

public class BildGroszanzeige extends JFrame {
  
  /** Enthaelt den Wert fuer die Spalte der Exif-Bezeichnungen. */
  private static final int KEYSPALTE = 0;
  
  /** Enthaelt den Wert fuer die Spalte der Exif-Werte. */
  private static final int WERTSPALTE = 1;
  
  /** Enthaelt den Standardabstand fuer Komponenten. */
  private static final int STD_ABSTAND = 15;
  
  private static final long serialVersionUID = 1L;
  
  /** Enthaelt das BildDokument zu dem die Informationen angezeigt werden
   * sollen. */
  private BildDokument dok = null;
  
  /** Enthaehlt das Tabellenmodell fuer die Tabelle in denen Zusatzdetails
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

  /**
   * This is the default constructor
   */
  public BildGroszanzeige(List<ThumbnailAnzeigePanel> taps, BildDokument dok) {
    super();
    listeAnzeigePanel = taps;
    this.dok = dok;
    initialize();
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
    this.setContentPane(getCpInhalt());
    this.setFocusable(true);
    this.setTitle("Groszanzeige - JPictureProspector");
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
      lNaechstesBild = new JLabel();
      lNaechstesBild.setIcon(new ImageIcon(getClass().getResource("uiimgs/pfeilrechts.png")));
      lNaechstesBild.setText("");
      lNaechstesBild.addMouseListener(new java.awt.event.MouseAdapter() {   
      	public void mouseClicked(java.awt.event.MouseEvent e) {   
          
          /* Waehlt das naechste Bild in der Liste aus
           * oder das erste wenn das letzte Bild der Liste angezeigt wird
           */
          boolean neuesBildGewaehlt = false;
          for (int i = 0; i < listeAnzeigePanel.size() && !neuesBildGewaehlt; i++) {
            if (dok.equals(listeAnzeigePanel.get(i).gibBildDokument())
                && i < listeAnzeigePanel.size() - 1) {
              dok = listeAnzeigePanel.get(i + 1).gibBildDokument();
              pGroszanzeige.setzeDok(dok);
              detailsTableModel.setzeDokument(dok);
              neuesBildGewaehlt = true;
            } else if (i == listeAnzeigePanel.size() - 1){
              dok = listeAnzeigePanel.get(0).gibBildDokument();
              pGroszanzeige.setzeDok(dok);
              detailsTableModel.setzeDokument(dok);
              neuesBildGewaehlt = true;
            }
          }
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
          
          /* Waehlt das vorige Bild in der Liste aus und das letzte Bild
           * wenn das erste Bild der Liste ausgewaehlt ist
           */
          boolean neuesBildGewaehlt = false;
          for (int i = 0; i < listeAnzeigePanel.size() && !neuesBildGewaehlt; i++) {
            if (dok.equals(listeAnzeigePanel.get(i).gibBildDokument()) 
                && i > 0) {
              dok = listeAnzeigePanel.get(i - 1).gibBildDokument();
              pGroszanzeige.setzeDok(dok);
              detailsTableModel.setzeDokument(dok);
              neuesBildGewaehlt = true;
            } else if (dok.equals(listeAnzeigePanel.get(i).gibBildDokument()) 
                && i == 0){
              dok = listeAnzeigePanel.get(listeAnzeigePanel.size() - 1).gibBildDokument();
              pGroszanzeige.setzeDok(dok);
              detailsTableModel.setzeDokument(dok);
              neuesBildGewaehlt = true;
            }
          }
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
  private BildGroszanzeigeZeichner getPGroszanzeige() {
    if (pGroszanzeige == null) {
      pGroszanzeige = new BildGroszanzeigeZeichner(dok);
      pGroszanzeige.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      pGroszanzeige.addBildGeladenListener(new BildGeladenListener() {
        public void bildWurdeGeladen() {
          updateAnsicht();
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

/**
 * Ein Objekt der Klasse zeichnet aus einer Datei ein Bild in den eigenen
 * Anzeigebereich.
 */
class BildGroszanzeigeZeichner extends JPanel {
  
  /** Enthaelt das Dokument, dass den Pfad zur Datei enthaelt. */
  private BildDokument dok = null;
  
  /** Enthaelt eine Liste an Listenern die darauf reagieren, wenn ein
   * Bild geladen wurde. */
  private List<BildGeladenListener> listener = null;
  
  /**
   * Enthaelt das <code>ImageIcon</code> zu diesem Bild was den 
   * Ladevorgang uebernimmt.
   */
  private ImageIcon icon = null;
  
  /**
   * Erzeugt ein neues Objekt der Klasse mit den entsprechenden Daten.
   * @param dok  das Bilddokument, dass den Pfad zur Datei enthaelt
   */
  public BildGroszanzeigeZeichner(BildDokument dok) {
    this.dok = dok;
    this.listener = new ArrayList<BildGeladenListener>();
    icon = new ImageIcon((String)
        dok.getMerkmal(DateipfadMerkmal.FELDNAME).getWert());
    repaint();
  }
  
  /**
   * Fuegt einen <code>BildGeladenListener</code> zur Liste der Listener
   * hinzu.
   * @param l  der hinzuzufuegende Listener
   */
  public void addBildGeladenListener(BildGeladenListener l) {
    listener.add(l);
  }
  
  /**
   * Loescht einen <code>BildGeladenListener</code> aus der Liste der
   * Listener.
   * @param l  der zu loeschende Listener
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
   * @param dok  das Dokument mit den benoetigten Informationen
   */
  public void setzeDok(BildDokument dok) {
    this.dok = dok;
    icon = new ImageIcon((String)
        dok.getMerkmal(DateipfadMerkmal.FELDNAME).getWert());
    this.repaint();
  }
  
  /**
   * Zeichnet das Bild in die Oberflaeche, mit den entsprechenden Abmessungen. 
   */
  protected void paintComponent(Graphics g) {
    
    Image bild = icon.getImage();
    fireBildGeladen();
    double originalBreite = 
      (Integer) dok.getMerkmal(BildbreiteMerkmal.FELDNAME).getWert();
    double originalHoehe = 
      (Integer) dok.getMerkmal(BildhoeheMerkmal.FELDNAME).getWert();
    double hoeheBild = bild.getHeight(this);
    double breiteBild = bild.getWidth(this);
    double dieseBreite = getWidth();
    double dieseHoehe = getHeight();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
  
    if (originalBreite <= dieseBreite && originalHoehe <= dieseHoehe) {
      
      g.drawImage(bild, 
          (int) (dieseBreite - originalBreite) / 2,
          (int) (dieseHoehe - originalHoehe) / 2,
          (int) originalBreite, (int) originalHoehe, this);
    } else if (Math.abs(dieseBreite / breiteBild) < Math.abs(dieseHoehe / hoeheBild)) {
      // Anpassung der Größe an dieses Objekt
      
      // Breite voll ausgefuellt, Hoehe muss neu berechnet werden
      g.drawImage(bild,
          0,
          (int) (dieseHoehe - hoeheBild * (dieseBreite / breiteBild)) / 2,
          (int) dieseBreite,
          (int) (hoeheBild * (dieseBreite / breiteBild)), this);
    } else {
      
      // Hoehe voll ausgefuellt, Breite muss neu berechnet werden
      g.drawImage(bild, 
          (int) (dieseBreite - breiteBild * (dieseHoehe / hoeheBild)) / 2,
          0,
          (int) (breiteBild * (dieseHoehe / hoeheBild)),
          (int) dieseHoehe, this);
    }
    
  }
}
