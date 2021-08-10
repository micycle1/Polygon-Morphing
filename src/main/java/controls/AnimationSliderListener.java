/*    */ package controls;
/*    */ 
/*    */ import application.ExtendedController;
/*    */ import application.Model;
/*    */ import javax.swing.JSlider;
/*    */ import javax.swing.event.ChangeEvent;
/*    */ import shapes.Polygon;
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
/*    */ public class AnimationSliderListener
/*    */   extends CGSliderListener
/*    */ {
/*    */   private Model model;
/*    */   private ExtendedController ec;
/*    */   
/*    */   public AnimationSliderListener(ExtendedController ec) {
/* 29 */     this.ec = ec;
/* 30 */     this.model = ec.getModel();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stateChanged(ChangeEvent e) {
/* 41 */     JSlider s = (JSlider)e.getSource();
/* 42 */     this.model.clear();
/* 43 */     Polygon display = this.ec.getMorphs()[s.getValue() - 1];
/* 44 */     this.model.append(display);
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\controls\AnimationSliderListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */