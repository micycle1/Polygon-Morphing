
package micycle.polygonmorphing.tools;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Constants {
    public static final double[] WEIGHTS = new double[]{0.3333333333333333, 0.3333333333333333, 0.3333333333333333};
    public static final int[] DENOMINATORS = new int[]{3, 3, 3};
    public static final double LAMBDA = 1.0;
    public static final int MAX_SKIPS = 3;
    public static final int MAX_FPS = 25;
    public static final int MIN_FPS = 1;
    public static final int NORM_FPS = 15;
    public static final int MAX_STEPS = 2000;
    public static final int NORM_STEPS = 50;
    public static final int MIN_STEPS = 1;
    public static final double MIN_TIME = 0.0;
    public static final double MAX_TIME = 60.0;
    public static final double NORM_TIME = 5.0;
    public static final int ROS_SIZE = 10;
    public static final int SAMPLE_RATE = 5;
    public static final double MAX_ANGLE = 130.0;
    public static final double MIN_LENGTH = 0.01;
    public static final int MAX_FEATUREPOINTS = 35;
    public static final Border BLACKLINE = BorderFactory.createLineBorder(Color.black);

    public static void addIntoGrid(JPanel panel, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
        gridbag.setConstraints(comp, c);
        panel.add(comp);
    }
}

