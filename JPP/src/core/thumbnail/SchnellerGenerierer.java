package core.thumbnail;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import core.GeoeffnetesBild;

public class SchnellerGenerierer implements ThumbnailGenerierer {

  public BufferedImage generiereThumbnail(GeoeffnetesBild bild, int maxBreite,
      int maxHoehe) {

    Image image = Toolkit.getDefaultToolkit().getImage(
        bild.getDatei().getAbsolutePath());
    
    MediaTracker mediaTracker = new MediaTracker(new Container());
    mediaTracker.addImage(image, 0);
    try {
      mediaTracker.waitForID(0);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // determine thumbnail size from WIDTH and HEIGHT
    int thumbWidth = maxBreite;
    int thumbHeight = maxHoehe;
    double thumbRatio = (double) thumbWidth / (double) thumbHeight;
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    double imageRatio = (double) imageWidth / (double) imageHeight;
    if (thumbRatio < imageRatio) {
      thumbHeight = (int) (thumbWidth / imageRatio);
    } else {
      thumbWidth = (int) (thumbHeight * imageRatio);
    }

    // draw original image to thumbnail image object and
    // scale it to the new size on-the-fly
    BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g = thumbImage.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    
    g.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

    return thumbImage;
  }

}
