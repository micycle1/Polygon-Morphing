package application;

import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JApplet;

public class AppletStart extends JApplet {
  private ExtendedController ec;
  
  public void init() {
    this.ec = new ExtendedController(true);
    setLayout(new BorderLayout());
    add(this.ec, "Center");
  }
  
  public void paint(Graphics g) {
    this.ec.paint(g);
  }
}
