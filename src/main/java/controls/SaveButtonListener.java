/*     */ package controls;
/*     */ 
/*     */ import application.ExtendedController;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.filechooser.FileFilter;
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
/*     */ 
/*     */ 
/*     */ public class SaveButtonListener
/*     */   implements ActionListener
/*     */ {
/*     */   private ExtendedController ec;
/*     */   private Component parent;
/*     */   
/*     */   public SaveButtonListener(ExtendedController ec, Component parent) {
/*  40 */     this.ec = ec;
/*  41 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*  49 */     String actionCommand = e.getActionCommand();
/*  50 */     JFileChooser fc = new JFileChooser();
/*     */     
/*  52 */     if (e.getActionCommand().equals("save_morph")) {
/*  53 */       fc.setDialogTitle("save morph as svg");
/*  54 */       fc.setApproveButtonToolTipText("export the morphing sequence into an svg file");
/*     */     }
/*     */     else {
/*     */       
/*  58 */       fc.setDialogTitle("save polygon");
/*  59 */       fc.setApproveButtonToolTipText("export the polygon to svg or 2pd");
/*     */     } 
/*  61 */     fc.setApproveButtonText("save");
/*     */ 
/*     */     
/*  64 */     fc.addChoosableFileFilter(new FileFilter(this) {
/*     */           public boolean accept(File f) {
/*  66 */             if (f.isDirectory())
/*  67 */               return true; 
/*  68 */             String fileExtension = this.this$0.getExtension(f);
/*  69 */             if (fileExtension != null && fileExtension.equals("svg"))
/*  70 */               return true; 
/*  71 */             return false;
/*     */           }
/*     */           final SaveButtonListener this$0;
/*     */           public String getDescription() {
/*  75 */             return "*.svg-files"; }
/*     */         });
/*  77 */     if (!actionCommand.equals("save_morph")) {
/*  78 */       fc.addChoosableFileFilter(new FileFilter(this) { final SaveButtonListener this$0;
/*     */             public boolean accept(File f) {
/*  80 */               if (f.isDirectory())
/*  81 */                 return true; 
/*  82 */               String fileExtension = this.this$0.getExtension(f);
/*  83 */               if ((((fileExtension != null) ? 1 : 0) & fileExtension.equals("2dp")) != 0)
/*  84 */                 return true; 
/*  85 */               return false;
/*     */             }
/*     */             public String getDescription() {
/*  88 */               return "*.2dp-files";
/*     */             } }
/*     */         );
/*     */       
/*  92 */       fc.addChoosableFileFilter(new FileFilter(this) { final SaveButtonListener this$0;
/*     */             public boolean accept(File f) {
/*  94 */               if (f.isDirectory()) {
/*  95 */                 return true;
/*     */               }
/*  97 */               String fileExtension = this.this$0.getExtension(f);
/*  98 */               if (fileExtension != null && (
/*  99 */                 fileExtension.equals("svg") || fileExtension.equals("2dp"))) {
/* 100 */                 return true;
/*     */               }
/* 102 */               return false;
/*     */             }
/*     */             
/*     */             public String getDescription() {
/* 106 */               return "*.svg, *.2dp";
/*     */             } }
/*     */         );
/*     */     } 
/* 110 */     fc.setAcceptAllFileFilterUsed(false);
/* 111 */     int returnVal = fc.showSaveDialog(this.parent);
/* 112 */     if (returnVal == 0) {
/*     */       
/* 114 */       File f = fc.getSelectedFile();
/* 115 */       String extension = fc.getFileFilter().getDescription();
/* 116 */       if (extension.equals("*.svg, *.2dp")) {
/* 117 */         extension = "svg, 2dp";
/* 118 */       } else if (extension.equals("*.svg-files")) {
/* 119 */         extension = "svg";
/*     */       } else {
/* 121 */         extension = "2dp";
/*     */       } 
/* 123 */       if (actionCommand.equals("save_morph")) {
/*     */         
/*     */         try {
/* 126 */           File file = changeFileExtension(f, "svg");
/* 127 */           FileWriter writer = new FileWriter(file);
/* 128 */           writer.write(this.ec.toSVG());
/* 129 */           writer.flush();
/* 130 */           writer.close();
/* 131 */         } catch (IOException e1) {
/* 132 */           System.err.println("Failure during FileWriting-process");
/* 133 */           e1.printStackTrace();
/*     */         } 
/*     */       } else {
/*     */         File file; Polygon p;
/* 137 */         fc.setDialogTitle("save polygon");
/*     */         
/* 139 */         if (actionCommand.equals("save_source")) {
/* 140 */           p = this.ec.getOriginal().getModel().getPolygon();
/*     */         } else {
/* 142 */           p = this.ec.getTarget().getModel().getPolygon();
/* 143 */         }  if (extension.equals("svg, 2dp")) {
/* 144 */           String ex = getExtension(f);
/* 145 */           if (ex != null && ex.equals("svg")) {
/* 146 */             file = changeFileExtension(f, "svg");
/*     */           } else {
/* 148 */             file = changeFileExtension(f, "2dp");
/*     */           } 
/*     */         } else {
/* 151 */           file = changeFileExtension(f, extension);
/* 152 */         }  if (getExtension(file).equals("svg")) {
/*     */           try {
/* 154 */             FileWriter writer = new FileWriter(file);
/* 155 */             writer.write(p.toSVG());
/* 156 */             writer.flush();
/* 157 */             writer.close();
/*     */           }
/* 159 */           catch (IOException e1) {
/* 160 */             System.err.println("Failure during FileWriting-process");
/* 161 */             e1.printStackTrace();
/*     */           } 
/*     */         } else {
/*     */           
/*     */           try {
/* 166 */             FileWriter writer = new FileWriter(file);
/* 167 */             writer.write(p.toSaveFormat());
/* 168 */             writer.flush();
/* 169 */             writer.close();
/*     */           }
/* 171 */           catch (IOException e1) {
/* 172 */             System.err.println("Failure during FileWriting-process");
/* 173 */             e1.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 179 */       System.out.println("saving operation canceled by user");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getExtension(File f) {
/* 187 */     String ext = null;
/* 188 */     String fileName = f.getName();
/* 189 */     int i = fileName.lastIndexOf('.');
/* 190 */     if (i > 0 && i < fileName.length() - 1) {
/* 191 */       ext = fileName.substring(i + 1).toLowerCase();
/*     */     }
/* 193 */     return ext;
/*     */   }
/*     */   
/*     */   private File changeFileExtension(File f, String extension) {
/* 197 */     String ext = getExtension(f);
/*     */     
/* 199 */     if (ext == null) {
/*     */       try {
/* 201 */         String fileName = f.getCanonicalPath();
/* 202 */         StringBuffer buff = new StringBuffer();
/* 203 */         buff.append(fileName);
/* 204 */         buff.append("." + extension);
/* 205 */         return new File(new String(buff));
/* 206 */       } catch (IOException e) {
/*     */         
/* 208 */         e.printStackTrace();
/*     */       }
/*     */     
/* 211 */     } else if (!ext.equals(extension)) {
/*     */       try {
/* 213 */         String fileName = f.getCanonicalPath();
/* 214 */         StringBuffer buff = new StringBuffer();
/* 215 */         int i = fileName.lastIndexOf('.');
/* 216 */         System.out.println(i);
/* 217 */         buff.append(fileName.substring(0, i));
/* 218 */         buff.append("." + extension);
/* 219 */         File newFileName = new File(new String(buff));
/* 220 */         return newFileName;
/* 221 */       } catch (IOException e) {
/* 222 */         System.err.println("Apparently encountered problems while trying");
/* 223 */         System.err.println("to get the CanonicalPath of File " + f.getName());
/* 224 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 227 */     return f;
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\controls\SaveButtonListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */