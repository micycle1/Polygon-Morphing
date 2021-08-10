package micycle.polygonmorphing.application;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import micycle.polygonmorphing.controls.LoadButtonListener;
import micycle.polygonmorphing.shapes.Polygon;
import micycle.polygonmorphing.tools.Constants;

public class ComboBoxMenu extends JPanel implements ActionListener {
	private JComboBox sourceBox;
	private JComboBox targetBox;
	private ExtendedController ec;
	private LoadButtonListener lb;

	public ComboBoxMenu(ExtendedController ec) {
		this.ec = ec;
		this.lb = new LoadButtonListener(null);
		this.createComponents();
	}

	private void createComponents() {
		String[] polygon_types = new String[] { "", "5-gon", "5-gon with noise", "7-gon", "13-gon", "C", "table", "tortoise", "unicorn1",
				"unicorn2", "3 uprisings", "4 uprisings" };
		this.sourceBox = new JComboBox<String>(polygon_types);
		this.targetBox = new JComboBox<String>(polygon_types);
		this.sourceBox.setActionCommand("new_source");
		this.targetBox.setActionCommand("new_target");
		JLabel source = new JLabel("source");
		JLabel target = new JLabel("target");
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gbl);
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.gridheight = 1;
		c.fill = 2;
		c.insets = new Insets(1, 1, 1, 1);
		c.gridx = 0;
		c.gridy = 0;
		ComboBoxMenu.addIntoGrid(this, source, gbl, c);
		c.gridx = 2;
		ComboBoxMenu.addIntoGrid(this, target, gbl, c);
		c.gridy = 2;
		c.gridx = 0;
		ComboBoxMenu.addIntoGrid(this, this.sourceBox, gbl, c);
		c.gridx = 2;
		ComboBoxMenu.addIntoGrid(this, this.targetBox, gbl, c);
		this.sourceBox.addActionListener(this);
		this.targetBox.addActionListener(this);
		this.setBorder(BorderFactory.createTitledBorder(Constants.BLACKLINE, "Choose Polygons"));
	}

	public void actionPerformed(ActionEvent e) {
		Model m = e.getActionCommand().equals("new_source") ? this.ec.getOriginal().getModel() : this.ec.getTarget().getModel();
		String polygon_string = (String) ((JComboBox) e.getSource()).getSelectedItem();
		Polygon p = new Polygon();
		StringBuffer buff = new StringBuffer();
		if (polygon_string.equals("")) {
			m.reset();
		} else {
			String ressource;
			if (polygon_string.equals("4 uprisings")) {
				buff.append("4_uprisings.2pd");
			} else if (polygon_string.equals("5-gon")) {
				buff.append("5-gon.2dp");
			} else if (polygon_string.equals("5-gon with noise")) {
				buff.append("5-gon_with_noise.2dp");
			} else if (polygon_string.equals("7-gon")) {
				buff.append("7-gon.2dp");
			} else if (polygon_string.equals("tortoise")) {
				buff.append("tortoise.2dp");
			} else if (polygon_string.equals("table")) {
				buff.append("table.2dp");
			} else if (polygon_string.equals("unicorn1")) {
				buff.append("unicorn1.2dp");
			} else if (polygon_string.equals("unicorn2")) {
				buff.append("unicorn2.2dp");
			} else if (polygon_string.equals("13-gon")) {
				buff.append("13-gon.2dp");
			} else if (polygon_string.equals("C")) {
				buff.append("C.2dp");
			} else {
				buff.append("3_uprisings.2pd");
			}
			String pathname = buff.toString();
			try {
				ressource = this.loadTextResource("polygons", pathname);
			} catch (IOException io) {
				ressource = null;
				System.err.println(io.toString());
			}
			this.lb.readPolygonFile(m, ressource, p);
		}
	}

	public void resetSource(Object o) {
		this.sourceBox.setSelectedIndex(0);
		this.ec.getOriginal().getModel().append(o);
	}

	public void resetTarget(Object o) {
		this.targetBox.setSelectedIndex(0);
		this.ec.getTarget().getModel().append(o);
	}

	private static void addIntoGrid(JPanel panel, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
		gridbag.setConstraints(comp, c);
		panel.add(comp);
	}

	private String loadTextResource(String pkgname, String file_name) throws IOException {
		String ret = null;
		InputStream is = this.getResourceStream(pkgname, file_name);
		if (is != null) {
			int c;
			StringBuffer buff = new StringBuffer();
			while ((c = is.read()) != -1) {
				buff.append((char) c);
			}
			is.close();
			ret = buff.toString();
		}
		return ret;
	}

	private InputStream getResourceStream(String pkgname, String file_name) {
		String resname = "/" + pkgname.replace('.', '/') + "/" + file_name;
		Class<?> clazz = this.getClass();
		InputStream is = clazz.getResourceAsStream(resname);
		return is;
	}
}
