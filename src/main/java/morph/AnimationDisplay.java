package morph;

import application.Controller;
import application.Model;
import shapes.Polygon;

public class AnimationDisplay extends Thread {
  private Polygon[] morphs;
  
  private Model model;
  
  private int steps;
  
  private int sleeptime;
  
  private Controller controller;
  
  public AnimationDisplay(Polygon[] morphs, Model model, Controller controller, int fps) {
    this.morphs = morphs;
    this.model = model;
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
  
  public void run() {
    for (int i = 0; i < this.steps; i++) {
      this.controller.getAnimationSlider().setValue(i + 1);
      try {
        sleep(this.sleeptime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } 
    } 
    this.controller.stopAnimator();
  }
}
