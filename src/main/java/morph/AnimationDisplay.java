/*    */ package morph;
/*    */ 
/*    */ import application.Controller;
/*    */ import application.Model;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnimationDisplay
/*    */   extends Thread
/*    */ {
/*    */   private Polygon[] morphs;
/*    */   private Model model;
/*    */   private int steps;
/*    */   private int sleeptime;
/*    */   private Controller controller;
/*    */   
/*    */   public AnimationDisplay(Polygon[] morphs, Model model, Controller controller, int fps) {
/* 35 */     this.morphs = morphs;
/* 36 */     this.model = model;
/* 37 */     this.steps = this.morphs.length;
/* 38 */     this.sleeptime = 1000 / fps;
/* 39 */     this.controller = controller;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnimationDisplay(Polygon[] morphs, Model model, Controller controller) {
/* 49 */     this(morphs, model, controller, 20);
/*    */   }
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
/*    */   public void setFPS(int fps) {
/* 64 */     this.sleeptime = 1000 / fps;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSleeptime(int sleeptime) {
/* 72 */     this.sleeptime = sleeptime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 79 */     for (int i = 0; i < this.steps; i++) {
/*    */ 
/*    */       
/* 82 */       this.controller.getAnimationSlider().setValue(i + 1);
/*    */       try {
/* 84 */         sleep(this.sleeptime);
/* 85 */       } catch (InterruptedException e) {
/*    */         
/* 87 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/* 90 */     this.controller.stopAnimator();
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\morph\AnimationDisplay.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */