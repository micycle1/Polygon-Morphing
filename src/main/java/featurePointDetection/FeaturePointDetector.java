
package featurePointDetection;

import featurePointDetection.QuickSort;
import java.util.Vector;
import math.Covariance;
import shapes.FeaturePoint;
import shapes.Point;
import shapes.Polygon;

public class FeaturePointDetector {
    private double max_angle;
    private double min_length;
    private int max_featurePoints;
    private double angle;

    public FeaturePointDetector() {
        this.setMax_angle(130.0);
        this.setMin_length(0.01);
        this.setMax_featurePoints(35);
    }

    public FeaturePointDetector(double max_angle, double min_size, int max_featurePoints) {
        this.setMax_angle(max_angle);
        this.setMin_length(min_size);
        this.setMax_featurePoints(max_featurePoints);
    }

    public Polygon featureDetection(Polygon p) {
        Polygon detected = new Polygon();
        if (p.isClosed()) {
            Point end;
            int count = p.getCount();
            Point start = p.getVertex(count - 1);
            Point middle = p.getVertex(0);
            double polygon_length = p.getLength();
            for (int i = 1; i < count; ++i) {
                end = p.getVertex(i);
                if (this.isFeaturePoint(start, middle, end, polygon_length)) {
                    detected.addVertex(new FeaturePoint(middle));
                    ((FeaturePoint)detected.getFeaturePoints().lastElement()).setAngle(this.angle);
                } else {
                    detected.addVertex(new Point(middle));
                }
                start = middle;
                middle = end;
            }
            end = p.getVertex(0);
            if (this.isFeaturePoint(start, middle, end, polygon_length)) {
                detected.addVertex(new FeaturePoint(middle));
                ((FeaturePoint)detected.getFeaturePoints().lastElement()).setAngle(this.angle);
            } else {
                detected.addVertex(new Point(middle));
            }
            detected.close();
        }
        return detected;
    }

    public void filterMostProminent(Polygon p) {
        if (p.getFeaturePointCount() > this.max_featurePoints) {
            Object[] tmp = p.getFeaturePoints().toArray();
            FeaturePoint[] fp = new FeaturePoint[tmp.length];
            for (int i = 0; i < tmp.length; ++i) {
                fp[i] = (FeaturePoint)tmp[i];
            }
            QuickSort.sort(fp);
            Vector featurePoints = p.getFeaturePoints();
            Vector vertices = p.getAllVertices();
            for (int i = this.max_featurePoints; i < fp.length; ++i) {
                Point point = new Point(fp[i]);
                featurePoints.removeElement(fp[i]);
                int index = vertices.indexOf(fp[i]);
                vertices.removeElement(fp[i]);
                vertices.insertElementAt(point, index);
            }
            p.updateFeaturePointCount();
        }
    }

    private boolean isFeaturePoint(Point start, Point middle, Point end, double polygon_length) {
        double start_middle_x = start.getX() - middle.getX();
        double start_middle_y = start.getY() - middle.getY();
        double end_middle_x = end.getX() - middle.getX();
        double end_middle_y = end.getY() - middle.getY();
        double dotProd = Covariance.dotProduct(start_middle_x, start_middle_y, end_middle_x, end_middle_y);
        double length1 = Math.sqrt(Math.pow(start_middle_x, 2.0) + Math.pow(start_middle_y, 2.0));
        double length2 = Math.sqrt(Math.pow(end_middle_x, 2.0) + Math.pow(end_middle_y, 2.0));
        this.angle = Math.acos(dotProd / (length1 * length2));
        this.angle = Math.toDegrees(this.angle);
        if (this.angle <= this.max_angle) {
            double relative_length = (length1 + length2) / polygon_length;
            return relative_length >= this.min_length;
        }
        return false;
    }

    public void setMax_angle(double max_angle) {
        this.max_angle = max_angle;
    }

    public double getMax_angle() {
        return this.max_angle;
    }

    public void setMin_length(double min_size) {
        this.min_length = min_size;
    }

    public double getMin_length() {
        return this.min_length;
    }

    public void setMax_featurePoints(int max_featurePoints) {
        this.max_featurePoints = max_featurePoints;
    }

    public int getMax_featurePoints() {
        return this.max_featurePoints;
    }
}

