package morph;

import java.util.Vector;
import math.Covariance;
import math.Eigenvalue;
import shapes.FeaturePoint;
import shapes.Point;
import shapes.Polygon;
import tools.Node;
import tools.Path;

public class MorphCalculator {
  private static int diff;
  
  public static void preparePolygon(Polygon p, int sample_rate, int range) {
    Vector fPoints = p.getFeaturePoints();
    Vector samplePoints = p.getSample(sample_rate);
    int count = fPoints.size();
    int sample_size = samplePoints.size();
    double[] feature_variation = new double[count];
    double[] side_feature_v = new double[count];
    double[] feature_size = new double[count];
    double[][] eigenvectors = new double[2][2];
    double epsilon = 1.0D;
    double[][] tmp_evec = new double[2][2];
    double polygon_length = p.getLength();
    for (int i = 0; i < count; i++) {
      double tangent_eva, normal_eva, tmp_t_eva, tmp_n_eva;
      Point feature = fPoints.elementAt(i);
      int index = samplePoints.indexOf(feature);
      Vector ros = new Vector();
      int j;
      for (j = 0; j < 2 * range + 1; j++)
        ros.add(samplePoints.elementAt((index - range + sample_size + j) % sample_size)); 
      double[][] covariance = Covariance.covariance(ros);
      double[] eigenvalues = Eigenvalue.hqr2(covariance, eigenvectors);
      Point next = ros.elementAt(range + 1);
      double xn = (feature.getX() - next.getX());
      double yn = (feature.getY() - next.getY());
      double dot1 = Math.abs(Covariance.dotProduct(eigenvectors[0][0], eigenvectors[1][0], xn, yn));
      double dot2 = Math.abs(Covariance.dotProduct(eigenvectors[0][1], eigenvectors[1][1], xn, yn));
      if (dot1 > dot2) {
        tangent_eva = eigenvalues[0];
        normal_eva = eigenvalues[1];
      } else {
        tangent_eva = eigenvalues[1];
        normal_eva = eigenvalues[0];
      } 
      FeaturePoint fp = p.getFeaturePoint(i);
      Polygon help = new Polygon(p, i);
      if (help.contains((Point)fp)) {
        fp.setConcave();
      } else {
        fp.setConvex();
      } 
      if (fp.getConvex()) {
        epsilon = 1.0D;
      } else {
        epsilon = -1.0D;
      } 
      feature_variation[i] = epsilon * normal_eva / (normal_eva + tangent_eva);
      fp.setFeat_var(feature_variation[i]);
      Vector rol = new Vector();
      Vector ror = new Vector();
      for (j = 0; j < range; j++) {
        rol.add(ros.elementAt(j));
        ror.add(ros.elementAt(j + range + 1));
      } 
      double[][] cov_rol = Covariance.covariance(rol);
      double[][] cov_ror = Covariance.covariance(ror);
      double[] tmp_eva = Eigenvalue.hqr2(cov_rol, tmp_evec);
      next = rol.elementAt(range / 2 - 1);
      xn = next.getX();
      yn = next.getY();
      next = rol.elementAt(range / 2);
      xn -= next.getX();
      yn -= next.getY();
      dot1 = Math.abs(Covariance.dotProduct(tmp_evec[0][0], tmp_evec[1][0], xn, yn));
      dot2 = Math.abs(Covariance.dotProduct(tmp_evec[0][1], tmp_evec[1][1], xn, yn));
      if (dot1 > dot2) {
        tmp_t_eva = tmp_eva[0];
        tmp_n_eva = tmp_eva[1];
      } else {
        tmp_t_eva = tmp_eva[1];
        tmp_n_eva = tmp_eva[0];
      } 
      double sigma_rol = tmp_n_eva / (tmp_n_eva + tmp_t_eva);
      tmp_eva = Eigenvalue.hqr2(cov_ror, tmp_evec);
      next = ror.elementAt(range / 2 - 1);
      xn = next.getX();
      yn = next.getY();
      next = ror.elementAt(range / 2);
      xn -= next.getX();
      yn -= next.getY();
      dot1 = Math.abs(Covariance.dotProduct(tmp_evec[0][0], tmp_evec[1][0], xn, yn));
      dot2 = Math.abs(Covariance.dotProduct(tmp_evec[0][1], tmp_evec[1][1], xn, yn));
      if (dot1 > dot2) {
        tmp_t_eva = tmp_eva[0];
        tmp_n_eva = tmp_eva[1];
      } else {
        tmp_t_eva = tmp_eva[1];
        tmp_n_eva = tmp_eva[0];
      } 
      double sigma_ror = tmp_n_eva / (tmp_n_eva + tmp_t_eva);
      side_feature_v[i] = (sigma_rol + sigma_ror) / 2.0D;
      fp.setSide_var(side_feature_v[i]);
      xn = (((Point)rol.firstElement()).getX() - ((Point)rol.lastElement()).getX());
      yn = (((Point)rol.firstElement()).getY() - ((Point)rol.lastElement()).getY());
      double rol_length = Math.sqrt(xn * xn + yn * yn);
      xn = (((Point)ror.firstElement()).getX() - ((Point)ror.lastElement()).getX());
      yn = (((Point)ror.firstElement()).getY() - ((Point)ror.lastElement()).getY());
      double ror_length = Math.sqrt(xn * xn + yn * yn);
      feature_size[i] = (rol_length + ror_length) / polygon_length / 2.0D;
      fp.setFeat_size(feature_size[i]);
    } 
  }
  
  public static Path calculateOptimalPath(Polygon s, Polygon t) {
    System.out.println("called Method calculateOptimalPath");
    int s_points = s.getCount();
    int t_points = t.getCount();
    double[][] costs = new double[s_points][t_points];
    int i;
    for (i = 0; i < s_points; i++) {
      for (int j = 0; j < t_points; j++)
        costs[i][j] = FeaturePoint.calculate_Sim_Cost(s.getFeaturePoint(i), t.getFeaturePoint(j)); 
    } 
    int[][][] nodes = new int[s_points][t_points][2];
    double[][] min_path_costs = new double[s_points][t_points];
    int index_s = -1, index_t = -1;
    for (i = 0; i < s_points; i++) {
      for (int j = 0; j < t_points; j++) {
        double min_costs = Double.MAX_VALUE;
        for (int k = 1; k < 4; k++) {
          for (int l = 1; l < 4; l++) {
            int temp_index_s = (i - k + s_points) % s_points;
            int temp_index_t = (j - l + t_points) % t_points;
            double path_costs = costs[temp_index_s][temp_index_t];
            path_costs += calcDeltaCosts(s, temp_index_s, i, t, temp_index_t, j);
            if (path_costs < min_costs) {
              min_costs = path_costs;
              index_s = temp_index_s;
              index_t = temp_index_t;
            } 
          } 
        } 
        min_path_costs[i][j] = min_costs;
        nodes[i][j][0] = index_s;
        nodes[i][j][1] = index_t;
      } 
    } 
    double complete_path_costs = 0.0D;
    Path optimalPath = new Path();
    if (!s.isClosed()) {
      int temp_index_s = s_points - 1;
      int temp_index_t = t_points - 1;
      optimalPath.add(new Node(index_s, index_t, costs[index_s][index_t]));
      while (index_s != 0 && index_t != 0) {
        index_s = nodes[temp_index_s][temp_index_t][0];
        index_t = nodes[temp_index_s][temp_index_t][1];
        if (temp_index_s < index_s)
          index_s = 0; 
        if (temp_index_t < index_t)
          index_t = 0; 
        complete_path_costs += min_path_costs[temp_index_s][temp_index_t];
        temp_index_s = index_s;
        temp_index_t = index_t;
        optimalPath.add(new Node(index_s, index_t));
      } 
    } else {
      Path path = new Path();
      double min_complete_path_costs = Double.MAX_VALUE;
      for (i = s_points - 1; i >= 0; i--) {
        for (int j = t_points - 1; j >= 0; j--) {
          boolean s_returned = false;
          boolean t_returned = false;
          int temp_index_s = i;
          int temp_index_t = j;
          do {
            path.add(new Node(index_s, index_t));
            complete_path_costs += min_path_costs[index_s][index_t];
            if (index_s > i)
              s_returned = true; 
            if (index_t > j)
              t_returned = true; 
            if (s_returned && index_s < i) {
              index_s = i;
            } else {
              index_s = nodes[temp_index_s][temp_index_t][0];
            } 
            if (t_returned && index_t < j) {
              index_t = j;
            } else {
              index_t = nodes[temp_index_s][temp_index_t][1];
            } 
            temp_index_s = index_s;
            temp_index_t = index_t;
          } while (complete_path_costs < min_complete_path_costs && index_s != i && index_t != j);
          path.add(new Node(i, j));
          if (complete_path_costs < min_complete_path_costs) {
            min_complete_path_costs = complete_path_costs;
            optimalPath = new Path(path);
            optimalPath.setCosts(min_complete_path_costs);
            optimalPath.setSimCosts(costs);
          } 
          path.clear();
        } 
      } 
    } 
    System.out.println("done! :)");
    System.out.println("Method calculateOptimalPath successful!");
    return optimalPath;
  }
  
  public static double sumDisCosts(Polygon p, int i, int j) {
    int points = p.getCount();
    if (i < 0 || j < 0 || j > points - 1 || i > points - 1)
      throw new IllegalArgumentException("Index out of bounds"); 
    double costs = 0.0D;
    if (i == j)
      return costs; 
    int index = (i + 1) % points;
    while (index != j) {
      costs += p.getFeaturePoint(index).getDisCost();
      index = (index + 1) % points;
    } 
    return costs;
  }
  
  public static double calcDeltaCosts(Polygon s, int s_i, int s_j, Polygon t, int t_i, int t_j) {
    double costs = 0.0D;
    costs += sumDisCosts(s, s_i, s_j);
    costs += sumDisCosts(t, t_i, t_j);
    costs += 1.0D * FeaturePoint.calculate_Sim_Cost(s.getFeaturePoint(s_j), t.getFeaturePoint(t_j));
    return costs;
  }
  
  public static Node[][] getAllNodes(Polygon s, Polygon t) {
    int s_count = s.getCount();
    int t_count = t.getCount();
    Node[][] allNodes = new Node[s_count][t_count];
    double node_costs = 0.0D;
    for (int i = 0; i < s_count; i++) {
      FeaturePoint s_point = s.getFeaturePoint(i);
      for (int j = 0; j < t_count; j++) {
        FeaturePoint t_point = t.getFeaturePoint(j);
        node_costs = FeaturePoint.calculate_Sim_Cost(s_point, t_point);
        allNodes[i][j] = new Node(i, j, node_costs);
      } 
    } 
    return allNodes;
  }
  
  private static void calcIncompletePath(Node[][] nodes, Polygon s, Polygon t) {
    int dim1 = nodes.length;
    int dim2 = (nodes[0]).length;
    int node_index1 = 0, node_index2 = 0;
    for (int i = 0; i < dim1; i++) {
      for (int j = 0; j < dim2; j++) {
        double min_costs = Double.MAX_VALUE;
        for (int k = 0; k <= 3; k++) {
          for (int l = 0; l <= 3; l++) {
            double current_costs = 0.0D;
            node_index1 = (i - k + dim1) % dim1;
            node_index2 = (j - l + dim2) % dim2;
            Node pred = nodes[node_index1][node_index2];
            current_costs += pred.getSimCosts();
            current_costs += calcDeltaCosts(s, node_index1, i, t, node_index2, j);
            if (current_costs < min_costs && (node_index1 != i || node_index2 != j)) {
              min_costs = current_costs;
              nodes[i][j].setPredecessor(pred);
              nodes[i][j].setPathCosts(min_costs);
            } 
          } 
        } 
      } 
    } 
  }
  
  private static Node getBestPossiblePredecessor(Node[][] nodes, Polygon s, Polygon t, int i, int j, int i_bound, int j_bound) {
    Node pred = null;
    Node node = nodes[i][j];
    double optimal_costs = Double.MAX_VALUE;
    int dim1 = nodes.length;
    int dim2 = (nodes[0]).length;
    int steps1 = Math.min(3, i - i_bound);
    int steps2 = Math.min(3, j - j_bound);
    for (int k = 0; k <= steps1; k++) {
      int node_index1 = (i - k + dim1) % dim1;
      for (int l = 0; l <= steps2; l++) {
        double current_costs = 0.0D;
        int node_index2 = (j - l + dim2) % dim2;
        node = nodes[node_index1][node_index2];
        current_costs += node.getSimCosts();
        current_costs += calcDeltaCosts(s, i, i, t, node_index2, j);
        if (current_costs < optimal_costs && (node_index1 != i || node_index2 != j)) {
          optimal_costs = current_costs;
          pred = nodes[node_index1][node_index2];
          pred.setPathCosts(optimal_costs);
        } 
      } 
    } 
    return pred;
  }
  
  private static Node getBestPossiblePredecessor(Node[][] nodes, Polygon s, Polygon t, int i, int j, boolean first_index_fixed) {
    Node pred = null;
    Node node = nodes[i][j];
    double optimal_costs = Double.MAX_VALUE;
    int dim1 = nodes.length;
    int dim2 = (nodes[0]).length;
    if (first_index_fixed) {
      for (int k = 0; k < 3; k++) {
        double current_costs = 0.0D;
        int node_index2 = (j - k + dim2) % dim2;
        node = nodes[i][node_index2];
        current_costs += node.getSimCosts();
        current_costs += calcDeltaCosts(s, i, i, t, node_index2, j);
        if (current_costs < optimal_costs) {
          optimal_costs = current_costs;
          pred = nodes[i][node_index2];
          pred.setPathCosts(optimal_costs);
        } 
      } 
    } else {
      for (int k = 0; k < 3; k++) {
        double current_costs = 0.0D;
        int node_index1 = (i - k + dim1) % dim1;
        node = nodes[node_index1][j];
        current_costs += node.getSimCosts();
        current_costs += calcDeltaCosts(s, node_index1, i, t, j, j);
        if (current_costs < optimal_costs) {
          optimal_costs = current_costs;
          pred = nodes[node_index1][j];
          pred.setPathCosts(optimal_costs);
        } 
      } 
    } 
    return pred;
  }
  
  private static Node getAlternative(Node[][] nodes, Polygon original, Polygon target, int x_limit, int y_limit, int x_index, int y_index) {
    Node alternative = null;
    if (x_index < x_limit)
      throw new IllegalArgumentException("x_index lower than lower boundary"); 
    if (y_index < y_limit)
      throw new IllegalArgumentException("y_index lower than lower boundary"); 
    double min_costs = Double.MAX_VALUE;
    int dim1 = nodes.length;
    int dim2 = (nodes[0]).length;
    int steps1 = Math.min(3, x_index - x_limit);
    int steps2 = Math.min(3, y_index - y_limit);
    int i;
    for (i = 1; i <= steps2; i++) {
      int node_index2 = (y_index - i + dim2) % dim2;
      double node_costs = nodes[x_index][node_index2].getSimCosts();
      node_costs += calcDeltaCosts(original, x_limit, x_limit, target, y_index, node_index2);
      if (node_costs < min_costs) {
        min_costs = node_costs;
        alternative = new Node(x_index, node_index2);
        alternative.setPathCosts(node_costs);
      } 
    } 
    for (i = 1; i <= steps1; i++) {
      int node_index1 = (x_index - i + dim1) % dim1;
      for (int j = 0; j <= steps2; j++) {
        int node_index2 = (y_index - j + dim2) % dim2;
        double node_costs = nodes[node_index1][node_index2].getSimCosts();
        node_costs += calcDeltaCosts(original, node_index1, x_limit, target, node_index2, y_limit);
        if (node_costs < min_costs) {
          min_costs = node_costs;
          alternative = new Node(node_index1, node_index2);
          alternative.setPathCosts(node_costs);
        } 
      } 
    } 
    return alternative;
  }
  
  public static Path getOptimalPath(Polygon s, Polygon t) {
    Path optimal_path = new Path();
    double min_path_costs = Double.MAX_VALUE;
    double current_path_costs = 0.0D;
    Node[][] nodes = getAllNodes(s, t);
    calcIncompletePath(nodes, s, t);
    if (!s.isClosed())
      t.isClosed(); 
    if (s.isClosed() && t.isClosed()) {
      Path path = new Path();
      int s_count = s.getCount();
      int t_count = t.getCount();
      boolean s_mod = false;
      boolean t_mod = false;
      boolean x_fixed = false;
      boolean y_fixed = false;
      boolean x_moved = false;
      boolean y_moved = false;
      for (int i = 0; i < s_count; i++) {
        for (int j = 0; j < t_count; j++) {
          s_mod = t_mod = x_fixed = y_fixed = false;
          Node current_node = nodes[i][j];
          path.add(current_node);
          current_path_costs = 0.0D;
          boolean closed = false;
          s_mod = t_mod = x_moved = y_moved = false;
          int x = current_node.getX();
          int y = current_node.getY();
          while (current_path_costs < min_path_costs && !closed) {
            Node old_node = current_node;
            int pred_x = current_node.getPredecessor().getX();
            int pred_y = current_node.getPredecessor().getY();
            if (!s_mod && !t_mod) {
              if (!x_moved && pred_x != x)
                x_moved = true; 
              if (!y_moved && pred_y != y)
                y_moved = true; 
              current_node = current_node.getPredecessor();
              path.add(current_node);
              current_path_costs += current_node.getPathCosts();
            } else if (s_mod && !t_mod) {
              if (pred_x < i) {
                Node temp = getAlternative(nodes, s, t, i, j, x, y);
                current_node = nodes[temp.getX()][temp.getY()];
                path.add(current_node);
                current_path_costs += temp.getPathCosts();
              } else {
                current_node = current_node.getPredecessor();
                path.add(current_node);
                current_path_costs += current_node.getPathCosts();
              } 
            } else if (!s_mod && t_mod) {
              if (pred_y < j) {
                Node temp = getAlternative(nodes, s, t, i, j, x, y);
                current_node = nodes[temp.getX()][temp.getY()];
                path.add(current_node);
                current_path_costs += temp.getPathCosts();
              } else {
                current_node = current_node.getPredecessor();
                path.add(current_node);
                current_path_costs += current_node.getPathCosts();
              } 
            } else if (x_moved && y_moved) {
              if (pred_x < i || pred_y < j || pred_x > x || pred_y > y) {
                Node temp = getAlternative(nodes, s, t, i, j, x, y);
                current_node = nodes[temp.getX()][temp.getY()];
                path.add(current_node);
                current_path_costs += temp.getPathCosts();
              } else {
                current_node = current_node.getPredecessor();
                path.add(current_node);
                current_path_costs += current_node.getPathCosts();
              } 
            } else if (x_moved && !y_moved) {
              if (pred_x < i || pred_x > x) {
                Node temp = getAlternative(nodes, s, t, i, j, x, y);
                current_node = nodes[temp.getX()][temp.getY()];
                path.add(current_node);
                current_path_costs += temp.getPathCosts();
              } else {
                current_node = current_node.getPredecessor();
                path.add(current_node);
                current_path_costs += current_node.getPathCosts();
              } 
            } else if (!x_moved && y_moved) {
              if (pred_y < j || pred_y > y) {
                Node temp = getAlternative(nodes, s, t, i, j, x, y);
                current_node = nodes[temp.getX()][temp.getY()];
                path.add(current_node);
                current_path_costs += temp.getPathCosts();
              } else {
                current_node = current_node.getPredecessor();
                path.add(current_node);
                current_path_costs += current_node.getPathCosts();
              } 
            } else {
              current_node = current_node.getPredecessor();
              path.add(current_node);
              current_path_costs += current_node.getPathCosts();
            } 
            if (old_node.getX() != current_node.getX()) {
              x_moved = true;
              if (old_node.getX() < current_node.getX())
                s_mod = true; 
            } 
            if (old_node.getY() != current_node.getY()) {
              y_moved = true;
              if (old_node.getY() < current_node.getY())
                t_mod = true; 
            } 
            x = current_node.getX();
            y = current_node.getY();
            if (x == i && y == j && x_moved && y_moved)
              closed = true; 
          } 
          if (closed && current_path_costs < min_path_costs) {
            optimal_path = path;
            min_path_costs = current_path_costs;
            optimal_path.setCosts(min_path_costs);
          } 
          path = new Path();
        } 
      } 
    } 
    return optimal_path;
  }
  
  public static Polygon[] createCompleteMorph(Path p, Path q, Polygon s, Polygon t) {
    System.out.println("called Method createCompleteMorph");
    Polygon[] readyToMorph = new Polygon[2];
    int sCount = s.getCount();
    int tCount = t.getCount();
    Polygon s1 = new Polygon();
    Polygon t1 = new Polygon();
    for (int i = 0; i < p.getSize(); i++) {
      Node temp = p.getNodeAt(i);
      int index_s = temp.getX();
      int index_t = temp.getY();
      FeaturePoint currentPoint = s.getFeaturePoint(index_s);
      FeaturePoint correspondence = t.getFeaturePoint(index_t);
      s1.addVertex((Point)currentPoint);
      t1.addVertex((Point)correspondence);
      currentPoint.setCorrespondence((Point)correspondence);
    } 
    handleRemainingPathNodes(q, s, t, s1, t1, false);
    handleRemainingPathNodes(q, t, s, t1, s1, true);
    int index;
    for (index = 0; index < sCount; index++) {
      if (!s.getFeaturePoint(index).hasCorrespondence()) {
        FeaturePoint pred = findPredecessor(s, index);
        FeaturePoint t_pred = (FeaturePoint)pred.getCorrespondence();
        FeaturePoint pred_pred = findPredecessor(s, s.getFeaturePointIndex(pred));
        FeaturePoint t_pred_pred = (FeaturePoint)pred_pred.getCorrespondence();
        FeaturePoint succ = findSuccesor(s, index);
        FeaturePoint t_succ = (FeaturePoint)succ.getCorrespondence();
        createNewFeaturePoint(s, t, s1, t1, index, pred, succ, t_succ, t_pred);
      } 
    } 
    for (index = 0; index < tCount; index++) {
      if (!t.getFeaturePoint(index).hasCorrespondence()) {
        FeaturePoint t_pred = findPredecessor(t, index);
        FeaturePoint pred = (FeaturePoint)t_pred.getCorrespondence();
        FeaturePoint t_pred_pred = findPredecessor(t, t.getFeaturePointIndex(t_pred));
        FeaturePoint pred_pred = (FeaturePoint)t_pred_pred.getCorrespondence();
        FeaturePoint t_succ = findSuccesor(t, index);
        FeaturePoint succ = (FeaturePoint)t_succ.getCorrespondence();
        boolean toRight = toRight(s, pred, succ, pred_pred);
        createNewFeaturePoint(t, s, t1, s1, index, t_pred, t_succ, succ, pred);
      } 
    } 
    if (s.isClosed())
      s1.close(); 
    if (t.isClosed())
      t1.close(); 
    readyToMorph[0] = s1;
    readyToMorph[1] = t1;
    System.out.println("Method createCompleteMorph successful");
    return readyToMorph;
  }
  
  private static void handleRemainingPathNodes(Path q, Polygon s, Polygon t, Polygon s1, Polygon t1, boolean reverse) {
    int sCount = s.getCount();
    int tCount = t.getCount();
    FeaturePoint pred = null, succ = null;
    FeaturePoint pred_pred = null;
    int t_pred_index = -1, t_succ_index = -1, t_pred_pred_index = -1;
    for (int i = 0; i < q.getSize(); i++) {
      int index_s;
      Node temp = q.getNodeAt(i);
      if (reverse) {
        index_s = temp.getY();
      } else {
        index_s = temp.getX();
      } 
      if (!s.getFeaturePoint(index_s).hasCorrespondence()) {
        boolean toRight, found_pred = false;
        boolean found_succ = false;
        boolean found_pred_pred = false;
        int pred_index = index_s;
        int succ_index = index_s;
        while (!found_pred) {
          pred_index--;
          if (pred_index < 0)
            pred_index += sCount; 
          pred = s.getFeaturePoint(pred_index);
          if (pred.hasCorrespondence())
            found_pred = true; 
        } 
        int pred_pred_index = pred_index;
        while (!found_pred_pred) {
          pred_pred_index--;
          if (pred_pred_index < 0)
            pred_pred_index += sCount; 
          pred_pred = s.getFeaturePoint(pred_pred_index);
          if (pred_pred.hasCorrespondence())
            found_pred_pred = true; 
        } 
        while (!found_succ) {
          succ_index++;
          if (succ_index > sCount - 1)
            succ_index = 0; 
          succ = s.getFeaturePoint(succ_index);
          if (succ.hasCorrespondence())
            found_succ = true; 
        } 
        FeaturePoint t_succ = (FeaturePoint)succ.getCorrespondence();
        FeaturePoint t_pred = (FeaturePoint)pred.getCorrespondence();
        FeaturePoint t_pred_pred = (FeaturePoint)pred_pred.getCorrespondence();
        t_pred_index = t.getFeaturePointIndex(t_pred);
        t_succ_index = t.getFeaturePointIndex(t_succ);
        t_pred_pred_index = t.getFeaturePointIndex(t_pred_pred);
        if (t_pred_pred_index < t_pred_index) {
          if (t_pred_index < t_succ_index) {
            diff = t_succ_index - t_pred_index;
            toRight = true;
          } else if (t_pred_pred_index < t_succ_index) {
            diff = t_pred_index - t_succ_index;
            toRight = false;
          } else {
            diff = t_succ_index + tCount - t_pred_index;
            toRight = true;
          } 
        } else if (t_succ_index < t_pred_index) {
          diff = t_pred_index - t_succ_index;
          toRight = false;
        } else if (t_pred_pred_index < t_succ_index) {
          diff = t_pred_index + tCount - t_succ_index;
          toRight = false;
        } else {
          diff = t_succ_index - t_pred_index;
          toRight = true;
        } 
        if (diff == 1) {
          createNewFeaturePoint(s, t, s1, t1, index_s, pred, succ, t_succ, t_pred);
        } else if (diff == 2) {
          int t_index;
          if (toRight) {
            t_index = (t_pred_index + 1) % tCount;
          } else {
            t_index = (t_pred_index - 1 + tCount) % tCount;
          } 
          FeaturePoint currentPoint = s.getFeaturePoint(index_s);
          FeaturePoint correspondence = t.getFeaturePoint(t_index);
          s1.addVertexBehind((Point)currentPoint, (Point)pred);
          t1.addVertexBehind((Point)correspondence, (Point)t_pred);
          currentPoint.setCorrespondence((Point)correspondence);
        } else {
          FeaturePoint bestMatch = t.getFeaturePoint((t_pred_index + 1) % tCount);
          double minCost = Double.MAX_VALUE;
          FeaturePoint toBeHandled = s.getFeaturePoint(index_s);
          if (toRight) {
            for (int j = 1; j < diff; j++) {
              FeaturePoint tmp = t.getFeaturePoint((t_pred_index + j) % tCount);
              double cost = FeaturePoint.calculate_Sim_Cost(toBeHandled, tmp);
              if (cost < minCost)
                bestMatch = tmp; 
            } 
          } else {
            for (int j = 1; j < diff; j++) {
              FeaturePoint tmp = t.getFeaturePoint((t_pred_index - j + tCount) % tCount);
              double cost = FeaturePoint.calculate_Sim_Cost(toBeHandled, tmp);
              if (cost < minCost)
                bestMatch = tmp; 
            } 
          } 
          s1.addVertexBehind((Point)toBeHandled, (Point)pred);
          t1.addVertexBehind((Point)bestMatch, (Point)t_pred);
          toBeHandled.setCorrespondence((Point)bestMatch);
        } 
      } 
    } 
  }
  
  private static void createNewFeaturePoint(Polygon s, Polygon t, Polygon s1, Polygon t1, int index_s, FeaturePoint pred, FeaturePoint succ, FeaturePoint t_succ, FeaturePoint t_pred) {
    FeaturePoint newPoint, middle = s.getFeaturePoint(index_s);
    int t_pred_index = t.getFeaturePointIndex(t_pred);
    int t_succ_index = t.getFeaturePointIndex(t_succ);
    int diff = Math.abs(t_pred_index - t_succ_index);
    double length_predToMid = Math.sqrt(Math.pow((pred.getX() - middle.getX()), 2.0D) + Math.pow((pred.getY() - middle.getY()), 2.0D));
    double length_MidToSucc = Math.sqrt(Math.pow((succ.getX() - middle.getX()), 2.0D) + Math.pow((succ.getY() - middle.getY()), 2.0D));
    double pred_MidToTotal = length_predToMid / (length_MidToSucc + length_predToMid);
    if (diff == 1) {
      int x_slope = t_succ.getX() - t_pred.getX();
      int y_slope = t_succ.getY() - t_pred.getY();
      int t_x = (int)(x_slope * pred_MidToTotal + 0.5D);
      int t_y = (int)(y_slope * pred_MidToTotal + 0.5D);
      t_x += t_pred.getX();
      t_y += t_pred.getY();
      newPoint = new FeaturePoint(t_x, t_y);
      s1.addVertexBehind((Point)middle, (Point)pred);
      t1.addVertexBehind((Point)newPoint, (Point)t_pred);
      t.addVertexBetween((Point)newPoint, (Point)t_pred, (Point)t_succ);
      t.addVertexBehind((Point)newPoint, (Point)t_pred);
    } else {
      FeaturePoint featurePoint3;
      double total_length = 0.0D;
      FeaturePoint t_pred_pred = (FeaturePoint)findPredecessor(s, s.getFeaturePointIndex(pred)).getCorrespondence();
      boolean toRight = toRight(t, t_pred, t_succ, t_pred_pred);
      FeaturePoint featurePoint2 = t_pred;
      FeaturePoint featurePoint1 = t_pred;
      int t_count = t.getCount();
      int i = t_pred_index;
      if (toRight) {
        while (i != t_succ_index) {
          FeaturePoint featurePoint = t.getFeaturePoint((i + 1 + t_count) % t_count);
          total_length += 
            Math.sqrt(Math.pow((featurePoint1.getX() - featurePoint.getX()), 2.0D) + Math.pow((featurePoint1.getY() - featurePoint.getY()), 2.0D));
          featurePoint1 = featurePoint;
          i = (i + 1) % t_count;
        } 
      } else {
        while (i != t_succ_index) {
          FeaturePoint featurePoint = t.getFeaturePoint((i - 1 + t_count) % t_count);
          total_length += 
            Math.sqrt(Math.pow((featurePoint1.getX() - featurePoint.getX()), 2.0D) + Math.pow((featurePoint1.getY() - featurePoint.getY()), 2.0D));
          featurePoint1 = featurePoint;
          i = (i - 1 + t_count) % t_count;
        } 
      } 
      double t_pred_MidToTotal = total_length * pred_MidToTotal;
      double current_length = 0.0D;
      double part_length = 0.0D;
      int index = t_pred_index;
      if (toRight) {
        while (current_length < t_pred_MidToTotal) {
          part_length = Math.sqrt(Math.pow((t.getFeaturePoint(index).getX() - 
                t.getFeaturePoint((index + 1) % t_count).getX()), 2.0D) + 
              Math.pow((t.getFeaturePoint(index).getY() - 
                t.getFeaturePoint((index + 1) % t_count).getY()), 2.0D));
          current_length += part_length;
          index = (index + 1) % t_count;
        } 
      } else {
        while (current_length < t_pred_MidToTotal) {
          part_length = Math.sqrt(Math.pow((t.getFeaturePoint(index).getX() - 
                t.getFeaturePoint((index - 1 + t_count) % t_count).getX()), 2.0D) + 
              Math.pow((t.getFeaturePoint(index).getY() - 
                t.getFeaturePoint((index - 1 + t_count) % t_count).getY()), 2.0D));
          current_length += part_length;
          index = (index - 1 + t_count) % t_count;
        } 
      } 
      current_length -= part_length;
      double t_part_toMid = t_pred_MidToTotal - current_length;
      double factor = t_part_toMid / part_length;
      if (toRight) {
        featurePoint3 = t.getFeaturePoint(index);
        featurePoint2 = t.getFeaturePoint((index - 1 + t_count) % t_count);
      } else {
        featurePoint3 = t.getFeaturePoint(index);
        featurePoint2 = t.getFeaturePoint((index + 1) % t_count);
      } 
      int x_slope = featurePoint3.getX() - featurePoint2.getX();
      int y_slope = featurePoint3.getY() - featurePoint2.getY();
      int t_x = (int)(x_slope * pred_MidToTotal + 0.5D);
      int t_y = (int)(y_slope * pred_MidToTotal + 0.5D);
      t_x += featurePoint2.getX();
      t_y += featurePoint2.getY();
      newPoint = new FeaturePoint(t_x, t_y);
      s1.addVertexBetween((Point)middle, (Point)pred, (Point)succ);
      t1.addVertexBetween((Point)newPoint, (Point)t_pred, (Point)t_succ);
      t.addVertexBetween((Point)newPoint, (Point)featurePoint2, (Point)featurePoint3);
    } 
    int index_middle = s.getFeaturePointIndex(middle);
    int index_newPoint = t.getFeaturePointIndex(newPoint);
    s.getFeaturePoint(index_middle).setCorrespondence((Point)t.getFeaturePoint(index_newPoint));
  }
  
  private static FeaturePoint findPredecessor(Polygon s, int index) {
    FeaturePoint predecessor;
    boolean found_pred = false;
    do {
      index--;
      if (index < 0)
        index += s.getCount(); 
      predecessor = s.getFeaturePoint(index);
      if (!predecessor.hasCorrespondence())
        continue; 
      found_pred = true;
    } while (!found_pred);
    return predecessor;
  }
  
  private static FeaturePoint findSuccesor(Polygon s, int index) {
    FeaturePoint successor;
    boolean found_succ = false;
    int sCount = s.getCount();
    do {
      index++;
      if (index >= sCount)
        index -= sCount; 
      successor = s.getFeaturePoint(index);
      if (!successor.hasCorrespondence())
        continue; 
      found_succ = true;
    } while (!found_succ);
    return successor;
  }
  
  private static boolean toRight(Polygon s, FeaturePoint pred, FeaturePoint succ, FeaturePoint pred_pred) {
    boolean toRight;
    int pred_index = s.getFeaturePointIndex(pred);
    int succ_index = s.getFeaturePointIndex(succ);
    int pred_pred_index = s.getFeaturePointIndex(pred_pred);
    if (pred_pred_index < pred_index) {
      if (pred_index < succ_index) {
        diff = succ_index - pred_index;
        toRight = true;
      } else if (pred_pred_index < succ_index) {
        diff = pred_index - succ_index;
        toRight = false;
      } else {
        diff = succ_index + s.getCount() - pred_index;
        toRight = true;
      } 
    } else if (succ_index < pred_index) {
      diff = pred_index - succ_index;
      toRight = false;
    } else if (pred_pred_index < succ_index) {
      diff = pred_index + s.getCount() - succ_index;
      toRight = false;
    } else {
      diff = succ_index - pred_index;
      toRight = true;
    } 
    return toRight;
  }
  
  public static double[][][][] calculateAllDeltaCosts(Polygon source, Polygon target, int skips) {
    int dim1 = source.getFeaturePointCount();
    int dim2 = target.getFeaturePointCount();
    double[][][][] delta_field = new double[dim1][dim2][skips + 1][skips + 1];
    for (int i = 0; i < dim1; i++) {
      for (int j = 0; j < dim2; j++) {
        for (int k = 0; k <= skips; k++) {
          int source_index = (i - k + dim1) % dim1;
          for (int l = 0; l <= skips; l++) {
            int target_index = (j - l + dim2) % dim2;
            delta_field[i][j][k][l] = deltaCosts(source, target, i, source_index, j, target_index);
          } 
        } 
      } 
    } 
    return delta_field;
  }
  
  public static double deltaCosts(Polygon source, Polygon target, int start_index_s, int end_index_s, int start_index_t, int end_index_t) {
    double costs = 0.0D;
    costs += disCosts(source, start_index_s, end_index_s);
    costs += disCosts(target, start_index_t, end_index_t);
    costs += FeaturePoint.calculate_Sim_Cost(source.getFeaturePoint(end_index_s), target.getFeaturePoint(end_index_t));
    return costs;
  }
  
  public static double disCosts(Polygon polygon, int start_index, int end_index) {
    int steps;
    double disCosts = 0.0D;
    int dim = polygon.getFeaturePointCount();
    if (start_index == end_index)
      return disCosts; 
    if (end_index > start_index) {
      steps = start_index + dim - end_index;
    } else {
      steps = start_index - end_index;
    } 
    for (int i = 1; i < steps; i++)
      disCosts += polygon.getFeaturePoint((start_index - i + dim) % dim).getDisCost(); 
    return disCosts;
  }
  
  public static Path calculatePath(Polygon source, Polygon target, int skips) {
    double[][][][] deltaCosts = calculateAllDeltaCosts(source, target, skips);
    int dim1 = source.getFeaturePointCount();
    int dim2 = target.getFeaturePointCount();
    double min_path_costs = Double.MAX_VALUE;
    Path path = new Path();
    Node[][] field = new Node[dim1 + 1][dim2 + 1];
    Vector source_fps = source.getFeaturePoints();
    Vector target_fps = target.getFeaturePoints();
    for (int k = 0; k < dim1; k++) {
      for (int l = 0; l < dim2; l++) {
        field = initializeField(field, k, l, source_fps, target_fps);
        field[0][0].setPathCosts(deltaCosts[k][l][0][0]);
        int i;
        for (i = 1; i < dim1 + 1; i++)
          setPredecessor(field, i, 0, deltaCosts, k, l, skips); 
        for (i = 1; i < dim2 + 1; i++)
          setPredecessor(field, 0, i, deltaCosts, k, l, skips); 
        for (i = 1; i < dim1 + 1; i++) {
          for (int j = 1; j < dim2 + 1; j++)
            setPredecessor(field, i, j, deltaCosts, k, l, skips); 
        } 
        Node current_node = field[dim1][dim2];
        if (current_node.getPathCosts() < min_path_costs) {
          min_path_costs = current_node.getPathCosts();
          path = new Path();
          path.add(current_node);
          path.setCosts(current_node.getPathCosts());
          while (current_node.getPredecessor() != null) {
            current_node = current_node.getPredecessor();
            path.add(current_node);
          } 
        } 
      } 
    } 
    return path;
  }
  
  public static Node setPredecessor(Node[][] field, int source_index, int target_index, double[][][][] deltaCosts, int skips) {
    int source_boundary, target_boundary, dim1 = deltaCosts.length;
    int dim2 = (deltaCosts[0]).length;
    Node predecessor = null;
    if (source_index < skips) {
      source_boundary = source_index;
    } else {
      source_boundary = skips;
    } 
    if (target_index < skips) {
      target_boundary = target_index;
    } else {
      target_boundary = skips;
    } 
    double min_costs = Double.MAX_VALUE;
    double current_costs = 0.0D;
    int i;
    for (i = 1; i <= source_boundary; i++) {
      current_costs = field[source_index - i][target_index].getPathCosts();
      current_costs += deltaCosts[source_index % dim1][target_index % dim2][i][0];
      if (current_costs < min_costs) {
        min_costs = current_costs;
        predecessor = field[source_index - i][target_index];
      } 
    } 
    for (i = 1; i <= target_boundary; i++) {
      current_costs = field[source_index][target_index - i].getPathCosts();
      current_costs += deltaCosts[source_index % dim1][target_index % dim2][0][i];
      if (current_costs < min_costs) {
        min_costs = current_costs;
        predecessor = field[source_index][target_index - i];
      } 
    } 
    for (i = 1; i <= source_boundary; i++) {
      for (int j = 1; j <= target_boundary; j++) {
        current_costs = field[source_index - i][target_index - j].getPathCosts();
        current_costs += deltaCosts[source_index % dim1][target_index % dim2][i][j];
        if (current_costs < min_costs) {
          min_costs = current_costs;
          predecessor = field[source_index - i][target_index - j];
        } 
      } 
    } 
    field[source_index][target_index].setPredecessor(predecessor);
    field[source_index][target_index].setPathCosts(min_costs);
    return predecessor;
  }
  
  public static Node setPredecessor(Node[][] field, int field_index_source, int field_index_target, double[][][][] deltaCosts, int first_node_source, int first_node_target, int skips) {
    int source_boundary, target_boundary, dim1 = deltaCosts.length;
    int dim2 = (deltaCosts[0]).length;
    Node predecessor = null;
    if (field_index_source < skips) {
      source_boundary = field_index_source;
    } else {
      source_boundary = skips;
    } 
    if (field_index_target < skips) {
      target_boundary = field_index_target;
    } else {
      target_boundary = skips;
    } 
    double min_costs = Double.MAX_VALUE;
    double current_costs = 0.0D;
    int i;
    for (i = 1; i <= source_boundary; i++) {
      current_costs = field[field_index_source - i][field_index_target].getPathCosts();
      current_costs += deltaCosts[(field_index_source + first_node_source) % dim1][(
          field_index_target + first_node_target) % dim2][i][0];
      if (current_costs < min_costs) {
        min_costs = current_costs;
        predecessor = field[field_index_source - i][field_index_target];
      } 
    } 
    for (i = 1; i <= target_boundary; i++) {
      current_costs = field[field_index_source][field_index_target - i].getPathCosts();
      current_costs += deltaCosts[(field_index_source + first_node_source) % dim1][(
          field_index_target + first_node_target) % dim2][0][i];
      if (current_costs < min_costs) {
        min_costs = current_costs;
        predecessor = field[field_index_source][field_index_target - i];
      } 
    } 
    for (i = 1; i <= source_boundary; i++) {
      for (int j = 1; j <= target_boundary; j++) {
        current_costs = field[field_index_source - i][field_index_target - j].getPathCosts();
        current_costs += deltaCosts[(field_index_source + first_node_source) % dim1][(
            field_index_target + first_node_target) % dim2][i][j];
        if (current_costs < min_costs) {
          min_costs = current_costs;
          predecessor = field[field_index_source - i][field_index_target - j];
        } 
      } 
    } 
    field[field_index_source][field_index_target].setPredecessor(predecessor);
    field[field_index_source][field_index_target].setPathCosts(min_costs);
    return predecessor;
  }
  
  public static Node[][] initializeField(Node[][] field, int source_index, int target_index) {
    int dim1 = field.length;
    int dim2 = (field[0]).length;
    int source_point_count = dim1 - 1;
    int target_point_count = dim2 - 1;
    int i;
    for (i = 0; i < dim1; i++)
      field[i][0] = new Node((source_index + i) % source_point_count, target_index); 
    for (i = 1; i < dim2; i++)
      field[0][i] = new Node(source_index, (target_index + i) % target_point_count); 
    for (i = 1; i < dim1; i++) {
      for (int j = 0; j < dim2; j++)
        field[i][j] = new Node((source_index + i) % source_point_count, (
            target_index + j) % target_point_count); 
    } 
    return field;
  }
  
  public static Node[][] initializeField(Node[][] field, int source_index, int target_index, Vector sourceFPs, Vector targetFPs) {
    int dim1 = field.length;
    int dim2 = (field[0]).length;
    int source_point_count = dim1 - 1;
    int target_point_count = dim2 - 1;
    FeaturePoint tmp = targetFPs.elementAt(target_index);
    int i;
    for (i = 0; i < dim1; i++) {
      int source_curr_index = (source_index + i) % source_point_count;
      field[i][0] = new Node(source_curr_index, target_index, 
          sourceFPs.elementAt(source_curr_index), tmp);
    } 
    tmp = sourceFPs.elementAt(source_index);
    for (i = 1; i < dim2; i++) {
      int target_curr_index = (target_index + i) % target_point_count;
      field[0][i] = new Node(source_index, target_curr_index, 
          tmp, targetFPs.elementAt(target_curr_index));
    } 
    for (i = 1; i < dim1; i++) {
      for (int j = 0; j < dim2; j++) {
        int source_curr_index = (source_index + i) % source_point_count;
        int target_curr_index = (target_index + j) % target_point_count;
        field[i][j] = new Node(source_curr_index, target_curr_index, 
            sourceFPs.elementAt(source_curr_index), 
            targetFPs.elementAt(target_curr_index));
      } 
    } 
    return field;
  }
  
  public static Polygon[] createCompleteMorph2(Path fPath, Path oPath, Polygon source, Polygon target) {
    Polygon[] morphablePolygons = new Polygon[2];
    Polygon s1 = new Polygon();
    Polygon t1 = new Polygon();
    for (int i = 0; i < fPath.getSize(); i++) {
      Node node = fPath.getNodeAt(i);
      FeaturePoint curr = node.getSourcePoint();
      FeaturePoint corr = node.getTargetPoint();
      curr.setCorrespondence((Point)corr);
      s1.addVertex((Point)curr);
      t1.addVertex((Point)corr);
    } 
    if (fPath.getSize() + 1 < oPath.getSize()) {
      handleRemainingPoints(oPath, source, target, s1, t1, true);
      handleRemainingPoints(oPath, target, source, t1, s1, false);
    } 
    int fp_count = source.getFeaturePointCount();
    int j;
    for (j = 0; j < fp_count; j++) {
      FeaturePoint curr = source.getFeaturePoint(j);
      if (!curr.hasCorrespondence())
        createCorrespondence(source, target, s1, t1, (Point)curr); 
    } 
    fp_count = target.getFeaturePointCount();
    for (j = 0; j < fp_count; j++) {
      FeaturePoint curr = target.getFeaturePoint(j);
      if (!curr.hasCorrespondence())
        createCorrespondence(target, source, t1, s1, (Point)curr); 
    } 
    fp_count = source.getCount();
    int k;
    for (k = 0; k < fp_count; k++) {
      Point curr_point = source.getVertex(k);
      if (!curr_point.hasCorrespondence())
        createCorrespondence(source, target, s1, t1, curr_point); 
    } 
    fp_count = target.getCount();
    for (k = 0; k < fp_count; k++) {
      Point curr_point = target.getVertex(k);
      if (!curr_point.hasCorrespondence())
        createCorrespondence(target, source, t1, s1, curr_point); 
    } 
    if (source.isClosed())
      s1.close(); 
    if (target.isClosed())
      t1.close(); 
    morphablePolygons[0] = s1;
    morphablePolygons[1] = t1;
    return morphablePolygons;
  }
  
  private static void handleRemainingPoints(Path oPath, Polygon source, Polygon target, Polygon s1, Polygon t1, boolean s_points) {
    for (int i = 0; i < oPath.getSize(); i++) {
      FeaturePoint curr;
      Node node = oPath.getNodeAt(i);
      if (s_points) {
        curr = node.getSourcePoint();
      } else {
        curr = node.getTargetPoint();
      } 
      if (!curr.hasCorrespondence()) {
        int t_pred_index, t_succ_index, point_count;
        boolean feature_points;
        FeaturePoint pred = getPredecessor(source, curr);
        FeaturePoint succ = getSuccessor(source, curr);
        Point t_pred = pred.getCorrespondence();
        Point t_succ = succ.getCorrespondence();
        if (t_pred instanceof FeaturePoint && t_succ instanceof FeaturePoint) {
          t_pred_index = target.getFeaturePointIndex(
              (FeaturePoint)pred.getCorrespondence());
          t_succ_index = target.getFeaturePointIndex((FeaturePoint)succ.getCorrespondence());
          point_count = target.getFeaturePointCount();
          feature_points = true;
        } else {
          t_pred_index = target.getIndex(t_pred);
          t_succ_index = target.getIndex(t_succ);
          point_count = target.getCount();
          feature_points = false;
        } 
        int start = t_pred_index;
        int diff = 0;
        while (start != t_succ_index) {
          start = (start + 1) % point_count;
          diff++;
        } 
        if (diff == 0) {
          System.out.println("Predecessor and Successor have one common correspondence!");
        } else if (diff == 2) {
          Point corr;
          if (feature_points) {
            FeaturePoint featurePoint = target.getFeaturePoint((t_pred_index + 1) % point_count);
          } else {
            corr = target.getVertex((t_pred_index + 1) % point_count);
          } 
          curr.setCorrespondence(corr);
          s1.addVertexBefore((Point)curr, (Point)pred);
          t1.addVertexBefore(corr, pred.getCorrespondence());
        } else if (diff > 2) {
          FeaturePoint featurePoint;
          if (!feature_points) {
            Point bestmatch = target.getVertex((t_pred_index + 1) % point_count);
            double length1 = 0.0D;
            double length2 = 0.0D;
            FeaturePoint featurePoint1 = pred;
            int s_count = source.getCount();
            int start_index = source.getIndex((Point)pred);
            int end_index = source.getIndex((Point)curr);
            while (start_index != end_index) {
              start_index = (start_index + 1) % s_count;
              Point tmp2 = source.getVertex(start_index);
              length1 += 
                Math.sqrt(Math.pow((featurePoint1.getX() - tmp2.getX()), 2.0D) + Math.pow((featurePoint1.getY() - tmp2.getY()), 2.0D));
              point1 = tmp2;
            } 
            end_index = source.getIndex((Point)succ);
            while (start_index != end_index) {
              start_index = (start_index + 1) % s_count;
              Point tmp2 = source.getVertex(start_index);
              length2 += 
                Math.sqrt(Math.pow((point1.getX() - tmp2.getX()), 2.0D) + Math.pow((point1.getY() - tmp2.getY()), 2.0D));
              point1 = tmp2;
            } 
            double source_length = length1 + length2;
            double pred_to_curr = length1 / source_length;
            Point point1 = t_pred;
            double target_length = 0.0D;
            double[] segments = new double[diff];
            int counter = 0;
            while (!point1.equals(t_succ)) {
              segments[counter] = 
                Math.sqrt(Math.pow((point1.getX() - bestmatch.getX()), 2.0D) + Math.pow((point1.getY() - bestmatch.getY()), 2.0D));
              target_length += segments[counter];
              counter++;
              point1 = bestmatch;
              bestmatch = target.getVertex((t_pred_index + 1 + counter) % point_count);
            } 
            double t_pred_to_corr = target_length * pred_to_curr;
            counter = 0;
            double t_length = 0.0D;
            while (t_length < t_pred_to_corr) {
              t_length += segments[counter];
              counter++;
            } 
            if (counter == diff) {
              bestmatch = target.getVertex((t_pred_index + diff - 1) % point_count);
            } else if (counter == 1) {
              bestmatch = target.getVertex((t_pred_index + 1) % point_count);
            } else {
              counter--;
              double dist1 = Math.abs(t_length - t_pred_to_corr);
              double dist2 = Math.abs(t_length - t_pred_to_corr - segments[counter]);
              if (dist1 < dist2) {
                bestmatch = target.getVertex((t_pred_index + counter + 1) % point_count);
              } else {
                bestmatch = target.getVertex((t_pred_index + counter) % point_count);
              } 
            } 
          } else {
            featurePoint = target.getFeaturePoint((t_pred_index + 1) % point_count);
            double min_costs = FeaturePoint.calculate_Sim_Cost(curr, featurePoint);
            for (int j = 2; j < diff; j++) {
              FeaturePoint tmp = target.getFeaturePoint((t_pred_index + j) % point_count);
              double costs = FeaturePoint.calculate_Sim_Cost(curr, tmp);
              if (costs < min_costs) {
                featurePoint = tmp;
                min_costs = costs;
              } 
            } 
          } 
          if (featurePoint.equals(new Point(434, 416)))
            System.out.println("bla"); 
          curr.setCorrespondence((Point)featurePoint);
          s1.addVertexBefore((Point)curr, (Point)pred);
          t1.addVertexBefore((Point)featurePoint, pred.getCorrespondence());
        } else {
          int v_diff = 0;
          int t_count = target.getCount();
          int v_start = target.getIndex(pred.getCorrespondence());
          start = v_start;
          int v_end = target.getIndex(succ.getCorrespondence());
          while (start != v_end) {
            v_diff++;
            start = (start + 1) % t_count;
          } 
          if (v_diff == 2 && !target.getVertex((v_start + 1) % t_count).hasCorrespondence()) {
            Point vertex = target.getVertex((v_start + 1) % t_count);
            curr.setCorrespondence(vertex);
            s1.addVertexBefore((Point)curr, (Point)pred);
            t1.addVertexBefore(vertex, t_pred);
          } else {
            createCorrespondingPoint(source, target, s1, t1, (Point)curr, (Point)pred, (Point)succ);
          } 
        } 
      } 
    } 
  }
  
  private static FeaturePoint getPredecessor(Polygon polygon, FeaturePoint curr) {
    Vector fps = polygon.getFeaturePoints();
    int fp_count = polygon.getFeaturePointCount();
    int index = polygon.getFeaturePointIndex(curr);
    while (!((FeaturePoint)fps.elementAt(index)).hasCorrespondence())
      index = (index - 1 + fp_count) % fp_count; 
    return fps.elementAt(index);
  }
  
  private static FeaturePoint getSuccessor(Polygon polygon, FeaturePoint curr) {
    Vector fps = polygon.getFeaturePoints();
    int fp_count = polygon.getFeaturePointCount();
    int index = polygon.getFeaturePointIndex(curr);
    while (!((FeaturePoint)fps.elementAt(index)).hasCorrespondence())
      index = (index + 1) % fp_count; 
    return fps.elementAt(index);
  }
  
  private static void createCorrespondingPoint(Polygon source, Polygon target, Polygon s1, Polygon t1, Point curr, Point pred, Point succ) {
    Point t_pred = pred.getCorrespondence();
    Point t_succ = succ.getCorrespondence();
    int diff = 0;
    int x = curr.getX();
    int y = curr.getY();
    int t_pred_index = target.getIndex(t_pred);
    int t_count = target.getCount();
    int i = t_pred_index;
    int j = target.getIndex(t_succ);
    while (i != j) {
      i = (i + 1) % t_count;
      diff++;
    } 
    int start_index = source.getIndex(pred);
    int end_index = source.getIndex(curr);
    Point start = pred;
    int s_count = source.getCount();
    double length_pred_to_curr = 0.0D;
    double length_succ_to_curr = 0.0D;
    while (start_index != end_index) {
      start_index = (start_index + 1) % s_count;
      Point end = source.getVertex(start_index);
      length_pred_to_curr += 
        Math.sqrt(Math.pow((start.getX() - end.getX()), 2.0D) + Math.pow((start.getY() - end.getY()), 2.0D));
      start = end;
    } 
    end_index = source.getIndex(succ);
    while (start_index != end_index) {
      start_index = (start_index + 1) % s_count;
      Point end = source.getVertex(start_index);
      length_succ_to_curr += 
        Math.sqrt(Math.pow((start.getX() - end.getX()), 2.0D) + Math.pow((start.getY() - end.getY()), 2.0D));
      start = end;
    } 
    double pred_curr_to_total = length_pred_to_curr / (length_pred_to_curr + length_succ_to_curr);
    if (diff == 1) {
      int x_slope = t_succ.getX() - t_pred.getX();
      int y_slope = t_succ.getY() - t_pred.getY();
      int t_x = (int)(x_slope * pred_curr_to_total + 0.5D);
      int t_y = (int)(y_slope * pred_curr_to_total + 0.5D);
      t_x += t_pred.getX();
      t_y += t_pred.getY();
      Point corr = new Point(t_x, t_y);
      curr.setCorrespondence(corr);
      s1.addVertexBefore(curr, pred);
      t1.addVertexBefore(corr, t_pred);
      target.addVertexBehind(corr, t_pred);
    } else {
      double total_length = 0.0D;
      i = t_pred_index;
      Point segment_start = t_pred;
      double[] segments = new double[diff];
      int counter = 0;
      while (i != j) {
        i = (i + 1) % t_count;
        Point point = target.getVertex(i);
        segments[counter] = 
          Math.sqrt(Math.pow((point.getX() - segment_start.getX()), 2.0D) + Math.pow((point.getY() - segment_start.getY()), 2.0D));
        total_length += segments[counter];
        counter++;
        segment_start = point;
      } 
      double t_pred_curr_to_total = total_length * pred_curr_to_total;
      double current_length = 0.0D;
      i = t_pred_index;
      counter = 0;
      while (current_length < t_pred_curr_to_total) {
        current_length += segments[counter];
        counter++;
      } 
      counter--;
      if (counter < 0) {
        current_length = 0.0D;
        counter = 0;
      } else {
        current_length -= segments[counter];
      } 
      double t_part = t_pred_curr_to_total - current_length;
      t_part /= segments[counter];
      segment_start = target.getVertex((t_pred_index + counter) % t_count);
      Point segment_end = target.getVertex((t_pred_index + counter + 1) % t_count);
      int x_slope = segment_end.getX() - segment_start.getX();
      int y_slope = segment_end.getY() - segment_start.getY();
      int t_x = (int)(x_slope * t_part + 0.5D);
      int t_y = (int)(y_slope * t_part + 0.5D);
      t_x += segment_start.getX();
      t_y += segment_start.getY();
      Point corr = new Point(t_x, t_y);
      curr.setCorrespondence(corr);
      s1.addVertexBefore(curr, pred);
      t1.addVertexBefore(corr, t_pred);
      target.addVertexBehind(corr, segment_start);
    } 
  }
  
  private static Point getPredecessor2(Polygon polygon, Point curr) {
    Vector vertices = polygon.getAllVertices();
    int v_count = polygon.getCount();
    int index = polygon.getIndex(curr);
    while (!((Point)vertices.elementAt(index)).hasCorrespondence())
      index = (index - 1 + v_count) % v_count; 
    return vertices.elementAt(index);
  }
  
  private static Point getSuccessor2(Polygon polygon, Point curr) {
    Vector vertices = polygon.getAllVertices();
    int v_count = polygon.getCount();
    int index = polygon.getIndex(curr);
    while (!((Point)vertices.elementAt(index)).hasCorrespondence())
      index = (index + 1) % v_count; 
    return vertices.elementAt(index);
  }
  
  private static void createCorrespondence(Polygon source, Polygon target, Polygon s1, Polygon t1, Point curr) {
    Point pred = getPredecessor2(source, curr);
    Point succ = getSuccessor2(source, curr);
    createCorrespondingPoint(source, target, s1, t1, curr, pred, succ);
  }
}
