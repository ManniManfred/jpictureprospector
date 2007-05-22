package ui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JProgressBar;
import java.awt.GridBagConstraints;

public class LadebalkenDialog extends JDialog {

  private static final long serialVersionUID = 1L;

  private JPanel jContentPane = null;

  private JProgressBar pbLadebalken = null;

  /**
   * @param owner
   */
  public LadebalkenDialog(Frame owner) {
    super(owner);
    initialize();
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(300, 200);
    this.setContentPane(getJContentPane());
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane() {
    if (jContentPane == null) {
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      jContentPane = new JPanel();
      jContentPane.setLayout(new GridBagLayout());
      jContentPane.add(getPbLadebalken(), gridBagConstraints);
    }
    return jContentPane;
  }

  /**
   * This method initializes pbLadebalken	
   * 	
   * @return javax.swing.JProgressBar	
   */
  private JProgressBar getPbLadebalken() {
    if (pbLadebalken == null) {
      pbLadebalken = new JProgressBar();
    }
    return pbLadebalken;
  }

}
