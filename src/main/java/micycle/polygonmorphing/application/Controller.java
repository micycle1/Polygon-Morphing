package micycle.polygonmorphing.application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import micycle.polygonmorphing.controls.CGSliderListener;
import micycle.polygonmorphing.controls.ClearButtonListener;
import micycle.polygonmorphing.controls.Resetable;
import micycle.polygonmorphing.morph.AnimationDisplay;
import micycle.polygonmorphing.morph.Animator;
import micycle.polygonmorphing.morph.MorphCalculator;
import micycle.polygonmorphing.shapes.Polygon;
import micycle.polygonmorphing.tools.Path;

public class Controller extends JPanel implements Observer, Resetable {
	private View view;

	private ViewController original;

	private ViewController target;

	private Model model;

	private Vector elv;

	static final int SEL = 0;

	private JComboBox choice;

	protected boolean simple_ROS;

	protected boolean applet;

	protected CGSlider animationSlider;

	private Polygon start = null;

	private Polygon finish = null;

	private Polygon[] morphs;

	private long seconds;

	private AnimationDisplay ad;

	protected boolean changed;

	static final int WIDTH = 400;

	static final int HEIGHT = 300;

	private int sample_rate = 5;

	private int ros_size = 10;

	private int skips = 3;

	public Controller(boolean applet) {
		this.applet = applet;
		setView(new View());
		setOriginal(new ViewController(true));
		setTarget(new ViewController(true));
		setModel(new Model());
		registerWithModel();
		add(createComponents(), "Center");
		getModel().setToView(getView());
	}

	protected Component createComponents() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel tmp = new JPanel();
		JPanel tmp2 = new JPanel(new BorderLayout());
		JPanel menu = new JPanel(new BorderLayout());
		tmp2.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		tmp2.add(getView(), "Center");
		tmp = new JPanel();
		tmp.add(getOriginal(), "West");
		tmp.add(getTarget(), "East");
		tmp2.add(tmp, "North");
		menu = createMenu();
		panel.add(tmp2, "Center");
		panel.add(menu, "East");
		return panel;
	}

	protected JPanel createMenu() {
		JPanel menu = new JPanel();
		JPanel tmp = new JPanel();
		JPanel tmp2 = new JPanel();
		menu.setLayout(new BorderLayout());
		JButton calcMorph = new JButton("Calculate Morph");
		calcMorph.setSize(150, 25);
		CalculateMorphButtonListener cmbl = new CalculateMorphButtonListener(this);
		calcMorph.addActionListener(cmbl);
		JButton animate = new JButton("Animate!");
		animate.setSize(120, 25);
		AnimationButtonListener abl = new AnimationButtonListener(this);
		animate.addActionListener(abl);
		tmp.add(calcMorph, "North");
		tmp.add(animate, "South");
		menu.add(tmp, "North");
		CGSlider zoom = new CGSlider("Zoomfaktor", 1, 10, 1, new ZoomFactorListener(this, getModel()));
		tmp2.add(zoom, "North");
		JButton clear = new JButton("Clear");
		clear.setSize(new Dimension(80, 25));
		ClearButtonListener cbl = new ClearButtonListener();
		cbl.append(getModel());
		cbl.append(zoom);
		clear.addActionListener((ActionListener) cbl);
		tmp2.add(clear, "South");
		menu.add(tmp2, "South");
		return menu;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Model getModel() {
		return this.model;
	}

	public void setView(View view) {
		this.view = view;
	}

	public View getView() {
		return this.view;
	}

	public void setOriginal(ViewController original) {
		this.original = original;
	}

	public ViewController getOriginal() {
		return this.original;
	}

	public void setTarget(ViewController target) {
		this.target = target;
	}

	public ViewController getTarget() {
		return this.target;
	}

	public void registerWithModel() {
		getModel().addObserver(this);
		getOriginal().getModel().addObserver(this);
		getTarget().getModel().addObserver(this);
	}

	public void update(Observable sender, Object arg) {
		getView().display(((Model) sender).getComponent());
	}

	public void reset() {
	}

	protected void setStart(Polygon start) {
		this.start = start;
	}

	public Polygon getStart() {
		return this.start;
	}

	protected void setFinish(Polygon finish) {
		this.finish = finish;
	}

	public Polygon getFinish() {
		return this.finish;
	}

	public void setSample_rate(int sample_rate) {
		this.sample_rate = sample_rate;
	}

	public int getSample_rate() {
		return this.sample_rate;
	}

	public void setROS_size(int sample_size) {
		this.ros_size = sample_size;
	}

	public int getROS_size() {
		return this.ros_size;
	}

	public void setSkips(int skips) {
		this.skips = skips;
	}

	public int getSkips() {
		return this.skips;
	}

	public void setMorphs(Polygon[] morphs) {
		this.morphs = morphs;
	}

	public Polygon[] getMorphs() {
		return this.morphs;
	}

	protected class ZoomFactorListener extends CGSliderListener {
		private Model model;

		final Controller this$0;

		public ZoomFactorListener(Controller this$0, Model model) {
			this.this$0 = this$0;
			this.model = model;
		}

		public void stateChanged(ChangeEvent e) {
			this.model.setFactor(((JSlider) e.getSource()).getValue());
		}
	}

	protected class CalculateMorphButtonListener implements ActionListener {
		final Controller this$0;

		protected CalculateMorphButtonListener(Controller this$0) {
			this.this$0 = this$0;
		}

		public void actionPerformed(ActionEvent event) {
			Polygon originalPoly, targetPoly;
			try {
				originalPoly = (Polygon) this.this$0.original.getModel().getPolygon().clone();
				targetPoly = (Polygon) this.this$0.target.getModel().getPolygon().clone();
			} catch (CloneNotSupportedException e) {
				System.err.println(e.toString());
				System.out.println("Error during cloning of Polygons occured. Trying to use originals.");
				originalPoly = this.this$0.original.getModel().getPolygon();
				targetPoly = this.this$0.target.getModel().getPolygon();
			}
			originalPoly.preparePolygon(this.this$0.getSample_rate(), this.this$0.getROS_size(), true);
			targetPoly.preparePolygon(this.this$0.getSample_rate(), this.this$0.getROS_size(), true);
			Path optimalPath = MorphCalculator.calculatePath(originalPoly, targetPoly, this.this$0.skips);
			System.out.println(optimalPath.toString());
			Path finalPath = optimalPath.getFinalPath();
			Polygon[] MorphablePolygons = MorphCalculator.createCompleteMorph(finalPath, optimalPath, originalPoly, targetPoly);
			this.this$0.setStart(MorphablePolygons[0]);
			this.this$0.setFinish(MorphablePolygons[1]);
			this.this$0.start.changeSize(2.0D);
			this.this$0.finish.changeSize(2.0D);
			this.this$0.original.getModel().getPolygon().deleteCorrespondences();
			this.this$0.animationSlider.setMaximum((this.this$0.getMorphs()).length);
			this.this$0.setMorphs(Animator.animate(this.this$0.start, this.this$0.finish, 5));
			this.this$0.seconds = 5L;
			this.this$0.ad = new AnimationDisplay(this.this$0.getMorphs(), this.this$0.model, this.this$0);
			this.this$0.model.append(this.this$0.start);
		}
	}

	protected class AnimationButtonListener implements ActionListener {
		final Controller this$0;

		protected AnimationButtonListener(Controller this$0) {
			this.this$0 = this$0;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (this.this$0.getMorphs() != null)
				this.this$0.ad.start();
		}
	}

	public void stopAnimator() {
		this.ad = null;
		this.ad = new AnimationDisplay(getMorphs(), this.model, this);
	}

	public CGSlider getAnimationSlider() {
		return this.animationSlider;
	}

	public void setSimple_ROS(boolean simple_ROS) {
		if (simple_ROS != this.simple_ROS)
			this.changed = true;
		this.simple_ROS = simple_ROS;
	}

	public boolean isSimple_ROS() {
		return this.simple_ROS;
	}
}
