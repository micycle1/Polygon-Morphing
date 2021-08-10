package morph;

import shapes.FeaturePoint;
import shapes.Point;
import shapes.Polygon;

public class Animator {
  public static Polygon[] animate(Polygon source, Polygon target, int steps) {
    int source_points = source.getCount();
    Polygon[] morph_in_betweens = new Polygon[steps];
    morph_in_betweens[0] = source;
    int i;
    for (i = 1; i < steps - 1; i++)
      morph_in_betweens[i] = new Polygon(); 
    for (i = 0; i < source_points; i++) {
      Point start = source.getVertex(i);
      Point end = target.getVertex(i);
      double x = start.getX();
      double y = start.getY();
      double x_diff = end.getX() - x;
      double y_diff = end.getY() - y;
      x_diff /= (steps - 1);
      y_diff /= (steps - 1);
      if (start instanceof FeaturePoint) {
        for (int j = 1; j < steps - 1; j++) {
          x += x_diff;
          y += y_diff;
          morph_in_betweens[j].addVertex((Point)new FeaturePoint((int)(x + 0.5D), (int)(y + 0.5D)));
        } 
      } else {
        for (int j = 1; j < steps - 1; j++) {
          x += x_diff;
          y += y_diff;
          morph_in_betweens[j].addVertex(new Point((int)(x + 0.5D), (int)(y + 0.5D)));
        } 
      } 
    } 
    morph_in_betweens[steps - 1] = target;
    for (i = 1; i < steps - 1; i++)
      morph_in_betweens[i].close(); 
    return morph_in_betweens;
  }
  
  public static Polygon[] animate(Polygon source, Polygon target, int fps, double time) {
    double calctime = time * 100.0D;
    int int_time = (int)(calctime + 0.5D);
    calctime = int_time / 100.0D;
    double decimal_places = calctime - (int_time / 100);
    double total_steps = calctime * fps;
    return animate(source, target, (int)(total_steps + 0.5D));
  }
}
