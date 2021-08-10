package micycle.polygonmorphing.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import micycle.polygonmorphing.application.ExtendedController;
import micycle.polygonmorphing.application.Model;
import micycle.polygonmorphing.shapes.Point;
import micycle.polygonmorphing.shapes.Polygon;

public class LoadButtonListener implements ActionListener {
	private ExtendedController ec;

	public LoadButtonListener(ExtendedController ec) {
		this.ec = ec;
	}

	public void actionPerformed(ActionEvent e) {
		Model model = e.getActionCommand().equals("load_source") ? this.ec.getOriginal().getModel() : this.ec.getTarget().getModel();
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileFilter() {

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				String fileExtension = LoadButtonListener.this.getExtension(f);
				return fileExtension != null && fileExtension.equals("2dp");
			}

			public String getDescription() {
				return "*.2dp-files";
			}
		});
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == 0) {
			File f = fc.getSelectedFile();
			Polygon polygon = new Polygon();
			this.readPolygonFile(model, f, polygon);
		}
	}

	public void readPolygonFile(Model model, File f, Polygon polygon) {
		try {
			boolean closed;
			BufferedReader buff = new BufferedReader(new FileReader(f));
			String line = buff.readLine();
			if (!line.equals("2D-Polygon for Morphing")) {
				throw new IllegalArgumentException("Wrong file format");
			}
			buff.readLine();
			buff.readLine();
			line = buff.readLine();
			if (line.equals("is closed")) {
				closed = true;
			} else if (line.equals("is not closed")) {
				closed = false;
			} else {
				throw new IllegalArgumentException("Wrong file format");
			}
			buff.readLine();
			while ((line = buff.readLine()) != null) {
				String[] point_rep = line.split(",");
				Point p = new Point(new Integer(point_rep[0]), new Integer(point_rep[1]));
				polygon.addVertex(p);
			}
			if (closed) {
				polygon.close();
			}
			model.append(polygon);
		} catch (IOException e1) {
			System.err.println("Failure during FileReading-process");
			e1.printStackTrace();
		}
	}

	public void readPolygonFile(Model model, String poly_string, Polygon polygon) {
		boolean closed;
		String[] lines = poly_string.split("\n");
		if (!lines[0].equals("2D-Polygon for Morphing")) {
			throw new IllegalArgumentException("Wrong file format");
		}
		if (lines[3].equals("is closed")) {
			closed = true;
		} else if (lines[3].equals("is not closed")) {
			closed = false;
		} else {
			throw new IllegalArgumentException("Wrong file format");
		}
		for (int i = 5; i < lines.length; ++i) {
			String[] point_rep = lines[i].split(",");
			Point p = new Point(new Integer(point_rep[0]), new Integer(point_rep[1]));
			polygon.addVertex(p);
		}
		if (closed) {
			polygon.close();
		}
		model.append(polygon);
	}

	private String getExtension(File f) {
		String ext = null;
		String fileName = f.getName();
		int i = fileName.lastIndexOf(46);
		if (i > 0 && i < fileName.length() - 1) {
			ext = fileName.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}
