/*     */ package controls;
/*     */ 
/*     */ import application.ExtendedController;
/*     */ import application.Model;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import shapes.Point;
/*     */ import shapes.Polygon;
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
/*     */ public class LoadButtonListener
/*     */   implements ActionListener
/*     */ {
/*     */   private ExtendedController ec;
/*     */   
/*     */   public LoadButtonListener(ExtendedController ec) {
/*  39 */     this.ec = ec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*     */     Model model;
/*  48 */     if (e.getActionCommand().equals("load_source")) {
/*  49 */       model = this.ec.getOriginal().getModel();
/*     */     } else {
/*  51 */       model = this.ec.getTarget().getModel();
/*     */     } 
/*  53 */     JFileChooser fc = new JFileChooser();
/*  54 */     fc.addChoosableFileFilter(new FileFilter(this) {
/*     */           public boolean accept(File f) {
/*  56 */             if (f.isDirectory())
/*  57 */               return true; 
/*  58 */             String fileExtension = this.this$0.getExtension(f);
/*  59 */             if (fileExtension != null && fileExtension.equals("2dp"))
/*  60 */               return true; 
/*  61 */             return false;
/*     */           }
/*     */           final LoadButtonListener this$0;
/*     */           public String getDescription() {
/*  65 */             return "*.2dp-files";
/*     */           }
/*     */         });
/*  68 */     fc.setAcceptAllFileFilterUsed(false);
/*  69 */     int returnVal = fc.showOpenDialog(null);
/*  70 */     if (returnVal == 0) {
/*  71 */       File f = fc.getSelectedFile();
/*     */ 
/*     */ 
/*     */       
/*  75 */       Polygon polygon = new Polygon();
/*     */       
/*  77 */       readPolygonFile(model, f, polygon);
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
/*     */   
/*     */   public void readPolygonFile(Model model, File f, Polygon polygon) {
/*     */     try {
/*     */       boolean closed;
/*  94 */       BufferedReader buff = new BufferedReader(new FileReader(f));
/*  95 */       String line = buff.readLine();
/*  96 */       if (!line.equals("2D-Polygon for Morphing"))
/*  97 */         throw new IllegalArgumentException("Wrong file format"); 
/*  98 */       buff.readLine();
/*  99 */       buff.readLine();
/* 100 */       line = buff.readLine();
/* 101 */       if (line.equals("is closed")) {
/* 102 */         closed = true;
/* 103 */       } else if (line.equals("is not closed")) {
/* 104 */         closed = false;
/*     */       } else {
/* 106 */         throw new IllegalArgumentException("Wrong file format");
/*     */       } 
/* 108 */       buff.readLine();
/*     */       
/* 110 */       while ((line = buff.readLine()) != null) {
/* 111 */         String[] point_rep = line.split(",");
/* 112 */         Point p = new Point((new Integer(point_rep[0])).intValue(), (new Integer(point_rep[1])).intValue());
/* 113 */         polygon.addVertex(p);
/*     */       } 
/* 115 */       if (closed) {
/* 116 */         polygon.close();
/*     */       }
/* 118 */       model.append(polygon);
/* 119 */     } catch (IOException e1) {
/* 120 */       System.err.println("Failure during FileReading-process");
/* 121 */       e1.printStackTrace();
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
/*     */   
/*     */   public void readPolygonFile(Model model, String poly_string, Polygon polygon) {
/*     */     boolean closed;
/* 137 */     String[] lines = poly_string.split("\n");
/* 138 */     if (!lines[0].equals("2D-Polygon for Morphing"))
/* 139 */       throw new IllegalArgumentException("Wrong file format"); 
/* 140 */     if (lines[3].equals("is closed")) {
/* 141 */       closed = true;
/* 142 */     } else if (lines[3].equals("is not closed")) {
/* 143 */       closed = false;
/*     */     } else {
/* 145 */       throw new IllegalArgumentException("Wrong file format");
/* 146 */     }  for (int i = 5; i < lines.length; i++) {
/* 147 */       String[] point_rep = lines[i].split(",");
/* 148 */       Point p = new Point((new Integer(point_rep[0])).intValue(), (new Integer(point_rep[1])).intValue());
/* 149 */       polygon.addVertex(p);
/*     */     } 
/* 151 */     if (closed) {
/* 152 */       polygon.close();
/*     */     }
/* 154 */     model.append(polygon);
/*     */   }
/*     */   
/*     */   private String getExtension(File f) {
/* 158 */     String ext = null;
/* 159 */     String fileName = f.getName();
/* 160 */     int i = fileName.lastIndexOf('.');
/* 161 */     if (i > 0 && i < fileName.length() - 1) {
/* 162 */       ext = fileName.substring(i + 1).toLowerCase();
/*     */     }
/* 164 */     return ext;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\controls\LoadButtonListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */