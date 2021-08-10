package application;

import controls.LoadButtonListener;
import controls.SaveButtonListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import tools.Constants;

public class LoadSaveMenu extends JPanel {
  private ExtendedController ec;
  
  private JButton s_save;
  
  private JButton t_save;
  
  private JButton m_save;
  
  public LoadSaveMenu(ExtendedController ec) {
    this.ec = ec;
    createComponents();
    setEnableSource(false);
    setEnableTarget(false);
    setEnableMorph(false);
  }
  
  private void createComponents() {
    setLayout(new GridBagLayout());
    JPanel source_panel = new JPanel();
    JPanel target_panel = new JPanel();
    JPanel morph_panel = new JPanel();
    this.s_save = new JButton("save");
    this.s_save.setActionCommand("save_source");
    JButton s_load = new JButton("load");
    s_load.setActionCommand("load_source");
    this.t_save = new JButton("save");
    this.t_save.setActionCommand("save_target");
    JButton t_load = new JButton("load");
    t_load.setActionCommand("load_target");
    this.m_save = new JButton("save");
    this.m_save.setActionCommand("save_morph");
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    source_panel.setLayout(gbl);
    target_panel.setLayout(gbl);
    morph_panel.setLayout(gbl);
    c.gridwidth = 2;
    c.weightx = 1.0D;
    c.gridheight = 1;
    c.fill = 2;
    c.insets = new Insets(1, 1, 1, 1);
    c.gridx = 0;
    c.gridy = 0;
    addIntoGrid(source_panel, s_load, gbl, c);
    addIntoGrid(target_panel, t_load, gbl, c);
    c.gridx = 2;
    addIntoGrid(source_panel, this.s_save, gbl, c);
    addIntoGrid(target_panel, this.t_save, gbl, c);
    c.gridx = 1;
    addIntoGrid(morph_panel, this.m_save, gbl, c);
    SaveButtonListener sbl = new SaveButtonListener(this.ec, null);
    this.s_save.addActionListener((ActionListener)sbl);
    this.t_save.addActionListener((ActionListener)sbl);
    this.m_save.addActionListener((ActionListener)sbl);
    LoadButtonListener lbl = new LoadButtonListener(this.ec);
    s_load.addActionListener((ActionListener)lbl);
    t_load.addActionListener((ActionListener)lbl);
    source_panel.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Source"));
    target_panel.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Target"));
    morph_panel.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Morph"));
    GridBagLayout g = new GridBagLayout();
    setLayout(g);
    c.gridy = 0;
    addIntoGrid(this, source_panel, g, c);
    c.gridy = 1;
    addIntoGrid(this, target_panel, g, c);
    c.gridy = 2;
    addIntoGrid(this, morph_panel, g, c);
    setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Load / Save Menu"));
  }
  
  private static void addIntoGrid(JPanel panel, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
    gridbag.setConstraints(comp, c);
    panel.add(comp);
  }
  
  public void setEnableSource(boolean enabled) {
    this.s_save.setEnabled(enabled);
  }
  
  public void setEnableTarget(boolean enabled) {
    this.t_save.setEnabled(enabled);
  }
  
  public void setEnableMorph(boolean enabled) {
    this.m_save.setEnabled(enabled);
  }
}
