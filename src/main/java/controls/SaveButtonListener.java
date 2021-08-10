package controls;

import application.ExtendedController;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import shapes.Polygon;

public class SaveButtonListener implements ActionListener {
  private ExtendedController ec;
  
  private Component parent;
  
  public SaveButtonListener(ExtendedController ec, Component parent) {
    this.ec = ec;
    this.parent = parent;
  }
  
  public void actionPerformed(ActionEvent e) {
    String actionCommand = e.getActionCommand();
    JFileChooser fc = new JFileChooser();
    if (e.getActionCommand().equals("save_morph")) {
      fc.setDialogTitle("save morph as svg");
      fc.setApproveButtonToolTipText("export the morphing sequence into an svg file");
    } else {
      fc.setDialogTitle("save polygon");
      fc.setApproveButtonToolTipText("export the polygon to svg or 2pd");
    } 
    fc.setApproveButtonText("save");
    fc.addChoosableFileFilter(new FileFilter(this) {
          final SaveButtonListener this$0;
          
          public boolean accept(File f) {
            if (f.isDirectory())
              return true; 
            String fileExtension = this.this$0.getExtension(f);
            if (fileExtension != null && fileExtension.equals("svg"))
              return true; 
            return false;
          }
          
          public String getDescription() {
            return "*.svg-files";
          }
        });
    if (!actionCommand.equals("save_morph")) {
      fc.addChoosableFileFilter(new FileFilter(this) {
            final SaveButtonListener this$0;
            
            public boolean accept(File f) {
              if (f.isDirectory())
                return true; 
              String fileExtension = this.this$0.getExtension(f);
              if ((((fileExtension != null) ? 1 : 0) & fileExtension.equals("2dp")) != 0)
                return true; 
              return false;
            }
            
            public String getDescription() {
              return "*.2dp-files";
            }
          });
      fc.addChoosableFileFilter(new FileFilter(this) {
            final SaveButtonListener this$0;
            
            public boolean accept(File f) {
              if (f.isDirectory())
                return true; 
              String fileExtension = this.this$0.getExtension(f);
              if (fileExtension != null && (
                fileExtension.equals("svg") || fileExtension.equals("2dp")))
                return true; 
              return false;
            }
            
            public String getDescription() {
              return "*.svg, *.2dp";
            }
          });
    } 
    fc.setAcceptAllFileFilterUsed(false);
    int returnVal = fc.showSaveDialog(this.parent);
    if (returnVal == 0) {
      File f = fc.getSelectedFile();
      String extension = fc.getFileFilter().getDescription();
      if (extension.equals("*.svg, *.2dp")) {
        extension = "svg, 2dp";
      } else if (extension.equals("*.svg-files")) {
        extension = "svg";
      } else {
        extension = "2dp";
      } 
      if (actionCommand.equals("save_morph")) {
        try {
          File file = changeFileExtension(f, "svg");
          FileWriter writer = new FileWriter(file);
          writer.write(this.ec.toSVG());
          writer.flush();
          writer.close();
        } catch (IOException e1) {
          System.err.println("Failure during FileWriting-process");
          e1.printStackTrace();
        } 
      } else {
        File file;
        Polygon p;
        fc.setDialogTitle("save polygon");
        if (actionCommand.equals("save_source")) {
          p = this.ec.getOriginal().getModel().getPolygon();
        } else {
          p = this.ec.getTarget().getModel().getPolygon();
        } 
        if (extension.equals("svg, 2dp")) {
          String ex = getExtension(f);
          if (ex != null && ex.equals("svg")) {
            file = changeFileExtension(f, "svg");
          } else {
            file = changeFileExtension(f, "2dp");
          } 
        } else {
          file = changeFileExtension(f, extension);
        } 
        if (getExtension(file).equals("svg")) {
          try {
            FileWriter writer = new FileWriter(file);
            writer.write(p.toSVG());
            writer.flush();
            writer.close();
          } catch (IOException e1) {
            System.err.println("Failure during FileWriting-process");
            e1.printStackTrace();
          } 
        } else {
          try {
            FileWriter writer = new FileWriter(file);
            writer.write(p.toSaveFormat());
            writer.flush();
            writer.close();
          } catch (IOException e1) {
            System.err.println("Failure during FileWriting-process");
            e1.printStackTrace();
          } 
        } 
      } 
    } else {
      System.out.println("saving operation canceled by user");
    } 
  }
  
  private String getExtension(File f) {
    String ext = null;
    String fileName = f.getName();
    int i = fileName.lastIndexOf('.');
    if (i > 0 && i < fileName.length() - 1)
      ext = fileName.substring(i + 1).toLowerCase(); 
    return ext;
  }
  
  private File changeFileExtension(File f, String extension) {
    String ext = getExtension(f);
    if (ext == null) {
      try {
        String fileName = f.getCanonicalPath();
        StringBuffer buff = new StringBuffer();
        buff.append(fileName);
        buff.append("." + extension);
        return new File(new String(buff));
      } catch (IOException e) {
        e.printStackTrace();
      } 
    } else if (!ext.equals(extension)) {
      try {
        String fileName = f.getCanonicalPath();
        StringBuffer buff = new StringBuffer();
        int i = fileName.lastIndexOf('.');
        System.out.println(i);
        buff.append(fileName.substring(0, i));
        buff.append("." + extension);
        File newFileName = new File(new String(buff));
        return newFileName;
      } catch (IOException e) {
        System.err.println("Apparently encountered problems while trying");
        System.err.println("to get the CanonicalPath of File " + f.getName());
        e.printStackTrace();
      } 
    } 
    return f;
  }
}
