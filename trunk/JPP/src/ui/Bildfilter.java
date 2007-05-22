package ui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

public class Bildfilter extends FileFilter {
  
  private static ArrayList<String> dateitypen = null;
  
  public Bildfilter() {
    
    dateitypen = new ArrayList<String>();
    dateitypen.add("jpg");
    dateitypen.add("jpeg");
    dateitypen.add("gif");
    dateitypen.add("bmp");
    dateitypen.add("tif");
    dateitypen.add("tiff");
    dateitypen.add("png");
  }

  @Override
  public boolean accept(File f) {
    
    if (f.isDirectory()) {
      return true;
    } else {
      String extension = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');
      if (i > 0 &&  i < s.length() - 1) {
        extension = s.substring(i+1).toLowerCase();
      }
      if (extension != null) {
        if (dateitypen.contains(extension)) {
        return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }
  }

  @Override
  public String getDescription() {
    
    return "Bilddateien";
  }

}
