
package micycle.polygonmorphing.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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

import micycle.polygonmorphing.application.ExtendedController;
import micycle.polygonmorphing.tools.Constants;

public class FeatureDetectionMenu
extends JPanel
implements ActionListener {
    private ExtendedController ec;
    private JPanel parameters;
    private JTextField max_angle;
    private JTextField min_length;
    private JTextField max_fp;
    private JRadioButton nolimit;
    private JRadioButton limit;
    private JLabel max_angle_label;
    private JLabel min_length_label;
    private JLabel max_fp_label;

    public FeatureDetectionMenu(ExtendedController ec) {
        this.ec = ec;
        this.createMenu();
        this.diasableContent(this.parameters);
    }

    private void createMenu() {
        this.setLayout(new BorderLayout());
        ButtonGroup featureDetection = new ButtonGroup();
        JPanel checkbox_panel = new JPanel();
        checkbox_panel.setLayout(new GridLayout(2, 1));
        JRadioButton on = new JRadioButton("on", false);
        JRadioButton off = new JRadioButton("off", true);
        on.setActionCommand("on");
        off.setActionCommand("off");
        on.addActionListener(this);
        off.addActionListener(this);
        featureDetection.add(on);
        featureDetection.add(off);
        checkbox_panel.add(on);
        checkbox_panel.add(off);
        checkbox_panel.setBorder(Constants.BLACKLINE);
        this.add((Component)checkbox_panel, "West");
        this.max_angle = new JTextField(4);
        this.max_angle_label = new JLabel("Maximal angle:");
        this.max_angle.setToolTipText("Enter the maximal inner/outer angle for a vailid feature point");
        this.max_angle.setText(new Double(this.ec.getMax_angle()).toString());
        this.max_angle.addActionListener(new MaxAngleUpdate());
        this.min_length = new JTextField(4);
        this.min_length_label = new JLabel("Minmal length:");
        this.min_length.setToolTipText("Enter the minimal relative length of the shape at a feature point");
        this.min_length.setText(new Double(this.ec.getMin_length()).toString());
        this.min_length.addActionListener(new MinLengthUpdate());
        this.max_fp = new JTextField(4);
        this.max_fp_label = new JLabel("Max Feature Points:");
        this.max_fp.setToolTipText("Enter the maximal number of feature points for a shape");
        this.max_fp.setText(new Integer(this.ec.getMax_featurePoints()).toString());
        this.max_fp.addActionListener(new MaxFPUpdate());
        ButtonGroup b = new ButtonGroup();
        this.nolimit = new JRadioButton("Using Parameters", true);
        this.limit = new JRadioButton("Using Max");
        this.nolimit.setActionCommand("nolimit");
        this.limit.setActionCommand("limit");
        Chooser chooser = new Chooser();
        this.nolimit.addActionListener(chooser);
        this.limit.addActionListener(chooser);
        b.add(this.nolimit);
        b.add(this.limit);
        JPanel fdp = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        fdp.setLayout(gbl);
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.gridheight = 1;
        c.fill = 2;
        c.insets = new Insets(1, 1, 1, 1);
        c.gridx = 2;
        c.gridy = 0;
        this.addIntoGrid(fdp, this.max_angle_label, gbl, c);
        c.gridx = 4;
        this.addIntoGrid(fdp, this.max_angle, gbl, c);
        c.gridx = 2;
        c.gridy = 1;
        this.addIntoGrid(fdp, this.min_length_label, gbl, c);
        c.gridx = 4;
        this.addIntoGrid(fdp, this.min_length, gbl, c);
        c.gridx = 0;
        c.gridy = 0;
        this.addIntoGrid(fdp, this.nolimit, gbl, c);
        c.gridy = 1;
        this.addIntoGrid(fdp, this.limit, gbl, c);
        c.gridx = 2;
        c.gridy = 3;
        this.addIntoGrid(fdp, this.max_fp_label, gbl, c);
        c.gridx = 4;
        this.addIntoGrid(fdp, this.max_fp, gbl, c);
        this.add((Component)fdp, "Center");
        this.parameters = fdp;
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "FeaturePoint Detection"));
    }

    public ExtendedController getController() {
        return this.ec;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("on")) {
            this.nolimit.setEnabled(true);
            this.limit.setEnabled(true);
            if (this.nolimit.isSelected()) {
                this.nolimit.doClick();
            } else {
                this.limit.doClick();
            }
            this.ec.setFeatureDetection(true);
        } else if (e.getActionCommand().equals("off")) {
            this.diasableContent(this.parameters);
            this.ec.setFeatureDetection(false);
        } else {
            throw new IllegalArgumentException("Someting went horribly wrong...");
        }
    }

    private void addIntoGrid(JPanel container, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
        gridbag.setConstraints(comp, c);
        container.add(comp);
    }

    private void diasableContent(JPanel p) {
        Component[] comps = p.getComponents();
        for (int i = 0; i < comps.length; ++i) {
            comps[i].setEnabled(false);
        }
    }

    private class MaxAngleUpdate
    implements ActionListener {
        MaxAngleUpdate() {
        }

        public void actionPerformed(ActionEvent e) {
            JTextField text = (JTextField)e.getSource();
            try {
                double angle = Double.parseDouble(text.getText());
                if (angle <= 0.0) {
                    System.out.println("Only double values larger 0 are allowed as max angle.");
                    text.setText(new Double(FeatureDetectionMenu.this.ec.getMax_angle()).toString());
                } else if (angle >= 180.0) {
                    System.out.println("Only double values smaller 180 are allowed as max angle.");
                    text.setText(new Double(FeatureDetectionMenu.this.ec.getMax_angle()).toString());
                } else if (angle != FeatureDetectionMenu.this.ec.getMax_angle()) {
                    FeatureDetectionMenu.this.ec.setMax_angle(angle);
                }
            }
            catch (NumberFormatException nfe) {
                System.err.println("input for angle not recognised as double - ignoring input!");
                System.err.println(nfe.getStackTrace());
                text.setText(new Double(FeatureDetectionMenu.this.ec.getMax_angle()).toString());
            }
        }
    }

    private class MinLengthUpdate
    implements ActionListener {
        MinLengthUpdate() {
        }

        public void actionPerformed(ActionEvent e) {
            JTextField text = (JTextField)e.getSource();
            try {
                double length = Double.parseDouble(text.getText());
                if (length < 0.0) {
                    System.out.println("Only double values larger or equal to 0 are allowed as max angle.");
                    text.setText(new Double(FeatureDetectionMenu.this.ec.getMin_length()).toString());
                } else if (length >= 1.0) {
                    System.out.println("Only double values smaller 1 are allowed as max angle.");
                    text.setText(new Double(FeatureDetectionMenu.this.ec.getMin_length()).toString());
                } else if (length != FeatureDetectionMenu.this.ec.getMin_length()) {
                    FeatureDetectionMenu.this.ec.setMin_length(length);
                }
            }
            catch (NumberFormatException nfe) {
                System.err.println("input for min length not recognised as double - ignoring input!");
                System.err.println(nfe.getStackTrace());
                text.setText(new Double(FeatureDetectionMenu.this.ec.getMin_length()).toString());
            }
        }
    }

    private class MaxFPUpdate
    implements ActionListener {
        MaxFPUpdate() {
        }

        public void actionPerformed(ActionEvent e) {
            JTextField text = (JTextField)e.getSource();
            try {
                int max_fp = Integer.parseInt(text.getText());
                if (max_fp < 1) {
                    System.out.println("Only integer values larger 0 are allowed for number of skips.");
                    text.setText(new Integer(FeatureDetectionMenu.this.ec.getMax_featurePoints()).toString());
                } else if (max_fp > 100) {
                    System.out.println("For runtime reasons maximal 100 feature points per polygon are allowed.");
                    text.setText(new Integer(FeatureDetectionMenu.this.ec.getMax_featurePoints()).toString());
                } else if (max_fp != FeatureDetectionMenu.this.ec.getMax_featurePoints()) {
                    FeatureDetectionMenu.this.ec.setMax_featurePoints(max_fp);
                }
            }
            catch (NumberFormatException nfe) {
                System.err.println("input for max feature points not recognised as integer - ignoring input!");
                System.err.println(nfe.getStackTrace());
                text.setText(new Integer(FeatureDetectionMenu.this.ec.getMax_featurePoints()).toString());
            }
        }
    }

    private class Chooser
    implements ActionListener {
        Chooser() {
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("nolimit")) {
                this.switchContentes(true);
            } else if (e.getActionCommand().equals("limit")) {
                this.switchContentes(false);
            } else {
                throw new IllegalArgumentException("Problem with Actionlistener for FeatureDetection Mode");
            }
        }

        private void switchContentes(boolean nolimit) {
            FeatureDetectionMenu.this.ec.setLimit(!nolimit);
            FeatureDetectionMenu.this.max_fp.setEnabled(!nolimit);
            FeatureDetectionMenu.this.max_fp.setEditable(!nolimit);
            FeatureDetectionMenu.this.max_fp_label.setEnabled(!nolimit);
            FeatureDetectionMenu.this.max_angle.setEnabled(nolimit);
            FeatureDetectionMenu.this.max_angle.setEditable(nolimit);
            FeatureDetectionMenu.this.min_length.setEnabled(nolimit);
            FeatureDetectionMenu.this.min_length.setEnabled(nolimit);
            FeatureDetectionMenu.this.max_angle_label.setEnabled(nolimit);
            FeatureDetectionMenu.this.min_length_label.setEnabled(nolimit);
        }
    }
}

