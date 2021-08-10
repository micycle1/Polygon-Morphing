package tools;

import shapes.Point;
import shapes.Polygon;

public class Test_space {
  public static void main(String[] args) {
    Point a = new Point(1, 1);
    Point b = new Point(100, 1);
    Point c = new Point(100, 100);
    Point d = new Point(1, 100);
    Polygon p = new Polygon();
    p.addVertex(a);
    p.addVertex(b);
    p.addVertex(c);
    p.addVertex(d);
    Polygon q = new Polygon();
    q.addVertex(new Point(101, 101));
    q.addVertex(new Point(201, 301));
    q.addVertex(new Point(201, 301));
    q.addVertex(new Point(151, 401));
    Polygon g = new Polygon();
    g.addVertex(new Point(100, 100));
    g.addVertex(new Point(130, 75));
    g.addVertex(new Point(140, 125));
    g.addVertex(new Point(150, 95));
    g.addVertex(new Point(180, 200));
    g.addVertex(new Point(160, 150));
    g.addVertex(new Point(110, 210));
    g.close();
    p.close();
    p.preparePolygon(5, 10, true);
    g.preparePolygon(5, 10, true);
  }
}
