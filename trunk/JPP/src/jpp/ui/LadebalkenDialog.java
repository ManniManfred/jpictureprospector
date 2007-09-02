package jpp.ui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.JButton;

import jpp.ui.listener.AbortListener;


/**
 * Ein Objekt der Klasse repraesentiert einen Ladebalken, der geladen wird
 * wenn Bilder importiert werden. Der Ladebalken bietet eine Anzeige
 * ueber die Anzahl der Bilder die geladen wurden im Vergleich zur
 * Gesamtanzahl der zu ladenden Bilder.
 * 
 * @author Nils Verheyen
 *
 */
public class LadebalkenDialog extends JDialog {
  
  /** Enthaelt die Anzahl welchen Abstand eine Komponente zu einer 
   * Anderen haben soll.
   */
  private static final int STD_INSETS = 10;

  private static final long serialVersionUID = 1L;
  
  /** Enthaelt die Gesamtanzahl, wie viele Bilder zu importieren sind. */
  private double gesamtanzahl = 0.0;
  
  /** Enthaelt die Zahl des Bildes welches zur Zeit importiert wird. */
  private int anzahl = 0;

  private JPanel jContentPane = null;

  private JProgressBar pbLadebalken = null;

  private JLabel lAnzahl = null;

  private JLabel lSlash = null;

  private JLabel lGesamtanzahl = null;

  private JPanel pAnzahlen = null;

  private JLabel lUeberschrift = null;

  private JButton jButton = null;

  private List<AbortListener> abortListener = new ArrayList<AbortListener>(1);

  /**
   * Erzeugt ein neues Objekt der Klasse.
   * @param owner  der Besitzer dieses Dialogs
   * @param gesamtanzahl  die Gesamtanzahl der zu ladenden Dateien
   */
  public LadebalkenDialog(Frame owner, int gesamtanzahl) {
    super(owner);
    initialize();
    this.lGesamtanzahl.setText(gesamtanzahl + "");
    this.gesamtanzahl = gesamtanzahl;
  }
  
  /**
   * Setzt die Anzahl der geladenen Bilder neu. Die aktuelle Nummer
   * entspricht immer der Nummer, welches Bild zur Zeit geladen wird.
   * @param anzahl  die Zahl welches Bild geladen wird
   */
  public void setzeAnzahl(int anzahl) {
    
    this.anzahl = anzahl;
    double prozent = (anzahl / gesamtanzahl) * 100;
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(0);
    pbLadebalken.setString(nf.format(prozent) + "%");
    pbLadebalken.setValue((int) prozent);
    this.lAnzahl.setText(anzahl + "");
  }
  
  /**
   * Liefert die Anzahl welches Bild zur Zeit geladen wird.
   * @return  die Anzahl welches Bild zur Zeit geladen wird
   */
  public int gibAnzahl() {
    return this.anzahl;
  }


  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(406, 257);
    this.setResizable(false);
    this.setTitle("Importiere");
    this.setContentPane(getJContentPane());
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        fireAbortEvent();
      }
    });
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane() {
    if (jContentPane == null) {
      GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
      gridBagConstraints1.gridx = 0;
      gridBagConstraints1.gridy = 4;
      GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
      gridBagConstraints5.gridx = 0;
      gridBagConstraints5.insets = new Insets(STD_INSETS, 0, STD_INSETS, 0);
      gridBagConstraints5.gridy = 0;
      lUeberschrift = new JLabel();
      lUeberschrift.setText("lade Bild Nummer:");
      GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
      gridBagConstraints4.gridx = 0;
      gridBagConstraints4.insets = new Insets(STD_INSETS, 0, STD_INSETS, 0);
      gridBagConstraints4.gridy = 2;
      lGesamtanzahl = new JLabel();
      lGesamtanzahl.setText("");
      lGesamtanzahl.setHorizontalTextPosition(SwingConstants.LEFT);
      lGesamtanzahl.setHorizontalAlignment(SwingConstants.LEFT);
      lSlash = new JLabel();
      lSlash.setText("/");
      lAnzahl = new JLabel();
      lAnzahl.setText("");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridwidth = 1;
      gridBagConstraints.insets = new Insets(STD_INSETS, 0, STD_INSETS, 0);
      gridBagConstraints.gridy = 3;
      jContentPane = new JPanel();
      jContentPane.setLayout(new GridBagLayout());
      jContentPane.add(getPbLadebalken(), gridBagConstraints);
      jContentPane.add(getPAnzahlen(), gridBagConstraints4);
      jContentPane.add(lUeberschrift, gridBagConstraints5);
      jContentPane.add(getJButton(), gridBagConstraints1);
    }
    return jContentPane;
  }

  /**
   * This method initializes pbLadebalken	
   * 	
   * @return javax.swing.JProgressBar	
   */
  private JProgressBar getPbLadebalken() {
    if (pbLadebalken == null) {
      pbLadebalken = new JProgressBar();
      pbLadebalken.setStringPainted(true);
    }
    return pbLadebalken;
  }

  /**
   * This method initializes pAnzahlen	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getPAnzahlen() {
    if (pAnzahlen == null) {
      pAnzahlen = new JPanel();
      pAnzahlen.setLayout(new FlowLayout());
      pAnzahlen.add(lAnzahl, null);
      pAnzahlen.add(lSlash, null);
      pAnzahlen.add(lGesamtanzahl, null);
    }
    return pAnzahlen;
  }

  /**
   * This method initializes jButton	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getJButton() {
    if (jButton == null) {
      jButton = new JButton();
      jButton.setText("Abbrechen");
      jButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          fireAbortEvent();
        }
      });
    }
    return jButton;
  }

  
  private void fireAbortEvent() {
    for (AbortListener l : abortListener) {
      l.abort();
    }
  }
  
  public void addAbortListener(AbortListener l) {
    this.abortListener.add(l);
  }
  
  public void removeAbortListener(AbortListener l) {
    this.abortListener .remove(l);
  }
  
}  //  @jve:decl-index=0:visual-constraint="10,10"
