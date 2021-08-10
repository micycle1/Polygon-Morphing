package micycle.polygonmorphing.shapes;

import java.awt.Graphics;
import java.util.Enumeration;
import java.util.Vector;

import micycle.polygonmorphing.math.Covariance;
import micycle.polygonmorphing.math.Eigenvalue;
import micycle.polygonmorphing.tools.Bresenham;

public class Polygon extends GraphicObject implements Cloneable {
  public static final boolean DASHED = true;
  
  private int fp_count;
  
  private int total_count;
  
  private Vector<Point> featurePoints;
  
  private Vector<Point> all_vertices;
  
  private int sample_rate = 0;
  
  private Vector<Point> lastsample = null;
  
  private boolean closed = false;
  
  private boolean changed = false;
  
  private boolean dashed = false;
  
  private int region;
  
  public Polygon() {
    super(1);
    this.total_count = 0;
    this.featurePoints = new Vector();
    this.all_vertices = new Vector();
  }
  
  public Polygon(Point start, int factor) {
    super(factor);
    this.total_count = 0;
    this.fp_count = 0;
    this.featurePoints = new Vector();
    this.all_vertices = new Vector();
    addVertex(start);
  }
  
  public Polygon(Point start, int region, int factor) {
    this(start, factor);
    setRegion(region);
  }
  
  public Polygon(Point start, int region, int factor, boolean dashed) {
    this(start, factor);
    setRegion(region);
    setDashed(dashed);
  }
  
  public Polygon(Polygon original) {
    this.featurePoints = new Vector();
    this.all_vertices = new Vector();
    this.total_count = 0;
    this.fp_count = 0;
    Enumeration<Point> e = original.all_vertices.elements();
    while (e.hasMoreElements())
      addVertex(e.nextElement()); 
    if (original.isClosed())
      close(); 
  }
  
  public Polygon(Polygon p, int i) {
    int vertex_count = p.getCount();
    if (i < 0 || i >= vertex_count)
      throw new IllegalArgumentException("index i= " + i + " of removed vertex is out of bounds! \nFor this polygon it must be between (including) 0 and" + (p.getCount() - 1) + "!"); 
    this.total_count = 0;
    this.fp_count = 0;
    this.featurePoints = new Vector();
    this.all_vertices = new Vector();
    int j;
    for (j = 0; j < i; j++)
      addVertex(p.getVertex(j)); 
    for (j = i + 1; j < vertex_count; j++)
      addVertex(p.getVertex(j)); 
    if (p.isClosed())
      close(); 
  }
  
  public void addVertex(Point p) {
    if (p instanceof FeaturePoint) {
      FeaturePoint fp = new FeaturePoint(p);
      if (this.featurePoints.add(fp)) {
        this.all_vertices.add(fp);
        this.total_count++;
        this.fp_count++;
        this.changed = true;
      } 
    } else {
      this.all_vertices.add(new Point(p));
      this.total_count++;
    } 
  }
  
  public void addVertices(String point_string) {
    String[] new_lines = point_string.split("\n");
    for (int i = 0; i < new_lines.length; i++) {
      String[] point_rep = new_lines[i].split(",");
      Point p = new Point((new Integer(point_rep[0])).intValue(), (new Integer(point_rep[1])).intValue());
      addVertex(p);
    } 
  }
  
  public Point getVertex(int index) {
    if (index < 0 || index >= this.total_count)
      throw new IllegalArgumentException("Given index (" + index + ") is out of legal values."); 
    return this.all_vertices.elementAt(index);
  }
  
  public boolean isVertex(Point p) {
    boolean is_vertex = false;
    Enumeration<Point> e = this.all_vertices.elements();
    while (!is_vertex && e.hasMoreElements()) {
      Point q = e.nextElement();
      is_vertex = p.equals(q);
    } 
    return is_vertex;
  }
  
  public void addVertexBehind(Point p, Point q) {
    int index = getIndex(q);
    if (index < 0)
      throw new IllegalArgumentException("Point " + q.toString() + " is no vortex in this polygon."); 
    if (p instanceof FeaturePoint) {
      this.fp_count++;
      int f_index = -1;
      if (q instanceof FeaturePoint) {
        f_index = getFeaturePointIndex((FeaturePoint)q);
        this.featurePoints.insertElementAt(p, f_index + 1);
      } else {
        Point tmp;
        f_index = index;
        do {
          f_index = (f_index - 1 + this.total_count) % this.total_count;
          tmp = this.all_vertices.elementAt(f_index);
        } while (!(tmp instanceof FeaturePoint));
        f_index = getFeaturePointIndex((FeaturePoint)tmp);
        this.featurePoints.insertElementAt(p, f_index + 1);
      } 
    } 
    this.total_count++;
    this.all_vertices.insertElementAt(p, index + 1);
  }
  
  public void addVertexBefore(Point p, Point q) {
    int index = getIndex(q);
    if (index < 0)
      throw new IllegalArgumentException("Point " + q.toString() + " is no vortex in this polygon."); 
    if (p instanceof FeaturePoint) {
      this.fp_count++;
      int f_index = -1;
      if (q instanceof FeaturePoint) {
        f_index = getFeaturePointIndex((FeaturePoint)q);
        this.featurePoints.insertElementAt(p, f_index);
      } else {
        Point tmp;
        f_index = index;
        do {
          f_index = (f_index - 1 + this.total_count) % this.total_count;
          tmp = this.all_vertices.elementAt(f_index);
        } while (!(tmp instanceof FeaturePoint));
        f_index = getFeaturePointIndex((FeaturePoint)tmp);
        this.featurePoints.insertElementAt(p, f_index);
      } 
    } 
    this.total_count++;
    this.all_vertices.insertElementAt(p, index);
  }
  
  public void addVertexBetween(Point p, Point q, Point r) {
    int index_q = -1;
    int index_r = -1;
    int i = 0;
    while (index_q == -1 && i < this.total_count) {
      if (q.equals(this.all_vertices.elementAt(i)))
        index_q = i; 
      i++;
    } 
    if (index_q < 0)
      throw new IllegalArgumentException("Point " + q.toString() + " is no vortex in this polygon."); 
    i = 0;
    while (index_r == -1 && i < this.total_count) {
      if (r.equals(this.all_vertices.elementAt(i)))
        index_r = i; 
      i++;
    } 
    if (index_r < 0)
      throw new IllegalArgumentException("Point " + r.toString() + " is no vortex in this polygon."); 
    if (index_q < index_r) {
      if (index_r - index_q == 1) {
        if (p instanceof FeaturePoint) {
          FeaturePoint fp = new FeaturePoint(p);
          this.total_count++;
          this.all_vertices.insertElementAt(fp, index_r);
          this.featurePoints.insertElementAt(fp, 0);
        } 
      } else if (index_q == 0 && index_r == this.total_count - 1) {
        addVertex(p);
      } else {
        throw new IllegalArgumentException("Points " + p.toString() + 
            " and " + q.toString() + " are not next to each other.");
      } 
    } else if (index_q - index_r == 1) {
      this.total_count++;
      this.featurePoints.insertElementAt(new FeaturePoint(p), index_q);
    } else if (index_r == 0 && index_q == this.total_count - 1) {
      addVertex(p);
    } else {
      throw new IllegalArgumentException("Points " + p.toString() + 
          " and " + q.toString() + " are not next to each other.");
    } 
  }
  
  public Vector getFeaturePoints() {
    return this.featurePoints;
  }
  
  public Vector getAllVertices() {
    return this.all_vertices;
  }
  
  public Vector getSample(int sample_rate) {
    if (sample_rate == this.sample_rate && this.lastsample != null && !this.changed)
      return this.lastsample; 
    if (this.total_count == 0)
      return null; 
    if (this.total_count == 1) {
      this.lastsample = this.all_vertices;
      this.changed = false;
    } else {
      this.lastsample = new Vector();
      Enumeration<Point> e = this.all_vertices.elements();
      Point start = e.nextElement();
      while (e.hasMoreElements()) {
        Point end = e.nextElement();
        this.lastsample.addAll(Bresenham.semiUniformSample(start, end, sample_rate));
        this.lastsample.removeElementAt(this.lastsample.size() - 1);
        start = end;
      } 
      if (this.closed) {
        Point end = this.all_vertices.firstElement();
        this.lastsample.addAll(Bresenham.semiUniformSample(start, end, sample_rate));
        this.lastsample.removeElementAt(this.lastsample.size() - 1);
      } 
    } 
    this.sample_rate = sample_rate;
    this.changed = false;
    return this.lastsample;
  }
  
  public int[] getSampleArray(int sample_rate) {
    if (this.sample_rate != sample_rate || this.lastsample == null || this.changed)
      getSample(sample_rate); 
    int length = this.lastsample.size();
    int[] return_array = new int[2 * length];
    for (int i = 0; i < length; i++) {
      Point a = this.lastsample.elementAt(i);
      return_array[i * 2] = a.getX();
      return_array[i * 2 + 1] = a.getY();
    } 
    return return_array;
  }
  
  public double getLength() {
    Point tmp1 = this.all_vertices.firstElement();
    double length = 0.0D;
    for (int i = 1; i < this.total_count; i++) {
      Point tmp2 = this.all_vertices.elementAt(i);
      double xlen = (tmp2.getX() - tmp1.getX());
      double ylen = (tmp2.getY() - tmp1.getY());
      length += Math.sqrt(xlen * xlen + ylen * ylen);
      tmp1 = tmp2;
    } 
    if (this.closed) {
      Point tmp2 = this.all_vertices.firstElement();
      double xlen = (tmp2.getX() - tmp1.getX());
      double ylen = (tmp2.getY() - tmp1.getY());
      length += Math.sqrt(xlen * xlen + ylen * ylen);
    } 
    return length;
  }
  
  public void close() {
    this.closed = true;
    this.changed = true;
  }
  
  public FeaturePoint getFeaturePoint(int index) {
    if (index < 0 || index >= this.fp_count)
      throw new IllegalArgumentException("desired vertex index " + index + " is out bounds!"); 
    return (FeaturePoint) this.featurePoints.elementAt(index);
  }
  
  public int getFeaturePointIndex(FeaturePoint fp) {
    int index = -1;
    this.fp_count = this.featurePoints.size();
    int counter = 0;
    while (index == -1 && counter < this.fp_count) {
      if (fp == (FeaturePoint)this.featurePoints.elementAt(counter))
        index = counter; 
      counter++;
    } 
    if (index == -1) {
      counter = 0;
      while (index == -1 && counter < this.fp_count) {
        if (fp.equals(this.featurePoints.elementAt(counter)))
          index = counter; 
        counter++;
      } 
    } 
    return index;
  }
  
  public int getIndex(Point p) {
    int index = -1;
    int counter = 0;
    while (index == -1 && counter < this.total_count) {
      if (p == (Point)this.all_vertices.elementAt(counter))
        index = counter; 
      counter++;
    } 
    if (index == -1) {
      counter = 0;
      while (index == -1 && counter < this.total_count) {
        if (p.equals(this.all_vertices.elementAt(counter)))
          index = counter; 
        counter++;
      } 
    } 
    return index;
  }
  
  public int getCount() {
    return this.all_vertices.size();
  }
  
  public int getFeaturePointCount() {
    return this.featurePoints.size();
  }
  
  public void setRegion(int region) {
    if (region < 0)
      throw new IllegalArgumentException("Size of the region must be >=0 !"); 
    this.region = region;
  }
  
  public int getRegion() {
    return this.region;
  }
  
  public boolean contains(Point p) {
    int x = p.getX();
    int y = p.getY();
    boolean contains = false;
    int x1 = ((Point)this.all_vertices.elementAt(this.total_count - 1)).getX();
    int y1 = ((Point)this.all_vertices.elementAt(this.total_count - 1)).getY();
    int x2 = ((Point)this.all_vertices.elementAt(0)).getX();
    int y2 = ((Point)this.all_vertices.elementAt(0)).getY();
    boolean start_above = (y1 >= y);
    for (int i = 1; i < this.total_count; i++) {
      boolean bool = (y2 >= y);
      if (start_above != bool)
        if ((y2 - y) * (x2 - x1) <= (y2 - y1) * (x2 - x)) {
          if (bool)
            contains = !contains; 
        } else if (!bool) {
          contains = !contains;
        }  
      start_above = bool;
      y1 = y2;
      x1 = x2;
      y2 = ((Point)this.all_vertices.elementAt(i)).getY();
      x2 = ((Point)this.all_vertices.elementAt(i)).getX();
    } 
    boolean end_above = (y2 >= y);
    if (start_above != end_above)
      if ((y2 - y) * (x2 - x1) <= (y2 - y1) * (x2 - x)) {
        if (end_above)
          contains = !contains; 
      } else if (!end_above) {
        contains = !contains;
      }  
    return contains;
  }
  
  public boolean isConvex(Point p) {
    int index;
    if ((index = this.featurePoints.indexOf(p)) == -1)
      throw new IllegalArgumentException("Point not contained in polygon!"); 
    if (this.closed || (index - 2 >= 0 && index + 2 < this.total_count)) {
      int ux = p.getX();
      int uy = p.getY();
      Point tmp1 = this.featurePoints.elementAt((index - 2 + this.total_count) % this.total_count);
      Point tmp2 = this.featurePoints.elementAt((index - 1 + this.total_count) % this.total_count);
      int px = tmp1.getX();
      int py = tmp1.getY();
      int vx = tmp2.getX() - px;
      int vy = tmp2.getY() - py;
      int result1 = ux * vy - uy * vx + py * vx - px * vy;
      tmp1 = this.featurePoints.elementAt((index + 1 + this.total_count) % this.total_count);
      tmp2 = this.featurePoints.elementAt((index + 2 + this.total_count) % this.total_count);
      px = tmp1.getX();
      py = tmp1.getY();
      vx = tmp2.getX() - px;
      vy = tmp2.getY() - py;
      int result2 = ux * vy - uy * vx + py * vx - px * vy;
      if (result1 >= 0) {
        if (result2 >= 0)
          return true; 
        return false;
      } 
      if (result2 <= 0)
        return true; 
      return false;
    } 
    return true;
  }
  
  public boolean isClosingPoint(Point p) {
    if (this.total_count < 3)
      return false; 
    Point start = getVertex(0);
    int xs = start.getX();
    int ys = start.getY();
    int x = p.getX();
    int y = p.getY();
    return (xs - this.region <= x && xs + this.region >= x && ys - this.region <= y && ys + this.region >= y);
  }
  
  public boolean isClosed() {
    return this.closed;
  }
  
  public void setAllVerticesToFeaturePoints() {
    Vector<Point> tmp = new Vector();
    Enumeration<Point> e = this.all_vertices.elements();
    while (e.hasMoreElements()) {
      FeaturePoint fp = new FeaturePoint(e.nextElement());
      this.featurePoints.add(fp);
      tmp.add(fp);
    } 
    this.fp_count = this.total_count;
    this.all_vertices = tmp;
  }
  
  public void setDashed(boolean dashed) {
    this.dashed = dashed;
  }
  
  public boolean isDashed() {
    return this.dashed;
  }
  
  public void preparePolygon(int sample_rate, int range, boolean simple_ROS) {
    Vector samplePoints = getSample(sample_rate);
    int sample_size = samplePoints.size();
    double[][] eigenvectors = new double[2][2];
    double epsilon = 1.0D;
    double[][] tmp_evec = new double[2][2];
    double polygon_length = getLength();
    int size = this.featurePoints.size();
    for (int i = 0; i < this.fp_count; i++) {
      double tangent_eva, normal_eva, tmp_t_eva, tmp_n_eva;
      FeaturePoint feature = (FeaturePoint) this.featurePoints.elementAt(i);
      Point pred = this.featurePoints.elementAt((i - 1 + size) % size);
      Point succ = this.featurePoints.elementAt((i + 1) % size);
      int index = getIndex(feature);
      Vector ros = new Vector();
      if (!simple_ROS) {
        int pred_index = getIndex(pred);
        int start_index = pred_index;
        int end_index = getIndex(feature);
        int diff = 0;
        while (start_index != end_index) {
          diff++;
          start_index = (start_index + 1) % this.total_count;
        } 
        double[] segments = new double[diff];
        start_index = pred_index;
        Point tmp1 = pred;
        int counter = 0;
        double length = 0.0D;
        while (counter != diff) {
          start_index = (pred_index + counter + 1) % this.total_count;
          Point tmp2 = getVertex(start_index);
          segments[counter] = 
            Math.sqrt(Math.pow((tmp1.getX() - tmp2.getX()), 2.0D) + Math.pow((tmp1.getY() - tmp2.getY()), 2.0D));
          length += segments[counter];
          counter++;
          tmp1 = tmp2;
        } 
        double part_length = length / range;
        ros.add(pred);
        counter = 0;
        double bla = segments[0];
        double current_length = part_length;
        int k;
        for (k = 1; k < range; k++) {
          while (bla < current_length) {
            counter++;
            bla += segments[counter];
          } 
          double missing_length = current_length - bla + segments[counter];
          double ratio = missing_length / segments[counter];
          tmp1 = getVertex((pred_index + counter) % this.total_count);
          Point tmp2 = getVertex((pred_index + counter + 1) % this.total_count);
          double x_slope = (tmp2.getX() - tmp1.getX());
          double y_slope = (tmp2.getY() - tmp1.getY());
          ros.add(new Point((int)(x_slope * ratio + tmp1.getX() + 0.5D), 
                (int)(y_slope * ratio + tmp1.getY() + 0.5D)));
          current_length += part_length;
        } 
        ros.add(feature);
        start_index = index;
        end_index = getIndex(succ);
        diff = 0;
        while (start_index != end_index) {
          diff++;
          start_index = (start_index + 1) % this.total_count;
        } 
        segments = new double[diff];
        start_index = index;
        tmp1 = feature;
        counter = 0;
        length = 0.0D;
        while (counter != diff) {
          start_index = (index + counter + 1) % this.total_count;
          Point tmp2 = getVertex(start_index);
          segments[counter] = 
            Math.sqrt(Math.pow((tmp1.getX() - tmp2.getX()), 2.0D) + Math.pow((tmp1.getY() - tmp2.getY()), 2.0D));
          length += segments[counter];
          counter++;
          tmp1 = tmp2;
        } 
        part_length = length / range;
        counter = 0;
        bla = segments[0];
        current_length = part_length;
        for (k = 1; k < range; k++) {
          while (bla < current_length) {
            counter++;
            bla += segments[counter];
          } 
          double missing_length = current_length - bla + segments[counter];
          double ratio = missing_length / segments[counter];
          tmp1 = getVertex((index + counter) % this.total_count);
          Point tmp2 = getVertex((index + counter + 1) % this.total_count);
          double x_slope = (tmp2.getX() - tmp1.getX());
          double y_slope = (tmp2.getY() - tmp1.getY());
          ros.add(new Point((int)(x_slope * ratio + tmp1.getX() + 0.5D), 
                (int)(y_slope * ratio + tmp1.getY() + 0.5D)));
          current_length += part_length;
        } 
        ros.add(succ);
      } else {
        for (int k = 0; k < 2 * range + 1; k++)
          ros.add(samplePoints.elementAt((index - range + sample_size + k) % sample_size)); 
      } 
      double[][] covariance = Covariance.covariance(ros);
      double[] eigenvalues = Eigenvalue.hqr2(covariance, eigenvectors);
      double[] bisector = Covariance.getBisector(ros);
      double dot1 = Math.abs(Covariance.dotProduct(eigenvectors[0][0], eigenvectors[1][0], bisector[0], bisector[1]));
      double dot2 = Math.abs(Covariance.dotProduct(eigenvectors[0][1], eigenvectors[1][1], bisector[0], bisector[1]));
      if (dot1 > dot2) {
        tangent_eva = eigenvalues[0];
        normal_eva = eigenvalues[1];
      } else {
        tangent_eva = eigenvalues[1];
        normal_eva = eigenvalues[0];
      } 
      int v_index = getIndex(feature);
      Polygon help = new Polygon(this, v_index);
      if (help.contains(feature)) {
        feature.setConcave();
      } else {
        feature.setConvex();
      } 
      if (feature.getConvex()) {
        epsilon = 1.0D;
      } else {
        epsilon = -1.0D;
      } 
      feature.setFeat_var(epsilon * normal_eva / (normal_eva + tangent_eva));
      Vector rol = new Vector();
      Vector ror = new Vector();
      ror.add(feature);
      for (int j = 0; j < range; j++) {
        rol.add(ros.elementAt(j));
        ror.add((Point)ros.elementAt(j + range + 1));
      } 
      rol.add(feature);
      double[][] cov_rol = Covariance.covariance(rol);
      double[][] cov_ror = Covariance.covariance(ror);
      double[] tmp_eva = Eigenvalue.hqr2(cov_rol, tmp_evec);
      bisector = Covariance.getBisector(rol);
      dot1 = Math.abs(Covariance.dotProduct(tmp_evec[0][0], tmp_evec[1][0], bisector[0], bisector[1]));
      dot2 = Math.abs(Covariance.dotProduct(tmp_evec[0][1], tmp_evec[1][1], bisector[0], bisector[1]));
      if (dot1 > dot2) {
        tmp_t_eva = tmp_eva[0];
        tmp_n_eva = tmp_eva[1];
      } else {
        tmp_t_eva = tmp_eva[1];
        tmp_n_eva = tmp_eva[0];
      } 
      double sigma_rol = tmp_n_eva / (tmp_n_eva + tmp_t_eva);
      feature.setL_feat_var(sigma_rol);
      tmp_eva = Eigenvalue.hqr2(cov_ror, tmp_evec);
      bisector = Covariance.getBisector(ror);
      dot1 = Math.abs(Covariance.dotProduct(tmp_evec[0][0], tmp_evec[1][0], bisector[0], bisector[1]));
      dot2 = Math.abs(Covariance.dotProduct(tmp_evec[0][1], tmp_evec[1][1], bisector[0], bisector[1]));
      if (dot1 > dot2) {
        tmp_t_eva = tmp_eva[0];
        tmp_n_eva = tmp_eva[1];
      } else {
        tmp_t_eva = tmp_eva[1];
        tmp_n_eva = tmp_eva[0];
      } 
      double sigma_ror = tmp_n_eva / (tmp_n_eva + tmp_t_eva);
      feature.setR_feat_var(sigma_ror);
      feature.setSide_var((sigma_rol + sigma_ror) / 2.0D);
      double rol_length = getLengthBetween(pred, feature);
      feature.setL_size(rol_length / polygon_length);
      double ror_length = getLengthBetween(feature, succ);
      feature.setR_size(ror_length / polygon_length);
      feature.setFeat_size((feature.getL_size() + feature.getR_size()) / 2.0D);
      feature.setPrepared(true);
    } 
  }
  
  private double getLengthBetween(Point a, Point b) {
    double length = 0.0D;
    int start_index = getIndex(a);
    int end_index = getIndex(b);
    Point tmp1 = a;
    while (start_index != end_index) {
      start_index = (start_index + 1) % this.total_count;
      Point tmp2 = getVertex(start_index);
      length += Math.sqrt(Math.pow((tmp1.getX() - tmp2.getX()), 2.0D) + Math.pow((tmp1.getY() - tmp2.getY()), 2.0D));
      tmp1 = tmp2;
    } 
    return length;
  }
  
  public void changeSize(double factor) {
    for (int i = 0; i < this.total_count; i++) {
      Point p = getVertex(i);
      int new_x = p.getX();
      int new_y = p.getY();
      new_x = (int)(new_x * factor + 0.5D);
      new_y = (int)(new_y * factor + 0.5D);
      p.setX(new_x);
      p.setY(new_y);
    } 
  }
  
  public void deleteCorrespondences() {
    for (int i = 0; i < this.total_count; i++)
      ((Point)this.all_vertices.elementAt(i)).clearCorrespondence(); 
  }
  
  public String toString() {
    StringBuffer buff = new StringBuffer();
    Enumeration e = this.all_vertices.elements();
    while (e.hasMoreElements())
      buff.append(e.nextElement().toString()); 
    if (this.closed)
      buff.append("\n The polygon is closed."); 
    return buff.toString();
  }
  
  public void updateFeaturePointCount() {
    this.fp_count = this.featurePoints.size();
  }
  
  public void paint(Graphics g) {
    Point p = this.all_vertices.firstElement();
    if (!this.dashed) {
      for (int i = 1; i < this.total_count; i++) {
        Point q = this.all_vertices.elementAt(i);
        Bresenham.draw(p, q, g, this.factor);
        p = q;
      } 
      if (this.closed) {
        Point q = this.all_vertices.firstElement();
        Bresenham.draw(p, q, g, this.factor);
      } 
    } else {
      for (int i = 1; i < this.total_count; i++) {
        Point q = this.all_vertices.elementAt(i);
        Bresenham.dashedLine(p, q, g, this.factor);
        p = q;
      } 
      if (this.closed) {
        Point q = this.all_vertices.firstElement();
        Bresenham.dashedLine(p, q, g, this.factor);
      } 
    } 
  }
  
  public Object clone() throws CloneNotSupportedException {
    Polygon clone = (Polygon)super.clone();
    clone.total_count = this.total_count;
    clone.featurePoints = (Vector)this.featurePoints.clone();
    clone.sample_rate = this.sample_rate;
    if (this.lastsample != null) {
      clone.lastsample = (Vector)this.lastsample.clone();
    } else {
      clone.lastsample = null;
    } 
    clone.closed = this.closed;
    clone.changed = this.changed;
    clone.dashed = this.dashed;
    clone.region = this.region;
    return clone;
  }
  
  public String toSVG() {
    StringBuffer buff = new StringBuffer();
    buff.append("\t\t<path");
    buff.append(toSVGPath());
    buff.append("\n");
    buff.append("\t\t fill=\"none\"");
    buff.append(" stroke=\"black\" stroke-width=\"1\" />\n");
    return new String(buff);
  }
  
  public String toSVGPath() {
    StringBuffer buff = new StringBuffer();
    Point p = this.all_vertices.firstElement();
    buff.append("\t\t\t d=\"M " + p.getX() + " " + p.getY() + " ");
    for (int i = 1; i < this.all_vertices.size(); i++) {
      if (i % 3 == 0)
        buff.append("\n\t\t\t"); 
      p = this.all_vertices.elementAt(i);
      buff.append("L " + p.getX() + " " + p.getY() + " ");
    } 
    if (!this.closed) {
      p = this.all_vertices.lastElement();
      buff.append("L " + p.getX() + " " + p.getY() + " z\" ");
    } else {
      buff.append("\n\t\t\tz\" ");
    } 
    return new String(buff);
  }
  
  public String toSaveFormat() {
    StringBuffer buff = new StringBuffer();
    buff.append("2D-Polygon for Morphing\n");
    buff.append("listing all vertices as x and y coordinates now:\n\n");
    if (isClosed()) {
      buff.append("is closed\n");
    } else {
      buff.append("is not closed\n");
    } 
    buff.append("\n");
    for (int i = 0; i < this.total_count; i++) {
      Point p = this.all_vertices.elementAt(i);
      buff.append(String.valueOf(p.getX()) + "," + p.getY() + "\n");
    } 
    return new String(buff);
  }
}
