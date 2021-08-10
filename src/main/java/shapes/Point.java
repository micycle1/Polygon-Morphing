/*     */ package shapes;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Point
/*     */   extends GraphicObject
/*     */   implements Cloneable
/*     */ {
/*     */   private int x;
/*     */   private int y;
/*  22 */   private Point correspondence = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isFeaturePoint = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean convex = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MaxX = 800;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MaxY = 600;
/*     */ 
/*     */   
/*     */   public static final int Min = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public Point() {
/*  46 */     this(0, 0, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point(int x, int y) {
/*  56 */     this(x, y, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point(Point p) {
/*  64 */     this(p.getX(), p.getY(), p.getFactor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point(int x, int y, int factor) {
/*  75 */     super(factor);
/*  76 */     setX(x);
/*  77 */     setY(y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getX() {
/*  85 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getY() {
/*  92 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(int x) throws IllegalArgumentException {
/* 102 */     if (x < 0) throw new IllegalArgumentException("No negative x value allowed! x=" + x + " "); 
/* 103 */     if (x > 800) throw new IllegalArgumentException("No x value lager than 800 allowed! x=" + x + " "); 
/* 104 */     this.x = x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(int y) throws IllegalArgumentException {
/* 115 */     if (y < 0) throw new IllegalArgumentException("No negative y value allowed! y=" + y + " "); 
/* 116 */     if (y > 600) throw new IllegalArgumentException("No y value lager than 600 allowed! y=" + y + " "); 
/* 117 */     this.y = y;
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
/*     */   public void setConvex() {
/* 133 */     this.convex = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConcave() {
/* 142 */     this.convex = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getConvex() {
/* 153 */     return this.convex;
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
/*     */   public void setCorrespondence(Point correspondence) {
/* 165 */     if (correspondence.hasCorrespondence())
/* 166 */       throw new IllegalArgumentException("Point has already correspondence"); 
/* 167 */     this.correspondence = correspondence;
/* 168 */     correspondence.correspondence = this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point getCorrespondence() {
/* 177 */     return this.correspondence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasCorrespondence() {
/* 187 */     if (this.correspondence == null)
/* 188 */       return false; 
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCorrespondence() {
/* 196 */     if (hasCorrespondence()) {
/* 197 */       this.correspondence.correspondence = null;
/*     */     }
/* 199 */     this.correspondence = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Point p) {
/* 209 */     return (this.x == p.getX() && this.y == p.getY());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 217 */     return "Point: x=" + this.x + " y=" + this.y + "\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paint(Graphics g) {
/* 225 */     g.fillOval(this.x * this.factor, this.y * this.factor, this.factor, this.factor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Point p) {
/* 234 */     return (Math.abs(p.getX() - this.x) + Math.abs(p.getY() - this.y) < 5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 242 */     Point p = (Point)super.clone();
/* 243 */     p.x = this.x;
/* 244 */     p.y = this.y;
/* 245 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSVG() {
/* 254 */     return "\t\t\t<circle cx=\"" + getX() + "\" cy=\"" + getY() + "\" r=\"1\" fill=\"black\" />\n";
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\shapes\Point.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */