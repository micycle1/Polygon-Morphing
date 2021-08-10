/*     */ package math;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import shapes.Point;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Covariance
/*     */ {
/*     */   public static double[] calculate_center(Vector v) {
/*  25 */     double[] center = new double[2];
/*  26 */     double count = v.size();
/*  27 */     double y_sum = 0.0D, x_sum = y_sum;
/*     */     
/*  29 */     int i = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double[][] covariance(Vector v) {
/*  52 */     double[][] matrix = new double[2][2];
/*  53 */     double count = v.size();
/*  54 */     double[] center = calculate_center(v);
/*  55 */     double x_c = center[0];
/*  56 */     double y_c = center[1];
/*     */     
/*  58 */     double a00 = 0.0D, a01 = 0.0D, a11 = 0.0D;
/*     */     
/*  60 */     for (int i = 0; i < count; i++) {
/*  61 */       Point p = v.elementAt(i);
/*  62 */       double x = p.getX() - x_c;
/*  63 */       double y = p.getY() - y_c;
/*  64 */       a00 += x * x;
/*  65 */       a01 += x * y;
/*  66 */       a11 += y * y;
/*     */     } 
/*  68 */     a00 /= count;
/*  69 */     a01 /= count;
/*  70 */     a11 /= count;
/*     */     
/*  72 */     matrix[0][0] = a00;
/*  73 */     matrix[0][1] = a01; matrix[1][0] = a01;
/*  74 */     matrix[1][1] = a11;
/*  75 */     return matrix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double dotProduct(double a1, double a2, double b1, double b2) {
/*  88 */     return a1 * b1 + a2 * b2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double[] getBisector(Vector v) {
/* 100 */     double[] bisector = new double[2];
/* 101 */     int mid = v.size() / 2;
/* 102 */     Point left = v.elementAt(mid - 1);
/* 103 */     Point right = v.elementAt(mid + 1);
/* 104 */     Point center = v.elementAt(mid);
/*     */ 
/*     */     
/* 107 */     double center_x = center.getX();
/* 108 */     double center_y = center.getY();
/* 109 */     double left_x = left.getX();
/* 110 */     double left_y = left.getY();
/* 111 */     double right_x = right.getX();
/* 112 */     double right_y = right.getY();
/* 113 */     double dist1 = Math.sqrt(Math.pow(left_x - center_x, 2.0D) + Math.pow(left_y - center_y, 2.0D));
/* 114 */     double dist2 = Math.sqrt(Math.pow(center_x - right_x, 2.0D) + Math.pow(center_y - right_y, 2.0D));
/* 115 */     double dist_ratio = dist1 / (dist1 + dist2);
/*     */     
/* 117 */     double bisec_x = left_x + dist_ratio * (right_x - left_x);
/* 118 */     double bisec_y = left_y + dist_ratio * (right_y - left_y);
/*     */ 
/*     */     
/* 121 */     bisec_x -= center_x;
/* 122 */     bisec_y -= center_y;
/*     */     
/* 124 */     bisector[0] = bisec_x;
/* 125 */     bisector[1] = bisec_y;
/* 126 */     return bisector;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\math\Covariance.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */