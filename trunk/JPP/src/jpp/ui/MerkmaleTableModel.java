package jpp.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import jpp.core.AbstractJPPCore;
import jpp.core.BildDokument;
import jpp.core.JPPCore;
import jpp.core.exceptions.ErzeugeException;
import jpp.merkmale.Merkmal;


public class MerkmaleTableModel extends DefaultTableModel {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = -4447059015506099390L;

  /** Enthaelt die Zeilenanzahl des Modells. */
  private int zeilenAnzahl;

  /** Enthaelt die Spaltenanzahl des Modells. */
  private int spaltenAnzahl;

  /** Enthaelt die Daten in der Tabelle. */
  private ArrayList<Object[]> daten = new ArrayList<Object[]>();

  /**
   * Enthaelt die Information, ob eine Zelle editierbar sein soll oder nicht.
   */
  private ArrayList<Boolean> zeilenEditierbar = new ArrayList<Boolean>();

  /**
   * Enthaelt die Informationen, ob in einer Zeile eine Checkbox angezeigt wird
   * oder nicht.
   */
  private ArrayList<Boolean> checkboxAnzeige = new ArrayList<Boolean>();

  /**
   * Enthaelt die Informationen, ob das Merkmal, dass sich in der Zeile befindet
   * den gleichen oder unterschiedliche Werte besitzt.
   */
  private ArrayList<Boolean> merkmalUebereinstimmung = new ArrayList<Boolean>();

  /** Namen, der Merkmale, die in der Tabelle verwaltet werden. */
  private ArrayList<String> merkmalsnamen = new ArrayList<String>();

  /** Enthaelt die Bilddokumente, deren Merkmale angezeigt werden. */
  private List<BildDokument> bilddokumente = null;

  /** Merkmale, die nicht in die Tabelle aufgenommen werden. */
  private static final String[] MERKMAL_FELDER = { "Schl\u00fcsselw\u00f6rter",
      "Beschreibung", "Thumbnail" };

  /** Namen der einzelnen Spalten. */
  private static final String[] SPALTENNAMEN = { "Merkmal", "Wert", "edit" };

  /** Wird angezeigt, wenn Merkmale verschiedene Werte aufweisen. */
  private static final String VERSCH_WERTE = "Verschiedene Werte";


  /**
   * Ein Objekt der Klasse stellt das Tabellenmodell der Merkmalstabelle dar.
   * 
   * @author Marion Mecking
   */
  public MerkmaleTableModel(List<BildDokument> bilddokumente) {

    this.bilddokumente = bilddokumente;

    try {

      // Merkmalsnamen lesen, einzelne Merkmale ausschliessen.
      String[] alleMerkmalsnamen = AbstractJPPCore.getMerkmalsnamen();

      for (int i = 0; i < alleMerkmalsnamen.length; i++) {
        this.merkmalsnamen.add(alleMerkmalsnamen[i]);
      }
      for (int i = 0; i < MERKMAL_FELDER.length; i++) {
        this.merkmalsnamen.remove(MERKMAL_FELDER[i]);
      }

    } catch (ErzeugeException e) {
      System.out.println("Fehler im Tabellenmodell: JPP Core konnte nicht"
          + "erzeugt werden");
    } catch (Exception e) {
      System.out.println("Fehler beim Auslesen der Merkmalsdaten.");
    }

    this.aktualisiereDaten();
  }

  /**
   * Aktualisiert die Liste der Bilddokumente.
   * 
   * @param bilddokumente Dokumente, die in der Tabelle verwaltet werden sollen.
   */
  public void aktualisiereBilddokumente(List<BildDokument> bilddokumente) {

    this.bilddokumente = bilddokumente;
    this.aktualisiereDaten();
  }

  /**
   * Aktualisiert den Inhalt des Tabellenmodells.
   */
  private void aktualisiereDaten() {

    this.daten.clear();
    this.checkboxAnzeige.clear();
    this.zeilenEditierbar.clear();
    this.merkmalUebereinstimmung.clear();

    if (bilddokumente.size() == 0) {
      this.zeilenAnzahl = 3;
      this.spaltenAnzahl = 2;

      /* Leere Datensaetze erzeugen */
      Object[] datensatz = { "", "", false };
      this.daten = new ArrayList<Object[]>();
      for (int i = 0; i < this.zeilenAnzahl; i++) {
        this.daten.add(datensatz);
        this.zeilenEditierbar.add(false);
        this.checkboxAnzeige.add(false);
      }

    } else {

      /* Zeile und Spaltenanzahl bestimmen */
      try {
        this.zeilenAnzahl = AbstractJPPCore.getMerkmalsKlassen().size()
            - MERKMAL_FELDER.length;
        this.spaltenAnzahl = (bilddokumente.size() == 1)
            ? 2
            : 3;
      } catch (Exception e) {
        System.out.println("JPPCore konnte nicht erzeugt werden");
      }

      // Merkmale in Daten schreiben
      for (int j = 0; j < this.merkmalsnamen.size(); j++) {

        Merkmal merkmal = bilddokumente.get(0).getMerkmal(merkmalsnamen.get(j));
        boolean editierbar = merkmal.istEditierbar();
        boolean checkbox_anzeigen = false;
        boolean gleicherWert = this.hatMerkmalGleichenWert(merkmal.getName());
        String merkmalswert = merkmal.getWert().toString();

        /*
         * Werte einfuegen: Merkmal editierbar, gleicher Wert -> Anzeige Wert
         * Merkmal editierbar, ungleicher Wert -> Vorerst nicht editierbar,
         * "verschiedene Werte", Checkbox deaktiviert Merkmal nicht editierbar,
         * gleicher Wert -> Anzeige Wert, keine Checkbox Merkmal nicht
         * editierbar, ungleicher Wert -> Anzeige "versch Werte", keine Checkbox
         */
        if (editierbar) {
          checkbox_anzeigen = true;

          if (gleicherWert) {
            merkmalswert = merkmal.getWert().toString();
          } else {
            editierbar = false;
            merkmalswert = VERSCH_WERTE;
          }
        } else {
          if (!gleicherWert) {
            merkmalswert = VERSCH_WERTE;
            checkbox_anzeigen = false;
          }
        }

        Object[] datensatz = { merkmal.getName(), merkmalswert, editierbar };
        this.daten.add(datensatz);
        this.zeilenEditierbar.add(editierbar);
        this.checkboxAnzeige.add(checkbox_anzeigen);
        this.merkmalUebereinstimmung.add(gleicherWert);

      }
    }

    /* Spaltennamen neu setzen, falls Spalte hinzugekommen ist */
    this.setColumnIdentifiers(SPALTENNAMEN);
    fireTableDataChanged();
  }

  /**
   * Gibt an, ob die angegebene Zelle editierbar ist oder nicht.
   * 
   * @param zeile Zeile der angegebenen Zelle
   * @param spalte Spalte der angegebenen Zelle
   * @return <code>true</code> wenn die angegebene Zelle editierbar ist,
   *         <code>false</code> wenn sie nicht editierbar ist.
   */
  public boolean isCellEditable(int zeile, int spalte) {

    /*
     * Entweder die Zelle liegt in der ersten Spalte (Merkmalsnamen, nie
     * editierbar), in der zweiten Spalte (Editierbarkeit abhaengig von Wert in
     * der Liste editierbar) oder in der dritten Spalte, die nur editierbar ist,
     * wenn sie eine Checkbox enthaelt.
     */
    return (spalte == 2 && this.enthaeltZeileCheckbox(zeile))
        || (this.istZeileEditierbar(zeile) && spalte != 0);
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

  /**
   * Liefert das Element der abgefragten Zelle.
   * 
   * @param zeile Zeile der abgefragten Zelle
   * @param spalte Spalte der abgefragten Zelle
   * @return Inhalt der Zelle mit angegebener Zeile und Spalte.
   */
  public Object getValueAt(int zeile, int spalte) {
    return (this.daten.get(zeile))[spalte];
  }

  /**
   * Liefert die Anzahl der Spalten dieses Modells.
   * 
   * @return Anzahl der Spalten dieses Modells.
   */
  public int getColumnCount() {
    return this.spaltenAnzahl;
  }

  /**
   * Liefert die Anzahl der Zeilen dieses Modells.
   * 
   * @return Anzahl der Zeilen dieses Modells.
   */
  public int getRowCount() {
    return this.zeilenAnzahl;
  }

  /**
   * Liefert die Klassen, die die einzelnen Spalten bilden.
   * 
   * @param spaltenIndex Index der Spalte, dessen Klasse zurueckgegeben wird.
   * @return Klasse der angegebenen Spalte.
   */
  public Class getColumnClass(int spaltenIndex) {

    /** Dritte Spalte enthaelt boolschen Wert */
    if (spaltenIndex == 2) {
      return Boolean.class;
    } else {
      return String.class;
    }
  }

  /**
   * Setzt einen neuen Wert in die angegebene Zelle.
   * 
   * @param value neuer Wert
   * @param zeile Zeile der Zelle, in der der neue Wert gesetzt wird.
   * @param spalte Spalte der Zelle, in der der neue Wert gesetzt wird.
   */
  public void setValueAt(Object value, int zeile, int spalte) {

    /* Zeile geben lass und neuen Wert setzen. */
    Object[] zeileninhalt = this.daten.get(zeile);
    zeileninhalt[spalte] = value;
    this.daten.set(zeile, zeileninhalt);

    /*
     * Wird ein boolscher Wert uebergeben, wurde der Wert einer Checkbox
     * veraendert. Ist der Wert true, wird das Feld in der zweiten Spalte
     * (Merkmalswert) editierbar gemacht, wird der Wert auf false gesetzt, so
     * hatten die Merkmale unterschiedliche Werte und dies wird zur�ck in die
     * Zelle der Merkmalswerte geschrieben
     */
    if (value instanceof Boolean) {
      if ((Boolean) value) {
        zeileninhalt[1] = "";
      } else {
        if (merkmalUebereinstimmung.get(zeile)) {
          zeileninhalt[1] = "";
        } else {
          zeileninhalt[1] = VERSCH_WERTE;
        }
      }

      this.zeilenEditierbar.set(zeile, (Boolean) value);
    }

    /*
     * Bewirkt, dass die gesamte Tabelle neu gerendert wird (und damit z.B.
     * Farbaenderungen sichtbar werden)
     */
    fireTableDataChanged();

  }

  /**
   * Gibt an, ob die MerkmalsWerte in der angegebenen Zeile editierbar sind.
   * 
   * @param zeile Zeile, fuer die abgefragt wird, ob der Wert editierbar ist.
   * @return <code>true</code> wenn Merkmalswert editierbar ist,
   *         <code>false</code> wenn nicht.
   */
  public boolean istZeileEditierbar(int zeile) {
    return this.zeilenEditierbar.get(zeile);
  }

  /**
   * Gibt an, ob die amgegebene Zeile eine Checkbox enthaelt.
   * 
   * @param zeile Zeile, die abgefragt wird.
   * @return <code>true</code> wenn Zeile eine Checkbox enthaelt.
   *         <code>false</code> wenn nicht.
   */
  public boolean enthaeltZeileCheckbox(int zeile) {
    return this.checkboxAnzeige.get(zeile);
  }

  /**
   * Aendert die Daten der Bilddokumente.
   * 
   * @return Liste der geaenderten Bilddokumente.
   */
  public List<BildDokument> aendereDaten() {

    for (int i = 0; i < this.getRowCount(); i++) {

      if (this.istZeileEditierbar(i)) {
        String merkmalsname = this.daten.get(i)[0].toString();
        String merkmalswert = this.daten.get(i)[1].toString();

        /* Bilddokumente aendern. */
        for (int j = 0; j < this.bilddokumente.size(); j++) {
          bilddokumente.get(j).getMerkmal(merkmalsname).setWert(merkmalswert);
        }
      }
    }

    return this.bilddokumente;
  }

}
