package application;

import controls.Resetable;
import java.util.Observable;
import java.util.Vector;
import javax.swing.JComponent;
import shapes.GraphicObject;
import shapes.Polygon;

public class Model extends Observable implements Resetable {
  private Vector v;
  
  private Object o;
  
  private int factor;
  
  private int width;
  
  private int height;
  
  public Model() {
    setFactor(1);
  }
  
  public void reset() {
    this.o = null;
    setFactor(1);
    notifyChanged("cleared");
  }
  
  public void setFactor(int factor) {
    this.factor = factor;
    if (this.o != null)
      ((GraphicObject)this.o).setFactor(factor); 
    notifyChanged("M_factor");
  }
  
  public int getFactor() {
    return this.factor;
  }
  
  public void append(Object o) {
    this.o = o;
    notifyChanged("M_append");
  }
  
  public void notifyChanged(Object arg) {
    setChanged();
    notifyObservers(arg);
  }
  
  public String toString() {
    return this.o.toString();
  }
  
  public void setToView(View view) {
    if (view.isSmall()) {
      this.width = 320;
      this.height = 240;
    } else {
      this.width = 640;
      this.height = 480;
    } 
  }
  
  public JComponent getComponent() {
    DrawingPanel d = new DrawingPanel(this.width * this.factor, this.height * this.factor);
    if (this.o != null)
      d.add((JComponent)this.o); 
    return d;
  }
  
  public Polygon getPolygon() {
    return (Polygon)this.o;
  }
  
  public void clear() {
    this.factor = 1;
  }
}
