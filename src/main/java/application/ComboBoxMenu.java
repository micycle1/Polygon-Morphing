/*     */ package application;
/*     */ 
/*     */ import controls.LoadButtonListener;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import shapes.Polygon;
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
/*     */ public class ComboBoxMenu
/*     */   extends JPanel
/*     */   implements ActionListener
/*     */ {
/*     */   private JComboBox sourceBox;
/*     */   private JComboBox targetBox;
/*     */   private ExtendedController ec;
/*     */   private LoadButtonListener lb;
/*     */   
/*     */   public ComboBoxMenu(ExtendedController ec) {
/*  51 */     this.ec = ec;
/*  52 */     this.lb = new LoadButtonListener(null);
/*  53 */     createComponents();
/*     */   }
/*     */   
/*     */   private void createComponents() {
/*  57 */     String[] polygon_types = { "", "5-gon", "5-gon with noise", "7-gon", "13-gon", "C", 
/*  58 */         "table", "tortoise", "unicorn1", "unicorn2", "3 uprisings", "4 uprisings" };
/*  59 */     this.sourceBox = new JComboBox(polygon_types);
/*  60 */     this.targetBox = new JComboBox(polygon_types);
/*  61 */     this.sourceBox.setActionCommand("new_source");
/*  62 */     this.targetBox.setActionCommand("new_target");
/*  63 */     JLabel source = new JLabel("source");
/*  64 */     JLabel target = new JLabel("target");
/*  65 */     GridBagLayout gbl = new GridBagLayout();
/*  66 */     GridBagConstraints c = new GridBagConstraints();
/*  67 */     setLayout(gbl);
/*  68 */     c.gridwidth = 2;
/*  69 */     c.weightx = 1.0D;
/*  70 */     c.gridheight = 1;
/*  71 */     c.fill = 2;
/*  72 */     c.insets = new Insets(1, 1, 1, 1);
/*  73 */     c.gridx = 0;
/*  74 */     c.gridy = 0;
/*  75 */     addIntoGrid(this, source, gbl, c);
/*  76 */     c.gridx = 2;
/*  77 */     addIntoGrid(this, target, gbl, c);
/*  78 */     c.gridy = 2;
/*  79 */     c.gridx = 0;
/*  80 */     addIntoGrid(this, this.sourceBox, gbl, c);
/*  81 */     c.gridx = 2;
/*  82 */     addIntoGrid(this, this.targetBox, gbl, c);
/*     */ 
/*     */     
/*  85 */     this.sourceBox.addActionListener(this);
/*  86 */     this.targetBox.addActionListener(this);
/*     */     
/*  88 */     setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Choose Polygons"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*     */     Model m;
/*  96 */     if (e.getActionCommand().equals("new_source")) {
/*  97 */       m = this.ec.getOriginal().getModel();
/*     */     } else {
/*  99 */       m = this.ec.getTarget().getModel();
/* 100 */     }  String polygon_string = (String)((JComboBox)e.getSource()).getSelectedItem();
/* 101 */     Polygon p = new Polygon();
/* 102 */     StringBuffer buff = new StringBuffer();
/*     */     
/* 104 */     if (polygon_string.equals("")) {
/* 105 */       m.reset();
/*     */     } else {
/*     */       String ressource;
/* 108 */       if (polygon_string.equals("4 uprisings")) {
/* 109 */         buff.append("4_uprisings.2pd");
/* 110 */       } else if (polygon_string.equals("5-gon")) {
/* 111 */         buff.append("5-gon.2dp");
/* 112 */       } else if (polygon_string.equals("5-gon with noise")) {
/* 113 */         buff.append("5-gon_with_noise.2dp");
/* 114 */       } else if (polygon_string.equals("7-gon")) {
/* 115 */         buff.append("7-gon.2dp");
/* 116 */       } else if (polygon_string.equals("tortoise")) {
/* 117 */         buff.append("tortoise.2dp");
/* 118 */       } else if (polygon_string.equals("table")) {
/* 119 */         buff.append("table.2dp");
/* 120 */       } else if (polygon_string.equals("unicorn1")) {
/* 121 */         buff.append("unicorn1.2dp");
/* 122 */       } else if (polygon_string.equals("unicorn2")) {
/* 123 */         buff.append("unicorn2.2dp");
/* 124 */       } else if (polygon_string.equals("13-gon")) {
/* 125 */         buff.append("13-gon.2dp");
/* 126 */       } else if (polygon_string.equals("C")) {
/* 127 */         buff.append("C.2dp");
/*     */       } else {
/* 129 */         buff.append("3_uprisings.2pd");
/*     */       } 
/* 131 */       String pathname = buff.toString();
/*     */ 
/*     */       
/*     */       try {
/* 135 */         ressource = loadTextResource("polygons", pathname);
/*     */       }
/* 137 */       catch (IOException io) {
/* 138 */         ressource = null;
/* 139 */         System.err.println(io.toString());
/*     */       } 
/* 141 */       this.lb.readPolygonFile(m, ressource, p);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetSource(Object o) {
/* 151 */     this.sourceBox.setSelectedIndex(0);
/* 152 */     this.ec.getOriginal().getModel().append(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTarget(Object o) {
/* 162 */     this.targetBox.setSelectedIndex(0);
/* 163 */     this.ec.getTarget().getModel().append(o);
/*     */   }
/*     */   
/*     */   private static void addIntoGrid(JPanel panel, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
/* 167 */     gridbag.setConstraints(comp, c);
/* 168 */     panel.add(comp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String loadTextResource(String pkgname, String file_name) throws IOException {
/* 179 */     String ret = null;
/* 180 */     InputStream is = getResourceStream(pkgname, file_name);
/* 181 */     if (is != null) {
/* 182 */       StringBuffer buff = new StringBuffer();
/*     */       while (true) {
/* 184 */         int c = is.read();
/* 185 */         if (c == -1) {
/*     */           break;
/*     */         }
/* 188 */         buff.append((char)c);
/*     */       } 
/* 190 */       is.close();
/* 191 */       ret = buff.toString();
/*     */     } 
/* 193 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   private InputStream getResourceStream(String pkgname, String file_name) {
/* 198 */     String resname = "/" + pkgname.replace('.', '/') + "/" + file_name;
/* 199 */     Class clazz = getClass();
/* 200 */     InputStream is = clazz.getResourceAsStream(resname);
/* 201 */     return is;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\ComboBoxMenu.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */