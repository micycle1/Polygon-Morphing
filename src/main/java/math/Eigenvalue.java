/*     */ package math;
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
/*     */ 
/*     */ public class Eigenvalue
/*     */ {
/*     */   public static double[] eigenvalue(double a, double b, double c) {
/*  26 */     int igh = 1;
/*  27 */     double[] wr = new double[2];
/*     */     
/*  29 */     double s = 0.0D, zz = 0.0D;
/*  30 */     int en = 1;
/*     */     
/*  32 */     double norm = Math.abs(a) + 2.0D * Math.abs(b) + Math.abs(c);
/*     */ 
/*     */     
/*  35 */     while (en >= 0) {
/*  36 */       int na = en - 1;
/*     */       
/*  38 */       int l = en;
/*  39 */       if (l != 0) {
/*  40 */         s = Math.abs(a) + Math.abs(c);
/*  41 */         if (s == 0.0D)
/*  42 */           s = norm; 
/*  43 */         double tst1 = s;
/*  44 */         double tst2 = tst1 + Math.abs(b);
/*  45 */         if (tst2 != tst1)
/*  46 */           l--; 
/*     */       } 
/*  48 */       double x = c;
/*  49 */       double y = a;
/*  50 */       double w = b * b;
/*     */       
/*  52 */       double p = (-x + y) / 2.0D;
/*  53 */       double q = p * p + w;
/*  54 */       zz = Math.sqrt(Math.abs(q));
/*  55 */       if (q >= 0.0D) {
/*  56 */         zz = (p < 0.0D) ? (-zz + p) : (p + zz);
/*  57 */         wr[na] = x + zz; wr[en] = x + zz;
/*  58 */         if (zz != 0.0D)
/*  59 */           wr[en] = -(w / zz) + x; 
/*     */       } 
/*  61 */       en--;
/*  62 */       en--;
/*     */     } 
/*  64 */     return wr;
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
/*     */   public static double[] hqr2(double[][] a, double[][] b) {
/*  78 */     int igh = 1;
/*     */     
/*  80 */     double[] wr = new double[2];
/*     */     
/*  82 */     double[] wi = new double[2];
/*  83 */     double s = 0.0D, zz = 0.0D;
/*  84 */     int en = 1;
/*     */     
/*  86 */     b[0][0] = 1.0D; b[1][1] = 1.0D;
/*  87 */     b[1][0] = 0.0D; b[0][1] = 0.0D;
/*     */     
/*  89 */     double norm = Math.abs(a[0][0]) + Math.abs(a[0][1]) + Math.abs(a[1][0]) + Math.abs(a[1][1]);
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
/*     */   public static double[] hrq_tweaked(double[][] a) {
/* 288 */     int igh = 1;
/* 289 */     double[] wr = new double[2];
/*     */     
/* 291 */     double s = 0.0D, zz = 0.0D;
/* 292 */     int en = 1;
/*     */     
/* 294 */     double norm = Math.abs(a[0][0]) + Math.abs(a[0][1]) + Math.abs(a[1][0]) + Math.abs(a[1][1]);
/*     */ 
/*     */ 
/*     */     
/* 298 */     while (en >= 0) {
/*     */       
/* 300 */       int na = en - 1;
/*     */ 
/*     */ 
/*     */       
/* 304 */       int l = en;
/* 305 */       if (l != 0) {
/* 306 */         s = Math.abs(a[l - 1][l - 1]) + Math.abs(a[l][l]);
/* 307 */         if (s == 0.0D)
/* 308 */           s = norm; 
/* 309 */         double tst1 = s;
/* 310 */         double tst2 = tst1 + Math.abs(a[l][l - 1]);
/* 311 */         if (tst2 != tst1) {
/* 312 */           l--;
/*     */         }
/*     */       } 
/* 315 */       double x = a[en][en];
/*     */       
/* 317 */       if (l == en) {
/* 318 */         wr[en] = x;
/* 319 */         en--;
/*     */         
/*     */         continue;
/*     */       } 
/* 323 */       double y = a[na][na];
/* 324 */       double w = a[en][na] * a[na][en];
/*     */       
/* 326 */       if (l == na) {
/* 327 */         double p = (-x + y) / 2.0D;
/* 328 */         double q = p * p + w;
/* 329 */         zz = Math.sqrt(Math.abs(q));
/*     */ 
/*     */         
/* 332 */         if (q >= 0.0D) {
/* 333 */           zz = (p < 0.0D) ? (-zz + p) : (p + zz);
/* 334 */           wr[na] = x + zz; wr[en] = x + zz;
/* 335 */           if (zz != 0.0D)
/* 336 */             wr[en] = -(w / zz) + x; 
/*     */         } 
/*     */       } 
/* 339 */       en--;
/* 340 */       en--;
/*     */     } 
/*     */     
/* 343 */     return wr;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\math\Eigenvalue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */