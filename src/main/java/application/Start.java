
package application;

import application.ExtendedController;
import java.awt.Component;
import java.util.Locale;
import javax.swing.JFrame;

public class Start {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        JFrame frame = new JFrame("Polygon-Morph");
        frame.getContentPane().add((Component)new ExtendedController(false), "Center");
        frame.setDefaultCloseOperation(3);
        frame.pack();
        frame.setVisible(true);
    }
}

