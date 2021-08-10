
package micycle.polygonmorphing.shapes;

import micycle.polygonmorphing.shapes.Point;
import micycle.polygonmorphing.tools.Constants;

public class FeaturePoint
extends Point
implements Cloneable,
Comparable {
    private double feat_var;
    private double side_var;
    private double feat_size;
    private double dis_cost;
    private double r_feat_var;
    private double l_feat_var;
    private double r_size;
    private double l_size;
    private boolean prepared = false;
    private double angle;

    public FeaturePoint() {
    }

    public FeaturePoint(int x, int y) {
        super(x, y);
    }

    public FeaturePoint(Point p) {
        super(p.getX(), p.getY());
    }

    public FeaturePoint(FeaturePoint fp) {
        this((Point)fp);
        this.setFeat_var(fp.getFeat_var());
        this.setSide_var(fp.getSide_var());
        this.setFeat_size(fp.getFeat_size());
        this.setCorrespondence(fp.getCorrespondence());
    }

    public FeaturePoint(int x, int y, int factor) {
        super(x, y, factor);
    }

    public void setFeat_var(double feat_var) {
        this.feat_var = feat_var;
    }

    public double getFeat_var() {
        return this.feat_var;
    }

    public void setSide_var(double side_var) {
        this.side_var = side_var;
    }

    public double getSide_var() {
        return this.side_var;
    }

    public void setFeat_size(double feat_size) {
        this.feat_size = feat_size;
    }

    public double getFeat_size() {
        return this.feat_size;
    }

    public void setR_feat_var(double r_feat_var) {
        this.r_feat_var = r_feat_var;
    }

    public double getR_feat_var() {
        return this.r_feat_var;
    }

    public void setL_feat_var(double l_feat_var) {
        this.l_feat_var = l_feat_var;
    }

    public double getL_feat_var() {
        return this.l_feat_var;
    }

    public void setR_size(double r_size) {
        this.r_size = r_size;
    }

    public double getR_size() {
        return this.r_size;
    }

    public void setL_size(double l_size) {
        this.l_size = l_size;
    }

    public double getL_size() {
        return this.l_size;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    public boolean isPrepared() {
        return this.prepared;
    }

    public String toString() {
        return String.valueOf(super.toString()) + " feature variation: " + this.feat_var + "\n feature side variation: " + this.side_var + "\n feature size: " + this.feat_size + "\n" + this.prepared + "\n\n";
    }

    public double getDisCost() {
        if (!this.prepared) {
            throw new RuntimeException("this feature point is not properly prepared for calculating discard costs!");
        }
        if (this.dis_cost == 0.0) {
            this.calculate_Dis_Costs();
        }
        return this.dis_cost;
    }

    private void calculate_Dis_Costs() {
        this.dis_cost = this.feat_size * (Math.abs(this.feat_var * Constants.WEIGHTS[0]) + Math.abs(this.side_var * Constants.WEIGHTS[1]) + Math.abs(this.feat_size * Constants.WEIGHTS[2]));
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return this.angle;
    }

    public static double calculate_Sim_Cost(FeaturePoint s, FeaturePoint t) {
        if (!s.isPrepared()) {
            throw new RuntimeException("feature point " + s.toString() + " is not prepared yet!");
        }
        if (!t.isPrepared()) {
            throw new RuntimeException("feature point " + t.toString() + " is not prepared yet!");
        }
        double importance = Math.max(s.getFeat_size(), t.getFeat_size());
        double delta_sigma = Math.abs(s.getFeat_var() - t.getFeat_var());
        double delta_tau = 0.5 * (Math.abs(s.getL_feat_var() - t.getL_feat_var()) + Math.abs(s.getR_feat_var() - t.getR_feat_var()));
        double delta_roh = 0.5 * (Math.abs(s.getL_size() - t.getL_size()) + Math.abs(s.getR_size() - t.getR_size()));
        return importance * (Constants.WEIGHTS[0] * delta_sigma + Constants.WEIGHTS[1] * delta_tau + Constants.WEIGHTS[2] * delta_roh);
    }

    public int compareTo(FeaturePoint fp) {
        if (this.angle < fp.angle) {
            return -1;
        }
        if (this.angle > fp.angle) {
            return 1;
        }
        return 0;
    }

    public Object clone() throws CloneNotSupportedException {
        FeaturePoint fp = (FeaturePoint)super.clone();
        fp.feat_var = this.feat_var;
        fp.feat_size = this.feat_size;
        fp.side_var = this.side_var;
        fp.dis_cost = this.dis_cost;
        fp.r_feat_var = this.r_feat_var;
        fp.l_feat_var = this.l_feat_var;
        fp.r_size = this.r_size;
        fp.l_size = this.l_size;
        fp.prepared = this.prepared;
        if (this.getCorrespondence() != null) {
            fp.setCorrespondence(this.getCorrespondence());
        }
        return fp;
    }

    public int compareTo(Object o) {
        if (o instanceof FeaturePoint) {
            return this.compareTo((FeaturePoint)o);
        }
        return 0;
    }
}

