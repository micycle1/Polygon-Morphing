
package micycle.polygonmorphing.application;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import micycle.polygonmorphing.application.DrawingPanel;

public class View
extends JScrollPane {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int WIDTH2 = 320;
    public static final int HEIGHT2 = 240;
    private static final int SCROLLBAR = 18;
    private boolean small;

    public View() {
        this(false);
    }

    public View(boolean small) {
        super(22, 32);
        this.small = small;
        if (small) {
            this.setPreferredSize(new Dimension(338, 258));
            this.getViewport().setView(new DrawingPanel(320, 240));
        } else {
            this.setPreferredSize(new Dimension(658, 498));
            this.getViewport().setView(new DrawingPanel(640, 480));
        }
    }

    public void display(JComponent j) {
        Dimension dim = j.getPreferredSize();
        int width = dim.width;
        int height = dim.height;
        Rectangle r = this.getViewport().getViewRect();
        this.getViewport().setView(j);
        if (r.width + r.x > width) {
            r.setLocation(width - r.width, r.y);
        }
        if (r.height + r.y > height) {
            r.setLocation(r.x, height - r.height);
        }
        this.getViewport().scrollRectToVisible(r);
    }

    public boolean isSmall() {
        return this.small;
    }
}

