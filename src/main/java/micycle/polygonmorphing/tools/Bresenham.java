package micycle.polygonmorphing.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Enumeration;
import java.util.Vector;

import micycle.polygonmorphing.shapes.Point;

public class Bresenham {
  public static int last_counter;
  
  public static void draw(Point p, Point q, Graphics g, int factor) {
    int inc_x, inc_y;
    Color c;
    int x = p.getX();
    int y = p.getY();
    int x_end = q.getX();
    int y_end = q.getY();
    int dx = x_end - x;
    int dy = y_end - y;
    if (dx > 0) {
      inc_x = 1;
    } else {
      inc_x = -1;
    } 
    if (dy > 0) {
      inc_y = 1;
    } else {
      inc_y = -1;
    } 
    if (Math.abs(dy) < Math.abs(dx)) {
      int error = -Math.abs(dx);
      int delta = 2 * Math.abs(dy);
      int step = 2 * error;
      while (x != x_end) {
        setPixel(x, y, factor, g);
        x += inc_x;
        error += delta;
        if (error > 0) {
          y += inc_y;
          error += step;
        } 
      } 
    } else {
      int error = -Math.abs(dy);
      int delta = 2 * Math.abs(dx);
      int step = 2 * error;
      while (y != y_end) {
        setPixel(x, y, factor, g);
        y += inc_y;
        error += delta;
        if (error > 0) {
          x += inc_x;
          error += step;
        } 
      } 
    } 
    if (q instanceof micycle.polygonmorphing.shapes.FeaturePoint) {
      c = Color.BLUE;
    } else {
      c = Color.RED;
    } 
    setFatPixel(x_end, y_end, factor, g, c);
  }
  
  public static Vector sample(Point p, Point q, int sample_rate, int offset) {
    int inc_x, inc_y;
    Vector return_sample = new Vector();
    int x = p.getX();
    int y = p.getY();
    int x_end = q.getX();
    int y_end = q.getY();
    int dx = x_end - x;
    int dy = y_end - y;
    int counter = offset;
    if (dx > 0) {
      inc_x = 1;
    } else {
      inc_x = -1;
    } 
    if (dy > 0) {
      inc_y = 1;
    } else {
      inc_y = -1;
    } 
    if (Math.abs(dy) < Math.abs(dx)) {
      int error = -Math.abs(dx);
      int delta = 2 * Math.abs(dy);
      int step = 2 * error;
      while (x != x_end) {
        if (counter >= sample_rate) {
          return_sample.add(new Point(x, y));
          counter = 0;
        } 
        counter++;
        x += inc_x;
        error += delta;
        if (error > 0) {
          y += inc_y;
          error += step;
        } 
      } 
    } else {
      int error = -Math.abs(dy);
      int delta = 2 * Math.abs(dx);
      int step = 2 * error;
      while (y != y_end) {
        if (counter >= sample_rate) {
          return_sample.add(new Point(x, y));
          counter = 0;
        } 
        counter++;
        y += inc_y;
        error += delta;
        if (error > 0) {
          x += inc_x;
          error += step;
        } 
      } 
    } 
    return_sample.add(q);
    last_counter = counter;
    return return_sample;
  }
  
  public static Vector semiUniformSample(Point p, Point q, int sample_rate) {
    int inc_x, inc_y;
    Vector return_sample = new Vector();
    int x = p.getX();
    int y = p.getY();
    int x_end = q.getX();
    int y_end = q.getY();
    double distance = Math.sqrt(Math.pow((x - x_end), 2.0D) + Math.pow((y - y_end), 2.0D));
    double part_distance = distance / (sample_rate + 1);
    int length = (int)(part_distance + 0.5D);
    int dx = x_end - x;
    int dy = y_end - y;
    if (dx > 0) {
      inc_x = 1;
    } else {
      inc_x = -1;
    } 
    if (dy > 0) {
      inc_y = 1;
    } else {
      inc_y = -1;
    } 
    return_sample.add(p);
    int counter = 0;
    if (Math.abs(dy) < Math.abs(dx)) {
      int error = -Math.abs(dx);
      int delta = 2 * Math.abs(dy);
      int step = 2 * error;
      while (x != x_end) {
        if (counter >= length) {
          return_sample.add(new Point(x, y));
          counter = 0;
        } 
        counter++;
        x += inc_x;
        error += delta;
        if (error > 0) {
          y += inc_y;
          error += step;
        } 
      } 
    } else {
      int error = -Math.abs(dy);
      int delta = 2 * Math.abs(dx);
      int step = 2 * error;
      while (y != y_end) {
        if (counter >= length) {
          return_sample.add(new Point(x, y));
          counter = 0;
        } 
        counter++;
        y += inc_y;
        error += delta;
        if (error > 0) {
          x += inc_x;
          error += step;
        } 
      } 
    } 
    if (!((Point)return_sample.lastElement()).equals(q))
      return_sample.add(q); 
    return return_sample;
  }
  
  public static void dashedLine(Point p, Point q, Graphics g, int factor) {
    int inc_x, inc_y, steps = 0;
    int x = p.getX();
    int y = p.getY();
    int x_end = q.getX();
    int y_end = q.getY();
    int dx = x_end - x;
    int dy = y_end - y;
    if (dx > 0) {
      inc_x = 1;
    } else {
      inc_x = -1;
    } 
    if (dy > 0) {
      inc_y = 1;
    } else {
      inc_y = -1;
    } 
    if (Math.abs(dy) < Math.abs(dx)) {
      int error = -Math.abs(dx);
      int delta = 2 * Math.abs(dy);
      int step = 2 * error;
      while (x != x_end) {
        if (steps++ / 3 % 2 == 0)
          setPixel(x, y, factor, g); 
        x += inc_x;
        error += delta;
        if (error > 0) {
          y += inc_y;
          error += step;
        } 
      } 
    } else {
      int error = -Math.abs(dy);
      int delta = 2 * Math.abs(dx);
      int step = 2 * error;
      while (y != y_end) {
        if (steps++ / 3 % 2 == 0)
          setPixel(x, y, factor, g); 
        y += inc_y;
        error += delta;
        if (error > 0) {
          x += inc_x;
          error += step;
        } 
      } 
    } 
    if (steps++ / 3 % 2 == 0)
      setPixel(x_end, y_end, factor, g); 
  }
  
  public static double getLength(Vector<Point> vector) {
    double length = 0.0D;
    int size = vector.size();
    if (size == 0 || size == 1)
      return length; 
    Enumeration<Point> e = vector.elements();
    Point a = e.nextElement();
    while (e.hasMoreElements()) {
      Point b = e.nextElement();
      int x_len = a.getX() - b.getX();
      int y_len = a.getY() - b.getY();
      length += Math.sqrt((x_len * x_len + y_len * y_len));
      a = b;
    } 
    return length;
  }
  
  private static void setPixel(int x, int y, int factor, Graphics g) {
    g.fillOval(x * factor, y * factor, factor, factor);
  }
  
  private static void setFatPixel(int x, int y, int factor, Graphics g, Color c) {
    Color color = g.getColor();
    g.setColor(c);
    g.fillOval((x - 1) * factor, (y - 1) * factor, 4 * factor, 4 * factor);
    g.setColor(color);
  }
}
