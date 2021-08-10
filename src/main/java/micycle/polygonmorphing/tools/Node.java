package micycle.polygonmorphing.tools;

import micycle.polygonmorphing.shapes.FeaturePoint;

public class Node {
	private int x;
	private int y;
	private double similarity_costs;
	private Node optimal_predecessor;
	private double path_costs;
	private FeaturePoint sourcePoint;
	private FeaturePoint targetPoint;

	public Node() {
		this.x = 0;
		this.y = 0;
		this.sourcePoint = null;
		this.targetPoint = null;
		this.similarity_costs = 0.0;
	}

	public Node(FeaturePoint sourcePoint, FeaturePoint targetPoint) {
		this.setSourcePoint(sourcePoint);
		this.setTargetPoint(targetPoint);
	}

	public Node(FeaturePoint sourcePoint, FeaturePoint targetPoint, double simcosts) {
		this(sourcePoint, targetPoint);
		this.setSimCosts(simcosts);
	}

	public Node(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public Node(int x, int y, double simcost) {
		this(x, y);
		this.setSimCosts(simcost);
	}

	public Node(int x, int y, FeaturePoint sourcePoint, FeaturePoint targetPoint) {
		this(x, y);
		this.setSourcePoint(sourcePoint);
		this.setTargetPoint(targetPoint);
	}

	public Node(int x, int y, FeaturePoint sourcePoint, FeaturePoint targetPoint, double simscosts) {
		this(x, y, sourcePoint, targetPoint);
		this.setSimCosts(simscosts);
	}

	public void setX(int x) {
		if (x < 0) {
			throw new IllegalArgumentException("Index x of node must be positive!");
		}
		this.x = x;
	}

	public int getX() {
		return this.x;
	}

	public void setY(int y) {
		if (y < 0) {
			throw new IllegalArgumentException("Index y of node must be positive!");
		}
		this.y = y;
	}

	public int getY() {
		return this.y;
	}

	public void setSourcePoint(FeaturePoint sourcePoint) {
		this.sourcePoint = sourcePoint;
	}

	public FeaturePoint getSourcePoint() {
		return this.sourcePoint;
	}

	public void setTargetPoint(FeaturePoint targetPoint) {
		this.targetPoint = targetPoint;
	}

	public FeaturePoint getTargetPoint() {
		return this.targetPoint;
	}

	public void setSimCosts(double simcost) {
		if (simcost < 0.0) {
			throw new IllegalArgumentException("Similarity costs are always greater or equal to 0.0!");
		}
		this.similarity_costs = simcost;
	}

	public void setPredecessor(Node pred) {
		this.optimal_predecessor = pred;
	}

	public Node getPredecessor() {
		return this.optimal_predecessor;
	}

	public boolean equals(Node other) {
		return this.x == other.getX() && this.y == other.getY();
	}

	public boolean equalsComplete(Node other) {
		return this.equals(other) && this.similarity_costs == other.getSimCosts();
	}

	public double getSimCosts() {
		return this.similarity_costs;
	}

	public void setPathCosts(double path_costs) {
		this.path_costs = path_costs;
	}

	public double getPathCosts() {
		return this.path_costs;
	}

	@Override
	public Object clone() {
		return new Node(this.x, this.y, this.similarity_costs);
	}

	@Override
	public String toString() {
		return "Node(" + this.x + "," + this.y + "); SimCosts:" + this.similarity_costs + "; PathCosts:" + this.path_costs + "\n";
	}
}
