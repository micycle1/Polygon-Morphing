package math;

import java.util.Vector;
import shapes.Point;

public class Covariance {
  public static double[] calculate_center(Vector v) {
    double[] center = new double[2];
    double count = v.size();
    double y_sum = 0.0D, x_sum = y_sum;
    int i = 0;
  }
  
  public static double[][] covariance(Vector v) {
    double[][] matrix = new double[2][2];
    double count = v.size();
    double[] center = calculate_center(v);
    double x_c = center[0];
    double y_c = center[1];
    double a00 = 0.0D, a01 = 0.0D, a11 = 0.0D;
    for (int i = 0; i < count; i++) {
      Point p = v.elementAt(i);
      double x = p.getX() - x_c;
      double y = p.getY() - y_c;
      a00 += x * x;
      a01 += x * y;
      a11 += y * y;
    } 
    a00 /= count;
    a01 /= count;
    a11 /= count;
    matrix[0][0] = a00;
    matrix[0][1] = a01;
    matrix[1][0] = a01;
    matrix[1][1] = a11;
    return matrix;
  }
  
  public static double dotProduct(double a1, double a2, double b1, double b2) {
    return a1 * b1 + a2 * b2;
  }
  
  public static double[] getBisector(Vector v) {
    double[] bisector = new double[2];
    int mid = v.size() / 2;
    Point left = v.elementAt(mid - 1);
    Point right = v.elementAt(mid + 1);
    Point center = v.elementAt(mid);
    double center_x = center.getX();
    double center_y = center.getY();
    double left_x = left.getX();
    double left_y = left.getY();
    double right_x = right.getX();
    double right_y = right.getY();
    double dist1 = Math.sqrt(Math.pow(left_x - center_x, 2.0D) + Math.pow(left_y - center_y, 2.0D));
    double dist2 = Math.sqrt(Math.pow(center_x - right_x, 2.0D) + Math.pow(center_y - right_y, 2.0D));
    double dist_ratio = dist1 / (dist1 + dist2);
    double bisec_x = left_x + dist_ratio * (right_x - left_x);
    double bisec_y = left_y + dist_ratio * (right_y - left_y);
    bisec_x -= center_x;
    bisec_y -= center_y;
    bisector[0] = bisec_x;
    bisector[1] = bisec_y;
    return bisector;
  }
}
