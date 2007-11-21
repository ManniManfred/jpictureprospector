package jpp;

import javax.swing.UIManager;

import jpp.client.JPPClient;
import jpp.core.AbstractJPPCore;
import jpp.core.JPPCore;
import jpp.core.exceptions.ErzeugeException;
import jpp.ui.Hauptfenster;
import jpp.ui.StartDialog;


/**
 * Diese Klasse dient nur dazu die richtige Anwendung zu starten und ein paar
 * initialisierungen durchzufuehren.
 * 
 * @author Manfred Rosskamp
 */
class Starter {
  
  /**
   * Startet die Applikation JPictureProspector
   * @param args
   * @throws ErzeugeException 
   */
  public static void main(String[] args) throws ErzeugeException {

    /* Look and Feel setzten */
    try {
      UIManager.setLookAndFeel(new com.lipstikLF.LipstikLookAndFeel());
    } catch (Exception e) {
      e.printStackTrace();
    }

    /* Abfrage Dialog starten */
    StartDialog dialog = new StartDialog(new javax.swing.JFrame(), true);
    dialog.setVisible(true);

    AbstractJPPCore kern = null;
    
    int result = dialog.getReturnStatus();
    if (result == StartDialog.RET_OK) {
      if (dialog.arbeiteLokal()) {
        kern = new JPPCore(dialog.getIndex());
      } else {
        kern = new JPPClient(dialog.getJPPServer());
      }
    }



    /* richtige Applikation starten */
    if (kern != null) {
      Hauptfenster hauptfenster = new Hauptfenster(kern);
      hauptfenster.setVisible(true);
    } else {
      System.exit(0);
      return;
    }
  }
}