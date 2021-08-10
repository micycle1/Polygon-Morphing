package application;

import controls.CGSliderListener;
import controls.ClearButtonListener;
import controls.Resetable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public class ViewController extends JPanel implements Observer, Resetable {
  private View view;
  
  private Model model;
  
  private PolygonListener listener;
  
  private int region = 10;
  
  public ViewController(boolean small) {
    if (small) {
      setView(new View(true));
    } else {
      setView(new View());
    } 
    setModel(new Model());
    registerWithModel();
    getModel().setToView(getView());
    setLayout(new BorderLayout());
    add(getView(), "Center");
    this.listener = new PolygonListener(this.model, this.region);
    this.view.addMouseListener(this.listener);
    this.view.addMouseMotionListener(this.listener);
    add(createComponents(), "South");
  }
  
  private Component createComponents() {
    JPanel panel = new JPanel();
    CGSlider zoom = new CGSlider("Zoom", 1, 10, 1, new ZoomFactorListener(getModel()));
    panel.add(zoom);
    JButton clear = new JButton("Clear");
    clear.setSize(new Dimension(80, 25));
    ClearButtonListener cbl = new ClearButtonListener();
    cbl.append(this.model);
    cbl.append(zoom);
    clear.addActionListener((ActionListener)cbl);
    panel.add(clear, "South");
    return panel;
  }
  
  public void setView(View view) {
    this.view = view;
  }
  
  public View getView() {
    return this.view;
  }
  
  public void setModel(Model model) {
    this.model = model;
  }
  
  public Model getModel() {
    return this.model;
  }
  
  public void registerWithModel() {
    getModel().addObserver(this);
  }
  
  public void update(Observable sender, Object arg1) {
    getView().display(((Model)sender).getComponent());
  }
  
  public void reset() {}
  
  protected static class ZoomFactorListener extends CGSliderListener {
    private Model model;
    
    public ZoomFactorListener(Model model) {
      this.model = model;
    }
    
    public void stateChanged(ChangeEvent e) {
      this.model.setFactor(((JSlider)e.getSource()).getValue());
    }
  }
}
