/*     */ package tools;
/*     */ 
/*     */ import shapes.FeaturePoint;
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
/*     */ public class Node
/*     */ {
/*     */   private int x;
/*     */   private int y;
/*     */   private double similarity_costs;
/*     */   private Node optimal_predecessor;
/*     */   private double path_costs;
/*     */   private FeaturePoint sourcePoint;
/*     */   private FeaturePoint targetPoint;
/*     */   
/*     */   public Node() {
/*  26 */     this.x = 0;
/*  27 */     this.y = 0;
/*  28 */     this.sourcePoint = null;
/*  29 */     this.targetPoint = null;
/*  30 */     this.similarity_costs = 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node(FeaturePoint sourcePoint, FeaturePoint targetPoint) {
/*  39 */     setSourcePoint(sourcePoint);
/*  40 */     setTargetPoint(targetPoint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node(FeaturePoint sourcePoint, FeaturePoint targetPoint, double simcosts) {
/*  51 */     this(sourcePoint, targetPoint);
/*  52 */     setSimCosts(simcosts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node(int x, int y) {
/*  61 */     setX(x);
/*  62 */     setY(y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node(int x, int y, double simcost) {
/*  73 */     this(x, y);
/*  74 */     setSimCosts(simcost);
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
/*     */   public Node(int x, int y, FeaturePoint sourcePoint, FeaturePoint targetPoint) {
/*  86 */     this(x, y);
/*  87 */     setSourcePoint(sourcePoint);
/*  88 */     setTargetPoint(targetPoint);
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
/*     */   public Node(int x, int y, FeaturePoint sourcePoint, FeaturePoint targetPoint, double simscosts) {
/* 102 */     this(x, y, sourcePoint, targetPoint);
/* 103 */     setSimCosts(simscosts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(int x) {
/* 111 */     if (x < 0) throw new IllegalArgumentException("Index x of node must be positive!"); 
/* 112 */     this.x = x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getX() {
/* 120 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(int y) {
/* 128 */     if (y < 0) throw new IllegalArgumentException("Index y of node must be positive!"); 
/* 129 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getY() {
/* 137 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourcePoint(FeaturePoint sourcePoint) {
/* 145 */     this.sourcePoint = sourcePoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FeaturePoint getSourcePoint() {
/* 154 */     return this.sourcePoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetPoint(FeaturePoint targetPoint) {
/* 163 */     this.targetPoint = targetPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FeaturePoint getTargetPoint() {
/* 172 */     return this.targetPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSimCosts(double simcost) {
/* 181 */     if (simcost < 0.0D) throw new IllegalArgumentException("Similarity costs are always greater or equal to 0.0!"); 
/* 182 */     this.similarity_costs = simcost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPredecessor(Node pred) {
/* 190 */     this.optimal_predecessor = pred;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getPredecessor() {
/* 198 */     return this.optimal_predecessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Node other) {
/* 208 */     return (this.x == other.getX() && this.y == other.getY());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equalsComplete(Node other) {
/* 218 */     return (equals(other) && this.similarity_costs == other.getSimCosts());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSimCosts() {
/* 226 */     return this.similarity_costs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathCosts(double path_costs) {
/* 234 */     this.path_costs = path_costs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getPathCosts() {
/* 242 */     return this.path_costs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 250 */     return new Node(this.x, this.y, this.similarity_costs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 258 */     return "Node(" + this.x + "," + this.y + "); SimCosts:" + this.similarity_costs + "; PathCosts:" + this.path_costs + "\n";
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\tools\Node.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */