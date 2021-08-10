/*    */ package application;
/*    */ 
/*    */ import controls.Resetable;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JSlider;
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
/*    */ 
/*    */ public class AnimationSlider
/*    */   extends JPanel
/*    */   implements Resetable
/*    */ {
/*    */   private JSlider slider;
/*    */   private ExtendedController ec;
/*    */   
/*    */   public AnimationSlider(ExtendedController ec) {
/* 32 */     this.ec = ec;
/* 33 */     createComponents();
/*    */   }
/*    */ 
/*    */   
/*    */   private void createComponents() {
/* 38 */     this.slider = new JSlider();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void reset() {
/* 46 */     this.slider.setValue(1);
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\AnimationSlider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */