
package micycle.polygonmorphing.shapes;

import java.awt.Graphics;

import micycle.polygonmorphing.shapes.GraphicObject;

public class Point
extends GraphicObject
implements Cloneable {
    private int x;
    private int y;
    private Point correspondence = null;
    private boolean isFeaturePoint = false;
    private boolean convex = false;
    public static final int MaxX = 800;
    public static final int MaxY = 600;
    public static final int Min = 0;

    public Point() {
        this(0, 0, 1);
    }

    public Point(int x, int y) {
        this(x, y, 1);
    }

    public Point(Point p) {
        this(p.getX(), p.getY(), p.getFactor());
    }

    public Point(int x, int y, int factor) {
        super(factor);
        this.setX(x);
        this.setY(y);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) throws IllegalArgumentException {
        if (x < 0) {
            throw new IllegalArgumentException("No negative x value allowed! x=" + x + " ");
        }
        if (x > 800) {
            throw new IllegalArgumentException("No x value lager than 800 allowed! x=" + x + " ");
        }
        this.x = x;
    }

    public void setY(int y) throws IllegalArgumentException {
        if (y < 0) {
            throw new IllegalArgumentException("No negative y value allowed! y=" + y + " ");
        }
        if (y > 600) {
            throw new IllegalArgumentException("No y value lager than 600 allowed! y=" + y + " ");
        }
        this.y = y;
    }

    public void setConvex() {
        this.convex = true;
    }

    public void setConcave() {
        this.convex = false;
    }

    public boolean getConvex() {
        return this.convex;
    }

    public void setCorrespondence(Point correspondence) {
        if (correspondence.hasCorrespondence()) {
            throw new IllegalArgumentException("Point has already correspondence");
        }
        this.correspondence = correspondence;
        correspondence.correspondence = this;
    }

    public Point getCorrespondence() {
        return this.correspondence;
    }

    public boolean hasCorrespondence() {
        return this.correspondence != null;
    }

    public void clearCorrespondence() {
        if (this.hasCorrespondence()) {
            this.correspondence.correspondence = null;
        }
        this.correspondence = null;
    }

    public boolean equals(Point p) {
        return this.x == p.getX() && this.y == p.getY();
    }

    public String toString() {
        return "Point: x=" + this.x + " y=" + this.y + "\n";
    }

    public void paint(Graphics g) {
        g.fillOval(this.x * this.factor, this.y * this.factor, this.factor, this.factor);
    }

    public boolean contains(Point p) {
        return Math.abs(p.getX() - this.x) + Math.abs(p.getY() - this.y) < 5;
    }

    public Object clone() throws CloneNotSupportedException {
        Point p = (Point)super.clone();
        p.x = this.x;
        p.y = this.y;
        return p;
    }

    public String toSVG() {
        return "\t\t\t<circle cx=\"" + this.getX() + "\" cy=\"" + this.getY() + "\" r=\"1\" fill=\"black\" />\n";
    }
}

