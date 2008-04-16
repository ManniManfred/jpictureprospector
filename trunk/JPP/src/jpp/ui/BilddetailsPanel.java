package jpp.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import jpp.core.AbstractJPPCore;
import jpp.core.BildDokument;
import jpp.core.exceptions.AendereException;
import jpp.ui.listener.AenderungsListener;
import selectionmanager.Auswaehlbar;
import selectionmanager.AuswahlListener;


/**
 * Ein Objekt der Klasse stellt die Oberflaeche zu den Bilddetails fuer
 * ausgewaehlte Bilddokumenten dar.
 * 
 * @author Marion Mecking
 */
public class BilddetailsPanel extends JPanel implements AuswahlListener {

  /** Bilddokumente, zu denen die Merkmale angezeigt werden. */
  private List<BildDokument> bilddokumente = new ArrayList<BildDokument>();

  /** Textkomponenten der Merkmale. */
  private HashMap<String, JTextComponent> textkomponenten = new HashMap<String, JTextComponent>(); // @jve:decl-index=0:

  /** Checkboxen der Merkmale. */
  private HashMap<String, JCheckBox> checkboxen = new HashMap<String, JCheckBox>();

  /** Namen der Merkmale, die im Panel angezeigt werden. */
  private static final String SCHLUESSELWOERTER = "Schl\u00fcsselw\u00f6rter";

  private static final String BESCHREIBUNG = "Beschreibung";

  private static final String[] merkmale = { SCHLUESSELWOERTER, BESCHREIBUNG };

  /** String fuer verschiedene Werte. */
  private static final String VERSCH_WERTE = "Verschiedene Werte";

  private static final long serialVersionUID = 1L;

  /** Label und Buttons. */
  private JLabel lSchluesselworter = null;

  private JLabel lBildbeschreibung = null;

  private JButton bAendern = null;

  /** Kern, der die Aenderungen uebernimmt. */
  private AbstractJPPCore kern;

  /** Tabelle, die die uebrigen Merkmale anzeigt. */
  private MerkmaleJTable tabelle;
  
  private List<AenderungsListener> aenderungsListener;

  /**
   * Erzeugt ein neues Panel fuer die Bilddetails. Es wird davon ausgegangen,
   * dass das Panel nur editierbare Merkmale enthaelt.
   * 
   * @param kern Kern der Anwendung.
   */
  public BilddetailsPanel(MerkmaleJTable tabelle, AbstractJPPCore kern) {
    super();
    initialize();
    this.tabelle = tabelle;
    this.kern = kern;
    this.aenderungsListener = new ArrayList<AenderungsListener>();
  }

  /**
   * Initialisiert die Oberflaeche
   */
  private void initialize() {

    JTextField schluesselwoerterFeld = new JTextField();
    schluesselwoerterFeld.setPreferredSize(new Dimension(200, 20));
    JTextArea beschreibungsbereich = new JTextArea(5, 30);
    beschreibungsbereich.setLineWrap(true);
    JScrollPane spBeschreibung = new JScrollPane(beschreibungsbereich);
    this.textkomponenten.put(SCHLUESSELWOERTER, schluesselwoerterFeld);
    this.textkomponenten.put(BESCHREIBUNG, beschreibungsbereich);
    this.checkboxen.put(SCHLUESSELWOERTER, new JCheckBox());
    this.checkboxen.put(BESCHREIBUNG, new JCheckBox());

    GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
    gridBagConstraints11.gridx = 2;
    gridBagConstraints11.anchor = GridBagConstraints.WEST;
    gridBagConstraints11.insets = new Insets(0, 10, 10, 0);
    gridBagConstraints11.gridy = 5;
    GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
    gridBagConstraints5.gridx = 0;
    gridBagConstraints5.gridy = 4;

    GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
    gridBagConstraints4.fill = GridBagConstraints.BOTH;
    gridBagConstraints4.gridy = 4;
    gridBagConstraints4.weightx = 1.0;
    gridBagConstraints4.weighty = 1.0;
    gridBagConstraints4.anchor = GridBagConstraints.WEST;
    gridBagConstraints4.insets = new Insets(0, 10, 10, 0);
    gridBagConstraints4.gridx = 2;

    GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
    gridBagConstraints3.gridx = 2;
    gridBagConstraints3.anchor = GridBagConstraints.WEST;
    gridBagConstraints3.insets = new Insets(0, 10, 10, 0);
    gridBagConstraints3.gridy = 3;
    lBildbeschreibung = new JLabel();
    lBildbeschreibung.setText(BESCHREIBUNG);

    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
    gridBagConstraints2.fill = GridBagConstraints.BOTH;
    gridBagConstraints2.gridy = 2;
    gridBagConstraints2.weightx = 1.0;
    gridBagConstraints2.anchor = GridBagConstraints.WEST;
    gridBagConstraints2.insets = new Insets(0, 10, 10, 0);
    gridBagConstraints2.gridx = 2;

    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
    gridBagConstraints1.gridx = 2;
    gridBagConstraints1.anchor = GridBagConstraints.WEST;
    gridBagConstraints1.insets = new Insets(0, 10, 10, 0);
    gridBagConstraints1.gridy = 1;
    lSchluesselworter = new JLabel();
    lSchluesselworter.setText(SCHLUESSELWOERTER);
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    this.setSize(300, 250);
    this.setLayout(new GridBagLayout());
    this.add(lSchluesselworter, gridBagConstraints1);
    this.add(lBildbeschreibung, gridBagConstraints3);
    this.add(getBAendern(), gridBagConstraints11);

    /* Setzen der Elemente fuer Schluesselwoerter. */
    this.add(this.checkboxen.get(SCHLUESSELWOERTER), gridBagConstraints);
    this.add(this.textkomponenten.get(SCHLUESSELWOERTER), gridBagConstraints2);

    /* Setzen der Elemente fuer Bildbeschreibung. */
    this.add(spBeschreibung, gridBagConstraints4);
    this.add(this.checkboxen.get(BESCHREIBUNG), gridBagConstraints5);

    /* ActionListener zu den Checkboxen hinzufuegen. */
    for (int i = 0; i < merkmale.length; i++) {
      this.checkboxen.get(merkmale[i]).addActionListener(
          new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
              checkBoxGewaehlt(evt);
            }
          });
      this.checkboxen.get(merkmale[i]).setVisible(false);
    }
  }

  /**
   * Initialisiert den Button zum Aendern der Bilddetails.
   * 
   * @return Button zum Aendern der Bilddetails.
   */
  private JButton getBAendern() {
    if (bAendern == null) {
      bAendern = new JButton();
      bAendern.setText("\u00C4nderungen \u00FCbernehmen");
      /* ActionListener fuer Button. */
      this.bAendern.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          datenaendern(evt);
        }
      });
    }
    return bAendern;
  }


  /**
   * Aktualisiert die Daten des Panels.
   */
  public void aktualisiereDaten() {
    String merkmalText = "";

    /* Checkboxen unsichtbar setzen. */
    for (int i = 0; i < merkmale.length; i++) {
      this.checkboxen.get(merkmale[i]).setVisible(false);
    }

    if (bilddokumente.size() != 0) {
      for (int i = 0; i < merkmale.length; i++) {
        if (hatMerkmalGleichenWert(merkmale[i])) {

          /*
           * Bei gleichem Merkmalswert wird Wert des ersten Bilddokumentes
           * angezeigt und die Textkomponente editierbar gemacht.
           */
          merkmalText = (String) bilddokumente.get(0).getMerkmal(merkmale[i])
              .getWert();
          this.textkomponenten.get(merkmale[i]).setEditable(true);
        } else {

          /*
           * Merkmal hat unterschiedliche Werte, Checkbox false, Textkomponente
           * nicht editierbar
           */
          merkmalText = VERSCH_WERTE;
          this.textkomponenten.get(merkmale[i]).setEditable(false);
          this.checkboxen.get(merkmale[i]).setVisible(true);
          this.checkboxen.get(merkmale[i]).setSelected(false);
        }

        this.textkomponenten.get(merkmale[i]).setText(merkmalText);
      }
    } else {
      // Felder leeren
      for (int i = 0; i < merkmale.length; i++) {
        this.textkomponenten.get(merkmale[i]).setText("");
      }
    }
  }

  /**
   * Aktualisiert die Werte und Zustaende der Textkomponenten, wenn eine
   * Checkbox aktiviert wurde
   * 
   * @param evt Ereignis, dass diese Methode ausloeste
   */
  private void checkBoxGewaehlt(java.awt.event.ActionEvent evt) {

    for (int i = 0; i < merkmale.length; i++) {
      if (this.checkboxen.get(merkmale[i]).isVisible()) {
        JCheckBox aktCheckBox = checkboxen.get(merkmale[i]);
        JTextComponent aktTextKomponente = this.textkomponenten
            .get(merkmale[i]);
        boolean neuerWert = aktCheckBox.isSelected();

        if (neuerWert) {
          // Checkbox war zuvor false
          aktTextKomponente.setText("");
          aktTextKomponente.setEditable(true);
        } else {
          aktTextKomponente.setText(VERSCH_WERTE);
          aktTextKomponente.setEditable(false);
        }
      }
    }
  }

  /**
   * Aendert die Daten der Bilddokumente.
   * 
   * @param evt Ereignis durch Klick auf Button
   */
  private void datenaendern(java.awt.event.ActionEvent evt) {

    /* Geaenderte Werte aus der Tabelle uebernehmen. */
    List<BildDokument> bilddok = this.tabelle.aendereDaten();

    try {
      for (int j = 0; j < bilddokumente.size(); j++) {
        for (int i = 0; i < merkmale.length; i++) {
          JCheckBox aktCheckBox = checkboxen.get(merkmale[i]);
          JTextComponent aktTextKomponente = this.textkomponenten
              .get(merkmale[i]);
  
          /*
           * Daten werden nur geaendert wenn die Checkbox nicht deaktiviert ist
           */
          if (!(aktCheckBox.isVisible() && !aktCheckBox.isSelected())) {
            bilddok.get(j).getMerkmal(merkmale[i]).setWert(
                aktTextKomponente.getText());
          }
        }
        
        this.kern.aendere(bilddok.get(j));
        fireAenderungDurchgefuehrt();
      }
    } catch (AendereException e) {
      System.out.println("Fehler beim Aendern der Merkmale");
    }
  }

  /**
   * Prueft, ob die BildDokumente fuer den angegebenen Merkmalsnamen den
   * gleichen Wert besitzen.
   * 
   * @param String Name des Merkmals, das abgefragt wird.
   * @return <code>true</code> wenn alle BildDokumente denselben Wert
   *         enthalten <code>false</code> wenn die Werte unterschiedlich sind
   */
  private boolean hatMerkmalGleichenWert(String merkmalsname) {

    boolean istGleich = true;
    int i = 0;
    while ((i < (bilddokumente.size() - 1)) && istGleich) {
      istGleich = bilddokumente.get(i).getMerkmal(merkmalsname).getWert()
          .equals(bilddokumente.get(i + 1).getMerkmal(merkmalsname).getWert());
      i++;
    }

    return istGleich;
  }

  public void auswahlGeaendert(Set<Auswaehlbar> ausgewaehlten) {

    /* Passe die Tabelle der Auswahl an */
    if (ausgewaehlten != null) {

      /* erstelle eine neue Liste mit den ausgewaehlten BildDokumenten */
      bilddokumente = new ArrayList<BildDokument>();

      for (Auswaehlbar a : ausgewaehlten) {
        BildDokument dok = ((ThumbnailAnzeigePanel) a).gibBildDokument();
        bilddokumente.add(dok);
      }

      this.aktualisiereDaten();
    }

  }
  
  private void fireAenderungDurchgefuehrt() {
    for (AenderungsListener l : aenderungsListener) {
      l.valueChanged();
    }
  }
  
  public void addAenderungsListener(AenderungsListener listener) {
    this.aenderungsListener.add(listener);
  }
  
  public void removeAenderungsListener(AenderungsListener listener) {
    if (this.aenderungsListener.contains(listener)) {
      this.aenderungsListener.remove(listener);
    }
  }

  public void markierungWurdeBewegt(Auswaehlbar neueMarkierung) {
    // TODO Auto-generated method stub

  }
}
