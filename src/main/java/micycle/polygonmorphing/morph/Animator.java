
package micycle.polygonmorphing.morph;

import micycle.polygonmorphing.shapes.FeaturePoint;
import micycle.polygonmorphing.shapes.Point;
import micycle.polygonmorphing.shapes.Polygon;

public class Animator {
    public static Polygon[] animate(Polygon source, Polygon target, int steps) {
        int i;
        int source_points = source.getCount();
        Polygon[] morph_in_betweens = new Polygon[steps];
        morph_in_betweens[0] = source;
        for (i = 1; i < steps - 1; ++i) {
            morph_in_betweens[i] = new Polygon();
        }
        for (i = 0; i < source_points; ++i) {
            int j;
            Point start = source.getVertex(i);
            Point end = target.getVertex(i);
            double x = start.getX();
            double y = start.getY();
            double x_diff = (double)end.getX() - x;
            double y_diff = (double)end.getY() - y;
            x_diff /= (double)(steps - 1);
            y_diff /= (double)(steps - 1);
            if (start instanceof FeaturePoint) {
                for (j = 1; j < steps - 1; ++j) {
                    morph_in_betweens[j].addVertex(new FeaturePoint((int)((x += x_diff) + 0.5), (int)((y += y_diff) + 0.5)));
                }
                continue;
            }
            for (j = 1; j < steps - 1; ++j) {
                morph_in_betweens[j].addVertex(new Point((int)((x += x_diff) + 0.5), (int)((y += y_diff) + 0.5)));
            }
        }
        morph_in_betweens[steps - 1] = target;
        for (i = 1; i < steps - 1; ++i) {
            morph_in_betweens[i].close();
        }
        return morph_in_betweens;
    }

    public static Polygon[] animate(Polygon source, Polygon target, int fps, double time) {
        double calctime = time * 100.0;
        int int_time = (int)(calctime + 0.5);
        calctime = (double)int_time / 100.0;
        double decimal_places = calctime - (double)(int_time / 100);
        double total_steps = calctime * (double)fps;
        return Animator.animate(source, target, (int)(total_steps + 0.5));
    }
}

