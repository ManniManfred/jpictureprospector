package jpp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import selectionmanager.ui.AuswaehlbaresJPanel;

import jpp.core.BildDokument;
import jpp.merkmale.DateinameMerkmal;
import jpp.merkmale.DateipfadMerkmal;
import jpp.merkmale.ThumbnailMerkmal;


/**
 * Ein Objekt der Klasse stellt eine Anzeige in einem Fenster dar in dem ein
 * Thumbnail und der dazugehoerige Dateiname angezeigt wird. Die Groesze dieses
 * Objektes ist variabel.
 */
public class ThumbnailAnzeigePanel extends AuswaehlbaresJPanel {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = -8705697105272815947L;


  /** Enthaelt das Label fuer den Dateinamen des Bildes. */
  private JLabel lDateiname;


  /** Enthaelt das Panel fuer die Anzeige des Thumbnails. */
  private ThumbnailAnzeige thumbnailAnzeige;


  /** Enthaelt das anzuzeigende BildDokument. */
  private BildDokument dok;

  /** Enthaelt die Groesze dieses ThumbnailAnzeigePanels. */
  private int groesze;
  
  /**
   * This method initializes
   */
  public ThumbnailAnzeigePanel(BildDokument dok, int groesze) {
    this.dok = dok;
    this.groesze = groesze;
    
    init();
  }


  /**
   * This method initializes this
   */
  private void init() {

    /* Allgemeine Einstellungen dieses ThumbnailAnzeigePanels */
    this.setLayout(new BorderLayout());
    this.setSize(new Dimension(groesze, groesze + 20));


    /* ThumbnailMerkmal in diesem AnzeigePanel darstellen */
    ThumbnailMerkmal m = (ThumbnailMerkmal) dok
        .getMerkmal(ThumbnailMerkmal.FELDNAME);
    this.thumbnailAnzeige = new ThumbnailAnzeige(m.getThumbnail(), dok);
    this.thumbnailAnzeige.setzeGroesze(groesze);
    this.add(this.thumbnailAnzeige, BorderLayout.CENTER);


    /* Den dateinamen auslesen und anzeigen */
    lDateiname = new JLabel(getDateiName());
    lDateiname.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(lDateiname, BorderLayout.SOUTH);

  }

  /**
   * Gibt die PreferredSize zurueck.
   */
  public Dimension getPreferredSize() {
    return new Dimension(groesze, groesze + 20);
  }
  
  /**
   * Gibt den Dateinamen des BildDokument, welches in diesem TAP angezeigt wird
   * zurueck.
   * 
   * @return Dateinamen des BildDokument, welches in diesem TAP angezeigt wird
   *         zurueck.
   */
  private String getDateiName() {
    
    /* Dateinamen aus dem BildDokument holen */
    String dateiname = dok.getMerkmal(DateinameMerkmal.FELDNAME).getWert()
        .toString();
    
    /* Die Dateiendung entfernen */
    int trenner = dateiname.lastIndexOf(".");
    if (trenner > 0) {
      dateiname = dateiname.substring(0, trenner);
    }
    
    return dateiname;
  }

  /**
   * Setzt die Groesze dieses Feldes.
   * 
   * @param groesze die Groesze die das Feld haben soll
   */
  public void setzeGroesze(int groesze) {
    this.groesze = groesze;
    this.setSize(groesze, groesze + 20);
    this.thumbnailAnzeige.setzeGroesze(groesze);
    
    this.revalidate();
  }

  
  /**
   * Liefert das <code>BildDokument</code> dieses Objekts.
   * 
   * @return das das <code>BildDokument</code> dieses Objekts
   */
  public BildDokument gibBildDokument() {
    return this.dok;
  }


}
