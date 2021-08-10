package application;

import java.util.Locale;
import javax.swing.JFrame;

public class Start {
  public static void main(String[] args) {
    Locale.setDefault(Locale.US);
    JFrame frame = new JFrame("Polygon-Morph");
    frame.getContentPane().add(new ExtendedController(false), "Center");
    frame.setDefaultCloseOperation(3);
    frame.pack();
    frame.setVisible(true);
  }
}
