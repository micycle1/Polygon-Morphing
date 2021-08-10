/*    */ package application;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JScrollPane;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class View
/*    */   extends JScrollPane
/*    */ {
/*    */   public static final int WIDTH = 640;
/*    */   public static final int HEIGHT = 480;
/*    */   public static final int WIDTH2 = 320;
/*    */   public static final int HEIGHT2 = 240;
/*    */   private static final int SCROLLBAR = 18;
/*    */   private boolean small;
/*    */   
/*    */   public View() {
/* 35 */     this(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public View(boolean small) {
/* 44 */     super(22, 32);
/* 45 */     this.small = small;
/* 46 */     if (small) {
/* 47 */       setPreferredSize(new Dimension(338, 258));
/* 48 */       getViewport().setView(new DrawingPanel(320, 240));
/*    */     } else {
/*    */       
/* 51 */       setPreferredSize(new Dimension(658, 498));
/* 52 */       getViewport().setView(new DrawingPanel(640, 480));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void display(JComponent j) {
/* 61 */     Dimension dim = j.getPreferredSize();
/* 62 */     int width = dim.width;
/* 63 */     int height = dim.height;
/* 64 */     Rectangle r = getViewport().getViewRect();
/* 65 */     getViewport().setView(j);
/*    */     
/* 67 */     if (r.width + r.x > width)
/* 68 */       r.setLocation(width - r.width, r.y); 
/* 69 */     if (r.height + r.y > height)
/* 70 */       r.setLocation(r.x, height - r.height); 
/* 71 */     getViewport().scrollRectToVisible(r);
/*    */   }
/*    */   public boolean isSmall() {
/* 74 */     return this.small;
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\View.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */