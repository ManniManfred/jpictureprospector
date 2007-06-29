package selectionmanager.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class OverAllLayout implements LayoutManager {

  public void addLayoutComponent(String name, Component comp) {
  }

  public void removeLayoutComponent(Component comp) {
  }
  
  
  public void layoutContainer(Container target) {
    int nmembers = target.getComponentCount();

    int width = target.getWidth();
    int height = target.getHeight();
    
    for (int i = 0 ; i < nmembers ; i++) {
        Component m = target.getComponent(i);
        m.setBounds(0, 0, width, height);
    }
  }

  public Dimension minimumLayoutSize(Container target) {
    int nmembers = target.getComponentCount();

    int width = 0;
    int height = 0;
    for (int i = 0 ; i < nmembers ; i++) {
        Component m = target.getComponent(i);
        width = Math.max(width, m.getMinimumSize().width);
        height = Math.max(height, m.getMinimumSize().height);
    }
    return new Dimension(width, height);
  }

  public Dimension preferredLayoutSize(Container target) {
    int nmembers = target.getComponentCount();
    int width = 0;
    int height = 0;
    for (int i = 0 ; i < nmembers ; i++) {
        Component m = target.getComponent(i);
        width = Math.max(width, m.getPreferredSize().width);
        height = Math.max(height, m.getPreferredSize().height);
    }
    return new Dimension(width, height);
  }


}
