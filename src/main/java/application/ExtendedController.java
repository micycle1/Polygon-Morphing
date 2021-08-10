/*     */ package application;
/*     */ 
/*     */ import controls.AnimationSliderListener;
/*     */ import controls.CGSliderListener;
/*     */ import controls.ClearButtonListener;
/*     */ import featurePointDetection.FeaturePointDetector;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.Observable;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import morph.AnimationDisplay;
/*     */ import morph.Animator;
/*     */ import morph.MorphCalculator;
/*     */ import shapes.Polygon;
/*     */ import tools.Constants;
/*     */ import tools.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedController
/*     */   extends Controller
/*     */ {
/*     */   private int steps;
/*     */   private int fps;
/*     */   private double time;
/*     */   private JButton animator;
/*     */   private JButton calculator;
/*     */   private boolean frames;
/*     */   private AnimationDisplay ad;
/*     */   private LoadSaveMenu lsm;
/*     */   private ComboBoxMenu cbm;
/*     */   private int feature_var_denom;
/*     */   private int feature_side_denom;
/*     */   private int feature_size_denom;
/*     */   private boolean weightSumOk;
/*     */   private double max_angle;
/*     */   private double min_length;
/*     */   private int max_featurePoints;
/*     */   private boolean featureDetection;
/*     */   private boolean isLimit;
/*     */   private boolean fd_changed;
/*     */   private FeaturePointDetector fpd;
/*     */   private Polygon svgStart;
/*     */   private Polygon svgEnd;
/*     */   static Class class$0;
/*     */   static Class class$1;
/*     */   
/*     */   public ExtendedController(boolean applet) {
/*  79 */     super(applet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Component createComponents() {
/*  88 */     this.fpd = new FeaturePointDetector();
/*     */     
/*  90 */     this.steps = 50;
/*  91 */     this.fps = 15;
/*  92 */     this.time = 5.0D;
/*  93 */     this.feature_var_denom = Constants.DENOMINATORS[0];
/*  94 */     this.feature_side_denom = Constants.DENOMINATORS[1];
/*  95 */     this.feature_size_denom = Constants.DENOMINATORS[2];
/*  96 */     this.weightSumOk = true;
/*  97 */     this.max_angle = 130.0D;
/*  98 */     this.min_length = 0.01D;
/*  99 */     this.max_featurePoints = 35;
/* 100 */     this.featureDetection = false;
/* 101 */     this.simple_ROS = true;
/* 102 */     this.isLimit = false;
/* 103 */     this.fd_changed = false;
/*     */ 
/*     */     
/* 106 */     JPanel panel = new JPanel(new BorderLayout());
/* 107 */     JPanel tmp = new JPanel();
/* 108 */     JPanel tmp2 = new JPanel(new BorderLayout());
/* 109 */     JPanel menu = new JPanel(new BorderLayout());
/* 110 */     tmp2.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
/* 111 */     tmp2.add(getView(), "Center");
/*     */     
/* 113 */     tmp = new JPanel();
/* 114 */     tmp.add(getOriginal(), "West");
/* 115 */     tmp.add(getTarget(), "East");
/* 116 */     tmp2.add(tmp, "North");
/*     */ 
/*     */     
/* 119 */     menu = createMenu();
/* 120 */     panel.add(tmp2, "Center");
/* 121 */     panel.add(menu, "East");
/* 122 */     this.calculator.setEnabled(false);
/* 123 */     this.changed = true;
/* 124 */     this.frames = false;
/* 125 */     checkChanged();
/* 126 */     return panel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JPanel createMenu() {
/* 133 */     JPanel menu = new JPanel();
/* 134 */     JPanel tmp = new JPanel();
/* 135 */     JPanel tmp2 = new JPanel();
/*     */     
/* 137 */     GridBagLayout gbl = new GridBagLayout();
/* 138 */     GridBagConstraints c = new GridBagConstraints();
/* 139 */     c.gridwidth = 6;
/* 140 */     c.weightx = 1.0D;
/* 141 */     c.gridheight = 6;
/* 142 */     c.fill = 2;
/* 143 */     c.insets = new Insets(1, 1, 1, 1);
/* 144 */     c.gridx = 0;
/* 145 */     c.gridy = 0;
/*     */     
/* 147 */     JButton calcMorph = new JButton("Calculate Morph");
/* 148 */     this.calculator = calcMorph;
/* 149 */     this.calculator.addActionListener(new CalcListener(this));
/* 150 */     calcMorph.setSize(150, 25);
/* 151 */     CalculateMorphButtonListener cmbl = new CalculateMorphButtonListener(this);
/* 152 */     calcMorph.addActionListener(cmbl);
/* 153 */     JButton animate = new JButton("Animate!");
/* 154 */     this.animator = animate;
/* 155 */     animate.setSize(120, 25);
/* 156 */     AnimationButtonListener abl = new AnimationButtonListener(this);
/* 157 */     animate.addActionListener(abl);
/*     */ 
/*     */     
/* 160 */     tmp.add(calcMorph);
/*     */     
/* 162 */     tmp.add(animate);
/* 163 */     JPanel animationMenu = new JPanel();
/* 164 */     animationMenu.setLayout(new BorderLayout());
/* 165 */     animationMenu.add(tmp, "North");
/*     */ 
/*     */     
/* 168 */     JPanel menus = new JPanel();
/*     */ 
/*     */     
/* 171 */     RadioButtonPanel rbp = new RadioButtonPanel(this.steps, this.fps, this.time);
/* 172 */     animationMenu.add(rbp.getPanel(), "Center");
/* 173 */     animationMenu.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Animation Menu"));
/* 174 */     ParameterPanel pp = new ParameterPanel(this);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     this.animationSlider = new CGSlider("Animation Step", 1, 1, 1, (CGSliderListener)new AnimationSliderListener(this));
/* 180 */     animationMenu.add(this.animationSlider, "South");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     menus.setLayout(gbl);
/* 187 */     Constants.addIntoGrid(menus, animationMenu, gbl, c);
/* 188 */     c.gridy = 6;
/* 189 */     if (!this.applet) {
/* 190 */       this.lsm = new LoadSaveMenu(this);
/* 191 */       Constants.addIntoGrid(menus, this.lsm, gbl, c);
/*     */     } else {
/*     */       
/* 194 */       this.cbm = new ComboBoxMenu(this);
/* 195 */       Constants.addIntoGrid(menus, this.cbm, gbl, c);
/*     */     } 
/* 197 */     c.gridy = 12;
/* 198 */     Constants.addIntoGrid(menus, pp, gbl, c);
/* 199 */     c.gridy = 18;
/* 200 */     Constants.addIntoGrid(menus, new FeatureDetectionMenu(this), gbl, c);
/* 201 */     menus.setBorder(BorderFactory.createLineBorder(Color.BLACK));
/*     */ 
/*     */     
/* 204 */     rbp.addObserver(this);
/*     */     
/* 206 */     CGSlider zoom = new CGSlider("Zoomfaktor", 1, 10, 1, new Controller.ZoomFactorListener(this, getModel()));
/*     */     
/* 208 */     JButton clear = new JButton("Clear");
/* 209 */     clear.setSize(new Dimension(80, 25));
/* 210 */     ClearButtonListener cbl = new ClearButtonListener();
/* 211 */     cbl.append(getModel());
/* 212 */     cbl.append(zoom);
/* 213 */     clear.addActionListener((ActionListener)cbl);
/*     */     
/* 215 */     gbl = new GridBagLayout();
/* 216 */     c.gridwidth = 3;
/* 217 */     c.gridx = 0;
/* 218 */     c.gridy = 0;
/* 219 */     tmp2.setLayout(gbl);
/* 220 */     Constants.addIntoGrid(tmp2, zoom, gbl, c);
/*     */ 
/*     */     
/* 223 */     c.gridx = 3;
/* 224 */     Constants.addIntoGrid(tmp2, clear, gbl, c);
/* 225 */     tmp2.setBorder(Constants.BLACKLINE);
/*     */ 
/*     */ 
/*     */     
/* 229 */     gbl = new GridBagLayout();
/* 230 */     menu.setLayout(gbl);
/* 231 */     c.gridheight = 1;
/* 232 */     c.gridwidth = 1;
/* 233 */     c.gridx = 0;
/* 234 */     c.gridy = 0;
/* 235 */     Constants.addIntoGrid(menu, menus, gbl, c);
/* 236 */     c.gridx = 0;
/* 237 */     c.gridy = 1;
/* 238 */     c.weighty = 1.0D;
/* 239 */     c.anchor = 20;
/* 240 */     c.insets = new Insets(10, 0, 0, 0);
/*     */     
/* 242 */     Constants.addIntoGrid(menu, tmp2, gbl, c);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     return menu;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Observable sender, Object arg) {
/* 256 */     if (class$0 == null) try {  } catch (ClassNotFoundException classNotFoundException) { throw new NoClassDefFoundError(null.getMessage()); }   if (class$0.equals(class$0 = Class.forName("application.RadioButtonPanel")))
/*     */     {
/*     */       
/* 259 */       if (arg.equals("steps")) {
/* 260 */         int tmp = ((RadioButtonPanel)sender).getSteps();
/* 261 */         if (this.steps != tmp) {
/* 262 */           this.changed = true;
/* 263 */           this.steps = tmp;
/* 264 */           System.out.println(this.steps);
/*     */         }
/*     */       
/* 267 */       } else if (arg.equals("fps")) {
/* 268 */         int tmp = ((RadioButtonPanel)sender).getFrames();
/* 269 */         if (this.fps != tmp) {
/* 270 */           this.changed = true;
/* 271 */           this.fps = tmp;
/* 272 */           System.out.println(this.fps);
/*     */         }
/*     */       
/* 275 */       } else if (arg.equals("time")) {
/* 276 */         double tmp = ((RadioButtonPanel)sender).getTime();
/* 277 */         if (this.time != tmp) {
/* 278 */           this.changed = true;
/* 279 */           this.time = tmp;
/* 280 */           System.out.println(this.time);
/*     */         }
/*     */       
/*     */       }
/* 284 */       else if (arg.equals("RadioSteps")) {
/* 285 */         this.changed = true;
/* 286 */         this.frames = false;
/*     */       }
/* 288 */       else if (arg.equals("RadioFrames")) {
/* 289 */         this.changed = true;
/* 290 */         this.frames = true;
/*     */       }
/*     */       else {
/*     */         
/* 294 */         throw new IllegalArgumentException("Illegal argument: " + arg.toString() + 
/* 295 */             " for notify change.");
/*     */       }  } 
/* 297 */     if (class$1 == null) try {  } catch (ClassNotFoundException classNotFoundException) { throw new NoClassDefFoundError(null.getMessage()); }   if (class$1.equals(class$1 = Class.forName("application.Model"))) {
/* 298 */       super.update(sender, arg);
/* 299 */       if (arg.equals("M_append")) {
/* 300 */         if ((Model)sender == getOriginal().getModel()) {
/*     */           
/* 302 */           Polygon tmp = getOriginal().getModel().getPolygon();
/* 303 */           if (getStart() != tmp) {
/* 304 */             this.changed = true;
/*     */           }
/* 306 */           setStart(new Polygon(tmp));
/* 307 */           if (!this.applet) {
/* 308 */             this.lsm.setEnableSource(true);
/*     */           }
/* 310 */         } else if ((Model)sender == getTarget().getModel()) {
/*     */           
/* 312 */           Polygon tmp = getTarget().getModel().getPolygon();
/* 313 */           if (getFinish() != tmp)
/* 314 */             this.changed = true; 
/* 315 */           setFinish(new Polygon(tmp));
/* 316 */           if (!this.applet) {
/* 317 */             this.lsm.setEnableTarget(true);
/*     */           }
/*     */         } 
/* 320 */       } else if (arg.equals("cleared")) {
/* 321 */         this.changed = true;
/* 322 */         if ((Model)sender == getOriginal().getModel()) {
/* 323 */           setStart(null);
/* 324 */         } else if ((Model)sender == getTarget().getModel()) {
/* 325 */           setFinish(null);
/*     */         }
/*     */       
/* 328 */       } else if ((Model)sender == getOriginal().getModel()) {
/*     */         
/* 330 */         this.changed = true;
/* 331 */         if (!this.applet) {
/* 332 */           this.lsm.setEnableSource(false);
/*     */         }
/* 334 */         this.calculator.setEnabled(false);
/*     */       }
/* 336 */       else if ((Model)sender == getTarget().getModel()) {
/* 337 */         this.changed = true;
/* 338 */         if (!this.applet) {
/* 339 */           this.lsm.setEnableTarget(false);
/*     */         }
/* 341 */         this.calculator.setEnabled(false);
/*     */       } 
/*     */       
/* 344 */       if (getFinish() != null && getStart() != null) {
/* 345 */         this.calculator.setEnabled(true);
/*     */       } else {
/* 347 */         this.calculator.setEnabled(false);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     checkChanged();
/*     */   } private class CalcListener implements ActionListener { final ExtendedController this$0;
/*     */     CalcListener(ExtendedController this$0) {
/* 357 */       this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent e) {} }
/*     */   
/*     */   protected class CalculateMorphButtonListener implements ActionListener { final ExtendedController this$0;
/*     */     
/*     */     protected CalculateMorphButtonListener(ExtendedController this$0) {
/* 364 */       this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent arg0) {
/* 367 */       if (this.this$0.changed) {
/* 368 */         Polygon source = new Polygon(this.this$0.getStart());
/* 369 */         Polygon target = new Polygon(this.this$0.getFinish());
/* 370 */         source.changeSize(2.0D);
/* 371 */         target.changeSize(2.0D);
/*     */         
/* 373 */         if (!this.this$0.featureDetection) {
/* 374 */           source.setAllVerticesToFeaturePoints();
/* 375 */           target.setAllVerticesToFeaturePoints();
/*     */         } else {
/*     */           
/* 378 */           this.this$0.fpd.setMax_angle(this.this$0.max_angle);
/* 379 */           this.this$0.fpd.setMax_featurePoints(this.this$0.max_featurePoints);
/* 380 */           this.this$0.fpd.setMin_length(this.this$0.min_length);
/* 381 */           if (!this.this$0.isLimit) {
/* 382 */             source = this.this$0.fpd.featureDetection(source);
/* 383 */             target = this.this$0.fpd.featureDetection(target);
/*     */           } else {
/*     */             
/* 386 */             this.this$0.fpd.setMax_angle(179.0D);
/* 387 */             this.this$0.fpd.setMin_length(0.01D);
/* 388 */             source = this.this$0.fpd.featureDetection(source);
/* 389 */             this.this$0.fpd.filterMostProminent(source);
/* 390 */             target = this.this$0.fpd.featureDetection(target);
/* 391 */             this.this$0.fpd.filterMostProminent(target);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 396 */         source.preparePolygon(this.this$0.getSample_rate(), this.this$0.getROS_size(), this.this$0.isSimple_ROS());
/*     */         
/* 398 */         target.preparePolygon(this.this$0.getSample_rate(), this.this$0.getROS_size(), this.this$0.isSimple_ROS());
/*     */         
/* 400 */         Path path = MorphCalculator.calculatePath(source, target, this.this$0.getSkips());
/* 401 */         Path finalpath = path.getFinalPath();
/* 402 */         Polygon[] morphablePolygons = MorphCalculator.createCompleteMorph2(finalpath, path, source, target);
/* 403 */         source = morphablePolygons[0];
/* 404 */         target = morphablePolygons[1];
/* 405 */         this.this$0.svgStart = source;
/* 406 */         this.this$0.svgEnd = target;
/*     */ 
/*     */ 
/*     */         
/* 410 */         if (!this.this$0.frames) {
/* 411 */           this.this$0.setMorphs(Animator.animate(source, target, this.this$0.steps));
/* 412 */           this.this$0.ad = new AnimationDisplay(this.this$0.getMorphs(), this.this$0.getModel(), this.this$0);
/*     */         } else {
/*     */           
/* 415 */           this.this$0.setMorphs(Animator.animate(source, target, this.this$0.fps, this.this$0.time));
/* 416 */           this.this$0.ad = new AnimationDisplay(this.this$0.getMorphs(), this.this$0.getModel(), this.this$0, this.this$0.fps);
/*     */         } 
/* 418 */         this.this$0.getModel().reset();
/* 419 */         this.this$0.getModel().append(source);
/* 420 */         this.this$0.changed = false;
/* 421 */         this.this$0.checkChanged();
/* 422 */         this.this$0.animationSlider.setValue(0);
/* 423 */         this.this$0.animationSlider.setMaximum((this.this$0.getMorphs()).length);
/* 424 */         this.this$0.animationSlider.setEnabled(true);
/*     */       } 
/*     */     } }
/*     */   protected class AnimationButtonListener implements ActionListener { final ExtendedController this$0;
/*     */     protected AnimationButtonListener(ExtendedController this$0) {
/* 429 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent arg0) {
/* 437 */       this.this$0.animationSlider.setEnabled(false);
/*     */       
/* 439 */       this.this$0.ad.start();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopAnimator() {
/* 452 */     this.ad = null;
/* 453 */     if (!this.frames) {
/* 454 */       this.ad = new AnimationDisplay(getMorphs(), getModel(), this);
/*     */     } else {
/* 456 */       this.ad = new AnimationDisplay(getMorphs(), getModel(), this, this.fps);
/* 457 */     }  this.animationSlider.setEnabled(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkChanged() {
/* 464 */     if (!this.weightSumOk) {
/* 465 */       this.animator.setEnabled(false);
/* 466 */       this.calculator.setEnabled(false);
/* 467 */       if (!this.applet)
/* 468 */         this.lsm.setEnableMorph(false); 
/* 469 */       this.animationSlider.setEnabled(false);
/*     */     } else {
/*     */       
/* 472 */       if (getStart() != null && getFinish() != null)
/* 473 */         this.calculator.setEnabled(true); 
/* 474 */       if (this.changed) {
/* 475 */         this.animator.setEnabled(false);
/* 476 */         if (!this.applet)
/* 477 */           this.lsm.setEnableMorph(false); 
/* 478 */         this.animationSlider.setEnabled(false);
/*     */       } else {
/*     */         
/* 481 */         this.animator.setEnabled(true);
/* 482 */         if (!this.applet) {
/* 483 */           this.lsm.setEnableMorph(true);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkips(int skips) {
/* 493 */     if (getSkips() != skips) {
/* 494 */       this.changed = true;
/*     */     }
/* 496 */     super.setSkips(skips);
/* 497 */     System.out.println("skips = " + getSkips());
/* 498 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSample_rate(int sample_rate) {
/* 506 */     if (getSample_rate() != sample_rate)
/* 507 */       this.changed = true; 
/* 508 */     super.setSample_rate(sample_rate);
/* 509 */     System.out.println("sample rate = " + getSample_rate());
/* 510 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setROS_size(int ros_size) {
/* 518 */     if (getROS_size() != ros_size)
/* 519 */       this.changed = true; 
/* 520 */     super.setROS_size(ros_size);
/* 521 */     System.out.println("ROS = " + getROS_size());
/* 522 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature_var_denom(int feature_var_denom) {
/* 530 */     this.feature_var_denom = feature_var_denom;
/* 531 */     checkWeights();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFeature_var_denom() {
/* 539 */     return this.feature_var_denom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature_side_denom(int feature_side_denom) {
/* 547 */     this.feature_side_denom = feature_side_denom;
/* 548 */     checkWeights();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFeature_side_denom() {
/* 556 */     return this.feature_side_denom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature_size_denom(int feature_size_denom) {
/* 564 */     this.feature_size_denom = feature_size_denom;
/* 565 */     checkWeights();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFeature_size_denom() {
/* 573 */     return this.feature_size_denom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkWeights() {
/* 580 */     long total_denominator = (this.feature_side_denom * this.feature_size_denom * this.feature_var_denom);
/* 581 */     long numerator_var = (this.feature_side_denom * this.feature_size_denom);
/* 582 */     long numerator_side = (this.feature_size_denom * this.feature_var_denom);
/* 583 */     long numerator_size = (this.feature_side_denom * this.feature_var_denom);
/* 584 */     if (numerator_side + numerator_size + numerator_var == total_denominator) {
/* 585 */       this.weightSumOk = true;
/*     */     } else {
/* 587 */       this.weightSumOk = false;
/* 588 */     }  this.changed = true;
/* 589 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMax_angle(double max_angle) {
/* 597 */     this.max_angle = max_angle;
/* 598 */     this.fd_changed = true;
/* 599 */     this.fpd.setMax_angle(max_angle);
/* 600 */     this.changed = true;
/* 601 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMax_angle() {
/* 609 */     return this.max_angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMin_length(double min_length) {
/* 617 */     this.min_length = min_length;
/* 618 */     this.fd_changed = true;
/* 619 */     this.fpd.setMin_length(min_length);
/* 620 */     this.changed = true;
/* 621 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMin_length() {
/* 629 */     return this.min_length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMax_featurePoints(int max_featurePoints) {
/* 637 */     this.max_featurePoints = max_featurePoints;
/* 638 */     this.fd_changed = true;
/* 639 */     this.fpd.setMax_featurePoints(max_featurePoints);
/* 640 */     this.changed = true;
/* 641 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMax_featurePoints() {
/* 649 */     return this.max_featurePoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeatureDetection(boolean featureDetection) {
/* 657 */     if (this.featureDetection != featureDetection) {
/* 658 */       this.featureDetection = featureDetection;
/* 659 */       this.changed = true;
/*     */     } 
/* 661 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFeatureDetection() {
/* 669 */     return this.featureDetection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLimit(boolean isLimit) {
/* 677 */     this.isLimit = isLimit;
/* 678 */     this.changed = true;
/* 679 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLimit() {
/* 687 */     return this.isLimit;
/*     */   }
/*     */   
/*     */   public void setSimple_ROS(boolean simple_ROS) {
/* 691 */     if (simple_ROS != isSimple_ROS()) {
/* 692 */       this.changed = true;
/* 693 */       this.simple_ROS = simple_ROS;
/*     */     } 
/* 695 */     checkChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSVG() {
/* 703 */     int width = 640;
/* 704 */     int height = 480;
/* 705 */     StringBuffer buff = new StringBuffer();
/* 706 */     buff.append("<?xml version=\"1.0\" standalone=\"no\"?>\n");
/* 707 */     buff.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");
/* 708 */     buff.append("<svg width=\"" + (width + 110) + "\" height=\"" + height + "\" viewBox=\"0 0 " + (width + 100) + " " + height + "\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n");
/* 709 */     buff.append("\t<desc>SVG-representation of the polygon Morphing</desc>\n");
/*     */     
/* 711 */     buff.append(this.svgStart.toSVG());
/* 712 */     buff.deleteCharAt(buff.length() - 3);
/* 713 */     buff.append("<animate begin=\"button.click\" dur=\"" + (this.time * 2.0D) + "s\" repeatCount=\"1\" fill=\"freeze\" attributeName=\"d\" ");
/* 714 */     buff.append("values=");
/* 715 */     StringBuffer tmp = new StringBuffer();
/* 716 */     tmp.append(this.svgStart.toSVGPath());
/* 717 */     tmp.delete(0, 6);
/* 718 */     tmp.deleteCharAt(tmp.length() - 2);
/* 719 */     buff.append(tmp);
/* 720 */     buff.append(";\n");
/* 721 */     tmp = new StringBuffer();
/* 722 */     tmp.append(this.svgEnd.toSVGPath());
/* 723 */     tmp.delete(4, 7);
/* 724 */     tmp.deleteCharAt(tmp.length() - 2);
/* 725 */     buff.append(tmp);
/* 726 */     buff.append(";\n");
/* 727 */     tmp = new StringBuffer();
/* 728 */     tmp.append(this.svgStart.toSVGPath());
/* 729 */     tmp.delete(4, 7);
/* 730 */     buff.append(tmp);
/* 731 */     buff.append("/> \n");
/* 732 */     buff.append("</path>\n");
/*     */     
/* 734 */     buff.append("<text x=\"" + (width + 50) + "\" y=\"" + '(' + "\" style=\"font-size:25;fill:black;text-anchor:middle\">Start</text>");
/* 735 */     buff.append("<rect id=\"button\" x=\"" + width + "\" y=\"" + '\n' + "\" width=\"100\" height=\"50\" style=\"fill;black;fill-opacity:0.1\"/>");
/*     */ 
/*     */     
/* 738 */     buff.append("\t\t<path id=\"bla\" d=\"M 640 495 L -320 495\" fill=\"none\" stroke=\"none\" stroke-width=\"0.5\" />\n");
/* 739 */     buff.append("\t\t<text x=\"0\" y=\"0\" \n\t\t\t font-family=\"Verdana\" font-size=\"10\" fill=\"green\" >\n");
/* 740 */     buff.append("\t\t\t\t <tspan font-weight=\"bold\" font-style=\"italic\" fill=\"black\"> really crappy </tspan>\n");
/* 741 */     buff.append("\t\t\t  SVG-implementation by Sven Albrecht in fall 2006\n");
/* 742 */     buff.append("\t\t\t\t <animateMotion dur=\"10s\" repeatCount=\"indefinite\" >\n");
/* 743 */     buff.append("\t\t\t\t\t <mpath xlink:href=\"#bla\"/>\n");
/* 744 */     buff.append("\t\t\t\t </animateMotion> \n");
/* 745 */     buff.append("\t\t</text>\n");
/* 746 */     buff.append("</svg>");
/*     */ 
/*     */ 
/*     */     
/* 750 */     return new String(buff);
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\ExtendedController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */