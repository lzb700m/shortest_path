package graph;

/**
 * Class that represents an arc in a Graph
 * 
 * @author Peng Li
 * @author Nan Zhang
 */
public class Edge {
	public Vertex From; // head vertex
	public Vertex To; // tail vertex
	public int Weight;// weight of the arc
	public boolean seen; // indicator if been visited
	public boolean isPartOfSP; // indicator if this edge is part of any shortest
								// path

	/**
	 * Constructor for Edge
	 * 
	 * @param u
	 *            : Vertex - The head of the arc
	 * @param v
	 *            : Vertex - The tail of the arc
	 * @param w
	 *            : int - The weight associated with the arc
	 */
	Edge(Vertex u, Vertex v, int w) {
		From = u;
		To = v;
		Weight = w;
	}

	/**
	 * Method to find the other end end of the arc given a vertex reference
	 * 
	 * @param u
	 *            : Vertex
	 * @return
	 */
	public Vertex otherEnd(Vertex u) {
		// if the vertex u is the head of the arc, then return the tail else
		// return the head
		if (From == u) {
			return To;
		} else {
			return From;
		}
	}

	/**
	 * Check if the edge contains vertex as its start or end node
	 * 
	 * @param u
	 * @return
	 */
	public boolean containsVertex(Vertex u) {
		if (From == u || To == u) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method to represent the edge in the form (x,y) where x is the head of the
	 * arc and y is the tail of the arc
	 */
	public String toString() {
		return "(" + From + "," + To + ")";
	}
}
