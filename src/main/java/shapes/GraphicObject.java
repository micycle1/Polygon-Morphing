/*    */ package shapes;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GraphicObject
/*    */   extends JComponent
/*    */ {
/*    */   protected int factor;
/*    */   
/*    */   public GraphicObject() {
/* 23 */     this(1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GraphicObject(int factor) {
/* 31 */     this.factor = factor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFactor(int factor) {
/* 39 */     if (factor < 1) throw new IllegalArgumentException("Factor must be 1 or larger!");
/*    */     
/* 41 */     this.factor = factor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getFactor() {
/* 49 */     return this.factor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "GaphicObject [factor=" + getFactor() + "]";
/*    */   }
/*    */   
/*    */   public abstract void paint(Graphics paramGraphics);
/*    */   
/*    */   public abstract boolean contains(Point paramPoint);
/*    */   
/*    */   public abstract String toSVG();
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\shapes\GraphicObject.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */