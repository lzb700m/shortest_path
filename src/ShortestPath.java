/**
 * Shortest path algorithm implementation
 * 
 * @author Peng Li
 * @author Nan Zhang
 */
import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import pq.IndexedHeap;

public class ShortestPath {

	/**
	 * check if input graph type falls into the following categories: uniform
	 * positive edge weight, DAG, graph with only non-negative (or positive)
	 * weights and others.
	 * 
	 * @param g
	 *            : Graph - input graph
	 * @param zeroCycleSensitive
	 *            : boolean - indicator if zero edge weight graph should fall
	 *            into DEF.OTHER category
	 * @return : int - graph type indicator as defined in DEF.java
	 */
	public static int sp_categorizer(Graph g, boolean zeroCycleSensitive) {
		if (hasUniformEdgeWeight(g)) {
			return DEF.UNIFORM_WEIGHT;
		} else if (!hasCycle(g)) {
			return DEF.DAG;
		} else if (!hasNegEdgeWeight(g, zeroCycleSensitive)) {
			return DEF.NON_NEG_WEIGHT;
		} else {
			return DEF.OTHER;
		}
	}

	/**
	 * Breadth first search algorithm for shortest path
	 * 
	 * @param g
	 *            : Graph - an uniform positive edge weight graph
	 * @param s
	 *            : Vertex - source Vertex
	 */
	public static void sp_bfs(Graph g, Vertex s) {
		// find the uniform positive edge weight
		int edgeWeight = Integer.MIN_VALUE;
		boolean initialized = false;
		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				if (edgeWeight == Integer.MIN_VALUE) {
					edgeWeight = e.Weight;
					initialized = true;
				}
				if (initialized) {
					break;
				}
			}
			if (initialized) {
				break;
			}
		}

		initGraph(g);
		Queue<Vertex> visited = new LinkedList<Vertex>();
		visited.offer(s);
		s.distance = 0;
		s.seen = true;

		while (!visited.isEmpty()) {
			Vertex u = visited.poll();
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen) {
					v.parent = u;
					// update shortest path
					v.distance = u.distance + edgeWeight;
					v.seen = true;
					visited.offer(v);
				}
			}
		}
	}

	/**
	 * shortest path implementation for DAG
	 * 
	 * @param g
	 *            : Graph - a DAG
	 * @param s
	 *            : Vertex - source Vertex
	 * @param zeroCycleSensitive
	 *            : boolean - indicator if we perform relax operation if equal
	 *            shortest path is found
	 */
	public static void sp_dag(Graph g, Vertex s, boolean zeroCycleSensitive) {
		List<Vertex> topOrder = topSort(g);
		initGraph(g);
		s.distance = 0;

		// relax outgoing edges for vertices in the topological sort order
		// NOTE: vertices appear prior to source will not be reachable
		for (Vertex u : topOrder) {
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				relax(u, v, e, zeroCycleSensitive);
			}
		}
	}

	/**
	 * Dijkstra shortest path algorithm implementation
	 * 
	 * @param g
	 *            : Graph - a graph with only non-negative (or positive) weight
	 *            edge
	 * @param s
	 *            : Vertex - source Vertex
	 * @param zeroCycleSensitive
	 *            : boolean - indicator if we perform relax operation if equal
	 *            shortest path is found
	 */
	public static void sp_dijkstra(Graph g, Vertex s, boolean zeroCycleSensitive) {
		initGraph(g);
		s.distance = 0;
		Vertex comp = new Vertex(0);
		IndexedHeap<Vertex> pq = new IndexedHeap<Vertex>(g.numNodes, comp);
		for (Vertex v : g) {
			pq.insert(v);
		}

		// relax outgoing edges for the vertex with shortest distance from
		// source, using a indexed heap
		while (!pq.isEmpty()) {
			Vertex u = pq.deleteMin();
			u.seen = true;
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (!v.seen) {
					if (relax(u, v, e, zeroCycleSensitive)) {
						pq.decreaseKey(v);
					}
				}
			}
		}
	}

	/**
	 * Bellman-Ford shortest path algorithm
	 * 
	 * @param g
	 *            : Graph - a graph (possibly with non-positive or negative
	 *            cycle)
	 * @param s
	 *            : Vertex - source vertex
	 * @param zeroCycleSensitive
	 *            : boolean - indicator if we perform relax operation if equal
	 *            shortest path is found
	 * @return : boolean - true if no non-positive or negative cycle is found;
	 *         false otherwise
	 */
	public static boolean sp_bf(Graph g, Vertex s, boolean zeroCycleSensitive) {
		initGraph(g);
		s.distance = 0;
		s.seen = true;
		Queue<Vertex> queue = new LinkedList<Vertex>();
		queue.offer(s);

		while (!queue.isEmpty()) {
			Vertex u = queue.poll();
			u.seen = false;
			u.count++;
			if (u.count >= g.numNodes) {
				return false; // negative cycle
			}

			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				if (relax(u, v, e, zeroCycleSensitive)) {
					if (!v.seen) {
						queue.offer(v);
						v.seen = true;
					}
				}
			}
		}
		return true;
	}

	/**
	 * find one (anyone) non-positive cycle in a graph
	 * 
	 * @param g
	 *            : Graph - a graph contains at least one non-positive cycle
	 * @return : List<Edge> a non-positive cycle of the input graph
	 */
	public static List<Edge> findNonPosCycle(Graph g) {
		// find a vertex whose shortest path's been updated more than |V| times
		// a non-negative cycle will connect to this vertex
		Vertex start = null;
		for (Vertex u : g) {
			if (u.count >= g.numNodes) {
				start = u;
				break;
			}
		}

		// walk backwards from the start vertex using parent pointer, keep all
		// predecessor in a HashSet. If a vertex appears in the HashSet again,
		// then it is part of a non-negative cycle.
		Set<Vertex> preDecesor = new HashSet<Vertex>();
		preDecesor.add(start);
		Vertex current = start.parent;
		while (!preDecesor.contains(current)) {
			preDecesor.add(current);
			current = current.parent;
		}
		LinkedList<Edge> ret = new LinkedList<Edge>();
		start = current;
		// walk backwards from the vertex found in previous step, a non-positive
		// cycle will be found
		do {
			for (Edge e : current.revAdj) {
				if (e.otherEnd(current) == current.parent) {
					ret.addFirst(e);
					current = current.parent;
					break;
				}
			}
		} while (current != start);
		return ret;
	}

	/**
	 * count the number of shortest path for each vertex reachable from source
	 * 
	 * @param g
	 *            : Graph - a graph that has been process by shortest path
	 *            algorithm
	 * @return : Graph - a DAG that contains the same vertices as the original
	 *         graph, but only edges that is part of a shortest path in the
	 *         original graph
	 */
	public static Graph countSPPath(Graph g) {
		Graph d = createSPDAG(g);
		List<Vertex> topOrder = topSort(d);
		Vertex source = findSource(d);
		source.spCount = 1;

		// process the vertex in topological order to update the number of path
		// for each vertex reachable from source
		for (Vertex u : topOrder) {
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				v.spCount += u.spCount;
			}
		}
		return d;
	}

	/**
	 * check if a graph has uniform positive edge weight
	 * 
	 * @param g
	 *            : Graph - input graph
	 * @return : boolean - true if input graph has uniform positive edge weight,
	 *         false otherwise
	 */
	private static boolean hasUniformEdgeWeight(Graph g) {
		int uniformWeight = Integer.MIN_VALUE;
		boolean initialized = false;
		// find edge weight for any edge
		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				uniformWeight = (uniformWeight == Integer.MIN_VALUE) ? e.Weight
						: uniformWeight;
				if (uniformWeight != Integer.MIN_VALUE) {
					initialized = true;
					break;
				}
			}
			if (initialized) {
				break;
			}
		}

		if (uniformWeight < 0) {
			return false;
		}

		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				if (e.Weight != uniformWeight) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * check if a graph has cycle using topological sort
	 * 
	 * @param g
	 *            : Graph - input graph
	 * @return : boolean - true if input graph has cycle, false otherwise
	 */
	private static boolean hasCycle(Graph g) {
		return topSort(g) == null;
	}

	/**
	 * topological sort
	 * 
	 * @param g
	 *            : Graph - input graph
	 * @return : List<Vertex> - topological order of vertex, null if such order
	 *         does not exists
	 */
	private static List<Vertex> topSort(Graph g) {
		// array to simulate deletion of edges
		int[] unvisitedEdge = new int[g.numNodes + 1];
		Queue<Vertex> zeroDegreeVertices = new LinkedList<Vertex>();

		for (Vertex v : g) {
			int inDegree = v.revAdj.size();
			unvisitedEdge[v.name] = inDegree;
			if (inDegree == 0) {
				zeroDegreeVertices.offer(v);
			}
		}

		List<Vertex> ret = new LinkedList<Vertex>();
		while (!zeroDegreeVertices.isEmpty()) {
			Vertex u = zeroDegreeVertices.poll();
			ret.add(u);
			for (Edge e : u.Adj) {
				Vertex v = e.otherEnd(u);
				unvisitedEdge[v.name]--;
				if (unvisitedEdge[v.name] == 0) {
					zeroDegreeVertices.offer(v);
				}
			}
		}

		for (int i = 1; i < unvisitedEdge.length; i++) {
			if (unvisitedEdge[i] > 0) {
				return null;
			}
		}
		return ret;
	}

	/**
	 * check if a graph has non-positive (or negative) edges
	 * 
	 * @param g
	 *            : Graph - input graph
	 * @param zeroCycleSensitive
	 *            : boolean - indicator if zero weight edge needs to be checked
	 * @return : boolean - true if input graph has non-positive (or negative)
	 *         edges, false otherwise
	 */
	private static boolean hasNegEdgeWeight(Graph g, boolean zeroCycleSensitive) {
		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				if (e.Weight < 0 || (zeroCycleSensitive && e.Weight == 0)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * helper method to initiate a graph
	 * 
	 * @param g
	 *            : Graph - input graph
	 */
	private static void initGraph(Graph g) {
		for (Vertex v : g) {
			v.distance = Integer.MAX_VALUE;
			v.parent = null;
			v.seen = false;
			v.count = 0;
			for (Edge e : v.Adj) {
				e.seen = false;
			}
		}
	}

	/**
	 * helper method for relaxing edges in shortest path algorithm
	 * 
	 * @param u
	 *            : Vertex - from vertex
	 * @param v
	 *            : Vertex - to vertex
	 * @param e
	 *            : Edge - edge needs to be relaxed
	 * @param zeroCycleSensitive
	 *            : boolean - indicator if equal length needs to be relaxed
	 * @return : boolean - true if edge is relaxed, false otherwise
	 */
	private static boolean relax(Vertex u, Vertex v, Edge e,
			boolean zeroCycleSensitive) {
		if (u.distance != Integer.MAX_VALUE) {
			int temp = u.distance + e.Weight;
			if (v.distance > temp || (zeroCycleSensitive && v.distance == temp)) {
				v.distance = temp;
				v.parent = u;
				return true;
			}
		}
		return false;
	}

	/**
	 * create a deep copy of original graph with all its vertices, but only
	 * edges that is part of a shortest path in the original graph
	 * 
	 * @param g
	 *            : Graph - a graph that has been process by shortest path
	 *            algorithm
	 * @return : Graph - a DAG that contains the same vertices as the original
	 *         graph, but only edges that is part of a shortest path in the
	 *         original graph
	 */
	private static Graph createSPDAG(Graph g) {
		Graph spDag = new Graph(g.numNodes);
		for (Vertex u : g) {
			// deep copy
			Vertex spDagU = spDag.verts.get(u.name);
			spDagU.distance = u.distance;

			if (u.distance != Integer.MAX_VALUE) {
				for (Edge e : u.Adj) {
					Vertex v = e.otherEnd(u);
					Vertex spDagV = spDag.verts.get(v.name);
					if (v.distance == u.distance + e.Weight) {
						spDag.addDirectedEdge(spDagU.name, spDagV.name,
								e.Weight);
					}
				}
			}
		}
		return spDag;
	}

	/**
	 * helper method to find the source as defined in DEF.java
	 * 
	 * @param g
	 *            : Graph - input graph
	 * @return : Vertex - the source vertex as defined in DEF.java
	 */
	static Vertex findSource(Graph g) {
		Vertex ret = null;
		for (Vertex v : g) {
			if (v.name == DEF.SOURCE) {
				ret = v;
			}
		}
		return ret;
	}
}