package shapes;

import java.awt.Graphics;
import javax.swing.JComponent;

public abstract class GraphicObject extends JComponent {
  protected int factor;
  
  public GraphicObject() {
    this(1);
  }
  
  public GraphicObject(int factor) {
    this.factor = factor;
  }
  
  public void setFactor(int factor) {
    if (factor < 1)
      throw new IllegalArgumentException("Factor must be 1 or larger!"); 
    this.factor = factor;
  }
  
  public int getFactor() {
    return this.factor;
  }
  
  public String toString() {
    return "GaphicObject [factor=" + getFactor() + "]";
  }
  
  public abstract void paint(Graphics paramGraphics);
  
  public abstract boolean contains(Point paramPoint);
  
  public abstract String toSVG();
}
