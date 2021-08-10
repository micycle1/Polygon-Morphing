package micycle.polygonmorphing.application;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import micycle.polygonmorphing.controls.CGSliderListener;
import micycle.polygonmorphing.controls.Resetable;

public class CGSlider extends JPanel implements ChangeListener, Resetable {
	private JSlider slider;
	private JLabel text;
	private JLabel valueText;
	private int init;

	public CGSlider(String text, int min, int max, int init, CGSliderListener sl) {
		this.init = init;
		this.text = new JLabel(text);
		this.text.setPreferredSize(new Dimension(110, 18));
		this.add(this.text);
		this.valueText = new JLabel("" + init);
		this.valueText.setPreferredSize(new Dimension(30, 18));
		this.valueText.setHorizontalAlignment(4);
		this.add(this.valueText);
		this.slider = new JSlider(min, max, init);
		this.slider.addChangeListener(sl);
		this.slider.addChangeListener(this);
		this.slider.setPreferredSize(new Dimension(100, 18));
		this.add(this.slider);
		this.setBorder(BorderFactory.createEtchedBorder());
	}

	public void setMaximum(int n) {
		this.slider.setMaximum(n);
	}

	public int getMaximum() {
		return this.slider.getMaximum();
	}

	public void setMinimum(int n) {
		this.slider.setMinimum(n);
	}

	public int getMinimum() {
		return this.slider.getMinimum();
	}

	public int getValue() {
		return this.slider.getValue();
	}

	public void setValue(int n) {
		this.slider.setValue(n);
	}

	public void reset() {
		this.slider.setValue(this.init);
	}

	public void stateChanged(ChangeEvent e) {
		this.valueText.setText("" + this.slider.getValue());
	}

	public void setEnabled(boolean mode) {
		this.slider.setEnabled(mode);
	}

	public boolean isEnabled() {
		return this.slider.isEnabled();
	}

	public void setSliderFocusAble(boolean focusable) {
		this.slider.setFocusable(focusable);
	}
}
