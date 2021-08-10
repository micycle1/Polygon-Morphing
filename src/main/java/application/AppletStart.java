
package application;

import application.ExtendedController;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JApplet;

public class AppletStart
extends JApplet {
    private ExtendedController ec;

    public void init() {
        this.ec = new ExtendedController(true);
        this.setLayout(new BorderLayout());
        this.add((Component)this.ec, "Center");
    }

    public void paint(Graphics g) {
        this.ec.paint(g);
    }
}

