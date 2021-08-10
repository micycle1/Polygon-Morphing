
package application;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class DrawingPanel
extends JPanel {
    public DrawingPanel(int width, int height) {
        this.setLayout(new OverlayLayout(this));
        Dimension dim = new Dimension(width, height);
        this.setMaximumSize(dim);
        this.setMinimumSize(dim);
        this.setPreferredSize(dim);
        this.setForeground(new Color(0, 0, 0));
        this.setBackground(new Color(255, 255, 255));
    }
}

