/*     */ package application;
/*     */ 
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.Observable;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JTextField;
/*     */ import tools.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RadioButtonPanel
/*     */   extends Observable
/*     */   implements ActionListener
/*     */ {
/*     */   private JPanel panel;
/*     */   private ButtonGroup b;
/*     */   private JRadioButton stepsButton;
/*     */   private JRadioButton framesButton;
/*     */   private JTextField stepCount;
/*     */   private JTextField fpsInput;
/*     */   private JTextField timeInput;
/*     */   private JLabel steps_label;
/*     */   private JLabel fps_label;
/*     */   private JLabel time_label;
/*     */   private int steps;
/*     */   private int fps;
/*     */   double time;
/*     */   private Integer intSteps;
/*     */   private Integer intFps;
/*     */   private Double doubleTime;
/*     */   
/*     */   public RadioButtonPanel(int steps, int fps, double time) {
/*  62 */     this.steps = steps;
/*  63 */     this.fps = fps;
/*  64 */     this.time = time;
/*  65 */     this.intSteps = new Integer(this.steps);
/*  66 */     this.intFps = new Integer(this.fps);
/*  67 */     this.doubleTime = new Double(this.time);
/*  68 */     createComponents();
/*  69 */     this.stepsButton.doClick();
/*  70 */     this.panel.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Animation Parameters"));
/*     */   }
/*     */   
/*     */   private void createComponents() {
/*  74 */     this.panel = new JPanel();
/*  75 */     GridBagLayout grid = new GridBagLayout();
/*  76 */     GridBagConstraints c = new GridBagConstraints();
/*  77 */     c.fill = 1;
/*  78 */     c.weightx = 1.0D;
/*  79 */     this.panel.setLayout(grid);
/*  80 */     this.b = new ButtonGroup();
/*  81 */     this.stepsButton = new JRadioButton("Steps");
/*  82 */     this.framesButton = new JRadioButton("Frames");
/*  83 */     this.stepCount = new JTextField(10);
/*  84 */     this.stepCount.setText(this.intSteps.toString());
/*  85 */     this.stepCount.setActionCommand("stepcount");
/*     */     
/*  87 */     this.fpsInput = new JTextField(3);
/*  88 */     this.fpsInput.setText(this.intFps.toString());
/*  89 */     this.fpsInput.setActionCommand("fps");
/*  90 */     this.fpsInput.setEditable(false);
/*  91 */     this.fpsInput.setEnabled(false);
/*     */     
/*  93 */     this.timeInput = new JTextField(3);
/*  94 */     this.timeInput.setText(this.doubleTime.toString());
/*  95 */     this.timeInput.setActionCommand("time");
/*  96 */     this.timeInput.setEditable(false);
/*  97 */     this.timeInput.setEnabled(false);
/*     */ 
/*     */     
/* 100 */     this.stepsButton.setSelected(true);
/* 101 */     this.stepsButton.setActionCommand("Steps");
/*     */     
/* 103 */     this.framesButton.setActionCommand("Frames");
/*     */     
/* 105 */     this.b.add(this.stepsButton);
/* 106 */     this.b.add(this.framesButton);
/*     */     
/* 108 */     this.steps_label = new JLabel("Number of animation steps:");
/* 109 */     this.fps_label = new JLabel("Frames per second");
/* 110 */     this.time_label = new JLabel("Animation time in seconds:");
/*     */ 
/*     */     
/* 113 */     this.stepsButton.addActionListener(this);
/* 114 */     this.framesButton.addActionListener(this);
/* 115 */     this.stepCount.addActionListener(this);
/* 116 */     this.fpsInput.addActionListener(this);
/* 117 */     this.timeInput.addActionListener(this);
/*     */     
/* 119 */     c.gridwidth = -1;
/* 120 */     addIntoGrid(this.stepsButton, grid, c);
/* 121 */     c.gridwidth = 0;
/*     */     
/* 123 */     addIntoGrid(this.framesButton, grid, c);
/* 124 */     c.weightx = 1.0D;
/* 125 */     c.gridwidth = -1;
/* 126 */     addIntoGrid(this.steps_label, grid, c);
/*     */ 
/*     */     
/* 129 */     c.gridwidth = 0;
/* 130 */     addIntoGrid(this.stepCount, grid, c);
/*     */     
/* 132 */     c.gridwidth = -1;
/* 133 */     addIntoGrid(this.fps_label, grid, c);
/*     */     
/* 135 */     c.gridwidth = 0;
/* 136 */     addIntoGrid(this.fpsInput, grid, c);
/* 137 */     c.gridwidth = -1;
/* 138 */     addIntoGrid(this.time_label, grid, c);
/*     */     
/* 140 */     c.gridwidth = 0;
/* 141 */     addIntoGrid(this.timeInput, grid, c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 153 */     if ("Steps".equals(e.getActionCommand())) {
/* 154 */       switchEditable(true);
/* 155 */       notifyChanged("RadioSteps");
/*     */     } 
/*     */     
/* 158 */     if ("Frames".equals(e.getActionCommand())) {
/* 159 */       switchEditable(false);
/* 160 */       notifyChanged("RadioFrames");
/*     */     } 
/* 162 */     if ("stepcount".equals(e.getActionCommand())) {
/* 163 */       int oldsteps = this.steps;
/*     */       try {
/* 165 */         int temp = Integer.parseInt(this.stepCount.getText());
/* 166 */         if (temp < 1) {
/* 167 */           this.stepCount.setText((new Integer(oldsteps)).toString());
/* 168 */           System.err.println("Only positive integer values >= 2 will be accepted!");
/*     */         } else {
/*     */           
/* 171 */           setSteps(temp);
/*     */         } 
/* 173 */       } catch (NumberFormatException nfe) {
/*     */         
/* 175 */         this.stepCount.setText((new Integer(oldsteps)).toString());
/* 176 */         System.err.println("input not recognised as integer - ignoring input!");
/*     */       } 
/*     */     } 
/*     */     
/* 180 */     if ("fps".equals(e.getActionCommand())) {
/*     */       try {
/* 182 */         int temp = Integer.parseInt(this.fpsInput.getText());
/* 183 */         if (temp < 1) {
/* 184 */           this.fpsInput.setText((new Integer(this.fps)).toString());
/* 185 */           System.err.println("At least 1 frameper second is required.");
/*     */         }
/* 187 */         else if (1 <= temp && temp <= 15) {
/* 188 */           setFps(temp);
/* 189 */         } else if (temp <= 25) {
/* 190 */           System.out.println("A framerate of " + temp + " frames per second might not work properly" + 
/* 191 */               " on some computers.");
/* 192 */           setFps(temp);
/*     */         } else {
/*     */           
/* 195 */           System.err.println("Framerate to high. Setting framerate to per 25 frames per second.");
/* 196 */           this.fpsInput.setText((new Integer(25)).toString());
/* 197 */           setFps(25);
/*     */         }
/*     */       
/*     */       }
/* 201 */       catch (NumberFormatException nfe) {
/* 202 */         this.fpsInput.setText((new Integer(this.fps)).toString());
/* 203 */         System.err.println("input not recognised as integer - ignoring input!");
/*     */       } 
/*     */     }
/*     */     
/* 207 */     if ("time".equals(e.getActionCommand())) {
/*     */       try {
/* 209 */         double temp = Double.parseDouble(this.timeInput.getText());
/* 210 */         if (temp <= 0.0D) {
/* 211 */           this.timeInput.setText((new Double(this.time)).toString());
/* 212 */           System.err.println("No negative animation time allowed.");
/*     */         }
/* 214 */         else if (temp > 0.0D && temp < 5.0D) {
/* 215 */           setTime(temp);
/*     */         }
/* 217 */         else if (temp <= 60.0D) {
/* 218 */           System.out.println("Using an animation time > than 15 seconds is not recommended.");
/* 219 */           setTime(temp);
/*     */         } else {
/*     */           
/* 222 */           System.err.println("Value for animation time too high, setting it to 60 seconds!");
/* 223 */           this.timeInput.setText((new Double(60.0D)).toString());
/* 224 */           setTime(60.0D);
/*     */         }
/*     */       
/*     */       }
/* 228 */       catch (NumberFormatException nfe) {
/* 229 */         this.timeInput.setText((new Double(this.time)).toString());
/* 230 */         System.err.println("input not recognised as double - ignoring input!");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getPanel() {
/* 245 */     return this.panel;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setSteps(int steps) {
/* 250 */     this.steps = steps;
/* 251 */     notifyChanged("steps");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSteps() {
/* 259 */     return this.steps;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setFps(int fps) {
/* 264 */     this.fps = fps;
/* 265 */     notifyChanged("fps");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFrames() {
/* 273 */     return this.fps;
/*     */   }
/*     */   
/*     */   private void setTime(double time) {
/* 277 */     this.time = time;
/* 278 */     notifyChanged("time");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTime() {
/* 286 */     return this.time;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notifyChanged(Object arg) {
/* 295 */     setChanged();
/* 296 */     notifyObservers(arg);
/*     */   }
/*     */   
/*     */   private void addIntoGrid(JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
/* 300 */     gridbag.setConstraints(comp, c);
/* 301 */     this.panel.add(comp);
/*     */   }
/*     */   
/*     */   private void switchEditable(boolean steps) {
/* 305 */     this.stepCount.setEditable(steps);
/* 306 */     this.stepCount.setEnabled(steps);
/* 307 */     this.steps_label.setEnabled(steps);
/* 308 */     this.fpsInput.setEditable(!steps);
/* 309 */     this.fpsInput.setEnabled(!steps);
/* 310 */     this.timeInput.setEditable(!steps);
/* 311 */     this.timeInput.setEnabled(!steps);
/* 312 */     this.fps_label.setEnabled(!steps);
/* 313 */     this.time_label.setEnabled(!steps);
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\RadioButtonPanel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */