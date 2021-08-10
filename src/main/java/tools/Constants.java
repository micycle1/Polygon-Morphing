/*    */ package tools;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.GridBagLayout;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.border.Border;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Constants
/*    */ {
/* 29 */   public static final double[] WEIGHTS = new double[] { 0.3333333333333333D, 0.3333333333333333D, 0.3333333333333333D };
/* 30 */   public static final int[] DENOMINATORS = new int[] { 3, 3, 3 };
/*    */   
/*    */   public static final double LAMBDA = 1.0D;
/*    */   
/*    */   public static final int MAX_SKIPS = 3;
/*    */   
/*    */   public static final int MAX_FPS = 25;
/*    */   public static final int MIN_FPS = 1;
/*    */   public static final int NORM_FPS = 15;
/*    */   public static final int MAX_STEPS = 2000;
/*    */   public static final int NORM_STEPS = 50;
/*    */   public static final int MIN_STEPS = 1;
/*    */   public static final double MIN_TIME = 0.0D;
/*    */   public static final double MAX_TIME = 60.0D;
/*    */   public static final double NORM_TIME = 5.0D;
/*    */   public static final int ROS_SIZE = 10;
/*    */   public static final int SAMPLE_RATE = 5;
/*    */   public static final double MAX_ANGLE = 130.0D;
/*    */   public static final double MIN_LENGTH = 0.01D;
/*    */   public static final int MAX_FEATUREPOINTS = 35;
/* 50 */   public static final Border BLACKLINE = BorderFactory.createLineBorder(Color.black);
/*    */ 
/*    */   
/*    */   public static void addIntoGrid(JPanel panel, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
/* 54 */     gridbag.setConstraints(comp, c);
/* 55 */     panel.add(comp);
/*    */   }
/*    */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\tools\Constants.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */