package jpp.ui;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.imageio.ImageIO;

/**
 * Ein Objekt der Klasse repraesentiert einen Lader, der Bilddateien aus
 * einem Verzeichnis und seinen Unterverzeichnissen laden kann.
 */
public class VerzeichnisLader {

  /** Das Startverzeichnis aus dem geladen werden soll. */
  private String pfad = null;
  
  /** Die erlaubten Dateitypen. */
  private List<String> dateitypen;
  
  /**
   * Erzeugt ein neues Objekt der Klasse mit den entsprechenden
   * Voreinstellungen.
   * @param pfad  das Startverzeichnis aus dem geladen wird
   */
  public VerzeichnisLader(String pfad) {
    
    this.pfad = pfad;
    dateitypen = Arrays.asList(ImageIO.getReaderFormatNames());
  }
  
  /**
   * Laedt aus einem Verzeichnis, ggfs mit Unterverzeichnissen, alle
   * gueltigen Dateien.
   * @param mitSubDir  <code>true</code> wenn Unterverzeichnisse mit
   *        einbezogen werden sollen
   * @return  eine Menge aller gefundenen Dateien
   */
  public Set<File> ladeVerzeichnis(boolean mitSubDir) {
    
    // Alle gefundenen Dateien
    Set<File> foundFiles = new HashSet<File>();
    
    // Erzeugt das Startverzeichnis
    final File startdir = new File(pfad);  
    
    if (mitSubDir) {
      
      // Enthaelt alle Verzeichnisse in denen gesucht wird
      final Stack<File> dirs = new Stack<File>();
      if (startdir.isDirectory()) {
        
        // Fuegt ein neues Verzeichnis hinzu in dem gesucht werden soll
        dirs.push(startdir); 
      }
   
      while (dirs.size() > 0) {
        
        // Durchlaeuft alle Dateien und nimmt das Verzeichnis vom Stack
        for (File file : dirs.pop().listFiles()) {
          
          if (file.isDirectory()) {
            
            // Fuegt ein neues Verzeichnis dem Stack hinzu
            dirs.push(file); 
          } else {
            
            // Ueberprueft alle Dateien eines Verzeichnisses
            for (String dateityp : dateitypen) {
              
              if (file.getName().endsWith(dateityp)) {
                
                foundFiles.add(file);
              }
            }
          } 
        } 
      } 
    } else {
      /* Es soll nur ein Verzeichnis durchsucht werden. */
      
      /* Enthaelt alle Dateien des Verzeichnisses */
      File[] files = startdir.listFiles();
      for (int i = 0; i < files.length; i++) {
        
        for (String dateityp : dateitypen) {
          
          if (files[i].getName().endsWith(dateityp)) {
            
            foundFiles.add(files[i]);
          }
        }
      }
    }
    return foundFiles;
  }
}
