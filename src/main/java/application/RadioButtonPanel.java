
package application;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import tools.Constants;

public class RadioButtonPanel
extends Observable
implements ActionListener {
    private JPanel panel;
    private ButtonGroup b;
    private JRadioButton stepsButton;
    private JRadioButton framesButton;
    private JTextField stepCount;
    private JTextField fpsInput;
    private JTextField timeInput;
    private JLabel steps_label;
    private JLabel fps_label;
    private JLabel time_label;
    private int steps;
    private int fps;
    double time;
    private Integer intSteps;
    private Integer intFps;
    private Double doubleTime;

    public RadioButtonPanel(int steps, int fps, double time) {
        this.steps = steps;
        this.fps = fps;
        this.time = time;
        this.intSteps = new Integer(this.steps);
        this.intFps = new Integer(this.fps);
        this.doubleTime = new Double(this.time);
        this.createComponents();
        this.stepsButton.doClick();
        this.panel.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Animation Parameters"));
    }

    private void createComponents() {
        this.panel = new JPanel();
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 1;
        c.weightx = 1.0;
        this.panel.setLayout(grid);
        this.b = new ButtonGroup();
        this.stepsButton = new JRadioButton("Steps");
        this.framesButton = new JRadioButton("Frames");
        this.stepCount = new JTextField(10);
        this.stepCount.setText(this.intSteps.toString());
        this.stepCount.setActionCommand("stepcount");
        this.fpsInput = new JTextField(3);
        this.fpsInput.setText(this.intFps.toString());
        this.fpsInput.setActionCommand("fps");
        this.fpsInput.setEditable(false);
        this.fpsInput.setEnabled(false);
        this.timeInput = new JTextField(3);
        this.timeInput.setText(this.doubleTime.toString());
        this.timeInput.setActionCommand("time");
        this.timeInput.setEditable(false);
        this.timeInput.setEnabled(false);
        this.stepsButton.setSelected(true);
        this.stepsButton.setActionCommand("Steps");
        this.framesButton.setActionCommand("Frames");
        this.b.add(this.stepsButton);
        this.b.add(this.framesButton);
        this.steps_label = new JLabel("Number of animation steps:");
        this.fps_label = new JLabel("Frames per second");
        this.time_label = new JLabel("Animation time in seconds:");
        this.stepsButton.addActionListener(this);
        this.framesButton.addActionListener(this);
        this.stepCount.addActionListener(this);
        this.fpsInput.addActionListener(this);
        this.timeInput.addActionListener(this);
        c.gridwidth = -1;
        this.addIntoGrid(this.stepsButton, grid, c);
        c.gridwidth = 0;
        this.addIntoGrid(this.framesButton, grid, c);
        c.weightx = 1.0;
        c.gridwidth = -1;
        this.addIntoGrid(this.steps_label, grid, c);
        c.gridwidth = 0;
        this.addIntoGrid(this.stepCount, grid, c);
        c.gridwidth = -1;
        this.addIntoGrid(this.fps_label, grid, c);
        c.gridwidth = 0;
        this.addIntoGrid(this.fpsInput, grid, c);
        c.gridwidth = -1;
        this.addIntoGrid(this.time_label, grid, c);
        c.gridwidth = 0;
        this.addIntoGrid(this.timeInput, grid, c);
    }

    public void actionPerformed(ActionEvent e) {
        if ("Steps".equals(e.getActionCommand())) {
            this.switchEditable(true);
            this.notifyChanged("RadioSteps");
        }
        if ("Frames".equals(e.getActionCommand())) {
            this.switchEditable(false);
            this.notifyChanged("RadioFrames");
        }
        if ("stepcount".equals(e.getActionCommand())) {
            int oldsteps = this.steps;
            try {
                int temp = Integer.parseInt(this.stepCount.getText());
                if (temp < 1) {
                    this.stepCount.setText(new Integer(oldsteps).toString());
                    System.err.println("Only positive integer values >= 2 will be accepted!");
                } else {
                    this.setSteps(temp);
                }
            }
            catch (NumberFormatException nfe) {
                this.stepCount.setText(new Integer(oldsteps).toString());
                System.err.println("input not recognised as integer - ignoring input!");
            }
        }
        if ("fps".equals(e.getActionCommand())) {
            try {
                int temp = Integer.parseInt(this.fpsInput.getText());
                if (temp < 1) {
                    this.fpsInput.setText(new Integer(this.fps).toString());
                    System.err.println("At least 1 frameper second is required.");
                } else if (1 <= temp && temp <= 15) {
                    this.setFps(temp);
                } else if (temp <= 25) {
                    System.out.println("A framerate of " + temp + " frames per second might not work properly" + " on some computers.");
                    this.setFps(temp);
                } else {
                    System.err.println("Framerate to high. Setting framerate to per 25 frames per second.");
                    this.fpsInput.setText(new Integer(25).toString());
                    this.setFps(25);
                }
            }
            catch (NumberFormatException nfe) {
                this.fpsInput.setText(new Integer(this.fps).toString());
                System.err.println("input not recognised as integer - ignoring input!");
            }
        }
        if ("time".equals(e.getActionCommand())) {
            try {
                double temp = Double.parseDouble(this.timeInput.getText());
                if (temp <= 0.0) {
                    this.timeInput.setText(new Double(this.time).toString());
                    System.err.println("No negative animation time allowed.");
                } else if (temp > 0.0 && temp < 5.0) {
                    this.setTime(temp);
                } else if (temp <= 60.0) {
                    System.out.println("Using an animation time > than 15 seconds is not recommended.");
                    this.setTime(temp);
                } else {
                    System.err.println("Value for animation time too high, setting it to 60 seconds!");
                    this.timeInput.setText(new Double(60.0).toString());
                    this.setTime(60.0);
                }
            }
            catch (NumberFormatException nfe) {
                this.timeInput.setText(new Double(this.time).toString());
                System.err.println("input not recognised as double - ignoring input!");
            }
        }
    }

    public JPanel getPanel() {
        return this.panel;
    }

    private void setSteps(int steps) {
        this.steps = steps;
        this.notifyChanged("steps");
    }

    public int getSteps() {
        return this.steps;
    }

    private void setFps(int fps) {
        this.fps = fps;
        this.notifyChanged("fps");
    }

    public int getFrames() {
        return this.fps;
    }

    private void setTime(double time) {
        this.time = time;
        this.notifyChanged("time");
    }

    public double getTime() {
        return this.time;
    }

    public void notifyChanged(Object arg) {
        this.setChanged();
        this.notifyObservers(arg);
    }

    private void addIntoGrid(JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
        gridbag.setConstraints(comp, c);
        this.panel.add(comp);
    }

    private void switchEditable(boolean steps) {
        this.stepCount.setEditable(steps);
        this.stepCount.setEnabled(steps);
        this.steps_label.setEnabled(steps);
        this.fpsInput.setEditable(!steps);
        this.fpsInput.setEnabled(!steps);
        this.timeInput.setEditable(!steps);
        this.timeInput.setEnabled(!steps);
        this.fps_label.setEnabled(!steps);
        this.time_label.setEnabled(!steps);
    }
}

