
package micycle.polygonmorphing.application;

import micycle.polygonmorphing.application.ExtendedController;
import micycle.polygonmorphing.controls.Resetable;

import javax.swing.JPanel;
import javax.swing.JSlider;

public class AnimationSlider
extends JPanel
implements Resetable {
    private JSlider slider;
    private ExtendedController ec;

    public AnimationSlider(ExtendedController ec) {
        this.ec = ec;
        this.createComponents();
    }

    private void createComponents() {
        this.slider = new JSlider();
    }

    public void reset() {
        this.slider.setValue(1);
    }
}

