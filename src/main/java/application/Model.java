/*     */ package application;
/*     */ 
/*     */ import controls.Resetable;
/*     */ import java.util.Observable;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JComponent;
/*     */ import shapes.GraphicObject;
/*     */ import shapes.Polygon;
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
/*     */ public class Model
/*     */   extends Observable
/*     */   implements Resetable
/*     */ {
/*     */   private Vector v;
/*     */   private Object o;
/*     */   private int factor;
/*     */   private int width;
/*     */   private int height;
/*     */   
/*     */   public Model() {
/*  37 */     setFactor(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  46 */     this.o = null;
/*  47 */     setFactor(1);
/*  48 */     notifyChanged("cleared");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFactor(int factor) {
/*  56 */     this.factor = factor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     if (this.o != null) {
/*  65 */       ((GraphicObject)this.o).setFactor(factor);
/*     */     }
/*  67 */     notifyChanged("M_factor");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFactor() {
/*  75 */     return this.factor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(Object o) {
/*  84 */     this.o = o;
/*  85 */     notifyChanged("M_append");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notifyChanged(Object arg) {
/*  95 */     setChanged();
/*  96 */     notifyObservers(arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return this.o.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToView(View view) {
/* 113 */     if (view.isSmall()) {
/* 114 */       this.width = 320;
/* 115 */       this.height = 240;
/*     */     } else {
/*     */       
/* 118 */       this.width = 640;
/* 119 */       this.height = 480;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JComponent getComponent() {
/* 128 */     DrawingPanel d = new DrawingPanel(this.width * this.factor, this.height * this.factor);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     if (this.o != null)
/* 134 */       d.add((JComponent)this.o); 
/* 135 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Polygon getPolygon() {
/* 145 */     return (Polygon)this.o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 153 */     this.factor = 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\Model.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */