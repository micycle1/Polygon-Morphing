/*     */ package application;
/*     */ 
/*     */ import controls.CGSliderListener;
/*     */ import controls.ClearButtonListener;
/*     */ import controls.Resetable;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.Observable;
/*     */ import java.util.Observer;
/*     */ import java.util.Vector;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import morph.AnimationDisplay;
/*     */ import morph.Animator;
/*     */ import morph.MorphCalculator;
/*     */ import shapes.Polygon;
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
/*     */ public class Controller
/*     */   extends JPanel
/*     */   implements Observer, Resetable
/*     */ {
/*     */   private View view;
/*     */   private ViewController original;
/*     */   private ViewController target;
/*     */   private Model model;
/*     */   private Vector elv;
/*     */   static final int SEL = 0;
/*     */   private JComboBox choice;
/*     */   protected boolean simple_ROS;
/*     */   protected boolean applet;
/*     */   protected CGSlider animationSlider;
/*  62 */   private Polygon start = null;
/*  63 */   private Polygon finish = null;
/*     */   
/*     */   private Polygon[] morphs;
/*     */   
/*     */   private long seconds;
/*     */   private AnimationDisplay ad;
/*     */   protected boolean changed;
/*     */   static final int WIDTH = 400;
/*     */   static final int HEIGHT = 300;
/*  72 */   private int sample_rate = 5;
/*  73 */   private int ros_size = 10;
/*  74 */   private int skips = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Controller(boolean applet) {
/*  85 */     this.applet = applet;
/*  86 */     setView(new View());
/*  87 */     setOriginal(new ViewController(true));
/*  88 */     setTarget(new ViewController(true));
/*  89 */     setModel(new Model());
/*  90 */     registerWithModel();
/*  91 */     add(createComponents(), "Center");
/*  92 */     getModel().setToView(getView());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Component createComponents() {
/* 101 */     JPanel panel = new JPanel(new BorderLayout());
/* 102 */     JPanel tmp = new JPanel();
/* 103 */     JPanel tmp2 = new JPanel(new BorderLayout());
/* 104 */     JPanel menu = new JPanel(new BorderLayout());
/* 105 */     tmp2.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
/* 106 */     tmp2.add(getView(), "Center");
/*     */     
/* 108 */     tmp = new JPanel();
/* 109 */     tmp.add(getOriginal(), "West");
/* 110 */     tmp.add(getTarget(), "East");
/* 111 */     tmp2.add(tmp, "North");
/*     */ 
/*     */     
/* 114 */     menu = createMenu();
/* 115 */     panel.add(tmp2, "Center");
/* 116 */     panel.add(menu, "East");
/* 117 */     return panel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JPanel createMenu() {
/* 127 */     JPanel menu = new JPanel();
/* 128 */     JPanel tmp = new JPanel();
/* 129 */     JPanel tmp2 = new JPanel();
/* 130 */     menu.setLayout(new BorderLayout());
/* 131 */     JButton calcMorph = new JButton("Calculate Morph");
/* 132 */     calcMorph.setSize(150, 25);
/* 133 */     CalculateMorphButtonListener cmbl = new CalculateMorphButtonListener(this);
/* 134 */     calcMorph.addActionListener(cmbl);
/* 135 */     JButton animate = new JButton("Animate!");
/* 136 */     animate.setSize(120, 25);
/* 137 */     AnimationButtonListener abl = new AnimationButtonListener(this);
/* 138 */     animate.addActionListener(abl);
/* 139 */     tmp.add(calcMorph, "North");
/* 140 */     tmp.add(animate, "South");
/* 141 */     menu.add(tmp, "North");
/* 142 */     CGSlider zoom = new CGSlider("Zoomfaktor", 1, 10, 1, new ZoomFactorListener(this, getModel()));
/* 143 */     tmp2.add(zoom, "North");
/*     */     
/* 145 */     JButton clear = new JButton("Clear");
/* 146 */     clear.setSize(new Dimension(80, 25));
/* 147 */     ClearButtonListener cbl = new ClearButtonListener();
/* 148 */     cbl.append(getModel());
/* 149 */     cbl.append(zoom);
/* 150 */     clear.addActionListener((ActionListener)cbl);
/* 151 */     tmp2.add(clear, "South");
/* 152 */     menu.add(tmp2, "South");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     return menu;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModel(Model model) {
/* 166 */     this.model = model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model getModel() {
/* 173 */     return this.model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setView(View view) {
/* 181 */     this.view = view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public View getView() {
/* 188 */     return this.view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOriginal(ViewController original) {
/* 195 */     this.original = original;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ViewController getOriginal() {
/* 202 */     return this.original;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(ViewController target) {
/* 209 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ViewController getTarget() {
/* 216 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerWithModel() {
/* 225 */     getModel().addObserver(this);
/* 226 */     getOriginal().getModel().addObserver(this);
/* 227 */     getTarget().getModel().addObserver(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Observable sender, Object arg) {
/* 236 */     getView().display(((Model)sender).getComponent());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setStart(Polygon start) {
/* 252 */     this.start = start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Polygon getStart() {
/* 260 */     return this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setFinish(Polygon finish) {
/* 268 */     this.finish = finish;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Polygon getFinish() {
/* 276 */     return this.finish;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSample_rate(int sample_rate) {
/* 284 */     this.sample_rate = sample_rate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSample_rate() {
/* 292 */     return this.sample_rate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setROS_size(int sample_size) {
/* 300 */     this.ros_size = sample_size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getROS_size() {
/* 308 */     return this.ros_size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkips(int skips) {
/* 316 */     this.skips = skips;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSkips() {
/* 324 */     return this.skips;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMorphs(Polygon[] morphs) {
/* 332 */     this.morphs = morphs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Polygon[] getMorphs() {
/* 340 */     return this.morphs;
/*     */   }
/*     */   
/*     */   protected class ZoomFactorListener extends CGSliderListener { private Model model;
/*     */     final Controller this$0;
/*     */     
/*     */     public ZoomFactorListener(Controller this$0, Model model) {
/* 347 */       this.this$0 = this$0;
/* 348 */       this.model = model;
/*     */     }
/*     */     
/*     */     public void stateChanged(ChangeEvent e) {
/* 352 */       this.model.setFactor(((JSlider)e.getSource()).getValue());
/*     */     } }
/*     */   
/*     */   protected class CalculateMorphButtonListener implements ActionListener {
/*     */     protected CalculateMorphButtonListener(Controller this$0) {
/* 357 */       this.this$0 = this$0;
/*     */     }
/*     */     final Controller this$0;
/*     */     public void actionPerformed(ActionEvent event) {
/*     */       Polygon originalPoly, targetPoly;
/*     */       try {
/* 363 */         originalPoly = (Polygon)this.this$0.original.getModel().getPolygon().clone();
/* 364 */         targetPoly = (Polygon)this.this$0.target.getModel().getPolygon().clone();
/* 365 */       } catch (CloneNotSupportedException e) {
/* 366 */         System.err.println(e.toString());
/* 367 */         System.out.println("Error during cloning of Polygons occured. Trying to use originals.");
/* 368 */         originalPoly = this.this$0.original.getModel().getPolygon();
/* 369 */         targetPoly = this.this$0.target.getModel().getPolygon();
/*     */       } 
/* 371 */       originalPoly.preparePolygon(this.this$0.getSample_rate(), this.this$0.getROS_size(), true);
/* 372 */       targetPoly.preparePolygon(this.this$0.getSample_rate(), this.this$0.getROS_size(), true);
/*     */       
/* 374 */       Path optimalPath = MorphCalculator.calculatePath(originalPoly, targetPoly, this.this$0.skips);
/* 375 */       System.out.println(optimalPath.toString());
/* 376 */       Path finalPath = optimalPath.getFinalPath();
/* 377 */       Polygon[] MorphablePolygons = MorphCalculator.createCompleteMorph(finalPath, optimalPath, originalPoly, targetPoly);
/* 378 */       this.this$0.setStart(MorphablePolygons[0]);
/* 379 */       this.this$0.setFinish(MorphablePolygons[1]);
/* 380 */       this.this$0.start.changeSize(2.0D);
/* 381 */       this.this$0.finish.changeSize(2.0D);
/* 382 */       this.this$0.original.getModel().getPolygon().deleteCorrespondences();
/* 383 */       this.this$0.animationSlider.setMaximum((this.this$0.getMorphs()).length);
/* 384 */       this.this$0.setMorphs(Animator.animate(this.this$0.start, this.this$0.finish, 5));
/* 385 */       this.this$0.seconds = 5L;
/* 386 */       this.this$0.ad = new AnimationDisplay(this.this$0.getMorphs(), this.this$0.model, this.this$0);
/* 387 */       this.this$0.model.append(this.this$0.start);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class AnimationButtonListener
/*     */     implements ActionListener
/*     */   {
/*     */     final Controller this$0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected AnimationButtonListener(Controller this$0) {
/* 407 */       this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent arg0) {
/* 411 */       if (this.this$0.getMorphs() != null) {
/* 412 */         this.this$0.ad.start();
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public void stopAnimator() {
/* 476 */     this.ad = null;
/* 477 */     this.ad = new AnimationDisplay(getMorphs(), this.model, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CGSlider getAnimationSlider() {
/* 485 */     return this.animationSlider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSimple_ROS(boolean simple_ROS) {
/* 493 */     if (simple_ROS != this.simple_ROS)
/* 494 */       this.changed = true; 
/* 495 */     this.simple_ROS = simple_ROS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSimple_ROS() {
/* 503 */     return this.simple_ROS;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\Controller.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */