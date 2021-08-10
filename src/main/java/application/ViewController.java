/*     */ package application;
/*     */ 
/*     */ import controls.CGSliderListener;
/*     */ import controls.ClearButtonListener;
/*     */ import controls.Resetable;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.Observable;
/*     */ import java.util.Observer;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.event.ChangeEvent;
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
/*     */ public class ViewController
/*     */   extends JPanel
/*     */   implements Observer, Resetable
/*     */ {
/*     */   private View view;
/*     */   private Model model;
/*     */   private PolygonListener listener;
/*  41 */   private int region = 10;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ViewController(boolean small) {
/*  51 */     if (small) {
/*  52 */       setView(new View(true));
/*     */     } else {
/*  54 */       setView(new View());
/*  55 */     }  setModel(new Model());
/*  56 */     registerWithModel();
/*  57 */     getModel().setToView(getView());
/*  58 */     setLayout(new BorderLayout());
/*  59 */     add(getView(), "Center");
/*  60 */     this.listener = new PolygonListener(this.model, this.region);
/*  61 */     this.view.addMouseListener(this.listener);
/*  62 */     this.view.addMouseMotionListener(this.listener);
/*  63 */     add(createComponents(), "South");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Component createComponents() {
/*  73 */     JPanel panel = new JPanel();
/*  74 */     CGSlider zoom = new CGSlider("Zoom", 1, 10, 1, new ZoomFactorListener(getModel()));
/*  75 */     panel.add(zoom);
/*  76 */     JButton clear = new JButton("Clear");
/*  77 */     clear.setSize(new Dimension(80, 25));
/*  78 */     ClearButtonListener cbl = new ClearButtonListener();
/*  79 */     cbl.append(this.model);
/*  80 */     cbl.append(zoom);
/*  81 */     clear.addActionListener((ActionListener)cbl);
/*  82 */     panel.add(clear, "South");
/*  83 */     return panel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setView(View view) {
/*  93 */     this.view = view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public View getView() {
/* 100 */     return this.view;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModel(Model model) {
/* 108 */     this.model = model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model getModel() {
/* 116 */     return this.model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerWithModel() {
/* 125 */     getModel().addObserver(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Observable sender, Object arg1) {
/* 132 */     getView().display(((Model)sender).getComponent());
/*     */   }
/*     */ 
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
/*     */   
/*     */   protected static class ZoomFactorListener
/*     */     extends CGSliderListener
/*     */   {
/*     */     private Model model;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ZoomFactorListener(Model model) {
/* 161 */       this.model = model;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void stateChanged(ChangeEvent e) {
/* 170 */       this.model.setFactor(((JSlider)e.getSource()).getValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\ViewController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */