package jpp.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class URLUtils {

  /** Logger, der alle Fehler loggt. */
  private static Logger logger = Logger.getLogger("jpp.utils.URLUtils");
  
  
  public static URL getEncodedURL(URL url) {
    URL result = null;
    String neueUrl = url.toString().replaceAll(" ", "%20");
    
    try {
      result = new URL(neueUrl);
    } catch (MalformedURLException e) {
      logger.log(Level.WARNING, "Url ist nicht wohlgeformt. ("
          + neueUrl + ")");
    }
    return result;
  }
  
  
  public static URL getEncodedURL(String url) {
    URL result = null;
    String neueUrl = url.replaceAll(" ", "%20");
    
    try {
      result = new URL(neueUrl);
    } catch (MalformedURLException e) {
      logger.log(Level.WARNING, "Url ist nicht wohlgeformt. ("
          + neueUrl + ")");
    }
    return result;
  }
}
