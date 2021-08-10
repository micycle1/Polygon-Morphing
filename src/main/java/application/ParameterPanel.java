package application;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import tools.Constants;

public class ParameterPanel extends JPanel {
  private Controller controller;
  
  private ButtonGroup b;
  
  public ParameterPanel(Controller controller) {
    this.controller = controller;
    createComponents();
  }
  
  private void createComponents() {
    JPanel tmp = new JPanel();
    tmp.setLayout(new BorderLayout());
    this.b = new ButtonGroup();
    JRadioButton simple_ros = new JRadioButton("Use uniformly sampled edges");
    JRadioButton normal_ros = new JRadioButton("Use next feature points as boundary");
    simple_ros.setActionCommand("simple_ros");
    normal_ros.setActionCommand("normal_ros");
    RosConstruction rc = new RosConstruction(this);
    simple_ros.doClick();
    simple_ros.addActionListener(rc);
    normal_ros.addActionListener(rc);
    this.b.add(simple_ros);
    this.b.add(normal_ros);
    tmp.add(simple_ros, "North");
    tmp.add(normal_ros, "South");
    tmp.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Method of ROS creation"));
    JTextField skips = new JTextField(4);
    JLabel skips_label = new JLabel("Skips:");
    skips.setToolTipText("Enter the maximal number of skips allowed during the dp pathfinding process");
    skips.setText((new Integer(this.controller.getSkips() - 1)).toString());
    skips.addActionListener(new SkipsUpdate(this));
    JTextField ros_size = new JTextField(4);
    JLabel ros_size_label = new JLabel("Size of ROS:");
    ros_size.setToolTipText("Enter the total size of the Region of Support for a Feature Point");
    ros_size.setText((new Integer(this.controller.getROS_size())).toString());
    ros_size.addActionListener(new RosUpdate(this));
    JTextField sample_size = new JTextField(4);
    JLabel sample_size_label = new JLabel("Sample points:");
    sample_size.setToolTipText("Enter the number of sampling points between two poylgon vertices");
    sample_size.setText((new Integer(this.controller.getSample_rate())).toString());
    sample_size.addActionListener(new SampleUpdate(this));
    JLabel feature_var_label = new JLabel("Weight, Feature Variation:");
    JLabel feature_side_label = new JLabel("Weight, Feature Side Variation:");
    JLabel feature_size_label = new JLabel("Weight, Feature Size:");
    JLabel nummerator1 = new JLabel("                       1 /");
    JLabel nummerator2 = new JLabel("                       1 /");
    JLabel nummerator3 = new JLabel("                       1 /");
    String weight_label_tooltip = "Attention: Sum of all three weights must be equal 1.";
    nummerator1.setToolTipText(weight_label_tooltip);
    nummerator2.setToolTipText(weight_label_tooltip);
    nummerator3.setToolTipText(weight_label_tooltip);
    feature_var_label.setToolTipText(weight_label_tooltip);
    feature_side_label.setToolTipText(weight_label_tooltip);
    feature_size_label.setToolTipText(weight_label_tooltip);
    DenominatorUpdate du = new DenominatorUpdate(this);
    ExtendedController ec = (ExtendedController)this.controller;
    JTextField feature_var = new JTextField(4);
    feature_var.setToolTipText("Enter the denominator of the fraction, to change the weight for feature variation.\n Sum of all weights must be 1!");
    feature_var.setText((new Integer(ec.getFeature_var_denom())).toString());
    feature_var.setActionCommand("feature_var");
    feature_var.addActionListener(du);
    JTextField feature_side = new JTextField(4);
    feature_side.setToolTipText("Enter the denominator of the fraction, to change the weight for side feature variation.\n Sum of all weights must be 1!");
    feature_side.setText((new Integer(ec.getFeature_side_denom())).toString());
    feature_side.setActionCommand("feature_side");
    feature_side.addActionListener(du);
    JTextField feature_size = new JTextField(4);
    feature_size.setToolTipText("Enter the denominator of the fraction, to change the weight for feature size variation.\n Sum of all weights must be 1!");
    feature_size.setText((new Integer(ec.getFeature_size_denom())).toString());
    feature_size.setActionCommand("feature_size");
    feature_size.addActionListener(du);
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    setLayout(gbl);
    c.gridwidth = 2;
    c.weightx = 1.0D;
    c.gridheight = 1;
    c.fill = 2;
    c.insets = new Insets(1, 1, 1, 1);
    c.gridx = 0;
    c.gridy = 0;
    addIntoGrid(tmp, gbl, c);
    c.gridy = 1;
    addIntoGrid(skips_label, gbl, c);
    c.gridx = 2;
    c.fill = 0;
    addIntoGrid(skips, gbl, c);
    c.fill = 2;
    c.gridx = 0;
    c.gridy = 2;
    addIntoGrid(ros_size_label, gbl, c);
    c.gridx = 2;
    c.fill = 0;
    addIntoGrid(ros_size, gbl, c);
    c.fill = 2;
    c.gridx = 0;
    c.gridy = 3;
    addIntoGrid(sample_size_label, gbl, c);
    c.gridx = 2;
    c.fill = 0;
    addIntoGrid(sample_size, gbl, c);
    c.gridx = 0;
    c.gridy = 4;
    c.fill = 2;
    c.gridwidth = 1;
    addIntoGrid(feature_var_label, gbl, c);
    c.gridx = 1;
    c.fill = 0;
    addIntoGrid(nummerator1, gbl, c);
    c.gridx = 2;
    addIntoGrid(feature_var, gbl, c);
    c.gridy = 5;
    c.gridx = 0;
    c.fill = 2;
    addIntoGrid(feature_side_label, gbl, c);
    c.fill = 0;
    c.gridx = 1;
    addIntoGrid(nummerator2, gbl, c);
    c.gridx = 2;
    addIntoGrid(feature_side, gbl, c);
    c.gridy = 6;
    c.gridx = 0;
    c.fill = 2;
    addIntoGrid(feature_size_label, gbl, c);
    c.fill = 0;
    c.gridx = 1;
    addIntoGrid(nummerator3, gbl, c);
    c.gridx = 2;
    addIntoGrid(feature_size, gbl, c);
    setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Parameters"));
  }
  
  private class SkipsUpdate implements ActionListener {
    final ParameterPanel this$0;
    
    SkipsUpdate(ParameterPanel this$0) {
      this.this$0 = this$0;
    }
    
    public void actionPerformed(ActionEvent e) {
      JTextField text = (JTextField)e.getSource();
      try {
        int skips = Integer.parseInt(text.getText());
        if (skips < 0) {
          System.out.println("Only integer values larger 0 are allowed for number of skips.");
          text.setText((new Integer(this.this$0.controller.getSkips() - 1)).toString());
        } else if (skips != this.this$0.controller.getSkips() - 1) {
          this.this$0.controller.setSkips(skips + 1);
        } 
      } catch (NumberFormatException nfe) {
        System.err.println("input for skips not recognised as integer - ignoring input!");
        System.err.println(nfe.getStackTrace());
        text.setText((new Integer(this.this$0.controller.getSkips() - 1)).toString());
      } 
    }
  }
  
  private class RosConstruction implements ActionListener {
    final ParameterPanel this$0;
    
    RosConstruction(ParameterPanel this$0) {
      this.this$0 = this$0;
    }
    
    public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().equals("simple_ros")) {
        this.this$0.controller.setSimple_ROS(true);
      } else {
        this.this$0.controller.setSimple_ROS(false);
      } 
    }
  }
  
  private class RosUpdate implements ActionListener {
    final ParameterPanel this$0;
    
    RosUpdate(ParameterPanel this$0) {
      this.this$0 = this$0;
    }
    
    public void actionPerformed(ActionEvent e) {
      JTextField text = (JTextField)e.getSource();
      try {
        int sample_size = Integer.parseInt(text.getText());
        if (sample_size < 3) {
          System.out.println("Only integer values larger or equal to 3 are allowed for size of ros.");
          text.setText((new Integer(this.this$0.controller.getROS_size())).toString());
        } else if (sample_size != this.this$0.controller.getROS_size()) {
          this.this$0.controller.setROS_size(sample_size);
        } 
      } catch (NumberFormatException nfe) {
        System.err.println("input for sample size not recognised as integer - ignoring input!");
        System.err.println(nfe.getStackTrace());
        text.setText((new Integer(this.this$0.controller.getROS_size())).toString());
      } 
    }
  }
  
  private class SampleUpdate implements ActionListener {
    final ParameterPanel this$0;
    
    SampleUpdate(ParameterPanel this$0) {
      this.this$0 = this$0;
    }
    
    public void actionPerformed(ActionEvent e) {
      JTextField text = (JTextField)e.getSource();
      try {
        int sample_rate = Integer.parseInt(text.getText());
        if (sample_rate < 1) {
          System.out.println("Only integer values larger 1 are allowed for sample rate.");
          text.setText((new Integer(this.this$0.controller.getSample_rate())).toString());
        } else if (sample_rate != this.this$0.controller.getSample_rate()) {
          this.this$0.controller.setSample_rate(sample_rate);
        } 
      } catch (NumberFormatException nfe) {
        System.err.println("input for sample size not recognised as integer - ignoring input!");
        System.err.println(nfe.getStackTrace());
        text.setText((new Integer(this.this$0.controller.getSample_rate())).toString());
      } 
    }
  }
  
  private class DenominatorUpdate implements ActionListener {
    final ParameterPanel this$0;
    
    DenominatorUpdate(ParameterPanel this$0) {
      this.this$0 = this$0;
    }
    
    public void actionPerformed(ActionEvent e) {
      ExtendedController ec = (ExtendedController)this.this$0.controller;
      JTextField text = (JTextField)e.getSource();
      try {
        int denom = Integer.parseInt(text.getText());
        if (denom < 1) {
          System.out.println("Only integer values larger 1 are allowed as denominator.");
          text.setText((new Integer(this.this$0.controller.getSample_rate())).toString());
        } else if (e.getActionCommand().equals("feature_var")) {
          if (denom != ec.getFeature_var_denom())
            ec.setFeature_var_denom(denom); 
        } else if (e.getActionCommand().equals("feature_side")) {
          if (denom != ec.getFeature_side_denom())
            ec.setFeature_side_denom(denom); 
        } else if (e.getActionCommand().equals("feature_size") && 
          denom != ec.getFeature_size_denom()) {
          ec.setFeature_size_denom(denom);
        } 
      } catch (NumberFormatException nfe) {
        System.err.println("input for denominator not recognised as integer - ignoring input!");
        System.err.println(nfe.getStackTrace());
        text.setText((new Integer(this.this$0.controller.getSample_rate())).toString());
      } 
    }
  }
  
  private void addIntoGrid(JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
    gridbag.setConstraints(comp, c);
    add(comp);
  }
}
