/*     */ package shapes;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import tools.Bresenham;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Line
/*     */   extends GraphicObject
/*     */ {
/*     */   public static final boolean DASHED = true;
/*     */   private Point start;
/*     */   private Point end;
/*     */   private boolean dashed;
/*     */   
/*     */   public Line(Point start, Point end, int factor) {
/*  29 */     this(start, end, factor, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Line(Point start, Point end, int factor, boolean dashed) {
/*  40 */     setStart(start);
/*  41 */     setEnd(end);
/*  42 */     setFactor(factor);
/*  43 */     this.dashed = dashed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStart(Point start) {
/*  50 */     this.start = start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point getStart() {
/*  57 */     return this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnd(Point end) {
/*  64 */     this.end = end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point getEnd() {
/*  71 */     return this.end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paint(Graphics g) {
/*  79 */     if (!this.dashed) {
/*  80 */       Color c = g.getColor();
/*  81 */       g.setColor(Color.blue);
/*  82 */       Bresenham.draw(this.start, this.end, g, this.factor);
/*  83 */       g.setColor(c);
/*     */     }
/*     */     else {
/*     */       
/*  87 */       Bresenham.dashedLine(this.start, this.end, g, this.factor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Point p) {
/*  96 */     return !(!this.start.contains(p) && !this.end.contains(p));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSVG() {
/* 104 */     StringBuffer buff = new StringBuffer();
/*     */     
/* 106 */     buff.append("\t\t\t<path d=\"M " + this.start.getX() + " " + this.start.getY() + " L " + this.end.getX() + " " + this.end.getY() + "\" stroke=\"black\" stroke-width=\"1\"/>\n");
/* 107 */     return new String(buff);
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\shapes\Line.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */