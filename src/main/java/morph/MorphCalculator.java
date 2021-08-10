/*      */ package morph;
/*      */ 
/*      */ import java.util.Vector;
/*      */ import math.Covariance;
/*      */ import math.Eigenvalue;
/*      */ import shapes.FeaturePoint;
/*      */ import shapes.Point;
/*      */ import shapes.Polygon;
/*      */ import tools.Node;
/*      */ import tools.Path;
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
/*      */ public class MorphCalculator
/*      */ {
/*      */   private static int diff;
/*      */   
/*      */   public static void preparePolygon(Polygon p, int sample_rate, int range) {
/*   39 */     Vector fPoints = p.getFeaturePoints();
/*   40 */     Vector samplePoints = p.getSample(sample_rate);
/*      */ 
/*      */ 
/*      */     
/*   44 */     int count = fPoints.size();
/*   45 */     int sample_size = samplePoints.size();
/*   46 */     double[] feature_variation = new double[count];
/*   47 */     double[] side_feature_v = new double[count];
/*   48 */     double[] feature_size = new double[count];
/*      */ 
/*      */     
/*   51 */     double[][] eigenvectors = new double[2][2];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   57 */     double epsilon = 1.0D;
/*      */ 
/*      */ 
/*      */     
/*   61 */     double[][] tmp_evec = new double[2][2];
/*      */ 
/*      */ 
/*      */     
/*   65 */     double polygon_length = p.getLength();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   72 */     for (int i = 0; i < count; i++) {
/*   73 */       double tangent_eva, normal_eva, tmp_t_eva, tmp_n_eva; Point feature = fPoints.elementAt(i);
/*      */       
/*   75 */       int index = samplePoints.indexOf(feature);
/*   76 */       Vector ros = new Vector(); int j;
/*   77 */       for (j = 0; j < 2 * range + 1; j++) {
/*   78 */         ros.add(samplePoints.elementAt((index - range + sample_size + j) % sample_size));
/*      */       }
/*      */       
/*   81 */       double[][] covariance = Covariance.covariance(ros);
/*      */       
/*   83 */       double[] eigenvalues = Eigenvalue.hqr2(covariance, eigenvectors);
/*      */       
/*   85 */       Point next = ros.elementAt(range + 1);
/*   86 */       double xn = (feature.getX() - next.getX());
/*   87 */       double yn = (feature.getY() - next.getY());
/*   88 */       double dot1 = Math.abs(Covariance.dotProduct(eigenvectors[0][0], eigenvectors[1][0], xn, yn));
/*   89 */       double dot2 = Math.abs(Covariance.dotProduct(eigenvectors[0][1], eigenvectors[1][1], xn, yn));
/*   90 */       if (dot1 > dot2) {
/*   91 */         tangent_eva = eigenvalues[0];
/*   92 */         normal_eva = eigenvalues[1];
/*      */       } else {
/*      */         
/*   95 */         tangent_eva = eigenvalues[1];
/*   96 */         normal_eva = eigenvalues[0];
/*      */       } 
/*   98 */       FeaturePoint fp = p.getFeaturePoint(i);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  105 */       Polygon help = new Polygon(p, i);
/*  106 */       if (help.contains((Point)fp)) {
/*  107 */         fp.setConcave();
/*      */       } else {
/*  109 */         fp.setConvex();
/*  110 */       }  if (fp.getConvex()) {
/*  111 */         epsilon = 1.0D;
/*      */       } else {
/*  113 */         epsilon = -1.0D;
/*      */       } 
/*      */       
/*  116 */       feature_variation[i] = epsilon * normal_eva / (normal_eva + tangent_eva);
/*      */       
/*  118 */       fp.setFeat_var(feature_variation[i]);
/*      */ 
/*      */       
/*  121 */       Vector rol = new Vector();
/*  122 */       Vector ror = new Vector();
/*  123 */       for (j = 0; j < range; j++) {
/*  124 */         rol.add(ros.elementAt(j));
/*  125 */         ror.add(ros.elementAt(j + range + 1));
/*      */       } 
/*  127 */       double[][] cov_rol = Covariance.covariance(rol);
/*  128 */       double[][] cov_ror = Covariance.covariance(ror);
/*      */       
/*  130 */       double[] tmp_eva = Eigenvalue.hqr2(cov_rol, tmp_evec);
/*      */       
/*  132 */       next = rol.elementAt(range / 2 - 1);
/*  133 */       xn = next.getX();
/*  134 */       yn = next.getY();
/*  135 */       next = rol.elementAt(range / 2);
/*  136 */       xn -= next.getX();
/*  137 */       yn -= next.getY();
/*  138 */       dot1 = Math.abs(Covariance.dotProduct(tmp_evec[0][0], tmp_evec[1][0], xn, yn));
/*  139 */       dot2 = Math.abs(Covariance.dotProduct(tmp_evec[0][1], tmp_evec[1][1], xn, yn));
/*  140 */       if (dot1 > dot2) {
/*  141 */         tmp_t_eva = tmp_eva[0];
/*  142 */         tmp_n_eva = tmp_eva[1];
/*      */       } else {
/*      */         
/*  145 */         tmp_t_eva = tmp_eva[1];
/*  146 */         tmp_n_eva = tmp_eva[0];
/*      */       } 
/*  148 */       double sigma_rol = tmp_n_eva / (tmp_n_eva + tmp_t_eva);
/*      */       
/*  150 */       tmp_eva = Eigenvalue.hqr2(cov_ror, tmp_evec);
/*      */       
/*  152 */       next = ror.elementAt(range / 2 - 1);
/*  153 */       xn = next.getX();
/*  154 */       yn = next.getY();
/*  155 */       next = ror.elementAt(range / 2);
/*  156 */       xn -= next.getX();
/*  157 */       yn -= next.getY();
/*  158 */       dot1 = Math.abs(Covariance.dotProduct(tmp_evec[0][0], tmp_evec[1][0], xn, yn));
/*  159 */       dot2 = Math.abs(Covariance.dotProduct(tmp_evec[0][1], tmp_evec[1][1], xn, yn));
/*  160 */       if (dot1 > dot2) {
/*  161 */         tmp_t_eva = tmp_eva[0];
/*  162 */         tmp_n_eva = tmp_eva[1];
/*      */       } else {
/*      */         
/*  165 */         tmp_t_eva = tmp_eva[1];
/*  166 */         tmp_n_eva = tmp_eva[0];
/*      */       } 
/*  168 */       double sigma_ror = tmp_n_eva / (tmp_n_eva + tmp_t_eva);
/*  169 */       side_feature_v[i] = (sigma_rol + sigma_ror) / 2.0D;
/*  170 */       fp.setSide_var(side_feature_v[i]);
/*      */ 
/*      */       
/*  173 */       xn = (((Point)rol.firstElement()).getX() - ((Point)rol.lastElement()).getX());
/*  174 */       yn = (((Point)rol.firstElement()).getY() - ((Point)rol.lastElement()).getY());
/*  175 */       double rol_length = Math.sqrt(xn * xn + yn * yn);
/*  176 */       xn = (((Point)ror.firstElement()).getX() - ((Point)ror.lastElement()).getX());
/*  177 */       yn = (((Point)ror.firstElement()).getY() - ((Point)ror.lastElement()).getY());
/*  178 */       double ror_length = Math.sqrt(xn * xn + yn * yn);
/*  179 */       feature_size[i] = (rol_length + ror_length) / polygon_length / 2.0D;
/*  180 */       fp.setFeat_size(feature_size[i]);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Path calculateOptimalPath(Polygon s, Polygon t) {
/*  198 */     System.out.println("called Method calculateOptimalPath");
/*  199 */     int s_points = s.getCount();
/*  200 */     int t_points = t.getCount();
/*      */     
/*  202 */     double[][] costs = new double[s_points][t_points];
/*      */     
/*      */     int i;
/*  205 */     for (i = 0; i < s_points; i++) {
/*  206 */       for (int j = 0; j < t_points; j++) {
/*  207 */         costs[i][j] = FeaturePoint.calculate_Sim_Cost(s.getFeaturePoint(i), t.getFeaturePoint(j));
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  216 */     int[][][] nodes = new int[s_points][t_points][2];
/*  217 */     double[][] min_path_costs = new double[s_points][t_points];
/*      */ 
/*      */     
/*  220 */     int index_s = -1, index_t = -1;
/*      */ 
/*      */     
/*  223 */     for (i = 0; i < s_points; i++) {
/*  224 */       for (int j = 0; j < t_points; j++) {
/*      */         
/*  226 */         double min_costs = Double.MAX_VALUE;
/*  227 */         for (int k = 1; k < 4; k++) {
/*  228 */           for (int l = 1; l < 4; l++) {
/*  229 */             int temp_index_s = (i - k + s_points) % s_points;
/*  230 */             int temp_index_t = (j - l + t_points) % t_points;
/*  231 */             double path_costs = costs[temp_index_s][temp_index_t];
/*  232 */             path_costs += calcDeltaCosts(s, temp_index_s, i, t, temp_index_t, j);
/*  233 */             if (path_costs < min_costs) {
/*  234 */               min_costs = path_costs;
/*  235 */               index_s = temp_index_s;
/*  236 */               index_t = temp_index_t;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  241 */         min_path_costs[i][j] = min_costs;
/*  242 */         nodes[i][j][0] = index_s;
/*  243 */         nodes[i][j][1] = index_t;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  248 */     double complete_path_costs = 0.0D;
/*  249 */     Path optimalPath = new Path();
/*  250 */     if (!s.isClosed()) {
/*      */       
/*  252 */       int temp_index_s = s_points - 1;
/*  253 */       int temp_index_t = t_points - 1;
/*  254 */       optimalPath.add(new Node(index_s, index_t, costs[index_s][index_t]));
/*  255 */       while (index_s != 0 && index_t != 0) {
/*  256 */         index_s = nodes[temp_index_s][temp_index_t][0];
/*  257 */         index_t = nodes[temp_index_s][temp_index_t][1];
/*  258 */         if (temp_index_s < index_s)
/*  259 */           index_s = 0; 
/*  260 */         if (temp_index_t < index_t)
/*  261 */           index_t = 0; 
/*  262 */         complete_path_costs += min_path_costs[temp_index_s][temp_index_t];
/*  263 */         temp_index_s = index_s;
/*  264 */         temp_index_t = index_t;
/*  265 */         optimalPath.add(new Node(index_s, index_t));
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  274 */       Path path = new Path();
/*  275 */       double min_complete_path_costs = Double.MAX_VALUE;
/*      */       
/*  277 */       for (i = s_points - 1; i >= 0; i--) {
/*  278 */         for (int j = t_points - 1; j >= 0; j--) {
/*  279 */           boolean s_returned = false;
/*  280 */           boolean t_returned = false;
/*  281 */           int temp_index_s = i;
/*  282 */           int temp_index_t = j;
/*      */           do {
/*  284 */             path.add(new Node(index_s, index_t));
/*  285 */             complete_path_costs += min_path_costs[index_s][index_t];
/*  286 */             if (index_s > i)
/*  287 */               s_returned = true; 
/*  288 */             if (index_t > j)
/*  289 */               t_returned = true; 
/*  290 */             if (s_returned && index_s < i) {
/*  291 */               index_s = i;
/*      */             } else {
/*  293 */               index_s = nodes[temp_index_s][temp_index_t][0];
/*  294 */             }  if (t_returned && index_t < j) {
/*  295 */               index_t = j;
/*      */             } else {
/*  297 */               index_t = nodes[temp_index_s][temp_index_t][1];
/*  298 */             }  temp_index_s = index_s;
/*  299 */             temp_index_t = index_t;
/*      */           }
/*  301 */           while (complete_path_costs < min_complete_path_costs && index_s != i && index_t != j);
/*  302 */           path.add(new Node(i, j));
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
/*  326 */           if (complete_path_costs < min_complete_path_costs) {
/*  327 */             min_complete_path_costs = complete_path_costs;
/*  328 */             optimalPath = new Path(path);
/*  329 */             optimalPath.setCosts(min_complete_path_costs);
/*  330 */             optimalPath.setSimCosts(costs);
/*      */           } 
/*  332 */           path.clear();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  337 */     System.out.println("done! :)");
/*  338 */     System.out.println("Method calculateOptimalPath successful!");
/*  339 */     return optimalPath;
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
/*      */   public static double sumDisCosts(Polygon p, int i, int j) {
/*  361 */     int points = p.getCount();
/*  362 */     if (i < 0 || j < 0 || j > points - 1 || i > points - 1) throw new IllegalArgumentException("Index out of bounds"); 
/*  363 */     double costs = 0.0D;
/*  364 */     if (i == j)
/*  365 */       return costs; 
/*  366 */     int index = (i + 1) % points;
/*  367 */     while (index != j) {
/*  368 */       costs += p.getFeaturePoint(index).getDisCost();
/*  369 */       index = (index + 1) % points;
/*      */     } 
/*  371 */     return costs;
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
/*      */   public static double calcDeltaCosts(Polygon s, int s_i, int s_j, Polygon t, int t_i, int t_j) {
/*  393 */     double costs = 0.0D;
/*      */     
/*  395 */     costs += sumDisCosts(s, s_i, s_j);
/*  396 */     costs += sumDisCosts(t, t_i, t_j);
/*  397 */     costs += 1.0D * FeaturePoint.calculate_Sim_Cost(s.getFeaturePoint(s_j), t.getFeaturePoint(t_j));
/*      */     
/*  399 */     return costs;
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
/*      */   public static Node[][] getAllNodes(Polygon s, Polygon t) {
/*  425 */     int s_count = s.getCount();
/*  426 */     int t_count = t.getCount();
/*  427 */     Node[][] allNodes = new Node[s_count][t_count];
/*      */     
/*  429 */     double node_costs = 0.0D;
/*  430 */     for (int i = 0; i < s_count; i++) {
/*  431 */       FeaturePoint s_point = s.getFeaturePoint(i);
/*  432 */       for (int j = 0; j < t_count; j++) {
/*  433 */         FeaturePoint t_point = t.getFeaturePoint(j);
/*  434 */         node_costs = FeaturePoint.calculate_Sim_Cost(s_point, t_point);
/*  435 */         allNodes[i][j] = new Node(i, j, node_costs);
/*      */       } 
/*      */     } 
/*  438 */     return allNodes;
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
/*      */   private static void calcIncompletePath(Node[][] nodes, Polygon s, Polygon t) {
/*  457 */     int dim1 = nodes.length;
/*  458 */     int dim2 = (nodes[0]).length;
/*      */ 
/*      */ 
/*      */     
/*  462 */     int node_index1 = 0, node_index2 = 0;
/*  463 */     for (int i = 0; i < dim1; i++) {
/*  464 */       for (int j = 0; j < dim2; j++) {
/*  465 */         double min_costs = Double.MAX_VALUE;
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
/*  488 */         for (int k = 0; k <= 3; k++) {
/*  489 */           for (int l = 0; l <= 3; l++) {
/*  490 */             double current_costs = 0.0D;
/*  491 */             node_index1 = (i - k + dim1) % dim1;
/*  492 */             node_index2 = (j - l + dim2) % dim2;
/*  493 */             Node pred = nodes[node_index1][node_index2];
/*  494 */             current_costs += pred.getSimCosts();
/*  495 */             current_costs += calcDeltaCosts(s, node_index1, i, t, node_index2, j);
/*      */ 
/*      */             
/*  498 */             if (current_costs < min_costs && (node_index1 != i || node_index2 != j)) {
/*  499 */               min_costs = current_costs;
/*  500 */               nodes[i][j].setPredecessor(pred);
/*  501 */               nodes[i][j].setPathCosts(min_costs);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Node getBestPossiblePredecessor(Node[][] nodes, Polygon s, Polygon t, int i, int j, int i_bound, int j_bound) {
/*  527 */     Node pred = null;
/*  528 */     Node node = nodes[i][j];
/*  529 */     double optimal_costs = Double.MAX_VALUE;
/*      */     
/*  531 */     int dim1 = nodes.length;
/*  532 */     int dim2 = (nodes[0]).length;
/*      */ 
/*      */ 
/*      */     
/*  536 */     int steps1 = Math.min(3, i - i_bound);
/*  537 */     int steps2 = Math.min(3, j - j_bound);
/*  538 */     for (int k = 0; k <= steps1; k++) {
/*  539 */       int node_index1 = (i - k + dim1) % dim1;
/*  540 */       for (int l = 0; l <= steps2; l++) {
/*  541 */         double current_costs = 0.0D;
/*  542 */         int node_index2 = (j - l + dim2) % dim2;
/*  543 */         node = nodes[node_index1][node_index2];
/*  544 */         current_costs += node.getSimCosts();
/*  545 */         current_costs += calcDeltaCosts(s, i, i, t, node_index2, j);
/*  546 */         if (current_costs < optimal_costs && (node_index1 != i || node_index2 != j)) {
/*  547 */           optimal_costs = current_costs;
/*  548 */           pred = nodes[node_index1][node_index2];
/*  549 */           pred.setPathCosts(optimal_costs);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  554 */     return pred;
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
/*      */   private static Node getBestPossiblePredecessor(Node[][] nodes, Polygon s, Polygon t, int i, int j, boolean first_index_fixed) {
/*  573 */     Node pred = null;
/*  574 */     Node node = nodes[i][j];
/*  575 */     double optimal_costs = Double.MAX_VALUE;
/*      */     
/*  577 */     int dim1 = nodes.length;
/*  578 */     int dim2 = (nodes[0]).length;
/*      */ 
/*      */     
/*  581 */     if (first_index_fixed) {
/*  582 */       for (int k = 0; k < 3; k++) {
/*  583 */         double current_costs = 0.0D;
/*  584 */         int node_index2 = (j - k + dim2) % dim2;
/*  585 */         node = nodes[i][node_index2];
/*  586 */         current_costs += node.getSimCosts();
/*  587 */         current_costs += calcDeltaCosts(s, i, i, t, node_index2, j);
/*  588 */         if (current_costs < optimal_costs) {
/*  589 */           optimal_costs = current_costs;
/*  590 */           pred = nodes[i][node_index2];
/*  591 */           pred.setPathCosts(optimal_costs);
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  596 */       for (int k = 0; k < 3; k++) {
/*  597 */         double current_costs = 0.0D;
/*  598 */         int node_index1 = (i - k + dim1) % dim1;
/*  599 */         node = nodes[node_index1][j];
/*  600 */         current_costs += node.getSimCosts();
/*  601 */         current_costs += calcDeltaCosts(s, node_index1, i, t, j, j);
/*  602 */         if (current_costs < optimal_costs) {
/*  603 */           optimal_costs = current_costs;
/*  604 */           pred = nodes[node_index1][j];
/*  605 */           pred.setPathCosts(optimal_costs);
/*      */         } 
/*      */       } 
/*      */     } 
/*  609 */     return pred;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Node getAlternative(Node[][] nodes, Polygon original, Polygon target, int x_limit, int y_limit, int x_index, int y_index) {
/*  615 */     Node alternative = null;
/*  616 */     if (x_index < x_limit) throw new IllegalArgumentException("x_index lower than lower boundary"); 
/*  617 */     if (y_index < y_limit) throw new IllegalArgumentException("y_index lower than lower boundary"); 
/*  618 */     double min_costs = Double.MAX_VALUE;
/*      */     
/*  620 */     int dim1 = nodes.length;
/*  621 */     int dim2 = (nodes[0]).length;
/*  622 */     int steps1 = Math.min(3, x_index - x_limit);
/*  623 */     int steps2 = Math.min(3, y_index - y_limit);
/*      */     int i;
/*  625 */     for (i = 1; i <= steps2; i++) {
/*  626 */       int node_index2 = (y_index - i + dim2) % dim2;
/*  627 */       double node_costs = nodes[x_index][node_index2].getSimCosts();
/*  628 */       node_costs += calcDeltaCosts(original, x_limit, x_limit, target, y_index, node_index2);
/*  629 */       if (node_costs < min_costs) {
/*  630 */         min_costs = node_costs;
/*  631 */         alternative = new Node(x_index, node_index2);
/*  632 */         alternative.setPathCosts(node_costs);
/*      */       } 
/*      */     } 
/*  635 */     for (i = 1; i <= steps1; i++) {
/*  636 */       int node_index1 = (x_index - i + dim1) % dim1;
/*  637 */       for (int j = 0; j <= steps2; j++) {
/*  638 */         int node_index2 = (y_index - j + dim2) % dim2;
/*  639 */         double node_costs = nodes[node_index1][node_index2].getSimCosts();
/*  640 */         node_costs += calcDeltaCosts(original, node_index1, x_limit, target, node_index2, y_limit);
/*  641 */         if (node_costs < min_costs) {
/*  642 */           min_costs = node_costs;
/*  643 */           alternative = new Node(node_index1, node_index2);
/*  644 */           alternative.setPathCosts(node_costs);
/*      */         } 
/*      */       } 
/*      */     } 
/*  648 */     return alternative;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Path getOptimalPath(Polygon s, Polygon t) {
/*  659 */     Path optimal_path = new Path();
/*  660 */     double min_path_costs = Double.MAX_VALUE;
/*  661 */     double current_path_costs = 0.0D;
/*      */ 
/*      */ 
/*      */     
/*  665 */     Node[][] nodes = getAllNodes(s, t);
/*      */     
/*  667 */     calcIncompletePath(nodes, s, t);
/*      */ 
/*      */     
/*  670 */     if (!s.isClosed()) t.isClosed();
/*      */ 
/*      */ 
/*      */     
/*  674 */     if (s.isClosed() && t.isClosed()) {
/*      */       
/*  676 */       Path path = new Path();
/*  677 */       int s_count = s.getCount();
/*  678 */       int t_count = t.getCount();
/*  679 */       boolean s_mod = false;
/*  680 */       boolean t_mod = false;
/*  681 */       boolean x_fixed = false;
/*  682 */       boolean y_fixed = false;
/*  683 */       boolean x_moved = false;
/*  684 */       boolean y_moved = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  693 */       for (int i = 0; i < s_count; i++) {
/*  694 */         for (int j = 0; j < t_count; j++) {
/*  695 */           s_mod = t_mod = x_fixed = y_fixed = false;
/*      */           
/*  697 */           Node current_node = nodes[i][j];
/*      */           
/*  699 */           path.add(current_node);
/*  700 */           current_path_costs = 0.0D;
/*  701 */           boolean closed = false;
/*  702 */           s_mod = t_mod = x_moved = y_moved = false;
/*  703 */           int x = current_node.getX();
/*  704 */           int y = current_node.getY();
/*      */ 
/*      */ 
/*      */           
/*  708 */           while (current_path_costs < min_path_costs && !closed) {
/*  709 */             Node old_node = current_node;
/*  710 */             int pred_x = current_node.getPredecessor().getX();
/*  711 */             int pred_y = current_node.getPredecessor().getY();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  718 */             if (!s_mod && !t_mod) {
/*  719 */               if (!x_moved && pred_x != x)
/*  720 */                 x_moved = true; 
/*  721 */               if (!y_moved && pred_y != y)
/*  722 */                 y_moved = true; 
/*  723 */               current_node = current_node.getPredecessor();
/*  724 */               path.add(current_node);
/*  725 */               current_path_costs += current_node.getPathCosts();
/*      */             }
/*  727 */             else if (s_mod && !t_mod) {
/*      */               
/*  729 */               if (pred_x < i) {
/*  730 */                 Node temp = getAlternative(nodes, s, t, i, j, x, y);
/*  731 */                 current_node = nodes[temp.getX()][temp.getY()];
/*  732 */                 path.add(current_node);
/*  733 */                 current_path_costs += temp.getPathCosts();
/*      */               } else {
/*      */                 
/*  736 */                 current_node = current_node.getPredecessor();
/*  737 */                 path.add(current_node);
/*  738 */                 current_path_costs += current_node.getPathCosts();
/*      */               }
/*      */             
/*  741 */             } else if (!s_mod && t_mod) {
/*      */               
/*  743 */               if (pred_y < j) {
/*  744 */                 Node temp = getAlternative(nodes, s, t, i, j, x, y);
/*  745 */                 current_node = nodes[temp.getX()][temp.getY()];
/*  746 */                 path.add(current_node);
/*  747 */                 current_path_costs += temp.getPathCosts();
/*      */               } else {
/*      */                 
/*  750 */                 current_node = current_node.getPredecessor();
/*  751 */                 path.add(current_node);
/*  752 */                 current_path_costs += current_node.getPathCosts();
/*      */               }
/*      */             
/*      */             }
/*  756 */             else if (x_moved && y_moved) {
/*  757 */               if (pred_x < i || pred_y < j || pred_x > x || pred_y > y) {
/*  758 */                 Node temp = getAlternative(nodes, s, t, i, j, x, y);
/*  759 */                 current_node = nodes[temp.getX()][temp.getY()];
/*  760 */                 path.add(current_node);
/*  761 */                 current_path_costs += temp.getPathCosts();
/*      */               } else {
/*      */                 
/*  764 */                 current_node = current_node.getPredecessor();
/*  765 */                 path.add(current_node);
/*  766 */                 current_path_costs += current_node.getPathCosts();
/*      */               }
/*      */             
/*  769 */             } else if (x_moved && !y_moved) {
/*  770 */               if (pred_x < i || pred_x > x) {
/*  771 */                 Node temp = getAlternative(nodes, s, t, i, j, x, y);
/*  772 */                 current_node = nodes[temp.getX()][temp.getY()];
/*  773 */                 path.add(current_node);
/*  774 */                 current_path_costs += temp.getPathCosts();
/*      */               } else {
/*      */                 
/*  777 */                 current_node = current_node.getPredecessor();
/*  778 */                 path.add(current_node);
/*  779 */                 current_path_costs += current_node.getPathCosts();
/*      */               }
/*      */             
/*  782 */             } else if (!x_moved && y_moved) {
/*  783 */               if (pred_y < j || pred_y > y) {
/*  784 */                 Node temp = getAlternative(nodes, s, t, i, j, x, y);
/*  785 */                 current_node = nodes[temp.getX()][temp.getY()];
/*  786 */                 path.add(current_node);
/*  787 */                 current_path_costs += temp.getPathCosts();
/*      */               } else {
/*      */                 
/*  790 */                 current_node = current_node.getPredecessor();
/*  791 */                 path.add(current_node);
/*  792 */                 current_path_costs += current_node.getPathCosts();
/*      */               } 
/*      */             } else {
/*      */               
/*  796 */               current_node = current_node.getPredecessor();
/*  797 */               path.add(current_node);
/*  798 */               current_path_costs += current_node.getPathCosts();
/*      */             } 
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
/*  814 */             if (old_node.getX() != current_node.getX()) {
/*  815 */               x_moved = true;
/*  816 */               if (old_node.getX() < current_node.getX())
/*  817 */                 s_mod = true; 
/*      */             } 
/*  819 */             if (old_node.getY() != current_node.getY()) {
/*  820 */               y_moved = true;
/*  821 */               if (old_node.getY() < current_node.getY()) {
/*  822 */                 t_mod = true;
/*      */               }
/*      */             } 
/*  825 */             x = current_node.getX();
/*  826 */             y = current_node.getY();
/*  827 */             if (x == i && y == j && x_moved && y_moved) {
/*  828 */               closed = true;
/*      */             }
/*      */           } 
/*      */           
/*  832 */           if (closed && current_path_costs < min_path_costs) {
/*  833 */             optimal_path = path;
/*  834 */             min_path_costs = current_path_costs;
/*  835 */             optimal_path.setCosts(min_path_costs);
/*      */           } 
/*      */           
/*  838 */           path = new Path();
/*      */         } 
/*      */       } 
/*      */     } 
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
/*  924 */     return optimal_path;
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
/*      */   public static Polygon[] createCompleteMorph(Path p, Path q, Polygon s, Polygon t) {
/* 1171 */     System.out.println("called Method createCompleteMorph");
/* 1172 */     Polygon[] readyToMorph = new Polygon[2];
/* 1173 */     int sCount = s.getCount();
/* 1174 */     int tCount = t.getCount();
/* 1175 */     Polygon s1 = new Polygon();
/* 1176 */     Polygon t1 = new Polygon();
/*      */ 
/*      */ 
/*      */     
/* 1180 */     for (int i = 0; i < p.getSize(); i++) {
/* 1181 */       Node temp = p.getNodeAt(i);
/* 1182 */       int index_s = temp.getX();
/* 1183 */       int index_t = temp.getY();
/* 1184 */       FeaturePoint currentPoint = s.getFeaturePoint(index_s);
/* 1185 */       FeaturePoint correspondence = t.getFeaturePoint(index_t);
/* 1186 */       s1.addVertex((Point)currentPoint);
/*      */       
/* 1188 */       t1.addVertex((Point)correspondence);
/*      */       
/* 1190 */       currentPoint.setCorrespondence((Point)correspondence);
/*      */     } 
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
/* 1203 */     handleRemainingPathNodes(q, s, t, s1, t1, false);
/* 1204 */     handleRemainingPathNodes(q, t, s, t1, s1, true);
/*      */ 
/*      */     
/*      */     int index;
/*      */ 
/*      */     
/* 1210 */     for (index = 0; index < sCount; index++) {
/* 1211 */       if (!s.getFeaturePoint(index).hasCorrespondence()) {
/* 1212 */         FeaturePoint pred = findPredecessor(s, index);
/* 1213 */         FeaturePoint t_pred = (FeaturePoint)pred.getCorrespondence();
/* 1214 */         FeaturePoint pred_pred = findPredecessor(s, s.getFeaturePointIndex(pred));
/* 1215 */         FeaturePoint t_pred_pred = (FeaturePoint)pred_pred.getCorrespondence();
/* 1216 */         FeaturePoint succ = findSuccesor(s, index);
/* 1217 */         FeaturePoint t_succ = (FeaturePoint)succ.getCorrespondence();
/*      */         
/* 1219 */         createNewFeaturePoint(s, t, s1, t1, index, pred, succ, t_succ, t_pred);
/*      */       } 
/*      */     } 
/* 1222 */     for (index = 0; index < tCount; index++) {
/* 1223 */       if (!t.getFeaturePoint(index).hasCorrespondence()) {
/* 1224 */         FeaturePoint t_pred = findPredecessor(t, index);
/* 1225 */         FeaturePoint pred = (FeaturePoint)t_pred.getCorrespondence();
/* 1226 */         FeaturePoint t_pred_pred = findPredecessor(t, t.getFeaturePointIndex(t_pred));
/* 1227 */         FeaturePoint pred_pred = (FeaturePoint)t_pred_pred.getCorrespondence();
/* 1228 */         FeaturePoint t_succ = findSuccesor(t, index);
/* 1229 */         FeaturePoint succ = (FeaturePoint)t_succ.getCorrespondence();
/* 1230 */         boolean toRight = toRight(s, pred, succ, pred_pred);
/* 1231 */         createNewFeaturePoint(t, s, t1, s1, index, t_pred, t_succ, succ, pred);
/*      */       } 
/*      */     } 
/* 1234 */     if (s.isClosed())
/* 1235 */       s1.close(); 
/* 1236 */     if (t.isClosed()) {
/* 1237 */       t1.close();
/*      */     }
/* 1239 */     readyToMorph[0] = s1;
/* 1240 */     readyToMorph[1] = t1;
/* 1241 */     System.out.println("Method createCompleteMorph successful");
/* 1242 */     return readyToMorph;
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
/*      */   private static void handleRemainingPathNodes(Path q, Polygon s, Polygon t, Polygon s1, Polygon t1, boolean reverse) {
/* 1261 */     int sCount = s.getCount();
/* 1262 */     int tCount = t.getCount();
/*      */ 
/*      */ 
/*      */     
/* 1266 */     FeaturePoint pred = null, succ = null;
/* 1267 */     FeaturePoint pred_pred = null;
/*      */     
/* 1269 */     int t_pred_index = -1, t_succ_index = -1, t_pred_pred_index = -1;
/*      */     
/* 1271 */     for (int i = 0; i < q.getSize(); i++) {
/* 1272 */       int index_s; Node temp = q.getNodeAt(i);
/* 1273 */       if (reverse) {
/* 1274 */         index_s = temp.getY();
/*      */       } else {
/* 1276 */         index_s = temp.getX();
/* 1277 */       }  if (!s.getFeaturePoint(index_s).hasCorrespondence()) {
/* 1278 */         boolean toRight, found_pred = false;
/* 1279 */         boolean found_succ = false;
/* 1280 */         boolean found_pred_pred = false;
/* 1281 */         int pred_index = index_s;
/* 1282 */         int succ_index = index_s;
/* 1283 */         while (!found_pred) {
/* 1284 */           pred_index--;
/* 1285 */           if (pred_index < 0)
/* 1286 */             pred_index += sCount; 
/* 1287 */           pred = s.getFeaturePoint(pred_index);
/* 1288 */           if (pred.hasCorrespondence())
/* 1289 */             found_pred = true; 
/*      */         } 
/* 1291 */         int pred_pred_index = pred_index;
/* 1292 */         while (!found_pred_pred) {
/* 1293 */           pred_pred_index--;
/* 1294 */           if (pred_pred_index < 0)
/* 1295 */             pred_pred_index += sCount; 
/* 1296 */           pred_pred = s.getFeaturePoint(pred_pred_index);
/* 1297 */           if (pred_pred.hasCorrespondence())
/* 1298 */             found_pred_pred = true; 
/*      */         } 
/* 1300 */         while (!found_succ) {
/* 1301 */           succ_index++;
/* 1302 */           if (succ_index > sCount - 1)
/* 1303 */             succ_index = 0; 
/* 1304 */           succ = s.getFeaturePoint(succ_index);
/* 1305 */           if (succ.hasCorrespondence()) {
/* 1306 */             found_succ = true;
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1312 */         FeaturePoint t_succ = (FeaturePoint)succ.getCorrespondence();
/* 1313 */         FeaturePoint t_pred = (FeaturePoint)pred.getCorrespondence();
/* 1314 */         FeaturePoint t_pred_pred = (FeaturePoint)pred_pred.getCorrespondence();
/* 1315 */         t_pred_index = t.getFeaturePointIndex(t_pred);
/* 1316 */         t_succ_index = t.getFeaturePointIndex(t_succ);
/* 1317 */         t_pred_pred_index = t.getFeaturePointIndex(t_pred_pred);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1322 */         if (t_pred_pred_index < t_pred_index) {
/* 1323 */           if (t_pred_index < t_succ_index) {
/* 1324 */             diff = t_succ_index - t_pred_index;
/* 1325 */             toRight = true;
/*      */           }
/* 1327 */           else if (t_pred_pred_index < t_succ_index) {
/* 1328 */             diff = t_pred_index - t_succ_index;
/* 1329 */             toRight = false;
/*      */           } else {
/*      */             
/* 1332 */             diff = t_succ_index + tCount - t_pred_index;
/* 1333 */             toRight = true;
/*      */           }
/*      */         
/*      */         }
/* 1337 */         else if (t_succ_index < t_pred_index) {
/* 1338 */           diff = t_pred_index - t_succ_index;
/* 1339 */           toRight = false;
/*      */         }
/* 1341 */         else if (t_pred_pred_index < t_succ_index) {
/* 1342 */           diff = t_pred_index + tCount - t_succ_index;
/* 1343 */           toRight = false;
/*      */         } else {
/*      */           
/* 1346 */           diff = t_succ_index - t_pred_index;
/* 1347 */           toRight = true;
/*      */         } 
/*      */         
/* 1350 */         if (diff == 1) {
/* 1351 */           createNewFeaturePoint(s, t, s1, t1, index_s, pred, succ, t_succ, t_pred);
/*      */         }
/* 1353 */         else if (diff == 2) {
/*      */           int t_index;
/* 1355 */           if (toRight) {
/* 1356 */             t_index = (t_pred_index + 1) % tCount;
/*      */           } else {
/* 1358 */             t_index = (t_pred_index - 1 + tCount) % tCount;
/* 1359 */           }  FeaturePoint currentPoint = s.getFeaturePoint(index_s);
/* 1360 */           FeaturePoint correspondence = t.getFeaturePoint(t_index);
/* 1361 */           s1.addVertexBehind((Point)currentPoint, (Point)pred);
/* 1362 */           t1.addVertexBehind((Point)correspondence, (Point)t_pred);
/* 1363 */           currentPoint.setCorrespondence((Point)correspondence);
/*      */         } else {
/*      */           
/* 1366 */           FeaturePoint bestMatch = t.getFeaturePoint((t_pred_index + 1) % tCount);
/*      */           
/* 1368 */           double minCost = Double.MAX_VALUE;
/*      */           
/* 1370 */           FeaturePoint toBeHandled = s.getFeaturePoint(index_s);
/* 1371 */           if (toRight) {
/* 1372 */             for (int j = 1; j < diff; j++) {
/* 1373 */               FeaturePoint tmp = t.getFeaturePoint((t_pred_index + j) % tCount);
/* 1374 */               double cost = FeaturePoint.calculate_Sim_Cost(toBeHandled, tmp);
/* 1375 */               if (cost < minCost) {
/* 1376 */                 bestMatch = tmp;
/*      */               }
/*      */             } 
/*      */           } else {
/*      */             
/* 1381 */             for (int j = 1; j < diff; j++) {
/* 1382 */               FeaturePoint tmp = t.getFeaturePoint((t_pred_index - j + tCount) % tCount);
/* 1383 */               double cost = FeaturePoint.calculate_Sim_Cost(toBeHandled, tmp);
/* 1384 */               if (cost < minCost) {
/* 1385 */                 bestMatch = tmp;
/*      */               }
/*      */             } 
/*      */           } 
/* 1389 */           s1.addVertexBehind((Point)toBeHandled, (Point)pred);
/* 1390 */           t1.addVertexBehind((Point)bestMatch, (Point)t_pred);
/* 1391 */           toBeHandled.setCorrespondence((Point)bestMatch);
/*      */         } 
/*      */       } 
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
/*      */   private static void createNewFeaturePoint(Polygon s, Polygon t, Polygon s1, Polygon t1, int index_s, FeaturePoint pred, FeaturePoint succ, FeaturePoint t_succ, FeaturePoint t_pred) {
/* 1425 */     FeaturePoint newPoint, middle = s.getFeaturePoint(index_s);
/* 1426 */     int t_pred_index = t.getFeaturePointIndex(t_pred);
/* 1427 */     int t_succ_index = t.getFeaturePointIndex(t_succ);
/* 1428 */     int diff = Math.abs(t_pred_index - t_succ_index);
/* 1429 */     double length_predToMid = Math.sqrt(Math.pow((pred.getX() - middle.getX()), 2.0D) + Math.pow((pred.getY() - middle.getY()), 2.0D));
/* 1430 */     double length_MidToSucc = Math.sqrt(Math.pow((succ.getX() - middle.getX()), 2.0D) + Math.pow((succ.getY() - middle.getY()), 2.0D));
/* 1431 */     double pred_MidToTotal = length_predToMid / (length_MidToSucc + length_predToMid);
/*      */     
/* 1433 */     if (diff == 1) {
/* 1434 */       int x_slope = t_succ.getX() - t_pred.getX();
/* 1435 */       int y_slope = t_succ.getY() - t_pred.getY();
/* 1436 */       int t_x = (int)(x_slope * pred_MidToTotal + 0.5D);
/* 1437 */       int t_y = (int)(y_slope * pred_MidToTotal + 0.5D);
/* 1438 */       t_x += t_pred.getX();
/* 1439 */       t_y += t_pred.getY();
/* 1440 */       newPoint = new FeaturePoint(t_x, t_y);
/* 1441 */       s1.addVertexBehind((Point)middle, (Point)pred);
/*      */       
/* 1443 */       t1.addVertexBehind((Point)newPoint, (Point)t_pred);
/*      */ 
/*      */       
/* 1446 */       t.addVertexBetween((Point)newPoint, (Point)t_pred, (Point)t_succ);
/*      */       
/* 1448 */       t.addVertexBehind((Point)newPoint, (Point)t_pred);
/*      */     } else {
/*      */       FeaturePoint featurePoint3;
/*      */       
/* 1452 */       double total_length = 0.0D;
/*      */ 
/*      */       
/* 1455 */       FeaturePoint t_pred_pred = (FeaturePoint)findPredecessor(s, s.getFeaturePointIndex(pred)).getCorrespondence();
/* 1456 */       boolean toRight = toRight(t, t_pred, t_succ, t_pred_pred);
/*      */       
/* 1458 */       FeaturePoint featurePoint2 = t_pred;
/* 1459 */       FeaturePoint featurePoint1 = t_pred;
/* 1460 */       int t_count = t.getCount();
/* 1461 */       int i = t_pred_index;
/* 1462 */       if (toRight) {
/* 1463 */         while (i != t_succ_index) {
/* 1464 */           FeaturePoint featurePoint = t.getFeaturePoint((i + 1 + t_count) % t_count);
/* 1465 */           total_length += 
/* 1466 */             Math.sqrt(Math.pow((featurePoint1.getX() - featurePoint.getX()), 2.0D) + Math.pow((featurePoint1.getY() - featurePoint.getY()), 2.0D));
/* 1467 */           featurePoint1 = featurePoint;
/* 1468 */           i = (i + 1) % t_count;
/*      */         } 
/*      */       } else {
/*      */         
/* 1472 */         while (i != t_succ_index) {
/* 1473 */           FeaturePoint featurePoint = t.getFeaturePoint((i - 1 + t_count) % t_count);
/* 1474 */           total_length += 
/* 1475 */             Math.sqrt(Math.pow((featurePoint1.getX() - featurePoint.getX()), 2.0D) + Math.pow((featurePoint1.getY() - featurePoint.getY()), 2.0D));
/* 1476 */           featurePoint1 = featurePoint;
/* 1477 */           i = (i - 1 + t_count) % t_count;
/*      */         } 
/*      */       } 
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
/* 1501 */       double t_pred_MidToTotal = total_length * pred_MidToTotal;
/*      */       
/* 1503 */       double current_length = 0.0D;
/* 1504 */       double part_length = 0.0D;
/* 1505 */       int index = t_pred_index;
/* 1506 */       if (toRight) {
/* 1507 */         while (current_length < t_pred_MidToTotal) {
/* 1508 */           part_length = Math.sqrt(Math.pow((t.getFeaturePoint(index).getX() - 
/* 1509 */                 t.getFeaturePoint((index + 1) % t_count).getX()), 2.0D) + 
/* 1510 */               Math.pow((t.getFeaturePoint(index).getY() - 
/* 1511 */                 t.getFeaturePoint((index + 1) % t_count).getY()), 2.0D));
/* 1512 */           current_length += part_length;
/* 1513 */           index = (index + 1) % t_count;
/*      */         } 
/*      */       } else {
/*      */         
/* 1517 */         while (current_length < t_pred_MidToTotal) {
/* 1518 */           part_length = Math.sqrt(Math.pow((t.getFeaturePoint(index).getX() - 
/* 1519 */                 t.getFeaturePoint((index - 1 + t_count) % t_count).getX()), 2.0D) + 
/* 1520 */               Math.pow((t.getFeaturePoint(index).getY() - 
/* 1521 */                 t.getFeaturePoint((index - 1 + t_count) % t_count).getY()), 2.0D));
/* 1522 */           current_length += part_length;
/* 1523 */           index = (index - 1 + t_count) % t_count;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1533 */       current_length -= part_length;
/* 1534 */       double t_part_toMid = t_pred_MidToTotal - current_length;
/* 1535 */       double factor = t_part_toMid / part_length;
/* 1536 */       if (toRight) {
/* 1537 */         featurePoint3 = t.getFeaturePoint(index);
/* 1538 */         featurePoint2 = t.getFeaturePoint((index - 1 + t_count) % t_count);
/*      */       } else {
/*      */         
/* 1541 */         featurePoint3 = t.getFeaturePoint(index);
/* 1542 */         featurePoint2 = t.getFeaturePoint((index + 1) % t_count);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1548 */       int x_slope = featurePoint3.getX() - featurePoint2.getX();
/* 1549 */       int y_slope = featurePoint3.getY() - featurePoint2.getY();
/* 1550 */       int t_x = (int)(x_slope * pred_MidToTotal + 0.5D);
/* 1551 */       int t_y = (int)(y_slope * pred_MidToTotal + 0.5D);
/* 1552 */       t_x += featurePoint2.getX();
/* 1553 */       t_y += featurePoint2.getY();
/* 1554 */       newPoint = new FeaturePoint(t_x, t_y);
/* 1555 */       s1.addVertexBetween((Point)middle, (Point)pred, (Point)succ);
/* 1556 */       t1.addVertexBetween((Point)newPoint, (Point)t_pred, (Point)t_succ);
/* 1557 */       t.addVertexBetween((Point)newPoint, (Point)featurePoint2, (Point)featurePoint3);
/*      */     } 
/*      */     
/* 1560 */     int index_middle = s.getFeaturePointIndex(middle);
/* 1561 */     int index_newPoint = t.getFeaturePointIndex(newPoint);
/* 1562 */     s.getFeaturePoint(index_middle).setCorrespondence((Point)t.getFeaturePoint(index_newPoint));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static FeaturePoint findPredecessor(Polygon s, int index) {
/*      */     FeaturePoint predecessor;
/* 1574 */     boolean found_pred = false;
/*      */     do {
/* 1576 */       index--;
/* 1577 */       if (index < 0)
/* 1578 */         index += s.getCount(); 
/* 1579 */       predecessor = s.getFeaturePoint(index);
/* 1580 */       if (!predecessor.hasCorrespondence())
/* 1581 */         continue;  found_pred = true;
/* 1582 */     } while (!found_pred);
/* 1583 */     return predecessor;
/*      */   }
/*      */   
/*      */   private static FeaturePoint findSuccesor(Polygon s, int index) {
/*      */     FeaturePoint successor;
/* 1588 */     boolean found_succ = false;
/* 1589 */     int sCount = s.getCount();
/*      */     do {
/* 1591 */       index++;
/* 1592 */       if (index >= sCount)
/* 1593 */         index -= sCount; 
/* 1594 */       successor = s.getFeaturePoint(index);
/* 1595 */       if (!successor.hasCorrespondence())
/* 1596 */         continue;  found_succ = true;
/* 1597 */     } while (!found_succ);
/* 1598 */     return successor;
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
/*      */   private static boolean toRight(Polygon s, FeaturePoint pred, FeaturePoint succ, FeaturePoint pred_pred) {
/*      */     boolean toRight;
/* 1616 */     int pred_index = s.getFeaturePointIndex(pred);
/* 1617 */     int succ_index = s.getFeaturePointIndex(succ);
/* 1618 */     int pred_pred_index = s.getFeaturePointIndex(pred_pred);
/* 1619 */     if (pred_pred_index < pred_index) {
/* 1620 */       if (pred_index < succ_index) {
/* 1621 */         diff = succ_index - pred_index;
/* 1622 */         toRight = true;
/*      */       }
/* 1624 */       else if (pred_pred_index < succ_index) {
/* 1625 */         diff = pred_index - succ_index;
/* 1626 */         toRight = false;
/*      */       } else {
/*      */         
/* 1629 */         diff = succ_index + s.getCount() - pred_index;
/* 1630 */         toRight = true;
/*      */       }
/*      */     
/*      */     }
/* 1634 */     else if (succ_index < pred_index) {
/* 1635 */       diff = pred_index - succ_index;
/* 1636 */       toRight = false;
/*      */     }
/* 1638 */     else if (pred_pred_index < succ_index) {
/* 1639 */       diff = pred_index + s.getCount() - succ_index;
/* 1640 */       toRight = false;
/*      */     } else {
/*      */       
/* 1643 */       diff = succ_index - pred_index;
/* 1644 */       toRight = true;
/*      */     } 
/*      */ 
/*      */     
/* 1648 */     return toRight;
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
/*      */   public static double[][][][] calculateAllDeltaCosts(Polygon source, Polygon target, int skips) {
/* 1681 */     int dim1 = source.getFeaturePointCount();
/* 1682 */     int dim2 = target.getFeaturePointCount();
/*      */ 
/*      */     
/* 1685 */     double[][][][] delta_field = new double[dim1][dim2][skips + 1][skips + 1];
/* 1686 */     for (int i = 0; i < dim1; i++) {
/* 1687 */       for (int j = 0; j < dim2; j++) {
/* 1688 */         for (int k = 0; k <= skips; k++) {
/* 1689 */           int source_index = (i - k + dim1) % dim1;
/* 1690 */           for (int l = 0; l <= skips; l++) {
/* 1691 */             int target_index = (j - l + dim2) % dim2;
/* 1692 */             delta_field[i][j][k][l] = deltaCosts(source, target, i, source_index, j, target_index);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1699 */     return delta_field;
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
/*      */   public static double deltaCosts(Polygon source, Polygon target, int start_index_s, int end_index_s, int start_index_t, int end_index_t) {
/* 1721 */     double costs = 0.0D;
/* 1722 */     costs += disCosts(source, start_index_s, end_index_s);
/* 1723 */     costs += disCosts(target, start_index_t, end_index_t);
/* 1724 */     costs += FeaturePoint.calculate_Sim_Cost(source.getFeaturePoint(end_index_s), target.getFeaturePoint(end_index_t));
/* 1725 */     return costs;
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
/*      */   public static double disCosts(Polygon polygon, int start_index, int end_index) {
/*      */     int steps;
/* 1744 */     double disCosts = 0.0D;
/* 1745 */     int dim = polygon.getFeaturePointCount();
/* 1746 */     if (start_index == end_index) {
/* 1747 */       return disCosts;
/*      */     }
/*      */     
/* 1750 */     if (end_index > start_index) {
/* 1751 */       steps = start_index + dim - end_index;
/*      */     } else {
/* 1753 */       steps = start_index - end_index;
/* 1754 */     }  for (int i = 1; i < steps; i++) {
/* 1755 */       disCosts += polygon.getFeaturePoint((start_index - i + dim) % dim).getDisCost();
/*      */     }
/*      */     
/* 1758 */     return disCosts;
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
/*      */   public static Path calculatePath(Polygon source, Polygon target, int skips) {
/* 1771 */     double[][][][] deltaCosts = calculateAllDeltaCosts(source, target, skips);
/*      */ 
/*      */ 
/*      */     
/* 1775 */     int dim1 = source.getFeaturePointCount();
/* 1776 */     int dim2 = target.getFeaturePointCount();
/*      */     
/* 1778 */     double min_path_costs = Double.MAX_VALUE;
/* 1779 */     Path path = new Path();
/*      */     
/* 1781 */     Node[][] field = new Node[dim1 + 1][dim2 + 1];
/*      */ 
/*      */ 
/*      */     
/* 1785 */     Vector source_fps = source.getFeaturePoints();
/* 1786 */     Vector target_fps = target.getFeaturePoints();
/*      */     
/* 1788 */     for (int k = 0; k < dim1; k++) {
/* 1789 */       for (int l = 0; l < dim2; l++) {
/*      */ 
/*      */ 
/*      */         
/* 1793 */         field = initializeField(field, k, l, source_fps, target_fps);
/*      */         
/* 1795 */         field[0][0].setPathCosts(deltaCosts[k][l][0][0]);
/*      */         int i;
/* 1797 */         for (i = 1; i < dim1 + 1; i++) {
/* 1798 */           setPredecessor(field, i, 0, deltaCosts, k, l, skips);
/*      */         }
/*      */         
/* 1801 */         for (i = 1; i < dim2 + 1; i++) {
/* 1802 */           setPredecessor(field, 0, i, deltaCosts, k, l, skips);
/*      */         }
/*      */         
/* 1805 */         for (i = 1; i < dim1 + 1; i++) {
/* 1806 */           for (int j = 1; j < dim2 + 1; j++) {
/* 1807 */             setPredecessor(field, i, j, deltaCosts, k, l, skips);
/*      */           }
/*      */         } 
/* 1810 */         Node current_node = field[dim1][dim2];
/* 1811 */         if (current_node.getPathCosts() < min_path_costs) {
/* 1812 */           min_path_costs = current_node.getPathCosts();
/* 1813 */           path = new Path();
/* 1814 */           path.add(current_node);
/* 1815 */           path.setCosts(current_node.getPathCosts());
/* 1816 */           while (current_node.getPredecessor() != null) {
/* 1817 */             current_node = current_node.getPredecessor();
/* 1818 */             path.add(current_node);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1824 */     return path;
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
/*      */   public static Node setPredecessor(Node[][] field, int source_index, int target_index, double[][][][] deltaCosts, int skips) {
/* 1875 */     int source_boundary, target_boundary, dim1 = deltaCosts.length;
/* 1876 */     int dim2 = (deltaCosts[0]).length;
/* 1877 */     Node predecessor = null;
/*      */     
/* 1879 */     if (source_index < skips) {
/* 1880 */       source_boundary = source_index;
/*      */     } else {
/* 1882 */       source_boundary = skips;
/* 1883 */     }  if (target_index < skips) {
/* 1884 */       target_boundary = target_index;
/*      */     } else {
/* 1886 */       target_boundary = skips;
/* 1887 */     }  double min_costs = Double.MAX_VALUE;
/* 1888 */     double current_costs = 0.0D;
/*      */     int i;
/* 1890 */     for (i = 1; i <= source_boundary; i++) {
/* 1891 */       current_costs = field[source_index - i][target_index].getPathCosts();
/* 1892 */       current_costs += deltaCosts[source_index % dim1][target_index % dim2][i][0];
/* 1893 */       if (current_costs < min_costs) {
/* 1894 */         min_costs = current_costs;
/* 1895 */         predecessor = field[source_index - i][target_index];
/*      */       } 
/*      */     } 
/* 1898 */     for (i = 1; i <= target_boundary; i++) {
/* 1899 */       current_costs = field[source_index][target_index - i].getPathCosts();
/* 1900 */       current_costs += deltaCosts[source_index % dim1][target_index % dim2][0][i];
/* 1901 */       if (current_costs < min_costs) {
/* 1902 */         min_costs = current_costs;
/* 1903 */         predecessor = field[source_index][target_index - i];
/*      */       } 
/*      */     } 
/*      */     
/* 1907 */     for (i = 1; i <= source_boundary; i++) {
/* 1908 */       for (int j = 1; j <= target_boundary; j++) {
/* 1909 */         current_costs = field[source_index - i][target_index - j].getPathCosts();
/*      */         
/* 1911 */         current_costs += deltaCosts[source_index % dim1][target_index % dim2][i][j];
/* 1912 */         if (current_costs < min_costs) {
/* 1913 */           min_costs = current_costs;
/* 1914 */           predecessor = field[source_index - i][target_index - j];
/*      */         } 
/*      */       } 
/*      */     } 
/* 1918 */     field[source_index][target_index].setPredecessor(predecessor);
/* 1919 */     field[source_index][target_index].setPathCosts(min_costs);
/*      */     
/* 1921 */     return predecessor;
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
/*      */   public static Node setPredecessor(Node[][] field, int field_index_source, int field_index_target, double[][][][] deltaCosts, int first_node_source, int first_node_target, int skips) {
/* 1942 */     int source_boundary, target_boundary, dim1 = deltaCosts.length;
/* 1943 */     int dim2 = (deltaCosts[0]).length;
/* 1944 */     Node predecessor = null;
/*      */     
/* 1946 */     if (field_index_source < skips) {
/* 1947 */       source_boundary = field_index_source;
/*      */     } else {
/* 1949 */       source_boundary = skips;
/* 1950 */     }  if (field_index_target < skips) {
/* 1951 */       target_boundary = field_index_target;
/*      */     } else {
/* 1953 */       target_boundary = skips;
/* 1954 */     }  double min_costs = Double.MAX_VALUE;
/* 1955 */     double current_costs = 0.0D;
/*      */     int i;
/* 1957 */     for (i = 1; i <= source_boundary; i++) {
/* 1958 */       current_costs = field[field_index_source - i][field_index_target].getPathCosts();
/* 1959 */       current_costs += deltaCosts[(field_index_source + first_node_source) % dim1][(
/* 1960 */           field_index_target + first_node_target) % dim2][i][0];
/* 1961 */       if (current_costs < min_costs) {
/* 1962 */         min_costs = current_costs;
/* 1963 */         predecessor = field[field_index_source - i][field_index_target];
/*      */       } 
/*      */     } 
/* 1966 */     for (i = 1; i <= target_boundary; i++) {
/* 1967 */       current_costs = field[field_index_source][field_index_target - i].getPathCosts();
/* 1968 */       current_costs += deltaCosts[(field_index_source + first_node_source) % dim1][(
/* 1969 */           field_index_target + first_node_target) % dim2][0][i];
/* 1970 */       if (current_costs < min_costs) {
/* 1971 */         min_costs = current_costs;
/* 1972 */         predecessor = field[field_index_source][field_index_target - i];
/*      */       } 
/*      */     } 
/*      */     
/* 1976 */     for (i = 1; i <= source_boundary; i++) {
/* 1977 */       for (int j = 1; j <= target_boundary; j++) {
/* 1978 */         current_costs = field[field_index_source - i][field_index_target - j].getPathCosts();
/* 1979 */         current_costs += deltaCosts[(field_index_source + first_node_source) % dim1][(
/* 1980 */             field_index_target + first_node_target) % dim2][i][j];
/* 1981 */         if (current_costs < min_costs) {
/* 1982 */           min_costs = current_costs;
/* 1983 */           predecessor = field[field_index_source - i][field_index_target - j];
/*      */         } 
/*      */       } 
/*      */     } 
/* 1987 */     field[field_index_source][field_index_target].setPredecessor(predecessor);
/* 1988 */     field[field_index_source][field_index_target].setPathCosts(min_costs);
/*      */     
/* 1990 */     return predecessor;
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
/*      */   public static Node[][] initializeField(Node[][] field, int source_index, int target_index) {
/* 2004 */     int dim1 = field.length;
/* 2005 */     int dim2 = (field[0]).length;
/* 2006 */     int source_point_count = dim1 - 1;
/* 2007 */     int target_point_count = dim2 - 1;
/*      */     
/*      */     int i;
/*      */     
/* 2011 */     for (i = 0; i < dim1; i++) {
/* 2012 */       field[i][0] = new Node((source_index + i) % source_point_count, target_index);
/*      */     }
/*      */     
/* 2015 */     for (i = 1; i < dim2; i++) {
/* 2016 */       field[0][i] = new Node(source_index, (target_index + i) % target_point_count);
/*      */     }
/*      */     
/* 2019 */     for (i = 1; i < dim1; i++) {
/* 2020 */       for (int j = 0; j < dim2; j++) {
/* 2021 */         field[i][j] = new Node((source_index + i) % source_point_count, (
/* 2022 */             target_index + j) % target_point_count);
/*      */       }
/*      */     } 
/* 2025 */     return field;
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
/*      */   public static Node[][] initializeField(Node[][] field, int source_index, int target_index, Vector sourceFPs, Vector targetFPs) {
/* 2045 */     int dim1 = field.length;
/* 2046 */     int dim2 = (field[0]).length;
/* 2047 */     int source_point_count = dim1 - 1;
/* 2048 */     int target_point_count = dim2 - 1;
/*      */ 
/*      */ 
/*      */     
/* 2052 */     FeaturePoint tmp = targetFPs.elementAt(target_index); int i;
/* 2053 */     for (i = 0; i < dim1; i++) {
/* 2054 */       int source_curr_index = (source_index + i) % source_point_count;
/* 2055 */       field[i][0] = new Node(source_curr_index, target_index, 
/* 2056 */           sourceFPs.elementAt(source_curr_index), tmp);
/*      */     } 
/*      */     
/* 2059 */     tmp = sourceFPs.elementAt(source_index);
/* 2060 */     for (i = 1; i < dim2; i++) {
/* 2061 */       int target_curr_index = (target_index + i) % target_point_count;
/* 2062 */       field[0][i] = new Node(source_index, target_curr_index, 
/* 2063 */           tmp, targetFPs.elementAt(target_curr_index));
/*      */     } 
/*      */     
/* 2066 */     for (i = 1; i < dim1; i++) {
/* 2067 */       for (int j = 0; j < dim2; j++) {
/* 2068 */         int source_curr_index = (source_index + i) % source_point_count;
/* 2069 */         int target_curr_index = (target_index + j) % target_point_count;
/* 2070 */         field[i][j] = new Node(source_curr_index, target_curr_index, 
/* 2071 */             sourceFPs.elementAt(source_curr_index), 
/* 2072 */             targetFPs.elementAt(target_curr_index));
/*      */       } 
/*      */     } 
/* 2075 */     return field;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Polygon[] createCompleteMorph2(Path fPath, Path oPath, Polygon source, Polygon target) {
/* 2083 */     Polygon[] morphablePolygons = new Polygon[2];
/*      */     
/* 2085 */     Polygon s1 = new Polygon();
/* 2086 */     Polygon t1 = new Polygon();
/*      */     
/* 2088 */     for (int i = 0; i < fPath.getSize(); i++) {
/* 2089 */       Node node = fPath.getNodeAt(i);
/* 2090 */       FeaturePoint curr = node.getSourcePoint();
/* 2091 */       FeaturePoint corr = node.getTargetPoint();
/* 2092 */       curr.setCorrespondence((Point)corr);
/* 2093 */       s1.addVertex((Point)curr);
/* 2094 */       t1.addVertex((Point)corr);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2099 */     if (fPath.getSize() + 1 < oPath.getSize()) {
/* 2100 */       handleRemainingPoints(oPath, source, target, s1, t1, true);
/* 2101 */       handleRemainingPoints(oPath, target, source, t1, s1, false);
/*      */     } 
/*      */     
/* 2104 */     int fp_count = source.getFeaturePointCount();
/*      */     int j;
/* 2106 */     for (j = 0; j < fp_count; j++) {
/* 2107 */       FeaturePoint curr = source.getFeaturePoint(j);
/* 2108 */       if (!curr.hasCorrespondence())
/* 2109 */         createCorrespondence(source, target, s1, t1, (Point)curr); 
/*      */     } 
/* 2111 */     fp_count = target.getFeaturePointCount();
/* 2112 */     for (j = 0; j < fp_count; j++) {
/* 2113 */       FeaturePoint curr = target.getFeaturePoint(j);
/* 2114 */       if (!curr.hasCorrespondence()) {
/* 2115 */         createCorrespondence(target, source, t1, s1, (Point)curr);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2121 */     fp_count = source.getCount(); int k;
/* 2122 */     for (k = 0; k < fp_count; k++) {
/* 2123 */       Point curr_point = source.getVertex(k);
/* 2124 */       if (!curr_point.hasCorrespondence())
/* 2125 */         createCorrespondence(source, target, s1, t1, curr_point); 
/*      */     } 
/* 2127 */     fp_count = target.getCount();
/* 2128 */     for (k = 0; k < fp_count; k++) {
/* 2129 */       Point curr_point = target.getVertex(k);
/* 2130 */       if (!curr_point.hasCorrespondence())
/* 2131 */         createCorrespondence(target, source, t1, s1, curr_point); 
/*      */     } 
/* 2133 */     if (source.isClosed())
/* 2134 */       s1.close(); 
/* 2135 */     if (target.isClosed()) {
/* 2136 */       t1.close();
/*      */     }
/*      */ 
/*      */     
/* 2140 */     morphablePolygons[0] = s1;
/* 2141 */     morphablePolygons[1] = t1;
/* 2142 */     return morphablePolygons;
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
/*      */   private static void handleRemainingPoints(Path oPath, Polygon source, Polygon target, Polygon s1, Polygon t1, boolean s_points) {
/* 2157 */     for (int i = 0; i < oPath.getSize(); i++) {
/* 2158 */       FeaturePoint curr; Node node = oPath.getNodeAt(i);
/* 2159 */       if (s_points) {
/* 2160 */         curr = node.getSourcePoint();
/*      */       } else {
/* 2162 */         curr = node.getTargetPoint();
/* 2163 */       }  if (!curr.hasCorrespondence()) {
/* 2164 */         int t_pred_index, t_succ_index, point_count; boolean feature_points; FeaturePoint pred = getPredecessor(source, curr);
/* 2165 */         FeaturePoint succ = getSuccessor(source, curr);
/*      */ 
/*      */         
/* 2168 */         Point t_pred = pred.getCorrespondence();
/* 2169 */         Point t_succ = succ.getCorrespondence();
/* 2170 */         if (t_pred instanceof FeaturePoint && t_succ instanceof FeaturePoint) {
/* 2171 */           t_pred_index = target.getFeaturePointIndex(
/* 2172 */               (FeaturePoint)pred.getCorrespondence());
/*      */           
/* 2174 */           t_succ_index = target.getFeaturePointIndex((FeaturePoint)succ.getCorrespondence());
/* 2175 */           point_count = target.getFeaturePointCount();
/* 2176 */           feature_points = true;
/*      */         } else {
/*      */           
/* 2179 */           t_pred_index = target.getIndex(t_pred);
/* 2180 */           t_succ_index = target.getIndex(t_succ);
/* 2181 */           point_count = target.getCount();
/* 2182 */           feature_points = false;
/*      */         } 
/*      */         
/* 2185 */         int start = t_pred_index;
/* 2186 */         int diff = 0;
/* 2187 */         while (start != t_succ_index) {
/* 2188 */           start = (start + 1) % point_count;
/* 2189 */           diff++;
/*      */         } 
/* 2191 */         if (diff == 0) {
/* 2192 */           System.out.println("Predecessor and Successor have one common correspondence!");
/*      */         }
/* 2194 */         else if (diff == 2) {
/* 2195 */           Point corr; if (feature_points) {
/* 2196 */             FeaturePoint featurePoint = target.getFeaturePoint((t_pred_index + 1) % point_count);
/*      */           } else {
/*      */             
/* 2199 */             corr = target.getVertex((t_pred_index + 1) % point_count);
/* 2200 */           }  curr.setCorrespondence(corr);
/* 2201 */           s1.addVertexBefore((Point)curr, (Point)pred);
/* 2202 */           t1.addVertexBefore(corr, pred.getCorrespondence());
/*      */         }
/* 2204 */         else if (diff > 2) {
/*      */           FeaturePoint featurePoint;
/* 2206 */           if (!feature_points) {
/* 2207 */             Point bestmatch = target.getVertex((t_pred_index + 1) % point_count);
/*      */ 
/*      */             
/* 2210 */             double length1 = 0.0D;
/* 2211 */             double length2 = 0.0D;
/* 2212 */             FeaturePoint featurePoint1 = pred;
/* 2213 */             int s_count = source.getCount();
/* 2214 */             int start_index = source.getIndex((Point)pred);
/* 2215 */             int end_index = source.getIndex((Point)curr);
/* 2216 */             while (start_index != end_index) {
/* 2217 */               start_index = (start_index + 1) % s_count;
/* 2218 */               Point tmp2 = source.getVertex(start_index);
/* 2219 */               length1 += 
/* 2220 */                 Math.sqrt(Math.pow((featurePoint1.getX() - tmp2.getX()), 2.0D) + Math.pow((featurePoint1.getY() - tmp2.getY()), 2.0D));
/* 2221 */               point1 = tmp2;
/*      */             } 
/* 2223 */             end_index = source.getIndex((Point)succ);
/* 2224 */             while (start_index != end_index) {
/* 2225 */               start_index = (start_index + 1) % s_count;
/* 2226 */               Point tmp2 = source.getVertex(start_index);
/* 2227 */               length2 += 
/* 2228 */                 Math.sqrt(Math.pow((point1.getX() - tmp2.getX()), 2.0D) + Math.pow((point1.getY() - tmp2.getY()), 2.0D));
/* 2229 */               point1 = tmp2;
/*      */             } 
/*      */ 
/*      */             
/* 2233 */             double source_length = length1 + length2;
/* 2234 */             double pred_to_curr = length1 / source_length;
/* 2235 */             Point point1 = t_pred;
/* 2236 */             double target_length = 0.0D;
/* 2237 */             double[] segments = new double[diff];
/* 2238 */             int counter = 0;
/* 2239 */             while (!point1.equals(t_succ)) {
/* 2240 */               segments[counter] = 
/* 2241 */                 Math.sqrt(Math.pow((point1.getX() - bestmatch.getX()), 2.0D) + Math.pow((point1.getY() - bestmatch.getY()), 2.0D));
/* 2242 */               target_length += segments[counter];
/* 2243 */               counter++;
/* 2244 */               point1 = bestmatch;
/* 2245 */               bestmatch = target.getVertex((t_pred_index + 1 + counter) % point_count);
/*      */             } 
/* 2247 */             double t_pred_to_corr = target_length * pred_to_curr;
/* 2248 */             counter = 0;
/* 2249 */             double t_length = 0.0D;
/* 2250 */             while (t_length < t_pred_to_corr) {
/* 2251 */               t_length += segments[counter];
/* 2252 */               counter++;
/*      */             } 
/* 2254 */             if (counter == diff) {
/* 2255 */               bestmatch = target.getVertex((t_pred_index + diff - 1) % point_count);
/* 2256 */             } else if (counter == 1) {
/* 2257 */               bestmatch = target.getVertex((t_pred_index + 1) % point_count);
/*      */             } else {
/* 2259 */               counter--;
/* 2260 */               double dist1 = Math.abs(t_length - t_pred_to_corr);
/* 2261 */               double dist2 = Math.abs(t_length - t_pred_to_corr - segments[counter]);
/* 2262 */               if (dist1 < dist2) {
/* 2263 */                 bestmatch = target.getVertex((t_pred_index + counter + 1) % point_count);
/*      */               } else {
/*      */                 
/* 2266 */                 bestmatch = target.getVertex((t_pred_index + counter) % point_count);
/*      */               } 
/*      */             } 
/*      */           } else {
/*      */             
/* 2271 */             featurePoint = target.getFeaturePoint((t_pred_index + 1) % point_count);
/*      */             
/* 2273 */             double min_costs = FeaturePoint.calculate_Sim_Cost(curr, featurePoint);
/*      */             
/* 2275 */             for (int j = 2; j < diff; j++) {
/* 2276 */               FeaturePoint tmp = target.getFeaturePoint((t_pred_index + j) % point_count);
/* 2277 */               double costs = FeaturePoint.calculate_Sim_Cost(curr, tmp);
/* 2278 */               if (costs < min_costs) {
/* 2279 */                 featurePoint = tmp;
/* 2280 */                 min_costs = costs;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 2285 */           if (featurePoint.equals(new Point(434, 416)))
/* 2286 */             System.out.println("bla"); 
/* 2287 */           curr.setCorrespondence((Point)featurePoint);
/* 2288 */           s1.addVertexBefore((Point)curr, (Point)pred);
/* 2289 */           t1.addVertexBefore((Point)featurePoint, pred.getCorrespondence());
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 2295 */           int v_diff = 0;
/* 2296 */           int t_count = target.getCount();
/* 2297 */           int v_start = target.getIndex(pred.getCorrespondence());
/* 2298 */           start = v_start;
/* 2299 */           int v_end = target.getIndex(succ.getCorrespondence());
/*      */           
/* 2301 */           while (start != v_end) {
/* 2302 */             v_diff++;
/* 2303 */             start = (start + 1) % t_count;
/*      */           } 
/* 2305 */           if (v_diff == 2 && !target.getVertex((v_start + 1) % t_count).hasCorrespondence()) {
/*      */             
/* 2307 */             Point vertex = target.getVertex((v_start + 1) % t_count);
/* 2308 */             curr.setCorrespondence(vertex);
/* 2309 */             s1.addVertexBefore((Point)curr, (Point)pred);
/* 2310 */             t1.addVertexBefore(vertex, t_pred);
/*      */           }
/*      */           else {
/*      */             
/* 2314 */             createCorrespondingPoint(source, target, s1, t1, (Point)curr, (Point)pred, (Point)succ);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static FeaturePoint getPredecessor(Polygon polygon, FeaturePoint curr) {
/* 2323 */     Vector fps = polygon.getFeaturePoints();
/* 2324 */     int fp_count = polygon.getFeaturePointCount();
/* 2325 */     int index = polygon.getFeaturePointIndex(curr);
/* 2326 */     while (!((FeaturePoint)fps.elementAt(index)).hasCorrespondence()) {
/* 2327 */       index = (index - 1 + fp_count) % fp_count;
/*      */     }
/* 2329 */     return fps.elementAt(index);
/*      */   }
/*      */   
/*      */   private static FeaturePoint getSuccessor(Polygon polygon, FeaturePoint curr) {
/* 2333 */     Vector fps = polygon.getFeaturePoints();
/* 2334 */     int fp_count = polygon.getFeaturePointCount();
/* 2335 */     int index = polygon.getFeaturePointIndex(curr);
/* 2336 */     while (!((FeaturePoint)fps.elementAt(index)).hasCorrespondence()) {
/* 2337 */       index = (index + 1) % fp_count;
/*      */     }
/* 2339 */     return fps.elementAt(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCorrespondingPoint(Polygon source, Polygon target, Polygon s1, Polygon t1, Point curr, Point pred, Point succ) {
/* 2348 */     Point t_pred = pred.getCorrespondence();
/* 2349 */     Point t_succ = succ.getCorrespondence();
/* 2350 */     int diff = 0;
/*      */     
/* 2352 */     int x = curr.getX();
/* 2353 */     int y = curr.getY();
/* 2354 */     int t_pred_index = target.getIndex(t_pred);
/* 2355 */     int t_count = target.getCount();
/* 2356 */     int i = t_pred_index;
/* 2357 */     int j = target.getIndex(t_succ);
/* 2358 */     while (i != j) {
/* 2359 */       i = (i + 1) % t_count;
/* 2360 */       diff++;
/*      */     } 
/*      */     
/* 2363 */     int start_index = source.getIndex(pred);
/* 2364 */     int end_index = source.getIndex(curr);
/*      */     
/* 2366 */     Point start = pred;
/* 2367 */     int s_count = source.getCount();
/* 2368 */     double length_pred_to_curr = 0.0D;
/* 2369 */     double length_succ_to_curr = 0.0D;
/* 2370 */     while (start_index != end_index) {
/* 2371 */       start_index = (start_index + 1) % s_count;
/* 2372 */       Point end = source.getVertex(start_index);
/* 2373 */       length_pred_to_curr += 
/* 2374 */         Math.sqrt(Math.pow((start.getX() - end.getX()), 2.0D) + Math.pow((start.getY() - end.getY()), 2.0D));
/* 2375 */       start = end;
/*      */     } 
/* 2377 */     end_index = source.getIndex(succ);
/* 2378 */     while (start_index != end_index) {
/* 2379 */       start_index = (start_index + 1) % s_count;
/* 2380 */       Point end = source.getVertex(start_index);
/* 2381 */       length_succ_to_curr += 
/* 2382 */         Math.sqrt(Math.pow((start.getX() - end.getX()), 2.0D) + Math.pow((start.getY() - end.getY()), 2.0D));
/* 2383 */       start = end;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2388 */     double pred_curr_to_total = length_pred_to_curr / (length_pred_to_curr + length_succ_to_curr);
/* 2389 */     if (diff == 1) {
/* 2390 */       int x_slope = t_succ.getX() - t_pred.getX();
/* 2391 */       int y_slope = t_succ.getY() - t_pred.getY();
/* 2392 */       int t_x = (int)(x_slope * pred_curr_to_total + 0.5D);
/* 2393 */       int t_y = (int)(y_slope * pred_curr_to_total + 0.5D);
/* 2394 */       t_x += t_pred.getX();
/* 2395 */       t_y += t_pred.getY();
/* 2396 */       Point corr = new Point(t_x, t_y);
/* 2397 */       curr.setCorrespondence(corr);
/* 2398 */       s1.addVertexBefore(curr, pred);
/* 2399 */       t1.addVertexBefore(corr, t_pred);
/* 2400 */       target.addVertexBehind(corr, t_pred);
/*      */     } else {
/*      */       
/* 2403 */       double total_length = 0.0D;
/* 2404 */       i = t_pred_index;
/*      */       
/* 2406 */       Point segment_start = t_pred;
/* 2407 */       double[] segments = new double[diff];
/* 2408 */       int counter = 0;
/* 2409 */       while (i != j) {
/* 2410 */         i = (i + 1) % t_count;
/* 2411 */         Point point = target.getVertex(i);
/* 2412 */         segments[counter] = 
/* 2413 */           Math.sqrt(Math.pow((point.getX() - segment_start.getX()), 2.0D) + Math.pow((point.getY() - segment_start.getY()), 2.0D));
/* 2414 */         total_length += segments[counter];
/* 2415 */         counter++;
/* 2416 */         segment_start = point;
/*      */       } 
/* 2418 */       double t_pred_curr_to_total = total_length * pred_curr_to_total;
/*      */       
/* 2420 */       double current_length = 0.0D;
/* 2421 */       i = t_pred_index;
/* 2422 */       counter = 0;
/* 2423 */       while (current_length < t_pred_curr_to_total) {
/* 2424 */         current_length += segments[counter];
/* 2425 */         counter++;
/*      */       } 
/* 2427 */       counter--;
/* 2428 */       if (counter < 0) {
/* 2429 */         current_length = 0.0D;
/* 2430 */         counter = 0;
/*      */       } else {
/*      */         
/* 2433 */         current_length -= segments[counter];
/* 2434 */       }  double t_part = t_pred_curr_to_total - current_length;
/* 2435 */       t_part /= segments[counter];
/* 2436 */       segment_start = target.getVertex((t_pred_index + counter) % t_count);
/* 2437 */       Point segment_end = target.getVertex((t_pred_index + counter + 1) % t_count);
/* 2438 */       int x_slope = segment_end.getX() - segment_start.getX();
/* 2439 */       int y_slope = segment_end.getY() - segment_start.getY();
/* 2440 */       int t_x = (int)(x_slope * t_part + 0.5D);
/* 2441 */       int t_y = (int)(y_slope * t_part + 0.5D);
/* 2442 */       t_x += segment_start.getX();
/* 2443 */       t_y += segment_start.getY();
/* 2444 */       Point corr = new Point(t_x, t_y);
/*      */ 
/*      */       
/* 2447 */       curr.setCorrespondence(corr);
/* 2448 */       s1.addVertexBefore(curr, pred);
/* 2449 */       t1.addVertexBefore(corr, t_pred);
/* 2450 */       target.addVertexBehind(corr, segment_start);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static Point getPredecessor2(Polygon polygon, Point curr) {
/* 2456 */     Vector vertices = polygon.getAllVertices();
/* 2457 */     int v_count = polygon.getCount();
/* 2458 */     int index = polygon.getIndex(curr);
/* 2459 */     while (!((Point)vertices.elementAt(index)).hasCorrespondence()) {
/* 2460 */       index = (index - 1 + v_count) % v_count;
/*      */     }
/* 2462 */     return vertices.elementAt(index);
/*      */   }
/*      */   
/*      */   private static Point getSuccessor2(Polygon polygon, Point curr) {
/* 2466 */     Vector vertices = polygon.getAllVertices();
/* 2467 */     int v_count = polygon.getCount();
/* 2468 */     int index = polygon.getIndex(curr);
/* 2469 */     while (!((Point)vertices.elementAt(index)).hasCorrespondence()) {
/* 2470 */       index = (index + 1) % v_count;
/*      */     }
/* 2472 */     return vertices.elementAt(index);
/*      */   }
/*      */   
/*      */   private static void createCorrespondence(Polygon source, Polygon target, Polygon s1, Polygon t1, Point curr) {
/* 2476 */     Point pred = getPredecessor2(source, curr);
/* 2477 */     Point succ = getSuccessor2(source, curr);
/* 2478 */     createCorrespondingPoint(source, target, s1, t1, curr, pred, succ);
/*      */   }
/*      */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\morph\MorphCalculator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */