package application;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class DrawingPanel extends JPanel {
  public DrawingPanel(int width, int height) {
    setLayout(new OverlayLayout(this));
    Dimension dim = new Dimension(width, height);
    setMaximumSize(dim);
    setMinimumSize(dim);
    setPreferredSize(dim);
    setForeground(new Color(0, 0, 0));
    setBackground(new Color(255, 255, 255));
  }
}
