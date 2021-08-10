/*     */ package shapes;
/*     */ 
/*     */ import tools.Constants;
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
/*     */ public class FeaturePoint
/*     */   extends Point
/*     */   implements Cloneable, Comparable
/*     */ {
/*     */   private double feat_var;
/*     */   private double side_var;
/*     */   private double feat_size;
/*     */   private double dis_cost;
/*     */   private double r_feat_var;
/*     */   private double l_feat_var;
/*     */   private double r_size;
/*     */   private double l_size;
/*     */   private boolean prepared = false;
/*     */   private double angle;
/*     */   
/*     */   public FeaturePoint() {}
/*     */   
/*     */   public FeaturePoint(int x, int y) {
/*  42 */     super(x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FeaturePoint(Point p) {
/*  50 */     super(p.getX(), p.getY());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FeaturePoint(FeaturePoint fp) {
/*  58 */     this(fp);
/*  59 */     setFeat_var(fp.getFeat_var());
/*  60 */     setSide_var(fp.getSide_var());
/*  61 */     setFeat_size(fp.getFeat_size());
/*  62 */     setCorrespondence(fp.getCorrespondence());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FeaturePoint(int x, int y, int factor) {
/*  73 */     super(x, y, factor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeat_var(double feat_var) {
/*  81 */     this.feat_var = feat_var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFeat_var() {
/*  88 */     return this.feat_var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSide_var(double side_var) {
/*  95 */     this.side_var = side_var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSide_var() {
/* 102 */     return this.side_var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeat_size(double feat_size) {
/* 110 */     this.feat_size = feat_size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFeat_size() {
/* 118 */     return this.feat_size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setR_feat_var(double r_feat_var) {
/* 127 */     this.r_feat_var = r_feat_var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getR_feat_var() {
/* 135 */     return this.r_feat_var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setL_feat_var(double l_feat_var) {
/* 143 */     this.l_feat_var = l_feat_var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getL_feat_var() {
/* 151 */     return this.l_feat_var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setR_size(double r_size) {
/* 159 */     this.r_size = r_size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getR_size() {
/* 167 */     return this.r_size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setL_size(double l_size) {
/* 175 */     this.l_size = l_size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getL_size() {
/* 183 */     return this.l_size;
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
/*     */   public void setPrepared(boolean prepared) {
/* 196 */     this.prepared = prepared;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/* 204 */     return this.prepared;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 212 */     return String.valueOf(super.toString()) + " feature variation: " + this.feat_var + "\n feature side variation: " + this.side_var + "\n feature size: " + this.feat_size + "\n" + this.prepared + "\n\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDisCost() {
/* 220 */     if (!this.prepared) {
/* 221 */       throw new RuntimeException("this feature point is not properly prepared for calculating discard costs!");
/*     */     }
/* 223 */     if (this.dis_cost == 0.0D)
/* 224 */       calculate_Dis_Costs(); 
/* 225 */     return this.dis_cost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void calculate_Dis_Costs() {
/* 232 */     this.dis_cost = this.feat_size * (
/* 233 */       Math.abs(this.feat_var * Constants.WEIGHTS[0]) + 
/* 234 */       Math.abs(this.side_var * Constants.WEIGHTS[1]) + 
/* 235 */       Math.abs(this.feat_size * Constants.WEIGHTS[2]));
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
/*     */   public void setAngle(double angle) {
/* 267 */     this.angle = angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAngle() {
/* 275 */     return this.angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double calculate_Sim_Cost(FeaturePoint s, FeaturePoint t) {
/* 285 */     if (!s.isPrepared()) throw new RuntimeException("feature point " + s.toString() + " is not prepared yet!"); 
/* 286 */     if (!t.isPrepared()) throw new RuntimeException("feature point " + t.toString() + " is not prepared yet!");
/*     */ 
/*     */     
/* 289 */     double importance = Math.max(s.getFeat_size(), t.getFeat_size());
/* 290 */     double delta_sigma = Math.abs(s.getFeat_var() - t.getFeat_var());
/* 291 */     double delta_tau = 0.5D * (Math.abs(s.getL_feat_var() - t.getL_feat_var()) + 
/* 292 */       Math.abs(s.getR_feat_var() - t.getR_feat_var()));
/* 293 */     double delta_roh = 0.5D * (Math.abs(s.getL_size() - t.getL_size()) + 
/* 294 */       Math.abs(s.getR_size() - t.getR_size()));
/* 295 */     return importance * (Constants.WEIGHTS[0] * delta_sigma + 
/* 296 */       Constants.WEIGHTS[1] * delta_tau + 
/* 297 */       Constants.WEIGHTS[2] * delta_roh);
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
/*     */   public int compareTo(FeaturePoint fp) {
/* 309 */     if (this.angle < fp.angle)
/* 310 */       return -1; 
/* 311 */     if (this.angle > fp.angle) {
/* 312 */       return 1;
/*     */     }
/* 314 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 322 */     FeaturePoint fp = (FeaturePoint)super.clone();
/* 323 */     fp.feat_var = this.feat_var;
/* 324 */     fp.feat_size = this.feat_size;
/* 325 */     fp.side_var = this.side_var;
/* 326 */     fp.dis_cost = this.dis_cost;
/* 327 */     fp.r_feat_var = this.r_feat_var;
/* 328 */     fp.l_feat_var = this.l_feat_var;
/* 329 */     fp.r_size = this.r_size;
/* 330 */     fp.l_size = this.l_size;
/* 331 */     fp.prepared = this.prepared;
/* 332 */     if (getCorrespondence() != null) {
/* 333 */       fp.setCorrespondence(getCorrespondence());
/*     */     }
/*     */ 
/*     */     
/* 337 */     return fp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Object o) {
/* 348 */     if (o instanceof FeaturePoint)
/* 349 */       return compareTo((FeaturePoint)o); 
/* 350 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\shapes\FeaturePoint.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */