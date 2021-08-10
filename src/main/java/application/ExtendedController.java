
package application;

import application.CGSlider;
import application.ComboBoxMenu;
import application.Controller;
import application.FeatureDetectionMenu;
import application.LoadSaveMenu;
import application.Model;
import application.ParameterPanel;
import application.RadioButtonPanel;
import controls.AnimationSliderListener;
import controls.ClearButtonListener;
import featurePointDetection.FeaturePointDetector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import morph.AnimationDisplay;
import morph.Animator;
import morph.MorphCalculator;
import shapes.Polygon;
import tools.Constants;
import tools.Path;

public class ExtendedController
extends Controller {
    private int steps;
    private int fps;
    private double time;
    private JButton animator;
    private JButton calculator;
    private boolean frames;
    private AnimationDisplay ad;
    private LoadSaveMenu lsm;
    private ComboBoxMenu cbm;
    private int feature_var_denom;
    private int feature_side_denom;
    private int feature_size_denom;
    private boolean weightSumOk;
    private double max_angle;
    private double min_length;
    private int max_featurePoints;
    private boolean featureDetection;
    private boolean isLimit;
    private boolean fd_changed;
    private FeaturePointDetector fpd;
    private Polygon svgStart;
    private Polygon svgEnd;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;

    public ExtendedController(boolean applet) {
        super(applet);
    }

    protected Component createComponents() {
        this.fpd = new FeaturePointDetector();
        this.steps = 50;
        this.fps = 15;
        this.time = 5.0;
        this.feature_var_denom = Constants.DENOMINATORS[0];
        this.feature_side_denom = Constants.DENOMINATORS[1];
        this.feature_size_denom = Constants.DENOMINATORS[2];
        this.weightSumOk = true;
        this.max_angle = 130.0;
        this.min_length = 0.01;
        this.max_featurePoints = 35;
        this.featureDetection = false;
        this.simple_ROS = true;
        this.isLimit = false;
        this.fd_changed = false;
        JPanel panel = new JPanel(new BorderLayout());
        JPanel tmp = new JPanel();
        JPanel tmp2 = new JPanel(new BorderLayout());
        JPanel menu = new JPanel(new BorderLayout());
        tmp2.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tmp2.add((Component)this.getView(), "Center");
        tmp = new JPanel();
        tmp.add((Component)this.getOriginal(), "West");
        tmp.add((Component)this.getTarget(), "East");
        tmp2.add((Component)tmp, "North");
        menu = this.createMenu();
        panel.add((Component)tmp2, "Center");
        panel.add((Component)menu, "East");
        this.calculator.setEnabled(false);
        this.changed = true;
        this.frames = false;
        this.checkChanged();
        return panel;
    }

    protected JPanel createMenu() {
        JButton animate;
        JButton calcMorph;
        JPanel menu = new JPanel();
        JPanel tmp = new JPanel();
        JPanel tmp2 = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 6;
        c.weightx = 1.0;
        c.gridheight = 6;
        c.fill = 2;
        c.insets = new Insets(1, 1, 1, 1);
        c.gridx = 0;
        c.gridy = 0;
        this.calculator = calcMorph = new JButton("Calculate Morph");
        this.calculator.addActionListener(new CalcListener());
        calcMorph.setSize(150, 25);
        CalculateMorphButtonListener cmbl = new CalculateMorphButtonListener();
        calcMorph.addActionListener(cmbl);
        this.animator = animate = new JButton("Animate!");
        animate.setSize(120, 25);
        AnimationButtonListener abl = new AnimationButtonListener();
        animate.addActionListener(abl);
        tmp.add(calcMorph);
        tmp.add(animate);
        JPanel animationMenu = new JPanel();
        animationMenu.setLayout(new BorderLayout());
        animationMenu.add((Component)tmp, "North");
        JPanel menus = new JPanel();
        RadioButtonPanel rbp = new RadioButtonPanel(this.steps, this.fps, this.time);
        animationMenu.add((Component)rbp.getPanel(), "Center");
        animationMenu.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Animation Menu"));
        ParameterPanel pp = new ParameterPanel(this);
        this.animationSlider = new CGSlider("Animation Step", 1, 1, 1, new AnimationSliderListener(this));
        animationMenu.add((Component)this.animationSlider, "South");
        menus.setLayout(gbl);
        Constants.addIntoGrid(menus, animationMenu, gbl, c);
        c.gridy = 6;
        if (!this.applet) {
            this.lsm = new LoadSaveMenu(this);
            Constants.addIntoGrid(menus, this.lsm, gbl, c);
        } else {
            this.cbm = new ComboBoxMenu(this);
            Constants.addIntoGrid(menus, this.cbm, gbl, c);
        }
        c.gridy = 12;
        Constants.addIntoGrid(menus, pp, gbl, c);
        c.gridy = 18;
        Constants.addIntoGrid(menus, new FeatureDetectionMenu(this), gbl, c);
        menus.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rbp.addObserver(this);
        CGSlider zoom = new CGSlider("Zoomfaktor", 1, 10, 1, new Controller.ZoomFactorListener(this, this.getModel()));
        JButton clear = new JButton("Clear");
        clear.setSize(new Dimension(80, 25));
        ClearButtonListener cbl = new ClearButtonListener();
        cbl.append(this.getModel());
        cbl.append(zoom);
        clear.addActionListener(cbl);
        gbl = new GridBagLayout();
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        tmp2.setLayout(gbl);
        Constants.addIntoGrid(tmp2, zoom, gbl, c);
        c.gridx = 3;
        Constants.addIntoGrid(tmp2, clear, gbl, c);
        tmp2.setBorder(Constants.BLACKLINE);
        gbl = new GridBagLayout();
        menu.setLayout(gbl);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        Constants.addIntoGrid(menu, menus, gbl, c);
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1.0;
        c.anchor = 20;
        c.insets = new Insets(10, 0, 0, 0);
        Constants.addIntoGrid(menu, tmp2, gbl, c);
        return menu;
    }

    public void update(Observable sender, Object arg) {
        Class<?> class_ = sender.getClass();
        Class<?> class_2 = class$0;
        if (class_2 == null) {
            try {
                class_2 = class$0 = Class.forName("application.RadioButtonPanel");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        if (class_.equals(class_2)) {
            if (arg.equals("steps")) {
                int tmp = ((RadioButtonPanel)sender).getSteps();
                if (this.steps != tmp) {
                    this.changed = true;
                    this.steps = tmp;
                    System.out.println(this.steps);
                }
            } else if (arg.equals("fps")) {
                int tmp = ((RadioButtonPanel)sender).getFrames();
                if (this.fps != tmp) {
                    this.changed = true;
                    this.fps = tmp;
                    System.out.println(this.fps);
                }
            } else if (arg.equals("time")) {
                double tmp = ((RadioButtonPanel)sender).getTime();
                if (this.time != tmp) {
                    this.changed = true;
                    this.time = tmp;
                    System.out.println(this.time);
                }
            } else if (arg.equals("RadioSteps")) {
                this.changed = true;
                this.frames = false;
            } else if (arg.equals("RadioFrames")) {
                this.changed = true;
                this.frames = true;
            } else {
                throw new IllegalArgumentException("Illegal argument: " + arg.toString() + " for notify change.");
            }
        }
        Class<?> class_3 = sender.getClass();
        Class<?> class_4 = class$1;
        if (class_4 == null) {
            try {
                class_4 = class$1 = Class.forName("application.Model");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        if (class_3.equals(class_4)) {
            super.update(sender, arg);
            if (arg.equals("M_append")) {
                if ((Model)sender == this.getOriginal().getModel()) {
                    Polygon tmp = this.getOriginal().getModel().getPolygon();
                    if (this.getStart() != tmp) {
                        this.changed = true;
                    }
                    this.setStart(new Polygon(tmp));
                    if (!this.applet) {
                        this.lsm.setEnableSource(true);
                    }
                } else if ((Model)sender == this.getTarget().getModel()) {
                    Polygon tmp = this.getTarget().getModel().getPolygon();
                    if (this.getFinish() != tmp) {
                        this.changed = true;
                    }
                    this.setFinish(new Polygon(tmp));
                    if (!this.applet) {
                        this.lsm.setEnableTarget(true);
                    }
                }
            } else if (arg.equals("cleared")) {
                this.changed = true;
                if ((Model)sender == this.getOriginal().getModel()) {
                    this.setStart(null);
                } else if ((Model)sender == this.getTarget().getModel()) {
                    this.setFinish(null);
                }
            } else if ((Model)sender == this.getOriginal().getModel()) {
                this.changed = true;
                if (!this.applet) {
                    this.lsm.setEnableSource(false);
                }
                this.calculator.setEnabled(false);
            } else if ((Model)sender == this.getTarget().getModel()) {
                this.changed = true;
                if (!this.applet) {
                    this.lsm.setEnableTarget(false);
                }
                this.calculator.setEnabled(false);
            }
            if (this.getFinish() != null && this.getStart() != null) {
                this.calculator.setEnabled(true);
            } else {
                this.calculator.setEnabled(false);
            }
        }
        this.checkChanged();
    }

    public void stopAnimator() {
        this.ad = null;
        this.ad = !this.frames ? new AnimationDisplay(this.getMorphs(), this.getModel(), this) : new AnimationDisplay(this.getMorphs(), this.getModel(), this, this.fps);
        this.animationSlider.setEnabled(true);
    }

    private void checkChanged() {
        if (!this.weightSumOk) {
            this.animator.setEnabled(false);
            this.calculator.setEnabled(false);
            if (!this.applet) {
                this.lsm.setEnableMorph(false);
            }
            this.animationSlider.setEnabled(false);
        } else {
            if (this.getStart() != null && this.getFinish() != null) {
                this.calculator.setEnabled(true);
            }
            if (this.changed) {
                this.animator.setEnabled(false);
                if (!this.applet) {
                    this.lsm.setEnableMorph(false);
                }
                this.animationSlider.setEnabled(false);
            } else {
                this.animator.setEnabled(true);
                if (!this.applet) {
                    this.lsm.setEnableMorph(true);
                }
            }
        }
    }

    public void setSkips(int skips) {
        if (this.getSkips() != skips) {
            this.changed = true;
        }
        super.setSkips(skips);
        System.out.println("skips = " + this.getSkips());
        this.checkChanged();
    }

    public void setSample_rate(int sample_rate) {
        if (this.getSample_rate() != sample_rate) {
            this.changed = true;
        }
        super.setSample_rate(sample_rate);
        System.out.println("sample rate = " + this.getSample_rate());
        this.checkChanged();
    }

    public void setROS_size(int ros_size) {
        if (this.getROS_size() != ros_size) {
            this.changed = true;
        }
        super.setROS_size(ros_size);
        System.out.println("ROS = " + this.getROS_size());
        this.checkChanged();
    }

    public void setFeature_var_denom(int feature_var_denom) {
        this.feature_var_denom = feature_var_denom;
        this.checkWeights();
    }

    public int getFeature_var_denom() {
        return this.feature_var_denom;
    }

    public void setFeature_side_denom(int feature_side_denom) {
        this.feature_side_denom = feature_side_denom;
        this.checkWeights();
    }

    public int getFeature_side_denom() {
        return this.feature_side_denom;
    }

    public void setFeature_size_denom(int feature_size_denom) {
        this.feature_size_denom = feature_size_denom;
        this.checkWeights();
    }

    public int getFeature_size_denom() {
        return this.feature_size_denom;
    }

    private void checkWeights() {
        long numerator_side = this.feature_size_denom * this.feature_var_denom;
        long numerator_size = this.feature_side_denom * this.feature_var_denom;
        long numerator_var = this.feature_side_denom * this.feature_size_denom;
        long total_denominator = this.feature_side_denom * this.feature_size_denom * this.feature_var_denom;
        this.weightSumOk = numerator_side + numerator_size + numerator_var == total_denominator;
        this.changed = true;
        this.checkChanged();
    }

    public void setMax_angle(double max_angle) {
        this.max_angle = max_angle;
        this.fd_changed = true;
        this.fpd.setMax_angle(max_angle);
        this.changed = true;
        this.checkChanged();
    }

    public double getMax_angle() {
        return this.max_angle;
    }

    public void setMin_length(double min_length) {
        this.min_length = min_length;
        this.fd_changed = true;
        this.fpd.setMin_length(min_length);
        this.changed = true;
        this.checkChanged();
    }

    public double getMin_length() {
        return this.min_length;
    }

    public void setMax_featurePoints(int max_featurePoints) {
        this.max_featurePoints = max_featurePoints;
        this.fd_changed = true;
        this.fpd.setMax_featurePoints(max_featurePoints);
        this.changed = true;
        this.checkChanged();
    }

    public int getMax_featurePoints() {
        return this.max_featurePoints;
    }

    public void setFeatureDetection(boolean featureDetection) {
        if (this.featureDetection != featureDetection) {
            this.featureDetection = featureDetection;
            this.changed = true;
        }
        this.checkChanged();
    }

    public boolean isFeatureDetection() {
        return this.featureDetection;
    }

    public void setLimit(boolean isLimit) {
        this.isLimit = isLimit;
        this.changed = true;
        this.checkChanged();
    }

    public boolean isLimit() {
        return this.isLimit;
    }

    public void setSimple_ROS(boolean simple_ROS) {
        if (simple_ROS != this.isSimple_ROS()) {
            this.changed = true;
            this.simple_ROS = simple_ROS;
        }
        this.checkChanged();
    }

    public String toSVG() {
        int width = 640;
        int height = 480;
        StringBuffer buff = new StringBuffer();
        buff.append("<?xml version=\"1.0\" standalone=\"no\"?>\n");
        buff.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");
        buff.append("<svg width=\"" + (width + 110) + "\" height=\"" + height + "\" viewBox=\"0 0 " + (width + 100) + " " + height + "\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n");
        buff.append("\t<desc>SVG-representation of the polygon Morphing</desc>\n");
        buff.append(this.svgStart.toSVG());
        buff.deleteCharAt(buff.length() - 3);
        buff.append("<animate begin=\"button.click\" dur=\"" + this.time * 2.0 + "s\" repeatCount=\"1\" fill=\"freeze\" attributeName=\"d\" ");
        buff.append("values=");
        StringBuffer tmp = new StringBuffer();
        tmp.append(this.svgStart.toSVGPath());
        tmp.delete(0, 6);
        tmp.deleteCharAt(tmp.length() - 2);
        buff.append(tmp);
        buff.append(";\n");
        tmp = new StringBuffer();
        tmp.append(this.svgEnd.toSVGPath());
        tmp.delete(4, 7);
        tmp.deleteCharAt(tmp.length() - 2);
        buff.append(tmp);
        buff.append(";\n");
        tmp = new StringBuffer();
        tmp.append(this.svgStart.toSVGPath());
        tmp.delete(4, 7);
        buff.append(tmp);
        buff.append("/> \n");
        buff.append("</path>\n");
        buff.append("<text x=\"" + (width + 50) + "\" y=\"" + 40 + "\" style=\"font-size:25;fill:black;text-anchor:middle\">Start</text>");
        buff.append("<rect id=\"button\" x=\"" + width + "\" y=\"" + 10 + "\" width=\"100\" height=\"50\" style=\"fill;black;fill-opacity:0.1\"/>");
        buff.append("\t\t<path id=\"bla\" d=\"M 640 495 L -320 495\" fill=\"none\" stroke=\"none\" stroke-width=\"0.5\" />\n");
        buff.append("\t\t<text x=\"0\" y=\"0\" \n\t\t\t font-family=\"Verdana\" font-size=\"10\" fill=\"green\" >\n");
        buff.append("\t\t\t\t <tspan font-weight=\"bold\" font-style=\"italic\" fill=\"black\"> really crappy </tspan>\n");
        buff.append("\t\t\t  SVG-implementation by Sven Albrecht in fall 2006\n");
        buff.append("\t\t\t\t <animateMotion dur=\"10s\" repeatCount=\"indefinite\" >\n");
        buff.append("\t\t\t\t\t <mpath xlink:href=\"#bla\"/>\n");
        buff.append("\t\t\t\t </animateMotion> \n");
        buff.append("\t\t</text>\n");
        buff.append("</svg>");
        return new String(buff);
    }

    private class CalcListener
    implements ActionListener {
        CalcListener() {
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    protected class CalculateMorphButtonListener
    implements ActionListener {
        protected CalculateMorphButtonListener() {
        }

        public void actionPerformed(ActionEvent arg0) {
            if (ExtendedController.this.changed) {
                Polygon source = new Polygon(ExtendedController.this.getStart());
                Polygon target = new Polygon(ExtendedController.this.getFinish());
                source.changeSize(2.0);
                target.changeSize(2.0);
                if (!ExtendedController.this.featureDetection) {
                    source.setAllVerticesToFeaturePoints();
                    target.setAllVerticesToFeaturePoints();
                } else {
                    ExtendedController.this.fpd.setMax_angle(ExtendedController.this.max_angle);
                    ExtendedController.this.fpd.setMax_featurePoints(ExtendedController.this.max_featurePoints);
                    ExtendedController.this.fpd.setMin_length(ExtendedController.this.min_length);
                    if (!ExtendedController.this.isLimit) {
                        source = ExtendedController.this.fpd.featureDetection(source);
                        target = ExtendedController.this.fpd.featureDetection(target);
                    } else {
                        ExtendedController.this.fpd.setMax_angle(179.0);
                        ExtendedController.this.fpd.setMin_length(0.01);
                        source = ExtendedController.this.fpd.featureDetection(source);
                        ExtendedController.this.fpd.filterMostProminent(source);
                        target = ExtendedController.this.fpd.featureDetection(target);
                        ExtendedController.this.fpd.filterMostProminent(target);
                    }
                }
                source.preparePolygon(ExtendedController.this.getSample_rate(), ExtendedController.this.getROS_size(), ExtendedController.this.isSimple_ROS());
                target.preparePolygon(ExtendedController.this.getSample_rate(), ExtendedController.this.getROS_size(), ExtendedController.this.isSimple_ROS());
                Path path = MorphCalculator.calculatePath(source, target, ExtendedController.this.getSkips());
                Path finalpath = path.getFinalPath();
                Polygon[] morphablePolygons = MorphCalculator.createCompleteMorph2(finalpath, path, source, target);
                source = morphablePolygons[0];
                target = morphablePolygons[1];
                ExtendedController.this.svgStart = source;
                ExtendedController.this.svgEnd = target;
                if (!ExtendedController.this.frames) {
                    ExtendedController.this.setMorphs(Animator.animate(source, target, ExtendedController.this.steps));
                    ExtendedController.this.ad = new AnimationDisplay(ExtendedController.this.getMorphs(), ExtendedController.this.getModel(), ExtendedController.this);
                } else {
                    ExtendedController.this.setMorphs(Animator.animate(source, target, ExtendedController.this.fps, ExtendedController.this.time));
                    ExtendedController.this.ad = new AnimationDisplay(ExtendedController.this.getMorphs(), ExtendedController.this.getModel(), ExtendedController.this, ExtendedController.this.fps);
                }
                ExtendedController.this.getModel().reset();
                ExtendedController.this.getModel().append(source);
                ExtendedController.this.changed = false;
                ExtendedController.this.checkChanged();
                ExtendedController.this.animationSlider.setValue(0);
                ExtendedController.this.animationSlider.setMaximum(ExtendedController.this.getMorphs().length);
                ExtendedController.this.animationSlider.setEnabled(true);
            }
        }
    }

    protected class AnimationButtonListener
    implements ActionListener {
        protected AnimationButtonListener() {
        }

        public void actionPerformed(ActionEvent arg0) {
            ExtendedController.this.animationSlider.setEnabled(false);
            ExtendedController.this.ad.start();
        }
    }
}

