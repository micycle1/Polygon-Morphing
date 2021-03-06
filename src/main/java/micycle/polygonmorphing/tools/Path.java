package micycle.polygonmorphing.tools;

import java.util.Enumeration;
import java.util.Vector;

public class Path {
	private Vector nodes = new Vector();
	private int size;
	private double costs;
	private int min_x;
	private int max_x;
	private int min_y;
	private int max_y;

	public Path() {
		this.size = 0;
		this.costs = 0.0;
		this.min_y = Integer.MAX_VALUE;
		this.min_x = Integer.MAX_VALUE;
		this.max_y = Integer.MIN_VALUE;
		this.max_x = Integer.MIN_VALUE;
	}

	public Path(Path original) {
		Enumeration e = original.getNodes();
		while (e.hasMoreElements()) {
			this.add((Node) e.nextElement());
		}
		this.min_x = original.min_x;
		this.max_x = original.max_x;
		this.min_y = original.min_y;
		this.max_y = original.max_y;
	}

	public void clear() {
		this.nodes.clear();
		this.min_y = Integer.MAX_VALUE;
		this.min_x = Integer.MAX_VALUE;
		this.max_y = Integer.MIN_VALUE;
		this.max_x = Integer.MIN_VALUE;
	}

	public boolean add(Node node) {
		if (this.nodes.add(node)) {
			++this.size;
			int x = node.getX();
			int y = node.getY();
			if (x > this.max_x) {
				this.max_x = x;
			}
			if (x < this.min_x) {
				this.min_x = x;
			}
			if (y > this.max_y) {
				this.max_y = y;
			}
			if (y < this.min_y) {
				this.min_y = y;
			}
			return true;
		}
		return false;
	}

	public void setCosts(double costs) {
		if (costs < 0.0) {
			throw new IllegalArgumentException("no negative costs allowed in this algorithm!");
		}
		this.costs = costs;
	}

	public double getCosts() {
		return this.costs;
	}

	public int[][] toArray() {
		int[][] return_Array = new int[this.size][2];
		for (int i = 0; i < this.size; ++i) {
			Node temp = (Node) this.nodes.elementAt(i);
			return_Array[i][0] = temp.getX();
			return_Array[i][1] = temp.getY();
		}
		return return_Array;
	}

	public void setSimCosts(double[][] costs) {
		for (int i = 0; i < this.size; ++i) {
			Node temp = (Node) this.nodes.elementAt(i);
			temp.setSimCosts(costs[temp.getX()][temp.getY()]);
		}
	}

	public Enumeration getNodes() {
		return this.nodes.elements();
	}

	public boolean isClosed() {
		return ((Node) this.nodes.firstElement()).equals((Node) this.nodes.lastElement());
	}

	public Path getFinalPath() {
		int pos;
		double min;
		int j;
		Node[] bla;
		Node node;
		int i;
		System.out.println("called method getFinalPath");
		Path finalPath = new Path();
		Vector temp = (Vector) this.nodes.clone();
		int space = this.max_x - this.min_x + 1;
		Vector[] bucket = new Vector[space];
		for (i = 0; i < space; ++i) {
			bucket[i] = new Vector();
		}
		for (i = 0; i < this.size; ++i) {
			node = (Node) temp.elementAt(i);
			bucket[node.getX() - this.min_x].add(node);
		}
		for (i = 0; i < space; ++i) {
			bla = new Node[bucket[i].size()];
			for (j = 0; j < bla.length; ++j) {
				bla[j] = (Node) bucket[i].elementAt(j);
			}
			if (bla.length <= 1) {
				continue;
			}
			min = bla[0].getSimCosts();
			pos = 0;
			for (j = 1; j < bla.length; ++j) {
				if (!(bla[j].getSimCosts() < min)) {
					continue;
				}
				pos = j;
				min = bla[j].getSimCosts();
			}
			node = bla[pos];
			bla[pos] = bla[0];
			bla[0] = node;
			for (j = 1; j < bla.length; ++j) {
				temp.removeElement(bla[j]);
			}
		}
		space = this.max_y - this.min_y + 1;
		bucket = new Vector[space];
		for (i = 0; i < space; ++i) {
			bucket[i] = new Vector();
		}
		for (i = 0; i < temp.size(); ++i) {
			node = (Node) temp.elementAt(i);
			bucket[node.getY() - this.min_y].add(node);
		}
		for (i = 0; i < space; ++i) {
			bla = new Node[bucket[i].size()];
			for (j = 0; j < bla.length; ++j) {
				bla[j] = (Node) bucket[i].elementAt(j);
			}
			if (bla.length <= 1) {
				continue;
			}
			min = bla[0].getSimCosts();
			pos = 0;
			for (j = 1; j < bla.length; ++j) {
				if (!(bla[j].getSimCosts() < min)) {
					continue;
				}
				pos = j;
				min = bla[j].getSimCosts();
			}
			node = bla[pos];
			bla[pos] = bla[0];
			bla[0] = node;
			for (j = 1; j < bla.length; ++j) {
				temp.removeElement(bla[j]);
			}
		}
		for (i = 0; i < temp.size(); ++i) {
			finalPath.add((Node) temp.elementAt(i));
		}
		System.out.println("Method getFinalPath successful!");
		return finalPath;
	}

	public Node getNodeAt(int i) {
		return (Node) this.nodes.elementAt(i);
	}

	public int getSize() {
		return this.size;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("Path, Number of Nodes: " + this.size + "; Total path costs: " + this.costs + "\n");
		for (int i = 0; i < this.size; ++i) {
			buff.append(((Node) this.nodes.elementAt(i)).toString());
		}
		return buff.toString();
	}
}
