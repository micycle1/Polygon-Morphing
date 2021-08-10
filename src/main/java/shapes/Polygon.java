/*      */ package shapes;
/*      */ 
/*      */ import java.awt.Graphics;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Vector;
/*      */ import math.Covariance;
/*      */ import math.Eigenvalue;
/*      */ import tools.Bresenham;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Polygon
/*      */   extends GraphicObject
/*      */   implements Cloneable
/*      */ {
/*      */   public static final boolean DASHED = true;
/*      */   private int fp_count;
/*      */   private int total_count;
/*      */   private Vector featurePoints;
/*      */   private Vector all_vertices;
/*   32 */   private int sample_rate = 0;
/*   33 */   private Vector lastsample = null;
/*      */   
/*      */   private boolean closed = false;
/*      */   
/*      */   private boolean changed = false;
/*      */   
/*      */   private boolean dashed = false;
/*      */   
/*      */   private int region;
/*      */   
/*      */   public Polygon() {
/*   44 */     super(1);
/*   45 */     this.total_count = 0;
/*   46 */     this.featurePoints = new Vector();
/*   47 */     this.all_vertices = new Vector();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Polygon(Point start, int factor) {
/*   58 */     super(factor);
/*   59 */     this.total_count = 0;
/*   60 */     this.fp_count = 0;
/*   61 */     this.featurePoints = new Vector();
/*   62 */     this.all_vertices = new Vector();
/*   63 */     addVertex(start);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Polygon(Point start, int region, int factor) {
/*   74 */     this(start, factor);
/*   75 */     setRegion(region);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Polygon(Point start, int region, int factor, boolean dashed) {
/*   89 */     this(start, factor);
/*   90 */     setRegion(region);
/*   91 */     setDashed(dashed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Polygon(Polygon original) {
/*  100 */     this.featurePoints = new Vector();
/*  101 */     this.all_vertices = new Vector();
/*  102 */     this.total_count = 0;
/*  103 */     this.fp_count = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  108 */     Enumeration e = original.all_vertices.elements();
/*  109 */     while (e.hasMoreElements()) {
/*  110 */       addVertex(e.nextElement());
/*      */     }
/*  112 */     if (original.isClosed()) {
/*  113 */       close();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Polygon(Polygon p, int i) {
/*  127 */     int vertex_count = p.getCount();
/*  128 */     if (i < 0 || i >= vertex_count) throw new IllegalArgumentException("index i= " + i + " of removed vertex is out of bounds! \nFor this polygon it must be between (including) 0 and" + (p.getCount() - 1) + "!");
/*      */     
/*  130 */     this.total_count = 0;
/*  131 */     this.fp_count = 0;
/*  132 */     this.featurePoints = new Vector();
/*  133 */     this.all_vertices = new Vector();
/*      */     int j;
/*  135 */     for (j = 0; j < i; j++) {
/*  136 */       addVertex(p.getVertex(j));
/*      */     }
/*  138 */     for (j = i + 1; j < vertex_count; j++) {
/*  139 */       addVertex(p.getVertex(j));
/*      */     }
/*  141 */     if (p.isClosed()) {
/*  142 */       close();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addVertex(Point p) {
/*  151 */     if (p instanceof FeaturePoint) {
/*  152 */       FeaturePoint fp = new FeaturePoint(p);
/*  153 */       if (this.featurePoints.add(fp)) {
/*  154 */         this.all_vertices.add(fp);
/*      */ 
/*      */ 
/*      */         
/*  158 */         this.total_count++;
/*  159 */         this.fp_count++;
/*  160 */         this.changed = true;
/*      */       } 
/*      */     } else {
/*      */       
/*  164 */       this.all_vertices.add(new Point(p));
/*  165 */       this.total_count++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addVertices(String point_string) {
/*  179 */     String[] new_lines = point_string.split("\n");
/*  180 */     for (int i = 0; i < new_lines.length; i++) {
/*  181 */       String[] point_rep = new_lines[i].split(",");
/*  182 */       Point p = new Point((new Integer(point_rep[0])).intValue(), (new Integer(point_rep[1])).intValue());
/*  183 */       addVertex(p);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Point getVertex(int index) {
/*  195 */     if (index < 0 || index >= this.total_count) throw new IllegalArgumentException("Given index (" + index + ") is out of legal values."); 
/*  196 */     return this.all_vertices.elementAt(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isVertex(Point p) {
/*  206 */     boolean is_vertex = false;
/*  207 */     Enumeration e = this.all_vertices.elements();
/*      */     
/*  209 */     while (!is_vertex && e.hasMoreElements()) {
/*  210 */       Point q = e.nextElement();
/*  211 */       is_vertex = p.equals(q);
/*      */     } 
/*  213 */     return is_vertex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addVertexBehind(Point p, Point q) {
/*  225 */     int index = getIndex(q);
/*  226 */     if (index < 0) throw new IllegalArgumentException("Point " + q.toString() + " is no vortex in this polygon.");
/*      */     
/*  228 */     if (p instanceof FeaturePoint) {
/*  229 */       this.fp_count++;
/*  230 */       int f_index = -1;
/*  231 */       if (q instanceof FeaturePoint) {
/*  232 */         f_index = getFeaturePointIndex((FeaturePoint)q);
/*  233 */         this.featurePoints.insertElementAt(p, f_index + 1);
/*      */       } else {
/*      */         Point tmp;
/*      */         
/*  237 */         f_index = index;
/*      */         do {
/*  239 */           f_index = (f_index - 1 + this.total_count) % this.total_count;
/*  240 */           tmp = this.all_vertices.elementAt(f_index);
/*  241 */         } while (!(tmp instanceof FeaturePoint));
/*  242 */         f_index = getFeaturePointIndex((FeaturePoint)tmp);
/*  243 */         this.featurePoints.insertElementAt(p, f_index + 1);
/*      */       } 
/*      */     } 
/*  246 */     this.total_count++;
/*  247 */     this.all_vertices.insertElementAt(p, index + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addVertexBefore(Point p, Point q) {
/*  259 */     int index = getIndex(q);
/*  260 */     if (index < 0) throw new IllegalArgumentException("Point " + q.toString() + " is no vortex in this polygon.");
/*      */     
/*  262 */     if (p instanceof FeaturePoint) {
/*  263 */       this.fp_count++;
/*  264 */       int f_index = -1;
/*  265 */       if (q instanceof FeaturePoint) {
/*  266 */         f_index = getFeaturePointIndex((FeaturePoint)q);
/*  267 */         this.featurePoints.insertElementAt(p, f_index);
/*      */       } else {
/*      */         Point tmp;
/*      */         
/*  271 */         f_index = index;
/*      */         do {
/*  273 */           f_index = (f_index - 1 + this.total_count) % this.total_count;
/*  274 */           tmp = this.all_vertices.elementAt(f_index);
/*  275 */         } while (!(tmp instanceof FeaturePoint));
/*  276 */         f_index = getFeaturePointIndex((FeaturePoint)tmp);
/*  277 */         this.featurePoints.insertElementAt(p, f_index);
/*      */       } 
/*      */     } 
/*  280 */     this.total_count++;
/*  281 */     this.all_vertices.insertElementAt(p, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addVertexBetween(Point p, Point q, Point r) {
/*  294 */     int index_q = -1;
/*  295 */     int index_r = -1;
/*  296 */     int i = 0;
/*  297 */     while (index_q == -1 && i < this.total_count) {
/*  298 */       if (q.equals(this.all_vertices.elementAt(i)))
/*  299 */         index_q = i; 
/*  300 */       i++;
/*      */     } 
/*  302 */     if (index_q < 0) throw new IllegalArgumentException("Point " + q.toString() + " is no vortex in this polygon."); 
/*  303 */     i = 0;
/*  304 */     while (index_r == -1 && i < this.total_count) {
/*  305 */       if (r.equals(this.all_vertices.elementAt(i)))
/*  306 */         index_r = i; 
/*  307 */       i++;
/*      */     } 
/*  309 */     if (index_r < 0) throw new IllegalArgumentException("Point " + r.toString() + " is no vortex in this polygon."); 
/*  310 */     if (index_q < index_r) {
/*  311 */       if (index_r - index_q == 1) {
/*  312 */         if (p instanceof FeaturePoint) {
/*  313 */           FeaturePoint fp = new FeaturePoint(p);
/*  314 */           this.total_count++;
/*  315 */           this.all_vertices.insertElementAt(fp, index_r);
/*  316 */           this.featurePoints.insertElementAt(fp, 0);
/*      */         }
/*      */       
/*  319 */       } else if (index_q == 0 && index_r == this.total_count - 1) {
/*  320 */         addVertex(p);
/*      */       } else {
/*      */         
/*  323 */         throw new IllegalArgumentException("Points " + p.toString() + 
/*  324 */             " and " + q.toString() + " are not next to each other.");
/*      */       }
/*      */     
/*  327 */     } else if (index_q - index_r == 1) {
/*  328 */       this.total_count++;
/*  329 */       this.featurePoints.insertElementAt(new FeaturePoint(p), index_q);
/*      */     }
/*  331 */     else if (index_r == 0 && index_q == this.total_count - 1) {
/*  332 */       addVertex(p);
/*      */     } else {
/*      */       
/*  335 */       throw new IllegalArgumentException("Points " + p.toString() + 
/*  336 */           " and " + q.toString() + " are not next to each other.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector getFeaturePoints() {
/*  348 */     return this.featurePoints;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector getAllVertices() {
/*  358 */     return this.all_vertices;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector getSample(int sample_rate) {
/*  372 */     if (sample_rate == this.sample_rate && this.lastsample != null && !this.changed)
/*  373 */       return this.lastsample; 
/*  374 */     if (this.total_count == 0)
/*  375 */       return null; 
/*  376 */     if (this.total_count == 1) {
/*  377 */       this.lastsample = this.all_vertices;
/*  378 */       this.changed = false;
/*      */     } else {
/*      */       
/*  381 */       this.lastsample = new Vector();
/*      */       
/*  383 */       Enumeration e = this.all_vertices.elements();
/*  384 */       Point start = e.nextElement();
/*  385 */       while (e.hasMoreElements()) {
/*  386 */         Point end = e.nextElement();
/*  387 */         this.lastsample.addAll(Bresenham.semiUniformSample(start, end, sample_rate));
/*  388 */         this.lastsample.removeElementAt(this.lastsample.size() - 1);
/*  389 */         start = end;
/*      */       } 
/*  391 */       if (this.closed) {
/*  392 */         Point end = this.all_vertices.firstElement();
/*  393 */         this.lastsample.addAll(Bresenham.semiUniformSample(start, end, sample_rate));
/*  394 */         this.lastsample.removeElementAt(this.lastsample.size() - 1);
/*      */       } 
/*      */     } 
/*  397 */     this.sample_rate = sample_rate;
/*  398 */     this.changed = false;
/*  399 */     return this.lastsample;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getSampleArray(int sample_rate) {
/*  464 */     if (this.sample_rate != sample_rate || this.lastsample == null || this.changed) {
/*  465 */       getSample(sample_rate);
/*      */     }
/*  467 */     int length = this.lastsample.size();
/*  468 */     int[] return_array = new int[2 * length];
/*      */     
/*  470 */     for (int i = 0; i < length; i++) {
/*  471 */       Point a = this.lastsample.elementAt(i);
/*  472 */       return_array[i * 2] = a.getX();
/*  473 */       return_array[i * 2 + 1] = a.getY();
/*      */     } 
/*  475 */     return return_array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getLength() {
/*  484 */     Point tmp1 = this.all_vertices.firstElement();
/*  485 */     double length = 0.0D;
/*  486 */     for (int i = 1; i < this.total_count; i++) {
/*  487 */       Point tmp2 = this.all_vertices.elementAt(i);
/*  488 */       double xlen = (tmp2.getX() - tmp1.getX());
/*  489 */       double ylen = (tmp2.getY() - tmp1.getY());
/*  490 */       length += Math.sqrt(xlen * xlen + ylen * ylen);
/*  491 */       tmp1 = tmp2;
/*      */     } 
/*  493 */     if (this.closed) {
/*  494 */       Point tmp2 = this.all_vertices.firstElement();
/*  495 */       double xlen = (tmp2.getX() - tmp1.getX());
/*  496 */       double ylen = (tmp2.getY() - tmp1.getY());
/*  497 */       length += Math.sqrt(xlen * xlen + ylen * ylen);
/*      */     } 
/*  499 */     return length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() {
/*  506 */     this.closed = true;
/*  507 */     this.changed = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FeaturePoint getFeaturePoint(int index) {
/*  517 */     if (index < 0 || index >= this.fp_count) throw new IllegalArgumentException("desired vertex index " + index + " is out bounds!");
/*      */     
/*  519 */     return this.featurePoints.elementAt(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFeaturePointIndex(FeaturePoint fp) {
/*  532 */     int index = -1;
/*  533 */     this.fp_count = this.featurePoints.size();
/*  534 */     int counter = 0;
/*  535 */     while (index == -1 && counter < this.fp_count) {
/*  536 */       if (fp == (FeaturePoint)this.featurePoints.elementAt(counter))
/*  537 */         index = counter; 
/*  538 */       counter++;
/*      */     } 
/*  540 */     if (index == -1) {
/*  541 */       counter = 0;
/*  542 */       while (index == -1 && counter < this.fp_count) {
/*  543 */         if (fp.equals(this.featurePoints.elementAt(counter)))
/*  544 */           index = counter; 
/*  545 */         counter++;
/*      */       } 
/*      */     } 
/*  548 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getIndex(Point p) {
/*  560 */     int index = -1;
/*  561 */     int counter = 0;
/*  562 */     while (index == -1 && counter < this.total_count) {
/*  563 */       if (p == (Point)this.all_vertices.elementAt(counter))
/*  564 */         index = counter; 
/*  565 */       counter++;
/*      */     } 
/*  567 */     if (index == -1) {
/*  568 */       counter = 0;
/*  569 */       while (index == -1 && counter < this.total_count) {
/*  570 */         if (p.equals(this.all_vertices.elementAt(counter)))
/*  571 */           index = counter; 
/*  572 */         counter++;
/*      */       } 
/*      */     } 
/*  575 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCount() {
/*  584 */     return this.all_vertices.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFeaturePointCount() {
/*  593 */     return this.featurePoints.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRegion(int region) {
/*  603 */     if (region < 0) throw new IllegalArgumentException("Size of the region must be >=0 !"); 
/*  604 */     this.region = region;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRegion() {
/*  612 */     return this.region;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(Point p) {
/*  628 */     int x = p.getX();
/*  629 */     int y = p.getY();
/*  630 */     boolean contains = false;
/*      */     
/*  632 */     int x1 = ((Point)this.all_vertices.elementAt(this.total_count - 1)).getX();
/*  633 */     int y1 = ((Point)this.all_vertices.elementAt(this.total_count - 1)).getY();
/*  634 */     int x2 = ((Point)this.all_vertices.elementAt(0)).getX();
/*  635 */     int y2 = ((Point)this.all_vertices.elementAt(0)).getY();
/*      */     
/*  637 */     boolean start_above = (y1 >= y);
/*      */     
/*  639 */     for (int i = 1; i < this.total_count; i++) {
/*  640 */       boolean bool = (y2 >= y);
/*  641 */       if (start_above != bool) {
/*  642 */         if ((y2 - y) * (x2 - x1) <= (y2 - y1) * (x2 - x)) {
/*  643 */           if (bool) {
/*  644 */             contains = !contains;
/*      */           
/*      */           }
/*      */         }
/*  648 */         else if (!bool) {
/*  649 */           contains = !contains;
/*      */         } 
/*      */       }
/*      */       
/*  653 */       start_above = bool;
/*  654 */       y1 = y2;
/*  655 */       x1 = x2;
/*  656 */       y2 = ((Point)this.all_vertices.elementAt(i)).getY();
/*  657 */       x2 = ((Point)this.all_vertices.elementAt(i)).getX();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  662 */     boolean end_above = (y2 >= y);
/*  663 */     if (start_above != end_above) {
/*  664 */       if ((y2 - y) * (x2 - x1) <= (y2 - y1) * (x2 - x)) {
/*  665 */         if (end_above) {
/*  666 */           contains = !contains;
/*      */         
/*      */         }
/*      */       }
/*  670 */       else if (!end_above) {
/*  671 */         contains = !contains;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  676 */     return contains;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isConvex(Point p) {
/*      */     int index;
/*  692 */     if ((index = this.featurePoints.indexOf(p)) == -1)
/*  693 */       throw new IllegalArgumentException("Point not contained in polygon!"); 
/*  694 */     if (this.closed || (index - 2 >= 0 && index + 2 < this.total_count)) {
/*      */ 
/*      */       
/*  697 */       int ux = p.getX();
/*  698 */       int uy = p.getY();
/*      */       
/*  700 */       Point tmp1 = this.featurePoints.elementAt((index - 2 + this.total_count) % this.total_count);
/*  701 */       Point tmp2 = this.featurePoints.elementAt((index - 1 + this.total_count) % this.total_count);
/*      */       
/*  703 */       int px = tmp1.getX();
/*  704 */       int py = tmp1.getY();
/*  705 */       int vx = tmp2.getX() - px;
/*  706 */       int vy = tmp2.getY() - py;
/*  707 */       int result1 = ux * vy - uy * vx + py * vx - px * vy;
/*  708 */       tmp1 = this.featurePoints.elementAt((index + 1 + this.total_count) % this.total_count);
/*  709 */       tmp2 = this.featurePoints.elementAt((index + 2 + this.total_count) % this.total_count);
/*      */       
/*  711 */       px = tmp1.getX();
/*  712 */       py = tmp1.getY();
/*  713 */       vx = tmp2.getX() - px;
/*  714 */       vy = tmp2.getY() - py;
/*  715 */       int result2 = ux * vy - uy * vx + py * vx - px * vy;
/*  716 */       if (result1 >= 0) {
/*  717 */         if (result2 >= 0) {
/*  718 */           return true;
/*      */         }
/*  720 */         return false;
/*      */       } 
/*      */       
/*  723 */       if (result2 <= 0) {
/*  724 */         return true;
/*      */       }
/*  726 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  730 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isClosingPoint(Point p) {
/*  746 */     if (this.total_count < 3)
/*  747 */       return false; 
/*  748 */     Point start = getVertex(0);
/*  749 */     int xs = start.getX();
/*  750 */     int ys = start.getY();
/*  751 */     int x = p.getX();
/*  752 */     int y = p.getY();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  759 */     return (xs - this.region <= x && xs + this.region >= x && ys - this.region <= y && ys + this.region >= y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isClosed() {
/*  767 */     return this.closed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllVerticesToFeaturePoints() {
/*  774 */     Vector tmp = new Vector();
/*  775 */     Enumeration e = this.all_vertices.elements();
/*      */     
/*  777 */     while (e.hasMoreElements()) {
/*  778 */       FeaturePoint fp = new FeaturePoint(e.nextElement());
/*  779 */       this.featurePoints.add(fp);
/*  780 */       tmp.add(fp);
/*      */     } 
/*  782 */     this.fp_count = this.total_count;
/*  783 */     this.all_vertices = tmp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDashed(boolean dashed) {
/*  794 */     this.dashed = dashed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDashed() {
/*  802 */     return this.dashed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void preparePolygon(int sample_rate, int range, boolean simple_ROS) {
/*  816 */     Vector samplePoints = getSample(sample_rate);
/*      */     
/*  818 */     int sample_size = samplePoints.size();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  823 */     double[][] eigenvectors = new double[2][2];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  829 */     double epsilon = 1.0D;
/*      */ 
/*      */ 
/*      */     
/*  833 */     double[][] tmp_evec = new double[2][2];
/*      */ 
/*      */ 
/*      */     
/*  837 */     double polygon_length = getLength();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  845 */     int size = this.featurePoints.size();
/*      */     
/*  847 */     for (int i = 0; i < this.fp_count; i++) {
/*  848 */       double tangent_eva, normal_eva, tmp_t_eva, tmp_n_eva; FeaturePoint feature = this.featurePoints.elementAt(i);
/*  849 */       Point pred = this.featurePoints.elementAt((i - 1 + size) % size);
/*  850 */       Point succ = this.featurePoints.elementAt((i + 1) % size);
/*      */ 
/*      */       
/*  853 */       int index = getIndex(feature);
/*  854 */       Vector ros = new Vector();
/*  855 */       if (!simple_ROS) {
/*      */ 
/*      */         
/*  858 */         int pred_index = getIndex(pred);
/*  859 */         int start_index = pred_index;
/*  860 */         int end_index = getIndex(feature);
/*  861 */         int diff = 0;
/*  862 */         while (start_index != end_index) {
/*  863 */           diff++;
/*  864 */           start_index = (start_index + 1) % this.total_count;
/*      */         } 
/*  866 */         double[] segments = new double[diff];
/*  867 */         start_index = pred_index;
/*      */         
/*  869 */         Point tmp1 = pred;
/*  870 */         int counter = 0;
/*  871 */         double length = 0.0D;
/*  872 */         while (counter != diff) {
/*  873 */           start_index = (pred_index + counter + 1) % this.total_count;
/*  874 */           Point tmp2 = getVertex(start_index);
/*  875 */           segments[counter] = 
/*  876 */             Math.sqrt(Math.pow((tmp1.getX() - tmp2.getX()), 2.0D) + Math.pow((tmp1.getY() - tmp2.getY()), 2.0D));
/*  877 */           length += segments[counter];
/*  878 */           counter++;
/*  879 */           tmp1 = tmp2;
/*      */         } 
/*  881 */         double part_length = length / range;
/*  882 */         ros.add(pred);
/*  883 */         counter = 0;
/*  884 */         double bla = segments[0];
/*  885 */         double current_length = part_length;
/*      */         
/*      */         int k;
/*  888 */         for (k = 1; k < range; k++) {
/*  889 */           while (bla < current_length) {
/*  890 */             counter++;
/*  891 */             bla += segments[counter];
/*      */           } 
/*  893 */           double missing_length = current_length - bla + segments[counter];
/*  894 */           double ratio = missing_length / segments[counter];
/*  895 */           tmp1 = getVertex((pred_index + counter) % this.total_count);
/*  896 */           Point tmp2 = getVertex((pred_index + counter + 1) % this.total_count);
/*  897 */           double x_slope = (tmp2.getX() - tmp1.getX());
/*  898 */           double y_slope = (tmp2.getY() - tmp1.getY());
/*  899 */           ros.add(new Point((int)(x_slope * ratio + tmp1.getX() + 0.5D), 
/*  900 */                 (int)(y_slope * ratio + tmp1.getY() + 0.5D)));
/*  901 */           current_length += part_length;
/*      */         } 
/*  903 */         ros.add(feature);
/*  904 */         start_index = index;
/*  905 */         end_index = getIndex(succ);
/*  906 */         diff = 0;
/*  907 */         while (start_index != end_index) {
/*  908 */           diff++;
/*  909 */           start_index = (start_index + 1) % this.total_count;
/*      */         } 
/*  911 */         segments = new double[diff];
/*  912 */         start_index = index;
/*  913 */         tmp1 = feature;
/*  914 */         counter = 0;
/*  915 */         length = 0.0D;
/*  916 */         while (counter != diff) {
/*  917 */           start_index = (index + counter + 1) % this.total_count;
/*  918 */           Point tmp2 = getVertex(start_index);
/*  919 */           segments[counter] = 
/*  920 */             Math.sqrt(Math.pow((tmp1.getX() - tmp2.getX()), 2.0D) + Math.pow((tmp1.getY() - tmp2.getY()), 2.0D));
/*  921 */           length += segments[counter];
/*  922 */           counter++;
/*  923 */           tmp1 = tmp2;
/*      */         } 
/*  925 */         part_length = length / range;
/*  926 */         counter = 0;
/*  927 */         bla = segments[0];
/*  928 */         current_length = part_length;
/*  929 */         for (k = 1; k < range; k++) {
/*  930 */           while (bla < current_length) {
/*  931 */             counter++;
/*  932 */             bla += segments[counter];
/*      */           } 
/*  934 */           double missing_length = current_length - bla + segments[counter];
/*  935 */           double ratio = missing_length / segments[counter];
/*  936 */           tmp1 = getVertex((index + counter) % this.total_count);
/*  937 */           Point tmp2 = getVertex((index + counter + 1) % this.total_count);
/*  938 */           double x_slope = (tmp2.getX() - tmp1.getX());
/*  939 */           double y_slope = (tmp2.getY() - tmp1.getY());
/*  940 */           ros.add(new Point((int)(x_slope * ratio + tmp1.getX() + 0.5D), 
/*  941 */                 (int)(y_slope * ratio + tmp1.getY() + 0.5D)));
/*  942 */           current_length += part_length;
/*      */         } 
/*  944 */         ros.add(succ);
/*      */       }
/*      */       else {
/*      */         
/*  948 */         for (int k = 0; k < 2 * range + 1; k++) {
/*  949 */           ros.add(samplePoints.elementAt((index - range + sample_size + k) % sample_size));
/*      */         }
/*      */       } 
/*      */       
/*  953 */       double[][] covariance = Covariance.covariance(ros);
/*      */       
/*  955 */       double[] eigenvalues = Eigenvalue.hqr2(covariance, eigenvectors);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  971 */       double[] bisector = Covariance.getBisector(ros);
/*  972 */       double dot1 = Math.abs(Covariance.dotProduct(eigenvectors[0][0], eigenvectors[1][0], bisector[0], bisector[1]));
/*  973 */       double dot2 = Math.abs(Covariance.dotProduct(eigenvectors[0][1], eigenvectors[1][1], bisector[0], bisector[1]));
/*      */       
/*  975 */       if (dot1 > dot2) {
/*  976 */         tangent_eva = eigenvalues[0];
/*  977 */         normal_eva = eigenvalues[1];
/*      */       } else {
/*      */         
/*  980 */         tangent_eva = eigenvalues[1];
/*  981 */         normal_eva = eigenvalues[0];
/*      */       } 
/*      */ 
/*      */       
/*  985 */       int v_index = getIndex(feature);
/*  986 */       Polygon help = new Polygon(this, v_index);
/*  987 */       if (help.contains(feature)) {
/*  988 */         feature.setConcave();
/*      */       } else {
/*  990 */         feature.setConvex();
/*  991 */       }  if (feature.getConvex()) {
/*  992 */         epsilon = 1.0D;
/*      */       } else {
/*  994 */         epsilon = -1.0D;
/*      */       } 
/*      */       
/*  997 */       feature.setFeat_var(epsilon * normal_eva / (normal_eva + tangent_eva));
/*      */ 
/*      */ 
/*      */       
/* 1001 */       Vector rol = new Vector();
/* 1002 */       Vector ror = new Vector();
/* 1003 */       ror.add(feature);
/* 1004 */       for (int j = 0; j < range; j++) {
/* 1005 */         rol.add(ros.elementAt(j));
/* 1006 */         ror.add((FeaturePoint)ros.elementAt(j + range + 1));
/*      */       } 
/* 1008 */       rol.add(feature);
/* 1009 */       double[][] cov_rol = Covariance.covariance(rol);
/* 1010 */       double[][] cov_ror = Covariance.covariance(ror);
/*      */       
/* 1012 */       double[] tmp_eva = Eigenvalue.hqr2(cov_rol, tmp_evec);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1022 */       bisector = Covariance.getBisector(rol);
/* 1023 */       dot1 = Math.abs(Covariance.dotProduct(tmp_evec[0][0], tmp_evec[1][0], bisector[0], bisector[1]));
/* 1024 */       dot2 = Math.abs(Covariance.dotProduct(tmp_evec[0][1], tmp_evec[1][1], bisector[0], bisector[1]));
/* 1025 */       if (dot1 > dot2) {
/* 1026 */         tmp_t_eva = tmp_eva[0];
/* 1027 */         tmp_n_eva = tmp_eva[1];
/*      */       } else {
/*      */         
/* 1030 */         tmp_t_eva = tmp_eva[1];
/* 1031 */         tmp_n_eva = tmp_eva[0];
/*      */       } 
/* 1033 */       double sigma_rol = tmp_n_eva / (tmp_n_eva + tmp_t_eva);
/* 1034 */       feature.setL_feat_var(sigma_rol);
/*      */       
/* 1036 */       tmp_eva = Eigenvalue.hqr2(cov_ror, tmp_evec);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1046 */       bisector = Covariance.getBisector(ror);
/* 1047 */       dot1 = Math.abs(Covariance.dotProduct(tmp_evec[0][0], tmp_evec[1][0], bisector[0], bisector[1]));
/* 1048 */       dot2 = Math.abs(Covariance.dotProduct(tmp_evec[0][1], tmp_evec[1][1], bisector[0], bisector[1]));
/* 1049 */       if (dot1 > dot2) {
/* 1050 */         tmp_t_eva = tmp_eva[0];
/* 1051 */         tmp_n_eva = tmp_eva[1];
/*      */       } else {
/*      */         
/* 1054 */         tmp_t_eva = tmp_eva[1];
/* 1055 */         tmp_n_eva = tmp_eva[0];
/*      */       } 
/* 1057 */       double sigma_ror = tmp_n_eva / (tmp_n_eva + tmp_t_eva);
/* 1058 */       feature.setR_feat_var(sigma_ror);
/*      */       
/* 1060 */       feature.setSide_var((sigma_rol + sigma_ror) / 2.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1067 */       double rol_length = getLengthBetween(pred, feature);
/*      */       
/* 1069 */       feature.setL_size(rol_length / polygon_length);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1074 */       double ror_length = getLengthBetween(feature, succ);
/*      */       
/* 1076 */       feature.setR_size(ror_length / polygon_length);
/*      */       
/* 1078 */       feature.setFeat_size((feature.getL_size() + feature.getR_size()) / 2.0D);
/* 1079 */       feature.setPrepared(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private double getLengthBetween(Point a, Point b) {
/* 1088 */     double length = 0.0D;
/* 1089 */     int start_index = getIndex(a);
/* 1090 */     int end_index = getIndex(b);
/*      */     
/* 1092 */     Point tmp1 = a;
/* 1093 */     while (start_index != end_index) {
/* 1094 */       start_index = (start_index + 1) % this.total_count;
/* 1095 */       Point tmp2 = getVertex(start_index);
/* 1096 */       length += Math.sqrt(Math.pow((tmp1.getX() - tmp2.getX()), 2.0D) + Math.pow((tmp1.getY() - tmp2.getY()), 2.0D));
/* 1097 */       tmp1 = tmp2;
/*      */     } 
/* 1099 */     return length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void changeSize(double factor) {
/* 1113 */     for (int i = 0; i < this.total_count; i++) {
/* 1114 */       Point p = getVertex(i);
/* 1115 */       int new_x = p.getX();
/* 1116 */       int new_y = p.getY();
/* 1117 */       new_x = (int)(new_x * factor + 0.5D);
/* 1118 */       new_y = (int)(new_y * factor + 0.5D);
/* 1119 */       p.setX(new_x);
/* 1120 */       p.setY(new_y);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteCorrespondences() {
/* 1128 */     for (int i = 0; i < this.total_count; i++) {
/* 1129 */       ((Point)this.all_vertices.elementAt(i)).clearCorrespondence();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1138 */     StringBuffer buff = new StringBuffer();
/* 1139 */     Enumeration e = this.all_vertices.elements();
/* 1140 */     while (e.hasMoreElements()) {
/* 1141 */       buff.append(e.nextElement().toString());
/*      */     }
/* 1143 */     if (this.closed)
/* 1144 */       buff.append("\n The polygon is closed."); 
/* 1145 */     return buff.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateFeaturePointCount() {
/* 1152 */     this.fp_count = this.featurePoints.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void paint(Graphics g) {
/* 1162 */     Point p = this.all_vertices.firstElement();
/* 1163 */     if (!this.dashed) {
/* 1164 */       for (int i = 1; i < this.total_count; i++) {
/* 1165 */         Point q = this.all_vertices.elementAt(i);
/* 1166 */         Bresenham.draw(p, q, g, this.factor);
/* 1167 */         p = q;
/*      */       } 
/* 1169 */       if (this.closed) {
/* 1170 */         Point q = this.all_vertices.firstElement();
/* 1171 */         Bresenham.draw(p, q, g, this.factor);
/*      */       } 
/*      */     } else {
/*      */       
/* 1175 */       for (int i = 1; i < this.total_count; i++) {
/* 1176 */         Point q = this.all_vertices.elementAt(i);
/* 1177 */         Bresenham.dashedLine(p, q, g, this.factor);
/* 1178 */         p = q;
/*      */       } 
/* 1180 */       if (this.closed) {
/* 1181 */         Point q = this.all_vertices.firstElement();
/* 1182 */         Bresenham.dashedLine(p, q, g, this.factor);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object clone() throws CloneNotSupportedException {
/* 1192 */     Polygon clone = (Polygon)super.clone();
/* 1193 */     clone.total_count = this.total_count;
/* 1194 */     clone.featurePoints = (Vector)this.featurePoints.clone();
/* 1195 */     clone.sample_rate = this.sample_rate;
/* 1196 */     if (this.lastsample != null) {
/* 1197 */       clone.lastsample = (Vector)this.lastsample.clone();
/*      */     } else {
/* 1199 */       clone.lastsample = null;
/* 1200 */     }  clone.closed = this.closed;
/* 1201 */     clone.changed = this.changed;
/* 1202 */     clone.dashed = this.dashed;
/* 1203 */     clone.region = this.region;
/* 1204 */     return clone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toSVG() {
/* 1212 */     StringBuffer buff = new StringBuffer();
/* 1213 */     buff.append("\t\t<path");
/* 1214 */     buff.append(toSVGPath());
/* 1215 */     buff.append("\n");
/* 1216 */     buff.append("\t\t fill=\"none\"");
/* 1217 */     buff.append(" stroke=\"black\" stroke-width=\"1\" />\n");
/* 1218 */     return new String(buff);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toSVGPath() {
/* 1227 */     StringBuffer buff = new StringBuffer();
/* 1228 */     Point p = this.all_vertices.firstElement();
/* 1229 */     buff.append("\t\t\t d=\"M " + p.getX() + " " + p.getY() + " ");
/* 1230 */     for (int i = 1; i < this.all_vertices.size(); i++) {
/* 1231 */       if (i % 3 == 0) {
/* 1232 */         buff.append("\n\t\t\t");
/*      */       }
/* 1234 */       p = this.all_vertices.elementAt(i);
/* 1235 */       buff.append("L " + p.getX() + " " + p.getY() + " ");
/*      */     } 
/* 1237 */     if (!this.closed) {
/* 1238 */       p = this.all_vertices.lastElement();
/* 1239 */       buff.append("L " + p.getX() + " " + p.getY() + " z\" ");
/*      */     } else {
/*      */       
/* 1242 */       buff.append("\n\t\t\tz\" ");
/* 1243 */     }  return new String(buff);
/*      */   }
/*      */   
/*      */   public String toSaveFormat() {
/* 1247 */     StringBuffer buff = new StringBuffer();
/* 1248 */     buff.append("2D-Polygon for Morphing\n");
/* 1249 */     buff.append("listing all vertices as x and y coordinates now:\n\n");
/* 1250 */     if (isClosed()) {
/* 1251 */       buff.append("is closed\n");
/*      */     } else {
/* 1253 */       buff.append("is not closed\n");
/* 1254 */     }  buff.append("\n");
/*      */     
/* 1256 */     for (int i = 0; i < this.total_count; i++) {
/* 1257 */       Point p = this.all_vertices.elementAt(i);
/* 1258 */       buff.append(String.valueOf(p.getX()) + "," + p.getY() + "\n");
/*      */     } 
/*      */     
/* 1261 */     return new String(buff);
/*      */   }
/*      */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\shapes\Polygon.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */