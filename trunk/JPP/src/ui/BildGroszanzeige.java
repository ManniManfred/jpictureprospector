package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import java.awt.Color;
import javax.swing.JTable;

public class BildGroszanzeige extends JFrame {

  /** Enthaelt den Standardabstand fuer Komponenten. */
  private static final int STD_ABSTAND = 15;
  
  private static final long serialVersionUID = 1L;
  
  private Hauptfenster hauptfenster = null;

  private JPanel jContentPane = null;

  private JPanel pToolbar = null;
  
  private JLabel lLetztesBild = null;
  
  private JLabel lNaechstesBild = null;
  
  private JLabel lLoeschen = null;
  
  private JButton bSchlieszen = null;

  private JTabbedPane tpGroszanzeige = null;

  private Vorschaupanel pGroszanzeige = null;

  private JPanel pBilddetails = null;

  private JTable tBilddetails = null;

  /**
   * This is the default constructor
   */
  public BildGroszanzeige(Hauptfenster hauptfenster) {
    super();
    this.hauptfenster = hauptfenster;
    initialize();
  }
  
  public Vorschaupanel gibVorschaupanel() {
    return this.pGroszanzeige;
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(800, 600);
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
      tpGroszanzeige.addTab("Bilddetails", null, getPBilddetails(), null);
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
   * This method initializes pBilddetails	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPBilddetails() {
    if (pBilddetails == null) {
      pBilddetails = new JPanel();
      pBilddetails.add(getTBilddetails(), null);
    }
    return pBilddetails;
  }

  /**
   * This method initializes tBilddetails	
   * 	
   * @return javax.swing.JTable	
   */
  private JTable getTBilddetails() {
    if (tBilddetails == null) {
      tBilddetails = new JTable();
    }
    return tBilddetails;
  }

}
