/*    */ package tools;
/*    */ 
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
/*    */ public class Test_space
/*    */ {
/*    */   public static void main(String[] args) {
/* 24 */     Point a = new Point(1, 1);
/* 25 */     Point b = new Point(100, 1);
/* 26 */     Point c = new Point(100, 100);
/* 27 */     Point d = new Point(1, 100);
/* 28 */     Polygon p = new Polygon();
/* 29 */     p.addVertex(a);
/* 30 */     p.addVertex(b);
/* 31 */     p.addVertex(c);
/* 32 */     p.addVertex(d);
/* 33 */     Polygon q = new Polygon();
/* 34 */     q.addVertex(new Point(101, 101));
/* 35 */     q.addVertex(new Point(201, 301));
/* 36 */     q.addVertex(new Point(201, 301));
/* 37 */     q.addVertex(new Point(151, 401));
/* 38 */     Polygon g = new Polygon();
/* 39 */     g.addVertex(new Point(100, 100));
/* 40 */     g.addVertex(new Point(130, 75));
/* 41 */     g.addVertex(new Point(140, 125));
/* 42 */     g.addVertex(new Point(150, 95));
/* 43 */     g.addVertex(new Point(180, 200));
/* 44 */     g.addVertex(new Point(160, 150));
/* 45 */     g.addVertex(new Point(110, 210));
/* 46 */     g.close();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     p.close();
/*    */     
/* 56 */     p.preparePolygon(5, 10, true);
/* 57 */     g.preparePolygon(5, 10, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\tools\Test_space.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */