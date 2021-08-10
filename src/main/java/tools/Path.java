/*     */ package tools;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
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
/*     */ public class Path
/*     */ {
/*     */   private Vector nodes;
/*     */   private int size;
/*     */   private double costs;
/*     */   private int min_x;
/*     */   private int max_x;
/*     */   private int min_y;
/*     */   private int max_y;
/*     */   
/*     */   public Path() {
/*  29 */     this.nodes = new Vector();
/*  30 */     this.size = 0;
/*  31 */     this.costs = 0.0D;
/*  32 */     this.min_x = this.min_y = Integer.MAX_VALUE;
/*  33 */     this.max_x = this.max_y = Integer.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path(Path original) {
/*  43 */     this.nodes = new Vector();
/*  44 */     Enumeration e = original.getNodes();
/*  45 */     while (e.hasMoreElements()) {
/*  46 */       add(e.nextElement());
/*     */     }
/*  48 */     this.min_x = original.min_x;
/*  49 */     this.max_x = original.max_x;
/*  50 */     this.min_y = original.min_y;
/*  51 */     this.max_y = original.max_y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  59 */     this.nodes.clear();
/*  60 */     this.min_x = this.min_y = Integer.MAX_VALUE;
/*  61 */     this.max_x = this.max_y = Integer.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(Node node) {
/*  70 */     if (this.nodes.add(node)) {
/*  71 */       this.size++;
/*  72 */       int x = node.getX();
/*  73 */       int y = node.getY();
/*  74 */       if (x > this.max_x)
/*  75 */         this.max_x = x; 
/*  76 */       if (x < this.min_x)
/*  77 */         this.min_x = x; 
/*  78 */       if (y > this.max_y)
/*  79 */         this.max_y = y; 
/*  80 */       if (y < this.min_y)
/*  81 */         this.min_y = y; 
/*  82 */       return true;
/*     */     } 
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCosts(double costs) {
/*  93 */     if (costs < 0.0D) throw new IllegalArgumentException("no negative costs allowed in this algorithm!");
/*     */     
/*  95 */     this.costs = costs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getCosts() {
/* 104 */     return this.costs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[][] toArray() {
/* 113 */     int[][] return_Array = new int[this.size][2];
/*     */     
/* 115 */     for (int i = 0; i < this.size; i++) {
/* 116 */       Node temp = this.nodes.elementAt(i);
/* 117 */       return_Array[i][0] = temp.getX();
/* 118 */       return_Array[i][1] = temp.getY();
/*     */     } 
/* 120 */     return return_Array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSimCosts(double[][] costs) {
/* 129 */     for (int i = 0; i < this.size; i++) {
/* 130 */       Node temp = this.nodes.elementAt(i);
/* 131 */       temp.setSimCosts(costs[temp.getX()][temp.getY()]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getNodes() {
/* 140 */     return this.nodes.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 149 */     if (((Node)this.nodes.firstElement()).equals(this.nodes.lastElement())) {
/* 150 */       return true;
/*     */     }
/* 152 */     return false;
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
/*     */   public Path getFinalPath() {
/* 164 */     System.out.println("called method getFinalPath");
/* 165 */     Path finalPath = new Path();
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
/*     */     
/* 187 */     Vector temp = (Vector)this.nodes.clone();
/*     */ 
/*     */ 
/*     */     
/* 191 */     int space = this.max_x - this.min_x + 1;
/* 192 */     Vector[] bucket = new Vector[space]; int i;
/* 193 */     for (i = 0; i < space; i++) {
/* 194 */       bucket[i] = new Vector();
/*     */     }
/* 196 */     for (i = 0; i < this.size; i++) {
/* 197 */       Node node = temp.elementAt(i);
/* 198 */       bucket[node.getX() - this.min_x].add(node);
/*     */     } 
/*     */     
/* 201 */     for (i = 0; i < space; i++) {
/*     */       
/* 203 */       Node[] bla = new Node[bucket[i].size()]; int j;
/* 204 */       for (j = 0; j < bla.length; j++) {
/* 205 */         bla[j] = bucket[i].elementAt(j);
/*     */       }
/* 207 */       if (bla.length > 1) {
/* 208 */         double min = bla[0].getSimCosts();
/* 209 */         int pos = 0;
/* 210 */         for (j = 1; j < bla.length; j++) {
/* 211 */           if (bla[j].getSimCosts() < min) {
/* 212 */             pos = j;
/* 213 */             min = bla[j].getSimCosts();
/*     */           } 
/*     */         } 
/* 216 */         Node node = bla[pos];
/* 217 */         bla[pos] = bla[0];
/* 218 */         bla[0] = node;
/* 219 */         for (j = 1; j < bla.length; j++) {
/* 220 */           temp.removeElement(bla[j]);
/*     */         }
/*     */       } 
/*     */     } 
/* 224 */     space = this.max_y - this.min_y + 1;
/* 225 */     bucket = new Vector[space];
/* 226 */     for (i = 0; i < space; i++) {
/* 227 */       bucket[i] = new Vector();
/*     */     }
/* 229 */     for (i = 0; i < temp.size(); i++) {
/* 230 */       Node node = temp.elementAt(i);
/* 231 */       bucket[node.getY() - this.min_y].add(node);
/*     */     } 
/* 233 */     for (i = 0; i < space; i++) {
/* 234 */       Node[] bla = new Node[bucket[i].size()]; int j;
/* 235 */       for (j = 0; j < bla.length; j++) {
/* 236 */         bla[j] = bucket[i].elementAt(j);
/*     */       }
/* 238 */       if (bla.length > 1) {
/* 239 */         double min = bla[0].getSimCosts();
/* 240 */         int pos = 0;
/* 241 */         for (j = 1; j < bla.length; j++) {
/* 242 */           if (bla[j].getSimCosts() < min) {
/* 243 */             pos = j;
/* 244 */             min = bla[j].getSimCosts();
/*     */           } 
/*     */         } 
/* 247 */         Node node = bla[pos];
/* 248 */         bla[pos] = bla[0];
/* 249 */         bla[0] = node;
/* 250 */         for (j = 1; j < bla.length; j++) {
/* 251 */           temp.removeElement(bla[j]);
/*     */         }
/*     */       } 
/*     */     } 
/* 255 */     for (i = 0; i < temp.size(); i++) {
/* 256 */       finalPath.add(temp.elementAt(i));
/*     */     }
/* 258 */     System.out.println("Method getFinalPath successful!");
/* 259 */     return finalPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNodeAt(int i) {
/* 268 */     return this.nodes.elementAt(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 276 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 284 */     StringBuffer buff = new StringBuffer();
/* 285 */     buff.append("Path, Number of Nodes: " + this.size + "; Total path costs: " + this.costs + "\n");
/* 286 */     for (int i = 0; i < this.size; i++) {
/* 287 */       buff.append(((Node)this.nodes.elementAt(i)).toString());
/*     */     }
/* 289 */     return buff.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\micyc\Downloads\app.jar!\tools\Path.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */