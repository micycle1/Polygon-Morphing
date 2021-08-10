/*    */ package tools;
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
/*    */ public class Stoppuhr
/*    */ {
/* 16 */   private static long startTime = 0L;
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean running = false;
/*    */ 
/*    */ 
/*    */   
/*    */   public static void start() {
/* 25 */     running = true;
/* 26 */     startTime = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static long stop() {
/* 36 */     if (!running) {
/* 37 */       return 0L;
/*    */     }
/*    */     
/* 40 */     running = false;
/* 41 */     return System.currentTimeMillis() - startTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\tools\Stoppuhr.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */