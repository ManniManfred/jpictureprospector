package jpp;

import javax.swing.UIManager;

import jpp.core.AbstractJPPCore;
import jpp.core.exceptions.ErzeugeException;
import jpp.settings.SettingsManager;
import jpp.settings.StarterSettings;
import jpp.ui.Hauptfenster;
import jpp.ui.StartDialog;


/**
 * Diese Klasse dient nur dazu die richtige Anwendung zu starten und ein paar
 * initialisierungen durchzufuehren.
 * 
 * @author Manfred Rosskamp
 */
public class Starter {
  
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

    StarterSettings s = SettingsManager.getSettings(StarterSettings.class);
    
    /* Abfrage Dialog starten */
    StartDialog dialog = new StartDialog(new javax.swing.JFrame(), true);
    dialog.setStarterSettings(s);
    
    
    dialog.setVisible(true);
    
    AbstractJPPCore kern = null;
    
    int result = dialog.getReturnStatus();
    if (result == StartDialog.RET_OK) {
      kern = dialog.getJPPCore();
      
      /* Wenn gewollt, dann die StarterSettings speichern */
      StarterSettings neueSettings = dialog.getStarterSettings();
      if (neueSettings.saveStarterSettings) {
        SettingsManager.saveSettings(neueSettings);
      } else {
        /* Wenn die Einstellungen nicht gespeichert werden sollen, dies in 
         * den alten Einstellungen setzten.
         */
        if (s.saveStarterSettings) {
          s.saveStarterSettings = false;
          SettingsManager.saveSettings(s);
        }
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