/*    */ package featurePointDetection;
/*    */ 
/*    */ import shapes.FeaturePoint;
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
/*    */ public class QuickSort
/*    */ {
/*    */   public static void sort(FeaturePoint[] p) {
/* 21 */     quicksort(p, 0, p.length - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   private static void quicksort(FeaturePoint[] p, int lower, int upper) {
/* 26 */     int i = lower;
/* 27 */     int j = upper;
/* 28 */     FeaturePoint x = p[(lower + upper) / 2];
/*    */     while (true) {
/*    */       while (true) {
/* 31 */         if (p[i].compareTo(x) >= 0) {
/* 32 */           for (; p[j].compareTo(x) > 0; j--);
/* 33 */           if (i <= j) {
/* 34 */             FeaturePoint tmp = p[i];
/* 35 */             p[i] = p[j];
/* 36 */             p[j] = tmp;
/* 37 */             i++;
/* 38 */             j--;
/*    */           } 
/* 40 */           if (i > j)
/*    */             break;  continue;
/*    */         }  i++;
/*    */       } 
/* 44 */       if (lower < j) quicksort(p, lower, j); 
/* 45 */       if (i < upper) quicksort(p, i, upper); 
/*    */       return;
/*    */     } 
/*    */     i++;
/*    */     continue;
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\featurePointDetection\QuickSort.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */