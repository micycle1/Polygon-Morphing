/*     */ package application;
/*     */ 
/*     */ import controls.CGSliderListener;
/*     */ import controls.Resetable;
/*     */ import java.awt.Dimension;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
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
/*     */ public class CGSlider
/*     */   extends JPanel
/*     */   implements ChangeListener, Resetable
/*     */ {
/*     */   private JSlider slider;
/*     */   private JLabel text;
/*     */   private JLabel valueText;
/*     */   private int init;
/*     */   
/*     */   public CGSlider(String text, int min, int max, int init, CGSliderListener sl) {
/*  40 */     this.init = init;
/*  41 */     this.text = new JLabel(text);
/*  42 */     this.text.setPreferredSize(new Dimension(110, 18));
/*  43 */     add(this.text);
/*     */     
/*  45 */     this.valueText = new JLabel(init);
/*  46 */     this.valueText.setPreferredSize(new Dimension(30, 18));
/*  47 */     this.valueText.setHorizontalAlignment(4);
/*  48 */     add(this.valueText);
/*     */     
/*  50 */     this.slider = new JSlider(min, max, init);
/*  51 */     this.slider.addChangeListener((ChangeListener)sl);
/*  52 */     this.slider.addChangeListener(this);
/*  53 */     this.slider.setPreferredSize(new Dimension(100, 18));
/*  54 */     add(this.slider);
/*  55 */     setBorder(BorderFactory.createEtchedBorder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaximum(int n) {
/*  63 */     this.slider.setMaximum(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximum() {
/*  71 */     return this.slider.getMaximum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinimum(int n) {
/*  79 */     this.slider.setMinimum(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinimum() {
/*  87 */     return this.slider.getMinimum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/*  95 */     return this.slider.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int n) {
/* 103 */     this.slider.setValue(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 110 */     this.slider.setValue(this.init);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stateChanged(ChangeEvent e) {
/* 120 */     this.valueText.setText(this.slider.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabled(boolean mode) {
/* 128 */     this.slider.setEnabled(mode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled() {
/* 136 */     return this.slider.isEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSliderFocusAble(boolean focusable) {
/* 144 */     this.slider.setFocusable(focusable);
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\CGSlider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */