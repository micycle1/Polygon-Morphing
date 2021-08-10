
package controls;

import application.Model;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;
import shapes.Line;
import shapes.Point;
import shapes.Polygon;

public class PolygonListener
extends MouseInputAdapter {
    private Model model;
    private Polygon poly;
    private int region;
    private Line dl;
    private boolean dashedMode = false;
    private Point startDashedLine;
    private Polygon dashedPoly;
    private String description;

    public PolygonListener(Model model, int region) {
        this.setModel(model);
        this.region = region;
        this.description = "Polygon";
    }

    public PolygonListener(Model model, int region, String description) {
        this.setModel(model);
        this.region = region;
        this.description = description;
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
        Rectangle r = ((JScrollPane)e.getSource()).getViewport().getViewRect();
        Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
        g.setXORMode(Color.cyan);
        int f = this.getModel().getFactor();
        int x = r.x + e.getX();
        int y = r.y + e.getY();
        Point p = new Point(x /= f, y /= f, f);
        if (this.poly == null) {
            this.poly = new Polygon(p, this.region, f);
            this.startDashedLine = new Point(e.getX() / f, e.getY() / f, f);
            this.dashedPoly = new Polygon(this.startDashedLine, this.region, f, true);
        } else if (this.poly.isClosingPoint(p)) {
            this.poly.close();
            this.dashedMode = false;
            this.model.append(this.poly);
            this.poly = null;
            this.startDashedLine = null;
            this.dashedPoly = null;
        } else {
            this.dashedMode = false;
            this.poly.addVertex(p);
            this.startDashedLine = new Point(e.getX() / f, e.getY() / f, f);
            this.dashedPoly.addVertex(this.startDashedLine);
        }
    }

    public void mouseExited(MouseEvent e) {
        if (this.poly != null) {
            Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
            g.setXORMode(Color.cyan);
            this.dashedPoly.paint(g);
            this.dl.paint(g);
            this.dashedMode = false;
            this.poly = null;
            this.startDashedLine = null;
            this.dashedPoly = null;
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (this.poly != null) {
            Graphics g = ((JScrollPane)e.getSource()).getViewport().getGraphics();
            g.setXORMode(Color.cyan);
            if (this.dashedMode) {
                this.dl.paint(g);
            }
            int f = this.getModel().getFactor();
            this.dl = new Line(this.startDashedLine, new Point(e.getX() / f, e.getY() / f, f), f, true);
            this.dl.paint(g);
            this.dashedMode = true;
        }
    }

    public String toString() {
        return this.description;
    }
}

