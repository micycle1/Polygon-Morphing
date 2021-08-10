/*    */ package morph;
/*    */ 
/*    */ import shapes.FeaturePoint;
/*    */ import shapes.Point;
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
/*    */ 
/*    */ 
/*    */ public class Animator
/*    */ {
/*    */   public static Polygon[] animate(Polygon source, Polygon target, int steps) {
/* 30 */     int source_points = source.getCount();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     Polygon[] morph_in_betweens = new Polygon[steps];
/*    */ 
/*    */     
/* 38 */     morph_in_betweens[0] = source; int i;
/* 39 */     for (i = 1; i < steps - 1; i++) {
/* 40 */       morph_in_betweens[i] = new Polygon();
/*    */     }
/*    */ 
/*    */     
/* 44 */     for (i = 0; i < source_points; i++) {
/* 45 */       Point start = source.getVertex(i);
/* 46 */       Point end = target.getVertex(i);
/* 47 */       double x = start.getX();
/* 48 */       double y = start.getY();
/*    */       
/* 50 */       double x_diff = end.getX() - x;
/*    */       
/* 52 */       double y_diff = end.getY() - y;
/*    */       
/* 54 */       x_diff /= (steps - 1);
/*    */       
/* 56 */       y_diff /= (steps - 1);
/* 57 */       if (start instanceof FeaturePoint) {
/* 58 */         for (int j = 1; j < steps - 1; j++) {
/* 59 */           x += x_diff;
/* 60 */           y += y_diff;
/* 61 */           morph_in_betweens[j].addVertex((Point)new FeaturePoint((int)(x + 0.5D), (int)(y + 0.5D)));
/*    */         } 
/*    */       } else {
/*    */         
/* 65 */         for (int j = 1; j < steps - 1; j++) {
/* 66 */           x += x_diff;
/* 67 */           y += y_diff;
/* 68 */           morph_in_betweens[j].addVertex(new Point((int)(x + 0.5D), (int)(y + 0.5D)));
/*    */         } 
/*    */       } 
/*    */     } 
/* 72 */     morph_in_betweens[steps - 1] = target;
/* 73 */     for (i = 1; i < steps - 1; i++) {
/* 74 */       morph_in_betweens[i].close();
/*    */     }
/* 76 */     return morph_in_betweens;
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
/*    */   
/*    */   public static Polygon[] animate(Polygon source, Polygon target, int fps, double time) {
/* 92 */     double calctime = time * 100.0D;
/* 93 */     int int_time = (int)(calctime + 0.5D);
/* 94 */     calctime = int_time / 100.0D;
/* 95 */     double decimal_places = calctime - (int_time / 100);
/* 96 */     double total_steps = calctime * fps;
/* 97 */     return animate(source, target, (int)(total_steps + 0.5D));
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\morph\Animator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */