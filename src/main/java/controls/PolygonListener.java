/*     */ package controls;
/*     */ 
/*     */ import application.Model;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import shapes.Line;
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
/*     */ public class PolygonListener
/*     */   extends MouseInputAdapter
/*     */ {
/*     */   private Model model;
/*     */   private Polygon poly;
/*     */   private int region;
/*     */   private Line dl;
/*     */   private boolean dashedMode = false;
/*     */   private Point startDashedLine;
/*     */   private Polygon dashedPoly;
/*     */   private String description;
/*     */   
/*     */   public PolygonListener(Model model, int region) {
/*  43 */     setModel(model);
/*  44 */     this.region = region;
/*  45 */     this.description = "Polygon";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolygonListener(Model model, int region, String description) {
/*  55 */     setModel(model);
/*  56 */     this.region = region;
/*  57 */     this.description = description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModel(Model model) {
/*  66 */     this.model = model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model getModel() {
/*  74 */     return this.model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegion(int region) {
/*  83 */     this.region = region;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRegion() {
/*  92 */     return this.region;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mousePressed(MouseEvent e) {
/* 103 */     Rectangle r = ((JScrollPane)e.getSource()).getViewport().getViewRect();
/* 104 */     Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
/* 105 */     g.setXORMode(Color.cyan);
/* 106 */     int f = getModel().getFactor();
/* 107 */     int x = r.x + e.getX();
/* 108 */     x /= f;
/* 109 */     int y = r.y + e.getY();
/* 110 */     y /= f;
/*     */     
/* 112 */     Point p = new Point(x, y, f);
/*     */     
/* 114 */     if (this.poly == null) {
/* 115 */       this.poly = new Polygon(p, this.region, f);
/* 116 */       this.startDashedLine = new Point(e.getX() / f, e.getY() / f, f);
/* 117 */       this.dashedPoly = new Polygon(this.startDashedLine, this.region, f, true);
/*     */     
/*     */     }
/* 120 */     else if (this.poly.isClosingPoint(p)) {
/* 121 */       this.poly.close();
/* 122 */       this.dashedMode = false;
/* 123 */       this.model.append(this.poly);
/* 124 */       this.poly = null;
/* 125 */       this.startDashedLine = null;
/* 126 */       this.dashedPoly = null;
/*     */     }
/*     */     else {
/*     */       
/* 130 */       this.dashedMode = false;
/* 131 */       this.poly.addVertex(p);
/* 132 */       this.startDashedLine = new Point(e.getX() / f, e.getY() / f, f);
/* 133 */       this.dashedPoly.addVertex(this.startDashedLine);
/*     */     } 
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
/*     */   public void mouseExited(MouseEvent e) {
/* 146 */     if (this.poly != null) {
/* 147 */       Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
/* 148 */       g.setXORMode(Color.cyan);
/* 149 */       this.dashedPoly.paint(g);
/* 150 */       this.dl.paint(g);
/* 151 */       this.dashedMode = false;
/* 152 */       this.poly = null;
/* 153 */       this.startDashedLine = null;
/* 154 */       this.dashedPoly = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseMoved(MouseEvent e) {
/* 163 */     if (this.poly != null) {
/* 164 */       Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
/* 165 */       g.setXORMode(Color.cyan);
/* 166 */       if (this.dashedMode) {
/* 167 */         this.dl.paint(g);
/*     */       }
/* 169 */       int f = getModel().getFactor();
/* 170 */       this.dl = new Line(this.startDashedLine, new Point(e.getX() / f, e.getY() / f, f), f, true);
/* 171 */       this.dl.paint(g);
/* 172 */       this.dashedMode = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 180 */     return this.description;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\controls\PolygonListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */