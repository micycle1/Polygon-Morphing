/*    */ package application;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JApplet;
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
/*    */ public class AppletStart
/*    */   extends JApplet
/*    */ {
/*    */   private ExtendedController ec;
/*    */   
/*    */   public void init() {
/* 24 */     this.ec = new ExtendedController(true);
/* 25 */     setLayout(new BorderLayout());
/* 26 */     add(this.ec, "Center");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void paint(Graphics g) {
/* 33 */     this.ec.paint(g);
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\AppletStart.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */