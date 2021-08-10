package micycle.polygonmorphing.controls;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import micycle.polygonmorphing.application.ExtendedController;
import micycle.polygonmorphing.application.Model;
import micycle.polygonmorphing.shapes.Polygon;

public class AnimationSliderListener extends CGSliderListener {
	private Model model;
	private ExtendedController ec;

	public AnimationSliderListener(ExtendedController ec) {
		this.ec = ec;
		this.model = ec.getModel();
	}

	public void stateChanged(ChangeEvent e) {
		JSlider s = (JSlider) e.getSource();
		this.model.clear();
		Polygon display = this.ec.getMorphs()[s.getValue() - 1];
		this.model.append(display);
	}
}
