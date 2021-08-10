/*     */ package application;
/*     */ 
/*     */ import controls.LoadButtonListener;
/*     */ import controls.SaveButtonListener;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
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
/*     */ public class LoadSaveMenu
/*     */   extends JPanel
/*     */ {
/*     */   private ExtendedController ec;
/*     */   private JButton s_save;
/*     */   private JButton t_save;
/*     */   private JButton m_save;
/*     */   
/*     */   public LoadSaveMenu(ExtendedController ec) {
/*  46 */     this.ec = ec;
/*  47 */     createComponents();
/*  48 */     setEnableSource(false);
/*  49 */     setEnableTarget(false);
/*  50 */     setEnableMorph(false);
/*     */   }
/*     */   
/*     */   private void createComponents() {
/*  54 */     setLayout(new GridBagLayout());
/*  55 */     JPanel source_panel = new JPanel();
/*  56 */     JPanel target_panel = new JPanel();
/*  57 */     JPanel morph_panel = new JPanel();
/*  58 */     this.s_save = new JButton("save");
/*  59 */     this.s_save.setActionCommand("save_source");
/*  60 */     JButton s_load = new JButton("load");
/*  61 */     s_load.setActionCommand("load_source");
/*  62 */     this.t_save = new JButton("save");
/*  63 */     this.t_save.setActionCommand("save_target");
/*  64 */     JButton t_load = new JButton("load");
/*  65 */     t_load.setActionCommand("load_target");
/*  66 */     this.m_save = new JButton("save");
/*  67 */     this.m_save.setActionCommand("save_morph");
/*     */     
/*  69 */     GridBagLayout gbl = new GridBagLayout();
/*  70 */     GridBagConstraints c = new GridBagConstraints();
/*  71 */     source_panel.setLayout(gbl);
/*  72 */     target_panel.setLayout(gbl);
/*  73 */     morph_panel.setLayout(gbl);
/*  74 */     c.gridwidth = 2;
/*  75 */     c.weightx = 1.0D;
/*  76 */     c.gridheight = 1;
/*  77 */     c.fill = 2;
/*  78 */     c.insets = new Insets(1, 1, 1, 1);
/*  79 */     c.gridx = 0;
/*  80 */     c.gridy = 0;
/*  81 */     addIntoGrid(source_panel, s_load, gbl, c);
/*  82 */     addIntoGrid(target_panel, t_load, gbl, c);
/*     */     
/*  84 */     c.gridx = 2;
/*  85 */     addIntoGrid(source_panel, this.s_save, gbl, c);
/*  86 */     addIntoGrid(target_panel, this.t_save, gbl, c);
/*  87 */     c.gridx = 1;
/*  88 */     addIntoGrid(morph_panel, this.m_save, gbl, c);
/*     */     
/*  90 */     SaveButtonListener sbl = new SaveButtonListener(this.ec, null);
/*  91 */     this.s_save.addActionListener((ActionListener)sbl);
/*  92 */     this.t_save.addActionListener((ActionListener)sbl);
/*  93 */     this.m_save.addActionListener((ActionListener)sbl);
/*     */     
/*  95 */     LoadButtonListener lbl = new LoadButtonListener(this.ec);
/*  96 */     s_load.addActionListener((ActionListener)lbl);
/*  97 */     t_load.addActionListener((ActionListener)lbl);
/*     */ 
/*     */     
/* 100 */     source_panel.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Source"));
/* 101 */     target_panel.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Target"));
/* 102 */     morph_panel.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Morph"));
/* 103 */     GridBagLayout g = new GridBagLayout();
/* 104 */     setLayout(g);
/* 105 */     c.gridy = 0;
/* 106 */     addIntoGrid(this, source_panel, g, c);
/* 107 */     c.gridy = 1;
/* 108 */     addIntoGrid(this, target_panel, g, c);
/* 109 */     c.gridy = 2;
/* 110 */     addIntoGrid(this, morph_panel, g, c);
/*     */ 
/*     */ 
/*     */     
/* 114 */     setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Load / Save Menu"));
/*     */   }
/*     */   
/*     */   private static void addIntoGrid(JPanel panel, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
/* 118 */     gridbag.setConstraints(comp, c);
/* 119 */     panel.add(comp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableSource(boolean enabled) {
/* 127 */     this.s_save.setEnabled(enabled);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableTarget(boolean enabled) {
/* 135 */     this.t_save.setEnabled(enabled);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableMorph(boolean enabled) {
/* 143 */     this.m_save.setEnabled(enabled);
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\application\LoadSaveMenu.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */