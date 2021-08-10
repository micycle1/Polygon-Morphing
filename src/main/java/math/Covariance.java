
package math;

import java.util.Vector;
import shapes.Point;

public class Covariance {
    public static double[] calculate_center(Vector v) {
        double[] center = new double[2];
        double count = v.size();
        double y_sum = 0.0;
        double x_sum = 0.0;
        int i = 0;
        while ((double)i < count) {
            Point p = (Point)v.elementAt(i);
            x_sum += (double)p.getX();
            y_sum += (double)p.getY();
            ++i;
        }
        center[0] = x_sum / count;
        center[1] = y_sum / count;
        return center;
    }

    public static double[][] covariance(Vector v) {
        double[][] matrix = new double[2][2];
        double count = v.size();
        double[] center = Covariance.calculate_center(v);
        double x_c = center[0];
        double y_c = center[1];
        double a00 = 0.0;
        double a01 = 0.0;
        double a11 = 0.0;
        int i = 0;
        while ((double)i < count) {
            Point p = (Point)v.elementAt(i);
            double x = (double)p.getX() - x_c;
            double y = (double)p.getY() - y_c;
            a00 += x * x;
            a01 += x * y;
            a11 += y * y;
            ++i;
        }
        matrix[0][0] = a00 /= count;
        double d = a01 /= count;
        matrix[0][1] = d;
        matrix[1][0] = d;
        matrix[1][1] = a11 /= count;
        return matrix;
    }

    public static double dotProduct(double a1, double a2, double b1, double b2) {
        return a1 * b1 + a2 * b2;
    }

    public static double[] getBisector(Vector v) {
        double[] bisector = new double[2];
        int mid = v.size() / 2;
        Point left = (Point)v.elementAt(mid - 1);
        Point right = (Point)v.elementAt(mid + 1);
        Point center = (Point)v.elementAt(mid);
        double center_x = center.getX();
        double center_y = center.getY();
        double left_x = left.getX();
        double left_y = left.getY();
        double right_x = right.getX();
        double right_y = right.getY();
        double dist1 = Math.sqrt(Math.pow(left_x - center_x, 2.0) + Math.pow(left_y - center_y, 2.0));
        double dist2 = Math.sqrt(Math.pow(center_x - right_x, 2.0) + Math.pow(center_y - right_y, 2.0));
        double dist_ratio = dist1 / (dist1 + dist2);
        double bisec_x = left_x + dist_ratio * (right_x - left_x);
        double bisec_y = left_y + dist_ratio * (right_y - left_y);
        bisector[0] = bisec_x -= center_x;
        bisector[1] = bisec_y -= center_y;
        return bisector;
    }
}

