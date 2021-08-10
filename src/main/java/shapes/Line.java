
package shapes;

import java.awt.Color;
import java.awt.Graphics;
import shapes.GraphicObject;
import shapes.Point;
import tools.Bresenham;

public class Line
extends GraphicObject {
    public static final boolean DASHED = true;
    private Point start;
    private Point end;
    private boolean dashed;

    public Line(Point start, Point end, int factor) {
        this(start, end, factor, false);
    }

    public Line(Point start, Point end, int factor, boolean dashed) {
        this.setStart(start);
        this.setEnd(end);
        this.setFactor(factor);
        this.dashed = dashed;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getStart() {
        return this.start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public Point getEnd() {
        return this.end;
    }

    public void paint(Graphics g) {
        if (!this.dashed) {
            Color c = g.getColor();
            g.setColor(Color.blue);
            Bresenham.draw(this.start, this.end, g, this.factor);
            g.setColor(c);
        } else {
            Bresenham.dashedLine(this.start, this.end, g, this.factor);
        }
    }

    public boolean contains(Point p) {
        return this.start.contains(p) || this.end.contains(p);
    }

    public String toSVG() {
        StringBuffer buff = new StringBuffer();
        buff.append("\t\t\t<path d=\"M " + this.start.getX() + " " + this.start.getY() + " L " + this.end.getX() + " " + this.end.getY() + "\" stroke=\"black\" stroke-width=\"1\"/>\n");
        return new String(buff);
    }
}

