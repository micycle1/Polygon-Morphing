/*    */ package application;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.OverlayLayout;
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
/*    */ 
/*    */ public class DrawingPanel
/*    */   extends JPanel
/*    */ {
/*    */   public DrawingPanel(int width, int height) {
/* 24 */     setLayout(new OverlayLayout(this));
/* 25 */     Dimension dim = new Dimension(width, height);
/*    */     
/* 27 */     setMaximumSize(dim);
/* 28 */     setMinimumSize(dim);
/* 29 */     setPreferredSize(dim);
/* 30 */     setForeground(new Color(0, 0, 0));
/* 31 */     setBackground(new Color(255, 255, 255));
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\DrawingPanel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */