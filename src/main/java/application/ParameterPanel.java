/*     */ package application;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
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
/*     */ public class ParameterPanel
/*     */   extends JPanel
/*     */ {
/*     */   private Controller controller;
/*     */   private ButtonGroup b;
/*     */   
/*     */   public ParameterPanel(Controller controller) {
/*  44 */     this.controller = controller;
/*  45 */     createComponents();
/*     */   }
/*     */ 
/*     */   
/*     */   private void createComponents() {
/*  50 */     JPanel tmp = new JPanel();
/*  51 */     tmp.setLayout(new BorderLayout());
/*  52 */     this.b = new ButtonGroup();
/*  53 */     JRadioButton simple_ros = new JRadioButton("Use uniformly sampled edges");
/*  54 */     JRadioButton normal_ros = new JRadioButton("Use next feature points as boundary");
/*  55 */     simple_ros.setActionCommand("simple_ros");
/*  56 */     normal_ros.setActionCommand("normal_ros");
/*  57 */     RosConstruction rc = new RosConstruction(this);
/*  58 */     simple_ros.doClick();
/*  59 */     simple_ros.addActionListener(rc);
/*  60 */     normal_ros.addActionListener(rc);
/*     */     
/*  62 */     this.b.add(simple_ros);
/*  63 */     this.b.add(normal_ros);
/*  64 */     tmp.add(simple_ros, "North");
/*  65 */     tmp.add(normal_ros, "South");
/*  66 */     tmp.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Method of ROS creation"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     JTextField skips = new JTextField(4);
/*  73 */     JLabel skips_label = new JLabel("Skips:");
/*  74 */     skips.setToolTipText("Enter the maximal number of skips allowed during the dp pathfinding process");
/*  75 */     skips.setText((new Integer(this.controller.getSkips() - 1)).toString());
/*  76 */     skips.addActionListener(new SkipsUpdate(this));
/*     */     
/*  78 */     JTextField ros_size = new JTextField(4);
/*  79 */     JLabel ros_size_label = new JLabel("Size of ROS:");
/*  80 */     ros_size.setToolTipText("Enter the total size of the Region of Support for a Feature Point");
/*  81 */     ros_size.setText((new Integer(this.controller.getROS_size())).toString());
/*  82 */     ros_size.addActionListener(new RosUpdate(this));
/*     */     
/*  84 */     JTextField sample_size = new JTextField(4);
/*  85 */     JLabel sample_size_label = new JLabel("Sample points:");
/*  86 */     sample_size.setToolTipText("Enter the number of sampling points between two poylgon vertices");
/*  87 */     sample_size.setText((new Integer(this.controller.getSample_rate())).toString());
/*  88 */     sample_size.addActionListener(new SampleUpdate(this));
/*     */     
/*  90 */     JLabel feature_var_label = new JLabel("Weight, Feature Variation:");
/*  91 */     JLabel feature_side_label = new JLabel("Weight, Feature Side Variation:");
/*  92 */     JLabel feature_size_label = new JLabel("Weight, Feature Size:");
/*  93 */     JLabel nummerator1 = new JLabel("                       1 /");
/*  94 */     JLabel nummerator2 = new JLabel("                       1 /");
/*  95 */     JLabel nummerator3 = new JLabel("                       1 /");
/*     */     
/*  97 */     String weight_label_tooltip = "Attention: Sum of all three weights must be equal 1.";
/*  98 */     nummerator1.setToolTipText(weight_label_tooltip);
/*  99 */     nummerator2.setToolTipText(weight_label_tooltip);
/* 100 */     nummerator3.setToolTipText(weight_label_tooltip);
/* 101 */     feature_var_label.setToolTipText(weight_label_tooltip);
/* 102 */     feature_side_label.setToolTipText(weight_label_tooltip);
/* 103 */     feature_size_label.setToolTipText(weight_label_tooltip);
/*     */     
/* 105 */     DenominatorUpdate du = new DenominatorUpdate(this);
/* 106 */     ExtendedController ec = (ExtendedController)this.controller;
/* 107 */     JTextField feature_var = new JTextField(4);
/* 108 */     feature_var.setToolTipText("Enter the denominator of the fraction, to change the weight for feature variation.\n Sum of all weights must be 1!");
/*     */     
/* 110 */     feature_var.setText((new Integer(ec.getFeature_var_denom())).toString());
/* 111 */     feature_var.setActionCommand("feature_var");
/* 112 */     feature_var.addActionListener(du);
/* 113 */     JTextField feature_side = new JTextField(4);
/* 114 */     feature_side.setToolTipText("Enter the denominator of the fraction, to change the weight for side feature variation.\n Sum of all weights must be 1!");
/*     */     
/* 116 */     feature_side.setText((new Integer(ec.getFeature_side_denom())).toString());
/* 117 */     feature_side.setActionCommand("feature_side");
/* 118 */     feature_side.addActionListener(du);
/* 119 */     JTextField feature_size = new JTextField(4);
/* 120 */     feature_size.setToolTipText("Enter the denominator of the fraction, to change the weight for feature size variation.\n Sum of all weights must be 1!");
/*     */     
/* 122 */     feature_size.setText((new Integer(ec.getFeature_size_denom())).toString());
/* 123 */     feature_size.setActionCommand("feature_size");
/* 124 */     feature_size.addActionListener(du);
/*     */     
/* 126 */     GridBagLayout gbl = new GridBagLayout();
/* 127 */     GridBagConstraints c = new GridBagConstraints();
/* 128 */     setLayout(gbl);
/* 129 */     c.gridwidth = 2;
/* 130 */     c.weightx = 1.0D;
/* 131 */     c.gridheight = 1;
/* 132 */     c.fill = 2;
/* 133 */     c.insets = new Insets(1, 1, 1, 1);
/* 134 */     c.gridx = 0;
/* 135 */     c.gridy = 0;
/* 136 */     addIntoGrid(tmp, gbl, c);
/* 137 */     c.gridy = 1;
/*     */     
/* 139 */     addIntoGrid(skips_label, gbl, c);
/* 140 */     c.gridx = 2;
/* 141 */     c.fill = 0;
/*     */     
/* 143 */     addIntoGrid(skips, gbl, c);
/* 144 */     c.fill = 2;
/* 145 */     c.gridx = 0;
/* 146 */     c.gridy = 2;
/*     */     
/* 148 */     addIntoGrid(ros_size_label, gbl, c);
/* 149 */     c.gridx = 2;
/* 150 */     c.fill = 0;
/*     */     
/* 152 */     addIntoGrid(ros_size, gbl, c);
/* 153 */     c.fill = 2;
/* 154 */     c.gridx = 0;
/* 155 */     c.gridy = 3;
/*     */     
/* 157 */     addIntoGrid(sample_size_label, gbl, c);
/* 158 */     c.gridx = 2;
/* 159 */     c.fill = 0;
/*     */     
/* 161 */     addIntoGrid(sample_size, gbl, c);
/* 162 */     c.gridx = 0;
/* 163 */     c.gridy = 4;
/* 164 */     c.fill = 2;
/*     */     
/* 166 */     c.gridwidth = 1;
/* 167 */     addIntoGrid(feature_var_label, gbl, c);
/*     */     
/* 169 */     c.gridx = 1;
/* 170 */     c.fill = 0;
/*     */     
/* 172 */     addIntoGrid(nummerator1, gbl, c);
/* 173 */     c.gridx = 2;
/* 174 */     addIntoGrid(feature_var, gbl, c);
/*     */     
/* 176 */     c.gridy = 5;
/* 177 */     c.gridx = 0;
/* 178 */     c.fill = 2;
/* 179 */     addIntoGrid(feature_side_label, gbl, c);
/* 180 */     c.fill = 0;
/* 181 */     c.gridx = 1;
/* 182 */     addIntoGrid(nummerator2, gbl, c);
/* 183 */     c.gridx = 2;
/* 184 */     addIntoGrid(feature_side, gbl, c);
/* 185 */     c.gridy = 6;
/* 186 */     c.gridx = 0;
/* 187 */     c.fill = 2;
/* 188 */     addIntoGrid(feature_size_label, gbl, c);
/* 189 */     c.fill = 0;
/* 190 */     c.gridx = 1;
/* 191 */     addIntoGrid(nummerator3, gbl, c);
/* 192 */     c.gridx = 2;
/* 193 */     addIntoGrid(feature_size, gbl, c);
/*     */     
/* 195 */     setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Parameters"));
/*     */   }
/*     */   private class SkipsUpdate implements ActionListener { final ParameterPanel this$0;
/*     */     SkipsUpdate(ParameterPanel this$0) {
/* 199 */       this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent e) {
/* 202 */       JTextField text = (JTextField)e.getSource();
/*     */       try {
/* 204 */         int skips = Integer.parseInt(text.getText());
/* 205 */         if (skips < 0) {
/* 206 */           System.out.println("Only integer values larger 0 are allowed for number of skips.");
/* 207 */           text.setText((new Integer(this.this$0.controller.getSkips() - 1)).toString());
/*     */         
/*     */         }
/* 210 */         else if (skips != this.this$0.controller.getSkips() - 1) {
/* 211 */           this.this$0.controller.setSkips(skips + 1);
/*     */         }
/*     */       
/*     */       }
/* 215 */       catch (NumberFormatException nfe) {
/* 216 */         System.err.println("input for skips not recognised as integer - ignoring input!");
/* 217 */         System.err.println(nfe.getStackTrace());
/* 218 */         text.setText((new Integer(this.this$0.controller.getSkips() - 1)).toString());
/*     */       } 
/*     */     } }
/*     */   
/*     */   private class RosConstruction implements ActionListener { RosConstruction(ParameterPanel this$0) {
/* 223 */       this.this$0 = this$0;
/*     */     } final ParameterPanel this$0;
/*     */     public void actionPerformed(ActionEvent e) {
/* 226 */       if (e.getActionCommand().equals("simple_ros")) {
/* 227 */         this.this$0.controller.setSimple_ROS(true);
/*     */       } else {
/* 229 */         this.this$0.controller.setSimple_ROS(false);
/*     */       } 
/*     */     } }
/*     */   private class RosUpdate implements ActionListener { final ParameterPanel this$0;
/*     */     RosUpdate(ParameterPanel this$0) {
/* 234 */       this.this$0 = this$0;
/*     */     } public void actionPerformed(ActionEvent e) {
/* 236 */       JTextField text = (JTextField)e.getSource();
/*     */       try {
/* 238 */         int sample_size = Integer.parseInt(text.getText());
/* 239 */         if (sample_size < 3) {
/* 240 */           System.out.println("Only integer values larger or equal to 3 are allowed for size of ros.");
/* 241 */           text.setText((new Integer(this.this$0.controller.getROS_size())).toString());
/*     */         
/*     */         }
/* 244 */         else if (sample_size != this.this$0.controller.getROS_size()) {
/* 245 */           this.this$0.controller.setROS_size(sample_size);
/*     */         }
/*     */       
/*     */       }
/* 249 */       catch (NumberFormatException nfe) {
/* 250 */         System.err.println("input for sample size not recognised as integer - ignoring input!");
/* 251 */         System.err.println(nfe.getStackTrace());
/* 252 */         text.setText((new Integer(this.this$0.controller.getROS_size())).toString());
/*     */       } 
/*     */     } }
/*     */   private class SampleUpdate implements ActionListener { final ParameterPanel this$0;
/*     */     
/*     */     SampleUpdate(ParameterPanel this$0) {
/* 258 */       this.this$0 = this$0;
/*     */     } public void actionPerformed(ActionEvent e) {
/* 260 */       JTextField text = (JTextField)e.getSource();
/*     */       try {
/* 262 */         int sample_rate = Integer.parseInt(text.getText());
/* 263 */         if (sample_rate < 1) {
/* 264 */           System.out.println("Only integer values larger 1 are allowed for sample rate.");
/* 265 */           text.setText((new Integer(this.this$0.controller.getSample_rate())).toString());
/*     */         
/*     */         }
/* 268 */         else if (sample_rate != this.this$0.controller.getSample_rate()) {
/* 269 */           this.this$0.controller.setSample_rate(sample_rate);
/*     */         }
/*     */       
/*     */       }
/* 273 */       catch (NumberFormatException nfe) {
/* 274 */         System.err.println("input for sample size not recognised as integer - ignoring input!");
/* 275 */         System.err.println(nfe.getStackTrace());
/* 276 */         text.setText((new Integer(this.this$0.controller.getSample_rate())).toString());
/*     */       } 
/*     */     } }
/*     */   private class DenominatorUpdate implements ActionListener { final ParameterPanel this$0;
/*     */     DenominatorUpdate(ParameterPanel this$0) {
/* 281 */       this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent e) {
/* 284 */       ExtendedController ec = (ExtendedController)this.this$0.controller;
/* 285 */       JTextField text = (JTextField)e.getSource();
/*     */       try {
/* 287 */         int denom = Integer.parseInt(text.getText());
/* 288 */         if (denom < 1) {
/* 289 */           System.out.println("Only integer values larger 1 are allowed as denominator.");
/* 290 */           text.setText((new Integer(this.this$0.controller.getSample_rate())).toString());
/*     */         
/*     */         }
/* 293 */         else if (e.getActionCommand().equals("feature_var")) {
/* 294 */           if (denom != ec.getFeature_var_denom()) {
/* 295 */             ec.setFeature_var_denom(denom);
/*     */           }
/* 297 */         } else if (e.getActionCommand().equals("feature_side")) {
/* 298 */           if (denom != ec.getFeature_side_denom()) {
/* 299 */             ec.setFeature_side_denom(denom);
/*     */           }
/* 301 */         } else if (e.getActionCommand().equals("feature_size") && 
/* 302 */           denom != ec.getFeature_size_denom()) {
/* 303 */           ec.setFeature_size_denom(denom);
/*     */         }
/*     */       
/*     */       }
/* 307 */       catch (NumberFormatException nfe) {
/* 308 */         System.err.println("input for denominator not recognised as integer - ignoring input!");
/* 309 */         System.err.println(nfe.getStackTrace());
/* 310 */         text.setText((new Integer(this.this$0.controller.getSample_rate())).toString());
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addIntoGrid(JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
/* 319 */     gridbag.setConstraints(comp, c);
/* 320 */     add(comp);
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\ParameterPanel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */