/*     */ package tools;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import shapes.Point;
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
/*     */ public class Bresenham
/*     */ {
/*     */   public static int last_counter;
/*     */   
/*     */   public static void draw(Point p, Point q, Graphics g, int factor) {
/*     */     int inc_x, inc_y;
/*     */     Color c;
/*  31 */     int x = p.getX();
/*  32 */     int y = p.getY();
/*  33 */     int x_end = q.getX();
/*  34 */     int y_end = q.getY();
/*     */     
/*  36 */     int dx = x_end - x;
/*  37 */     int dy = y_end - y;
/*     */     
/*  39 */     if (dx > 0) {
/*  40 */       inc_x = 1;
/*     */     } else {
/*  42 */       inc_x = -1;
/*  43 */     }  if (dy > 0)
/*  44 */     { inc_y = 1; }
/*  45 */     else { inc_y = -1; }
/*     */     
/*  47 */     if (Math.abs(dy) < Math.abs(dx)) {
/*  48 */       int error = -Math.abs(dx);
/*  49 */       int delta = 2 * Math.abs(dy);
/*  50 */       int step = 2 * error;
/*  51 */       while (x != x_end) {
/*  52 */         setPixel(x, y, factor, g);
/*  53 */         x += inc_x;
/*  54 */         error += delta;
/*  55 */         if (error > 0) {
/*  56 */           y += inc_y;
/*  57 */           error += step;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/*  62 */       int error = -Math.abs(dy);
/*  63 */       int delta = 2 * Math.abs(dx);
/*  64 */       int step = 2 * error;
/*  65 */       while (y != y_end) {
/*  66 */         setPixel(x, y, factor, g);
/*  67 */         y += inc_y;
/*  68 */         error += delta;
/*  69 */         if (error > 0) {
/*  70 */           x += inc_x;
/*  71 */           error += step;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  77 */     if (q instanceof shapes.FeaturePoint) {
/*  78 */       c = Color.BLUE;
/*     */     } else {
/*  80 */       c = Color.RED;
/*  81 */     }  setFatPixel(x_end, y_end, factor, g, c);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector sample(Point p, Point q, int sample_rate, int offset) {
/*     */     int inc_x, inc_y;
/* 102 */     Vector return_sample = new Vector();
/*     */ 
/*     */     
/* 105 */     int x = p.getX();
/* 106 */     int y = p.getY();
/* 107 */     int x_end = q.getX();
/* 108 */     int y_end = q.getY();
/*     */     
/* 110 */     int dx = x_end - x;
/* 111 */     int dy = y_end - y;
/*     */     
/* 113 */     int counter = offset;
/*     */     
/* 115 */     if (dx > 0) {
/* 116 */       inc_x = 1;
/*     */     } else {
/* 118 */       inc_x = -1;
/* 119 */     }  if (dy > 0)
/* 120 */     { inc_y = 1; }
/* 121 */     else { inc_y = -1; }
/*     */ 
/*     */ 
/*     */     
/* 125 */     if (Math.abs(dy) < Math.abs(dx)) {
/* 126 */       int error = -Math.abs(dx);
/* 127 */       int delta = 2 * Math.abs(dy);
/* 128 */       int step = 2 * error;
/* 129 */       while (x != x_end) {
/*     */         
/* 131 */         if (counter >= sample_rate) {
/* 132 */           return_sample.add(new Point(x, y));
/* 133 */           counter = 0;
/*     */         } 
/* 135 */         counter++;
/* 136 */         x += inc_x;
/* 137 */         error += delta;
/* 138 */         if (error > 0) {
/* 139 */           y += inc_y;
/* 140 */           error += step;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 145 */       int error = -Math.abs(dy);
/* 146 */       int delta = 2 * Math.abs(dx);
/* 147 */       int step = 2 * error;
/* 148 */       while (y != y_end) {
/*     */         
/* 150 */         if (counter >= sample_rate) {
/* 151 */           return_sample.add(new Point(x, y));
/* 152 */           counter = 0;
/*     */         } 
/* 154 */         counter++;
/* 155 */         y += inc_y;
/* 156 */         error += delta;
/* 157 */         if (error > 0) {
/* 158 */           x += inc_x;
/* 159 */           error += step;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     return_sample.add(q);
/* 165 */     last_counter = counter;
/*     */     
/* 167 */     return return_sample;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector semiUniformSample(Point p, Point q, int sample_rate) {
/*     */     int inc_x, inc_y;
/* 186 */     Vector return_sample = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     int x = p.getX();
/* 192 */     int y = p.getY();
/* 193 */     int x_end = q.getX();
/* 194 */     int y_end = q.getY();
/*     */     
/* 196 */     double distance = Math.sqrt(Math.pow((x - x_end), 2.0D) + Math.pow((y - y_end), 2.0D));
/* 197 */     double part_distance = distance / (sample_rate + 1);
/* 198 */     int length = (int)(part_distance + 0.5D);
/*     */     
/* 200 */     int dx = x_end - x;
/* 201 */     int dy = y_end - y;
/*     */     
/* 203 */     if (dx > 0) {
/* 204 */       inc_x = 1;
/*     */     } else {
/* 206 */       inc_x = -1;
/* 207 */     }  if (dy > 0)
/* 208 */     { inc_y = 1; }
/* 209 */     else { inc_y = -1; }
/*     */     
/* 211 */     return_sample.add(p);
/* 212 */     int counter = 0;
/* 213 */     if (Math.abs(dy) < Math.abs(dx)) {
/* 214 */       int error = -Math.abs(dx);
/* 215 */       int delta = 2 * Math.abs(dy);
/* 216 */       int step = 2 * error;
/* 217 */       while (x != x_end) {
/*     */         
/* 219 */         if (counter >= length) {
/* 220 */           return_sample.add(new Point(x, y));
/* 221 */           counter = 0;
/*     */         } 
/* 223 */         counter++;
/* 224 */         x += inc_x;
/* 225 */         error += delta;
/* 226 */         if (error > 0) {
/* 227 */           y += inc_y;
/* 228 */           error += step;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 233 */       int error = -Math.abs(dy);
/* 234 */       int delta = 2 * Math.abs(dx);
/* 235 */       int step = 2 * error;
/* 236 */       while (y != y_end) {
/*     */         
/* 238 */         if (counter >= length) {
/* 239 */           return_sample.add(new Point(x, y));
/* 240 */           counter = 0;
/*     */         } 
/* 242 */         counter++;
/* 243 */         y += inc_y;
/* 244 */         error += delta;
/* 245 */         if (error > 0) {
/* 246 */           x += inc_x;
/* 247 */           error += step;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 252 */     if (!((Point)return_sample.lastElement()).equals(q))
/* 253 */       return_sample.add(q); 
/* 254 */     return return_sample;
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
/*     */   public static void dashedLine(Point p, Point q, Graphics g, int factor) {
/* 266 */     int inc_x, inc_y, steps = 0;
/*     */     
/* 268 */     int x = p.getX();
/* 269 */     int y = p.getY();
/* 270 */     int x_end = q.getX();
/* 271 */     int y_end = q.getY();
/*     */     
/* 273 */     int dx = x_end - x;
/* 274 */     int dy = y_end - y;
/*     */     
/* 276 */     if (dx > 0) {
/* 277 */       inc_x = 1;
/*     */     } else {
/* 279 */       inc_x = -1;
/* 280 */     }  if (dy > 0)
/* 281 */     { inc_y = 1; }
/* 282 */     else { inc_y = -1; }
/*     */     
/* 284 */     if (Math.abs(dy) < Math.abs(dx)) {
/* 285 */       int error = -Math.abs(dx);
/* 286 */       int delta = 2 * Math.abs(dy);
/* 287 */       int step = 2 * error;
/* 288 */       while (x != x_end) {
/* 289 */         if (steps++ / 3 % 2 == 0)
/* 290 */           setPixel(x, y, factor, g); 
/* 291 */         x += inc_x;
/* 292 */         error += delta;
/* 293 */         if (error > 0) {
/* 294 */           y += inc_y;
/* 295 */           error += step;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 300 */       int error = -Math.abs(dy);
/* 301 */       int delta = 2 * Math.abs(dx);
/* 302 */       int step = 2 * error;
/* 303 */       while (y != y_end) {
/* 304 */         if (steps++ / 3 % 2 == 0)
/* 305 */           setPixel(x, y, factor, g); 
/* 306 */         y += inc_y;
/* 307 */         error += delta;
/* 308 */         if (error > 0) {
/* 309 */           x += inc_x;
/* 310 */           error += step;
/*     */         } 
/*     */       } 
/*     */     } 
/* 314 */     if (steps++ / 3 % 2 == 0) {
/* 315 */       setPixel(x_end, y_end, factor, g);
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
/*     */   public static double getLength(Vector vector) {
/* 328 */     double length = 0.0D;
/*     */ 
/*     */     
/* 331 */     int size = vector.size();
/* 332 */     if (size == 0 || size == 1) {
/* 333 */       return length;
/*     */     }
/* 335 */     Enumeration e = vector.elements();
/* 336 */     Point a = e.nextElement();
/* 337 */     while (e.hasMoreElements()) {
/* 338 */       Point b = e.nextElement();
/* 339 */       int x_len = a.getX() - b.getX();
/* 340 */       int y_len = a.getY() - b.getY();
/* 341 */       length += Math.sqrt((x_len * x_len + y_len * y_len));
/* 342 */       a = b;
/*     */     } 
/*     */     
/* 345 */     return length;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setPixel(int x, int y, int factor, Graphics g) {
/* 350 */     g.fillOval(x * factor, y * factor, factor, factor);
/*     */   }
/*     */   
/*     */   private static void setFatPixel(int x, int y, int factor, Graphics g, Color c) {
/* 354 */     Color color = g.getColor();
/* 355 */     g.setColor(c);
/* 356 */     g.fillOval((x - 1) * factor, (y - 1) * factor, 4 * factor, 4 * factor);
/* 357 */     g.setColor(color);
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\tools\Bresenham.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */