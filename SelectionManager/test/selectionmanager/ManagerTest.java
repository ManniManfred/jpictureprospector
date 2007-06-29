package selectionmanager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import selectionmanager.selectionmodel.MultipleSelectionModel;
import selectionmanager.selectionmodel.SelectionModel;
import selectionmanager.ui.OverAllLayout;
import selectionmanager.ui.SelectableJPanel;

public class ManagerTest extends JFrame {

  private static final long serialVersionUID = 1L;

  private JPanel jContentPane = null;

  private JLayeredPane centerPane = null;
  
  private JPanel jPanelMain = null;

  private SelectionManager manager;

  private JPanel jPanel = null;

  private JButton jButton = null;

  private JButton jButton1 = null;
  
  
  
  /**
   * This is the default constructor
   */
  public ManagerTest() {
    super();
    
    manager = new SelectionManager();
    
    manager.addAuswahlListener(new AuswahlListener() {
      public void auswahlGeaendert() {
        System.out.println("Ausgewaehlte = " + manager.gibAlleAusgewaehlten().toString());
      }

      public void markierungWurdeBewegt() {
        System.out.println("Angeklickt = " + manager.getMarkiert());
      }
      
    });
    
    
    manager.setContainerMitAuswaehlbaren(getJPanelMain());
    initialize();
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(401, 328);
    this.setContentPane(getJContentPane());
    this.setTitle("JFrame");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane() {
    if (jContentPane == null) {
      
      jContentPane = new JPanel();
      jContentPane.setLayout(new BorderLayout());
      jContentPane.add(manager.getAnzeigeKomponente(), BorderLayout.CENTER);
      jContentPane.add(getJPanel(), BorderLayout.NORTH);
      
    }
    return jContentPane;
  }
  
  
  private JPanel getJPanelMain() {
    if (jPanelMain == null) {
      jPanelMain = new JPanel();
      jPanelMain.setLayout(new FlowLayout());//new GridLayout(4,2)
      
      /* tabs einfuegen */
      for (int i = 0; i < 8; i++) {
//        ThumbnailAnzeigePanel tab = new ThumbnailAnzeigePanel();
//        centerPanel.add(tab);
//        manager.addSelectable(tab);
        SelectableJPanel a = new SelectableJPanel();
        a.add(new JLabel(" Text " + i + " ----"));
        jPanelMain.add(a);
        
        manager.addAuswaehlbar(a);
      }
    }
    return jPanelMain;
  }
  
  /**
   * This method initializes jPanel	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getJPanel() {
    if (jPanel == null) {
      jPanel = new JPanel();
      jPanel.setLayout(new GridBagLayout());
      jPanel.add(getJButton(), new GridBagConstraints());
      jPanel.add(getJButton1(), new GridBagConstraints());
    }
    return jPanel;
  }

  /**
   * This method initializes jButton	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getJButton() {
    if (jButton == null) {
      jButton = new JButton();
      jButton.setText("Clear Selection");
      jButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          manager.leereAuswahl();
        }
      });
    }
    return jButton;
  }

  /**
   * This method initializes jButton1	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getJButton1() {
    if (jButton1 == null) {
      jButton1 = new JButton();
      jButton1.setText("Select All");
      jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          manager.waehleAlleAus();
        }
      });
    }
    return jButton1;
  }

  public static void main(String[] args) {
    new ManagerTest().setVisible(true);
  }
}  //  @jve:decl-index=0:visual-constraint="10,10"
