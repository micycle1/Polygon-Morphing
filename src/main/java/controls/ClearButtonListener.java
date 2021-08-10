/*    */ package controls;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.util.Vector;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClearButtonListener
/*    */   implements ActionListener
/*    */ {
/* 17 */   private Vector rv = new Vector(3, 1);
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
/*    */   public void append(Resetable r) {
/* 31 */     this.rv.addElement(r);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 38 */     for (int i = 0; i < this.rv.size(); i++)
/* 39 */       ((Resetable)this.rv.elementAt(i)).reset(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\controls\ClearButtonListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */