package application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;
import shapes.Point;
import shapes.Polygon;
import tools.Bresenham;

public class PolygonListener extends MouseInputAdapter {
  private Model model;
  
  private Polygon polygon;
  
  private int region;
  
  private boolean dashed = false;
  
  private Point start_dashed;
  
  private Polygon dashed_polygon;
  
  private Point end_dashed = new Point();
  
  private int factor = 1;
  
  public PolygonListener(Model model, int region) {
    setModel(model);
    setRegion(region);
    System.out.println(this.region);
  }
  
  public void setModel(Model model) {
    this.model = model;
  }
  
  public Model getModel() {
    return this.model;
  }
  
  public void setRegion(int region) {
    this.region = region;
  }
  
  public int getRegion() {
    return this.region;
  }
  
  public void mousePressed(MouseEvent e) {
    JViewport v = ((JScrollPane)e.getSource()).getViewport();
    Rectangle r = v.getViewRect();
    Graphics g = v.getGraphics();
    g.setXORMode(Color.CYAN);
    this.factor = getModel().getFactor();
    int x = r.x + e.getX();
    x /= this.factor;
    int y = r.y + e.getY();
    y /= this.factor;
    Point point = new Point(x, y, this.factor);
    if (this.polygon == null) {
      this.polygon = new Polygon(point, this.region, this.factor);
      this.start_dashed = new Point(e.getX() / this.factor, e.getY() / this.factor, this.factor);
      this.dashed_polygon = new Polygon(this.start_dashed, this.region, this.factor, true);
    } else if (this.polygon.isClosingPoint(point)) {
      this.polygon.close();
      this.dashed = false;
      this.model.append(this.polygon);
      this.polygon = null;
      this.start_dashed = null;
      this.dashed_polygon = null;
    } else {
      this.dashed = false;
      this.polygon.addVertex(point);
      this.start_dashed = new Point(e.getX() / this.factor, e.getY() / this.factor, this.factor);
      this.dashed_polygon.addVertex(this.start_dashed);
    } 
  }
  
  public void mouseExited(MouseEvent e) {
    if (this.polygon != null) {
      Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
      g.setXORMode(Color.cyan);
      this.dashed_polygon.paint(g);
      Bresenham.dashedLine(this.start_dashed, this.end_dashed, g, this.factor);
      this.dashed = false;
      this.polygon = null;
      this.dashed_polygon = null;
      this.start_dashed = null;
    } 
  }
  
  public void mouseMoved(MouseEvent e) {
    if (this.polygon != null) {
      Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
      g.setXORMode(Color.CYAN);
      this.factor = getModel().getFactor();
      if (this.dashed)
        Bresenham.dashedLine(this.start_dashed, this.end_dashed, g, this.factor); 
      this.end_dashed.setX(e.getX() / this.factor);
      this.end_dashed.setY(e.getY() / this.factor);
      this.end_dashed.setFactor(this.factor);
      Bresenham.dashedLine(this.start_dashed, this.end_dashed, g, this.factor);
      this.dashed = true;
    } 
  }
}
