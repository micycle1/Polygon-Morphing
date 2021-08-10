/*     */ package application;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import shapes.Point;
/*     */ import shapes.Polygon;
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
/*     */ public class PolygonListener
/*     */   extends MouseInputAdapter
/*     */ {
/*     */   private Model model;
/*     */   private Polygon polygon;
/*     */   private int region;
/*     */   private boolean dashed = false;
/*     */   private Point start_dashed;
/*     */   private Polygon dashed_polygon;
/*  34 */   private Point end_dashed = new Point();
/*  35 */   private int factor = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolygonListener(Model model, int region) {
/*  49 */     setModel(model);
/*  50 */     setRegion(region);
/*  51 */     System.out.println(this.region);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModel(Model model) {
/*  60 */     this.model = model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model getModel() {
/*  69 */     return this.model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegion(int region) {
/*  78 */     this.region = region;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRegion() {
/*  87 */     return this.region;
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
/*     */   public void mousePressed(MouseEvent e) {
/* 104 */     JViewport v = ((JScrollPane)e.getSource()).getViewport();
/* 105 */     Rectangle r = v.getViewRect();
/* 106 */     Graphics g = v.getGraphics();
/* 107 */     g.setXORMode(Color.CYAN);
/* 108 */     this.factor = getModel().getFactor();
/* 109 */     int x = r.x + e.getX();
/* 110 */     x /= this.factor;
/* 111 */     int y = r.y + e.getY();
/* 112 */     y /= this.factor;
/*     */     
/* 114 */     Point point = new Point(x, y, this.factor);
/*     */     
/* 116 */     if (this.polygon == null) {
/* 117 */       this.polygon = new Polygon(point, this.region, this.factor);
/* 118 */       this.start_dashed = new Point(e.getX() / this.factor, e.getY() / this.factor, this.factor);
/* 119 */       this.dashed_polygon = new Polygon(this.start_dashed, this.region, this.factor, true);
/*     */     
/*     */     }
/* 122 */     else if (this.polygon.isClosingPoint(point)) {
/* 123 */       this.polygon.close();
/*     */       
/* 125 */       this.dashed = false;
/* 126 */       this.model.append(this.polygon);
/* 127 */       this.polygon = null;
/* 128 */       this.start_dashed = null;
/* 129 */       this.dashed_polygon = null;
/*     */     } else {
/*     */       
/* 132 */       this.dashed = false;
/* 133 */       this.polygon.addVertex(point);
/* 134 */       this.start_dashed = new Point(e.getX() / this.factor, e.getY() / this.factor, this.factor);
/* 135 */       this.dashed_polygon.addVertex(this.start_dashed);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseExited(MouseEvent e) {
/* 141 */     if (this.polygon != null) {
/* 142 */       Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
/* 143 */       g.setXORMode(Color.cyan);
/* 144 */       this.dashed_polygon.paint(g);
/* 145 */       Bresenham.dashedLine(this.start_dashed, this.end_dashed, g, this.factor);
/* 146 */       this.dashed = false;
/* 147 */       this.polygon = null;
/* 148 */       this.dashed_polygon = null;
/* 149 */       this.start_dashed = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseMoved(MouseEvent e) {
/* 156 */     if (this.polygon != null) {
/* 157 */       Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
/*     */       
/* 159 */       g.setXORMode(Color.CYAN);
/* 160 */       this.factor = getModel().getFactor();
/* 161 */       if (this.dashed)
/* 162 */         Bresenham.dashedLine(this.start_dashed, this.end_dashed, g, this.factor); 
/* 163 */       this.end_dashed.setX(e.getX() / this.factor);
/* 164 */       this.end_dashed.setY(e.getY() / this.factor);
/* 165 */       this.end_dashed.setFactor(this.factor);
/* 166 */       Bresenham.dashedLine(this.start_dashed, this.end_dashed, g, this.factor);
/* 167 */       this.dashed = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\PolygonListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */