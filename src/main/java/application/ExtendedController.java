package application;

import controls.AnimationSliderListener;
import controls.CGSliderListener;
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

public class ExtendedController extends Controller {
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
  
  static Class class$0;
  
  static Class class$1;
  
  public ExtendedController(boolean applet) {
    super(applet);
  }
  
  protected Component createComponents() {
    this.fpd = new FeaturePointDetector();
    this.steps = 50;
    this.fps = 15;
    this.time = 5.0D;
    this.feature_var_denom = Constants.DENOMINATORS[0];
    this.feature_side_denom = Constants.DENOMINATORS[1];
    this.feature_size_denom = Constants.DENOMINATORS[2];
    this.weightSumOk = true;
    this.max_angle = 130.0D;
    this.min_length = 0.01D;
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
    tmp2.add(getView(), "Center");
    tmp = new JPanel();
    tmp.add(getOriginal(), "West");
    tmp.add(getTarget(), "East");
    tmp2.add(tmp, "North");
    menu = createMenu();
    panel.add(tmp2, "Center");
    panel.add(menu, "East");
    this.calculator.setEnabled(false);
    this.changed = true;
    this.frames = false;
    checkChanged();
    return panel;
  }
  
  protected JPanel createMenu() {
    JPanel menu = new JPanel();
    JPanel tmp = new JPanel();
    JPanel tmp2 = new JPanel();
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.gridwidth = 6;
    c.weightx = 1.0D;
    c.gridheight = 6;
    c.fill = 2;
    c.insets = new Insets(1, 1, 1, 1);
    c.gridx = 0;
    c.gridy = 0;
    JButton calcMorph = new JButton("Calculate Morph");
    this.calculator = calcMorph;
    this.calculator.addActionListener(new CalcListener(this));
    calcMorph.setSize(150, 25);
    CalculateMorphButtonListener cmbl = new CalculateMorphButtonListener(this);
    calcMorph.addActionListener(cmbl);
    JButton animate = new JButton("Animate!");
    this.animator = animate;
    animate.setSize(120, 25);
    AnimationButtonListener abl = new AnimationButtonListener(this);
    animate.addActionListener(abl);
    tmp.add(calcMorph);
    tmp.add(animate);
    JPanel animationMenu = new JPanel();
    animationMenu.setLayout(new BorderLayout());
    animationMenu.add(tmp, "North");
    JPanel menus = new JPanel();
    RadioButtonPanel rbp = new RadioButtonPanel(this.steps, this.fps, this.time);
    animationMenu.add(rbp.getPanel(), "Center");
    animationMenu.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Animation Menu"));
    ParameterPanel pp = new ParameterPanel(this);
    this.animationSlider = new CGSlider("Animation Step", 1, 1, 1, (CGSliderListener)new AnimationSliderListener(this));
    animationMenu.add(this.animationSlider, "South");
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
    CGSlider zoom = new CGSlider("Zoomfaktor", 1, 10, 1, new Controller.ZoomFactorListener(this, getModel()));
    JButton clear = new JButton("Clear");
    clear.setSize(new Dimension(80, 25));
    ClearButtonListener cbl = new ClearButtonListener();
    cbl.append(getModel());
    cbl.append(zoom);
    clear.addActionListener((ActionListener)cbl);
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
    c.weighty = 1.0D;
    c.anchor = 20;
    c.insets = new Insets(10, 0, 0, 0);
    Constants.addIntoGrid(menu, tmp2, gbl, c);
    return menu;
  }
  
  public void update(Observable sender, Object arg) {
    if (class$0 == null)
      try {
      
      } catch (ClassNotFoundException classNotFoundException) {
        throw new NoClassDefFoundError(null.getMessage());
      }  
    if (class$0.equals(class$0 = Class.forName("application.RadioButtonPanel")))
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
        throw new IllegalArgumentException("Illegal argument: " + arg.toString() + 
            " for notify change.");
      }  
    if (class$1 == null)
      try {
      
      } catch (ClassNotFoundException classNotFoundException) {
        throw new NoClassDefFoundError(null.getMessage());
      }  
    if (class$1.equals(class$1 = Class.forName("application.Model"))) {
      super.update(sender, arg);
      if (arg.equals("M_append")) {
        if ((Model)sender == getOriginal().getModel()) {
          Polygon tmp = getOriginal().getModel().getPolygon();
          if (getStart() != tmp)
            this.changed = true; 
          setStart(new Polygon(tmp));
          if (!this.applet)
            this.lsm.setEnableSource(true); 
        } else if ((Model)sender == getTarget().getModel()) {
          Polygon tmp = getTarget().getModel().getPolygon();
          if (getFinish() != tmp)
            this.changed = true; 
          setFinish(new Polygon(tmp));
          if (!this.applet)
            this.lsm.setEnableTarget(true); 
        } 
      } else if (arg.equals("cleared")) {
        this.changed = true;
        if ((Model)sender == getOriginal().getModel()) {
          setStart(null);
        } else if ((Model)sender == getTarget().getModel()) {
          setFinish(null);
        } 
      } else if ((Model)sender == getOriginal().getModel()) {
        this.changed = true;
        if (!this.applet)
          this.lsm.setEnableSource(false); 
        this.calculator.setEnabled(false);
      } else if ((Model)sender == getTarget().getModel()) {
        this.changed = true;
        if (!this.applet)
          this.lsm.setEnableTarget(false); 
        this.calculator.setEnabled(false);
      } 
      if (getFinish() != null && getStart() != null) {
        this.calculator.setEnabled(true);
      } else {
        this.calculator.setEnabled(false);
      } 
    } 
    checkChanged();
  }
  
  private class CalcListener implements ActionListener {
    final ExtendedController this$0;
    
    CalcListener(ExtendedController this$0) {
      this.this$0 = this$0;
    }
    
    public void actionPerformed(ActionEvent e) {}
  }
  
  protected class CalculateMorphButtonListener implements ActionListener {
    final ExtendedController this$0;
    
    protected CalculateMorphButtonListener(ExtendedController this$0) {
      this.this$0 = this$0;
    }
    
    public void actionPerformed(ActionEvent arg0) {
      if (this.this$0.changed) {
        Polygon source = new Polygon(this.this$0.getStart());
        Polygon target = new Polygon(this.this$0.getFinish());
        source.changeSize(2.0D);
        target.changeSize(2.0D);
        if (!this.this$0.featureDetection) {
          source.setAllVerticesToFeaturePoints();
          target.setAllVerticesToFeaturePoints();
        } else {
          this.this$0.fpd.setMax_angle(this.this$0.max_angle);
          this.this$0.fpd.setMax_featurePoints(this.this$0.max_featurePoints);
          this.this$0.fpd.setMin_length(this.this$0.min_length);
          if (!this.this$0.isLimit) {
            source = this.this$0.fpd.featureDetection(source);
            target = this.this$0.fpd.featureDetection(target);
          } else {
            this.this$0.fpd.setMax_angle(179.0D);
            this.this$0.fpd.setMin_length(0.01D);
            source = this.this$0.fpd.featureDetection(source);
            this.this$0.fpd.filterMostProminent(source);
            target = this.this$0.fpd.featureDetection(target);
            this.this$0.fpd.filterMostProminent(target);
          } 
        } 
        source.preparePolygon(this.this$0.getSample_rate(), this.this$0.getROS_size(), this.this$0.isSimple_ROS());
        target.preparePolygon(this.this$0.getSample_rate(), this.this$0.getROS_size(), this.this$0.isSimple_ROS());
        Path path = MorphCalculator.calculatePath(source, target, this.this$0.getSkips());
        Path finalpath = path.getFinalPath();
        Polygon[] morphablePolygons = MorphCalculator.createCompleteMorph2(finalpath, path, source, target);
        source = morphablePolygons[0];
        target = morphablePolygons[1];
        this.this$0.svgStart = source;
        this.this$0.svgEnd = target;
        if (!this.this$0.frames) {
          this.this$0.setMorphs(Animator.animate(source, target, this.this$0.steps));
          this.this$0.ad = new AnimationDisplay(this.this$0.getMorphs(), this.this$0.getModel(), this.this$0);
        } else {
          this.this$0.setMorphs(Animator.animate(source, target, this.this$0.fps, this.this$0.time));
          this.this$0.ad = new AnimationDisplay(this.this$0.getMorphs(), this.this$0.getModel(), this.this$0, this.this$0.fps);
        } 
        this.this$0.getModel().reset();
        this.this$0.getModel().append(source);
        this.this$0.changed = false;
        this.this$0.checkChanged();
        this.this$0.animationSlider.setValue(0);
        this.this$0.animationSlider.setMaximum((this.this$0.getMorphs()).length);
        this.this$0.animationSlider.setEnabled(true);
      } 
    }
  }
  
  protected class AnimationButtonListener implements ActionListener {
    final ExtendedController this$0;
    
    protected AnimationButtonListener(ExtendedController this$0) {
      this.this$0 = this$0;
    }
    
    public void actionPerformed(ActionEvent arg0) {
      this.this$0.animationSlider.setEnabled(false);
      this.this$0.ad.start();
    }
  }
  
  public void stopAnimator() {
    this.ad = null;
    if (!this.frames) {
      this.ad = new AnimationDisplay(getMorphs(), getModel(), this);
    } else {
      this.ad = new AnimationDisplay(getMorphs(), getModel(), this, this.fps);
    } 
    this.animationSlider.setEnabled(true);
  }
  
  private void checkChanged() {
    if (!this.weightSumOk) {
      this.animator.setEnabled(false);
      this.calculator.setEnabled(false);
      if (!this.applet)
        this.lsm.setEnableMorph(false); 
      this.animationSlider.setEnabled(false);
    } else {
      if (getStart() != null && getFinish() != null)
        this.calculator.setEnabled(true); 
      if (this.changed) {
        this.animator.setEnabled(false);
        if (!this.applet)
          this.lsm.setEnableMorph(false); 
        this.animationSlider.setEnabled(false);
      } else {
        this.animator.setEnabled(true);
        if (!this.applet)
          this.lsm.setEnableMorph(true); 
      } 
    } 
  }
  
  public void setSkips(int skips) {
    if (getSkips() != skips)
      this.changed = true; 
    super.setSkips(skips);
    System.out.println("skips = " + getSkips());
    checkChanged();
  }
  
  public void setSample_rate(int sample_rate) {
    if (getSample_rate() != sample_rate)
      this.changed = true; 
    super.setSample_rate(sample_rate);
    System.out.println("sample rate = " + getSample_rate());
    checkChanged();
  }
  
  public void setROS_size(int ros_size) {
    if (getROS_size() != ros_size)
      this.changed = true; 
    super.setROS_size(ros_size);
    System.out.println("ROS = " + getROS_size());
    checkChanged();
  }
  
  public void setFeature_var_denom(int feature_var_denom) {
    this.feature_var_denom = feature_var_denom;
    checkWeights();
  }
  
  public int getFeature_var_denom() {
    return this.feature_var_denom;
  }
  
  public void setFeature_side_denom(int feature_side_denom) {
    this.feature_side_denom = feature_side_denom;
    checkWeights();
  }
  
  public int getFeature_side_denom() {
    return this.feature_side_denom;
  }
  
  public void setFeature_size_denom(int feature_size_denom) {
    this.feature_size_denom = feature_size_denom;
    checkWeights();
  }
  
  public int getFeature_size_denom() {
    return this.feature_size_denom;
  }
  
  private void checkWeights() {
    long total_denominator = (this.feature_side_denom * this.feature_size_denom * this.feature_var_denom);
    long numerator_var = (this.feature_side_denom * this.feature_size_denom);
    long numerator_side = (this.feature_size_denom * this.feature_var_denom);
    long numerator_size = (this.feature_side_denom * this.feature_var_denom);
    if (numerator_side + numerator_size + numerator_var == total_denominator) {
      this.weightSumOk = true;
    } else {
      this.weightSumOk = false;
    } 
    this.changed = true;
    checkChanged();
  }
  
  public void setMax_angle(double max_angle) {
    this.max_angle = max_angle;
    this.fd_changed = true;
    this.fpd.setMax_angle(max_angle);
    this.changed = true;
    checkChanged();
  }
  
  public double getMax_angle() {
    return this.max_angle;
  }
  
  public void setMin_length(double min_length) {
    this.min_length = min_length;
    this.fd_changed = true;
    this.fpd.setMin_length(min_length);
    this.changed = true;
    checkChanged();
  }
  
  public double getMin_length() {
    return this.min_length;
  }
  
  public void setMax_featurePoints(int max_featurePoints) {
    this.max_featurePoints = max_featurePoints;
    this.fd_changed = true;
    this.fpd.setMax_featurePoints(max_featurePoints);
    this.changed = true;
    checkChanged();
  }
  
  public int getMax_featurePoints() {
    return this.max_featurePoints;
  }
  
  public void setFeatureDetection(boolean featureDetection) {
    if (this.featureDetection != featureDetection) {
      this.featureDetection = featureDetection;
      this.changed = true;
    } 
    checkChanged();
  }
  
  public boolean isFeatureDetection() {
    return this.featureDetection;
  }
  
  public void setLimit(boolean isLimit) {
    this.isLimit = isLimit;
    this.changed = true;
    checkChanged();
  }
  
  public boolean isLimit() {
    return this.isLimit;
  }
  
  public void setSimple_ROS(boolean simple_ROS) {
    if (simple_ROS != isSimple_ROS()) {
      this.changed = true;
      this.simple_ROS = simple_ROS;
    } 
    checkChanged();
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
    buff.append("<animate begin=\"button.click\" dur=\"" + (this.time * 2.0D) + "s\" repeatCount=\"1\" fill=\"freeze\" attributeName=\"d\" ");
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
    buff.append("<text x=\"" + (width + 50) + "\" y=\"" + '(' + "\" style=\"font-size:25;fill:black;text-anchor:middle\">Start</text>");
    buff.append("<rect id=\"button\" x=\"" + width + "\" y=\"" + '\n' + "\" width=\"100\" height=\"50\" style=\"fill;black;fill-opacity:0.1\"/>");
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
}
