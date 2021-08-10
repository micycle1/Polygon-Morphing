/*    */ package application;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.swing.JFrame;
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
/*    */ public class Start
/*    */ {
/*    */   public static void main(String[] args) {
/* 25 */     Locale.setDefault(Locale.US);
/* 26 */     JFrame frame = new JFrame("Polygon-Morph");
/* 27 */     frame.getContentPane().add(new ExtendedController(false), "Center");
/*    */ 
/*    */     
/* 30 */     frame.setDefaultCloseOperation(3);
/* 31 */     frame.pack();
/* 32 */     frame.setVisible(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\Start.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */