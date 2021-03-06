package micycle.polygonmorphing.morph;

import micycle.polygonmorphing.application.Controller;
import micycle.polygonmorphing.application.Model;
import micycle.polygonmorphing.shapes.Polygon;

public class AnimationDisplay extends Thread {
	private Polygon[] morphs;
	private int steps;
	private int sleeptime;
	private Controller controller;

	public AnimationDisplay(Polygon[] morphs, Model model, Controller controller, int fps) {
		this.morphs = morphs;
		this.steps = this.morphs.length;
		this.sleeptime = 1000 / fps;
		this.controller = controller;
	}

	public AnimationDisplay(Polygon[] morphs, Model model, Controller controller) {
		this(morphs, model, controller, 20);
	}

	public void setFPS(int fps) {
		this.sleeptime = 1000 / fps;
	}

	public void setSleeptime(int sleeptime) {
		this.sleeptime = sleeptime;
	}

	@Override
	public void run() {
		for (int i = 0; i < this.steps; ++i) {
			this.controller.getAnimationSlider().setValue(i + 1);
			try {
				Thread.sleep(this.sleeptime);
				continue;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.controller.stopAnimator();
	}
}
