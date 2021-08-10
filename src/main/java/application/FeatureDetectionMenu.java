/*     */ package application;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.GridLayout;
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
/*     */ 
/*     */ 
/*     */ public class FeatureDetectionMenu
/*     */   extends JPanel
/*     */   implements ActionListener
/*     */ {
/*     */   private ExtendedController ec;
/*     */   private JPanel parameters;
/*     */   private JTextField max_angle;
/*     */   private JTextField min_length;
/*     */   private JTextField max_fp;
/*     */   private JRadioButton nolimit;
/*     */   private JRadioButton limit;
/*     */   private JLabel max_angle_label;
/*     */   private JLabel min_length_label;
/*     */   private JLabel max_fp_label;
/*     */   
/*     */   public FeatureDetectionMenu(ExtendedController ec) {
/*  58 */     this.ec = ec;
/*  59 */     createMenu();
/*  60 */     diasableContent(this.parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createMenu() {
/*  67 */     setLayout(new BorderLayout());
/*  68 */     ButtonGroup featureDetection = new ButtonGroup();
/*  69 */     JPanel checkbox_panel = new JPanel();
/*  70 */     checkbox_panel.setLayout(new GridLayout(2, 1));
/*  71 */     JRadioButton on = new JRadioButton("on", false);
/*  72 */     JRadioButton off = new JRadioButton("off", true);
/*  73 */     on.setActionCommand("on");
/*  74 */     off.setActionCommand("off");
/*  75 */     on.addActionListener(this);
/*  76 */     off.addActionListener(this);
/*     */     
/*  78 */     featureDetection.add(on);
/*  79 */     featureDetection.add(off);
/*     */     
/*  81 */     checkbox_panel.add(on);
/*  82 */     checkbox_panel.add(off);
/*     */     
/*  84 */     checkbox_panel.setBorder(Constants.BLACKLINE);
/*  85 */     add(checkbox_panel, "West");
/*     */     
/*  87 */     this.max_angle = new JTextField(4);
/*  88 */     this.max_angle_label = new JLabel("Maximal angle:");
/*  89 */     this.max_angle.setToolTipText("Enter the maximal inner/outer angle for a vailid feature point");
/*  90 */     this.max_angle.setText((new Double(this.ec.getMax_angle())).toString());
/*  91 */     this.max_angle.addActionListener(new MaxAngleUpdate(this));
/*     */     
/*  93 */     this.min_length = new JTextField(4);
/*  94 */     this.min_length_label = new JLabel("Minmal length:");
/*  95 */     this.min_length.setToolTipText("Enter the minimal relative length of the shape at a feature point");
/*  96 */     this.min_length.setText((new Double(this.ec.getMin_length())).toString());
/*  97 */     this.min_length.addActionListener(new MinLengthUpdate(this));
/*     */     
/*  99 */     this.max_fp = new JTextField(4);
/* 100 */     this.max_fp_label = new JLabel("Max Feature Points:");
/* 101 */     this.max_fp.setToolTipText("Enter the maximal number of feature points for a shape");
/* 102 */     this.max_fp.setText((new Integer(this.ec.getMax_featurePoints())).toString());
/* 103 */     this.max_fp.addActionListener(new MaxFPUpdate(this));
/*     */     
/* 105 */     ButtonGroup b = new ButtonGroup();
/*     */     
/* 107 */     this.nolimit = new JRadioButton("Using Parameters", true);
/* 108 */     this.limit = new JRadioButton("Using Max");
/* 109 */     this.nolimit.setActionCommand("nolimit");
/* 110 */     this.limit.setActionCommand("limit");
/* 111 */     Chooser chooser = new Chooser(this);
/* 112 */     this.nolimit.addActionListener(chooser);
/* 113 */     this.limit.addActionListener(chooser);
/*     */     
/* 115 */     b.add(this.nolimit);
/* 116 */     b.add(this.limit);
/*     */ 
/*     */ 
/*     */     
/* 120 */     JPanel fdp = new JPanel();
/* 121 */     GridBagLayout gbl = new GridBagLayout();
/* 122 */     GridBagConstraints c = new GridBagConstraints();
/* 123 */     fdp.setLayout(gbl);
/* 124 */     c.gridwidth = 2;
/* 125 */     c.weightx = 1.0D;
/* 126 */     c.gridheight = 1;
/* 127 */     c.fill = 2;
/* 128 */     c.insets = new Insets(1, 1, 1, 1);
/* 129 */     c.gridx = 2;
/* 130 */     c.gridy = 0;
/* 131 */     addIntoGrid(fdp, this.max_angle_label, gbl, c);
/* 132 */     c.gridx = 4;
/* 133 */     addIntoGrid(fdp, this.max_angle, gbl, c);
/* 134 */     c.gridx = 2;
/* 135 */     c.gridy = 1;
/* 136 */     addIntoGrid(fdp, this.min_length_label, gbl, c);
/* 137 */     c.gridx = 4;
/* 138 */     addIntoGrid(fdp, this.min_length, gbl, c);
/* 139 */     c.gridx = 0;
/* 140 */     c.gridy = 0;
/* 141 */     addIntoGrid(fdp, this.nolimit, gbl, c);
/* 142 */     c.gridy = 1;
/* 143 */     addIntoGrid(fdp, this.limit, gbl, c);
/* 144 */     c.gridx = 2;
/* 145 */     c.gridy = 3;
/* 146 */     addIntoGrid(fdp, this.max_fp_label, gbl, c);
/* 147 */     c.gridx = 4;
/* 148 */     addIntoGrid(fdp, this.max_fp, gbl, c);
/*     */     
/* 150 */     add(fdp, "Center");
/*     */     
/* 152 */     this.parameters = fdp;
/* 153 */     setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "FeaturePoint Detection"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedController getController() {
/* 163 */     return this.ec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 174 */     if (e.getActionCommand().equals("on")) {
/* 175 */       this.nolimit.setEnabled(true);
/* 176 */       this.limit.setEnabled(true);
/* 177 */       if (this.nolimit.isSelected()) {
/* 178 */         this.nolimit.doClick();
/*     */       } else {
/*     */         
/* 181 */         this.limit.doClick();
/* 182 */       }  this.ec.setFeatureDetection(true);
/*     */     }
/* 184 */     else if (e.getActionCommand().equals("off")) {
/* 185 */       diasableContent(this.parameters);
/* 186 */       this.ec.setFeatureDetection(false);
/*     */     } else {
/*     */       
/* 189 */       throw new IllegalArgumentException("Someting went horribly wrong...");
/*     */     } 
/*     */   } private class MaxAngleUpdate implements ActionListener { MaxAngleUpdate(FeatureDetectionMenu this$0) {
/* 192 */       this.this$0 = this$0;
/*     */     } final FeatureDetectionMenu this$0;
/*     */     public void actionPerformed(ActionEvent e) {
/* 195 */       JTextField text = (JTextField)e.getSource();
/*     */       try {
/* 197 */         double angle = Double.parseDouble(text.getText());
/* 198 */         if (angle <= 0.0D) {
/* 199 */           System.out.println("Only double values larger 0 are allowed as max angle.");
/* 200 */           text.setText((new Double(this.this$0.ec.getMax_angle())).toString());
/*     */         }
/* 202 */         else if (angle >= 180.0D) {
/* 203 */           System.out.println("Only double values smaller 180 are allowed as max angle.");
/* 204 */           text.setText((new Double(this.this$0.ec.getMax_angle())).toString());
/*     */         
/*     */         }
/* 207 */         else if (angle != this.this$0.ec.getMax_angle()) {
/* 208 */           this.this$0.ec.setMax_angle(angle);
/*     */         }
/*     */       
/*     */       }
/* 212 */       catch (NumberFormatException nfe) {
/* 213 */         System.err.println("input for angle not recognised as double - ignoring input!");
/* 214 */         System.err.println(nfe.getStackTrace());
/* 215 */         text.setText((new Double(this.this$0.ec.getMax_angle())).toString());
/*     */       } 
/*     */     } }
/*     */   private class MinLengthUpdate implements ActionListener { final FeatureDetectionMenu this$0;
/*     */     MinLengthUpdate(FeatureDetectionMenu this$0) {
/* 220 */       this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent e) {
/* 223 */       JTextField text = (JTextField)e.getSource();
/*     */       try {
/* 225 */         double length = Double.parseDouble(text.getText());
/* 226 */         if (length < 0.0D) {
/* 227 */           System.out.println("Only double values larger or equal to 0 are allowed as max angle.");
/* 228 */           text.setText((new Double(this.this$0.ec.getMin_length())).toString());
/*     */         }
/* 230 */         else if (length >= 1.0D) {
/* 231 */           System.out.println("Only double values smaller 1 are allowed as max angle.");
/* 232 */           text.setText((new Double(this.this$0.ec.getMin_length())).toString());
/*     */         
/*     */         }
/* 235 */         else if (length != this.this$0.ec.getMin_length()) {
/* 236 */           this.this$0.ec.setMin_length(length);
/*     */         }
/*     */       
/*     */       }
/* 240 */       catch (NumberFormatException nfe) {
/* 241 */         System.err.println("input for min length not recognised as double - ignoring input!");
/* 242 */         System.err.println(nfe.getStackTrace());
/* 243 */         text.setText((new Double(this.this$0.ec.getMin_length())).toString());
/*     */       } 
/*     */     } }
/*     */   private class MaxFPUpdate implements ActionListener { final FeatureDetectionMenu this$0;
/*     */     MaxFPUpdate(FeatureDetectionMenu this$0) {
/* 248 */       this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent e) {
/* 251 */       JTextField text = (JTextField)e.getSource();
/*     */       try {
/* 253 */         int max_fp = Integer.parseInt(text.getText());
/* 254 */         if (max_fp < 1) {
/* 255 */           System.out.println("Only integer values larger 0 are allowed for number of skips.");
/* 256 */           text.setText((new Integer(this.this$0.ec.getMax_featurePoints())).toString());
/*     */         }
/* 258 */         else if (max_fp > 100) {
/* 259 */           System.out.println("For runtime reasons maximal 100 feature points per polygon are allowed.");
/* 260 */           text.setText((new Integer(this.this$0.ec.getMax_featurePoints())).toString());
/*     */         
/*     */         }
/* 263 */         else if (max_fp != this.this$0.ec.getMax_featurePoints()) {
/* 264 */           this.this$0.ec.setMax_featurePoints(max_fp);
/*     */         }
/*     */       
/*     */       }
/* 268 */       catch (NumberFormatException nfe) {
/* 269 */         System.err.println("input for max feature points not recognised as integer - ignoring input!");
/* 270 */         System.err.println(nfe.getStackTrace());
/* 271 */         text.setText((new Integer(this.this$0.ec.getMax_featurePoints())).toString());
/*     */       } 
/*     */     } }
/*     */   private class Chooser implements ActionListener { final FeatureDetectionMenu this$0;
/*     */     Chooser(FeatureDetectionMenu this$0) {
/* 276 */       this.this$0 = this$0;
/*     */     }
/*     */     public void actionPerformed(ActionEvent e) {
/* 279 */       if (e.getActionCommand().equals("nolimit")) {
/* 280 */         switchContentes(true);
/*     */       
/*     */       }
/* 283 */       else if (e.getActionCommand().equals("limit")) {
/* 284 */         switchContentes(false);
/*     */       } else {
/*     */         
/* 287 */         throw new IllegalArgumentException("Problem with Actionlistener for FeatureDetection Mode");
/*     */       } 
/*     */     }
/*     */     private void switchContentes(boolean nolimit) {
/* 291 */       this.this$0.ec.setLimit(!nolimit);
/* 292 */       this.this$0.max_fp.setEnabled(!nolimit);
/* 293 */       this.this$0.max_fp.setEditable(!nolimit);
/* 294 */       this.this$0.max_fp_label.setEnabled(!nolimit);
/* 295 */       this.this$0.max_angle.setEnabled(nolimit);
/* 296 */       this.this$0.max_angle.setEditable(nolimit);
/* 297 */       this.this$0.min_length.setEnabled(nolimit);
/* 298 */       this.this$0.min_length.setEnabled(nolimit);
/* 299 */       this.this$0.max_angle_label.setEnabled(nolimit);
/* 300 */       this.this$0.min_length_label.setEnabled(nolimit);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addIntoGrid(JPanel container, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
/* 307 */     gridbag.setConstraints(comp, c);
/* 308 */     container.add(comp);
/*     */   }
/*     */   
/*     */   private void diasableContent(JPanel p) {
/* 312 */     Component[] comps = p.getComponents();
/* 313 */     for (int i = 0; i < comps.length; i++)
/* 314 */       comps[i].setEnabled(false); 
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\FeatureDetectionMenu.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */