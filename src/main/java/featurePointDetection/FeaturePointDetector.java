/*     */ package featurePointDetection;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import math.Covariance;
/*     */ import shapes.FeaturePoint;
/*     */ import shapes.Point;
/*     */ import shapes.Polygon;
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
/*     */ public class FeaturePointDetector
/*     */ {
/*     */   private double max_angle;
/*     */   private double min_length;
/*     */   private int max_featurePoints;
/*     */   private double angle;
/*     */   
/*     */   public FeaturePointDetector() {
/*  33 */     setMax_angle(130.0D);
/*  34 */     setMin_length(0.01D);
/*  35 */     setMax_featurePoints(35);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FeaturePointDetector(double max_angle, double min_size, int max_featurePoints) {
/*  46 */     setMax_angle(max_angle);
/*  47 */     setMin_length(min_size);
/*  48 */     setMax_featurePoints(max_featurePoints);
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
/*     */   public Polygon featureDetection(Polygon p) {
/*  94 */     Polygon detected = new Polygon();
/*  95 */     if (p.isClosed()) {
/*  96 */       int count = p.getCount();
/*     */       
/*  98 */       Point start = p.getVertex(count - 1);
/*  99 */       Point middle = p.getVertex(0);
/*     */       
/* 101 */       double polygon_length = p.getLength();
/* 102 */       for (int i = 1; i < count; i++) {
/* 103 */         Point point = p.getVertex(i);
/* 104 */         if (isFeaturePoint(start, middle, point, polygon_length)) {
/* 105 */           detected.addVertex((Point)new FeaturePoint(middle));
/* 106 */           ((FeaturePoint)detected.getFeaturePoints().lastElement()).setAngle(this.angle);
/*     */         } else {
/*     */           
/* 109 */           detected.addVertex(new Point(middle));
/*     */         } 
/* 111 */         start = middle;
/* 112 */         middle = point;
/*     */       } 
/* 114 */       Point end = p.getVertex(0);
/* 115 */       if (isFeaturePoint(start, middle, end, polygon_length)) {
/* 116 */         detected.addVertex((Point)new FeaturePoint(middle));
/* 117 */         ((FeaturePoint)detected.getFeaturePoints().lastElement()).setAngle(this.angle);
/*     */       } else {
/*     */         
/* 120 */         detected.addVertex(new Point(middle));
/* 121 */       }  detected.close();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 126 */     return detected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void filterMostProminent(Polygon p) {
/* 136 */     if (p.getFeaturePointCount() > this.max_featurePoints) {
/*     */       
/* 138 */       Object[] tmp = p.getFeaturePoints().toArray();
/* 139 */       FeaturePoint[] fp = new FeaturePoint[tmp.length];
/* 140 */       for (int i = 0; i < tmp.length; i++) {
/* 141 */         fp[i] = (FeaturePoint)tmp[i];
/*     */       }
/* 143 */       QuickSort.sort(fp);
/* 144 */       Vector featurePoints = p.getFeaturePoints();
/* 145 */       Vector vertices = p.getAllVertices();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 150 */       for (int j = this.max_featurePoints; j < fp.length; j++) {
/* 151 */         Point point = new Point((Point)fp[j]);
/* 152 */         featurePoints.removeElement(fp[j]);
/* 153 */         int index = vertices.indexOf(fp[j]);
/* 154 */         vertices.removeElement(fp[j]);
/* 155 */         vertices.insertElementAt(point, index);
/*     */       } 
/* 157 */       p.updateFeaturePointCount();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isFeaturePoint(Point start, Point middle, Point end, double polygon_length) {
/* 166 */     double start_middle_x = (start.getX() - middle.getX());
/* 167 */     double start_middle_y = (start.getY() - middle.getY());
/* 168 */     double end_middle_x = (end.getX() - middle.getX());
/* 169 */     double end_middle_y = (end.getY() - middle.getY());
/*     */ 
/*     */     
/* 172 */     double dotProd = Covariance.dotProduct(start_middle_x, start_middle_y, end_middle_x, end_middle_y);
/* 173 */     double length1 = Math.sqrt(Math.pow(start_middle_x, 2.0D) + Math.pow(start_middle_y, 2.0D));
/* 174 */     double length2 = Math.sqrt(Math.pow(end_middle_x, 2.0D) + Math.pow(end_middle_y, 2.0D));
/* 175 */     this.angle = Math.acos(dotProd / length1 * length2);
/* 176 */     this.angle = Math.toDegrees(this.angle);
/* 177 */     if (this.angle <= this.max_angle) {
/* 178 */       double relative_length = (length1 + length2) / polygon_length;
/* 179 */       if (relative_length >= this.min_length) {
/* 180 */         return true;
/*     */       }
/* 182 */       return false;
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMax_angle(double max_angle) {
/* 192 */     this.max_angle = max_angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMax_angle() {
/* 199 */     return this.max_angle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMin_length(double min_size) {
/* 206 */     this.min_length = min_size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMin_length() {
/* 213 */     return this.min_length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMax_featurePoints(int max_featurePoints) {
/* 220 */     this.max_featurePoints = max_featurePoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMax_featurePoints() {
/* 227 */     return this.max_featurePoints;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\featurePointDetection\FeaturePointDetector.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */